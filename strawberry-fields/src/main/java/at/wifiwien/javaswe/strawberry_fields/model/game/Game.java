package at.wifiwien.javaswe.strawberry_fields.model.game;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import at.wifiwien.javaswe.strawberry_fields.application.Constants;
import at.wifiwien.javaswe.strawberry_fields.exception.MoveException;
import at.wifiwien.javaswe.strawberry_fields.exception.SyntaxErrorException;
import at.wifiwien.javaswe.strawberry_fields.exception.UnknownCommandException;
import at.wifiwien.javaswe.strawberry_fields.model.io.InputHandler;
import at.wifiwien.javaswe.strawberry_fields.model.io.OutputHandler;
import at.wifiwien.javaswe.strawberry_fields.model.item.Item;
import at.wifiwien.javaswe.strawberry_fields.model.item.Piece;
import at.wifiwien.javaswe.strawberry_fields.model.item.Strawberry;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Game {

	// singleton instance
	private static Game game;

	private Player[] players;

	private final Position STARTING_POSITION_P1;
	private final Position STARTING_POSITION_P2;

	private Board board;
	private long numberStrawberries;

	ObservableList<Strawberry> strawberries;

	IntegerProperty strawberriesLeft = new SimpleIntegerProperty();

	private int boardHeight;
	private int boardWidth;

	private boolean gameIsStopped = false;

	private Position[] currentPosition;

	private IntegerProperty playersTurn = new SimpleIntegerProperty();

	private Optional<Player> winner;

	private BooleanProperty gameInitialized = new SimpleBooleanProperty(false);
	private BooleanProperty gameEnded = new SimpleBooleanProperty(false);

	private int cnt;

	public static Game getInstance() {

		if (game == null) {
			game = new Game(15, 10, 1);
			game.init();
		}
		return game;
	}

	private Game(int width, int height, long numberStrawberries) {

		// starting positions diametral
		STARTING_POSITION_P1 = new Position(0, 0);
		STARTING_POSITION_P2 = new Position(width - 1, height - 1);

		// set all fields
		this.numberStrawberries = numberStrawberries;
		this.boardWidth = width;
		this.boardHeight = height;
	}

	void init() {

		initPlayers();
		initItems();
	}

	private void initPlayers() {

		players = new Player[2];
		players[0] = new Player("Johannes");
		players[1] = new Player("Peter");

	}

	public void initItems() {

		// create 2 players
		players[0].setPiece(new Piece(Constants.PLAYER_1_FACE));
		players[1].setPiece(new Piece(Constants.PLAYER_2_FACE));

		// create strawberries
		strawberries = FXCollections.observableArrayList(
				Stream.generate(Strawberry::new).limit(numberStrawberries).collect(Collectors.toList()));

		// create empty board of given size
		board = new Board(boardWidth, boardHeight);
		board.init();

		// items in starting position
		layoutItemsOnBoard();
	}

	private void layoutItemsOnBoard() {

		// players at starting positions
		board.setItemAtPosition(players[0].getPiece(), STARTING_POSITION_P1);
		board.setItemAtPosition(players[1].getPiece(), STARTING_POSITION_P2);

		currentPosition = new Position[] { STARTING_POSITION_P1, STARTING_POSITION_P2 };

		// create random position streams
		Random rand = new Random(Instant.now().getEpochSecond());
		List<Position> randomPositions = Stream
				.generate(() -> new Position(rand.nextInt(boardWidth), rand.nextInt(boardHeight)))
				.filter(p -> !(p.equals(STARTING_POSITION_P1) || p.equals(STARTING_POSITION_P2))).distinct()
				.limit(numberStrawberries).collect(Collectors.toList());

		assert (randomPositions.size() == numberStrawberries);
		assert (randomPositions.size() == strawberries.size());

		// put strawberries at generated random positions
		for (int i = 0; i < strawberries.size(); i++) {
			board.setItemAtPosition(strawberries.get(i), randomPositions.get(i));
		}

		strawberriesLeft.set(strawberries.size());

		gameInitialized.set(true);

	}

	public void start() {

		OutputHandler.printInfo(Constants.WELCOME_MESSAGE);

		print();

		while (!gameIsStopped) {

			Command command;
			OutputHandler.printInfo(Constants.PROMPT_ENTER_COMMAND);

			try {
				command = InputHandler.handleCommand();
				executeCommand(command);
			} catch (SyntaxErrorException e) {
				System.out.println(e.getMessage());
				continue;
			} catch (UnknownCommandException e) {
				System.out.println(e.getMessage());
				continue;
			}

		}

	}

	private void executeCommand(Command command) throws SyntaxErrorException {

		switch (command) {
		case PRINT_COMMAND:
			print();
			break;
		case MOVE_COMMAND:
			OutputHandler.printInfo(Constants.PROMPT_ENTER_MOVE);
			Move move = InputHandler.handleMove();
			try {
				move(move);
			} catch (MoveException e) {
				OutputHandler.printWarning(e.getMessage());
			}

			break;
		case QUIT_COMMAND:
			// TBD: for GUI
			gameIsStopped = true;
			break;
		case HELP_COMMAND:
			OutputHandler.printHelpText();
			break;
		}

	}

	/**
	 * Verifies the given move according to rules and executes the move if verified
	 * 
	 * @param move
	 * @throws MoveException
	 */
	public void move(Move move) throws MoveException {

		OutputHandler.printDebug("Moving: " + move);

		// src position is given by the movable piece's location (stored after every
		// move)
		Position src = currentPosition[playersTurn.get()];

		// dest position is calculated from src position and given move (direction,
		// distance)
		Position dest = calculateDestPosition(src, move);

		// check positions inside board
		if (!board.positionInsideBoard(dest)) {

			throw new MoveException("dest outside of board");
		}

		Optional<Item> srcItem = board.getItemAtPosition(src);
		Optional<Item> destItem = board.getItemAtPosition(dest);

		if (srcItem.isEmpty() || !(srcItem.get() instanceof Piece)) {

			throw new MoveException("No piece at src position");
		}

		Optional<Item> optItem = board.removeItemFromPosition(src);

		if (optItem.isPresent())
			board.setItemAtPosition(optItem.get(), dest);

		// update score
		if (destItem.isPresent() && destItem.get() instanceof Strawberry) {
			Player currentPlayer = players[playersTurn.get()];
			currentPlayer.incrementScore();

			if (!strawberries.isEmpty()) {
				strawberries.remove(0);
				strawberriesLeft.set(strawberries.size());
				assert (players[0].getScore() + players[1].getScore() + strawberriesLeft.get() == numberStrawberries);
			}

		}

		if (strawberries.isEmpty()) {
			// game ended
			winner = determineWinner();
			gameEnded.set(true);
			
		}

		// update the current position
		currentPosition[playersTurn.get()] = dest;

		togglePlayersTurn();

	}

	private Optional<Player> determineWinner() {

		Player winner;

		if (players[0].getScore() > players[1].getScore()) {
			winner = players[0];
		} else if (players[0].getScore() < players[1].getScore()) {
			winner = players[1];
		} else {
			// DRAW
			winner = null;
		}

		return Optional.ofNullable(winner);
	}

	private void togglePlayersTurn() {

		int newTurn = playersTurn.get() == 0 ? 1 : 0;
		playersTurn.set(newTurn);
	}

	private Position calculateDestPosition(Position src, Move move) {
		int x = 0, y = 0;

		// determine vector of direction
		switch (move.getDirection()) {
		case UP:
			x = 0;
			y = -1;
			break;
		case DOWN:
			x = 0;
			y = 1;
			break;
		case LEFT:
			x = -1;
			y = 0;
			break;
		case RIGHT:
			x = 1;
			y = 0;
			break;
		}

		// we multiply by distance only non-0 value get changed
		// i.e. x=0; y=1; distance = 2 -> x=0; y=2;
		x *= move.getDistance();
		y *= move.getDistance();

		return new Position(src.x + x, src.y + y);
	}

	private void print() {

		OutputHandler.printInfo(board.toString());

	}

	public Board getBoard() {

		return board;
	}

	public Player getPlayer1() {
		return players[0];
	}

	public Player getPlayer2() {
		return players[1];
	}

	public Optional<Player> getWinner() {

		return winner;
	}

	public final IntegerProperty strawberriesLeftProperty() {
		return this.strawberriesLeft;
	}

	public final int getStrawberriesLeft() {
		return this.strawberriesLeftProperty().get();
	}

	public final void setStrawberriesLeft(final int strawberriesLeft) {
		this.strawberriesLeftProperty().set(strawberriesLeft);
	}

	public final BooleanProperty gameEndedProperty() {
		return this.gameEnded;
	}

	public final boolean isGameEnded() {
		return this.gameEndedProperty().get();
	}

	public final void setGameEnded(final boolean gameEnded) {
		this.gameEndedProperty().set(gameEnded);
	}

	public final IntegerProperty playersTurnProperty() {
		return this.playersTurn;
	}

	public final int getPlayersTurn() {
		return this.playersTurnProperty().get();
	}

	public final void setPlayersTurn(final int playersTurn) {
		this.playersTurnProperty().set(playersTurn);
	}

	public final BooleanProperty gameInitializedProperty() {
		return this.gameInitialized;
	}

	public final boolean isGameInitialized() {
		return this.gameInitializedProperty().get();
	}

	public final void setGameInitialized(final boolean gameInitialized) {
		this.gameInitializedProperty().set(gameInitialized);
	}

}
