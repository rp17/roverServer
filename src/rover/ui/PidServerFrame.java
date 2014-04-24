package rover.ui;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextPane;





import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;

import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import rover.controller.PidServerController;
import rover.controller.Commands;

public class PidServerFrame extends JFrame implements ChangeListener {
	public final static String Close = "Close";
	public final static String Stop = "Stop";
	public final static String Forward = "Forward";
	public final static String Backward = "Backward";
	public final static String Left = "Left";
	public final static String Right = "Right";
	public final static String Manual = "Manual";
	public final static String Remote = "Remote";
	public final static String coursePID = "Start Course PID";
	
	// Boundaries for Steer Slider
    private final int steerMin = -90;
    private final int steerMax = 90;
    private final int steerInit = 0;    //initial frames per second
    public static int currentSteerValue = 0;
    
	// Boundaries for Speed Slider
    private final int speedMin = 0;
    private final int speedMax = 300;
    private final int speedInit = speedMax/2;    //initial frames per second
    public  int currentSpeedValue = speedInit;
    
	private Handler handler = new Handler();
	private  JPanel topPanel;
	private  JPanel textPanel;
	private  JPanel buttonPanel;
	private	 JPanel motionPanel;
	private  JLabel courseLabel;
	private  JTextPane courseText;
	private  JLabel ratioLabel;
	private  JTextPane ratioText;
	private  JLabel desiredAzimuthLabel;
	private  JTextPane desiredAzimuthText;
	private  JLabel durationLabel;
	private  JTextPane durationText;
	private  JLabel ipLabel;
	private  JTextPane ipText;
	private  JLabel speedLabel;
	private  JLabel steerLabel;
	
	
	private  JButton stopButton;
	private  JButton fwdButton;
	private  JButton bkwdButton;
	private  JButton leftButton;
	private  JButton rightButton;
	private  JButton manButton;
	private  JButton remoteButton;
	private  JButton closeButton;
	private  JButton coursePIDButton;
	
	private final PidServerController contr;
	
	public PidServerFrame(final PidServerController contr) {
		this.contr = contr;
		this.getContentPane().add(getContent());
		Toolkit toolkit =  Toolkit.getDefaultToolkit();
		Dimension dim = toolkit.getScreenSize();
		int x = (int) ((dim.getWidth() - this.getWidth()) * 0.5f);
	    int y = (int) ((dim.getHeight() - this.getHeight()) * 0.5f);
	    this.setLocation(x, y);
		
		addWindowListener(new java.awt.event.WindowAdapter() {
		    public void windowClosing(java.awt.event.WindowEvent evt) {
		    	contr.operation(PidServerFrame.Close);
		    }
		});
		pack();
	}
	
	private JPanel getContent() {
		if (topPanel == null) {
			topPanel = new JPanel();
			GridBagLayout layout = new GridBagLayout();
			topPanel.setLayout(layout);
			//topPanel.setPreferredSize(new Dimension(300, 100));
			GridBagConstraints ps = new GridBagConstraints();
			ps.gridx = 0;
			ps.gridy = 0;
			ps.fill = GridBagConstraints.HORIZONTAL;
			
			GridBagConstraints bs = new GridBagConstraints();
			bs.gridx = 0;
			bs.gridy = 1;
			bs.fill = GridBagConstraints.HORIZONTAL;
			topPanel.add(getTextFieldPanel(), ps);
			topPanel.add(getButtonPanel(), bs);
		}
		return topPanel;
	}
	private JPanel getTextFieldPanel()
	{
		if(textPanel == null){
			GridBagConstraints bl = new GridBagConstraints();
			bl.gridx = 0;
			bl.gridy = 0;
			bl.fill = GridBagConstraints.HORIZONTAL;
			bl.insets = new Insets(15,3,15,15);
			
			GridBagConstraints bf = new GridBagConstraints();
			bf.gridx = 1;
			bf.gridy = 0;
			bf.fill = GridBagConstraints.HORIZONTAL;
			bf.insets = new Insets(0,5,15,15);
			
			GridBagConstraints blDesiredAzimuth = new GridBagConstraints();
			blDesiredAzimuth.gridx = 0;
			blDesiredAzimuth.gridy = 3;
			blDesiredAzimuth.fill = GridBagConstraints.HORIZONTAL;
			blDesiredAzimuth.insets = new Insets(0,3,15,15);
			
			GridBagConstraints bfDesiredAzimuth = new GridBagConstraints();
			bfDesiredAzimuth.gridx = 1;
			bfDesiredAzimuth.gridy = 3;
			bfDesiredAzimuth.fill = GridBagConstraints.HORIZONTAL;
			bfDesiredAzimuth.insets = new Insets(0,5,15,15);
			
			GridBagConstraints blDuration = new GridBagConstraints();
			blDuration.gridx = 0;
			blDuration.gridy = 6;
			blDuration.fill = GridBagConstraints.HORIZONTAL;
			blDuration.insets = new Insets(0,3,15,15);
			
			GridBagConstraints bfDuration = new GridBagConstraints();
			bfDuration.gridx = 1;
			bfDuration.gridy = 6;
			bfDuration.fill = GridBagConstraints.HORIZONTAL;
			bfDuration.insets = new Insets(0,5,15,15);
			
			GridBagConstraints blRatio = new GridBagConstraints();
			blRatio.gridx = 0;
			blRatio.gridy = 9;
			blRatio.fill = GridBagConstraints.HORIZONTAL;
			blRatio.insets = new Insets(0,3,15,15);
			
			GridBagConstraints bfRatio = new GridBagConstraints();
			bfRatio.gridx = 1;
			bfRatio.gridy = 9;
			bfRatio.fill = GridBagConstraints.HORIZONTAL;
			bfRatio.insets = new Insets(0,5,15,15);
			
			GridBagConstraints blIP = new GridBagConstraints();
			blIP.gridx = 0;
			blIP.gridy = 10;
			blIP.fill = GridBagConstraints.HORIZONTAL;
			blIP.insets = new Insets(0,3,15,15);
			
			GridBagConstraints bfIP = new GridBagConstraints();
			bfIP.gridx = 1;
			bfIP.gridy = 10;
			bfIP.fill = GridBagConstraints.HORIZONTAL;
			bfIP.insets = new Insets(0,5,15,15);
			
			GridBagConstraints blSpeed = new GridBagConstraints();
			blSpeed.gridx = 0;
			blSpeed.gridy = 12;
			blSpeed.fill = GridBagConstraints.HORIZONTAL;
			blSpeed.insets = new Insets(0,3,15,15);
			
			
			// Speed Slider Layout
			GridBagConstraints speedSlider = new GridBagConstraints();
			speedSlider.gridx = 0;
			speedSlider.gridy = 15;
			speedSlider.fill = GridBagConstraints.HORIZONTAL;
			speedSlider.insets = new Insets(10,0,10,0);
			
			GridBagConstraints blSteer = new GridBagConstraints();
			blSteer.gridx = 0;
			blSteer.gridy = 18;
			blSteer.fill = GridBagConstraints.HORIZONTAL;
			blSteer.insets = new Insets(0,3,15,15);
			
			// Steer Slider Layout
			GridBagConstraints steerSlider = new GridBagConstraints();
			steerSlider.gridx = 0;
			steerSlider.gridy = 21;
			steerSlider.fill = GridBagConstraints.HORIZONTAL;
			steerSlider.insets = new Insets(10,0,10,0);
			
			textPanel = new JPanel();
			textPanel.setLayout(new GridBagLayout());
			//textPanel.setPreferredSize(new Dimension(250, 50));
			
			
			courseLabel = new JLabel();
			courseLabel.setText("Course: ");
			courseLabel.setPreferredSize(new Dimension(50, 20));
			courseText = new JTextPane();
			courseText.setText("");
			courseText.setPreferredSize(new Dimension(50, 20));
			courseText.setEditable(false);
			
			
			ratioLabel = new JLabel();
			ratioLabel.setText("Ratio: ");
			ratioLabel.setPreferredSize(new Dimension(50, 20));
			ratioText = new JTextPane();
			// ratioText.setText("2000");
			ratioText.setPreferredSize(new Dimension(50, 20));
			ratioText.setEditable(false);
			
			desiredAzimuthLabel = new JLabel();
			desiredAzimuthLabel.setText("Desired Course: ");
			desiredAzimuthLabel.setPreferredSize(new Dimension(50, 20));
			desiredAzimuthText = new JTextPane();
			desiredAzimuthText.setText("0");
			desiredAzimuthText.setPreferredSize(new Dimension(50, 20));
			desiredAzimuthText.setEditable(true);
			
			durationLabel = new JLabel();
			durationLabel.setText("Duration: ");
			durationLabel.setPreferredSize(new Dimension(50, 20));
			durationText = new JTextPane();
			durationText.setText("0");
			durationText.setPreferredSize(new Dimension(100, 20));
			durationText.setEditable(true);
			
			ipLabel = new JLabel();
			ipLabel.setText("IP address: ");
			ipLabel.setPreferredSize(new Dimension(50, 20));
			ipText = new JTextPane();
			try {
				ipText.setText(getIP());
			}
			catch(SocketException ex) {
				ipText.setText("No IP: socket exception");
			}
			ipText.setPreferredSize(new Dimension(50, 20));
			ipText.setEditable(false);
			
			speedLabel = new JLabel();
			speedLabel.setText("Speed");
			speedLabel.setPreferredSize(new Dimension(50, 20));
			
			steerLabel = new JLabel();
			steerLabel.setText("Steer");
			steerLabel.setPreferredSize(new Dimension(50, 20));
			
		
			
			
			textPanel.add(courseLabel, bl);
			textPanel.add(courseText, bf);
			
			textPanel.add(ratioLabel, blRatio);
			textPanel.add(ratioText, bfRatio);
			
			textPanel.add(desiredAzimuthLabel, blDesiredAzimuth);
			textPanel.add(desiredAzimuthText, bfDesiredAzimuth);
			
			textPanel.add(durationLabel, blDuration);
			textPanel.add(durationText, bfDuration);
			
			textPanel.add(ipLabel, blIP);
			textPanel.add(ipText, bfIP);
			
			textPanel.add(speedLabel, blSpeed);
			textPanel.add(steerLabel, blSteer);
			
			// Create the Speed Slider.
	        JSlider speedFramesPerSecond = new JSlider(JSlider.HORIZONTAL,
	                                              speedMin, speedMax, speedInit);
	        speedFramesPerSecond.setName("speed");
	        speedFramesPerSecond.setPreferredSize(new Dimension(900, 50));
	        speedFramesPerSecond.addChangeListener(this);
	        speedFramesPerSecond.setMajorTickSpacing(10);
	        speedFramesPerSecond.setMinorTickSpacing(1);
	        speedFramesPerSecond.setPaintTicks(true);
	        speedFramesPerSecond.setPaintLabels(true);
	        Font font = new Font("Serif", Font.ITALIC, 15);
	        speedFramesPerSecond.setFont(font);
	        
	        textPanel.add(speedFramesPerSecond, speedSlider);
	        
	        
	        
			// Create the Speed Slider.
	        JSlider steerFramesPerSecond = new JSlider(JSlider.HORIZONTAL,
	                                              steerMin, steerMax, steerInit);
	        steerFramesPerSecond.setName("steer");
	        steerFramesPerSecond.setPreferredSize(new Dimension(500, 50));
	        steerFramesPerSecond.addChangeListener(this);
	        steerFramesPerSecond.setMajorTickSpacing(10);
	        steerFramesPerSecond.setMinorTickSpacing(1);
	        steerFramesPerSecond.setPaintTicks(true);
	        steerFramesPerSecond.setPaintLabels(true);
	        textPanel.add(steerFramesPerSecond, steerSlider);
	        
			
		}
		return textPanel;
	}
	private JPanel getButtonPanel()
	{
		if(buttonPanel == null){
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			
			
			GridBagConstraints course = new GridBagConstraints();
			course.gridx = 0;
			course.gridy = 0;
			
			coursePIDButton = new JButton(coursePID);
			coursePIDButton.addActionListener(handler);
			buttonPanel.add(coursePIDButton, course);
			
			GridBagConstraints rem = new GridBagConstraints();
			rem.gridx = 0;
			rem.gridy = 1;
			
			remoteButton = new JButton(Remote);
			remoteButton.addActionListener(handler);
			buttonPanel.add(remoteButton, rem);
			
			
			GridBagConstraints man = new GridBagConstraints();
			man.gridx = 0;
			man.gridy = 2;
			
			manButton = new JButton(Manual);
			manButton.addActionListener(handler);
			buttonPanel.add(manButton, man);
			
			
			motionPanel = new JPanel();
			motionPanel.setLayout(new BorderLayout());
			
			stopButton = new JButton(Stop);
			stopButton.addActionListener(handler);
			motionPanel.add(stopButton, BorderLayout.CENTER);
			
			fwdButton = new JButton(Forward);
			fwdButton.addActionListener(handler);
			motionPanel.add(fwdButton, BorderLayout.NORTH);
			
			bkwdButton = new JButton(Backward);
			bkwdButton.addActionListener(handler);
			motionPanel.add(bkwdButton, BorderLayout.SOUTH);
			
			leftButton = new JButton(Left);
			leftButton.addActionListener(handler);
			motionPanel.add(leftButton, BorderLayout.WEST);
			
			rightButton = new JButton(Right);
			rightButton.addActionListener(handler);
			motionPanel.add(rightButton, BorderLayout.EAST);
			
			GridBagConstraints motionP = new GridBagConstraints();
			motionP.gridx = 0;
			motionP.gridy = 3;
			
			buttonPanel.add(motionPanel, motionP);
			
			GridBagConstraints clButtonCtr = new GridBagConstraints();
			clButtonCtr.gridx = 0;
			clButtonCtr.gridy = 4;
			
			closeButton = new JButton(Close);
			closeButton.addActionListener(handler);
			buttonPanel.add(closeButton, clButtonCtr);
		}
		
		return buttonPanel;
	}
	public void setCourseText(int course) {
		courseText.setText(Integer.toString(course));
	}
	
	public int getSpeedSlider() {
		return currentSpeedValue * 10;
	}
	
	public void setCourseText(String msg) {
		courseText.setText(msg);
	}
	
	public String getCourseText() {
		return courseText.getText();
	}
	
	public String getDesiredAzimuthText() {
		return desiredAzimuthText.getText();
	}
	
	public void setDurationText(String seconds) {
		durationText.setText(seconds);
	}
	
	public String getDurationText() {
		return durationText.getText();
	}
	
	public void showError(String msg) {
		JOptionPane.showMessageDialog(this, msg);
	}
	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			String strCmd = evt.getActionCommand();
			contr.operation(strCmd);
		}
	}
		
	public String getIP() throws SocketException {
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()){
		    NetworkInterface current = interfaces.nextElement();
		    if (!current.isUp() || current.isLoopback() || current.isVirtual()) continue;
		    Enumeration<InetAddress> addresses = current.getInetAddresses();
		    while (addresses.hasMoreElements()){
		    	InetAddress currentAddr = addresses.nextElement();
		    	if(currentAddr instanceof Inet4Address) {
		    		return currentAddr.getHostAddress();
		    	}
		    }
		}
		return null;
	}
	@Override
	public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        
        if (!source.getValueIsAdjusting()) {
        	if(source.getName().equals("steer")) {
	        
	        	currentSteerValue = (int) source.getValue();	
	            int steerValue = Math.abs(currentSteerValue);
	            
	            if(currentSteerValue < -10) { // turn left
	            	
	            	if(PidServerController.remote) PidServerController.server.sendCmd(Commands.SLIGHTLEFT, getSpeedSlider(), steerValue);
	            
	            } else if(currentSteerValue > 10) {	// turn right
	            	
	            	if(PidServerController.remote) PidServerController.server.sendCmd(Commands.SLIGHTRIGHT, getSpeedSlider(), steerValue);
	            
	            } else {	// forward
	            	
	            	if(PidServerController.remote) PidServerController.server.sendCmd(Commands.FORWARD, getSpeedSlider());
	            }
	            // System.out.println("The slider value is " + sliderValue + " and % is " + steerPercentValue + " orig value " + origSteerProgress);
	
	        } else if(source.getName().equals("speed")) {
	        	currentSpeedValue = (int) source.getValue();
	        }     
        } 
	}
}
