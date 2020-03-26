package at.wifiwien.javaswe.strawberry_fields.model.item;

import at.wifiwien.javaswe.strawberry_fields.application.Constants;

public class Fence extends Item {

	boolean isHorizontal = true;

	public Fence() {
		
		super(Constants.FENCE_HORIZONTAL_FACE, Item.Type.FENCE);
	}

	@Override
	public String toString() {

		return isHorizontal ? Constants.FENCE_HORIZONTAL_FACE : Constants.FENCE_VERTICAL_FACE;
	}
}
