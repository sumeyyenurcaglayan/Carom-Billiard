import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	private Socket socket; 
    private ServerSocket server;

    OnlineListener onlineListener;
    
    private ObjectOutputStream output;
    private ObjectInputStream input;
    
    public Server() throws IOException {
    	server = new ServerSocket(3586);
    }
    
    public void startServer(){
    	try {
			socket = server.accept();
			System.out.println("Client accepted => " + socket);
			
			output = new ObjectOutputStream(socket.getOutputStream());
			output.writeInt(0);
			output.flush();
			
			input = new ObjectInputStream(socket.getInputStream());
			int dummyInput = input.readInt();
			System.out.println("dummyInput = " + dummyInput);
			
			onlineListener.onConnectionSuccess();
			
			listen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void listen() {
        while(true) {
            try {
                int check = input.readInt();
                if(check != 9)
                    continue;
                
                onlineListener.onDataReceived(input.readDouble(), input.readDouble());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
   
    public void sendShoot(double angle, double force) throws IOException {
        output.writeInt(9);
        output.writeDouble(angle);
        output.writeDouble(force);
        output.flush();
        
        
    }
    
    public void closeConnections() throws IOException {
        socket.close();
        server.close();
        output.close();
        input.close();
    }

	public void setOnlineListener(OnlineListener onlineListener) {
		this.onlineListener = onlineListener;
	}
}
