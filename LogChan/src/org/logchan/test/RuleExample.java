package org.logchan.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.RuleServiceProviderManager;
import javax.rules.StatefulRuleSession;
import javax.rules.StatelessRuleSession;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;

import org.jcp.jsr94.tck.model.Customer;
import org.jcp.jsr94.tck.model.Invoice;


public class RuleExample {

   // The rule service provider URI as defined by the reference implementation.

   private static final String RULE_SERVICE_PROVIDER = "org.jcp.jsr94.jess";



   /**

    * Main entry point.

    */

   public static void main(String[] args) {

      try {

         // Load the rule service provider of the reference implementation.

         // Loading this class will automatically register this provider with the

         // provider manager.

         Class.forName("org.jcp.jsr94.jess.RuleServiceProviderImpl");



         // Get the rule service provider from the provider manager.

         RuleServiceProvider serviceProvider = 

             RuleServiceProviderManager.getRuleServiceProvider(RULE_SERVICE_PROVIDER);



         // Get the rule administrator.

         RuleAdministrator ruleAdministrator = serviceProvider.getRuleAdministrator();

         System.out.println("\nAdministration API\n");

         System.out.println("Acquired RuleAdministrator: " + ruleAdministrator);



         // Get an input stream to a test XML ruleset.

         // This rule execution set is part of the TCK.

         InputStream inStream =

             org.jcp.jsr94.tck.model.Customer.class.getResourceAsStream(

             "/org/jcp/jsr94/tck/tck_res_1.xml");

         System.out.println("Acquired InputStream to RI tck_res_1.xml: " + inStream);



         // Parse the ruleset from the XML document.

         RuleExecutionSet res1 =

            ruleAdministrator.getLocalRuleExecutionSetProvider(

            null).createRuleExecutionSet( inStream, null );

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



         System.out.println("Got Stateless Rule Session: " + statelessRuleSession);



         // Create a customer as specified by the TCK documentation.

         // Then call executeRules on the input objects.

         Customer inputCustomer = new Customer("test");

         inputCustomer.setCreditLimit(5000);



         // Create an invoice as specified by the TCK documentation.

         Invoice inputInvoice = new Invoice("Invoice 1");

         inputInvoice.setAmount(2000);



         // Create an input list.

         List input = new ArrayList();

         input.add(inputCustomer);

         input.add(inputInvoice);



         // Print the input.

         System.out.println("Calling rule session with the following data");

         System.out.println("Customer credit limit input: " +

              inputCustomer.getCreditLimit());

         System.out.println(inputInvoice.getDescription() +

              " amount: " + inputInvoice.getAmount() +

              " status: " + inputInvoice.getStatus());



         // Execute the rules without a filter.

         List results = statelessRuleSession.executeRules(input);



         System.out.println("Called executeRules on Stateless Rule Session: " +

              statelessRuleSession);



         System.out.println("Result of calling executeRules: " + results.size() +

              " results.");



         // Loop over the results.

         Iterator itr = results.iterator();

         while(itr.hasNext()) {

            Object obj = itr.next();

            if (obj instanceof Customer)

                System.out.println("Customer credit limit result: " +

                 ((Customer) obj).getCreditLimit());

            if (obj instanceof Invoice)

                System.out.println(((Invoice) obj).getDescription() +

                " amount: " + ((Invoice) obj).getAmount() + " status: " +

                ((Invoice) obj).getStatus());

         }



         // Release the session.

         statelessRuleSession.release();

         System.out.println("Released Stateless Rule Session.");



         // Create a statefulRuleSession.

         StatefulRuleSession statefulRuleSession = 

             (StatefulRuleSession) ruleRuntime.createRuleSession(uri,

              new HashMap(), RuleRuntime.STATEFUL_SESSION_TYPE);



         System.out.println("Got Stateful Rule Session: " + statefulRuleSession);



         // Add another invoice.

         Invoice inputInvoice2 = new Invoice("Invoice 2");

         inputInvoice2.setAmount(1750);

         input.add(inputInvoice2);

         System.out.println("Calling rule session with the following data");

         System.out.println("Customer credit limit input: " +

              inputCustomer.getCreditLimit());

         System.out.println(inputInvoice.getDescription() +

              " amount: " + inputInvoice.getAmount() +

              " status: " + inputInvoice.getStatus());

         System.out.println(inputInvoice2.getDescription() +

              " amount: " + inputInvoice2.getAmount() +

              " status: " + inputInvoice2.getStatus());



         // Add an object to the statefulRuleSession.

         statefulRuleSession.addObjects(input);

         System.out.println("Called addObject on Stateful Rule Session: "+

              statefulRuleSession);



         statefulRuleSession.executeRules();

         System.out.println("Called executeRules");



         // Extract the objects from the statefulRuleSession.

         results = statefulRuleSession.getObjects();



         System.out.println("Result of calling getObjects: " + results.size() +

              " results.");



         // Loop over the results.

         itr = results.iterator();

         while(itr.hasNext()) {

            Object obj = itr.next();

            if (obj instanceof Customer)

               System.out.println("Customer credit limit result: " +

                ((Customer) obj).getCreditLimit());

            if (obj instanceof Invoice)

	       System.out.println(((Invoice) obj).getDescription() +

                    " amount: " + ((Invoice) obj).getAmount() +

                    " status: " + ((Invoice) obj).getStatus());

         }



         // Release the statefulRuleSession.

         statefulRuleSession.release();

         System.out.println( "Released Stateful Rule Session." );

         System.out.println();

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
