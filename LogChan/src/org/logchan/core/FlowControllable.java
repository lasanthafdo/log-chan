package org.logchan.core;

import java.io.IOException;
import java.util.List;

public interface FlowControllable {
	public List<String[]> parseFile(String filename) throws IOException;
	public void printParsedOutput();
	public void printMetaData();
}
