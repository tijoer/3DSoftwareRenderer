/**
 * 
 */
package org.md5reader2.md5;

import java.io.File;

import org.util.Textures;

/**
 * @author Marco Frisan, Tim Joergen
 * 
 */
public class MD5Mesh {
	private String shader;
	private int numVerts;
	private int numTris;
	private int numWeights;

	private MD5Vertex[] vertices;
	private MD5Triangle[] triangles;
	private MD5Weight[] weights;
	
	private int[] vertexIndices;

    private Textures textureLoader;
    private String decalTexture;
    private String localTexture;
    private String heightTexture;
    private String specularTexture;

	/**
	 * 
	 */
	public MD5Mesh() {
		//super();
		this.textureLoader = Textures.getInstance();
	}

	/**
	 * @return the shader
	 */
	public String getShader() {
		return shader;
	}

    /**
     * @param shader
     *            the shader to set, this will also load the texures into GPU memory
     */
    public void setShader(String shader) {
        this.shader = shader;
        initTextures();
    }

	/**
	 * @return the numVerts
	 */
	public int getNumVerts() {
		return numVerts;
	}

	/**
	 * @param numVerts
	 *            the numVerts to set
	 */
	public void setNumVerts(int numVerts) {
		this.numVerts = numVerts;
	}

	/**
	 * @return the numTris
	 */
	public int getNumTris() {
		return numTris;
	}

	/**
	 * @param numTris
	 *            the numTris to set
	 */
	public void setNumTris(int numTris) {
		this.numTris = numTris;
	}

	/**
	 * @return the numWeights
	 */
	public int getNumWeights() {
		return numWeights;
	}

	/**
	 * @param numWeights
	 *            the numWeights to set
	 */
	public void setNumWeights(int numWeights) {
		this.numWeights = numWeights;
	}

	/**
	 * @return the vertices
	 */
	public MD5Vertex[] getVertices() {
		return vertices;
	}

	/**
	 * @param vertices
	 *            the vertices to set
	 */
	public void setVertices(MD5Vertex[] vertices) {
		this.vertices = vertices;
	}

	/**
	 * @return the weights
	 */
	public MD5Weight[] getWeights() {
		return weights;
	}

	/**
	 * @param weights
	 *            the weights to set
	 */
	public void setWeights(MD5Weight[] weights) {
		this.weights = weights;
	}
	
	/**
	 * @return the triangles
	 */
	public MD5Triangle[] getTriangles() {
		return triangles;
	}

	/**
	 * @param triangles the triangles to set
	 */
	public void setTriangles(MD5Triangle[] triangles) {
		this.triangles = triangles;
	}

	/**
	 * @return the vertexIndices
	 */
	public int[] getVertexIndices() {
		return vertexIndices;
	}

	/**
	 * @param vertexIndices the vertexIndices to set
	 */
	public void setVertexIndices(int[] vertexIndices) {
		this.vertexIndices = vertexIndices;
	}

	/**
     * <p>
     * This loads the texures via a heuristic, assuming that the texures are 
     * named <code>this.getShader()+"_d.tga"</code> (for example). It is no problem, 
     * if a texture was not found. The verbose message is normaly no problem, but
     * if no texture was found, something is wrong.</p> 
     * <p>
     * I wanted to keep this methode simple, so i didn't create a second methode
     * to load all the underscore files automatically.
     * </p>
     */
    public void initTextures() {
        //for(int i=0; i<model.getMeshes().length; i++) {
        //System.out.println("Shader: " + model.getMeshes()[i].getShader());
        File f = new File(this.getShader() + ".tga");
        File f_d = new File(this.getShader() + "_d.tga");
        File f_h = new File(this.getShader() + "_h.tga");
        File f_local = new File(this.getShader() + "_local.tga");
        File f_s = new File(this.getShader() + "_s.tga");

        if (f.exists()) {
            if (this.decalTexture == null) {
            	String id = f.getPath();
            	String filename = f.getPath();
                textureLoader.load(id, filename);
                this.decalTexture = f.getPath();
            }
        } else {
            System.out.println("[Verbose] Texture not found: " + f.getPath());
        }
        
        if (f_d.exists()) {
            if (this.decalTexture == null) {
                textureLoader.load(f_d.getPath(), f_d.getPath());
                this.decalTexture = f_d.getPath();
            }
        } else {
            System.out.println("[Verbose] Texture not found: " + f_d.getPath());
        }
        
        if (f_h.exists()) {
            if (this.heightTexture == null) {
                textureLoader.load(f_h.getPath(), f_h.getPath());
                this.heightTexture = f_h.getPath();
            }
        } else {
            System.out.println("[Verbose] Texture not found: " + f_h.getPath());
        }
        
        if (f_local.exists()) {
            if (this.localTexture == null) {
                this.localTexture = f_local.getPath();
                textureLoader.load(f_local.getPath(), f_local.getPath());
            }
        } else {
            System.out.println("[Verbose] Texture not found: " + f_local.getPath());
        }
        
        if (f_s.exists()) {
            if (this.specularTexture == null) {
                this.specularTexture = f_s.getPath();
                textureLoader.load(f_s.getPath(), f_s.getPath());
            }
        } else {
            System.out.println("[Verbose] Texture not found: " + f_s.getPath());
        }

        if (this.decalTexture == null &&
                this.heightTexture == null &&
                this.localTexture == null &&
                this.specularTexture == null) {
            System.out.println("[Warning] No textures were found, for a mesh." );
        }

    }

    public String getDecalTexture() {
        return decalTexture;
    }

    public void setDecalTexture(String decalTexture) {
        this.decalTexture = decalTexture;
    }

    public String getHeightTexture() {
        return heightTexture;
    }

    public void setHeightTexture(String heightTexture) {
        this.heightTexture = heightTexture;
    }

    public String getLocalTexture() {
        return localTexture;
    }

    public void setLocalTexture(String localTexture) {
        this.localTexture = localTexture;
    }

    public String getSpecularTexture() {
        return specularTexture;
    }

    public void setSpecularTexture(String specularTexture) {
        this.specularTexture = specularTexture;
    }
}
