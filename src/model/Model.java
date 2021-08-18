package model;

import java.util.List;

import controller.Direction;
import controller.ATileMove;

/**
 * Manipulates the state of the board according to the rules of the game as well as keeping track of the score, high score, and checks for victory conditions.
 */
public class Model implements ModelInterface {
    private Board board;

    @Override
    public List<ATileMove> startGame() {
			board = SaverLoader.loadGame();
			// if no saved game state, start a new game with default width and height
			if (board == null) {
					board = new Board();
					return Rules.startGame(board, Rules.DEFAULT_WIDTH, Rules.DEFAULT_HEIGHT);
			// if have saved game state, return the state
			} else {
					return board.getAllTiles();
			}
    }

    @Override
    public List<ATileMove> startGame(int width, int height) {
			board = new Board();
			List<ATileMove> moves = null;
			try {
					moves = Rules.startGame(board, width, height);
			} catch (IllegalArgumentException e) { // if an exception is thrown due to bad width/height given, make the default board
					moves = Rules.startGame(board, Rules.DEFAULT_WIDTH, Rules.DEFAULT_HEIGHT);
			}
			return moves;
    }

    @Override
    public List<ATileMove> restartGame(int width, int height) {
			List<ATileMove> moves = null;
			try {
					moves = Rules.restartGame(board, width, height);
			} catch (IllegalArgumentException e) { // if an exception is thrown due to bad width/height given, make the default board
					moves = Rules.restartGame(board, Rules.DEFAULT_WIDTH, Rules.DEFAULT_HEIGHT);
			}
			return moves;
    }

    @Override
    public void endGame() {
			if (board.isGameOver()) {
					Rules.restartGame(board, board.getWidth(), board.getHeight());
			}
			SaverLoader.saveGame(board);
			board = null;
    }

    @Override
    public List<ATileMove> makeMove(Direction d) {
			return Rules.makeMove(board, d);
    }

    @Override
    public int getScore() {
			return board.getScore();
    }

    @Override
    public int getHighScore() {
			return board.getHighScore();
    }

    @Override
    public boolean isGameWon() {
			return board.isGameWon();
    }

    @Override
    public boolean isGameOver() {
			return board.isGameOver();
    }

    @Override
    public int getWidth() {
			return board.getWidth();
    }

    @Override
    public int getHeight() {
			return board.getHeight();
    }

}