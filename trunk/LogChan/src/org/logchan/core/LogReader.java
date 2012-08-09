/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.logchan.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author SAMILA
 */
public class LogReader {
	private String fileName;
    
	public String getFileName() {
		return fileName;
	}

	public String readFile(String fileName) throws IOException {
		StringBuilder content = new StringBuilder("\n");
		if (fileName != null && new File(fileName).exists()
				&& new File(fileName).isFile()) {
			this.fileName = fileName;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(fileName))));
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line).append("\n");
			}
		}
		return content.toString();

	}
       
}
