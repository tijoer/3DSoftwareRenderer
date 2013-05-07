///*
// * This is intellectual property. You are not allowed 
// * to use it in any way, except you have a written 
// * allowance by the owner.
// */
//package org.md5reader2.exampleImplementation;
//
//import javax.media.opengl.GL2;
//import javax.media.opengl.glu.GLU;
//
//import org.md5reader2.md5.MD5Model;
//import org.util.Camera;
//import org.util.Textures;
//import org.util.Vector3f;
//
///**
// *
// * @author Tim Joergen
// */
//public class Scene0 {
//
//    GL2 gl;
//    GLU glu;
//    Camera camera;
//    Textures textures;
//    MD5Model model;
//    float foo = 0.0f;
//    
//    //The following models are working w.o. problems :) 
//    //private static final String MESH = "data/doom3/models/md5/heads/sarge/sarge.md5mesh";
//    //private static final String ANIMATION = "data/doom3/models/md5/heads/sarge/sargeidle.md5anim";
//    //private static final String MESH = "data/doom3/models/md5/monsters/imp/imp.md5mesh";
//    //private static final String ANIMATION = "data/doom3/models/md5/monsters/imp/slash1.md5anim";
//    //private static final String MESH = "data/doom3/models/md5/monsters/zombies/zsec_machinegun/zsecmachinegun.md5mesh";
//    //private static final String ANIMATION = "data/doom3/models/md5/monsters/zombies/zsec_machinegun/machinegun_run.md5anim";
//    //private static final String MESH = "data/doom3/models/md5/monsters/zombies/morgue/morgue.md5mesh";
//    //private static final String ANIMATION = "data/doom3/models/md5/monsters/zombies/morgue/idle.md5anim";
//    //private static final String MESH = "data/doom3/models/md5/monsters/zombies/bernie/bernie.md5mesh";
//    //private static final String ANIMATION = "data/doom3/models/md5/monsters/zombies/bernie/stand.md5anim";
//    //private static final String MESH = "data/doom3/models/md5/monsters/zfat/zfat.md5mesh";
//    //private static final String ANIMATION = "data/doom3/models/md5/monsters/zfat/idle1.md5anim";
//    //private static final String MESH = "data/doom3/models/md5/monsters/hellknight/hellknight.md5mesh";
//    //private static final String ANIMATION = "data/doom3/models/md5/monsters/hellknight/idle2.md5anim";
//    
//    private static final String MESH = "data/models/marine.md5mesh";
//    private static final String ANIMATION = "data/models/marscity_marine1_ver1_hq_primary.md5anim";
//    int frame = 0;
//
//    Scene0(GL2 gl, GLU glu) {
//        this.gl = gl;
//        this.glu = glu;
//        this.textures = Textures.getInstance();
//        this.camera = Camera.getInstance(gl, glu);
//        this.camera.setPosition(new Vector3f(200.0f, 100.0f, 0.0f));
//        Camera.xyAngle = 10.0f;
//        Camera.xzAngle = -80.0f;
//        Camera.speed = 1.0f;
//
//        System.out.println("dir: " + System.getProperty("user.dir"));
//        model = new MD5Model(gl, MESH, ANIMATION);
//    }   
//
//    void draw() {
//        // camera
//        camera.rotateAccordingToCameraPosition();
//        camera.translateAccordingToCameraPosition();
//
//        gl.glDisable(GL2.GL_LIGHTING);
//        gl.glBegin(GL2.GL_LINES);
//        for (int x = -10; x <= 10; x++) {
//            gl.glColor3f(1.0f, 0.0f, 0.0f);
//            gl.glVertex3f(x * 10.0f, 0.0f, -100.0f);
//            gl.glVertex3f(x * 10.0f, 0.0f, 100.0f);
//        }
//        for (int x = -10; x <= 10; x++) {
//            gl.glColor3f(0.0f, 1.0f, 0.0f);
//            gl.glVertex3f(-100.0f, 0.0f, x * 10.0f);
//            gl.glVertex3f(100.0f, 0.0f, x * 10);
//        }
//        gl.glEnd();
//
//        float[] light0_ambient = {0.2f, 0.2f, 0.2f, 1.0f};
//        float[] light0_diffuse = {0.7f, 0.7f, 0.7f, 1.0f};
//        float[] light0_specular = {0.8f, 0.8f, 0.8f, 1.0f};
//        float[] light0_position = {23.0f,//(-5.0f + (float)(10.0f - Math.sin(foo*0.09)*25)),
//            ((float) (40.0f + Math.sin(foo * 0.08) * 30 - 0)),
//            ((float) (Math.sin(foo * 0.05) * 30) + 0),
//            1.0f
//        };
//        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light0_position, 0);
//        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, light0_ambient, 0);
//        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light0_diffuse, 0);
//        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, light0_specular, 0);
//        gl.glPushMatrix();
//        gl.glTranslatef(light0_position[0], light0_position[1], light0_position[2]);
//        //glut.glutSolidSphere(1.0f, 3, 3);
//        gl.glPopMatrix();
//
//        foo += 0.5f;
//
//        //gl.glTranslatef(foo, 0.0f, 0.0f);
//        gl.glRotatef(270.0f, 1.0f, 0.0f, 0.0f);
//        
//        model.drawMeshNew(gl, this.model.getMeshes()[0], 0);
//        model.animate();
//    }
//}
