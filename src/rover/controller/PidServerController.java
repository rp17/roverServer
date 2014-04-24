package rover.controller;

import rover.Main;
import rover.server.PIDServer;
import rover.server.UpdateServer;
import rover.ui.PidServerFrame;

public class PidServerController {
	private PidServerFrame frame;
	public static PIDServer server;
	public static UpdateServer updateServer;
	public static boolean remote = false;
	private static int speed;
	
	public void setView(PidServerFrame frame) {this.frame = frame;}
	public void setServer(PIDServer server) {this.server = server;}
	public void setUpdateServer(UpdateServer server) {this.updateServer = server;}
	
	public void operation(String op) {
		
		// retrieve desired speed from rover.ui speed textbox
		speed = frame.getSpeedSlider();
		
		if(op == PidServerFrame.coursePID) {
			if(remote) server.sendCmd(Commands.TIMEDPID, speed, 0, Integer.parseInt(frame.getDesiredAzimuthText()), Integer.parseInt(frame.getDurationText()));
			// Duration dur = new Duration(frame);
			// dur.start();
		}		
		else if(op == PidServerFrame.Close) {
			server.closeServer();
			updateServer.closeServer();
			Main.shutdown();
		}
		else if(op == PidServerFrame.Remote) {
			server.sendCmd(Commands.NOCMD);
			remote = true;
		}
		else if(op == PidServerFrame.Manual) {
			server.sendCmd(Commands.MANUAL);
			remote = false;
		}
		else if(op == PidServerFrame.Forward) {
			if(remote) server.sendCmd(Commands.FORWARD, speed);
		}
		else if(op == PidServerFrame.Backward) {
			if(remote) server.sendCmd(Commands.BACKWARD, speed);
		}
		else if(op == PidServerFrame.Left) {
			if(remote) server.sendCmd(Commands.LEFT, speed);
		}
		else if(op == PidServerFrame.Right) {
			if(remote) server.sendCmd(Commands.RIGHT, speed);
		}
		else if(op == PidServerFrame.Stop) {
			if(remote) server.sendCmd(Commands.STOP);
		}
	}
	

}
