package com.masr.engine.test;

import com.masr.engine.graphics3D.PolygonRenderer;
import com.masr.engine.graphics3D.SolidPolygonRenderer;
import com.masr.engine.input.GameAction;
import com.masr.engine.input.InputManager;
import com.masr.engine.math3D.Transform3D;
import com.masr.engine.math3D.Vector3D;
import com.masr.engine.math3D.ViewWindow;
import com.masr.engine.util.Constants;
import com.masr.engine.util.DebugWindow;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;


public abstract class GameCore3D extends GameCore {

	private static final long INSTRUCTIONS_TIME = 4000;

	protected PolygonRenderer polygonRenderer;
	protected ViewWindow viewWindow;
	protected List polygons;

	private boolean drawFrameRate = false;
	private boolean drawInstructions = true;
	private long drawInstructionsTime = 0;

	// for calculating frame rate
	private int numFrames;
	private long startTime;
	private float frameRate;

	protected InputManager inputManager;
	private GameAction exit = new GameAction("exit");
	private GameAction smallerView = new GameAction("smallerView",
			GameAction.DETECT_INITAL_PRESS_ONLY);
	private GameAction largerView = new GameAction("largerView",
			GameAction.DETECT_INITAL_PRESS_ONLY);
	private GameAction frameRateToggle = new GameAction("frameRateToggle",
			GameAction.DETECT_INITAL_PRESS_ONLY);
	protected GameAction goForward = new GameAction("goForward");
	protected GameAction goBackward = new GameAction("goBackward");
	protected GameAction goUp = new GameAction("goUp");
	protected GameAction goDown = new GameAction("goDown");
	protected GameAction goLeft = new GameAction("goLeft");
	protected GameAction goRight = new GameAction("goRight");
	protected GameAction turnLeft = new GameAction("turnLeft");
	protected GameAction turnRight = new GameAction("turnRight");
	protected GameAction tiltUp = new GameAction("tiltUp");
	protected GameAction tiltDown = new GameAction("tiltDown");
	protected GameAction tiltLeft = new GameAction("tiltLeft");
	protected GameAction tiltRight = new GameAction("tiltRight");

	public void init() {
		super.init();

		debugWindow = new DebugWindow();

		inputManager = new InputManager(screen.getFrame());
		inputManager.setRelativeMouseMode(true);
		inputManager.setCursor(InputManager.INVISIBLE_CURSOR);

		inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);
		inputManager.mapToKey(goForward, KeyEvent.VK_W);
		inputManager.mapToKey(goForward, KeyEvent.VK_UP);
		inputManager.mapToKey(goBackward, KeyEvent.VK_S);
		inputManager.mapToKey(goBackward, KeyEvent.VK_DOWN);
		inputManager.mapToKey(goLeft, KeyEvent.VK_A);
		inputManager.mapToKey(goLeft, KeyEvent.VK_LEFT);
		inputManager.mapToKey(goRight, KeyEvent.VK_D);
		inputManager.mapToKey(goRight, KeyEvent.VK_RIGHT);
		inputManager.mapToKey(goUp, KeyEvent.VK_PAGE_UP);
		inputManager.mapToKey(goDown, KeyEvent.VK_PAGE_DOWN);
		inputManager.mapToMouse(turnLeft, InputManager.MOUSE_MOVE_LEFT);
		inputManager.mapToMouse(turnRight, InputManager.MOUSE_MOVE_RIGHT);
		inputManager.mapToMouse(tiltUp, InputManager.MOUSE_MOVE_UP);
		inputManager.mapToMouse(tiltDown, InputManager.MOUSE_MOVE_DOWN);

		inputManager.mapToKey(tiltLeft, KeyEvent.VK_INSERT);
		inputManager.mapToKey(tiltRight, KeyEvent.VK_DELETE);

		inputManager.mapToKey(smallerView, KeyEvent.VK_SUBTRACT);
		inputManager.mapToKey(smallerView, KeyEvent.VK_MINUS);
		inputManager.mapToKey(largerView, KeyEvent.VK_ADD);
		inputManager.mapToKey(largerView, KeyEvent.VK_PLUS);
		inputManager.mapToKey(largerView, KeyEvent.VK_EQUALS);
		inputManager.mapToKey(frameRateToggle, KeyEvent.VK_R);

		// create the polygon renderer
		createPolygonRenderer();

		// create polygons
		polygons = new ArrayList();
		createPolygons();
	}

	public abstract void createPolygons();

	public void createPolygonRenderer() {
		// make the view window the entire screen
		viewWindow = new ViewWindow(0, 0, Constants.WIDTH, Constants.HEIGHT,
				(float) Math.toRadians(75));

		Transform3D camera = new Transform3D(0, 100, 0);
		polygonRenderer = new SolidPolygonRenderer(camera, viewWindow);
	}

	/**
	 * Sets the view bounds, centering the view on the screen.
	 */
	public void setViewBounds(int width, int height) {
		width = Math.min(width, Constants.WIDTH);
		height = Math.min(height, Constants.HEIGHT);
		width = Math.max(64, width);
		height = Math.max(48, height);
		viewWindow.setBounds((Constants.WIDTH - width) / 2,
				(Constants.HEIGHT - height) / 2, width, height);

		// clear the screen if view size changed
		// (clear both buffers)
		for (int i = 0; i < 2; i++) {
			Graphics2D g = screen.getGraphics();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
			screen.update();
		}

	}

	public void update(long elapsedTime) {

		// check options
		if (exit.isPressed()) {
			stop();
			return;
		}
		if (largerView.isPressed()) {
			setViewBounds(viewWindow.getWidth() + 64,
					viewWindow.getHeight() + 48);
		} else if (smallerView.isPressed()) {
			setViewBounds(viewWindow.getWidth() - 64,
					viewWindow.getHeight() - 48);
		}
		if (frameRateToggle.isPressed()) {
			drawFrameRate = !drawFrameRate;
		}

		drawInstructionsTime += elapsedTime;
		if (drawInstructionsTime >= INSTRUCTIONS_TIME) {
			drawInstructions = false;
		}
		updateWorld(elapsedTime);
	}

	public void updateWorld(long elapsedTime) {

		// cap elapsedTime
		elapsedTime = Math.min(elapsedTime, 100);

		float angleChange = 0.0002f * elapsedTime;
		float distanceChange = .5f * elapsedTime;

		Transform3D camera = polygonRenderer.getCamera();
		Vector3D cameraLoc = camera.getLocation();

		// apply movement
		if (goForward.isPressed()) {
			cameraLoc.x -= distanceChange * camera.getSinAngleY();
			cameraLoc.z -= distanceChange * camera.getCosAngleY();
		}
		if (goBackward.isPressed()) {
			cameraLoc.x += distanceChange * camera.getSinAngleY();
			cameraLoc.z += distanceChange * camera.getCosAngleY();
		}
		if (goLeft.isPressed()) {
			cameraLoc.x -= distanceChange * camera.getCosAngleY();
			cameraLoc.z += distanceChange * camera.getSinAngleY();
		}
		if (goRight.isPressed()) {
			cameraLoc.x += distanceChange * camera.getCosAngleY();
			cameraLoc.z -= distanceChange * camera.getSinAngleY();
		}
		if (goUp.isPressed()) {
			cameraLoc.y += distanceChange;
		}
		if (goDown.isPressed()) {
			cameraLoc.y -= distanceChange;
		}

		// look up/down (rotate around x)
		int tilt = tiltUp.getAmount() - tiltDown.getAmount();
		tilt = Math.min(tilt, 200);
		tilt = Math.max(tilt, -200);

		// limit how far you can look up/down
		float newAngleX = camera.getAngleX() + tilt * angleChange;
		newAngleX = Math.max(newAngleX, (float) -Math.PI / 2);
		newAngleX = Math.min(newAngleX, (float) Math.PI / 2);
		camera.setAngleX(newAngleX);

		// turn (rotate around y)
		int turn = turnLeft.getAmount() - turnRight.getAmount();
		turn = Math.min(turn, 200);
		turn = Math.max(turn, -200);
		camera.rotateAngleY(turn * angleChange);

		// tilet head left/right (rotate around z)
		if (tiltLeft.isPressed()) {
			camera.rotateAngleZ(10 * angleChange);
		}
		if (tiltRight.isPressed()) {
			camera.rotateAngleZ(-10 * angleChange);
		}
	}

//	public void draw(Graphics2D g) {
//		int viewX1 = viewWindow.getLeftOffset();
//		int viewY1 = viewWindow.getTopOffset();
//		int viewX2 = viewX1 + viewWindow.getWidth();
//		int viewY2 = viewY1 + viewWindow.getHeight();
//		if (viewX1 != 0 || viewY1 != 0) {
//			g.setColor(Color.BLACK);
//			g.fillRect(0, 0, viewX1, Constants.HEIGHT);
//			g.fillRect(viewX2, 0, Constants.WIDTH - viewX2, Constants.HEIGHT);
//			g.fillRect(viewX1, 0, viewWindow.getWidth(), viewY1);
//			g.fillRect(viewX1, viewY2, viewWindow.getWidth(), Constants.HEIGHT
//					- viewY2);
//		}
//
//		drawPolygons(g);
//	}
//
//	public void drawPolygons(Graphics2D g) {
//		polygonRenderer.startFrame(g);
//		for (int i = 0; i < polygons.size(); i++) {
//			polygonRenderer.draw(g, (Polygon3D) polygons.get(i));
//		}
//		polygonRenderer.endFrame(g);
//	}

	public void calcFrameRate() {
		numFrames++;
		long currTime = System.currentTimeMillis();

		// calculate the frame rate every 500 milliseconds
		if (currTime > startTime + 500) {
			frameRate = (float) numFrames * 1000 / (currTime - startTime);
			startTime = currTime;
			numFrames = 0;
		}
	}

}