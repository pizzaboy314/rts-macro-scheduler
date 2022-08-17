package classes;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class RtsMacroSchedulerRow extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel macro1StatusLabel;

	private JLabel macro1TimerLabel;
	private Timer macro1Timer;

	private JButton macro1ControlButton;

	private JLabel macro1TimerCountLabel;
	private JTextField macro1TimerCountInput;

	private JLabel macro1KeyPressStringLabel;
	private JTextField macro1KeyPressStringInput;
	
	private Integer macro1TimerCounter;
	private Integer macro1TimerCounterInit = 20;
	private Integer macro1TimerCounterDefault = 20;
	
	private String macro1KeyPressString;
	private String macro1KeyPressStringInit = "1q";
	
	private boolean macro1Enabled = false;
	
	private Map<String, Integer> keyEventLookup;
	
	public RtsMacroSchedulerRow(Integer timerCounter, String keyPressString) {
		if(timerCounter != null) {
			macro1TimerCounterInit = timerCounter;
			macro1TimerCounterDefault = timerCounter;
		}
		if(keyPressString != null) {
			macro1KeyPressStringInit = keyPressString;
		}
		macro1TimerCounter = macro1TimerCounterInit;
		macro1KeyPressString = macro1KeyPressStringInit;
		
		loadKeyEventMap();
		
		macro1StatusLabel = new JLabel(" OFF ");
		macro1StatusLabel.setForeground(Color.RED);
		
		macro1TimerLabel = new JLabel(macro1TimerCounter + "");
		macro1TimerLabel.setForeground(Color.WHITE);
		macro1TimerLabel.setMinimumSize(new Dimension(50, 20));
		macro1TimerLabel.setMaximumSize(new Dimension(50, 20));
		macro1TimerLabel.setPreferredSize(new Dimension(50, 20));
		
		macro1Timer = new Timer(1000,new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				macro1TimerLabel.setForeground(Color.WHITE);
				macro1TimerLabel.setText(macro1TimerCounter + "");
				macro1TimerCounter--;
				if(macro1TimerCounter == 0) {
					macro1TimerLabel.setForeground(Color.GREEN);
					macro1TimerLabel.setText("QUEUE");
					macro1TimerCounter = macro1TimerCounterInit;
					
					StringBuilder sb = new StringBuilder();
					for(String keyChar : macro1KeyPressString.split("")) {
						// verify supported char
						Integer keyPress = keyEventLookup.get(keyChar);
						if(keyPress != null) {
							keyPress(keyPress);
							sb.append(keyChar);
						}
					}
					
					if(!sb.toString().equals(macro1KeyPressString)) {
						macro1KeyPressString = sb.toString();
						macro1KeyPressStringInput.setText(sb.toString());
					}
				}
			}
		});

		macro1ControlButton = new JButton("Start Macro 1");
		macro1ControlButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				macro1Enabled = !macro1Enabled;
				
				if(macro1TimerCounter != macro1TimerCounterInit) {
					macro1TimerCounter = macro1TimerCounterInit;
					macro1TimerLabel.setText(macro1TimerCounter + "");
				}
				
				String buttonText;
				String statusText;
				Color statusColor;
				
				if(macro1Enabled) {
					macro1Timer.start();
					buttonText = "Stop Macro 1";
					statusText = " ON ";
					statusColor = Color.GREEN;
				} else {
					macro1Timer.stop();
					buttonText = "Start Macro 1";
					statusText = " OFF ";
					statusColor = Color.RED;
				}
				
				macro1ControlButton.setText(buttonText);
				macro1StatusLabel.setText(statusText);
				macro1StatusLabel.setForeground(statusColor);
			}
		});
		
		
		macro1TimerCountLabel = new JLabel("  Timer: ");
		macro1TimerCountLabel.setForeground(Color.WHITE);
		
		macro1TimerCountInput = new JTextField(3);
		macro1TimerCountInput.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				updateKeyPressString();
			}
			
			public void removeUpdate(DocumentEvent e) {
				updateKeyPressString();
			}
			
			public void insertUpdate(DocumentEvent e) {
				updateKeyPressString();
			}
			
			public void updateKeyPressString() {
				Runnable doTextUpdate = new Runnable() {
					@Override
					public void run() {
						String s = macro1TimerCountInput.getText();
						try {
							macro1TimerCounterInit = Integer.parseInt(s);
						} catch (NumberFormatException nfe){
							macro1TimerCounterInit = macro1TimerCounterDefault;
							macro1TimerCountInput.setText(macro1TimerCounterInit.toString());
						}
						if(!macro1Enabled) {
							macro1TimerCounter = macro1TimerCounterInit;
							macro1TimerLabel.setText(macro1TimerCounter + "");
						}
					}
				};       
				SwingUtilities.invokeLater(doTextUpdate);
			}
		});
		macro1TimerCountInput.setText(macro1TimerCounterInit.toString());
		macro1TimerCountInput.setMinimumSize(new Dimension(3, 3));
		
		macro1KeyPressStringLabel = new JLabel("  Key seq: ");
		macro1KeyPressStringLabel.setForeground(Color.WHITE);
		
		macro1KeyPressStringInput = new JTextField(8);
		macro1KeyPressStringInput.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				updateKeyPressString();
			}

			public void removeUpdate(DocumentEvent e) {
				updateKeyPressString();
			}

			public void insertUpdate(DocumentEvent e) {
				updateKeyPressString();
			}
			
			public void updateKeyPressString() {
				Runnable doTextUpdate = new Runnable() {
			        @Override
			        public void run() {
			        	macro1KeyPressString = macro1KeyPressStringInput.getText();
			        }
			    };       
			    SwingUtilities.invokeLater(doTextUpdate);
			}
		});
		macro1KeyPressStringInput.setText(macro1KeyPressStringInit.toString());
		macro1KeyPressStringInput.setMinimumSize(new Dimension(3, 8));
		
		setLayout(new FlowLayout());
		setBackground(new Color(28,31,34));
		
		add(macro1StatusLabel);
		add(macro1TimerLabel);
		add(macro1ControlButton);
		
		add(macro1TimerCountLabel);
		add(macro1TimerCountInput);
		
		add(macro1KeyPressStringLabel);
		add(macro1KeyPressStringInput);
	}
	
	public void actionPerformed(ActionEvent e) {
        // do things
    }
	
	private void keyPress(Integer keyPress) {
		try {
			Robot robot = new Robot();
			robot.keyPress(keyPress);
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
	}
	
	private void loadKeyEventMap() {
		keyEventLookup = new HashMap<String, Integer>();
		keyEventLookup.put("0", KeyEvent.VK_0);
		keyEventLookup.put("1", KeyEvent.VK_1);
		keyEventLookup.put("2", KeyEvent.VK_2);
		keyEventLookup.put("3", KeyEvent.VK_3);
		keyEventLookup.put("4", KeyEvent.VK_4);
		keyEventLookup.put("5", KeyEvent.VK_5);
		keyEventLookup.put("6", KeyEvent.VK_6);
		keyEventLookup.put("7", KeyEvent.VK_7);
		keyEventLookup.put("8", KeyEvent.VK_8);
		keyEventLookup.put("9", KeyEvent.VK_9);
		
		keyEventLookup.put("q", KeyEvent.VK_Q);
		keyEventLookup.put("w", KeyEvent.VK_W);
		keyEventLookup.put("e", KeyEvent.VK_E);
		keyEventLookup.put("a", KeyEvent.VK_A);
		keyEventLookup.put("s", KeyEvent.VK_S);
		keyEventLookup.put("d", KeyEvent.VK_D);
	}

}
