package com.testing;


import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import static java.lang.Math.sqrt;

class Player{
    public double x,y,z;             
    public double a;                 
    public double l;
    
    Player(double x, double y, double z, double a, double l){
        this.x=x;
        this.y=y;
        this.z=z;
        this.a=a;
        this.l=l;
    }
    public double cos(){
        return Math.cos(Math.toRadians(this.a));
    }
    public double sin(){
        return Math.sin(Math.toRadians(this.a));
    }
    public void look_left(){
        this.a-=4;
        if(a<  0){ a+=360;}
    }
    public void look_right(){
        this.a+=4;
        if(a>359){a-=360;}
    }
    public void look_up(){
        this.l-=1;
    }  
    public void look_down(){
        this.l+=1;
    }  
    public void move_forward(){
        double dx=Math.sin(Math.toRadians(this.a))*10;
        double dy=Math.cos(Math.toRadians(this.a))*10;
        this.x+=dx; this.y+=dy;
    }
    public void move_backward(){
        double dx=Math.sin(Math.toRadians(this.a))*10;
        double dy=Math.cos(Math.toRadians(this.a))*10;
        this.x-=dx; this.y-=dy;
    }
    public void move_left(){
        double dx=Math.sin(Math.toRadians(this.a))*10;
        double dy=Math.cos(Math.toRadians(this.a))*10;
        this.x+=dy; this.y-=dx;
    }
    public void move_right(){
        double dx=Math.sin(Math.toRadians(this.a))*10;
        double dy=Math.cos(Math.toRadians(this.a))*10;
        this.x-=dy; this.y+=dx;
    }
    public void move_up(){
        this.z-=4;
    }
    public void move_down(){
        this.z+=4;
    }
}


class Polygon3D {
    public double[][] wall={{-500,500,0}, {500,500,0},
                             {-500,1000,0}, {500,1000,0},
                             {-500,500,0}, {-500,1000,0},
                             {500,500,0}, {500,1000,0}
                        };
    private int SW=App.SW, SH=App.SH;
    public double[][][] wal2d= new double [4][4][4];
    public Polygon[] poly=new Polygon[4];
    public Player ply=App.ply;
    public Group grp;

    public Polygon3D(Group g){
        this.grp =g;
        draw();
        int i=0;
        for (double[][] sec : wal2d) {
            Polygon polygon = new Polygon();
            polygon.getPoints().addAll(
                sec[0][0], sec[0][1],
                sec[1][0], sec[1][1],
                sec[3][0], sec[3][1],
                sec[2][0], sec[2][1]
                );
            
            if (i==0) {
                polygon.setFill(Color.YELLOW);
            }
            else if (i==1) {
                polygon.setFill(Color.PURPLE);
            }
            else if (i==2) {
                polygon.setFill(Color.GREEN);
            }
            else{polygon.setFill(Color.RED);}
            polygon.setStroke(Color.DARKBLUE);
            polygon.setStrokeWidth(1);
            grp.getChildren().add(polygon);
            poly[i]=polygon;
            i++;
        } // Set fill color
        update_polygone();
    }

    public void draw(){
        for(int i=0; i<wall.length; i+=2){
            draw3d(wall[i], wall[i+1], i/2);
        }

    }
    public static double dist(double x1, double y1, double x2, double y2) {
        double distance = sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        return distance;
    }
    public void update_polygone(){
        draw();
        int[] ind ={0,1,2,3};
        for(int i=0; i<4; i++){
            for(int j=0; j<3-i; j++){
                if(wal2d[ind[j]][0][3] < wal2d[ind[j+1]][0][3])
                {
                    int ttemp = ind[j];
                    ind[j]=ind[j+1];
                    ind[j+1]=ttemp;

                }
            }
        }
        for(int i=0; i<4; i++){
            poly[i].getPoints().setAll(
                wal2d[i][0][0], wal2d[i][0][1],
                wal2d[i][1][0], wal2d[i][1][1],
                wal2d[i][3][0], wal2d[i][3][1],
                wal2d[i][2][0], wal2d[i][2][1]
            );
        }
        for (int i=0; i<4; i++) {
            poly[ind[i]].toFront();
        }
    }

    private void draw3d(double[] left, double[] right, int i){
        double[] ponit1_pos=new double[4];
        double[] ponit2_pos=new double[4];
        double[] ponit3_pos=new double[4];
        double[] ponit4_pos=new double[4];

        double x1=left[0]-ply.x, y1=left[1]-ply.y, z1=left[2]-ply.z;
        double x2=right[0]-ply.x, y2=right[1]-ply.y, z2=right[2]-ply.z;

        ponit1_pos[0]=x1*ply.cos()-y1*ply.sin();
        ponit2_pos[0]=x2*ply.cos()-y2*ply.sin();

        ponit1_pos[1]=y1*ply.cos()+x1*ply.sin();
        ponit2_pos[1]=y2*ply.cos()+x2*ply.sin();
        ponit1_pos[3]=dist(0,0, (ponit1_pos[0]+ponit2_pos[0])/2, (ponit1_pos[1]+ponit2_pos[1])/2 ); 

        ponit1_pos[2]=z1+((ply.l*ponit1_pos[1])/32);
        ponit2_pos[2]=z2+((ply.l*ponit2_pos[1]/32));


        ponit3_pos[0]=ponit1_pos[0];
        ponit4_pos[0]=ponit2_pos[0];

        ponit3_pos[1]=ponit1_pos[1];
        ponit4_pos[1]=ponit2_pos[1];

        ponit3_pos[2]=ponit1_pos[2]+1000;
        ponit4_pos[2]=ponit2_pos[2]+1000;
        
        if(ponit1_pos[1]<1 && ponit2_pos[1]<1)return;
        if(ponit1_pos[1]<1){
            clipBehindPlayer(ponit1_pos, ponit2_pos);
            clipBehindPlayer(ponit3_pos, ponit4_pos);
        }
        if(ponit2_pos[1]<1){
            clipBehindPlayer(ponit2_pos, ponit1_pos);
            clipBehindPlayer(ponit4_pos, ponit3_pos);
        }

        ponit1_pos[0]=ponit1_pos[0]*200/ponit1_pos[1]+(SW/2);
        ponit1_pos[1]=ponit1_pos[2]*200/ponit1_pos[1]+(SH/2);
        ponit2_pos[0]=ponit2_pos[0]*200/ponit2_pos[1]+(SW/2);
        ponit2_pos[1]=ponit2_pos[2]*200/ponit2_pos[1]+(SH/2);
        ponit3_pos[0]=ponit3_pos[0]*200/ponit3_pos[1]+(SW/2);
        ponit3_pos[1]=ponit3_pos[2]*200/ponit3_pos[1]+(SH/2);
        ponit4_pos[0]=ponit4_pos[0]*200/ponit4_pos[1]+(SW/2);
        ponit4_pos[1]=ponit4_pos[2]*200/ponit4_pos[1]+(SH/2);
        double[][] temp= new double[4][4];
        temp[0]=ponit1_pos;temp[1]=ponit2_pos;
        temp[2]=ponit3_pos;temp[3]=ponit4_pos;
        wal2d[i]=temp;

    }
    private void clipBehindPlayer(double[] point_pos1, double[] point_pos2 ){
        double da=point_pos1[1];                                 //distance plane -> point a
        double db= point_pos2[1];                                 //distance plane -> point b
        double d=da-db; if(d==0){ d=1;}
        double s = da/d;                         //intersection factor (between 0 and 1)
        point_pos1[0] = point_pos1[0] + s*(point_pos2[0]-(point_pos1[0]));
        point_pos1[1] = point_pos1[1] + s*(point_pos2[1]-(point_pos1[1]));
        if(Math.abs(point_pos1[1]) < 1e-6){point_pos1[1]=1;} //prevent divide by zero 
        point_pos1[2] = point_pos1[2] + s*(point_pos2[2]-(point_pos1[2]));
    }
    
}


public class App extends Application {
    
    public static Player ply = new Player(0, 0, 0, 0, 0);
    public static int SW=1000, SH=700;
    @Override
    public void start(Stage stage) {

        Group root = new Group();
        Scene scene = new Scene(root, SW, SH, Color.BLUE);
        Polygon3D pply=new Polygon3D(root);
        Polygon3D pply2=new Polygon3D(root);
        double[][] wall2={{-1200,500,0}, {-800,500,0},
                             {-1200,1000,0}, {-800,1000,0},
                             {-1200,500,0}, {-1200,1000,0},
                             {-800,500,0}, {-800,1000,0}
                        };
        pply2.wall=wall2;
        scene.setOnKeyPressed(event -> handleKeyPressed(event, pply, pply2));
        //root.getChildren().add(poly);
        
        stage.setTitle("3D world");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
   

    private void handleKeyPressed(KeyEvent event, Polygon3D poly, Polygon3D poly2) {
        switch (event.getCode()) {
            case W:
                ply.move_forward();
                poly.update_polygone();
                poly2.update_polygone();
                System.out.println(event.getCode());
                break;
            case S:
                ply.move_backward();
                poly.update_polygone();
                poly2.update_polygone();
                System.out.println(event.getCode());
                break;
            case A:
                ply.move_right();
                poly.update_polygone();
                poly2.update_polygone();
                System.out.println(event.getCode());
                break;
            case D:
                ply.move_left();
                poly.update_polygone();
                poly2.update_polygone();
                System.out.println(event.getCode());
                break;
            case Q:
                ply.move_up();
                poly.update_polygone();
                poly2.update_polygone();
                System.out.println(event.getCode());
                break;
            case E:
                ply.move_down();
                poly.update_polygone();
                poly2.update_polygone();
                System.out.println(event.getCode());
                break;
            case UP:
                ply.look_down();
                poly.update_polygone();
                poly2.update_polygone();
                System.out.println(event.getCode());
                break;
            case DOWN:
                ply.look_up();
                poly.update_polygone();
                poly2.update_polygone();
                System.out.println(event.getCode());
                break;
            case LEFT:
                ply.look_left();
                poly.update_polygone();
                poly2.update_polygone();
                System.out.println(event.getCode());
                break;
            case RIGHT:
                ply.look_right();
                poly.update_polygone();
                poly2.update_polygone();
                System.out.println(event.getCode());
                break;
            default:
                break;
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}