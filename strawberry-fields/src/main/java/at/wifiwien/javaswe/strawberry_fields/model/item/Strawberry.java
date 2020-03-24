package at.wifiwien.javaswe.strawberry_fields.model.item;

import at.wifiwien.javaswe.strawberry_fields.application.Constants;

public class Strawberry extends Item {

	public Strawberry() {
		
		// each strawberry has a random position calculated in the field
		// as only the field knows its size.
		super(Constants.STRAWBERRY_FACE, Type.STRAWBERRY);
		
	}
	
	@Override
	public String toString() {
		return face;
	}
}
