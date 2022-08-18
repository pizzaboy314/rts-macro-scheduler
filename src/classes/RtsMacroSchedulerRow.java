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
	
	private Integer m_rowId = 0;
	private JLabel m_statusLabel;

	private JLabel m_timerLabel;
	private Timer m_timer;

	private JButton m_controlButton;

	private JLabel m_timerCountLabel;
	private JTextField m_timerCountInput;

	private JLabel m_keyPressStringLabel;
	private JTextField m_keyPressStringInput;
	
	private Integer m_timerCounter;
	private Integer m_timerCounterInit = 20;
	private Integer m_timerCounterDefault = 20;
	
	private String m_keyPressString;
	private String m_keyPressStringInit = "1q";
	
	private boolean m_macro1Enabled = false;
	
	private Map<Integer, RtsMacroSchedulerRow> m_allRows;
	private Map<String, Integer> m_keyEventLookup;
	private Robot VK = null;
	
	public RtsMacroSchedulerRow(Map<Integer, RtsMacroSchedulerRow> rows, Integer id, Integer timerCounter, String keyPressString) {
		m_allRows = rows;
		m_rowId = id;
		
		if(timerCounter != null) {
			m_timerCounterInit = timerCounter;
			m_timerCounterDefault = timerCounter;
		}
		if(keyPressString != null) {
			m_keyPressStringInit = keyPressString;
		}
		m_timerCounter = m_timerCounterInit;
		m_keyPressString = m_keyPressStringInit;
		
		loadKeyEventMap();
		
		try {
			VK = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		
		m_statusLabel = new JLabel(" OFF ");
		m_statusLabel.setForeground(Color.RED);
		
		m_timerLabel = new JLabel(m_timerCounter + "");
		m_timerLabel.setForeground(Color.WHITE);
		m_timerLabel.setMinimumSize(new Dimension(50, 20));
		m_timerLabel.setMaximumSize(new Dimension(50, 20));
		m_timerLabel.setPreferredSize(new Dimension(50, 20));
		
		m_timer = new Timer(1000,new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(noStartCollisions()) {
					m_timerLabel.setForeground(Color.WHITE);
					m_timerLabel.setText(m_timerCounter + "");
					m_timerCounter--;
				} else {
					m_timerLabel.setForeground(Color.RED);
					m_timerLabel.setText("WAIT");
				}
				if(m_timerCounter == 0) {
					m_timerLabel.setForeground(Color.GREEN);
					m_timerLabel.setText("QUEUE");
					m_timerCounter = m_timerCounterInit;
					
					StringBuilder sb = new StringBuilder();
					for(String keyChar : m_keyPressString.split("")) {
						// verify supported char
						Integer keyPress = m_keyEventLookup.get(keyChar);
						if(keyPress != null) {
							keyPress(keyPress);
							sb.append(keyChar);
						}
					}
					
					if(!sb.toString().equals(m_keyPressString)) {
						m_keyPressString = sb.toString();
						m_keyPressStringInput.setText(sb.toString());
					}
				}
			}
		});

		m_controlButton = new JButton("Start Macro 1");
		m_controlButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_macro1Enabled = !m_macro1Enabled;
				
				if(m_timerCounter != m_timerCounterInit) {
					m_timerCounter = m_timerCounterInit;
					m_timerLabel.setText(m_timerCounter + "");
				}
				
				String buttonText;
				String statusText;
				Color statusColor;
				
				if(m_macro1Enabled) {
					m_timer.start();
					buttonText = "Stop Macro 1";
					statusText = " ON ";
					statusColor = Color.GREEN;
				} else {
					m_timer.stop();
					buttonText = "Start Macro 1";
					statusText = " OFF ";
					statusColor = Color.RED;
					
					m_timerLabel.setForeground(Color.WHITE);
					m_timerLabel.setText(m_timerCounter + "");
				}
				
				m_controlButton.setText(buttonText);
				m_statusLabel.setText(statusText);
				m_statusLabel.setForeground(statusColor);
			}
		});
		
		
		m_timerCountLabel = new JLabel("  Timer: ");
		m_timerCountLabel.setForeground(Color.WHITE);
		
		m_timerCountInput = new JTextField(3);
		m_timerCountInput.getDocument().addDocumentListener(new DocumentListener() {
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
						String s = m_timerCountInput.getText();
						try {
							m_timerCounterInit = Integer.parseInt(s);
						} catch (NumberFormatException nfe){
							m_timerCounterInit = m_timerCounterDefault;
							m_timerCountInput.setText(m_timerCounterInit.toString());
						}
						if(!m_macro1Enabled) {
							m_timerCounter = m_timerCounterInit;
							m_timerLabel.setText(m_timerCounter + "");
						}
					}
				};       
				SwingUtilities.invokeLater(doTextUpdate);
			}
		});
		m_timerCountInput.setText(m_timerCounterInit.toString());
		m_timerCountInput.setMinimumSize(new Dimension(3, 3));
		
		m_keyPressStringLabel = new JLabel("  Key seq: ");
		m_keyPressStringLabel.setForeground(Color.WHITE);
		
		m_keyPressStringInput = new JTextField(8);
		m_keyPressStringInput.getDocument().addDocumentListener(new DocumentListener() {
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
			        	m_keyPressString = m_keyPressStringInput.getText();
			        }
			    };       
			    SwingUtilities.invokeLater(doTextUpdate);
			}
		});
		m_keyPressStringInput.setText(m_keyPressStringInit.toString());
		m_keyPressStringInput.setMinimumSize(new Dimension(3, 8));
		
		setLayout(new FlowLayout());
		setBackground(new Color(28,31,34));
		
		add(m_statusLabel);
		add(m_timerLabel);
		add(m_controlButton);
		
		add(m_timerCountLabel);
		add(m_timerCountInput);
		
		add(m_keyPressStringLabel);
		add(m_keyPressStringInput);
	}
	
	public void actionPerformed(ActionEvent e) {
        // stub
    }

	public Integer getId() {
		return m_rowId;
	}
	
	public Integer getTimerCounter() {
		return m_timerCounter;
	}
	
	private boolean noStartCollisions() {
		for(RtsMacroSchedulerRow row : m_allRows.values()) {
			if(row.getId() != getId() && 
					row.getId() < getId() &&
					(row.getTimerCounter() == getTimerCounter() || 
					row.getTimerCounter() == getTimerCounter()+1 || 
					row.getTimerCounter() == getTimerCounter()-1 )) {
				return false;
			}
		}
		return true;
	}
	
	
	private void keyPress(Integer keyPress) {
		if(VK != null) {
			VK.keyPress(keyPress);
			VK.keyRelease(keyPress);
		}
	}
	
	private void loadKeyEventMap() {
		m_keyEventLookup = new HashMap<String, Integer>();
		m_keyEventLookup.put("0", KeyEvent.VK_0);
		m_keyEventLookup.put("1", KeyEvent.VK_1);
		m_keyEventLookup.put("2", KeyEvent.VK_2);
		m_keyEventLookup.put("3", KeyEvent.VK_3);
		m_keyEventLookup.put("4", KeyEvent.VK_4);
		m_keyEventLookup.put("5", KeyEvent.VK_5);
		m_keyEventLookup.put("6", KeyEvent.VK_6);
		m_keyEventLookup.put("7", KeyEvent.VK_7);
		m_keyEventLookup.put("8", KeyEvent.VK_8);
		m_keyEventLookup.put("9", KeyEvent.VK_9);
		
		m_keyEventLookup.put("q", KeyEvent.VK_Q);
		m_keyEventLookup.put("w", KeyEvent.VK_W);
		m_keyEventLookup.put("e", KeyEvent.VK_E);
		m_keyEventLookup.put("a", KeyEvent.VK_A);
		m_keyEventLookup.put("s", KeyEvent.VK_S);
		m_keyEventLookup.put("d", KeyEvent.VK_D);
	}


}
