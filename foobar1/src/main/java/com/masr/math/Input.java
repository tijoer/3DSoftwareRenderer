package com.masr.math;

///*
// * This is intellectual property. You are not allowed 
// * to use it in any way, except you have a written 
// * allowance by the owner.
// */
//package org.util;
//
//import javax.media.opengl.GL2;
//import javax.media.opengl.glu.GLU;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.awt.event.MouseMotionListener;
//import org.util.Camera;
//
//public final class Input implements KeyListener, MouseListener, MouseMotionListener, Runnable {
//
//    private GL2 gl;
//    private GLU glu;
//    public int mouseClickX;
//    public int mouseClickY;
//    private boolean keyForward;
//    private boolean keyBackward;
//    private boolean keySlideLeft;
//    private boolean keySlideRight;
//    private boolean keyUp;
//    private boolean keyDown;
//    private boolean dragging;
//    private Thread camAnimator;
//    public float mouseRotX;
//    public float mouseRotY;
//    private Camera camera;
//
//    //public Input(Camera camera) {
//    public Input(GL2 gl, GLU glu) {
//        this.gl = gl;
//        this.glu = glu;
//        this.keyForward = false;
//        this.keyBackward = false;
//        this.keySlideLeft = false;
//        this.keySlideRight = false;
//        this.keyUp = false;
//        this.keyDown = false;
//        this.dragging = false;
//        this.mouseClickX = 0;
//        this.mouseClickY = 0;
//        this.camAnimator = new Thread(this);
//        this.camAnimator.start();
//        this.mouseRotX = 0;
//        this.mouseRotY = 30;
//    }
//
//    public void run() {
//        this.camera = Camera.getInstance(gl, glu);
//
//        while (true) {
//            if (this.keyForward) {
//                this.camera.forward();
//            }
//            if (this.keyBackward) {
//                this.camera.backward();
//            }
//            if (this.keySlideLeft) {
//                this.camera.slideLeft();
//            }
//            if (this.keySlideRight) {
//                this.camera.slideRight();
//            }
//            if (this.keyUp) {
//                this.camera.up();
//            }
//            if (this.keyDown) {
//                this.camera.down();
//            }
//
//            try {
//                Thread.sleep(15);
//            } catch (InterruptedException e) {
//            }
//        }
//    }
//
//    public void keyPressed(KeyEvent e) {
//        int keyCode = e.getKeyCode();
//
//        if (keyCode == KeyEvent.VK_ESCAPE) {
//            System.exit(0);
//        }
//        if (keyCode == KeyEvent.VK_W) {
//            this.keyForward = true;
//        }
//        if (keyCode == KeyEvent.VK_S) {
//            this.keyBackward = true;
//        }
//        if (keyCode == KeyEvent.VK_A) {
//            this.keySlideLeft = true;
//        }
//        if (keyCode == KeyEvent.VK_D) {
//            this.keySlideRight = true;
//        }
//        if (keyCode == KeyEvent.VK_SPACE) {
//            this.keyUp = true;
//        }
//        if (keyCode == KeyEvent.VK_CONTROL) {
//            this.keyDown = true;
//        }
//        if (keyCode == KeyEvent.VK_R) {
//            //
//        }
//        
//        //presentation
//        if (keyCode == KeyEvent.VK_1) {
//            Camera.smoothMoveTo(new Vector3f(0.0f, 90.0f, -1050.0f),
//                00.0f, 180.0f);
//        }
//        if (keyCode == KeyEvent.VK_2) {
//            Camera.smoothMoveTo(new Vector3f(1500.0f, 1400.0f, 2350.0f),
//                20.0f, -33.0f);
//        }
//        if (keyCode == KeyEvent.VK_3) {
//            Camera.smoothMoveTo(new Vector3f(0.0f, 25.0f, 1287.0f),
//                4.0f, -1.5f);
//        }
//    }
//
//    public void keyReleased(KeyEvent e) {
//        int keyCode = e.getKeyCode();
//
//        if (keyCode == KeyEvent.VK_W) {
//            this.keyForward = false;
//        }
//        if (keyCode == KeyEvent.VK_S) {
//            this.keyBackward = false;
//        }
//        if (keyCode == KeyEvent.VK_A) {
//            this.keySlideLeft = false;
//        }
//        if (keyCode == KeyEvent.VK_D) {
//            this.keySlideRight = false;
//        }
//        if (keyCode == KeyEvent.VK_SPACE) {
//            this.keyUp = false;
//        }
//        if (keyCode == KeyEvent.VK_CONTROL) {
//            this.keyDown = false;
//        }
//    }
//
//    public void keyTyped(KeyEvent arg0) {
//    }
//
//    public void mouseReleased(MouseEvent e) {
//        int x = e.getX();
//        int y = e.getY();
//        this.mouseClickX = x;
//        this.mouseClickY = y;
//
//        this.dragging = false;
//    }
//
//    public void mousePressed(MouseEvent mouseEvent) {
//        int x = mouseEvent.getX();
//        int y = mouseEvent.getY();
////        if (this.gui.checkForMouseClicks(x, y)) {
////            return;
////        }
//        this.mouseClickX = x;
//        this.mouseClickY = y;
//    }
//
//    public void mouseClicked(MouseEvent arg0) {
//    }
//
//    public void mouseEntered(MouseEvent arg0) {
//    }
//
//    public void mouseExited(MouseEvent arg0) {
//    }
//
//    public void mouseDragged(MouseEvent e) {
//        int x = e.getX();
//        int y = e.getY();
////        if (this.gui.checkForMouseClicks(x, y)) {
////            return;
////        }
//        int dx = Math.abs(x - this.mouseClickX);
//        int dy = Math.abs(y - this.mouseClickY);
//
//        if (dragging == false) {
//        }
//
//        //set to true, so that the camera movement doesn't trigger window events
//        this.dragging = true;
//        // Calculate mouse movements
//        if (x < this.mouseClickX) {
//            this.camera.turnLeft(dx);
//        } else if (x > this.mouseClickX) {
//            this.camera.turnRight(dx);
//        }
//        if (y < this.mouseClickY) {
//            this.camera.turnUp(dy);
//        } else if (y > this.mouseClickY) {
//            this.camera.turnDown(dy);
//        }
//        this.mouseClickX = x;
//
//        this.mouseClickY = y;
//    }
//
//    public void mouseMoved(MouseEvent e) {
//    }
//}
