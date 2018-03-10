package br.com.inmetrics.jboss.cli;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jboss.dmr.ModelNode;

import br.com.inmetrics.jboss.cli.CLI.Result;

public class ServerInformation {
	
	private Result result;
	private ModelNode response;
	
	public ServerInformation() {
	}

	public void startTime(CLI cli){
		result = cli.cmd(":read-attribute(name=start-time)");  
		response = result.getResponse();  
		long startTime = response.get("result").asLong();
		System.out.println("O servidor foi iniciado em " + (convertTime(startTime)));
	}
	
	public void uptime(CLI cli) {
		result = cli.cmd(":read-attribute(name=uptime)");
		response = result.getResponse();
		String serverUptime = response.get("result").asString();
		System.out.println("Uptime " + serverUptime + " ms");
	}
	
	public String currentTime() {
		return convertTime(System.currentTimeMillis());
	}
	
	public static String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
        return format.format(date);
    }
}
