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


public class RtsMacroSchedulerApp extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JFrame resultFrame;
	private static int resultFrameBoundX;
	private static int resultFrameBoundY;
	private static int resultFrameBoundWidth;
	private static int resultFrameBoundHeight;
	
	private static JLabel macro1StatusLabel;

	private static JLabel macro1TimerLabel;
	private static Timer macro1Timer;

	private static JButton macro1ControlButton;

	private static JLabel macro1TimerCountLabel;
	private static JTextField macro1TimerCountInput;

	private static JLabel macro1KeyPressStringLabel;
	private static JTextField macro1KeyPressStringInput;
	
	private static Integer macro1TimerCounter;
	private static Integer macro1TimerCounterInit = 20;
	private static Integer macro1TimerCounterDefault = 20;
	
	private static String macro1KeyPressString;
	private static String macro1KeyPressStringInit = "1q";
	
	private static boolean macro1Enabled = false;
	
	private static Map<String, Integer> keyEventLookup;
	
	public static void main(String[] args) {
		macro1TimerCounter = macro1TimerCounterInit;
		macro1KeyPressString = macro1KeyPressStringInit;
		
		loadKeyEventMap();
		resultWindow();
		
		resultFrame.setVisible(true);
		resultFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public void actionPerformed(ActionEvent e) {
        // do things
    }
	
	public synchronized static void resultWindow() {
		resultFrame = new JFrame("RTS Macro Scheduler / Runner");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension((int) (screenSize.width / 2), (int) (screenSize.height / 2));
		resultFrameBoundX = (int) (frameSize.width / 2);
		resultFrameBoundY = (int) (frameSize.height / 2);
		resultFrameBoundWidth = 500;
		resultFrameBoundHeight = frameSize.height;
		resultFrame.setBounds(resultFrameBoundX, resultFrameBoundY, resultFrameBoundWidth, resultFrameBoundHeight);
		
		resultFrame.getContentPane().setBackground(new Color(28,31,34));
		resultFrame.getContentPane().add(new RtsMacroSchedulerRow(), BorderLayout.NORTH);
	}
	
	private static void keyPress(Integer keyPress) {
		try {
			Robot robot = new Robot();
			robot.keyPress(keyPress);
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
	}
	
	private static void loadKeyEventMap() {
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
