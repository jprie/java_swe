package ${package}.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PrimerController {

    @FXML
    private Label label;

    @FXML
    private Button button;
    
    @FXML
    protected void initialize() {
    	button.setText("New");
    }

    @FXML
    void buttonPressed(ActionEvent event) {
    	label.setText("Button was pressed");
    }

}
