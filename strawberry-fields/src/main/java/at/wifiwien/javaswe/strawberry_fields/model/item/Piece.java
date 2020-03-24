package at.wifiwien.javaswe.strawberry_fields.model.item;

public class Piece extends Item {

	private int strawberries = 0;

	public Piece(String face) {
		super(face, Type.PIECE);
		
	}
	
	public int getStrawberries() {
		return strawberries;
	}

	public void setStrawberries(int strawberries) {
		this.strawberries = strawberries;
	}
	
	public void incrementStrawberries(int by) {
		
		this.strawberries += by;
	}
	
	public void decrementStrawberries(int by) {
		
		this.strawberries -= by;
	}
	
	@Override
	public String toString() {
		
		return face;
	}
}
