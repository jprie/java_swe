package at.wifiwien.javaswe.strawberry_fields.model.game;

public enum Command {

	MOVE_COMMAND("m", "Move", "Move the current Player from src to dest"),
	PRINT_COMMAND("p", "Print", "Prints the game board to console"),
	QUIT_COMMAND("q", "Quit", "Ends the game after confirmation"),
	HELP_COMMAND("h", "Help", "Shows the help text");
	
	private final String abbreviation;
	private final String name;
	private final String info;
	
	private Command(String abbreviation, String name, String info) {
		
		this.abbreviation = abbreviation;
		this.name = name;
		this.info = info;
	}

	public String getAbbreviation() {
		return abbreviation;
	}


	public String getName() {
		return name;
	}

	public String getHelpText() {
		return abbreviation + "..." + name + "\n\t" + info;
	}
	
	
	
	
}
