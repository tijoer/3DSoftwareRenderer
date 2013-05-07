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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OldMain extends GameCore3D {

    private static final int NUM_BOTS = 500;
    private static final int GAME_AREA_SIZE = 1500;
    private static final float PLAYER_SPEED = .5f;
    private static final float PLAYER_TURN_SPEED = 0.04f;
    private static final float BULLET_HEIGHT = 75;
    protected GameAction fire = new GameAction("fire",
            GameAction.DETECT_INITAL_PRESS_ONLY);
    private PolygonGroup robotModel;
    private PolygonGroup powerUpModel;
    private PolygonGroup blastModel;
    private GameObjectManager gameObjectManager;
    private TexturedPolygon3D floor;
    long firstFrame;
    int frames;
    long currentFrame;
    int fps;

    public static void main(String[] args) {
        new OldMain().run();
    }

    @Override
    public void init() {
        super.init();

        inputManager.mapToKey(fire, KeyEvent.VK_SPACE);
        inputManager.mapToMouse(fire, InputManager.MOUSE_BUTTON_1);
    }

    @Override
    public void createPolygons() {

        // create floor
		Texture floorTexture = null;
		try {
			floorTexture = Texture.createTexture("/images/roof1.png", true);
		} catch (IOException ex) {
			Logger.getLogger(OldMain.class.getName()).log(Level.SEVERE, null, ex);
			return;
		}
//        Texture floorTexture = Texture.createTexture("images/roof1.png", true);
        ((ShadedTexture) floorTexture).setDefaultShadeLevel(ShadedTexture.MAX_LEVEL * 3 / 4);
        Rectangle3D floorTextureBounds = new Rectangle3D(new Vector3D(0, 0, 0),
                new Vector3D(1, 0, 0), new Vector3D(0, 0, 1),
                floorTexture.getWidth(), floorTexture.getHeight());
        float s = GAME_AREA_SIZE;
        floor = new TexturedPolygon3D(new Vector3D[]{new Vector3D(-s, 0, s),
                    new Vector3D(s, 0, s), new Vector3D(s, 0, -s),
                    new Vector3D(-s, 0, -s)});
        floor.setTexture(floorTexture, floorTextureBounds);

        // set up the local lights for the model.
        float ambientLightIntensity = .5f;
        List<PointLight3D> lights = new LinkedList<>();
        lights.add(new PointLight3D(-100, 100, 100, .5f, -1));
        lights.add(new PointLight3D(100, 100, 0, .5f, -1));

        // load the object models
        ObjectLoader loader = new ObjectLoader();
        loader.setLights(lights, ambientLightIntensity);
        try {
            robotModel = loader.loadObject("/images/robot.obj");
            powerUpModel = loader.loadObject("/images/cube.obj");
            blastModel = loader.loadObject("/images/blast.obj");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // create game objects
        gameObjectManager = new SimpleGameObjectManager();
        gameObjectManager.addPlayer(new GameObject(new PolygonGroup("Player")));
        gameObjectManager.getPlayer().getLocation().y = 5;

        for (int i = 0; i < NUM_BOTS; i++) {
            Bot object = new Bot((PolygonGroup) robotModel.clone());
            placeObject(object);
        }
    }

    // randomly place objects in game area
    public void placeObject(GameObject object) {
        float size = GAME_AREA_SIZE;
        object.getLocation().setTo((float) (Math.random() * size - size / 2),
                0, (float) (Math.random() * size - size / 2));
        gameObjectManager.add(object);
    }

    @Override
    public void createPolygonRenderer() {
        viewWindow = new ViewWindow(0, 0, Constants.WIDTH,
                Constants.HEIGHT, (float) Math.toRadians(75));

        Transform3D camera = new Transform3D();
        polygonRenderer = new ZBufferedRenderer(camera, viewWindow);
    }

    @Override
    public void updateWorld(long elapsedTime) {

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
        if (fire.isPressed()) {
            float cosX = playerTransform.getCosAngleX();
            float sinX = playerTransform.getSinAngleX();
            Blast blast = new Blast((PolygonGroup) blastModel.clone(),
                    new Vector3D(cosX * x, sinX, cosX * z));
            // blast starting location needs work. looks like
            // the blast is coming out of your forehead when
            // you're shooting down.
            blast.getLocation().setTo(player.getX(),
                    player.getY() + BULLET_HEIGHT, player.getZ());
            gameObjectManager.add(blast);
        }

        velocity.multiply(PLAYER_SPEED);
        playerTransform.setVelocity(velocity);

        // look up/down (rotate around x)
        angleVelocity = Math.min(tiltUp.getAmount(), 200);
        angleVelocity += Math.max(-tiltDown.getAmount(), -200);
        playerTransform.setAngleVelocityX(angleVelocity * PLAYER_TURN_SPEED
                / 200);

        // turn (rotate around y)
        angleVelocity = Math.min(turnLeft.getAmount(), 200);
        angleVelocity += Math.max(-turnRight.getAmount(), -200);
        playerTransform.setAngleVelocityY(angleVelocity * PLAYER_TURN_SPEED
                / 200);

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

        // set the camera to be 100 units above the player
        Transform3D camera = polygonRenderer.getCamera();
        camera.setTo(playerTransform);
        camera.getLocation().add(0, 100, 0);

    }

    @Override
    public void draw(Graphics2D g) {
        polygonRenderer.startFrame(g);

        // draw floor
        polygonRenderer.draw(g, floor);

        // draw objects
        gameObjectManager.draw(g, (GameObjectRenderer) polygonRenderer);

        polygonRenderer.endFrame(g);

        calculateFps();
        if (debugWindow != null) {
            debugWindow.addOrSetInfo("FPS", "FPS: " + fps);
        }
    }

    int calculateFps() {
        frames++;
        currentFrame = System.currentTimeMillis();
        if (currentFrame > firstFrame + 1000) {
            firstFrame = currentFrame;
            fps = frames;
            frames = 0;
        }
        return fps;
    }
}