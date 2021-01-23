
package com.steindl.kristof;

/**
 *
 * @author Steindl Kristof 
 */
public class Accumulator {
	private int player;
	private int score;

	public Accumulator() {
		this.player = 0;
		this.score = 0;
	}

	public void acc(int tile) {
		if (tile == this.player && tile != 0) {
			this.score++;
		} else {
			reset(tile);
		}
	}
	
	private void reset(int player) {
		this.player = player;
		this.score = 1;		
	}
	
	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	
	
	

}
