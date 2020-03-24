package at.wifiwien.javaswe.strawberry_fields.application;

import at.wifiwien.javaswe.strawberry_fields.model.game.Game;

public class App {

	
	public static void main(String[] args) {
		
		Game game = Game.getInstance();
		
		game.start();
	}
}
