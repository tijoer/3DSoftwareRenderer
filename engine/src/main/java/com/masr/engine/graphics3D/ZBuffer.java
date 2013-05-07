package com.masr.engine.graphics3D;

import com.masr.engine.util.Constants;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 * The ZBuffer class implements a z-buffer, or depth-buffer, that records the
 * depth of every pixel in a 3D view window. The value recorded for each pixel
 * is the inverse of the depth (1/z), so there is higher precision for close
 * objects and a lower precision for far-away objects (where high depth
 * precision is not as visually important).
 */
public class ZBuffer {

	private short[] depthBuffer;
	private int width;
	private int height;

	/**
	 * Creates a new z-buffer with the specified width and height.
	 */
	public ZBuffer(int width, int height) {
		depthBuffer = new short[width * height];
		this.width = width;
		this.height = height;
		clear();
	}

	/**
	 * Gets the width of this z-buffer.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the height of this z-buffer.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the array used for the depth buffer
	 */
	public short[] getArray() {
		return depthBuffer;
	}

	/**
	 * Clears the z-buffer. All depth values are set to 0.
	 */
	public final void clear() {
		for (int i = 0; i < depthBuffer.length; i++) {
			depthBuffer[i] = 0;
		}
	}

	/**
	 * Sets the depth of the pixel at at specified offset, overwriting its
	 * current depth.
	 */
	public void setDepth(int offset, short depth) {
		depthBuffer[offset] = depth;
	}

	/**
	 * Checks the depth at the specified offset, and if the specified depth is
	 * lower (is greater than or equal to the current depth at the specified
	 * offset), then the depth is set and this method returns true. Otherwise,
	 * no action occurs and this method returns false.
	 */
	public boolean checkDepth(int offset, short depth) {
		if (depth >= depthBuffer[offset]) {
			depthBuffer[offset] = depth;
			return true;
		} else {
			return false;
		}
	}

	public void displayZBufferInNewWindow() {
		final JFrame frame;

		// we need to copy the depth buffer, because drawin is asynchronous
		final short[] currentDepthBuffer = new short[this.depthBuffer.length + 1];
		System.arraycopy(this.depthBuffer, 0, currentDepthBuffer, 0, this.depthBuffer.length);

		frame = new JFrame() {

			@Override
			public void paint(Graphics g) {
				super.paint(g);

				BufferedImage off_Image = new BufferedImage(Constants.WIDTH,
					Constants.HEIGHT, BufferedImage.TYPE_INT_ARGB);
				off_Image.createGraphics();
				Graphics bufferedGraphics = off_Image.getGraphics();

				for (int y = 0; y < Constants.HEIGHT; y++) {
					for (int x = 0; x < Constants.WIDTH; x++) {
						final int offset = x + y * getHeight();
						int foo = (int) ((float) currentDepthBuffer[offset] / Short.MAX_VALUE * 255);

						bufferedGraphics.setColor(new Color(foo, foo, foo));
						bufferedGraphics.drawLine(x, y, x, y);
					}
				}

				g.drawImage(off_Image, 0, 0, null);
			}
		};

		frame.setTitle("Depth Buffer");
		frame.setSize(Constants.WIDTH, Constants.HEIGHT);
		frame.setVisible(true);
		frame.setLocation(Constants.WIDTH, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
		Action escapeAction = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		};
		frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
		frame.getRootPane().getActionMap().put("ESCAPE", escapeAction);
	}
}