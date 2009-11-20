package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;

public class Server extends Peer{
	
	private ArrayList<ClientInfo> clientList;
	protected int serverPort;
	protected String serverName;
	protected String password;
	protected boolean needsPassword;
	
	public Server(int serverPort, String serverName, String password) throws SocketException {
		super(serverPort);
		clientList = new ArrayList<ClientInfo>();
		this.serverPort=serverPort;
		this.serverName=serverName;
		this.password=password;
		needsPassword=password=="";
	}

//Client List Stuff
	public boolean addClient(ClientInfo client){
		System.out.println(client.clientHandle + " " + client.clientAddress + " " + client.clientPort);
		clientList.add(client);
		return true;
	}
	
	public boolean removeClient(ClientInfo client){
		int i = clientList.indexOf(client);
		if(i<0) return false;
		else clientList.remove(i);
		return true;
	}
	
	public int getNumMembers(){
		return clientList.size();
	}
	
	public void setPassword(String password){
		this.password = password;
	}

//Server Send/Recv Stuff
	@Override
	public void send(Message message) throws IOException{
		//Send to all clients
		if(message.getType()==MessageType.TEXT_MESSAGE){
			Iterator<ClientInfo> itr = clientList.iterator();
			int i=0;
			while( itr.hasNext() ){
				sendTo(message, itr.next().clientSocket);
			}
		}
	}
	
	public void receive(Message message) throws IOException{
	//Receive message from client
		this.send(message);
		String s = msgParse(message);
		display(s);
	}
	
	public void createServerWindow(){
		
	}
	
	private static String msgParse(Message message){
		//TODO: format msg properly
		TextMessage m = (TextMessage)message;
		String s = (Time.time() + " " + m.clientHandle + " " + m.message);
		return s;
	}
	
	public void display(String message){
		//TODO: send formatted msg to display
		System.out.println(message);
	}
	
	@Override
	protected void handleMessage(Message message) {
        switch(message.getType()) {
	        case TEXT_MESSAGE:
	        	try{
	        		System.out.println("text");
	        		receive(message);
	        	} catch(IOException e){
	        		
	        	}
	        	break;
	        case CHANNEL_UPDATE:
	        	break;
	        case ANNOUNCE:
	        	break;
			case JOIN:
				Join m = (Join)message;
				if(m.password.equals(this.password)){
					addClient(new ClientInfo(m.clientHandle, m.clientAddress, m.clientPort) );
					System.out.println("clients: " + getNumMembers());
				}
				else{
					try {
						super.sendTo(new Refuse("Invalid Password"), new InetSocketAddress(m.clientAddress, m.clientPort));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}     
				}
				break;
			case REFUSE:
	        	break;
            default:
            	System.out.println("Peer: received a " + message.getType());
        }

    }

}
