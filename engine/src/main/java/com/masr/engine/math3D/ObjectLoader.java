package com.masr.engine.math3D;

import com.masr.engine.graphics3D.texture.ShadedSurface;
import com.masr.engine.graphics3D.texture.ShadedTexture;
import com.masr.engine.graphics3D.texture.Texture;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The ObjectLoader class loads a subset of the Alias|Wavefront OBJ file
 * specification.
 *
 * Lines that begin with '#' are comments.
 *
 * OBJ file keywords:
 * <pre>
 * mtllib [filename]    - Load materials from an external .mtl
 * file.
 * v [x] [y] [z]        - Define a vertex with floating-point
 * coords (x,y,z).
 * f [v1] [v2] [v3] ... - Define a new face. a face is a flat,
 * convex polygon with vertices in
 * counter-clockwise order. Positive
 * numbers indicate the index of the
 * vertex that is defined in the file.
 * Negative numbers indicate the vertex
 * defined relative to last vertex read.
 * For example, 1 indicates the first
 * vertex in the file, -1 means the last
 * vertex read, and -2 is the vertex
 * before that.
 * g [name]             - Define a new group by name. The faces
 * following are added to this group.
 * usemtl [name]        - Use the named material (loaded from a
 * .mtl file) for the faces in this group.
 * </pre>
 *
 * MTL file keywords:
 * <pre>
 * newmtl [name]        - Define a new material by name.
 * map_Kd [filename]    - Give the material a texture map.
 * </pre>
 */
public class ObjectLoader {

	/**
	 * The Material class wraps a ShadedTexture.
	 */
	public static class Material {

		public String sourceFile;
		public ShadedTexture texture;
	}

	/**
	 * A LineParser is an interface to parse a line in a text file. Separate
	 * LineParsers and are used for OBJ and MTL files.
	 */
	protected interface LineParser {

		public void parseLine(String line) throws IOException,
			NumberFormatException, NoSuchElementException;
	}
	protected String filePath;
	protected List vertices;
	protected Material currentMaterial;
	protected HashMap materials;
	protected List lights;
	protected float ambientLightIntensity;
	protected HashMap parsers;
	private PolygonGroup object;
	private PolygonGroup currentGroup;

	/**
	 * Creates a new ObjectLoader.
	 */
	public ObjectLoader() {
		materials = new HashMap();
		vertices = new ArrayList();
		parsers = new HashMap();
		parsers.put("obj", new ObjLineParser());
		parsers.put("mtl", new MtlLineParser());
		currentMaterial = null;
		setLights(new ArrayList(), 1);
	}

	/**
	 * Sets the lights used for the polygons in the parsed objects. After
	 * calling this method calls to loadObject use these lights.
	 */
	public final void setLights(List lights,
		float ambientLightIntensity) {
		this.lights = lights;
		this.ambientLightIntensity = ambientLightIntensity;
	}

	/**
	 * Loads an OBJ file as a PolygonGroup.
	 */
	public PolygonGroup loadObject(String filename)
		throws IOException {
		URL url = ObjectLoader.class.getResource(filename);

		object = new PolygonGroup();
		object.setFilename(filename);

		StringTokenizer st = new StringTokenizer(filename, "/");
		String lastPath = "/";
		while (st.hasMoreTokens()) {
			lastPath = lastPath + st.nextToken() + "/";
			if (st.hasMoreTokens()) {
				this.filePath = lastPath;
			}
		}

		vertices.clear();
		currentGroup = object;
		parseFile(filename);

		return object;
	}

	/**
	 * Gets a Vector3D from the list of vectors in the file. Negative indeces
	 * count from the end of the list, postive indeces count from the beginning.
	 * 1 is the first index, -1 is the last. 0 is invalid and throws an
	 * exception.
	 */
	protected Vector3D getVector(String indexStr) {
		int index = Integer.parseInt(indexStr);
		if (index < 0) {
			index = vertices.size() + index + 1;
		}
		return (Vector3D) vertices.get(index - 1);
	}

	/**
	 * Parses an OBJ (ends with ".obj") or MTL file (ends with ".mtl").
	 */
	protected void parseFile(String filename)
		throws IOException {
		BufferedReader reader;
		InputStream is = ObjectLoader.class.getResourceAsStream(filename);
		if (is == null) {
			is = ObjectLoader.class.getResourceAsStream(filePath + filename);
		}
		reader = new BufferedReader(new InputStreamReader(is));

		// get the parser based on the file extention
		LineParser parser = null;
		int extIndex = filename.lastIndexOf('.');
		if (extIndex != -1) {
			String ext = filename.substring(extIndex + 1);
			parser = (LineParser) parsers.get(ext.toLowerCase());
		}
		if (parser == null) {
			parser = (LineParser) parsers.get("obj");
		}

		// parse every line in the file
		while (true) {
			String line = reader.readLine();
			// no more lines to read
			if (line == null) {
				reader.close();
				return;
			}

			line = line.trim();

			// ignore blank lines and comments
			if (line.length() > 0 && !line.startsWith("#")) {
				// interpret the line
				try {
					parser.parseLine(line);
				} catch (NumberFormatException | NoSuchElementException ex) {
					throw new IOException(ex.getMessage());
				}
			}

		}
	}

	/**
	 * Parses a line in an OBJ file.
	 */
	protected class ObjLineParser implements LineParser {

		@Override
		public void parseLine(String line) throws IOException,
			NumberFormatException, NoSuchElementException {
			StringTokenizer tokenizer = new StringTokenizer(line);
			String command = tokenizer.nextToken();
			switch (command) {
				case "v":
					// create a new vertex
					vertices.add(new Vector3D(
						Float.parseFloat(tokenizer.nextToken()),
						Float.parseFloat(tokenizer.nextToken()),
						Float.parseFloat(tokenizer.nextToken())));
					break;
				case "f":
					// create a new face (flat, convex polygon)
					List currVertices = new ArrayList();
					while (tokenizer.hasMoreTokens()) {
						String indexStr = tokenizer.nextToken();

						// ignore texture and normal coords
						int endIndex = indexStr.indexOf('/');
						if (endIndex != -1) {
							indexStr = indexStr.substring(0, endIndex);
						}

						currVertices.add(getVector(indexStr));
					}
					// create textured polygon
					Vector3D[] array =
						new Vector3D[currVertices.size()];
					currVertices.toArray(array);
					TexturedPolygon3D poly =
						new TexturedPolygon3D(array);
					// set the texture
					ShadedSurface.createShadedSurface(
						poly, currentMaterial.texture,
						lights, ambientLightIntensity);
					// add the polygon to the current group
					currentGroup.addPolygon(poly);
					break;
				case "g":
					// define the current group
					if (tokenizer.hasMoreTokens()) {
						String name = tokenizer.nextToken();
						currentGroup = new PolygonGroup(name);
					} else {
						currentGroup = new PolygonGroup();
					}
					object.addPolygonGroup(currentGroup);
					break;
				case "mtllib": {
					// load materials from file
					String name = tokenizer.nextToken();
					parseFile(name);
					break;
				}
				case "usemtl": {
					// define the current material
					String name = tokenizer.nextToken();
					currentMaterial = (Material) materials.get(name);
					if (currentMaterial == null) {
						System.out.println("no material: " + name);
					}
					break;
				}
				default:
					break;
			}

		}
	}

	/**
	 * Parses a line in a material MTL file.
	 */
	protected class MtlLineParser implements LineParser {

		@Override
		public void parseLine(String line)
			throws NoSuchElementException {
			StringTokenizer tokenizer = new StringTokenizer(line);
			String command = tokenizer.nextToken();
			switch (command) {
				case "newmtl": {
					// create a new material if needed
					String name = tokenizer.nextToken();
					currentMaterial = (Material) materials.get(name);
					if (currentMaterial == null) {
						currentMaterial = new Material();
						materials.put(name, currentMaterial);
					}
					break;
				}
				case "map_Kd": {
					// give the current material a texture
					String name = tokenizer.nextToken();
					//                File file = new File(name);
					if (!name.equals(currentMaterial.sourceFile)) {
						currentMaterial.sourceFile = name;
						try {
							currentMaterial.texture = (ShadedTexture) Texture.createTexture(filePath + name, true);
						} catch (IOException ex) {
							Logger.getLogger(ObjectLoader.class.getName()).log(Level.SEVERE, null, ex);
						}
					}
					break;
				}
				default:
					break;
			}
		}
	}
}
