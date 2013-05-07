package com.masr.engine;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.JFrame;

/**
 *
 * @author Tim Jörgen
 */
public class RenderWindow {

	private JFrame jFrame;
	private BufferedImage renderImage;
	private Graphics2D renderGraphics2D;
	private Color clearColor = Color.BLACK;
	private long frameStartTime;
	private int fpsTimer = 0;
	private int framesPerSec;

	public RenderWindow(String title, final int width, final int height) {
		this.jFrame = new JFrame();
		this.jFrame.setTitle(title);
		this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.jFrame.setSize(width, height);
		this.jFrame.setResizable(false);
		this.jFrame.setVisible(true);
		this.jFrame.createBufferStrategy(2);
		this.jFrame.setIgnoreRepaint(true);

		this.renderImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.renderGraphics2D = (Graphics2D) renderImage.getGraphics();
	}

	public Component getComponent() {
		return this.jFrame;
	}

	public void setClearColor(Color clearColor) {
		this.clearColor = clearColor;
	}

	public void clearScreen() {
		renderGraphics2D.setColor(this.clearColor);
		renderGraphics2D.fillRect(0, 0, jFrame.getWidth(), jFrame.getHeight());
	}

	public Graphics2D getRenderGraphics2D() {
		return this.renderGraphics2D;
	}

	void displayFrame() {
		BufferStrategy strategy = jFrame.getBufferStrategy();
		Graphics screen = strategy.getDrawGraphics();

		try {
			screen.drawImage(renderImage, 0, 0, null);
		} finally {
			screen.dispose();
		}

		strategy.show();
	}
}
