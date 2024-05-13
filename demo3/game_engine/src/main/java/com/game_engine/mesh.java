package com.game_engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class mesh {
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
