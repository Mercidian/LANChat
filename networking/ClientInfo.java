package networking;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class ClientInfo {
	//DataType
	public String clientHandle;
	public String clientAddress;
	public int clientPort;
	public InetSocketAddress clientSocket;
	
	public ClientInfo(String clientHandle, String clientAddress, int clientPort){
		super();
		this.clientHandle = clientHandle;
		this.clientAddress = clientAddress;
		this.clientPort = clientPort;
		clientSocket = new InetSocketAddress(clientAddress, clientPort);
	}
}
