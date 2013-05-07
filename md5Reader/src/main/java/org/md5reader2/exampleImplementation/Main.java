///*
// * This is intellectual property. You are not allowed 
// * to use it in any way, except you have a written 
// * allowance by the owner.
// */
//package org.md5reader2.exampleImplementation;
//
//import java.awt.Component;
//import java.awt.Frame;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//
//import javax.media.opengl.GL2;
//import javax.media.opengl.GLAutoDrawable;
//import javax.media.opengl.GLCapabilities;
//import javax.media.opengl.GLEventListener;
//import javax.media.opengl.GLProfile;
//import javax.media.opengl.awt.GLCanvas;
//import javax.media.opengl.glu.GLU;
//
//import org.util.Input;
//import org.util.Tools;
//
//import com.jogamp.opengl.util.Animator;
//
//
//public class Main implements GLEventListener {
//	
//	GL2 gl;
//	GLU glu = new GLU();
//	static Frame frame;
//	static Animator animator; 
//	
//	Input input;
//	Tools tools;
//	public static int scene = 0;
//	private Scene0 scene0;
//	
//	public static void main(String[] args) {
//		System.setProperty("java.library.path", ".:lib/");
//		
//		frame = new Frame("");
//		GLProfile glp = GLProfile.get(GLProfile.GL2);
//		GLCapabilities capabilities = new GLCapabilities(glp);
//		capabilities.setStencilBits(8);
//		GLCanvas canvas = new GLCanvas(capabilities);
//		// GLCanvas canvas = new GLCanvas();
//		
//		canvas.addGLEventListener(new Main());
//		frame.add(canvas);
//		frame.setSize(1024, 768);
//		animator = new Animator(canvas);
//		frame.addWindowListener(new WindowAdapter() {
//			
//			@Override
//			public void windowClosing(WindowEvent e) {
//				// Run this on another thread than the AWT event queue to
//				// make sure the call to Animator.stop() completes before
//				// exiting
//				new Thread(new Runnable() {
//					
//					public void run() {
//						animator.stop();
//						System.exit(0);
//					}
//				}).start();
//			}
//		});
//		// Center frame
//		frame.setLocationRelativeTo(null);
//		frame.setVisible(true);
//		animator.start();
//	}
//	
//	public void init(GLAutoDrawable gLAutoDrawable) {
//		// Use debug pipeline
//		// drawable.setGL(new DebugGL(drawable.getGL()));
//		gl = gLAutoDrawable.getGL().getGL2();
//		
//		// Setup the drawing area and shading mode
//		gl.glEnable(GL2.GL_CULL_FACE);
//		// gl.glDisable(GL.GL_CULL_FACE);
//		gl.glCullFace(GL2.GL_BACK);
//		gl.glFrontFace(GL2.GL_CCW);
//		gl.glShadeModel(GL2.GL_SMOOTH);
//		gl.glClearColor(0.3f, 0.3f, 0.5f, 1.0f);
//		gl.glClearDepth(1.0f);
//		gl.glEnable(GL2.GL_DEPTH_TEST);
//		gl.glDepthFunc(GL2.GL_LEQUAL);
//		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
//		gl.glEnable(GL2.GL_TEXTURE_2D);
//		
//		gl.setSwapInterval(0);
//		
//		this.input = new Input(gl, glu);
//		((Component) gLAutoDrawable).addKeyListener(input);
//		((Component) gLAutoDrawable).addMouseListener(input);
//		((Component) gLAutoDrawable).addMouseMotionListener(input);
//		this.gl = gLAutoDrawable.getGL().getGL2();
//		this.tools = new Tools(gl);
//		
//		scene0 = new Scene0(gl, glu);
//		
//	}
//	
//	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
//		gl = drawable.getGL().getGL2();
//		
//		if (height <= 0) { // avoid a divide by zero error
//		
//			height = 1;
//		}
//		final float h = (float) width / (float) height;
//		gl.glViewport(0, 0, width, height);
//		gl.glMatrixMode(GL2.GL_PROJECTION);
//		gl.glLoadIdentity();
//		glu.gluPerspective(45.0f, h, 5.00, 5000.0);
//		gl.glMatrixMode(GL2.GL_MODELVIEW);
//		gl.glLoadIdentity();
//	}
//	
//	/**
//	 * OpenGL entry Point is here.
//	 */
//	public void display(GLAutoDrawable drawable) {
//		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
//		gl.glLoadIdentity();
//		switch (scene) {
//		case 0:
//			scene0.draw();
//			break;
//		default:
//			break;
//		}
//		
//		tools.calculateFramerate();
//		frame.setTitle("MD5 Loader Example - fps: " + Tools.fps);
//	}
//	
//	}
//
//	public void dispose(GLAutoDrawable arg0) {
//		animator.stop();
//      frame.dispose();
//      System.exit(0);
//	}
//}
