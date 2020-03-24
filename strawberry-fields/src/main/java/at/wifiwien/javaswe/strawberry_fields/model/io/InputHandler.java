package at.wifiwien.javaswe.strawberry_fields.model.io;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.wifiwien.javaswe.strawberry_fields.exception.SyntaxErrorException;
import at.wifiwien.javaswe.strawberry_fields.exception.UnknownCommandException;
import at.wifiwien.javaswe.strawberry_fields.model.game.Command;
import at.wifiwien.javaswe.strawberry_fields.model.game.Move;
import at.wifiwien.javaswe.strawberry_fields.model.game.Move.Direction;
import at.wifiwien.javaswe.strawberry_fields.model.game.Position;

public class InputHandler {

	private static Scanner scan = new Scanner(System.in);

	public static Command handleCommand() throws SyntaxErrorException, UnknownCommandException {

		String input = scan.next();

		// check syntax via regex
		String regex = "[a-zA-z]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		if (!matcher.matches()) {
			throw new SyntaxErrorException("input did not match pattern: " + regex);
		}
		return commandFromAbbreviation(input);

	}

	private static Command commandFromAbbreviation(String input) throws UnknownCommandException {

		switch (input) {
		case "m":
			return Command.MOVE_COMMAND;
		case "p":
			return Command.PRINT_COMMAND;
		case "q":
			return Command.QUIT_COMMAND;
		case "h":
			return Command.HELP_COMMAND;
		default:
			throw new UnknownCommandException(input + " is not a valid command ");
		}

	}

	public static Move handleMove() throws SyntaxErrorException {
	
	String input = scan.next();
	String moveRegex = "([0-9])([a-zA-Z])";
	Pattern pattern = Pattern.compile(moveRegex);
	Matcher matcher = pattern.matcher(input);
	
	if (!matcher.matches()) {
		throw new SyntaxErrorException("Input did not match pattern: " + moveRegex);
	}
	
	int distance = Integer.parseInt(matcher.group(1));
	String direction = matcher.group(2);
	

	return new Move(distance, direction.toUpperCase());
}

	
// handle Move with to Positions given
//
//	public static Move handleMove() throws SyntaxErrorException {
//		
//		scan.nextLine();
//		
//		String input = scan.nextLine();
//		String posRegex = "([0-9]+):([0-9]+)";
//		String moveRegex = "(([0-9]+):([0-9]+)) (([0-9]+):([0-9]+))";
//
//		Pattern pattern = Pattern.compile(moveRegex);
//		Matcher matcher = pattern.matcher(input);
//		
//		if (!matcher.matches()) {
//			throw new SyntaxErrorException("Input did not match pattern: " + moveRegex);
//		}
//		
//		int xSrc = Integer.parseInt(matcher.group(2));
//		int ySrc = Integer.parseInt(matcher.group(3));
//		
//		int xDest = Integer.parseInt(matcher.group(5));
//		int yDest = Integer.parseInt(matcher.group(6));
//		
//
//		return new Move(new Position(xSrc, ySrc), new Position(xDest, yDest));
//	}

	

}
