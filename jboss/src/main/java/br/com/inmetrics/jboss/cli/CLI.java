package br.com.inmetrics.jboss.cli;

import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;


import org.jboss.as.cli.CommandContext;

import org.jboss.as.cli.CommandFormatException;
import org.jboss.as.cli.CommandLineException;
import org.jboss.dmr.ModelNode;
import br.com.inmetrics.connections.ConnectionHandler;


public class CLI {
	
	private CommandContext ctx;
	
    public CLI(ConnectionHandler ch) {
    	this.ctx = ch.connect();
    }
    
    public CommandContext getCtx(){
    	return this.ctx;
    }
    
    public CommandContext getCommandContext() {
        return this.ctx;
    }

    /**
     * Execute a CLI command.  This can be any command that you might execute on the CLI command line, including both
     * server-side operations and local commands such as 'cd' or 'cn'.
     *
     * @param cliCommand A CLI command.
     * @return A result object that provides all information about the execution of the command.
     */
    public Result cmd(String cliCommand) {
        try {
            ModelNode request = ctx.buildRequest(cliCommand);
            ModelNode response = ctx.getModelControllerClient().execute(request);
            return new Result(cliCommand, request, response);
        } catch (CommandFormatException cfe) {
            // if the command can not be converted to a ModelNode, it might be a local command
            try {
            	ctx.handle(cliCommand);
                return new Result(cliCommand, getCommandContext().getExitCode());
            } catch (CommandLineException cle) {
                throw new IllegalArgumentException("Error handling command: " + cliCommand, cle);
            }
        } catch (IOException ioe) {
            throw new IllegalStateException("Unable to send command " + cliCommand + " to server.", ioe);
        }
    }

    /**
     * The Result class provides all information about an executed CLI command.
     */
    public class Result {
        private String cliCommand;
        private ModelNode request;
        private ModelNode response;

        private boolean isSuccess = false;
        private boolean isLocalCommand = false;

        Result(String cliCommand, ModelNode request, ModelNode response) {
            this.cliCommand = cliCommand;
            this.request = request;
            this.response = response;
            this.isSuccess = response.get("outcome").asString().equals("success");
        }

        Result(String cliCommand, int exitCode) {
            this.cliCommand = cliCommand;
            this.isSuccess = exitCode == 0;
            this.isLocalCommand = true;
        }

        /**
         * Return the original command as a String.
         * @return The original CLI command.
         */
        public String getCliCommand() {
            return this.cliCommand;
        }

        /**
         * If the command resulted in a server-side operation, return the ModelNode representation of the operation.
         *
         * @return The request as a ModelNode, or <code>null</code> if this was a local command.
         */
        public ModelNode getRequest() {
            return this.request;
        }

        /**
         * If the command resulted in a server-side operation, return the ModelNode representation of the response.
         *
         * @return The server response as a ModelNode, or <code>null</code> if this was a local command.
         */
        public ModelNode getResponse() {
            return this.response;
        }

        /**
         * Return true if the command was successful.  For a server-side operation, this is determined by the outcome of the
         * operation on the server side.
         *
         * @return <code>true</code> if the command was successful, <code>false</code> otherwise.
         */
        public boolean isSuccess() {
            return this.isSuccess;
        }

        /**
         * Return true if the command was only executed locally and did not result in a server-side operation.
         *
         * @return <code>true</code> if the command was only executed locally, <code>false</code> otherwise.
         */
        public boolean isLocalCommand() {
            return this.isLocalCommand;
        }
    }
}

