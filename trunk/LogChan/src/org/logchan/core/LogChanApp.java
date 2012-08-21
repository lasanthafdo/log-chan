package org.logchan.core;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.logchan.rules.RuleManager;
import org.logchan.ui.UserInterface;

public class LogChanApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        try {
            new UserInterface().setVisible(true);
        } catch (Exception ex) {
            Logger.getLogger(LogReader.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

}
