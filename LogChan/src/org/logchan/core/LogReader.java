/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.logchan.core;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.logchan.ui.UserInterface;

/**
 *
 * @author SAMILA
 */
public class LogReader {
    
    public static void main(String []args)
    {
        try {
            new UserInterface().setVisible(true);
        } catch (Exception ex) {
            Logger.getLogger(LogReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
