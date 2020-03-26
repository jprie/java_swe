
package at.wifiwien.javaswe.strawberry_fields.model.item;

public class Item {
	
	public enum Type {
		PIECE, STRAWBERRY, FENCE
	}

	// set only at the beginning of the game
	final String face;
	final Type type;
	
	public Item(String face, Type type) {
		
		this.face = face;
		this.type = type;
	}

	public String getFace() {
		return face;
	}
	
	public Type getType() {
		return type;
	}
	
	
	
}
