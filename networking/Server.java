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
		//TODO: Check client is valid and return false if invalid
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
	/*
	public void send(Message message) throws IOException{
		//Send to all clients
		if(message.getType()==MessageType.CHANNEL_UPDATE){
			Iterator<ClientInfo> itr = clientList.iterator();
			while( itr.hasNext() ){
				//TODO: Form packet with msg and send to client
				sendTo(message, itr.next().clientSocket);
			}
		}
	}
	*/
	
	public void receive(Message message) throws IOException{
	//Receive message from client
		//send(message);
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

        //System.out.println("Peer: received a " + message.getType());
        switch(message.getType()) {
	        case TEXT_MESSAGE:
	        	try{
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
				}
				else{
					try {
						super.sendTo(new Refuse("Invalid Password"), new InetSocketAddress(m.clientAddress, m.clientPort));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}     
				}
				System.out.println("clients: " + getNumMembers());
				break;
			case REFUSE:
	        	break;
            default:
            	System.out.println("Peer: received a " + message.getType());
        }

    }

}
