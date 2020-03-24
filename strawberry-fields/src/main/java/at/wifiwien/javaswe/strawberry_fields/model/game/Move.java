package at.wifiwien.javaswe.strawberry_fields.model.game;

import at.wifiwien.javaswe.strawberry_fields.application.Constants;

public class Move {

	public enum Direction {
		UP, DOWN, LEFT, RIGHT
	}

	private final int distance;
	private final Direction direction;

	public Move(int distance, Direction direction) {
		super();
		this.direction = direction;
		this.distance = distance;
	}

	public Move(int distance, String direction) {

		this.distance = distance;

		switch (direction) {
		case Constants.MOVE_UP:
			this.direction = Direction.UP;
			break;
		case Constants.MOVE_DOWN:
			this.direction = Direction.DOWN;
			break;
		case Constants.MOVE_LEFT:
			this.direction = Direction.LEFT;
			break;
		case Constants.MOVE_RIGHT:
			this.direction = Direction.RIGHT;
			break;
		default:
			// ease the compiler
			this.direction = Direction.UP;
		}
	}

	public Direction getDirection() {
		return direction;
	}

	public int getDistance() {
		return distance;
	}

	@Override
	public String toString() {

		return distance + " " + direction;
	}

}
