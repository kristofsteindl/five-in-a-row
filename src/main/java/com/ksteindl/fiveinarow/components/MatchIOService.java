
package com.ksteindl.fiveinarow.components;

import com.ksteindl.fiveinarow.GameController;
import com.ksteindl.fiveinarow.exception.MatchLoadingException;
import com.ksteindl.fiveinarow.model.MatchState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 *
 * @author Steindl Kristof 
 */
public class MatchIOService {

	private final static String SAVE_FOLDER_REL_PATH = "saves";

	private static final Logger logger = LogManager.getLogger(GameController.class);

	public MatchIOService() {
		Path path = Paths.get(SAVE_FOLDER_REL_PATH);
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
				logger.debug(SAVE_FOLDER_REL_PATH + " folder was created into project root");
			} catch (IOException ioException) {
				logger.error("IOException occured, during save folder creation");
				throw new RuntimeException("IOException occured, during save folder creation", ioException);
			}

		}
	}

	public MatchState loadMatch(String savedFileName) throws MatchLoadingException {
		String stringPath = SAVE_FOLDER_REL_PATH + "/" + savedFileName + ".sav";
		Path path = Paths.get(stringPath);
		if (!Files.exists(path)) {
			throw new MatchLoadingException();
		} else {
			try {
				FileInputStream fileInputStream = new FileInputStream(stringPath);
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				MatchState loeadedMatch = (MatchState) objectInputStream.readObject();
				objectInputStream.close();
				fileInputStream.close();
				return loeadedMatch;
			 } catch (IOException ioException) {
				logger.debug("IOException", ioException);
				throw new MatchLoadingException();
			 } catch (ClassNotFoundException classNotFoundException) {
				logger.debug("MatchState class not found", classNotFoundException);
				throw new MatchLoadingException();
			 }
		}
		
	}
	
	
	public boolean softSave(String fileName, MatchState matchState, Scanner scanner) {
		String confirmKeyWord = "yes";
		Path path = Paths.get(SAVE_FOLDER_REL_PATH + "/" + fileName + ".sav");
		if (Files.exists(path)) {
			System.out.println("File name is already exists. Do you want to owerwrite it? ('" + confirmKeyWord + "' for yes, everything else for no)");
			if(scanner.next().equals(confirmKeyWord)) {
				return hardSave(fileName, matchState);
			} else {
				return softSavePrompt(fileName, matchState, scanner);
			}
		} else if (!fileNameIsValid(fileName)) {
			System.out.println("Name must be maximum 40 character long, and cannot start or end '-' character");
			return softSavePrompt(fileName, matchState, scanner);
		} else {
			return hardSave(fileName, matchState);
		}
	}

	private boolean fileNameIsValid(String fileName) {
		Character dash = '-';
		if (fileName.length() > 40) {
			return false;
		}
		if (fileName.charAt(0) == dash) {
			return false;
		}
		if (fileName.charAt(fileName.length() - 1) == dash) {
			return false;
		}
		return true;
	}

	private boolean softSavePrompt(String fileName, MatchState matchState, Scanner scanner) {
		System.out.println("Please give enter a file name for saving");
		fileName = scanner.next();
		return softSave(fileName, matchState, scanner);
	}
	
	private boolean hardSave(String fileName, MatchState matchState) {
		System.out.println("Hardsave beginning");
		try {
			 FileOutputStream fileOut = new FileOutputStream(SAVE_FOLDER_REL_PATH + "/" + fileName + ".sav");
			 ObjectOutputStream out = new ObjectOutputStream(fileOut);
			 out.writeObject(matchState);
			 out.close();
			 fileOut.close();
			 System.out.printf("Match is saved into " + SAVE_FOLDER_REL_PATH + "/" + fileName + ".sav");
			 return true;
      	} catch (IOException exception) {
			 exception.printStackTrace();
			 return false;
      	}
	}
	


}
