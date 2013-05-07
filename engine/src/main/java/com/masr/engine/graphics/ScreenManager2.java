package com.masr.engine.graphics;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Window;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.masr.engine.util.Constants;

/**
 * The ScreenManager class manages initializing and displaying full screen
 * graphics modes.
 */
public class ScreenManager2 {

	private GraphicsDevice device;
	private JFrame frame;

	/**
	 * Creates a new ScreenManager object.
	 */
	public ScreenManager2() {
		frame = new JFrame(Constants.NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		frame.setMinimumSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
		frame.createBufferStrategy(2);
		// frame.setLocation(100, 100);
	}

	
	/**
	 * Gets the graphics context for the display. The ScreenManager uses double
	 * buffering, so applications must call update() to show any graphics drawn.
	 * <p>
	 * The application must dispose of the graphics object.
	 */
	public Graphics2D getGraphics() {
		if (frame != null) {
			BufferStrategy strategy = frame.getBufferStrategy();
			return (Graphics2D) strategy.getDrawGraphics();
		} else {
			return null;
		}
	}

	/**
	 * Updates the display.
	 */
	public void update() {
		// Window window = device.getFullScreenWindow();
		if (frame != null) {
			BufferStrategy strategy = frame.getBufferStrategy();
			if (!strategy.contentsLost()) {
				strategy.show();
			}
		}
		// Sync the display on some systems.
		// (on Linux, this fixes event queue problems)
		// Toolkit.getDefaultToolkit().sync();
	}

	public JFrame getFrame() {
		return this.frame;
	}

	public void dispose() {
		frame.dispose();
	}

	/**
	 * Creates an image compatible with the current display.
	 */
	public BufferedImage createCompatibleImage(int w, int h, int transparancy) {
		Window window = device.getFullScreenWindow();
		if (window != null) {
			GraphicsConfiguration gc = window.getGraphicsConfiguration();
			return gc.createCompatibleImage(w, h, transparancy);
		}
		return null;
	}
}
