/*
 * Copyright by Tim Jörgen.
 */
package com.masr.engine;

import com.masr.input.InputAction;
import com.masr.input.InputManager;
import com.masr.math.Vector3f;
import java.util.ArrayList;

/**
 *
 * @author Tim Jörgen
 */
public class Main {

	private InputManager inputManager;
	private DebugWindow debugWindow;
	private MSREngine msrEngine;
	private InputAction leftMouseAction;

	public static void main(String[] args) {
		Main main = new Main();
	}

	public Main() {
		this.debugWindow = new DebugWindow();
		this.msrEngine = new MSREngine("3DSR", 800, 600, 75);
		
		this.inputManager = new InputManager(this.msrEngine.getComponent());
		this.leftMouseAction = new InputAction("leftMouseAction", InputAction.DETECT_INITAL_PRESS_ONLY);
		this.inputManager.mapToMouse(leftMouseAction, InputManager.MOUSE_BUTTON_1);
		
		this.setupScene();
		this.renderLoop();
	}

	private void setupScene() {
		ArrayList<Vector3f> vertices = new ArrayList<>();
		Vector3f vertex0, vertex1, vertex2;

		vertex0 = new Vector3f(0, 0, 0);
		vertex1 = new Vector3f(0, 1, 0);
		vertex2 = new Vector3f(1, 0, 0);
		vertices.add(vertex0);
		vertices.add(vertex1);
		vertices.add(vertex2);

		vertex0 = new Vector3f(0, 1, 0);
		vertex1 = new Vector3f(1, 1, 0);
		vertex2 = new Vector3f(1, 0, 0);
		vertices.add(vertex0);
		vertices.add(vertex1);
		vertices.add(vertex2);

		vertex0 = new Vector3f(1, 0, 0);
		vertex1 = new Vector3f(1, 1, 0);
		vertex2 = new Vector3f(1, 0, -1);
		vertices.add(vertex0);
		vertices.add(vertex1);
		vertices.add(vertex2);

		vertex0 = new Vector3f(1, 1, 0);
		vertex1 = new Vector3f(1, 1, -1);
		vertex2 = new Vector3f(1, 0, -1);
		vertices.add(vertex0);
		vertices.add(vertex1);
		vertices.add(vertex2);

		vertex0 = new Vector3f(0, 0, 0);
		vertex1 = new Vector3f(0, 1, 0);
		vertex2 = new Vector3f(0, 0, -1);
		vertices.add(vertex0);
		vertices.add(vertex1);
		vertices.add(vertex2);

		vertex0 = new Vector3f(0, 1, 0);
		vertex1 = new Vector3f(0, 1, -1);
		vertex2 = new Vector3f(0, 0, -1);
		vertices.add(vertex0);
		vertices.add(vertex1);
		vertices.add(vertex2);

		vertex0 = new Vector3f(0, 0, -1);
		vertex1 = new Vector3f(0, 1, -1);
		vertex2 = new Vector3f(1, 0, -1);
		vertices.add(vertex0);
		vertices.add(vertex1);
		vertices.add(vertex2);

		vertex0 = new Vector3f(0, 1, -1);
		vertex1 = new Vector3f(1, 1, -1);
		vertex2 = new Vector3f(1, 0, -1);
		vertices.add(vertex0);
		vertices.add(vertex1);
		vertices.add(vertex2);

		vertex0 = new Vector3f(0, 1, -1);
		vertex1 = new Vector3f(1, 1, -1);
		vertex2 = new Vector3f(0, 1, 0);
		vertices.add(vertex0);
		vertices.add(vertex1);
		vertices.add(vertex2);

		vertex0 = new Vector3f(0, 1, 0);
		vertex1 = new Vector3f(1, 1, 0);
		vertex2 = new Vector3f(1, 1, -1);
		vertices.add(vertex0);
		vertices.add(vertex1);
		vertices.add(vertex2);

		this.msrEngine.setVertices(vertices);
		this.msrEngine.setCameraPosition(new Vector3f(-0.5f, -0.5f, 3));
	}

	private void renderLoop() {
		while (true) {
			this.reactToInput();
			this.msrEngine.clearScreen();
			this.msrEngine.renderFrame();
		}
	}

	private void reactToInput() {
		if(this.leftMouseAction.isPressed()) {
			System.out.println("LMB");
		}
	}
}
