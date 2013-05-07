/**
 * 
 */
package org.md5reader2.md5;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.md5reader2.parser.MD5AnimParser;
import org.md5reader2.parser.MD5MeshParser;
import org.util.*;

/**
 * @author Marco Frisan, Tim Joergen
 * 
 */
public class MD5Model {

	private int md5Version; // MD5Version - should be 10
	private String commandLine; // commandline
	private int numJoints; // numJoints
	private int numMeshes; // numMeshes

	private MD5Joint[] joints;
	//private MD5TriMesh[] meshes;
	private MD5Mesh[] meshes;
	
	private MD5Animation animation;
//	private GL2 gl;
	private String meshName;
	private String animationName;
	
	private static Quaternion tmp = new Quaternion();
	
    public MD5AnimParser animParser;

    private Vector2f[][] uv;
//    private Shader shader;
    private Textures textureLoader;
    private org.util.Vector3f tangent = new org.util.Vector3f();
    private org.util.Vector3f normale = new org.util.Vector3f();
    private org.util.Vector3f binormale = new org.util.Vector3f();
    private org.util.Vector3f vertice0 = new org.util.Vector3f();
    private org.util.Vector3f vertice1 = new org.util.Vector3f();
    private org.util.Vector3f vertice2 = new org.util.Vector3f();
    private Vector3f[] vertices;
    private Vector3f finalVertex = new Vector3f(0.0f, 0.0f, 0.0f);
    private static Vector3f result = new Vector3f();
    private MD5BoundingBox[] boundings;
	

	/**
	 * @param animation2 
	 * @param mesh 
	 * @param gl 
	 */
    public MD5Model(InputStream meshFile, InputStream animFile) {
//        this.gl = gl;

        MD5MeshParser meshParser = new MD5MeshParser();
        meshParser.parseModel(meshFile, this);
        animParser = new MD5AnimParser(//gl,
			this);
        try {
        	animation = animParser.parseAnimation(animFile);
		} catch (IOException e) {
			System.out.println("This should not have happened... Check your animation file path.");
			e.printStackTrace();
		}

        this.textureLoader = Textures.getInstance();
//        try {
//            this.shader = new Shader(gl, "data/shader/md5.vert", "data/shader/md5.frag");
//        } catch (CompilerException ex) {
//            System.out.println("Compiler Exception werbv45342g");
//        } catch (LinkerException ex) {
//            System.out.println("Linker Exception fhgz323fdsf");
//        }
//        GLU glu = new GLU();
        Camera camera = Camera.getInstance(//gl,
			//glu
			);
    }

	/**
	 * @return the md5Version
	 */
	public int getMd5Version() {
		return md5Version;
	}

	/**
	 * @param md5Version
	 *            the md5Version to set
	 */
	public void setMd5Version(int md5Version) {
		this.md5Version = md5Version;

//		System.out.println("MD5Model: md5Version setted!\n");
	}

	/**
	 * @return the commandLine
	 */
	public String getCommandLine() {
		return commandLine;
	}

	/**
	 * @param commandLine
	 *            the commandLine to set
	 */
	public void setCommandLine(String commandLine) {
		this.commandLine = commandLine;

//		System.out.println("MD5Model: commandLine setted!\n");
	}

	/**
	 * @return the numJoints
	 */
	public int getNumJoints() {
		return numJoints;
	}

	/**
	 * Sets the number of joints and the lenght of the array containing joints.
	 * 
	 * @param numJoints
	 *            the numJoints to set
	 */
	public void setNumJoints(int numJoints) {
		this.numJoints = numJoints;
		this.joints = new MD5Joint[numJoints];

//		System.out
//				.println("MD5Model: numJoints setted and joints array initialized with lenght "
//						+ numJoints + " !\n");
	}

	/**
	 * @return the numMeshes
	 */
	public int getNumMeshes() {
		return numMeshes;
	}

	/**
	 * @param numMeshes
	 *            the numMeshes to set
	 */
	public void setNumMeshes(int numMeshes) {
		this.numMeshes = numMeshes;

		// Also initialize the array.
		//this.meshes = new MD5TriMesh[numMeshes];
		this.meshes = new MD5Mesh[numMeshes];

//		System.out.println("MD5Model: numMeshes setted!\n");
	}

	/**
	 * @return the joints
	 */
	public MD5Joint[] getJoints() {
		return joints;
	}

	/**
	 * @param joints
	 *            the joints to set
	 */
	public void setJoints(MD5Joint[] joints) {
		this.joints = joints;
	}

	/**
	 * @return the meshes
	 */
	//public MD5TriMesh[] getMeshes() {
	public MD5Mesh[] getMeshes() {
		return meshes;
	}

	/**
	 * @param meshes
	 *            the meshes to set
	 */
	//public void setMeshes(MD5TriMesh[] meshes) {
	public void setMeshes(MD5Mesh[] meshes) {
		this.meshes = meshes;
	}
	
//	/**
//	 * @return the vertexIndices
//	 */
//	public int[] getVertexIndices() {
//		return vertexIndices;
//	}
//
//	/**
//	 * @param vertexIndices the vertexIndices to set
//	 */
//	public void setVertexIndices(int[] vertexIndices) {
//		this.vertexIndices = vertexIndices;
//	}
	
	//-----------------------------------------------------------------------------------------------------//
	// The following methods are resposible to calculate Vertices positions using Weights and Joints data. //
	//-----------------------------------------------------------------------------------------------------//
	
	// I decided to place this method in MD5Model to avoid adding a reference to MD5Joint[] in MD5Mesh
	// This is the magic function
	//public void constructMesh(MD5TriMesh mesh) {
	public void constructMesh(MD5Mesh mesh) {
		System.out.println("[Verbose] entering methode constructMesh in" + this.getClass().getName());
		// Init vertex indices array
		mesh.setVertexIndices(new int[mesh.getNumTris() * 3]);
		
		// Populate the vertex indices array with a list of all triangles indices
		for (int i = 0, k = 0; i < mesh.getNumTris(); i++) {
			for (int j = 0; j < 3; j++, k++) {
				mesh.getVertexIndices()[k] = mesh.getTriangles()[i].getIndex()[j];
//				System.out.println(mesh.getTriangles()[i].getIndex()[0]);
				mesh.getVertexIndices()[k] = mesh.getTriangles()[i].getIndex()[j];
				mesh.getVertexIndices()[k] = mesh.getTriangles()[i].getIndex()[j];
			}
		}
		
		// Computes vertices positions.
		for (int i = 0; i < mesh.getNumVerts(); i++) {
			Vector3f result = new Vector3f();
			
			// cycle through weights of a vertex and calculate vertex position 
			for (int j = 0; j < mesh.getVertices()[i].getWeightsCount(); j++) {
				MD5Weight weight = mesh.getWeights()[mesh.getVertices()[i].getWeightsStart() + j]; // current weight
				MD5Joint joint = joints[weight.getJoint()];
				
				// Calculate transformed vertex for current weight
				Vector3f wv = new Vector3f();
				MD5Model.rotatePoint(joint.getOrient(), weight.getPos(), wv);
				
				// Sum of all weights bias should be 1
				result.x += (joint.getPos().x + wv.x) * weight.getBias();
				result.y += (joint.getPos().y + wv.y) * weight.getBias();
				result.z += (joint.getPos().z + wv.z) * weight.getBias();
			}
			
			mesh.getVertices()[i].setPosition(result);
		}
	}
	
	// Convenience method
	public void constructMesh(int index) {
		constructMesh(this.meshes[index]);
	}
	
	
	//-----------------------------------------------------------------------------------------------------//
	// Utilities to compute quaternion operations.
	//-----------------------------------------------------------------------------------------------------//
	
	public static void quatNormalize(Quaternion q) {
		// Compute magnitude.
		float mag = (float) Math.sqrt((q.x * q.x) + (q.y * q.y) + (q.z * q.z) + (q.w * q.w));
		
		// Protect against divide by zero.
//		if (mag > 0.0f) {
		if (mag != 0.0f) {
			// Normalize it.
			float oneOverMag = 1.0f / mag;
			
			q.x *= oneOverMag;
			q.y *= oneOverMag;
			q.z *= oneOverMag;
			q.w *= oneOverMag;
		}
	}
	
	// This are methods not implemented by jME.
	// Though it could exist another way to obtain the same result.
	public static void quatMultVec(Quaternion q, Vector3f v, Quaternion out) {
		out.w = - (q.x * v.x) - (q.y * v.y) - (q.z * v.z);
		out.x =   (q.w * v.x) + (q.y * v.z) - (q.z * v.y);
		out.y =   (q.w * v.y) + (q.z * v.x) - (q.x * v.z);
		out.z =   (q.w * v.z) + (q.x * v.y) - (q.y * v.x);
	}
	
	public static void quatMultQuat(Quaternion qa, Quaternion qb, Quaternion out) {
		out.w = (qa.w * qb.w) - (qa.x * qb.x) - (qa.y * qb.y) - (qa.z * qb.z);
		out.x = (qa.x * qb.w) + (qa.w * qb.x) + (qa.y * qb.z) - (qa.z * qb.y);
		out.y = (qa.y * qb.w) + (qa.w * qb.y) + (qa.z * qb.x) - (qa.x * qb.z);
		out.z = (qa.z * qb.w) + (qa.w * qb.z) + (qa.x * qb.y) - (qa.y * qb.x);
	}
	
	// TODO: this should be declared static and moved in some util class.
	public static void rotatePoint(Quaternion q, Vector3f v, Vector3f out) {
		// Calculate inverse of q.
		Quaternion inv = new Quaternion();
		inv.x = -q.x;
		inv.y = -q.y;
		inv.z = -q.z;
		inv.w = q.w;
		
		MD5Model.quatNormalize(inv);
		
		Quaternion tmp = new Quaternion();
		MD5Model.quatMultVec(q, v, tmp);
		
		Quaternion quatResult = new Quaternion();
		MD5Model.quatMultQuat(tmp, inv, quatResult);
		
		out.x = quatResult.x;
		out.y = quatResult.y;
		out.z = quatResult.z;
	}
	
	// TODO: this should be declared static and moved in some util class.
    public static Vector3f rotatePoint(Quaternion q, Vector3f v) {
        Quaternion inv = q.inverse();
        inv.normalize();
        tmp.w = q.w;
        tmp.x = q.x;
        tmp.y = q.y;
        tmp.z = q.z;

        tmp.multLocal(v.x, v.y, v.z, 0.0f);
        //Quaternion quatResult = MD5Model.quatMultQuat(tmp, inv);
        //Quaternion quatResult = new Quaternion(tmp);
        //quatResult.multLocal(inv.x, inv.y, inv.z, inv.w);
        tmp.multLocal(inv.x, inv.y, inv.z, inv.w);
        result.x = tmp.x;
        result.y = tmp.y;
        result.z = tmp.z;
        //result = q.toRotationMatrix().mult(v); // try this because I got some strange results :-)
        return result;
    }
    
    public void animate() {
        this.animation.animate(this);
    }
	
	 /**
     * This is nearly the same as constructMesh(MD5Mesh mesh). I didn't saw the 
     * old method earlyer, so I reimplemented it. These two methods could mostly
     * be merged in a later release.
     * @param gl
     * @param mesh
     * @param meshNumber
     */
    public void drawMeshNew(//GL2 gl,
		MD5Mesh mesh, int meshNumber) {
        if (vertices == null) {
            vertices = new Vector3f[mesh.getNumVerts()];
            for (int i = 0; i < mesh.getNumVerts(); i++) {
                vertices[i] = new Vector3f();
            }
        }

        if (this.uv == null) {
            this.uv = new Vector2f[this.getNumMeshes()][];
        }

        if (this.uv[meshNumber] == null) {
            this.uv[meshNumber] = new Vector2f[mesh.getNumVerts()];
            for (int i = 0; i < mesh.getNumVerts(); i++) {
                this.uv[meshNumber][i] = new Vector2f(mesh.getVertices()[i].getUvCoords().x,
                        1 - mesh.getVertices()[i].getUvCoords().y);
            }
        }

        for (int i = 0; i < mesh.getNumVerts(); i++) {
            // Calculate final vertex to draw with weights
            for (int j = 0; j < mesh.getVertices()[i].getWeightsCount(); j++) {
                MD5Weight weight = mesh.getWeights()[mesh.getVertices()[i].getWeightsStart() + j];

                //const struct md5_weight_t *weight = &mesh->weights[mesh->vertices[i].start + j];
                MD5Joint joint = this.getJoints()[weight.getJoint()];
                //const struct md5_joint_t     *joint =   & joints[weight ->  joint];

                // Calculate transformed vertex for this weight
                Vector3f wv;
                wv = MD5Model.rotatePoint(joint.getOrient(), weight.getPos());
                //Quat_rotatePoint(joint ->  orient, weight ->  pos, wv);

                // the sum of all weight->bias should be 1.0
                finalVertex.x += (joint.getPos().x + wv.x) * weight.getBias();
                finalVertex.y += (joint.getPos().y + wv.y) * weight.getBias();
                finalVertex.z += (joint.getPos().z + wv.z) * weight.getBias();
            }
            
            vertices[i].x = finalVertex.x;
            vertices[i].y = finalVertex.y;
            vertices[i].z = finalVertex.z;
            mesh.getVertices()[i].setPosition(vertices[i]);
            finalVertex.x = 0.0f;
            finalVertex.y = 0.0f;
            finalVertex.z = 0.0f;
        }

//        this.shader.activate();
//        this.shader.setUniformVar3f("CAMERA_POSITION", Camera.getPosition().x, Camera.getPosition().y, Camera.getPosition().z);

        if (mesh.getDecalTexture() != null) {
//            this.shader.setUniformVar1i("base", 0);
//            gl.glActiveTexture(GL2.GL_TEXTURE0);
            textureLoader.select(mesh.getDecalTexture());
        }
        if (mesh.getSpecularTexture() != null) {
//            this.shader.setUniformVar1i("glossMap", 1);
//            gl.glActiveTexture(GL2.GL_TEXTURE1);
            textureLoader.select(mesh.getSpecularTexture());
        }
        if (mesh.getLocalTexture() != null) {
//            this.shader.setUniformVar1i("normalMap", 2);
//            gl.glActiveTexture(GL2.GL_TEXTURE2);
            textureLoader.select(mesh.getLocalTexture());
        }
        if (mesh.getHeightTexture() != null) {
//            this.shader.setUniformVar1i("heightMap", 3);
//            gl.glActiveTexture(GL2.GL_TEXTURE3);
            textureLoader.select(mesh.getHeightTexture());
        }

        for (int i = 0; i < mesh.getNumTris(); i++) {
            int v0 = mesh.getTriangles()[i].getIndex()[0];
            int v1 = mesh.getTriangles()[i].getIndex()[1];
            int v2 = mesh.getTriangles()[i].getIndex()[2];

            Triangle tri = new Triangle(new org.util.Vector3f(vertices[v0].x, vertices[v0].y, vertices[v0].z),
                    new org.util.Vector3f(vertices[v1].x, vertices[v1].y, vertices[v1].z),
                    new org.util.Vector3f(vertices[v2].x, vertices[v2].y, vertices[v2].z),
                    this.uv[meshNumber][v0].x, this.uv[meshNumber][v0].y,
                    this.uv[meshNumber][v1].x, this.uv[meshNumber][v1].y,
                    this.uv[meshNumber][v2].x, this.uv[meshNumber][v2].y);

            tri.calcTBN();
            tri.calcNormal();
            normale = tri.normal;
            binormale = tri.bitangent;
            tangent = tri.tangent;

//            int tangentLoc = gl.glGetAttribLocation(this.shader.progid, "tangent");
//            int binormalLoc = gl.glGetAttribLocation(this.shader.progid, "binormal");
//            int vertexViewVecLoc = gl.glGetAttribLocation(this.shader.progid, "vertexViewVec");
//            gl.glDisable(GL2.GL_CULL_FACE);

//            gl.glBegin(GL2.GL_TRIANGLES);
//            //gl.glVertexAttrib3f(vertexViewVecLoc, Camera.getPosition().x - vertices[v2].x,
//            //        Camera.getPosition().y - vertices[v2].y, Camera.getPosition().z - vertices[v2].z);
//            gl.glVertexAttrib3f(binormalLoc, binormale.x, binormale.y, binormale.z);
//            gl.glVertexAttrib3f(tangentLoc, tangent.x, tangent.y, tangent.z);
//            gl.glNormal3f(normale.x, normale.y, normale.z);
//            gl.glMultiTexCoord2f(GL2.GL_TEXTURE0, this.uv[meshNumber][v2].x, this.uv[meshNumber][v2].y);
//            gl.glVertex3f(vertices[v2].x, vertices[v2].y, vertices[v2].z);
//
//            //gl.glVertexAttrib3f(vertexViewVecLoc, Camera.getPosition().x - vertices[v1].x,
//            //        Camera.getPosition().y - vertices[v1].y, Camera.getPosition().z - vertices[v1].z);
//            gl.glVertexAttrib3f(binormalLoc, binormale.x, binormale.y, binormale.z);
//            gl.glVertexAttrib3f(tangentLoc, tangent.x, tangent.y, tangent.z);
//            gl.glMultiTexCoord2f(GL2.GL_TEXTURE0, this.uv[meshNumber][v1].x, this.uv[meshNumber][v1].y);
//            gl.glVertex3f(vertices[v1].x, vertices[v1].y, vertices[v1].z);
//
//            //gl.glVertexAttrib3f(vertexViewVecLoc, Camera.getPosition().x - vertices[v0].x,
//            //        Camera.getPosition().y - vertices[v0].y, Camera.getPosition().z - vertices[v0].z);
//            gl.glVertexAttrib3f(binormalLoc, binormale.x, binormale.y, binormale.z);
//            gl.glVertexAttrib3f(tangentLoc, tangent.x, tangent.y, tangent.z);
//            gl.glMultiTexCoord2f(GL2.GL_TEXTURE0, this.uv[meshNumber][v0].x, this.uv[meshNumber][v0].y);
//            gl.glVertex3f(vertices[v0].x, vertices[v0].y, vertices[v0].z);
//            gl.glEnd();
        }

//        this.shader.deactivate();
//        gl.glActiveTexture(GL2.GL_TEXTURE0);
    }
	
	
	
	//-----------------------------------------------------------------------------------------------------//
	// Getter and setter for animation.
	//-----------------------------------------------------------------------------------------------------//
	
	/**
	 * @return the animation
	 */
	public MD5Animation getAnimation() {
		return animation;
	}

	/**
	 * @param animation the animation to set
	 */
	public void setAnimation(MD5Animation animation) {
		this.animation = animation;
	}
}
