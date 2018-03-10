package br.com.inmetrics.connections;

public class Connection {
	
	private String hostController;
	private int port;
	private String username;
	private char[] password;
	
	public Connection(String hostController, int port, String username, char[] password) {
		this.hostController = hostController;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	public String getHostController() {
		return hostController;
	}

	public int getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

	public char[] getPassword() {
		return password;
	}
}
