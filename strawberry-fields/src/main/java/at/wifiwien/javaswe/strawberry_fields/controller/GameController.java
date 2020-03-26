package at.wifiwien.javaswe.strawberry_fields.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class GameController extends CommonPropertyController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	AnchorPane view;

	@FXML
	void initialize() {

		boardInitialized.addListener((obs, ov, nv) -> view.getScene().getWindow().sizeToScene());
		
	}
}
