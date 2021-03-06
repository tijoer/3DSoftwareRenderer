package org.md5reader2.parser;

import java.io.*;
import java.net.URL;

//import javax.media.opengl.GL;

import org.md5reader2.md5.MD5Animation;
import org.md5reader2.md5.MD5Model;

import org.util.FastMath;
import org.util.Quaternion;

public abstract class MD5Parser {
	// TODO: maybe it is better to move this variables to the subclasses.
	// NOTE: it seems, instead, useful to leave them here. MD5AnimParser uses
	// "model" to checkAnimation().
	protected MD5Model model;
	protected MD5Animation animation;
//	private GL gl;

	// protected boolean animationIsValid = true;

	public MD5Parser(//GL gl
		) {
//		super();
//		this.gl = gl;
	}

//	// should throw Exception
//	public void parse(InputStream is) throws IOException {
//		parse(is.toURI().toURL());
//	}

	public void parse(InputStream is) throws IOException {
		// Creates the StreamTokenizer to read from file.

		InputStreamReader reader = new InputStreamReader(is);

		if (reader == null) {
			System.out
			.println("MD5 Reader 2, ERROR: "
					+ "org.md5reader2.parser.MD5Parser: "
					+ "for some unknown reason FileReader, reader, has not been initialized.");
			// return null;
			return;
		}

		// Creates StreamTokenizer.
		StreamTokenizer st = new StreamTokenizer(reader);
		// Setup StreamTokenizer lexical rules.
		st.quoteChar('"'); // Text between pairs of " will be considered as
		// a single string token.
		st.ordinaryChar('{');
		st.ordinaryChar('}');
		st.ordinaryChar('(');
		st.ordinaryChar(')');
		st.parseNumbers(); // Evalutates strings representing numbers as
		// double.
		// Ignores Java like single line comments.
		st.slashSlashComments(true);
		// st.slashStarComments(true);
		st.eolIsSignificant(true); // EOLs are read as tokens.

		// Parses the file.
		int token;
		// Runs the loop till it finds the end of file.
		// 
		// For how this parser is built now, this while loop
		// reads the first token of each section, the section
		// key: for example, "MD5Version".
		// parseSections() takes the task to verify which section
		// is the current one and then launch the code to read
		// section content.
		// The section code, returns after setting the last token to
		// the last available token value in the current section.
		// Then also parseSections() returns and, this loop reads
		// the first token (the key) of the next section and re-call.
		// parseSections().
		while ((token = st.nextToken()) != StreamTokenizer.TT_EOF) {
			parseSections(st, token);
		}

		// TODO: this approach gives to user the responsibility to verify if
		// animation is valid
		// before to use it. At the moment there is no way to do it. Maybe
		// is better I move
		// "animationIsValid" variable to MD5Animation so that user can test
		// it.
		// Just for debug.
		// if (!animationIsValid) System.out.println("ERROR: animation is
		// not valid!");

		// Closes the reader.
		reader.close();

		// This method can be implemented by inheriting classes to perform additional computations.
		finalizeParsing(st, token);

	}

    public void parseModel(InputStream is, MD5Model model) {
		
        this.model = model;
        try {
			parse(is);
		} catch (IOException e) {
			System.out.println("Something during the parsing went horribly wrong.");
			e.printStackTrace();
		}
        // TODO: model is never null! Because it is initialized
        // in previous lines. But its fields could be empty if parse()
        // failed to load the file...
        // How to handle it? Is better to let parse() to throw exceptions
        // instead of catching them? And if we make parse() throwing exceptions
        // should we make also the current method throw them and leave
        // users to handle these exceptions in their code?
        // if (model == null) return null;
        //return model;
    }


//	public MD5Animation parseAnimation(URL url) throws IOException {
//		this.animation = new MD5Animation();
//		// Code to verify the Hash code of animation, is for testing the "null" frame bug.
//		System.out.println("Hash code of animParser.animation: " + animation.hashCode());
//		parse(url);
//		// TODO: read parseModel() about throwing exceptions.
//		return animation;
//	}

	public MD5Animation parseAnimation(InputStream is) throws IOException {
		this.animation = new MD5Animation();
		// Code to verify the Hash code of animation, is for testing the "null" frame bug.
		System.out.println("Hash code of animParser.animation: " + animation.hashCode());
		parse(is);
		// TODO: read parseModel() about throwing exceptions.
		return animation;
	}

	public void computeW(Quaternion q) {
		float t = 1.0f - (q.x * q.x) - (q.y * q.y) - (q.z * q.z);

		if (t < 0.0f) {
			q.w = 0.0f;
		} else {
			q.w = -(FastMath.sqrt(t));
		}

	}

	// NOTE: passing token is not necessary.
	protected abstract void parseSections(StreamTokenizer st, int token)
			throws IOException;
	
	/**
	 * This method can be implemented by inheriting classes to perform additional computations.
	 * It is called by this class after the file has completely parsed.
	 * 
	 * @param st
	 * @param token
	 */
	protected abstract void finalizeParsing(StreamTokenizer st, int token);

	// Internal test method only!!!
	// Not to be used in production.
	public MD5Animation getAnimation() {
		return animation;
	}
}
