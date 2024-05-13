package com.game_engine;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class triangle {
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
        metrix matRotZ=metrix.RotateZ(fTheta);

        triangle triRotatedZ= new triangle();

        metrix.MultiplyMatrixVector(this.arr[0], triRotatedZ.arr[0], matRotZ);
        metrix.MultiplyMatrixVector(this.arr[1], triRotatedZ.arr[1], matRotZ);
        metrix.MultiplyMatrixVector(this.arr[2], triRotatedZ.arr[2], matRotZ);

        return triRotatedZ;

    }
    public triangle RotateY(float fTheta){
        metrix matRotZ=metrix.RotateY(fTheta);

        triangle triRotatedZ= new triangle();

        metrix.MultiplyMatrixVector(this.arr[0], triRotatedZ.arr[0], matRotZ);
        metrix.MultiplyMatrixVector(this.arr[1], triRotatedZ.arr[1], matRotZ);
        metrix.MultiplyMatrixVector(this.arr[2], triRotatedZ.arr[2], matRotZ);

        return triRotatedZ;

    }

    public triangle RotateX(float fTheta){
        metrix matRotX =metrix.RotateX(fTheta);

        triangle triRotatedX= new triangle();

        metrix.MultiplyMatrixVector(this.arr[0], triRotatedX.arr[0], matRotX);
        metrix.MultiplyMatrixVector(this.arr[1], triRotatedX.arr[1], matRotX);
        metrix.MultiplyMatrixVector(this.arr[2], triRotatedX.arr[2], matRotX);

        return triRotatedX;
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

        normal.x = line1.y * line2.z - line1.z * line2.y;
		normal.y = line1.z * line2.x - line1.x * line2.z;
		normal.z = line1.x * line2.y - line1.y * line2.x;

        double l = Math.sqrt(normal.x*normal.x + normal.y*normal.y + normal.z*normal.z);
        if(l>0){
            normal.x /= l; normal.y /= l; normal.z /= l;
        }
        return normal;
    }
}
