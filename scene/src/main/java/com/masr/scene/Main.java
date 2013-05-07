package com.masr.scene;

import com.masr.engine.game.GameObject;
import com.masr.engine.game.GameObjectManager;
import com.masr.engine.game.GameObjectRenderer;
import com.masr.engine.game.SimpleGameObjectManager;
import com.masr.engine.graphics3D.ZBufferedRenderer;
import com.masr.engine.graphics3D.texture.ShadedTexture;
import com.masr.engine.graphics3D.texture.Texture;
import com.masr.engine.input.GameAction;
import com.masr.engine.input.InputManager;
import com.masr.engine.math3D.*;
import com.masr.engine.test.GameCore3D;
import com.masr.engine.util.Constants;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.md5reader2.md5.MD5Model;

public class Main extends GameCore3D {

	private static final int GAME_AREA_SIZE = 100;
	private static final float PLAYER_SPEED = .1f;
	private static final float PLAYER_TURN_SPEED = 0.04f;
	protected GameAction fire;
	protected GameAction printZBuffer;
	protected GameAction up;
	protected GameAction down;
	private GameObjectManager gameObjectManager;
	private ZBufferedRenderer zBufferedRenderer;
	//The following models are working w.o. problems :) 
	//private static final String MESH = "data/doom3/models/md5/heads/sarge/sarge.md5mesh";
	//private static final String ANIMATION = "data/doom3/models/md5/heads/sarge/sargeidle.md5anim";
	//private static final String MESH = "data/doom3/models/md5/monsters/imp/imp.md5mesh";
	//private static final String ANIMATION = "data/doom3/models/md5/monsters/imp/slash1.md5anim";
	//private static final String MESH = "data/doom3/models/md5/monsters/zombies/zsec_machinegun/zsecmachinegun.md5mesh";
	//private static final String ANIMATION = "data/doom3/models/md5/monsters/zombies/zsec_machinegun/machinegun_run.md5anim";
	//private static final String MESH = "data/doom3/models/md5/monsters/zombies/morgue/morgue.md5mesh";
	//private static final String ANIMATION = "data/doom3/models/md5/monsters/zombies/morgue/idle.md5anim";
	//private static final String MESH = "data/doom3/models/md5/monsters/zombies/bernie/bernie.md5mesh";
	//private static final String ANIMATION = "data/doom3/models/md5/monsters/zombies/bernie/stand.md5anim";
	//private static final String MESH = "data/doom3/models/md5/monsters/zfat/zfat.md5mesh";
	//private static final String ANIMATION = "data/doom3/models/md5/monsters/zfat/idle1.md5anim";
	//private static final String MESH = "data/doom3/models/md5/monsters/hellknight/hellknight.md5mesh";
	//private static final String ANIMATION = "data/doom3/models/md5/monsters/hellknight/idle2.md5anim";
	private static final String MESH = "/models/marine.md5mesh";
	private static final String ANIMATION = "/models/marscity_marine1_ver1_hq_primary.md5anim";
	private MD5Model model;
	private ArrayList<TexturedPolygon3D> md5ModelList = new ArrayList<>();

	public static void main(String[] args) {
		new Main().run();
	}

	@Override
	public void init() {
		super.init();
		fire = new GameAction("fire", GameAction.DETECT_INITAL_PRESS_ONLY);
		inputManager.mapToMouse(fire, InputManager.MOUSE_BUTTON_1);

		printZBuffer = new GameAction("printZBuffer", GameAction.DETECT_INITAL_PRESS_ONLY);
		inputManager.mapToKey(printZBuffer, KeyEvent.VK_1);

		up = new GameAction("up");
		inputManager.mapToKey(up, KeyEvent.VK_SPACE);

		down = new GameAction("down");
		inputManager.mapToKey(down, KeyEvent.VK_CONTROL);
		inputManager.mapToKey(down, KeyEvent.VK_C);
	}

	@Override
	public void createPolygons() {
//		createFloor();

		System.out.println("dir: " + System.getProperty("user.dir"));

		InputStream meshInputStream = Main.class.getResourceAsStream(MESH);
		InputStream animationInputStream = Main.class.getResourceAsStream(ANIMATION);

		model = new MD5Model(meshInputStream, animationInputStream);

		// create game objects
		gameObjectManager = new SimpleGameObjectManager();
		gameObjectManager.addPlayer(new GameObject(new PolygonGroup("Player")));
	}

	@Override
	public void createPolygonRenderer() {
		viewWindow = new ViewWindow(0, 0, Constants.WIDTH,
			Constants.HEIGHT, (float) Math.toRadians(75));

		Transform3D camera = new Transform3D();
		this.zBufferedRenderer = new ZBufferedRenderer(camera, viewWindow);
		polygonRenderer = zBufferedRenderer;
	}

	@Override
	public void updateWorld(long elapsedTime) {

		Transform3D camera = polygonRenderer.getCamera();
		float angleVelocity;

		// cap elapsedTime
		elapsedTime = Math.min(elapsedTime, 100);

		GameObject player = gameObjectManager.getPlayer();
		MovingTransform3D playerTransform = player.getTransform();
		Vector3D velocity = playerTransform.getVelocity();

		playerTransform.stop();
		float x = -playerTransform.getSinAngleY();
		float z = -playerTransform.getCosAngleY();
		if (goForward.isPressed()) {
			velocity.add(x, 0, z);
		}
		if (goBackward.isPressed()) {
			velocity.add(-x, 0, -z);
		}
		if (goLeft.isPressed()) {
			velocity.add(z, 0, -x);
		}
		if (goRight.isPressed()) {
			velocity.add(-z, 0, x);
		}

		velocity.multiply(PLAYER_SPEED);
		playerTransform.setVelocity(velocity);

		// look up/down (rotate around x)
		angleVelocity = Math.min(tiltUp.getAmount(), 200);
		angleVelocity += Math.max(-tiltDown.getAmount(), -200);
		playerTransform.setAngleVelocityX(angleVelocity * PLAYER_TURN_SPEED / 200);

		// turn (rotate around y)
		angleVelocity = Math.min(turnLeft.getAmount(), 200);
		angleVelocity += Math.max(-turnRight.getAmount(), -200);
		playerTransform.setAngleVelocityY(angleVelocity * PLAYER_TURN_SPEED / 200);

		// for now, mark the entire world as visible in this frame.
		gameObjectManager.markAllVisible();

		// update objects
		gameObjectManager.update(elapsedTime);

		// limit look up/down
		float angleX = playerTransform.getAngleX();
		float limit = (float) Math.PI / 2;
		if (angleX < -limit) {
			playerTransform.setAngleX(-limit);
		} else if (angleX > limit) {
			playerTransform.setAngleX(limit);
		}

		camera.setTo(playerTransform);
		camera.getLocation().add(0, 10, 0);

		if (up.isPressed()) {
			player.getLocation().add(0, PLAYER_SPEED, 0);
		}
		if (down.isPressed()) {
			player.getLocation().add(0, -PLAYER_SPEED, 0);
		}

		if (printZBuffer.isPressed()) {
			zBufferedRenderer.getZBuffer().displayZBufferInNewWindow();
		}


		//model.drawMeshNew(this.model.getMeshes()[0], 0);
		//model.animate();

		this.md5ModelList.clear();
		for (int i = -1; i < 4; i++) {
			Texture floorTexture = null;
			try {
				floorTexture = Texture.createTexture("/images/roof1.png", true);
			} catch (IOException ex) {
				Logger.getLogger(OldMain.class.getName()).log(Level.SEVERE, null, ex);
			}
			((ShadedTexture) floorTexture).setDefaultShadeLevel(ShadedTexture.MAX_LEVEL * 3 / 4);
			Rectangle3D floorTextureBounds = new Rectangle3D(new Vector3D(0, 0, 0),
				new Vector3D(1, 0, 0), new Vector3D(0, 0, 1),
				floorTexture.getWidth(), floorTexture.getHeight());
			float s = GAME_AREA_SIZE;

			TexturedPolygon3D newPolygon = new TexturedPolygon3D(new Vector3D[]{new Vector3D(-10, i, 10),
					new Vector3D(10, i, 10),
					new Vector3D(10, i, -10),
					new Vector3D(-10, i, -10)});
			newPolygon.setTexture(floorTexture, floorTextureBounds);
			this.md5ModelList.add(newPolygon);
		}
	}

	@Override
	public void draw(Graphics2D g) {
		polygonRenderer.startFrame(g);

//		// draw floor
//		polygonRenderer.draw(g, floor);
//		
		//draw MD5 model
		for (TexturedPolygon3D polygon : this.md5ModelList) {
			polygonRenderer.draw(g, polygon);
		}

		// draw objects
		gameObjectManager.draw(g, (GameObjectRenderer) polygonRenderer);

		polygonRenderer.endFrame(g);

		if (debugWindow != null) {
			debugWindow.addOrSetInfo("FPS", "FPS: " + polygonRenderer.getFps());
		}
	}
}