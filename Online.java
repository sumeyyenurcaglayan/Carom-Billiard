import java.io.IOException;

public class Online {

	private Server server;
	private Client client;
	
	private boolean isReady;
	
	public Online() {
		server = null;
		client = null;
		
		try {
			server = new Server();
			System.out.println("server created");
		} catch (IOException e) {
			try {
				client = new Client();
				System.out.println("client created");
			} catch (Exception e1) {
				System.out.println("can not create client");
				e1.printStackTrace();
			}
		}
	}
	 
	public boolean isServer() {
		return server != null && client == null;
	}
	
	public boolean isReady() {
		return isReady;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}

	public Server getServer() {
		return server;
	}

	public Client getClient() {
		return client;
	}
	
}
