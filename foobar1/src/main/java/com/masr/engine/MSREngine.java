package com.masr.engine;

import com.masr.math.Matrix4f;
import com.masr.math.Triangle;
import com.masr.math.Vector3f;
import com.masr.math.Vector4f;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Tim Jörgen
 */
public class MSREngine {

	private RenderWindow renderWindow;
	private Viewport viewport;
	private HashMap<String, ArrayList<Triangle>> triangleLists = new HashMap<>();
	private ArrayList<Vector3f> vertices = new ArrayList<>();
	private Vector3f cameraPosition = new Vector3f();
	
	MSREngine(String title, int width, int height, float viewAngle) {
		this.renderWindow = new RenderWindow(title, width, height);
		this.renderWindow.setClearColor(Color.LIGHT_GRAY);
		this.viewport = new Viewport(viewAngle, width, height, 0.1f, 100.0f);
	}
	
	public void renderFrame() {
		Graphics2D renderGraphics = renderWindow.getRenderGraphics2D();
		renderGraphics.setColor(Color.RED);

		Matrix4f camera = new Matrix4f(
			1, 0, 0, this.cameraPosition.x,
			0, 1, 0, this.cameraPosition.y,
			0, 0, 1, this.cameraPosition.z,
			0, 0, 0, 1);
		viewport.setViewMatrix(camera);

		Matrix4f modelPosition = new Matrix4f(
			1, 0, 0, 0,
			0, 1, 0, 0,
			0, 0, 1, 0,
			0, 0, 0, 1);
		viewport.setModelMatrix(modelPosition);

		Matrix4f modelMatrix = viewport.getModelMatrix();
		Matrix4f viewMatrix = viewport.getViewMatrix();
		Matrix4f projectionMatrix = viewport.getProjectionMatrix();

		Matrix4f modelViewMatrix = viewMatrix.mult(modelMatrix);
		Matrix4f modelViewProjectionMatrix = projectionMatrix.mult(modelViewMatrix);

		boolean clipTri = false;

		ArrayList<Vector3f> screenSpaceVertices = new ArrayList<>(this.vertices.size());
		for (Vector3f vertex : this.vertices) {
			Vector4f point = new Vector4f(vertex.x, vertex.y, vertex.z, 1);

//			screenSpaceVertex = modelViewProjectionMatrix.mult(vertex);

			Vector4f eyeVertex = modelViewMatrix.mult(point);
			Vector4f clipVertex = projectionMatrix.mult(eyeVertex);

			float w = clipVertex.w;
			float normalizedX = clipVertex.x / w;
			float normalizedY = clipVertex.y / w;
			float normalizedZ = clipVertex.z / w;
			// TODO prüfung pro dreieck
			//if (normalizedZ < -1 || normalizedZ > 1) {
			//	clipTri = true;
			//	break;
			//}

			int screenX = (int) (viewport.getWidth() / 2 * normalizedX + viewport.getWidth() / 2);
			int screenY = (int) (viewport.getHeight() / 2 * normalizedY + viewport.getHeight() / 2);
			float screenZ = (viewport.getzFar() - viewport.getzNear()) / 2 * normalizedZ + (viewport.getzFar() + viewport.getzNear()) / 2;
			
			Vector3f screenSpaceVertex = new Vector3f(screenX, screenY, screenZ);
			screenSpaceVertices.add(screenSpaceVertex);
		}
		
		//TODO prüfung pro dreieck
		//if(clipTri)
		//		continue;

		for (int i = 0; i < screenSpaceVertices.size(); i += 3) {
			Vector3f vertex0, vertex1, vertex2;
			vertex0 = screenSpaceVertices.get(i);
			vertex1 = screenSpaceVertices.get(i + 1);
			vertex2 = screenSpaceVertices.get(i + 2);

			int xPoints[] = new int[3];
			xPoints[0] = (int) vertex0.x;
			xPoints[1] = (int) vertex1.x;
			xPoints[2] = (int) vertex2.x;

			int yPoints[] = new int[3];
			yPoints[0] = (int) vertex0.y;
			yPoints[1] = (int) vertex1.y;
			yPoints[2] = (int) vertex2.y;
			
			renderGraphics.drawPolygon(xPoints, yPoints, 3);
		}

		renderGraphics.drawChars("hallo".toCharArray(), 0, 5, 40, 40);

		renderWindow.displayFrame();
	}

	void clearScreen() {
		renderWindow.clearScreen();
	}

	void addTriangleList(String name, ArrayList<Triangle> triangles) {
		this.triangleLists.put(name, triangles);
	}

	public void setVertices(ArrayList<Vector3f> vertices) {
		this.vertices = vertices;
	}

	void setCameraPosition(Vector3f position) {
		this.cameraPosition = position;
	}

	public Component getComponent() {
		return this.renderWindow.getComponent();
	}
	
	//	public void translate(Vector3f vec) {
//		Matrix4f translationMatrix = new Matrix4f(
//			1, 0, 0, vec.x,
//			0, 1, 0, vec.y,
//			0, 0, 1, vec.z,
//			0, 0, 0, 1);
//
//		Matrix4f scaleMatrix = new Matrix4f(
//			vec.x, 0, 0, 0,
//			0, vec.y, 0, 0,
//			0, 0, vec.z, 0,
//			0, 0, 0, 1);
//	}
}
