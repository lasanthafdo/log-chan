package org.logchan.templates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.logchan.core.SystemConstants;

public class TimeStampBasedDiscoverer extends TemplateDiscoverer {
	private List<String> tsRegexes;
	private String startEscapeClass;
	private String endEscapeClass;

	public TimeStampBasedDiscoverer() {
		super();
		startEscapeClass = "^[.*?&&[^\\[]]+\\["; // Not for now: :,;\"'\\{\\(
		endEscapeClass = "[.*?]+$"; // Not for now: :,;\"'\\}\\)
		tsRegexes = new ArrayList<String>();
		addTsRegex("([\\w/]+\\w[\\d+:]+\\s[+\\-]\\d{4})");
	}

	private void addTsRegex(String regex) {
		tsRegexes.add(regex);
	}

	@Override
	protected String buildRegularExpression(String delimitChar,
			List<String[]> escapeSequences) {
		// TODO Auto-generated method stub
		return super.buildRegularExpression(delimitChar, escapeSequences);
	}

	protected String buildRegexGroup(String initRegex) {
		String grpRegex = "^(.*?)" + initRegex + "(.*+)$";
		return grpRegex;
	}

	@Override
	public Map<String, Object> discoverTemplate(String filename, Long filesize)
			throws IOException {
		readSampleLines(filename, filesize);
		List<String> samples = getSamples();
		for (String sample : samples) {
			analyzeSampleLine(sample);
		}

		return super.discoverTemplate(filename, filesize);
	}

	private void analyzeSampleLine(String line) {
		Pattern p;
		Matcher regexMatcher;
		MatchResult result;
		for (String regex : tsRegexes) {
			p = Pattern.compile(buildRegexGroup(regex));
			regexMatcher = p.matcher(line);
			if (regexMatcher.find()) {
				result = regexMatcher.toMatchResult();
				if(result.group(2) != null) {
					int startIndex = result.start(2);
					int endIndex = result.end(2);
					System.out.println("Match: " + result.group(2));
					Character startChar = line.charAt(startIndex -1);
					Character endChar = line.charAt(endIndex);
					Character matchToChar = SystemConstants.ESCAPE_CHARS.get(startChar);
					// Try for whitespace characters, quotes etc
					// What if there is no escape sequence and delimiter and startChar == endChar
					if(matchToChar != null && endChar.equals(matchToChar))
						addEscapeSequence(SystemConstants.REGEX_ESCAPE_CHARS.get(startChar));
					System.out.println("Delimiter? " + line.charAt(startIndex - 2));
					System.out.println("Delimiter? " + line.charAt(endIndex + 1));
				}
			}
		}
	}
}
