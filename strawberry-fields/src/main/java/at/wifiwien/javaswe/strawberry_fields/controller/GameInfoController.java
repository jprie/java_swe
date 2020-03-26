package at.wifiwien.javaswe.strawberry_fields.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import at.wifiwien.javaswe.strawberry_fields.model.game.Game;
import at.wifiwien.javaswe.strawberry_fields.controller.GameEndedDialog;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
 

public class GameInfoController {

	Game game = Game.getInstance();

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Label strawberriesPlayer1Label;

	@FXML
	private Label strawberriesPlayer2Label;

	@FXML Label strawberriesLeftLabel;

	@FXML Label player1Label;

	@FXML Label player2Label;

	@FXML Label numberStrawberriesLeftLabel;

	@FXML
	void initialize() {
		assert strawberriesPlayer1Label != null : "fx:id=\"straberriesPlayer1Label\" was not injected: check your FXML file 'GameInfo.fxml'.";
		assert strawberriesPlayer2Label != null : "fx:id=\"straberriesPlayer2Label\" was not injected: check your FXML file 'GameInfo.fxml'.";

		// internationalize i18n
		player1Label.setText(resources.getString("player") + 1 + ":");
		player2Label.setText(resources.getString("player") + 2 + ":");
		strawberriesLeftLabel.setText(resources.getString("strawberriesLeft")+ ":");
		
		// converter
		StringConverter<Number> converter = new NumberStringConverter();

		// bind scores of players and remaining fruits to outputLabels
		strawberriesPlayer1Label.textProperty().bindBidirectional(game.getPlayer1().scoreProperty(), converter);
		strawberriesPlayer2Label.textProperty().bindBidirectional(game.getPlayer2().scoreProperty(), converter);
		numberStrawberriesLeftLabel.textProperty().bindBidirectional(game.strawberriesLeftProperty(), converter);

		// listen to changes in game ended property
		game.gameEndedProperty().addListener(this::handleGameEndedChanged);


	}
	
	public void handleGameEndedChanged(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		
		// show GameEndedDialog only if game ended
		if (newValue == true) {
			
			String dialogInfo = "";
			if (game.getWinner().isPresent()) {
				// Winner
				String winnerName = game.getWinner().map((w) -> w.getName()).orElse("Error: no winner");
				dialogInfo = winnerName + " " + resources.getString("winsMessage");
				
			} else {
				// Draw
				dialogInfo = resources.getString("drawMessage");
			}
			
			String dialogHeader = resources.getString("gameEnded");
			GameEndedDialog dialog = new GameEndedDialog(dialogHeader, dialogInfo);
			
			// show dialog and wait for answer (could be used to start a new game)
			Optional<ButtonType> result = dialog.showAndWait();
			if (result.isPresent()) {
				if (result.get() == ButtonType.OK) {
					System.out.println("Dialog ok");
				} else {
					System.out.println("Dialog cancel");
				}
			}

			
		}
		
	}
}
