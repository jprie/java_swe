package at.wifiwien.javaswe.strawberry_fields.model.game;

import java.util.Optional;

import at.wifiwien.javaswe.strawberry_fields.application.Constants;
import at.wifiwien.javaswe.strawberry_fields.model.item.Item;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Field {

	// package visible for easier access
	ObjectProperty<Position> position;
	ObjectProperty<Optional<Item>> item = new SimpleObjectProperty<>(Optional.empty());

	public Field(Position pos) {

		this.position = new SimpleObjectProperty<Position>(pos);
	}


	public final ObjectProperty<Position> positionProperty() {
		return this.position;
	}

	public final Position getPosition() {
		return this.positionProperty().get();
	}

	public final void setPosition(final Position position) {
		this.positionProperty().set(position);
	}

	public final ObjectProperty<Optional<Item>> itemProperty() {
		return this.item;
	}

	public final Optional<Item> getItem() {
		return this.itemProperty().get();
	}

	public final void setItem(final Item item) {
		this.itemProperty().set(Optional.ofNullable(item));
	}
	

	@Override
	public String toString() {

		return itemProperty().get().isPresent() ? itemProperty().get().get().toString() : Constants.EMPTY_FIELD;
	}

}
