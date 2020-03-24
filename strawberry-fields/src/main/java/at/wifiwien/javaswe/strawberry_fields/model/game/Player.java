package at.wifiwien.javaswe.strawberry_fields.model.game;

import at.wifiwien.javaswe.strawberry_fields.model.item.Piece;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Player {

	private StringProperty name = new SimpleStringProperty();
	private IntegerProperty score = new SimpleIntegerProperty();
	private ObjectProperty<Piece> piece = new SimpleObjectProperty<Piece>();

	
	public Player(String name) {
		
		this.name.set(name);
	}
	
	public final StringProperty nameProperty() {
		return this.name;
	}

	public final String getName() {
		return this.nameProperty().get();
	}

	public final void setName(final String name) {
		this.nameProperty().set(name);
	}

	

	public final ObjectProperty<Piece> pieceProperty() {
		return this.piece;
	}
	

	public final Piece getPiece() {
		return this.pieceProperty().get();
	}
	

	public final void setPiece(final Piece piece) {
		this.pieceProperty().set(piece);
	}

	public final IntegerProperty scoreProperty() {
		return this.score;
	}
	

	public final int getScore() {
		return this.scoreProperty().get();
	}
	

	public final void setScore(final int score) {
		this.scoreProperty().set(score);
	}

	public void incrementScore() {
		score.set(score.get()+1);
	}
	
	
	
	

}
