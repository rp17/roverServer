package rover;

import rover.controller.PidServerController;
import rover.server.PIDServer;
import rover.ui.PidServerFrame;

import javax.swing.SwingUtilities;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Main {
	static PidServerController pidServerController;
	static PIDServer pidServer;
	static PidServerFrame pidServerFrame;
	private static final ExecutorService singleServerPool = Executors.newSingleThreadExecutor();
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
		    	  pidServerController.setServer(pidServer);
		  		  singleServerPool.execute(pidServer);
		  		  pidServerFrame.setVisible(true);
		      }
		    });
	}

	public static void shutdown(){
		pidServerFrame.setVisible(false);
		pidServerFrame.dispose();
		pidServer.active = false;
		shutdownAndAwaitTermination(singleServerPool);
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
