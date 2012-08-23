package org.logchan.core;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.logchan.ui.UserInterface;

public class LogChanApp {
	private static final Logger log = Logger.getLogger(LogChanApp.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PropertyConfigurator.configure("lib/log4j.properties");
		log.debug("LogChan launched");
        try {        	
            new UserInterface().setVisible(true);
        } catch (Exception ex) {
        	log.fatal("LogChan initialize error");
        }
	}

}
