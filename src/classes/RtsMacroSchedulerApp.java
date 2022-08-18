package classes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


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
	
	public static void main(String[] args) {
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
		resultFrameBoundHeight = 200;
		resultFrame.setBounds(resultFrameBoundX, resultFrameBoundY, resultFrameBoundWidth, resultFrameBoundHeight);
		
		resultFrame.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		resultFrame.getContentPane().setBackground(new Color(28,31,34));
		
		Map<Integer, RtsMacroSchedulerRow> rows = new HashMap<Integer, RtsMacroSchedulerRow>();
		RtsMacroSchedulerRow firstRow = new RtsMacroSchedulerRow(rows, 1, 20,"1q");
		RtsMacroSchedulerRow secondRow = new RtsMacroSchedulerRow(rows, 2, 35,"9wwww");
		rows.put(1, firstRow); 
		rows.put(2, secondRow);
		
		JButton addButton = new JButton("Add Macro");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer newId = Collections.max(rows.keySet()) + 1;
				RtsMacroSchedulerRow newRow = new RtsMacroSchedulerRow(rows, newId, 20,"1q");
				rows.put(newId, newRow); 
				resultFrame.getContentPane().add(newRow, BorderLayout.NORTH);
				
				resultFrameBoundHeight += 50;
				resultFrame.setBounds(resultFrameBoundX, resultFrameBoundY, resultFrameBoundWidth, resultFrameBoundHeight);
			}
		});
		resultFrame.getContentPane().add(addButton, BorderLayout.NORTH);
		resultFrame.getContentPane().add(firstRow, BorderLayout.NORTH);
		resultFrame.getContentPane().add(secondRow, BorderLayout.NORTH);
	}

}
