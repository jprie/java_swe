package at.wifiwien.javaswe.strawberry_fields.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import at.wifiwien.javaswe.strawberry_fields.application.Constants;
import at.wifiwien.javaswe.strawberry_fields.exception.MoveException;
import at.wifiwien.javaswe.strawberry_fields.model.game.Field;
import at.wifiwien.javaswe.strawberry_fields.model.game.Game;
import at.wifiwien.javaswe.strawberry_fields.model.game.Move;
import at.wifiwien.javaswe.strawberry_fields.model.io.OutputHandler;
import at.wifiwien.javaswe.strawberry_fields.model.item.Item;
import at.wifiwien.javaswe.strawberry_fields.model.item.Strawberry;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

public class BoardController extends BaseController {

	Game game = Game.getInstance();

	int numStrawberries = 0;
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TilePane tilePane;

	private int cnt;

	@FXML
	void initialize() {
		assert tilePane != null : "fx:id=\"tilePane\" was not injected: check your FXML file 'Board.fxml'.";

		tilePane.getStyleClass().add("tile-pane");
		tilePane.setOnKeyPressed(this::handleKeyPressed);

		// calls update in ListChange
		fields = FXCollections.observableArrayList((p) -> new Observable[] { p.itemProperty() });
		fields.addListener(this::handleFieldsUpdated);

		// bidirectional binding to model information
		Bindings.bindContentBidirectional(fields, game.getBoard().getFields());

		if (game.isGameInitialized() == true) {
			// initBoard();
			// bidirectional binding to model information

			generateFields();
		} else {
			// register listener and init board later
			game.gameInitializedProperty().addListener((obs, ov, nv) -> initBoard());

		}
	}

	public void handleFieldsUpdated(Change<? extends Field> c) {

		while (c.next()) {
			if (c.wasUpdated()) {

				System.out.println("updated: " + c.getFrom() + "-" + c.getTo());
				// only one field is updated at a time
				updateAtBoard(c.getFrom());
			}
		}

	}

	/**
	 * Initialize board in a separate Thread/Task
	 */
	void initBoard() {

		Task<Integer> task = new Task<Integer>() {

			@Override
			protected Integer call() throws Exception {

				generateFields();
				return null;
			}
		};

		Thread t = new Thread(task);
		t.start();

	}

	/**
	 * Idea: only one row can be changed at a time, update row that was altered
	 * depending on index
	 */
	private void updateAtBoard(int index) {

		Optional<Item> item = fields.get(index).getItem();

		StackPane sp = (StackPane) tilePane.getChildren().get(index);

		// item at field from before ("ergo Strawberry")
		if (!sp.getChildren().isEmpty()) {
			sp.getChildren().remove(0);
		}

		// new item at field ("ergo piece")
		if (item.isPresent()) {
			ImageView imageView = new ImageView(
					new Image(getClass().getResourceAsStream(getImageURLForItem(item.get()))));
			imageView.setPreserveRatio(true);
			imageView.setFitHeight(60);
			((StackPane) tilePane.getChildren().get(index)).getChildren().add(imageView);
		}

	}

	private void generateFields() {

		int numColumns = game.getBoard().getWidth();
		int numRows = game.getBoard().getHeight();
//		OutputHandler.printDebug("before loop: " + numColumns + ":" + numRows);

		// init board

		List<StackPane> stacks = new ArrayList<StackPane>();
		int numItems = 0;
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {

				Optional<Item> item = fields.get(i * numColumns + j).getItem();

				StackPane stackPane = new StackPane();
				stackPane.getStyleClass().add("stack-pane");
				if (item.isPresent()) {
					System.out.println(item.get().toString());
					ImageView imageView = imageViewForItem(item.get());

					stackPane.getChildren().add(imageView);
					numItems++;

				}

				stacks.add(stackPane);

			}
		}

		// check that all items were drawn
		assert (numItems - 2 == game.getStrawberriesLeft());

		addStacksToTilePane(numColumns, numRows, stacks);
	}

	private void addStacksToTilePane(int numColumns, int numRows, List<StackPane> stacks) {
		Platform.runLater(() -> {
			tilePane.setPrefColumns(numColumns);
			tilePane.getChildren().addAll(stacks);
			tilePane.setPrefTileWidth(80);
			tilePane.setPrefTileHeight(80);
			// set focus for getting key events
			tilePane.requestFocus();

			// resize to fit tilePane
			tilePane.getScene().getWindow().setWidth(numColumns * 80 + 20 + 10);
			tilePane.getScene().getWindow().setHeight(numRows * 80 + 40 + 60 + 10);

			OutputHandler.printDebug(game.getBoard().getFields().toString());
		});
	}

	private ImageView imageViewForItem(Item item) {

		if (item instanceof Strawberry) {
			System.out.println(++numStrawberries);
		}

		ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(getImageURLForItem(item))));
		imageView.setPreserveRatio(true);
		imageView.setFitHeight(60);
		return imageView;
	}

	private String getImageURLForItem(Item item) {
		String url;

		switch (item.getType()) {
		case PIECE:

			url = item == game.getPlayer1().getPiece() ? Constants.PATH_TO_PLAYER_MALE_IMAGE
					: Constants.PATH_TO_PLAYER_FEMALE_IMAGE;
			break;
		case STRAWBERRY:

			url = Constants.PATH_TO_STRAWBERRY_IMAGE;
			break;
		default:
			url = "";
		}

		System.out.println(url);
		return url;

	}

	@FXML
	private void handleKeyPressed(KeyEvent event) {
		System.out.println("Scene");
		try {
			switch (event.getCode()) {
			case UP:
				game.move(new Move(1, Move.Direction.UP));
				break;
			case DOWN:
				game.move(new Move(1, Move.Direction.DOWN));
				break;
			case LEFT:
				game.move(new Move(1, Move.Direction.LEFT));
				break;
			case RIGHT:
				game.move(new Move(1, Move.Direction.RIGHT));
				break;
			default:
				System.out.println(event.getCode());

			}
		} catch (MoveException e) {
			OutputHandler.printDebug("MoveException: " + e.getMessage());

			animateRedBorder();
		}
	}

	private void animateRedBorder() {
		// set false, so tilePane doesn't redistribute the columns while being shrunk by
		// the border
		tilePane.setManaged(false);

		int mills[] = { -20 };
		KeyFrame keyFrames[] = Stream.iterate(0, i -> i + 1).limit(5) // 5 steps
				.map(i -> new Border(new BorderStroke(Color.ORANGERED, BorderStrokeStyle.SOLID, new CornerRadii(5),
						new BorderWidths(i)))) // width is changing
				.map(b -> new KeyFrame(Duration.millis(mills[0] += 20),
						new KeyValue(((AnchorPane) tilePane.getParent()).borderProperty(), b, Interpolator.EASE_IN))) // border
																														// is
																														// changing
				.toArray(KeyFrame[]::new); // return KeyFrames

		// An animation consists of a time line of key frames
		final Timeline timeline = new Timeline(keyFrames);
		timeline.setCycleCount(2);
		timeline.setAutoReverse(true); // go back to state before the animation started
		timeline.play();
		timeline.setOnFinished((finishedEvent) -> tilePane.setManaged(true));
	}

}
