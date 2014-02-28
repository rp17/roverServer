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

import rover.controller.PidServerController;

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
	
	// Boundaries for Slider
    private final int FPS_MIN = -90;
    private final int FPS_MAX = 90;
    private final int FPS_INIT = 0;    //initial frames per second
    public static int sliderValue = 0;
    
    private float origSteerProgress = FPS_INIT;
    
	private Handler handler = new Handler();
	private  JPanel topPanel;
	private  JPanel textPanel;
	private  JPanel buttonPanel;
	private	 JPanel motionPanel;
	private  JLabel courseLabel;
	private  JTextPane courseText;
	private  JLabel speedLabel;
	private  JTextPane speedText;
	private  JLabel desiredAzimuthLabel;
	private  JTextPane desiredAzimuthText;
	private  JLabel durationLabel;
	private  JTextPane durationText;
	
	
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
			
			GridBagConstraints blSpeed = new GridBagConstraints();
			blSpeed.gridx = 0;
			blSpeed.gridy = 9;
			blSpeed.fill = GridBagConstraints.HORIZONTAL;
			blSpeed.insets = new Insets(0,3,15,15);
			
			GridBagConstraints bfSpeed = new GridBagConstraints();
			bfSpeed.gridx = 1;
			bfSpeed.gridy = 9;
			bfSpeed.fill = GridBagConstraints.HORIZONTAL;
			bfSpeed.insets = new Insets(0,5,15,15);
			
			// Slider Layout
			GridBagConstraints bSlider = new GridBagConstraints();
			bSlider.gridx = 0;
			bSlider.gridy = 12;
			bSlider.fill = GridBagConstraints.HORIZONTAL;
			bSlider.insets = new Insets(10,0,10,0);
			
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
			
			
			speedLabel = new JLabel();
			speedLabel.setText("Speed: ");
			speedLabel.setPreferredSize(new Dimension(50, 20));
			speedText = new JTextPane();
			speedText.setText("2000");
			speedText.setPreferredSize(new Dimension(50, 20));
			speedText.setEditable(true);
			
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
			durationText.setPreferredSize(new Dimension(50, 20));
			durationText.setEditable(false);
			
			
			textPanel.add(courseLabel, bl);
			textPanel.add(courseText, bf);
			
			textPanel.add(speedLabel, blSpeed);
			textPanel.add(speedText, bfSpeed);
			
			textPanel.add(desiredAzimuthLabel, blDesiredAzimuth);
			textPanel.add(desiredAzimuthText, bfDesiredAzimuth);
			
			textPanel.add(durationLabel, blDuration);
			textPanel.add(durationText, bfDuration);
			
			 //Create the slider.
	        JSlider framesPerSecond = new JSlider(JSlider.HORIZONTAL,
	                                              FPS_MIN, FPS_MAX, FPS_INIT);
	        framesPerSecond.setPreferredSize(new Dimension(500, 50));
	        framesPerSecond.addChangeListener(this);
	        framesPerSecond.setMajorTickSpacing(10);
	        framesPerSecond.setMinorTickSpacing(1);
	        framesPerSecond.setPaintTicks(true);
	        framesPerSecond.setPaintLabels(true);
	        //framesPerSecond.setBorder(
	        //        BorderFactory.createEmptyBorder(0,0,0,0));
	        Font font = new Font("Serif", Font.ITALIC, 15);
	        framesPerSecond.setFont(font);
	        
	        textPanel.add(framesPerSecond, bSlider);
			
		}
		return textPanel;
	}
	private JPanel getButtonPanel()
	{
		if(buttonPanel == null){
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			
			GridBagConstraints rem = new GridBagConstraints();
			rem.gridx = 0;
			rem.gridy = 0;
			
			coursePIDButton = new JButton(coursePID);
			coursePIDButton.addActionListener(handler);
			buttonPanel.add(coursePIDButton, BorderLayout.CENTER);
			
			remoteButton = new JButton(Remote);
			remoteButton.addActionListener(handler);
			buttonPanel.add(remoteButton, rem);
			
			
			GridBagConstraints man = new GridBagConstraints();
			man.gridx = 0;
			man.gridy = 1;
			
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
			motionP.gridy = 2;
			
			buttonPanel.add(motionPanel, motionP);
			
			GridBagConstraints clButtonCtr = new GridBagConstraints();
			clButtonCtr.gridx = 0;
			clButtonCtr.gridy = 3;
			
			closeButton = new JButton(Close);
			closeButton.addActionListener(handler);
			buttonPanel.add(closeButton, clButtonCtr);
		}
		
		return buttonPanel;
	}
	public void setCourseText(int course) {
		courseText.setText(Integer.toString(course));
	}
	
	public String getSpeedText() {
		return speedText.getText();
	}
	
	public void setCourseText(String msg) {
		courseText.setText(msg);
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
	
	private int speed;
	
	@Override
	public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        
        speed = Integer.parseInt(getSpeedText());
        
        if (!source.getValueIsAdjusting()) {
            sliderValue = (int) source.getValue();
                        
           // float steerPercentValue = (float) origSteerProgress - (float) Math.abs(sliderValue);
            
            int steerValue = Math.abs(sliderValue);
            
            if(sliderValue < -10) { // turn left
            	
            	if(PidServerController.remote) PidServerController.server.sendCmd(7, speed, steerValue);
            
            } else if(sliderValue > 10) {	// turn right
            	
            	if(PidServerController.remote) PidServerController.server.sendCmd(6, speed, steerValue);
            
            } else {	// forward
            	
            	if(PidServerController.remote) PidServerController.server.sendCmd(1, speed);
            }
            // System.out.println("The slider value is " + sliderValue + " and % is " + steerPercentValue + " orig value " + origSteerProgress);

            // Reset original slider value to the new slider value
            origSteerProgress = sliderValue;
        }
	}
}
