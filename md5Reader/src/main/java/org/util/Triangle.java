/*
 * This is intellectual property. You are not allowed 
 * to use it in any way, except you have a written 
 * allowance by the owner.
 */
package org.util;

/**
 * <h3>Triangle</h3>
 *
 * This class represents a triangle. The vertices are stored in v0, v1 and v3.
 * The correspondending normals are n0, n1 and n2. triangleNormal is the normal
 * for the center of the triangle. It can calculated with a call of
 * calcNormal(), after the vertices are set. If it is not set manualy and
 * calcNormal() is not called, the normal will be (0.0f, 1.0f, 0.0f).
 *
 * This class also holds informations about its edges, which will be set
 * automatically, of you use the Triangle(Vector3f, Vector3f , Vector3f)
 * constructor.
 *
 * @author Tim Jörgen
 */
public class Triangle {

	public Vector3f v0, v1, v2;
	public Vector3f n0, n1, n2;
	public float v0u, v0v, v1u, v1v, v2u, v2v;
	public Edge edge0, edge1, edge2;
	public Vector3f normal = new Vector3f();
	public Vector3f tangent = new Vector3f();
	public Vector3f bitangent = new Vector3f();

	/**
	 * Creates a new triangle. Beware: most values are not initialised.
	 */
	public Triangle() {
	}

	/**
	 * Creates a new triangle. default for this.triangleNormal: (0, 1, 0)
	 *
	 * @param v0
	 * @param v1
	 * @param v2
	 */
	public Triangle(Vector3f v0, Vector3f v1, Vector3f v2) {
		this.v0 = v0;
		this.v1 = v1;
		this.v2 = v2;

//        this.edge0 = new Edge(v0, v1);
//        this.edge1 = new Edge(v1, v2);
//        this.edge2 = new Edge(v2, v0);

		this.normal = new Vector3f(0.0f, 1.0f, 0.0f);
	}

	public Triangle(Vector3f v0, Vector3f v1, Vector3f v2, float v0u, float v0v, float v1u, float v1v, float v2u, float v2v) {
		this.v0u = v0u;
		this.v0v = v0v;
		this.v1u = v1u;
		this.v1v = v1v;
		this.v2u = v2u;
		this.v2v = v2v;

		this.v0 = v0;
		this.v1 = v1;
		this.v2 = v2;

//        this.edge0 = new Edge(v0, v1);
//        this.edge1 = new Edge(v1, v2);
//        this.edge2 = new Edge(v2, v0);

		this.normal = new Vector3f(0.0f, 1.0f, 0.0f);
	}

	/**
	 * Calculates the normalized normal of this triangle. The triangleNormal
	 * will be saved in this.noramle, but also returned.
	 */
	public Vector3f calcNormal() {
		Vector3f c0 = new Vector3f();
		Vector3f c1 = new Vector3f();

		// Calculate The Vector From Point 1 To Point 0
		c0.x = v0.x - v1.x;  // Vector 1.x=Vertex[0].x-Vertex[1].x
		c0.y = v0.y - v1.y;  // Vector 1.y=Vertex[0].y-Vertex[1].y
		c0.z = v0.z - v1.z;  // Vector 1.z=Vertex[0].y-Vertex[1].z

		// Calculate The Vector From Point 2 To Point 1
		c1.x = v1.x - v2.x;  // Vector 2.x=Vertex[0].x-Vertex[1].x
		c1.y = v1.y - v2.y;  // Vector 2.y=Vertex[0].y-Vertex[1].y
		c1.z = v1.z - v2.z;  // Vector 2.z=Vertex[0].z-Vertex[1].z
		// Compute The Cross Product To Give Us A Surface Normal

		normal.x = c0.y * c1.z - c0.z * c1.y;  // Cross Product For Y - Z
		normal.y = c0.z * c1.x - c0.x * c1.z;  // Cross Product For X - Z
		normal.z = c0.x * c1.y - c0.y * c1.x;  // Cross Product For X - Y

		normal.x = -normal.x;
		normal.y = -normal.y;
		normal.z = -normal.z;
		normal.normalize();

		return normal;
	}
	Vector3f v1v0 = new Vector3f();
	Vector3f v2v0 = new Vector3f();

	public void calcTBN() {
		v1v0.set(v1);
		v1v0.subtractLocal(v0);
		v2v0.set(v2);
		v2v0.subtractLocal(v0);
		v1v0.normalize();
		v2v0.normalize();

		// Calculate c1c0_T and c1c0_B
		float c1c0_T = v1u - v0u;
		float c1c0_B = v1v - v0v;

		// Calculate c3c1_T and c3c1_B
		float c2c0_T = v2u - v0u;
		float c2c0_B = v2v - v0v;

		float fDenominator = c1c0_T * c2c0_B - c2c0_T * c1c0_B;
		float fScale1 = 1.0f / fDenominator;

		tangent.x = (c2c0_B * v1v0.x - c1c0_B * v2v0.x) * fScale1;
		tangent.y = (c2c0_B * v1v0.y - c1c0_B * v2v0.y) * fScale1;
		tangent.z = (c2c0_B * v1v0.z - c1c0_B * v2v0.z) * fScale1;
		tangent.normalize();

		bitangent.x = (-c2c0_T * v1v0.x + c1c0_T * v2v0.x) * fScale1;
		bitangent.y = (-c2c0_T * v1v0.y + c1c0_T * v2v0.y) * fScale1;
		bitangent.z = (-c2c0_T * v1v0.z + c1c0_T * v2v0.z) * fScale1;
		bitangent.normalize();

		normal.set(tangent);
		normal.crossLocal(bitangent);
		normal.normalize();
	}
}
