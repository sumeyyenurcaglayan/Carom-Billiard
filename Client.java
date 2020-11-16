import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	private Socket socket = null;

	private ObjectInputStream input = null;
	private ObjectOutputStream output = null;

	OnlineListener onlineListener;

	public Client() throws UnknownHostException, IOException {
		socket = new Socket("127.0.0.1", 3586);
	}

	public void startClient() {
		try {
			output = new ObjectOutputStream(socket.getOutputStream());
			output.writeInt(0);
			output.flush();

			input = new ObjectInputStream(socket.getInputStream());
			int dummyInput = input.readInt();
			System.out.println("dummyInput = " + dummyInput);
			
			onlineListener.onConnectionSuccess();
			
			listen();
		} catch (Exception e) {
			System.out.println("Error when trying to connect to the server => "
					+ e.getMessage());
			e.printStackTrace();
		}
	}

	public void closeConnections() throws IOException {
		input.close();
		output.close();
		socket.close();
	}

	public void sendShoot(double angle, double force) throws IOException {
		output.writeInt(9);
		output.writeDouble(angle);
		output.writeDouble(force);
		output.flush();
	}

	public void listen() {
		while (true) {
			try {
				int check = input.readInt();
				if (check != 9)
					continue;

				onlineListener.onDataReceived(input.readDouble(), input.readDouble());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setOnlineListener(OnlineListener onlineListener) {
		this.onlineListener = onlineListener;
	}
}
