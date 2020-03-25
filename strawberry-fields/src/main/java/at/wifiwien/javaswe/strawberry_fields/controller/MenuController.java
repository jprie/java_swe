package at.wifiwien.javaswe.strawberry_fields.controller;

import java.net.URL;
import java.util.ResourceBundle;

import at.wifiwien.javaswe.strawberry_fields.model.game.Game;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Menu;

public class MenuController {

	Game game = Game.getInstance();
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuItem newGameMenuItem;

    @FXML
    private MenuItem configureMenuItem;

    @FXML
    private MenuItem aboutMenuItem;

	@FXML Menu gameMenu;

	@FXML Menu editMenu;

	@FXML Menu helpMenu;

	@FXML MenuItem quitGameMenuItem;

    @FXML
    void handleAbout(ActionEvent event) {

    }

    @FXML
    void handleConfigure(ActionEvent event) {

    }

    
    @FXML
    void handleExit(ActionEvent event) {
    	
    	Stage currentStage = (Stage)((Node)event.getTarget()).getScene().getWindow();
    	currentStage.close();
    }

    @FXML
    void handleNewGame(ActionEvent event) {
    	
    }


    public void initialize() {
        assert newGameMenuItem != null : "fx:id=\"newGameMenuItem\" was not injected: check your FXML file 'Menu.fxml'.";
        assert configureMenuItem != null : "fx:id=\"configureMenuItem\" was not injected: check your FXML file 'Menu.fxml'.";
        assert aboutMenuItem != null : "fx:id=\"aboutMenuItem\" was not injected: check your FXML file 'Menu.fxml'.";
        
        System.out.println(resources.getString("game") + "##################");
        
        // internationalization i18n
        gameMenu.setText(resources.getString("game"));
        editMenu.setText(resources.getString("edit"));
        helpMenu.setText(resources.getString("help"));
        
        newGameMenuItem.setText(resources.getString("new"));
        quitGameMenuItem.setText(resources.getString("quit"));
        aboutMenuItem.setText(resources.getString("about"));
        configureMenuItem.setText(resources.getString("configure"));

    }
}
