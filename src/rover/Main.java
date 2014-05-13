package rover;

import rover.controller.PidServerController;
import rover.ui.PidServerFrame;
import rover.websocket.PCCamera;
import rover.websocket.PCCommand;
import rover.websocket.PCUpdater;

import javax.swing.SwingUtilities;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main 
{
	static PidServerController pidServerController;
	static PCCommand command;
	static PCUpdater update;
	static PidServerFrame pidServerFrame;
	private static final ExecutorService singleServerPool = Executors.newSingleThreadExecutor();
	private static final ExecutorService singleUpdaterPool = Executors.newSingleThreadExecutor();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		pidServerController = new PidServerController();
	    SwingUtilities.invokeLater(new Runnable() {
		      public void run() {
		    	  pidServerFrame = new PidServerFrame(pidServerController);
		    	  pidServerController.setView(pidServerFrame);
		    	  command = new PCCommand(pidServerFrame);
		    	  update = new PCUpdater(pidServerFrame);
		    	  
		    	  pidServerController.setServer(command);
		    	  
		    	  singleUpdaterPool.execute(update);
		  		  singleServerPool.execute(command);
		  		  
		  		  pidServerFrame.setVisible(true);
		  		  pidServerFrame.f.setVisible(true);
		  		  
		      }
		    });
	    
	    
	    SwingUtilities.invokeLater(new Runnable() {
		      public void run() {
		  // Android Camera Initialization
		    	  PCCamera cam_thread = new PCCamera(); 
		}
	    });
	    
	}

	public static void shutdown(){
		pidServerFrame.f.setVisible(false);
		pidServerFrame.f.dispose();
		
		pidServerFrame.setVisible(false);
		pidServerFrame.dispose();
		// pidServer.active = false;
		
		//shutdownAndAwaitTermination(singleServerPool);
		System.exit(0);
	}
	static void shutdownAndAwaitTermination(ExecutorService pool) {
		   pool.shutdown(); // Disable new tasks from being submitted
		   try {
		     // Wait a while for existing tasks to terminate
		     if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
		       pool.shutdownNow(); // Cancel currently executing tasks
		       // Wait a while for tasks to respond to being cancelled
		       if (!pool.awaitTermination(60, TimeUnit.SECONDS)) System.err.println("Pool did not terminate");
		     }
		   }
		   catch (InterruptedException ie) {
		     // (Re-)Cancel if current thread also interrupted
		     pool.shutdownNow();
		     // Preserve interrupt status
		     Thread.currentThread().interrupt();
		   }
	 }
}
