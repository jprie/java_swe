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

public class BoardController extends CommonPropertyController {

	Game game = Game.getInstance();

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TilePane tilePane;

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
			initBoard();
			

		} else {
			// register listener and init board later
			game.gameInitializedProperty().addListener((obs, ov, nv) -> initBoard());

		}
	}

	// handles when local fields get updated triggered by a change in the model fields 
	public void handleFieldsUpdated(Change<? extends Field> c) {

		while (c.next()) {
			if (c.wasUpdated()) {

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
	 * Update takes an imageView (if available) from the board and replaces it with a piece
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

	/**
	 * Generates individual fields of the surface in a 2d-tilePane
	 * Fields can contain an imageView with an Image representing an item
	 */
	private void generateFields() {

		int numColumns = game.getBoard().getWidth();
		int numRows = game.getBoard().getHeight();

		// init board

		List<StackPane> stacks = new ArrayList<StackPane>();
		int numItems = 0;
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {

				// calculate 2d-coordinates to 1-d list-index
				Optional<Item> item = fields.get(i * numColumns + j).getItem();

				// create a stack pane for each field in the model
				StackPane stackPane = new StackPane();
				stackPane.getStyleClass().add("stack-pane");
				
				if (item.isPresent()) {
				
					// if item exists represent it with an image and add it to the stack
					ImageView imageView = imageViewForItem(item.get());
					stackPane.getChildren().add(imageView);
					
					// count items for checking later
					numItems++;
				}

				stacks.add(stackPane);

			}
		}

		// check that all items were drawn
		assert (numItems - 2 - 8 == game.getStrawberriesLeft());

		
		addStacksToTilePane(numColumns, numRows, stacks);
	}

	/**
	 * Configures the tiles of the tile pane and adds the stacks to it as children
	 * This has to be done, since the tilePane is already added to the scene. Thus, we must
	 * add the children on the ApplicationThread! 
	 * @param numColumns
	 * @param numRows
	 * @param stacks
	 */
	private void addStacksToTilePane(int numColumns, int numRows, List<StackPane> stacks) {
		Platform.runLater(() -> {
			tilePane.setPrefColumns(numColumns);
			tilePane.getChildren().addAll(stacks);
			
			// set focus for getting key events
			tilePane.requestFocus();

			// tell game controller that tile pane was initialized
			boardInitialized.set(true);

		});
	}

	/**
	 * Creates an image view with an image that represents the given item
	 * @param item
	 * @return
	 */
	private ImageView imageViewForItem(Item item) {
		
		ImageView itemView = new ImageView(new Image(getClass().getResourceAsStream(getImageURLForItem(item))));
		itemView.setPreserveRatio(true);
		itemView.setFitHeight(Constants.ITEMVIEW_HEIGHT_IN_STACKVIEW);
		return itemView;
	}

	/**
	 * Returns the URL where the image file can be found
	 * @param item
	 * @return
	 */
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
		case FENCE:
			url = Constants.PATH_TO_FENCE_IMAGE;
			break;
		default:
			url = "";
		}

		return url;

	}

	/**
	 * Handles key events and sends moves to the model
	 * @param event
	 */
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

	/**
	 * Sample animation showing the red border when pieces go outside of board
	 */
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
