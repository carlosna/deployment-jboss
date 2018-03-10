package br.com.inmetrics.jboss.program;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


import br.com.inmetrics.connections.CheckConnection;
import br.com.inmetrics.connections.Connection;
import br.com.inmetrics.connections.ConnectionControllerPort;
import br.com.inmetrics.connections.ConnectionControllerPortUsernamePassword;
import br.com.inmetrics.connections.ConnectionHandler;
import br.com.inmetrics.jboss.cli.CLI;
import br.com.inmetrics.jboss.cli.Deployment;
import br.com.inmetrics.jboss.cli.ServerInformation;


public class App 
{
	private static String host;
	private static int port;
	private static Connection connection;
	private static String username;
	private static char[] password;
	private static ConnectionHandler ch;
	private static List<String> pacotes = new ArrayList<String>();
	private final static Logger log = Logger.getLogger(App.class.getName());

	public static void main(String[] args) throws Exception
    {	
		if(args.length == 0)
	    {
	        System.out.println("Usage: App.java host port or host port username password");
	        System.exit(0);
	    }
		
		host = args[0];
	    port = Integer.parseInt(args[1]);
		
		
		if (args.length == 4) {
			username = args[2];
			password = args[3].toCharArray();
			connection = new Connection(host, port, username, password);
			ch = new ConnectionControllerPortUsernamePassword(connection);
		} else {
			connection = new Connection(host, port, null, null);
			ch = new ConnectionControllerPort(connection);
		}		
		
    	log.info("Iniciando a conexão com o jboss-cli");
		CLI cli = new CLI(ch);
    	CheckConnection cc = new CheckConnection(); 
    	ServerInformation info = new ServerInformation();
    	
    	if (cc.checkAlreadyConnected(cli.getCtx()))
    		log.info("Conectado " + info.currentTime());
    	else
    		log.warning("Não possível estabelecer a conexão. Verifique com o administrador");
    	
    	if (cli.getCommandContext().isDomainMode()) {  
      		cli.cmd("cd /host=master/core-service=platform-mbean/type=runtime");  
    	} else {  
      		cli.cmd("cd /core-service=platform-mbean/type=runtime");
      		log.info("Modo standalone");
    	} 			
    	
    	initLoadPropertie();
    	Deployment deploy = new Deployment();

    	for (String pacote: pacotes) {
        	// pega o nome do pacote
    		File f = new File(pacote);
    		String name = f.getName().toLowerCase();
    		
    		if (deploy.deployExist(cli, cli.getCtx(), name))
    			deploy.undeploy(cli, name);
    		
			deploy.doDeploy(cli, pacote);
    	
    	}
    	log.info("Deploy finalizado " + info.currentTime());
		ch.disconnect(cli.getCtx());    	  
    }
	
	public static List<String> initLoadPropertie(){
    	Properties props = new Properties();
    	InputStream input = null;
    	String value;
    	
    	try {
    	    //load a properties file from class path, inside static method
    	    //props.load(App.class.getClassLoader().getResourceAsStream("multicanalpf.properties"));
    		input = new FileInputStream("deployment.properties");
    		props.load(input);
    		
    		String base = "source-package";
    		int index = 1;
    		
    		while(( value = (String) props.get(base + index)) != null ) 
    		{
    			pacotes.add(value);
    			index++;
    		}    		
    		
    	} 
    	catch (IOException ex) {
    	    ex.printStackTrace();
    	    log.warning("Problema ao tentar localizar arquivo de properties");
    	}
    	finally {
			if (input != null){
				try {
					input.close();
				} 
				catch (IOException ex){
					ex.printStackTrace();
				}
			}
		}
    	return pacotes;
	}
}
