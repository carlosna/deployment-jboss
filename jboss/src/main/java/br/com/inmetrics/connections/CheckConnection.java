package br.com.inmetrics.connections;

import org.jboss.as.cli.CommandContext;

public class CheckConnection {
	
	public CheckConnection() {
	}
 
	public boolean checkAlreadyConnected(CommandContext ctx) {
        if (ctx != null)
        	return true;
        //throw new IllegalStateException("Already connected to server.");
        return false;
    }
    
//	public void checkNotConnected(CommandContext ctx) {
//        if (ctx == null) throw new IllegalStateException("Not connected to server.");
//        if (ctx.isTerminated()) throw new IllegalStateException("Session is terminated.");
//    }
}
