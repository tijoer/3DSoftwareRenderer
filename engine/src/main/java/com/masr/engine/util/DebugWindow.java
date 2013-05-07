package com.masr.engine.util;

import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class DebugWindow {
	private JFrame frame;
	private HashMap<String, JLabel> labels = new HashMap<>();
	
	public DebugWindow() {
		frame = new JFrame();

		//frame.add( new JTextField() );
		frame.setSize( 300, 200 );
		frame.setVisible( true );
		frame.setLocation(Constants.WIDTH, 0);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void addOrSetInfo(String key, String value){
		if(labels.containsKey(key)) {
			labels.get(key).setText(value);
		} else {
			JLabel label = new JLabel("Hallo Welt");
			frame.getContentPane().add(label);
//			frame.pack();
			frame.setVisible( true );
			labels.put(key, label);
		}
	}
	
	public void dispose() {
		frame.dispose();
	}
}
