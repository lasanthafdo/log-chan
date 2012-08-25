package org.logchan.formats;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class LGCFilter extends FileFilter {

	@Override
	public String getDescription() {
		return "*.lgc (Log Chan File)";
	}

	@Override
	public boolean accept(File file) {
		if(file.isDirectory())
			return true;
		
		String extension = getExtension(file);
		if (extension != null && extension.equals("lgc"))
			return true;
		else
			return false;
	}

	private String getExtension(File file) {
		String ext = null;
		String s = file.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}

		return ext;
	}
}
