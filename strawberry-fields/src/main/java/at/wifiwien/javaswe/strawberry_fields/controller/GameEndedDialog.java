package at.wifiwien.javaswe.strawberry_fields.controller;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class GameEndedDialog extends Dialog<ButtonType> {

	public GameEndedDialog(String header, String content) {
		this.setContentText(content);
		this.setHeaderText(header);
		this.getDialogPane().getButtonTypes().add(ButtonType.OK);
		this.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
	}
}
