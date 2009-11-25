package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;

public class Server extends Peer{
	
	private ArrayList<ClientInfo> clientList;
	String name;
	String password;
	
	public Server(int serverPort, String name, String password) throws SocketException {
		super(serverPort);
		clientList = new ArrayList<ClientInfo>();
		this.name=name;
		this.password=password;
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
	
	public void setPassword(String password){
		this.password = password;
	}

//Server Send/Recv Stuff
	public void send(Message message) throws IOException{
		//Send to all clients
		if(message.getType()==MessageType.CHANNEL_UPDATE){
			Iterator<ClientInfo> itr = clientList.iterator();
			while( itr.hasNext() ){
				//TODO: Form packet with msg and send to client
				sendTo(message, itr.next().clientAddress);
			}
		}
	}
	
	public void receive(Message message) throws IOException{
		//Receive message from client
		send(message);
		String s = msgParse(message);
		display(s);
	}
	
	public void createServerWindow(){
		
	}
	
	private static String msgParse(Message message){
		//TODO: format msg properly
		TextMessage m = (TextMessage)message;
		String s = (Time.time() + m.clientHandle + " " + m.message);
		return s;
	}
	
	public void display(String message){
		//TODO: send formatted msg to display
		System.out.println(message);
	}
	
	@Override
	protected void handleMessage(Message message) {

        System.out.println("Peer: received a " + message.getType());
        switch(message.getType()) {
        	/*
			case JOIN:
				if(message.password == this.password)
					ClientInfo client = new ClientInfo();
					client.clientAddress = message.ipAddr;
					addClient(client);
				else
					Refuse msg = new Refuse("");
					super.sendTo(message.ipAddr, msg);
        	*/     
            case TEXT_MESSAGE:
            	try{
            		receive(message);
            	} catch(IOException e){
            		
            	}
                //TextMessage txt = (TextMessage)message;
                //System.out.println(String.format("[TextMessage] %s: %s", txt.clientHandle, txt.message));
            default:
            	System.out.println("Peer: received a " + message.getType());
        }

    }

}
