
package com.steindl.kristof;

import java.io.Serializable;

/**
 *
 * @author Steindl Kristof 
 */
public class MatchState implements Serializable{
	
	static final long serialVersionUID = 1L;
	
	private int[][] board;
	private int playerOnTurn;

	public MatchState(int[][] board, int playerOnTurn) {
		this.board = board;
		this.playerOnTurn = playerOnTurn;
	}
	
	public void togglePlayer() {
		this.playerOnTurn = playerOnTurn % 2 + 1;
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
	
	

}
