package rover.server;

import rover.Main;
import rover.ui.PidServerFrame;

import javax.swing.SwingUtilities;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class PIDServer implements Runnable {
	public volatile boolean active = true;
	private static final int PORT = 5000;      // for this server
	private static final int BUFSIZE = 1024;   // max size of a message
	
	private  PidServerFrame frame;
	private Client client;
	InetAddress clientAddr = null;
	int clientPort = 0;
	private DatagramSocket serverSock;
	public PIDServer(final PidServerFrame frame) {
	    try {  // try to create a socket for the server
	        serverSock = new DatagramSocket(PORT);
	        this.frame = frame;
	      }
	      catch(SocketException se)
	      {  System.out.println(se);
	         System.exit(1);
	      }
	      catch(IOException ex) {
	    	System.out.println(ex);
	        System.exit(1);
	      }
	}
	
	public void run() {
		while(active) {
			waitForPackets();
		}
		serverSock.close();
	}
	
	
	  private void waitForPackets()
	  // wait for a packet, process it, repeat
	  {
	    DatagramPacket receivePacket;
	    byte data[];

	    //System.out.println("Ready for client messages");
	    try {
	      
	        data = new byte[BUFSIZE];  // set up an empty packet
	        receivePacket = new DatagramPacket(data, data.length);
	        serverSock.receive( receivePacket );  // wait for a packet

	        // extract client address, port, message
	        if(clientAddr == null) {
	        	clientAddr = receivePacket.getAddress();
	        	clientPort = receivePacket.getPort();
	        }
	        final String clientMsg = new String( receivePacket.getData(), 0,
	                           receivePacket.getLength() ).trim();
		    SwingUtilities.invokeLater(new Runnable() {
			      public void run() {
			    	  frame.setCourseText(clientMsg);
			      }
			    });
	        //System.out.println("Received: " + clientMsg);

	        //processClient(clientMsg, clientAddr, clientPort);
	      
	    }
	    catch(IOException ioe)
	    {  System.out.println(ioe);  }
	  }  // end of waitForPackets()
	  
	  private void processClient(String msg, InetAddress addr, int port) {
		  
	  }
	  private void sendMessage(String msg, InetAddress addr, int port)
	  // send message back to the client
	  {
	    try {
	      DatagramPacket sendPacket =
	          new DatagramPacket( msg.getBytes(), msg.length(), 
	   						addr, port);
	      serverSock.send( sendPacket );
	    }
	    catch(IOException ioe)
	    {  System.out.println(ioe);  }
	  } // end of sendMessage()
	  
	  public void closeServer() {active = false;}
	  public void sendCmd(int cmd) {
		  String msg = Integer.toString(cmd);
		  if(clientAddr == null) {
			  System.out.println("Client address is null");
		  }
		  else sendMessage(msg, clientAddr, clientPort);
	  }
}
