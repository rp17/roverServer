package rover.controller;

import rover.Main;
import rover.server.PIDServer;
import rover.ui.PidServerFrame;

public class PidServerController {
	private PidServerFrame frame;
	private PIDServer server;
	private boolean remote = false;
	
	public void setView(PidServerFrame frame) {this.frame = frame;}
	public void setServer(PIDServer server) {this.server = server;}
	public void operation(String op) {
		if(op == PidServerFrame.Close) {
			server.closeServer();
			Main.shutdown();
		}
		else if(op == PidServerFrame.Remote) {
			server.sendCmd(0);
			remote = true;
		}
		else if(op == PidServerFrame.Manual) {
			server.sendCmd(-1);
			remote = false;
		}
		else if(op == PidServerFrame.Forward) {
			if(remote) server.sendCmd(1);
		}
		else if(op == PidServerFrame.Backward) {
			if(remote) server.sendCmd(2);
		}
		else if(op == PidServerFrame.Left) {
			if(remote) server.sendCmd(3);
		}
		else if(op == PidServerFrame.Right) {
			if(remote) server.sendCmd(4);
		}
	}
	

}
