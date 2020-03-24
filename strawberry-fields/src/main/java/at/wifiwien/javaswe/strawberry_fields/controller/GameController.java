package at.wifiwien.javaswe.strawberry_fields.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;

public class GameController extends BaseController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	AnchorPane view;

	@FXML
	void initialize() {

		view.sceneProperty().addListener(this::handleSceneChanged);

	}

	public void handleSceneChanged(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {

		newValue.windowProperty().addListener(this::handleStageChanged);
//		newValue.setOnKeyPressed(this::handleKeyPressed);
	}

	public void handleStageChanged(ObservableValue<? extends Window> observable, Window oldValue, Window newValue) {

//		newValue.setHeight(game.getBoard().getHeight() * 80 + 20);
//		newValue.setWidth(game.getBoard().getWidth() * 80);

	}
}
