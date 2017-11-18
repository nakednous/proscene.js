package basics;

import processing.core.PApplet;
import remixlab.input.event.KeyEvent;
import remixlab.input.event.MotionEvent;
import remixlab.core.Graph;
import remixlab.core.Node;
import remixlab.proscene.Scene;

public class TwoD extends PApplet {
    Scene scene;
    Node eye, node;
    boolean target = true;

    public void info() {
        println(scene.radius());
        scene.center().print();
        scene.eye().position().print();
        println(scene.zNear());
        println(scene.zFar());
        scene.matrixHandler().projection().print();
        scene.matrixHandler().view().print();
        scene.matrixHandler().modelView().print();
    }

    @Override
    public void settings() {
        size(800, 800, P2D);
    }

    @Override
    public void setup() {
        rectMode(CENTER);
        scene = new Scene(this);
        if(scene.is3D())
            scene.setType(Graph.Type.ORTHOGRAPHIC);

        node = new InteractiveFrame();
        eye = new InteractiveFrame();

        scene.setEye(eye);
        scene.setDefaultNode(eye);
        scene.setRadius(200);
        scene.fitBallInterpolation();
    }

    @Override
    public void draw() {
        background(0);
        scene.drawAxes(scene.radius());
        pushMatrix();
        scene.applyTransformation(node);
        if (node.grabsInput())
            fill(255, 0, 0);
        else
            fill(0,255,0);
        rect(0,0,50,100);
        popMatrix();

        if(target) {
            pushStyle();
            stroke(255);
            strokeWeight(2);
            scene.drawPickingTarget(node);
            popStyle();
        }

        scene.beginScreenDrawing();
        fill(0, 0, 255);
        rect(80,80,50,100);
        scene.endScreenDrawing();
    }

    @Override
    public void keyPressed() {
        if(key == 'f')
            target = !target;
        if(key == ' ')
            scene.flip();
        if(key == 'i')
            info();
    }

    public class InteractiveFrame extends Node {
        public InteractiveFrame() {
            super(scene);
        }

        protected InteractiveFrame(Graph otherGraph, InteractiveFrame otherFrame) {
            super(otherGraph, otherFrame);
        }

        @Override
        public InteractiveFrame get() {
            return new InteractiveFrame(this.graph(), this);
        }

        @Override
        public void interact(MotionEvent event) {
            switch (event.shortcut().id()) {
                case PApplet.LEFT:
                    rotate(event);
                    break;
                case PApplet.RIGHT:
                    translate(event);
                    break;
                case processing.event.MouseEvent.WHEEL:
                    scale(event);
                    break;
                case PApplet.CENTER:
                    if(isEye())
                        zoomOnRegion(event);
            }
        }

        @Override
        public void interact(KeyEvent event) {
            if (event.id() == PApplet.UP)
                translateYPos();
            if (event.id() == PApplet.DOWN)
                translateYNeg();
            if (event.id() == PApplet.LEFT)
                translateXNeg();
            if (event.id() == PApplet.RIGHT)
                translateXPos();
        }
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{"basics.TwoD"});
    }
}
