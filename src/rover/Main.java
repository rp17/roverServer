package rover;

import rover.camera.cam_thread_UDP;
import rover.controller.PidServerController;
import rover.server.PIDServer;
import rover.server.UpdateServer;
import rover.ui.PidServerFrame;

import javax.swing.SwingUtilities;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Main 
{
	static PidServerController pidServerController;
	static PIDServer pidServer;
	static UpdateServer updateServer;
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
		    	  pidServer = new PIDServer(pidServerFrame);
		    	  updateServer = new UpdateServer(pidServerFrame, pidServer);
		    	  
		    	  pidServerController.setServer(pidServer);
		    	  pidServerController.setUpdateServer(updateServer);
		    	  
		    	  singleUpdaterPool.execute(updateServer);
		  		  singleServerPool.execute(pidServer);
		  		  
		  		  pidServerFrame.setVisible(true);
		  		  pidServerFrame.f.setVisible(true);
		  		  
		      }
		    });
	    
	    
	    SwingUtilities.invokeLater(new Runnable() {
		      public void run() {
		  // Android Camera Initialization
		    	  cam_thread_UDP cam_thread = new cam_thread_UDP(); 
		}
	    });
	    
	}

	public static void shutdown(){
		pidServerFrame.f.setVisible(false);
		pidServerFrame.f.dispose();
		
		pidServerFrame.setVisible(false);
		pidServerFrame.dispose();
		pidServer.active = false;
		updateServer.active = false;
		
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
