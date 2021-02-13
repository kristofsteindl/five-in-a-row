
package com.ksteindl.fiveinarow;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Steindl Kristof 
 */
public class App {

	private static final Logger logger = LogManager.getLogger(App.class);

	public static void main(String[] args) {
		try (GameController gameController = new GameController()) {
			gameController.startApp();
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

}
