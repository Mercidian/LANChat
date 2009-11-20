package networking;

import java.io.IOException;

public class Client extends Peer{

	String nickname;
	String password;
	boolean hasPassword;
	
	public Client(String serverAddress, int serverPort, String nickname, String password) throws IOException{
		super(serverAddress, serverPort);
		this.nickname = new String(nickname);
		this.password = new String(password);
	}
	
	public void hasPassword(boolean b){
		hasPassword = b;
	}
	
	public void send(String message) throws IOException{
		Message m = new TextMessage(nickname, message, password);
		send(m);
	}
	
	public static void receive(Message message){
		if(message.getType()==MessageType.CHANNEL_UPDATE){
			String s = MsgParse(message);
			ClientDisplay(s);
		}
	}
	
	public void createClientWindow(){
		
	}

	private static String MsgParse(Message message){
		//TODO: format msg properly
		ChannelUpdate m = (ChannelUpdate)message;
		String s = (m.date + " " + m.clientHandle + " " + m.message);
		return s;
	}
	
	private static void ClientDisplay(String message){
		//TODO: GUI display msg in client window
		System.out.println(message);
	}
	
	public void run() {
		System.out.println("client");
		super.run();
	}
	
	@Override
	protected void handleMessage(Message message) {
		//System.out.println("Peer: received a " + message.getType());
        switch(message.getType()) {
        	/*
        	case REFUSE:
        		throw new ServerRefusedException();
        	*/
            case CHANNEL_UPDATE:
            	try{
            		receive(message);
            	} catch(Exception e){
            		
            	}
                //TextMessage txt = (TextMessage)message;
                //System.out.println(String.format("[TextMessage] %s: %s", txt.clientHandle, txt.message));
            default:
            	System.out.println("Peer: received a " + message.getType());
        }
	}

}
