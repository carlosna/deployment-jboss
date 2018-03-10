package br.com.inmetrics.connections;

import org.jboss.as.cli.CliInitializationException;
import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandContextFactory;
import org.jboss.as.cli.CommandLineException;

public class ConnectionControllerPortUsernamePassword implements ConnectionHandler{
	
	private CommandContext ctx;
	private Connection connection;
	
	public ConnectionControllerPortUsernamePassword(Connection connection) {
		this.connection = connection;
	}

	public CommandContext connect() {
        try {
            ctx = CommandContextFactory.getInstance().newCommandContext(connection.getHostController(), 
            															connection.getPort(), 
            															connection.getUsername(), 
            															connection.getPassword());
            ctx.connectController();
        } catch (CliInitializationException e) {
            throw new IllegalStateException("Unable to initialize command context.", e);
        } catch (CommandLineException e) {
            throw new IllegalStateException("Unable to connect to controller.", e);
        }
        return ctx;
	}

	public void disconnect(CommandContext ctx) {
	     try {
	    //     cc.checkNotConnected(ctx);
	         ctx.terminateSession();
	     } 
	     finally {
	         ctx = null;
	     }
	 }
}

