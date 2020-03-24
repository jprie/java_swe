package at.wifiwien.javaswe.strawberry_fields.model.item;

import at.wifiwien.javaswe.strawberry_fields.application.Constants;

public class Fence {

	boolean isHorizontal;

	public Fence() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {

		return isHorizontal ? Constants.FENCE_HORIZONTAL_FACE : Constants.FENCE_VERTICAL_FACE;
	}
}
