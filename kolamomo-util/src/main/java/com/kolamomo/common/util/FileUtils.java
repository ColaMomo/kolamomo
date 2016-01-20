package com.kolamomo.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
	public static List<String> readLine(String fileName) {
		List<String> result = new ArrayList<String> ();
		File file = new File(fileName);
		BufferedReader fileReader = null;
		try {
			fileReader = new BufferedReader(new FileReader(file));
			String tempStr = null;
			while((tempStr = fileReader.readLine()) != null) {
				result.add(tempStr);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	public static void writeLine(String fileName, String content) {
		writeLine(fileName, content, true);
	}

	public static void writeLine(String fileName, String content, boolean append) {
		File file = new File(fileName);
		BufferedWriter fileWriter = null;
		try {
			fileWriter = new BufferedWriter(new FileWriter(file, append));
			fileWriter.write(content);
			fileWriter.newLine();
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	} 
}
