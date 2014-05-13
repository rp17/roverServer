package rover.websocket;

import rover.ui.PidServerFrame;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

@ClientEndpoint
public class PCCamera implements Runnable {

	Session userSession = null;

    int nb = 0;
    Thread t;
    
	public static int HEADER_SIZE = 5;
	public static int DATAGRAM_MAX_SIZE = 1450;
	public static int DATA_MAX_SIZE = DATAGRAM_MAX_SIZE - HEADER_SIZE;
	
	
	public PCCamera() {
		try 
        {
        	t = new Thread(this);
        	t.start();
        } 
        catch (Exception e){e.printStackTrace();}
	}
	
    @OnOpen
    public void onOpen(Session userSession) throws IOException {
        this.userSession = userSession;
        String str = "Started PC Camera";
        sendMessage(str.getBytes());
        System.out.println("Cam thread waiting...");
    }
 
    @OnClose
    public void onClose(Session userSession, CloseReason reason) throws IOException {
    	System.out.println("Updater session closing " + userSession.getId() + " " + reason);
        this.userSession.close();
    	this.userSession = null;
    }
 
    @OnMessage
    public void onMessage(ByteBuffer message) {
    	handleConnection_UDP(message);
		System.out.println("onmessage received: ");    	 	
    }

    public void sendMessage(byte[] message) {
    	ByteBuffer bytebuf = ByteBuffer.wrap(message);
        this.userSession.getAsyncRemote().sendBinary(bytebuf);
    }
	
	
	public void run() {
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
			container.connectToServer(this, new URI("ws://wildfly8websocket-calvincarter.rhcloud.com:8000/camera"));
		} catch (DeploymentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 	 
	
	public void handleConnection_UDP(ByteBuffer buf) 
    {
		// Retrieve all bytes in the buffer
		buf.clear();
		byte[] data = new byte[buf.capacity()];
		buf.get(data, 0, data.length);
		
		int current_frame = -1;
		int slicesStored = 0;
		byte[] imageData = null;

		try 
		{		         	
			//while (true) 
			//{				
				int frame_nb = (int)data[0];
				int nb_packets = (int)data[1];
				int packet_nb = (int)data[2];
				int size_packet = (int) ((data[3] & 0xff) << 8 | (data[4] & 0xff)); 

				//if((packet_nb==0) && (current_frame != frame_nb))
				//{
					current_frame = frame_nb;
					slicesStored = 0;				
					imageData = new byte[nb_packets * DATA_MAX_SIZE];
				//}

				//if(frame_nb == current_frame)
				//{
						System.arraycopy(data, HEADER_SIZE, imageData, packet_nb * DATA_MAX_SIZE, size_packet);
						slicesStored++;				
				//}

				/* If image is complete display it */
				//if (slicesStored == nb_packets) 
				//{	
					System.out.println("display image");
					
					ByteArrayInputStream is = new ByteArrayInputStream(imageData);

			        try {
			        	PidServerFrame.bImageFromConvert = ImageIO.read(is);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}


			        
			        
			        SwingUtilities.invokeLater(new Runnable() {
				        public void run() {
				        		PidServerFrame.f.validate();
				        		PidServerFrame.f.repaint();
				        	//}
				      }
			        });

			 	//}
			//}
		} catch (Exception e) 
		{
			e.printStackTrace();
		} 
    }
}
