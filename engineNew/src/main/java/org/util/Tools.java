/*
 * This is intellectual property. You are not allowed 
 * to use it in any way, except you have a written 
 * allowance by the owner.
 */
package org.util;

//import javax.media.opengl.GL2;
//import javax.media.opengl.glu.GLU;

/**
 * This is a helper Class, that contains methods, that are nearly alywase 
 * needed.
 * 
 * @author Tim Jörgen
 */
public class Tools {
    
//    private GL2 gl;
//    private GLU glu;
    private static long lastTime;
    public static int fps;
    private long startTime;
    private long timePassed;
    private long currentTime;
    public static int currentFrame = 0;
    public static long movementFactor;
    private static Tools instance = null;
    public static final float PIOVER180 = 0.0174532925f;
    private int fpsPassed = 0;
    private long lastSecond = 0;

    public Tools(//GL2 gl
		) {
//        this.gl = gl;
//        this.glu = new GLU();
        lastTime = System.currentTimeMillis();
        startTime = System.currentTimeMillis();
        lastSecond = System.currentTimeMillis();
        fps = 0;
    }

    public static Tools getInstance() {
        return instance;
    }

    static double degreeToRadian(float angle) {
        return angle * Math.PI / 180.0f;
    }

    public void calculateFramerate() {
        currentTime = System.currentTimeMillis();
        timePassed = currentTime - startTime;
        movementFactor = (long) (timePassed / 15);
        currentFrame++;
        
        if(currentTime - lastSecond >= 1000) {
            fps = fpsPassed;
            lastSecond = currentTime;
            fpsPassed = 0;
        }
        
        fpsPassed++;
    }
    
    public void createRayFromDisplayCoordinates(int x, int y) {
        Vector3f start = new Vector3f(),
                end = new Vector3f();

        int viewport[] = new int[4];
        double mvmatrix[] = new double[16];
        double projmatrix[] = new double[16];
        int realy = 0;// GL y coord pos

        double wcoord[] = new double[4];
//
//        gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);
//        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, mvmatrix, 0);
//        gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projmatrix, 0);
        // note viewport[3] is height of window in pixels
        realy = viewport[3] - (int) y - 1;
//        glu.gluUnProject((double) x, (double) realy, 0.0,
//                mvmatrix, 0,
//                projmatrix, 0,
//                viewport, 0,
//                wcoord, 0);
        start.x = (float) wcoord[0];
        start.y = (float) wcoord[1];
        start.z = (float) wcoord[2];

        //ByteBuffer buffer = BufferUtil.newByteBuffer(4);
        //gl.glReadPixels(x, y, 1, 1, GL.GL_DEPTH_COMPONENT, GL.GL_FLOAT, buffer);
        //System.out.println("zValue: " + buffer.asFloatBuffer().get(0));
//        glu.gluUnProject((double) x, (double) realy, 1.0, //
//                mvmatrix, 0,//
//                projmatrix, 0,//
//                viewport, 0, //
//                wcoord, 0);
        end.x = (float) wcoord[0];
        end.y = (float) wcoord[1];
        end.z = (float) wcoord[2];

        //return this.heightMap.testClickOnMap(start, end);
        
    //if(!) {
    //    this.checkForMouseClickOnWorld = false;
    //}
    }
}