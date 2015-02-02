package com.makalu.phonenum.core.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class FileUtil {
	final Logger LOGGER = Logger.getLogger(FileUtil.class.getName());
	
	public List<String> generatePhoneList(String fileName) {
		List<String> phoneList = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				phoneList.add(line);
			}
			reader.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return phoneList;
	}

	public void generateCleanFile(String dirtyFileName, String cleanFileName) {
		String pattern = "\\[2K(\\[D)+";
		String waitPattern = "[\\.]{6}\\s\\s[\\[|\\/\\-\\\\\\]+\\s]+";
		BufferedReader br = null;
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(dirtyFileName));
			while ((sCurrentLine = br.readLine()) != null) {
				String output = sCurrentLine.replaceAll("\\p{Cntrl}", "");
				String cleanOutput = output.replaceAll(pattern, "");
				String clearWait = cleanOutput.replaceAll(waitPattern, "");
				try (PrintWriter out = new PrintWriter(new BufferedWriter(
						new FileWriter(cleanFileName, true)))) {
					out.println(clearWait);
				} catch (IOException e) {
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void deleteFile(String fileName) {
		File outputFile = new File(fileName);
		if (outputFile.exists() && !outputFile.isDirectory()) {
			outputFile.delete();
		}
	}

	public void writeListToFile(List<String> phoneList, String fileName) {
		if (!phoneList.isEmpty()) {
			FileWriter writer;
			try {
				writer = new FileWriter(fileName);
				for (String phoneNum : phoneList) {
					writer.write(phoneNum + "\n");
				}
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void createDir(String dirName) {
		File file = new File(dirName);
		file.mkdirs();
	}
	
	public String getAppBasePath() {
		InputStream in = this.getClass().getResourceAsStream("/phoneNumCore.properties");
		Properties prop = new Properties();
		String appBasePath = null;
		try {
			prop.load(in);
			appBasePath = prop.getProperty("appBasePath");
		} catch (IOException ex) {
	        ex.printStackTrace();
	    }
		
		return appBasePath;
	}

}
