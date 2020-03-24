package at.wifiwien.javaswe.strawberry_fields.model.io;

import java.util.stream.Stream;

import at.wifiwien.javaswe.strawberry_fields.application.Constants;
import at.wifiwien.javaswe.strawberry_fields.model.game.Command;

public class OutputHandler {

	public static void printInfo(String message) {
		
		System.out.println(message);
	}
	
	public static void printDebug(String message) {
		
		System.out.print(Constants.PREFIX_DEBUG);
		System.out.println(message);
	}
	
	public static void printWarning(String message) {
		
		System.out.println(Constants.PREFIX_WARNING);
		System.out.println(message);
	}
	
	public static void printError(String message) {
		
		System.out.print(Constants.PREFIX_ERROR);
		System.out.println(message);
	}

	public static void printHelpText() {
	
		String helpText = Stream.of(Command.values()).map(Command::getHelpText).reduce("Help:", (a,b) -> a + "\n\n" + b);
		System.out.println(helpText + "\n");
		
	}
	
	

	
}
