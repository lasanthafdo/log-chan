package org.logchan.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomURLGenerator {
	
    private static final String lCase = "abcdefghijklmnopqrstuvwxyz";
    private static final String uCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String sChar = "!@#$%^&*";
    private static final String intChar = "0123456789";
	
	private Set<String> getRandomStrings(int stringCount){
		Random randomLength = new Random();
		Random randomCharacterType = new Random();
		Set<String> stringSet = new HashSet<String>();
		for(int i=0;i<stringCount;i++){
			int stringLength = randomLength.nextInt(100);
			StringBuilder string = new StringBuilder();
			while(string.length() != stringLength){
				int characterType = randomCharacterType.nextInt(4);
				switch (characterType) {
				case 0:
					Random random1 = new Random();
					string.append(lCase.charAt(random1.nextInt(26)));
					break;
				case 1:
					Random random2 = new Random();
					string.append(uCase.charAt(random2.nextInt(26)));
					break;
				case 2:
					Random random3 = new Random();
					string.append(sChar.charAt(random3.nextInt(8)));
					break;
				case 3:
					Random random4 = new Random();
					string.append(intChar.charAt(random4.nextInt(10)));
					break;
				default:
					break;
				}
			}
			stringSet.add(string.toString());
		}
		return stringSet;
	}
	
	public Set<URL> getURLSet(int urlCount){
		Set<String> stringSet = getRandomStrings(urlCount);
		Set<URL> urlSet = new HashSet<URL>();
		for(String string:stringSet){
			try {
				URL url = new URL("http", "example.com/", string + ".html");
				urlSet.add(url);
			} catch (MalformedURLException e) {
				//TODO log
			}
		}
		return urlSet;
	}
	
	public static void main(String[] args) {
		RandomURLGenerator gen = new RandomURLGenerator();
		Set<URL> set = gen.getURLSet(250);
		String beforeUrl = "10.116.47.101 - - [15/Jun/2012:16:23:47 +0530] \"GET /testfiles/korra.tar.gz HTTP/1.1\" 200 6999656628 ";
		String afterUrl  = " \"Mozilla/5.0 (Windows NT 5.1; rv:13.0) Gecko/20100101 Firefox/13.0\"";
		File file = new File("F:/sampleLog.lg");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
			for(URL url : set){
				writer.write(beforeUrl + "\"" + url.toString() + "\"" + afterUrl);
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
		}
		


		
	}
}