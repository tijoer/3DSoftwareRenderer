package com.masr.engine;

import com.masr.math.Matrix4f;

/**
 *
 * @author Tim Jörgen
 */
class Viewport {

	private Matrix4f projectionMatrix;
	private Matrix4f modelMatrix;
	private Matrix4f viewMatrix; //also known as camera matrix
	private final float zNear;
	private final float zFar;
	private final float viewAngle;
	private final float width;
	private final float height;

	/**
	 * Constructs a projection matrix.
	 *
	 * @param left the left-hand side clipping plane in camera space
	 * @param right the right-hand side clipping plane in camera space
	 * @param bottom the lower clipping plane in camera space
	 * @param top the upper clipping plane in camera space
	 * @param near the closer clipping plane in camera space
	 * @param far the clipping plane further away in camera space
	 */
	public Matrix4f perspectiveFrustum(
		float left, float right,
		float bottom, float top,
		float near, float far) {
		Matrix4f projection = new Matrix4f();

		// note the signature: set(COLUMN, ROW, value)
		// it may be different in the matrix implementation that you use

		projection.set(0, 0, (2f * near) / (right - left));
		projection.set(2, 0, (right + left) / (right - left));

		projection.set(1, 1, (2 * near) / (top - bottom));
		projection.set(2, 1, (top + bottom) / (top - bottom));

		projection.set(2, 2, -(far + near) / (far - near));
		projection.set(3, 2, -2 * (far * near) / (far - near));

		projection.set(2, 3, -1);
		projection.set(3, 3, 0);

		return projection;
	}

	/**
	 * Constructs a projection matrix out of a field-of-view angle
	 *
	 * @param viewAngle field-of-view angle in degrees
	 * @param width the width of the screen in camera space
	 * @param height the height of the screen in camera space
	 * @param nearClippingPlaneDistance the near clipping plane (projection
	 * plane)
	 * @param farClippingPlaneDistance the far clipping plane
	 */
	private Matrix4f createProjectionMatrix(
		float viewAngle,
		float width, float height,
		float nearClippingPlaneDistance, float farClippingPlaneDistance) {
		// convert angle from degree to radians
		final float radians = (float) (viewAngle * Math.PI / 180f);

		float halfHeight = (float) (Math.tan(radians / 2) * nearClippingPlaneDistance);

		float halfScaledAspectRatio = halfHeight * (width / height);

		Matrix4f projection = perspectiveFrustum(-halfScaledAspectRatio, halfScaledAspectRatio, -halfHeight, halfHeight, nearClippingPlaneDistance, farClippingPlaneDistance);

		return projection;
	}

	// Viewport(int x, int y, int width, int height, float zNear, float zFar, float viewAngle) {
	public Viewport(float viewAngle, float width, float height, float zNear, float zFar) {
		this.viewAngle = viewAngle;
		this.width = width;
		this.height = height;
		this.zNear = zNear;
		this.zFar = zFar;
		
		float w = (float) (1.0 / Math.tan(Math.toRadians(viewAngle) / 2));
		float h = (float) (1.0 / Math.tan(Math.toRadians(viewAngle) / 2));
		float q = zFar / (zFar - zNear);

		this.projectionMatrix = new Matrix4f(
			w, 0, 0, 0,
			0, h, 0, 0,
			0, 0, q, -q * zNear,
			0, 0, -1, 0);

//		this.projectionMatrix = createProjectionMatrix(viewAngle, width, height, zNear, zFar);

		this.modelMatrix = new Matrix4f(
			1, 0, 0, 0,
			0, 1, 0, 0,
			0, 0, 1, 0,
			0, 0, 0, 1);

		this.viewMatrix = new Matrix4f(
			1, 0, 0, 0,
			0, 1, 0, 0,
			0, 0, 1, 0,
			0, 0, 0, 1);
	}

	public Matrix4f getModelMatrix() {
		return modelMatrix;
	}

	public void setModelMatrix(Matrix4f modelMatrix) {
		this.modelMatrix = modelMatrix;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public void setProjectionMatrix(Matrix4f projectionMatrix) {
		this.projectionMatrix = projectionMatrix;
	}

	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}

	public void setViewMatrix(Matrix4f viewMatrix) {
		this.viewMatrix = viewMatrix;
	}

	public float getzFar() {
		return zFar;
	}

	public float getzNear() {
		return zNear;
	}

	public float getViewAngle() {
		return viewAngle;
	}

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}	
}
