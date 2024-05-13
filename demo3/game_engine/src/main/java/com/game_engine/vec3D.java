package com.game_engine;

public class vec3D{
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