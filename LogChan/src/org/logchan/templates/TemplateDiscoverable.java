package org.logchan.templates;

import java.io.IOException;
import java.util.Map;

public interface TemplateDiscoverable {
	public Map<String,Object> discoverTemplate(String filename, Long filesize) throws IOException;
}
