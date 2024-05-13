package com.game_engine;

import java.util.ArrayList;
import java.util.Collections;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class World {
    private String[] obj={"src/main/resources/com/game_engine/axis.obj", 
                          "src/main/resources/com/game_engine/teapot.obj",
                          "src/main/resources/com/game_engine/VideoShip.obj"};
    private vec3D camera=new vec3D(0,0,-8);
    private vec3D light=new vec3D(0,0,-1);
    public Group group= new Group();
    private float fTheta;
    private mesh cuibe;
    private metrix matProj;
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;
    private long lastTime = System.nanoTime();
    private int frameCount = 0;
    private Label fpsLabel = new Label();
    private Canvas canvas;
    private GraphicsContext gc;


    public World(){
        group.getChildren().add(fpsLabel);
        cuibe=new mesh(obj[1]);
        
        matProj=metrix.matProj();
        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        group.getChildren().add(canvas);
        
    }
    public void update() {

    
        fTheta += 0.01f;
        ArrayList<triangle> buffer = new ArrayList<>();
    
        for (triangle tri : cuibe.tris) {
            triangle protri = new triangle(tri);
            triangle triRotatedZX = new triangle(tri).RotateX(fTheta).RotateY(fTheta).RotateZ(fTheta);
            
            //triRotatedZX=triRotatedZX.RotateX(180);
            triRotatedZX.calculateNormal();
    
            double dotproduct = (triRotatedZX.normal.x * (triRotatedZX.arr[0].x - camera.x)) +
                    (triRotatedZX.normal.y * (triRotatedZX.arr[0].y - camera.y)) +
                    (triRotatedZX.normal.z * (triRotatedZX.arr[0].z - camera.z));
            
            if (dotproduct < 0) {
                
                triangle transtri = new triangle(triRotatedZX);
                double l = Math.sqrt(light.x * light.x + light.y * light.y + light.z * light.z);
                light.x /= l;
                light.y /= l;
                light.z /= l;
                transtri.calculateluminescence(light);
    
                transtri.arr[0].z += 7.0f;
                transtri.arr[1].z += 7.0f;
                transtri.arr[2].z += 7.0f;
                metrix.MultiplyMatrixVector(transtri.arr[0], protri.arr[0], matProj);
                metrix.MultiplyMatrixVector(transtri.arr[1], protri.arr[1], matProj);
                metrix.MultiplyMatrixVector(transtri.arr[2], protri.arr[2], matProj);
    
                protri.arr[0].x -= 1.0f;
                protri.arr[0].y -= 1.0f;
                protri.arr[1].x -= 1.0f;
                protri.arr[1].y -= 1.0f;
                protri.arr[2].x -= 1.0f;
                protri.arr[2].y -= 1.0f;
                protri.arr[0].x *= -0.5f * (float) WIDTH;
                protri.arr[0].y *= -0.5f * (float) HEIGHT;
                protri.arr[1].x *= -0.5f * (float) WIDTH;
                protri.arr[1].y *= -0.5f * (float) HEIGHT;
                protri.arr[2].x *= -0.5f * (float) WIDTH;
                protri.arr[2].y *= -0.5f * (float) HEIGHT;
                

                protri.dp = transtri.dp;
                buffer.add(protri);
            }
        }
        Collections.sort(buffer, (t1, t2) -> {
            double avgZ1 = (t1.arr[0].z + t1.arr[1].z + t1.arr[2].z) / 3.0;
            double avgZ2 = (t2.arr[0].z + t2.arr[1].z + t2.arr[2].z) / 3.0;
            return Double.compare(avgZ1, avgZ2)*-1 ;
        });
    
        renderTriangles(buffer);
    }
    
    public void updateFrameRate(long now) {
        frameCount++;

        if (now - lastTime >= 1e9) { // One second has passed
            double fps = frameCount / ((now - lastTime) / 1e9);
            fpsLabel.setText(String.format("FPS: %.2f", fps));

            // Reset counters
            frameCount = 0;
            lastTime = now;
        }
    }
    private void renderTriangles(ArrayList<triangle> buffer) {
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        for (triangle tir : buffer) {
            double brightness=(tir.dp+1)/2.0;    
            gc.setFill(new Color(brightness*0.5, brightness*0.5, brightness, 1)); // Set fill color to null (no fill)
            gc.setStroke(null);
            gc.fillPolygon(
                    new double[]{tir.arr[0].x, tir.arr[1].x, tir.arr[2].x},
                    new double[]{tir.arr[0].y, tir.arr[1].y, tir.arr[2].y},
                    3
            );
        }
    }
}
