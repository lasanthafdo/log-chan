package org.logchan.rules;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

import javax.rules.ConfigurationException;
import javax.rules.InvalidRuleSessionException;
import javax.rules.RuleExecutionSetNotFoundException;
import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.RuleServiceProviderManager;
import javax.rules.RuleSessionCreateException;
import javax.rules.RuleSessionTypeUnsupportedException;
import javax.rules.StatefulRuleSession;
import javax.rules.StatelessRuleSession;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;
import javax.rules.admin.RuleExecutionSetRegisterException;

public class LogRuleEngine {
	// The rule service provider URI as defined by the reference implementation.

	private static final String RULE_SERVICE_PROVIDER = "org.jcp.jsr94.jess";

	private RuleServiceProvider serviceProvider;
	private RuleAdministrator ruleAdministrator;
	private RuleRuntime ruleRuntime;
	private List<Object> input;

	public LogRuleEngine() {
		init();
	}

	private void init() {
		try {
			// Load the rule service provider of the reference implementation.
			// Loading this class will automatically register this provider with
			// the provider manager.
			Class.forName("org.jcp.jsr94.jess.RuleServiceProviderImpl");

			serviceProvider = RuleServiceProviderManager
					.getRuleServiceProvider(RULE_SERVICE_PROVIDER);
			ruleAdministrator = serviceProvider.getRuleAdministrator();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private RuleExecutionSet getRulesFromFile(String resourceName) {
		RuleExecutionSet res = null;
		try {
			InputStream inStream = RuleManager.class
					.getResourceAsStream(resourceName);
			// Parse the rule set
			res = ruleAdministrator.getLocalRuleExecutionSetProvider(null)
					.createRuleExecutionSet(inStream, null);
			inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RuleExecutionSetCreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;
	}

	private void registerRuleExecutionSet(RuleExecutionSet res) {
		try {
			String uri = res.getName();
			ruleAdministrator.registerRuleExecutionSet(uri, res, null);
		} catch (RuleExecutionSetRegisterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setInput(List<Object> input) {
		this.input = input;
	}

	public List<Object> processRulesFromResource(String resourceName) {

		List<Object> results = null;
		try {
			RuleExecutionSet res1 = getRulesFromFile(resourceName);
			registerRuleExecutionSet(res1);
			ruleRuntime = serviceProvider.getRuleRuntime();

			results = executeStatelessRules(res1);
		} catch (NoClassDefFoundError e) {
			if (e.getMessage().indexOf("JessException") != -1) {
				System.err.println("Error: The RI Jess could not be found.");
			} else {
				System.err.println("Error: " + e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	public List<Object> executeStatelessRules(RuleExecutionSet res) {
		List<Object> results = null;
		try {
			StatelessRuleSession statelessRuleSession = (StatelessRuleSession) ruleRuntime
					.createRuleSession(res.getName(), new HashMap(),
							RuleRuntime.STATELESS_SESSION_TYPE);
			results = statelessRuleSession.executeRules(input);
			statelessRuleSession.release();
		} catch (RuleSessionTypeUnsupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuleSessionCreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuleExecutionSetNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRuleSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return results;
	}

	public List<Object> executeStatefulRuleSession(RuleExecutionSet res) {
		List<Object> results = null;

		try {
			StatefulRuleSession statefulRuleSession = (StatefulRuleSession) ruleRuntime
					.createRuleSession(res.getName(), new HashMap(),
							RuleRuntime.STATEFUL_SESSION_TYPE);
			statefulRuleSession.addObjects(input);

			statefulRuleSession.executeRules();
			results = statefulRuleSession.getObjects();
			statefulRuleSession.release();

		} catch (RuleSessionTypeUnsupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuleSessionCreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuleExecutionSetNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRuleSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return results;
	}

}
