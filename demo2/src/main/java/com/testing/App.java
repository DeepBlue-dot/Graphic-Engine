package com.testing;


import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Collections;


class vec3D{
    public float x;
    public float y;
    public float z;

    public vec3D(float x, float y, float z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
    public vec3D(vec3D v){
        this.x=v.x;
        this.y=v.y;
        this.z=v.z;
    }
}

class triangle{
    public vec3D[] arr;
    public vec3D normal=new vec3D(0,0,0);
    public double dp;
    public triangle(vec3D[] arr){
        this.arr=new vec3D[3];
        this.arr[0]=new vec3D(arr[0]);
        this.arr[1]=new vec3D(arr[1]);
        this.arr[2]=new vec3D(arr[2]);
        calculateNormal();
    }

    public triangle(triangle tri){
        this(tri.arr);
        calculateNormal();
    }

    public triangle(){
        this(new vec3D(0, 0, 0) , new vec3D (0, 0, 0) , new vec3D  (0, 0, 0)); 
        calculateNormal();
    }

    public triangle(vec3D a, vec3D b, vec3D c){
        this.arr=new vec3D[3];
        arr[0]=a;
        arr[1]=b;
        arr[2]=c;
    }

    public void calculateluminescence(vec3D light){
        calculateNormal();
        dp = (normal.x * light.x) + (normal.y * light.y) + (normal.z * light.z);
    }

    public triangle RotateZ(float fTheta){
        metrix matRotZ=new metrix();

		matRotZ.metr[0][0] = (float)(Math.cos(fTheta));
		matRotZ.metr[0][1] = (float)(Math.sin(fTheta));
		matRotZ.metr[1][0] = (float)-(Math.sin(fTheta));
		matRotZ.metr[1][1] = (float)(Math.cos(fTheta));
		matRotZ.metr[2][2] = 1;
		matRotZ.metr[3][3] = 1;

        triangle triRotatedZ= new triangle();

        MultiplyMatrixVector(this.arr[0], triRotatedZ.arr[0], matRotZ);
        MultiplyMatrixVector(this.arr[1], triRotatedZ.arr[1], matRotZ);
        MultiplyMatrixVector(this.arr[2], triRotatedZ.arr[2], matRotZ);

        return triRotatedZ;

    }

    public triangle RotateX(float fTheta){
        metrix matRotX =new metrix();

		matRotX.metr[0][0] = 1;
		matRotX.metr[1][1] = (float)(Math.cos(fTheta * 0.5f));
		matRotX.metr[1][2] = (float)(Math.sin(fTheta * 0.5f));
		matRotX.metr[2][1] = -(float)(Math.sin(fTheta * 0.5f));
		matRotX.metr[2][2] = (float)(Math.cos(fTheta * 0.5f));
		matRotX.metr[3][3] = 1;

        triangle triRotatedX= new triangle();

        MultiplyMatrixVector(this.arr[0], triRotatedX.arr[0], matRotX);
        MultiplyMatrixVector(this.arr[1], triRotatedX.arr[1], matRotX);
        MultiplyMatrixVector(this.arr[2], triRotatedX.arr[2], matRotX);

        return triRotatedX;
    }

    void MultiplyMatrixVector(vec3D i, vec3D o, metrix m)
	{
		o.x = i.x * m.metr[0][0] + i.y * m.metr[1][0] + i.z * m.metr[2][0] + m.metr[3][0];
		o.y = i.x * m.metr[0][1] + i.y * m.metr[1][1] + i.z * m.metr[2][1] + m.metr[3][1];
		o.z = i.x * m.metr[0][2] + i.y * m.metr[1][2] + i.z * m.metr[2][2] + m.metr[3][2];
		float w = i.x * m.metr[0][3] + i.y * m.metr[1][3] + i.z * m.metr[2][3] + m.metr[3][3];

		if (w != 0.0f)
		{
			o.x /= w; o.y /= w; o.z /= w;
		}
	}
    public Polygon render(){
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(
            (double)this.arr[0].x, (double)this.arr[0].y,
            (double)this.arr[1].x, (double)this.arr[1].y,
            (double)this.arr[2].x, (double)this.arr[2].y
            );

        double brightness=(dp+1)/2.0;    
        polygon.setFill(new Color(brightness*0.5, brightness*0.1, brightness, 1));
        //polygon.setStroke(new Color(brightness*0.5, brightness, brightness, 0.5));
 
        return polygon;
    }
    public vec3D calculateNormal(){
        vec3D line1=new vec3D(this.arr[1].x-this.arr[0].x, this.arr[1].y-this.arr[0].y,this.arr[1].z-this.arr[0].z);
        vec3D line2=new vec3D(this.arr[2].x-this.arr[0].x, this.arr[2].y-this.arr[0].y,this.arr[2].z-this.arr[0].z);

        this.normal.x = line1.y * line2.z - line1.z * line2.y;
		this.normal.y = line1.z * line2.x - line1.x * line2.z;
		this.normal.z = line1.x * line2.y - line1.y * line2.x;

        double l = Math.sqrt(normal.x*normal.x + normal.y*normal.y + normal.z*normal.z);
        if(l>0){
            normal.x /= l; normal.y /= l; normal.z /= l;
        }
        return this.normal;
    }
}

class mesh{
    public ArrayList<triangle> tris=new ArrayList<>();
    public mesh(ArrayList<triangle> tris){
        this.tris=tris;
    }
    public mesh(triangle... arr){
        for (triangle tt : arr) {
            this.tris.add(tt);
        }
    }
    public mesh(String filename){
        File obj=new File(filename);
        ArrayList<vec3D> vectors=new ArrayList<>();
        try {
            Scanner sc=new Scanner(obj);
            while (sc.hasNextLine()){
                String line=sc.nextLine();
                if (line.startsWith("v")) {
                    String[] words = line.split("\\s+");
                    float x = Float.parseFloat(words[1]);
                    float y = Float.parseFloat(words[2]);
                    float z = Float.parseFloat(words[3]);
                    vectors.add(new vec3D(x, y, z));
                }
                if (line.startsWith("f")) {
                    String[] words = line.split("\\s+");
                    int index1 = Integer.parseInt(words[1]) - 1;
                    int index2 = Integer.parseInt(words[2]) - 1;
                    int index3 = Integer.parseInt(words[3]) - 1;
                    tris.add(new triangle(vectors.get(index1), vectors.get(index2), vectors.get(index3)));
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
class metrix{
    public float[][] metr={
                        {0, 0, 0, 0},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0}
                    };
    public metrix(float... arr){
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                metr[i][j]=arr[(i*4)+j];
            }
        }
    }
    public metrix(){

    }
}

public class App extends Application {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;
    private metrix matProj;
    private mesh cuibe;
    private Group group= new Group();
    private float fTheta;
    private vec3D camera=new vec3D(0,0,-15);
    private vec3D light=new vec3D(0,0,-1);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("3D_WORLD");

        Scene scene = new Scene(group, Color.BLACK);

        // Handle keyboard input
        scene.setOnKeyPressed(e -> handleKeyPress(e.getCode()));

        stage.setResizable(false);
        stage.setScene(scene);
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        starting();

        // Set up the game loop
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Update game logic
                update();
            }
        };
        

        stage.show();

        // Start the game loop
        gameLoop.start();
    }

    private void handleKeyPress(KeyCode code) {
        switch (code) {
            case LEFT:
                break;
            case RIGHT:
                break;
            case UP:
                break;
            case DOWN:
                break;
        }
    }
    private void starting(){
            /*cuibe=new mesh(
            // SOUTH
            new triangle(new vec3D(0.0f, 0.0f, 0.0f) , new vec3D (0.0f, 1.0f, 0.0f) , new vec3D  (1.0f, 1.0f, 0.0f)),
            new triangle(new vec3D(0.0f, 0.0f, 0.0f) , new vec3D (1.0f, 1.0f, 0.0f) , new vec3D  (1.0f, 0.0f, 0.0f)), 
            // EAST
            new triangle(new vec3D(1.0f, 0.0f, 0.0f) , new vec3D (1.0f, 1.0f, 0.0f) , new vec3D  (1.0f, 1.0f, 1.0f)),
            new triangle(new vec3D(1.0f, 0.0f, 0.0f) , new vec3D (1.0f, 1.0f, 1.0f) , new vec3D  (1.0f, 0.0f, 1.0f)),  
            // NORTH                                            
            new triangle(new vec3D(1.0f, 0.0f, 1.0f) , new vec3D (1.0f, 1.0f, 1.0f) , new vec3D  (0.0f, 1.0f, 1.0f)),
            new triangle(new vec3D(1.0f, 0.0f, 1.0f) , new vec3D (0.0f, 1.0f, 1.0f) , new vec3D  (0.0f, 0.0f, 1.0f)), 
            // WEST 
            new triangle(new vec3D(0.0f, 0.0f, 1.0f) , new vec3D (0.0f, 1.0f, 1.0f) , new vec3D  (0.0f, 1.0f, 0.0f)),
            new triangle(new vec3D(0.0f, 0.0f, 1.0f) , new vec3D (0.0f, 1.0f, 0.0f) , new vec3D  (0.0f, 0.0f, 0.0f)), 
            // TOP                                             
            new triangle(new vec3D(0.0f, 1.0f, 0.0f) , new vec3D (0.0f, 1.0f, 1.0f) , new vec3D  (1.0f, 1.0f, 1.0f)),
            new triangle(new vec3D(0.0f, 1.0f, 0.0f) , new vec3D (1.0f, 1.0f, 1.0f) , new vec3D  (1.0f, 1.0f, 0.0f)),  
            // BOTTOM 
            new triangle(new vec3D(1.0f, 0.0f, 1.0f) , new vec3D (0.0f, 0.0f, 1.0f) , new vec3D  (0.0f, 0.0f, 0.0f)),
            new triangle(new vec3D(1.0f, 0.0f, 1.0f) , new vec3D (0.0f, 0.0f, 0.0f) , new vec3D  (1.0f, 0.0f, 0.0f))
            );
            */
            cuibe=new mesh("/home/yeabsira/MyProjects/Java/3D_Game/demo3/game_engine/src/main/resources/com/game_engine/axis.obj");

        float fNear = 0.1f;
		float fFar = 1000.0f;
		double fFov = 90.0f;
		float fAspectRatio = (float)HEIGHT / (float)WIDTH;
		float fFovRad = (float)(1.0 / Math.tan(Math.toRadians(fFov * 0.5)));

		matProj=new metrix(
            fAspectRatio*fFovRad, 0         , 0                         ,0,
            0                   , fFovRad   , 0                         ,0,
            0                   , 0         ,fFar/(fFar-fNear)          ,1.0f,
            0                   , 0         ,(-fFar*fNear)/(fFar-fNear) ,0
        );

    }

    void MultiplyMatrixVector(vec3D i, vec3D o, metrix m)
	{
		o.x = i.x * m.metr[0][0] + i.y * m.metr[1][0] + i.z * m.metr[2][0] + m.metr[3][0];
		o.y = i.x * m.metr[0][1] + i.y * m.metr[1][1] + i.z * m.metr[2][1] + m.metr[3][1];
		o.z = i.x * m.metr[0][2] + i.y * m.metr[1][2] + i.z * m.metr[2][2] + m.metr[3][2];
		float w = i.x * m.metr[0][3] + i.y * m.metr[1][3] + i.z * m.metr[2][3] + m.metr[3][3];

		if (w != 0.0f)
		{
			o.x /= w; o.y /= w; o.z /= w;
		}
	}

    private void update() {
        
        group.getChildren().clear();
		//fTheta += 0.05f;
        ArrayList <triangle> buffer=new ArrayList<>();
        for (triangle tri : cuibe.tris) {
            triangle protri= new triangle();
            triangle triRotatedZ= tri.RotateZ(fTheta); 
            triangle triRotatedZX=triRotatedZ.RotateX(fTheta);
            triRotatedZX.calculateNormal();

            double dotproduct=(triRotatedZX.normal.x * (triRotatedZX.arr[0].x - camera.x)) + 
			                   (triRotatedZX.normal.y * (triRotatedZX.arr[0].y - camera.y)) +
			                    (triRotatedZX.normal.z * (triRotatedZX.arr[0].z - camera.z));

            if(dotproduct < 0){
                triangle trantri= new triangle(triRotatedZX);
                double l = Math.sqrt(light.x*light.x + light.y*light.y + light.z*light.z);
				light.x /= l; light.y /= l; light.z /= l;
                trantri.calculateluminescence(light);
            
                trantri.arr[0].z+=8.0f;
                trantri.arr[1].z+=8.0f;
                trantri.arr[2].z+=8.0f;
                MultiplyMatrixVector(trantri.arr[0], protri.arr[0], matProj);
                MultiplyMatrixVector(trantri.arr[1], protri.arr[1], matProj);
                MultiplyMatrixVector(trantri.arr[2], protri.arr[2], matProj);

                protri.arr[0].x += 1.0f; protri.arr[0].y += 1.0f;
                protri.arr[1].x += 1.0f; protri.arr[1].y += 1.0f;
                protri.arr[2].x += 1.0f; protri.arr[2].y += 1.0f;
                protri.arr[0].x *= 0.5f * (float)WIDTH;
                protri.arr[0].y *= 0.5f * (float)HEIGHT;
                protri.arr[1].x *= 0.5f * (float)WIDTH;
                protri.arr[1].y *= 0.5f * (float)HEIGHT;
                protri.arr[2].x *= 0.5f * (float)WIDTH;
                protri.arr[2].y *= 0.5f * (float)HEIGHT;
                protri.dp=trantri.dp;
                buffer.add(protri);
            }

        } 
        Collections.sort(buffer, (t1, t2) -> {
                double avgZ1 = (t1.arr[0].z + t1.arr[1].z + t1.arr[2].z) / 3.0;
                double avgZ2 = (t2.arr[0].z + t2.arr[1].z + t2.arr[2].z) / 3.0;
                return (Double.compare(avgZ1, avgZ2)*-1);
            });
            
            for (triangle tir : buffer) {
                group.getChildren().add(tir.render());
            }
            
    }
}
