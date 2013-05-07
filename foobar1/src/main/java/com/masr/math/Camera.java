package com.masr.math;

import com.masr.util.Tools;

/**
 * <h2> The Camera Class</h2>
 * This initilises a simple camera on the given location. There is no framerate
 * checking done, this has to be done through an input class (or something else).
 * 
 * This class is a <b>singleton</b> so you can not use the constructor. Use 
 * getInstance instead.
 * 
 * To change the speed of the camera change the var called speed.
 * 
 * @author Tim Joergen
 */
public final class Camera {

    private static Camera instance;
//    private static GL2 gl;
//    private static GLU glu;
    private static Vector3f newPosition;
    private static float newXyAngle,  newXzAngle;
    
    public static Vector3f position,  direction;
    public static float xzAngle,  xyAngle;
    public static float speed = 14.0f;

    public static Camera getInstance(//GL2 gl, GLU glu
		) {
        if (Camera.instance == null) {
            Camera.instance = new Camera(//gl, glu
				);
        }
        return Camera.instance;
    }
    

    private Camera(//GL2 gl, GLU glu
		) {
//        Camera.gl = gl;
//        Camera.glu = glu;

        xzAngle = 0.0f;
        xyAngle = 0.0f;

        Camera.position = new Vector3f(0.0f, 0.0f, 0.0f);
        Camera.direction = new Vector3f(0.0f, 0.0f, 1.0f);
    }

    public void rotateAccordingToCameraPosition() {
        if (Camera.xyAngle < -180.0f) {
            Camera.xyAngle = 180.0f;
        }
        if (Camera.xyAngle > 180.0f) {
            Camera.xyAngle = -180.0f;
        }
        if (Camera.xzAngle < -180.0f) {
            Camera.xzAngle = 180.0f;
        }
        if (Camera.xzAngle > 180.0f) {
            Camera.xzAngle = -180.0f;
        }
//        gl.glRotatef(Camera.xyAngle, 1.0f, 0.0f, 0.0f);
//        gl.glRotatef(Camera.xzAngle, 0.0f, 1.0f, 0.0f);
    }

    public void translateAccordingToCameraPosition() {
        if (Camera.cameraMoving == true) {
            smoothMoveTo();
        }
//        gl.glTranslatef(-position.x, -position.y, -position.z);
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        Camera.direction = direction;
    }

    public static Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        Camera.position = position;
    }

    public void forward() {
        Vector3f move = new Vector3f(0.0f, 0.0f, 0.0f);
        float x, y, z;

        x = (float) Math.sin((180.0f + Camera.xzAngle) * Tools.PIOVER180);
        y = -(float) Math.sin((180.0f + Camera.xyAngle) * Tools.PIOVER180);
        z = -(float) Math.cos((180.0f + Camera.xzAngle) * Tools.PIOVER180);

        move.x = x;
        move.y = y;
        move.z = z;
        move.normalize();
        move.multLocal(Camera.speed);

        Camera.position.subtractLocal(move);
    }

    public void backward() {
        Vector3f move = new Vector3f(0.0f, 0.0f, 0.0f);
        float x, y, z;

        x = (float) Math.sin((0.0f + Camera.xzAngle) * Tools.PIOVER180);
        y = -(float) Math.sin((0.0f + Camera.xyAngle) * Tools.PIOVER180);
        z = -(float) Math.cos((0.0f + Camera.xzAngle) * Tools.PIOVER180);

        move.x = x;
        move.y = y;
        move.z = z;
        move.normalize();
        move.multLocal(Camera.speed);

        Camera.position.subtractLocal(move);
    }

    void up() {
        Camera.position.addLocal(new Vector3f(0.0f, 1.0f * Camera.speed, 0.0f));
    }

    void down() {
        Camera.position.subtractLocal(new Vector3f(0.0f, 1.0f * Camera.speed, 0.0f));
    }

    void turnLeft(int dx) {
        Camera.xzAngle -= 0.5f * dx;
        updateDirection();
    }

    void turnRight(int dx) {
        Camera.xzAngle += 0.5f * dx;
        updateDirection();
    }

    void turnUp(int dy) {
        Camera.xyAngle -= 0.5f * dy;
        updateDirection();
    }

    void turnDown(int dy) {
        Camera.xyAngle += 0.5f * dy;
        updateDirection();
    }

    public void slideLeft() {
        Vector3f slide = new Vector3f(0.0f, 0.0f, 0.0f);
        float x;
        float z;

        x = (float) Math.sin((90.0f + Camera.xzAngle) * Tools.PIOVER180);
        z = -(float) Math.cos((90.0f + Camera.xzAngle) * Tools.PIOVER180);

        slide.x = x;
        slide.z = z;
        slide.normalize();
        slide.multLocal(Camera.speed);

        Camera.position.subtractLocal(slide);
    }

    public void slideRight() {
        Vector3f slide = new Vector3f(0.0f, 0.0f, 0.0f);
        float x;
        float z;

        x = (float) Math.sin((270.0f + Camera.xzAngle) * Tools.PIOVER180);
        z = -(float) Math.cos((270.0f + Camera.xzAngle) * Tools.PIOVER180);

        slide.x = x;
        slide.z = z;
        slide.normalize();
        slide.multLocal(Camera.speed);

        Camera.position.subtractLocal(slide);
    }

    private void updateDirection() {
        float x;
        float y = 0.0f;
        float z;
        x = -(float) Math.sin(degreeToRadian(Camera.xzAngle));
        y = (float) Math.sin(degreeToRadian(Camera.xyAngle));
        z = (float) Math.cos(degreeToRadian(Camera.xzAngle));

        Camera.direction.x = x;
        Camera.direction.y = y;
        Camera.direction.z = z;
    }

    double degreeToRadian(float angle) {
        return angle * Math.PI / 180.0f;
    }
    static boolean cameraMoving = false;

    public static void smoothMoveTo() {
        if (Math.abs(Camera.position.x - newPosition.x) < 33.0f &&
                Math.abs(Camera.position.y - newPosition.y) < 33.0f &&
                Math.abs(Camera.position.z - newPosition.z) < 33.0f &&
                Math.abs(Camera.xyAngle - newXyAngle) < 5.0f &&
                Math.abs(Camera.xzAngle - newXzAngle) < 5.0f) {
            cameraMoving = false;
            return;
        }

        float xFactor = -1;
        float yFactor = -1;
        float zFactor = -1;
        if ((Camera.position.x - newPosition.x) < 0) {
            xFactor = 1;
        }
        if ((Camera.position.y - newPosition.y) < 0) {
            yFactor = 1;
        }
        if ((Camera.position.z - newPosition.z) < 0) {
            zFactor = 1;
        }
        Camera.position.x = Camera.position.x + 16.0f * xFactor;
        Camera.position.y = Camera.position.y + 16.0f * yFactor;
        Camera.position.z = Camera.position.z + 16.0f * zFactor;

        float xyAngleFactor = -1;
        if ((Math.abs(newXyAngle - Camera.xyAngle) > 2.0f)) {
            if (Camera.xyAngle - newXyAngle > 0) {
                xyAngleFactor = 1;
            }
            Camera.xyAngle -= 1.0f * xyAngleFactor;
        }

        float xzAngleFactor = -1;
        if ((Math.abs(newXzAngle - Camera.xzAngle) > 2.0f)) {
            if (Camera.xzAngle - newXzAngle > 0) {
                xzAngleFactor = 1;
            }
            Camera.xzAngle -= (1.0f * xzAngleFactor);
        }
    }

    public static void smoothMoveTo(Vector3f newPosition, float xyAngle, float xzAngle) {
        Camera.newPosition = newPosition;
        Camera.newXyAngle = xyAngle;
        Camera.newXzAngle = xzAngle;
        Camera.cameraMoving = true;
    }
}
