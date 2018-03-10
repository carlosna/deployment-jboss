package br.com.inmetrics.connections;

import org.jboss.as.cli.CommandContext;

public interface ConnectionHandler {
	
	public CommandContext connect();
	public void disconnect(CommandContext ctx);

}
