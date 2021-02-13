
package com.ksteindl.fiveinarow.model;

import java.io.Serializable;

/**
 *
 * @author Steindl Kristof 
 */
public class MatchState implements Serializable{
	
	static final long serialVersionUID = 1L;
	
	private int[][] board;
	private int playerOnTurn;
	private final Character player1Symbol;
	private final Character player2Symbol;

	public MatchState(int[][] board, int playerOnTurn, Character player1Symbol, Character player2Symbol) {
		this.player1Symbol = player1Symbol;
		this.player2Symbol = player2Symbol;
		this.board = board;
		this.playerOnTurn = playerOnTurn;
	}

	public int[][] getBoard() {
		return board;
	}

	public void setBoard(int[][] board) {
		this.board = board;
	}

	public int getPlayerOnTurn() {
		return playerOnTurn;
	}

	public void setPlayerOnTurn(int playerOnTurn) {
		this.playerOnTurn = playerOnTurn;
	}

	public Character getPlayer1Symbol() {
		return player1Symbol;
	}

	public Character getPlayer2Symbol() {
		return player2Symbol;
	}
}
