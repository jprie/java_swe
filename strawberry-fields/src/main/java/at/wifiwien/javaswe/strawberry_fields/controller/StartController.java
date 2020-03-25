package at.wifiwien.javaswe.strawberry_fields.controller;

import java.io.IOError;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import at.wifiwien.javaswe.strawberry_fields.application.Constants;
import at.wifiwien.javaswe.strawberry_fields.model.io.OutputHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StartController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button newGameButton;

	@FXML
	private Button settingsButton;

	@FXML
	private Button quitButton;

	@FXML
	void handleNewGame(ActionEvent event) {
		try {
			loadInPopupNewStageModally(Constants.PATH_TO_GAME_FXML);
			
		} catch (IOException e) {
			OutputHandler.printError("Could not load: " + Constants.PATH_TO_GAME_FXML);
		}

	}

	private void loadInPopupNewStageModally(String location) throws IOException {
		
		FXMLLoader loader = new FXMLLoader();
		loader.setResources(ResourceBundle.getBundle(Constants.PATH_TO_APPLICATION_BUNDLE, Locale.GERMANY));
		loader.setLocation(getClass().getResource(location));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource(Constants.PATH_TO_APPLICATION_CSS).toExternalForm());
		
		// open game in another window
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.showAndWait();
		
	}

	@FXML
	void handleQuit(ActionEvent event) {
		Platform.exit();
	}

	@FXML
	void handleSettings(ActionEvent event) {
		try {
			loadInPopupNewStageModally(Constants.PATH_TO_SETTINGS_FXML);
			
		} catch (IOException e) {
			OutputHandler.printError("Could not load: " + Constants.PATH_TO_GAME_FXML);
		}

	}
	
	@FXML
	void initialize() {
		assert newGameButton != null : "fx:id=\"newGameButton\" was not injected: check your FXML file 'Start.fxml'.";
		assert settingsButton != null : "fx:id=\"settingsButton\" was not injected: check your FXML file 'Start.fxml'.";
		assert quitButton != null : "fx:id=\"quitButton\" was not injected: check your FXML file 'Start.fxml'.";

	}
}
