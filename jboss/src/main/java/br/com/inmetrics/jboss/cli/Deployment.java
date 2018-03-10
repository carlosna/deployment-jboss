package br.com.inmetrics.jboss.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jboss.as.cli.CommandContext;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;

//import br.com.inmetrics.jboss.cli.CLI.Result;

public class Deployment {
	
	private final static Logger log = Logger.getLogger(Deployment.class.getName());
//	private Result result;
//	private ModelNode response;
	List<String> lista = new ArrayList<String>();
	
	public Deployment(){ }
	
	public void doDeploy(CLI cli, String path){
		log.info("Iniciando o deploy " + path + ", Keep Calm!");
		try {
			cli.cmd("deploy " + path);
		}catch (RuntimeException re){
			throw new RuntimeException("Não foi possível efetuar o deploy. Verifique o pacote" , re);
		}
	}
	
	public void undeploy(CLI cli, String path){
		log.info("Realizando undeploy, porque já existe um pacote com o mesmo nome " + path + ", Keep Calm!");
		cli.cmd("undeploy " + path);
	}

	public boolean deployExist(CLI cli, CommandContext ctx, String name) throws Exception  {

	ModelNode operation = new ModelNode( );
	operation.get("address").add("deployment", name );
	operation.get("operation").set("read-resource");
//	operation.get("name" ).set( "name" );
	
	ModelControllerClient client = ctx.getModelControllerClient();
	ModelNode result = client.execute( operation );
	
	System.out.println(result.get("result").get("name").asString());
	
	if (result.get( "result" ).get("name").asString().equals(name))
		return true;

	return false;
	}
	 
}
