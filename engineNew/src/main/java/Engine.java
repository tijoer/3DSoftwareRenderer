

/*
 Simple Software 3D Java Rendering Engine
 (C) Dean Camera, 2007

 dean_camera (at} fourwalledcubicle [dot> com
 http://www.fourwalledcubicle.com
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.JFrame;

public final class Engine extends JFrame {

	private static Shape3D shapes[];
	private static Light Lights[];
	private static Thread Animator;
	private static BufferedImage RenderImage;
	private static Graphics2D RenderGraphics2D;

	public static void main(String[] args) {
		new Engine("Teapot 2.s3o");
	}

	public Engine(final String Filename) {
		setTitle("3D Engine Test");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(ScreenTrans.SCREEN_WIDTH, ScreenTrans.SCREEN_HEIGHT);
		setResizable(false);
		setVisible(true);
		createBufferStrategy(2);
		setIgnoreRepaint(true);

		RenderImage = new BufferedImage(ScreenTrans.SCREEN_WIDTH,
			ScreenTrans.SCREEN_HEIGHT,
			BufferedImage.TYPE_INT_RGB);
		RenderGraphics2D = (Graphics2D) RenderImage.getGraphics();

		if (LoadObjects(Filename) == true) {
			RenderLoop();
		}
	}

	private boolean LoadObjects(final String filename) {
		System.out.println("nLoading graphics objects...");

		shapes = new Shape3D[1];

		shapes[0] = S3OLoader.LoadS3OFile(filename);

		for (int i = (shapes.length - 1); i >= 0; --i) {
			if (shapes[i] == null) {
				System.out.println("One or more shapes null, aborting...");
				return false;
			}
		}

		System.out.println("Loaded graphics objects.\n");

		System.out.println("Locating objects in 3D space...");
		shapes[0].SetLocation(new Point(10, 10, -40));
		System.out.println("Objects located.\n");

		printNumOfFaces();

		System.out.println("Creating lights...");

		/*
		 * LIGHT CREATION
		 */
		Lights = new Light[3];

		Lights[0] = new Light(new Point(20, 20, -100), 1.0f, new Color(255, 255, 255), 5);
		Lights[1] = new Light(new Point(20, 50, 0), 1.0f, new Color(0, 0, 100));
		Lights[2] = new Light(new Point(5, 5, 20), 0.3f, new Color(50, 0, 0));

		System.out.println("  Ambient Light:"
			+ "\n     # Intensity: " + (ScreenTrans.SCREEN_AMBIENT_INT * 100) + "%"
			+ "\n     # Color:     R: " + ScreenTrans.SCREEN_AMBIENT.getRed()
			+ "  G: " + ScreenTrans.SCREEN_AMBIENT.getGreen()
			+ "  B: " + ScreenTrans.SCREEN_AMBIENT.getBlue()
			+ "\n\n  @ Total Lights:  " + Lights.length);


//		Color[] ColorList = new Color[(int) TotalVerticies];
//		for (int i = (ColorList.length - 1); i >= 0; --i) {
//			ColorList[i] =
//				new Color((int) (Math.random() * 255), (int) (Math.random() * 255),
//				(int) (Math.random() * 255));
//		}
//
//		for (int i = (Shapes.length - 1); i >= 0; --i) {
//			Shapes[i].ColorShape(ColorList);
//		}

		return true;
	}

	private void printNumOfFaces() {
		long totalPoints = 0;
		long totalVerticies = 0;

		for (int i = (shapes.length - 1); i >= 0; --i) {
			totalPoints += shapes[i].GetPoints().length;
			totalVerticies += shapes[i].GetTriangles().length;
		}

		System.out.println("Total Objects:  " + shapes.length
			+ "\nTotal Points:   " + totalPoints
			+ "\nTotal Faces:    " + totalVerticies);
	}

	private void RenderLoop() {
		BufferStrategy strategy = getBufferStrategy();

		final double RotXDegF = ((Math.PI / 180) * 2);
		final double RotYDegF = ((Math.PI / 180) * 1);

		final float RotXSin = (float) Math.sin(RotXDegF);
		final float RotXCos = (float) Math.cos(RotXDegF);
		final float RotYSin = (float) Math.sin(RotYDegF);
		final float RotYCos = (float) Math.cos(RotYDegF);

		final float RotLXSin = (float) Math.sin(-2 * RotXDegF);
		final float RotLXCos = (float) Math.cos(-2 * RotXDegF);
		final float RotLYSin = (float) Math.sin(3 * RotYDegF);
		final float RotLYCos = (float) Math.cos(3 * RotYDegF);

		long FPSTimer = System.currentTimeMillis();
		long FrameStartTime;

		int framesPerSec = 0;

		System.out.println("\n\n");

		for (;;) {
			FrameStartTime = System.currentTimeMillis();

			if (FrameStartTime >= (FPSTimer + 1000)) {
				FPSTimer = FrameStartTime;
				System.out.print("\rFrames per second: " + framesPerSec);
				framesPerSec = 0;
			}
			framesPerSec++;

			for (int i = (shapes.length - 1); i >= 0; --i) {
				shapes[i].RotatePointsX(RotXSin, RotXCos);
				shapes[i].RotatePointsY(RotYSin, RotYCos);
			}

			Lights[0].GetLocation().RotateX(RotLXSin, RotLXCos);
			Lights[0].GetLocation().RotateY(RotLYSin, RotLYCos);
			Lights[1].GetLocation().RotateX(RotLXSin, RotLXCos);

			Arrays.sort(shapes);

			RenderGraphics2D.setColor(ScreenTrans.SCREEN_BGCOLOR);
			RenderGraphics2D.fillRect(0, 0, ScreenTrans.SCREEN_WIDTH, ScreenTrans.SCREEN_HEIGHT);

			for (int i = (shapes.length - 1); i >= 0; --i) {
				shapes[i].drawShape(RenderGraphics2D, Lights);
			}

			LightManager.drawLights(RenderGraphics2D, Lights);

			Graphics Screen = strategy.getDrawGraphics();

			try {
				Screen.drawImage(RenderImage, 0, 0, null);
			} finally {
				Screen.dispose();
			}

			strategy.show();

			int frameRenderTime = (int) (System.currentTimeMillis() - FrameStartTime);

			if (frameRenderTime < 33) {
				try {
					Thread.sleep(33 - frameRenderTime);
				} catch (Exception e) {
					return;
				}
			}
		}
	}
}
