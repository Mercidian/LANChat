import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Date;

import networking.Announce;
import networking.ChannelUpdate;
import networking.Client;
import networking.Join;
import networking.Message;
import networking.Peer;
import networking.Refuse;
import networking.Server;
import networking.ServerAnnouncer;
import networking.ServerFinder;
import networking.TextMessage;


public class Testing {


    public static class MyServerListener implements ServerFinder.ServerListener {
        public MyServerListener() {

        }
        @Override
        public void serverFound(SocketAddress address, String serverName, int numMembers, boolean needsPassword) {
            System.out.println("ServerListener: Server found at " + address + " named " + serverName + ", " + numMembers + " members, needsPassword=" + needsPassword);
        }
    }



	public static void main(String[] args) throws InterruptedException, IOException {

		SocketAddress addr1 = new InetSocketAddress("192.168.2.0", 49000);
		SocketAddress addr2 = new InetSocketAddress("192.168.2.0", 48000);

		System.out.println(addr1.equals(addr2));
		
		// Setup a server
		Peer peer = new Server(64000, "name", "pw");
        peer.setDaemon(true);
        peer.start();
        
        /*
        // Setup a client
        Peer peer = new Client("192.168.111.102", 64000, "name", "pw");
        peer(true);
        peer();*/
        
        Message[] messages = {new TextMessage("rob", "message1", "password"),
        					  new TextMessage("will", "message2", "password"),
        					  new ChannelUpdate("rob", "message2", new Date()),
        					  new Announce("server1", ((InetSocketAddress)peer.getLocalSocketAddress()).getHostName(), 64000, 0, true),
        					  new Join("rob", "pw", "127.0.0.1"),
        					  new Join("will", "pw", "127.0.0.1"),
        					  new Refuse("Invalid Password")};

        // Send some messages to ourself
        for(Message message : messages) {
        	peer.send(message);
        }

        String mAddr = "230.0.0.1";
        int mPort = 45000;

        MyServerListener listener = new MyServerListener();
        ServerFinder finder = new ServerFinder(mAddr, mPort);
        finder.addServerListener(listener);
        finder.start();

        /*
        Server server = new Server("Server1", 1600, false);
        ServerAnnouncer announcer = new ServerAnnouncer(mAddr, mPort, 1000, server);
        announcer.start();

        for(int i=0; i<10; i++) {
        	Thread.sleep(200);
        	server.setNumMembers(server.getNumMembers() + 1);
        }
        */
	}

}
