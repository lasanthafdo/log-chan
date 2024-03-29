package org.logchan.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.RuleServiceProviderManager;
import javax.rules.StatelessRuleSession;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;

import org.logchan.model.SourceApplication;
import org.logchan.model.WebServerLog;
import org.logchan.rules.RuleManager;

public class RuleExample {

	// The rule service provider URI as defined by the reference implementation.

	private static final String RULE_SERVICE_PROVIDER = "org.jcp.jsr94.jess";

	/**
	 * 
	 * Main entry point.
	 */

	public void runExample() {

		try {

			// Load the rule service provider of the reference implementation.

			// Loading this class will automatically register this provider with
			// the

			// provider manager.

			Class.forName("org.jcp.jsr94.jess.RuleServiceProviderImpl");

			// Get the rule service provider from the provider manager.

			RuleServiceProvider serviceProvider =

			RuleServiceProviderManager
					.getRuleServiceProvider(RULE_SERVICE_PROVIDER);

			// Get the rule administrator.

			RuleAdministrator ruleAdministrator = serviceProvider
					.getRuleAdministrator();

			System.out.println("\nAdministration API\n");

			System.out.println("Acquired RuleAdministrator: "
					+ ruleAdministrator);

			// Get an input stream to a test XML ruleset.

			// This rule execution set is part of the TCK.

			InputStream inStream = RuleManager.class
					.getResourceAsStream("/org/logchan/rules/size_rules.xml");

			System.out.println("Acquired InputStream to RI size_rules.xml: "
					+ inStream);

			// Parse the ruleset from the XML document.

			RuleExecutionSet res1 =

			ruleAdministrator.getLocalRuleExecutionSetProvider(

			null).createRuleExecutionSet(inStream, null);

			inStream.close();

			System.out.println("Loaded RuleExecutionSet: " + res1);

			// Register the rule execution set.

			String uri = res1.getName();

			ruleAdministrator.registerRuleExecutionSet(uri, res1, null);

			System.out.println("Bound RuleExecutionSet to URI: " + uri);

			// Get a RuleRuntime and invoke the rule engine.

			System.out.println("\nRuntime API\n");

			RuleRuntime ruleRuntime = serviceProvider.getRuleRuntime();

			System.out.println("Acquired RuleRuntime: " + ruleRuntime);

			// Create a statelessRuleSession.

			StatelessRuleSession statelessRuleSession =

			(StatelessRuleSession) ruleRuntime.createRuleSession(uri,

			new HashMap(), RuleRuntime.STATELESS_SESSION_TYPE);

			System.out.println("Got Stateless Rule Session: "
					+ statelessRuleSession);

			// Create a customer as specified by the TCK documentation.

			// Then call executeRules on the input objects.

			WebServerLog webLog = new WebServerLog("test.log");

			webLog.setFileSize(5000);
			webLog.setLineCount(3000);

			// Create an invoice as specified by the TCK documentation.

			SourceApplication app = new SourceApplication("Apache Web Server");

			app.setStandardLogRate(2000);

			// Create an input list.

			List input = new ArrayList();

			input.add(webLog);
			input.add(app);

			// Print the input.

			System.out.println("Calling rule session with the following data");

			System.out.println("Log file line count: " + webLog.getLineCount());

			System.out.println(app.getApplicationName() + " log rate: " + app.getStandardLogRate());

			// Execute the rules without a filter.

			List results = statelessRuleSession.executeRules(input);

			System.out
					.println("Called executeRules on Stateless Rule Session: " +

					statelessRuleSession);

			System.out.println("Result of calling executeRules: "
					+ results.size() +

					" results.");

			// Loop over the results.

			Iterator itr = results.iterator();

			while (itr.hasNext()) {

				Object obj = itr.next();

				if (obj instanceof WebServerLog)

					System.out.println("Log file size status: " +

					((WebServerLog) obj).getLogSizeStatus());

				if (obj instanceof SourceApplication)
					System.out.println(((SourceApplication) obj).getApplicationName() + " log rate: " 
							+ ((SourceApplication) obj).getStandardLogRate() + " rating: " 
							+ ((SourceApplication) obj).getApplicationRating());

			}

			// Release the session.

			statelessRuleSession.release();

			System.out.println("Released Stateless Rule Session.");
			
		} catch (NoClassDefFoundError e) {

			if (e.getMessage().indexOf("JessException") != -1) {

				System.err.println("Error: The RI Jess could not be found.");

			} else {

				System.err.println("Error: " + e.getMessage());

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}
