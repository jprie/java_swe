package at.wifiwien.javaswe.strawberry_fields.controller;

import at.wifiwien.javaswe.strawberry_fields.model.game.Field;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;


public class CommonPropertyController {
	
	static ObservableList<Field > fields;
	static BooleanProperty boardInitialized = new SimpleBooleanProperty();
	
}
