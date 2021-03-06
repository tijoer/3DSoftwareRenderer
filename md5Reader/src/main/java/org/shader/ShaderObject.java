//package org.shader;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.net.URL;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.media.opengl.GL2;
//
///** 
// * <h3>Wrapper for loading and compiling shader objects.</h3>
// * 
// * @author Christoph Peuser, Tim Joergen
// */
//class ShaderObject {
//    int shaderobject;
//    GL2 gl;
//
//    protected ShaderObject(GL2 gl, int type, String filename) throws CompilerException{
//        System.out.print("Loading "+filename);
//        this.gl = gl;
//        this.shaderobject = gl.glCreateShader(type);
//        try {
//            // load the files contents into a string-array
//            URL url = this.getClass().getResource(filename);
//            String src = "";
//
//            BufferedReader input = new BufferedReader(new FileReader(new File(filename)));
//            //BufferedReader input = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(filename)));
//            String current;
//            while( (current = input.readLine()) != null){
//                src += current;
//                src += '\n';
//            }
//            String[] srcarr = new String[1];
//            srcarr[0] = src.toString();
//            // and pass the source on to opengl
//            gl.glShaderSource(shaderobject, 1, srcarr, null, 0);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(ShaderObject.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex){
//            Logger.getLogger(ShaderObject.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        System.out.print(" finished ");
//        compile();
//        System.out.println("and compiled");
//    }
//
//    /**
//     * This compiles a shader file, which was written in the GLSL language. The
//     * shader id on the GPU is referenced through <code>this.shaderObject</code>
//     * .
//     * @throws org.shader.CompilerException
//     */
//    private void compile() throws CompilerException {
//        // (try to) compile the src
//        gl.glCompileShader(shaderobject);
//
//        // check for errors in compilation
//        int[] comperror = new int[1];
//        gl.glGetShaderiv(shaderobject, GL2.GL_COMPILE_STATUS, comperror, 0);
//        if(comperror[0] != GL2.GL_TRUE){
//            // compilation failed, get the compilation log and throw an exception
//            // first get the lenght of the error log
//            gl.glGetShaderiv(shaderobject, GL2.GL_INFO_LOG_LENGTH, comperror, 0);
//            // then the log itself
//            byte[] infolog = new byte[comperror[0]];
//            gl.glGetShaderInfoLog(shaderobject, comperror[0], null, 0, infolog, 0);
//            // throw an exception containing the error log
//            throw new CompilerException(infolog);
//        }
//    }
//}
