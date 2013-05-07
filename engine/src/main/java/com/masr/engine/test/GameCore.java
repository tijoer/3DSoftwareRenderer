package com.masr.engine.test;

import com.masr.engine.graphics.ScreenManager2;

import java.awt.*;

import javax.swing.ImageIcon;

import com.masr.engine.util.DebugWindow;

/**
 * Simple abstract class used for testing. Subclasses should implement the
 * draw() method.
 */
public abstract class GameCore {
	
	private boolean isRunning;
	protected ScreenManager2 screen;
	
	protected DebugWindow debugWindow;

	/**
	 * Signals the game loop that it's time to quit
	 */
	public void stop() {
		isRunning = false;
	}

	/**
	 * Calls init() and gameLoop()
	 */
	public void run() {
		init();
		gameLoop();
	}

	/**
	 * Exits the VM from a daemon thread. The daemon thread waits 2 seconds then
	 * calls System.exit(0). Since the VM should exit when only daemon threads
	 * are running, this makes sure System.exit(0) is only called if neccesary.
	 * It's neccesary if the Java Sound system is running.
	 */
	public void lazilyExit() {
		Thread thread = new Thread() {
			public void run() {
				// first, wait for the VM exit on its own.
				try {
					Thread.sleep(2000);
				} catch (InterruptedException ex) {
				}
				// system is still running, so force an exit
//				System.exit(0);
			}
		};
		thread.setDaemon(true);
		thread.start();
	}

	/**
	 * Sets full screen mode and initiates and objects.
	 */
	public void init() {		
		screen = new ScreenManager2();

		Window window = screen.getFrame();
		window.setFont(new Font("Dialog", Font.PLAIN, 12));
		window.setBackground(Color.blue);
		window.setForeground(Color.white);

		isRunning = true;
	}

	public Image loadImage(String fileName) {
		return new ImageIcon(fileName).getImage();
	}

	/**
	 * Runs through the game loop until stop() is called.
	 */
	public void gameLoop() {
		long startTime = System.currentTimeMillis();
		long currTime = startTime;

		while (isRunning) {
			long elapsedTime = System.currentTimeMillis() - currTime;
			currTime += elapsedTime;

			// update
			update(elapsedTime);

			// draw the screen
			Graphics2D g = screen.getGraphics();
			draw(g);
			g.dispose();
			screen.update();
		}
		
		screen.dispose();
		if (debugWindow != null) {
			debugWindow.dispose();
		}
	}

	/**
	 * Updates the state of the game/animation based on the amount of elapsed
	 * time that has passed.
	 */
	public void update(long elapsedTime) {
		// do nothing
	}

	/**
	 * Draws to the screen. Subclasses must override this method.
	 */
	public abstract void draw(Graphics2D g);
}
