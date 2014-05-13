package rover.websocket;

import rover.ui.PidServerFrame;

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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@ClientEndpoint
public class PCUpdater implements Runnable {

	Session userSession = null;
	private  PidServerFrame frame;
	public volatile boolean active = true;
	
	public PCUpdater(final PidServerFrame frame) {
		this.frame = frame;
	}
	
    @OnOpen
    public void onOpen(Session userSession) throws IOException {
        this.userSession = userSession;
        sendMessage("Started PC Updater");
    }
 
    @OnClose
    public void onClose(Session userSession, CloseReason reason) throws IOException {
    	System.out.println("Updater session closing " + userSession.getId() + " " + reason);
        this.userSession.close();
    	this.userSession = null;
    }
 
    @OnMessage
    public void onMessage(final String message) {
        if (message.equals(null))
        	System.out.println("Error: Updater message wasn't received from server");
        else {
		    SwingUtilities.invokeLater(new Runnable() {
			      public void run() {
			    	  frame.setCourseText(message);
			      }
			});
        }
    }

    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }
	
	
	public void run() {
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
			container.connectToServer(this, new URI("ws://wildfly8websocket-calvincarter.rhcloud.com:8000/updater"));
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
}
