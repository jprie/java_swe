package at.wifiwien.javaswe.strawberry_fields.model.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import at.wifiwien.javaswe.strawberry_fields.model.io.OutputHandler;
import at.wifiwien.javaswe.strawberry_fields.model.item.Item;
import at.wifiwien.javaswe.strawberry_fields.model.item.Strawberry;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;

public class Board {

	// backing array
	private ObservableList<Field> fields;
	
	private int numColumns;
	private int numRows;

	public Board(int width, int height) {

		this.numColumns = width;
		this.numRows = height;

	}

	public void init() {

		fields = FXCollections.observableArrayList();
		 
		for (int i = 0; i < numRows; i++) {

			for (int j = 0; j < numColumns; j++) {

				// BEWARE: our position should be x,y although we have a y-x-arraylist
				fields.add(new Field(new Position(j, i)));
			}

		}
		

	}
	

	public boolean positionInsideBoard(Position pos) {
		
		return pos.x >= 0 && pos.x < numColumns && pos.y >= 0 && pos.y < numRows;
	}

	public Optional<Item> getItemAtPosition(Position pos) {
		int index = pos.y*numColumns+pos.x;
		OutputHandler.printDebug("getItemAtPosition: index=" + index);
		return fields.get(index).getItem();
	}

	public void setItemAtPosition(Item item, Position pos) {

		fields.get(pos.y*numColumns+pos.x).setItem(item);
	}
	
	public Optional<Item> removeItemFromPosition(Position pos) {
		
		int index = pos.y*numColumns+pos.x;
		Optional<Item> item = fields.get(index).getItem(); 
		fields.get(index).setItem(null);
		return item;
	}
	
	public boolean positionContainsItems(Position pos) {
		
		return fields.get(pos.y*numColumns+pos.x).getItem() != null;
	}

	public String toString() {

		String fieldsRepresentation = "";

		for (int i = 0; i < numRows; i++) {

			for (int j = 0; j < numColumns; j++) {
				fieldsRepresentation += fields.get(i*numColumns+j);

			}
			fieldsRepresentation += "\n";
		}

		return fieldsRepresentation;
	}
	
	public ObservableList<Field> getFields() {
		
		return fields;
	}

	public int getWidth() {
		return numColumns;
	}

	public int getHeight() {
		return numRows;
	}
	
	

}
