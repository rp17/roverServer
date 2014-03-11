package rover.controller;

import rover.ui.PidServerFrame;

public class Duration extends Thread {
	
	private PidServerFrame frame;
	
	// Record start time of program 
	long startTime;
	
	// Record end time of program
	long endTime;

	// Record total run time of program
	long totalTime;
	
	
	Duration(PidServerFrame frame)  {
		this.frame = frame;
		startTime = System.currentTimeMillis();
		
	}
		
	public void run() {
		
			/*while (true) {
			
				try {
				   
				   endTime   = System.currentTimeMillis();
				   totalTime = endTime - startTime;
				   				   
				   //if(PidServerController.remote)
				   frame.setDurationText(Long.toString((totalTime / 1000) % 60));
				   
				// Print the runtime of program
					 System.out.println("The run time of program was: " + totalTime + " milliseconds (" + (totalTime / 1000) % 60 + " seconds)." );

				
				   sleep(10);
				   
				} 
				catch (Exception e) {
					
				}
			}
			*/
		
			  endTime = System.currentTimeMillis() + Integer.parseInt(frame.getDurationText()) * 1000;
			  
			  while (System.currentTimeMillis() < endTime) {
			  synchronized (this) {
			  
				  try {
					   wait(endTime - System.currentTimeMillis());
					   System.out.println("done waiting " + frame.getDurationText() + " seconds.");
					   
				  } catch (Exception e) {
					}
				  }
		      }
			
			
			
		}
	
	// Print the runtime of program
	// System.out.println("The run time of program was: " + totalTime + " milliseconds (" + (totalTime / 1000) % 60 + " seconds)." );

}
