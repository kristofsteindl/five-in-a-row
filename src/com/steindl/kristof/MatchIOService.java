
package com.steindl.kristof;

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
	
	private final static Scanner SCANNER = new Scanner(System.in);
	
	public static MatchState loadMatch(String savedFileName) throws MatchLoadingException {
		String stringPath = "saves/" + savedFileName + ".sav";
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
			 } catch (IOException exception) {
				exception.printStackTrace();
				throw new MatchLoadingException();
			 } catch (ClassNotFoundException classNotFoundException) {
				System.out.println("MatchState class not found");
				classNotFoundException.printStackTrace();
				throw new MatchLoadingException();
			 }
		}
		
	}
	
	
	public static boolean softSave(String fileName, MatchState matchState) {
		Path path = Paths.get("saves/" + fileName + ".sav");
		boolean isExist = Files.exists(path);
		if (isExist) {
			System.out.println("File name is already exists. Do you want to owerwrite it? ('y' for yes, everything else for no)");
			if(SCANNER.next().equals("y")) {
				return hardSave(fileName, matchState);
			} else {
				System.out.println("Please give enter a file name for saving");
				fileName = SCANNER.next();
				return softSave(fileName, matchState);
			}
		} else {
			return hardSave(fileName, matchState);
		}
	}
	
	private static boolean hardSave(String fileName, MatchState matchState) {
		System.out.println("Hardsave begenning");
		try {
         FileOutputStream fileOut = new FileOutputStream("saves/" + fileName + ".sav");
         ObjectOutputStream out = new ObjectOutputStream(fileOut);
         out.writeObject(matchState);
         out.close();
         fileOut.close();
         System.out.printf("Match is saved into saves/" + fileName + ".sav");
		 return true;
      } catch (IOException exception) {
         exception.printStackTrace();
		 return false;
      }
	}
	


}
