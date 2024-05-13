package com.game_engine;

public class metrix {
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
    public static metrix matProj(){
        float fNear = 0.1f;
		float fFar = 1000.0f;
		double fFov = 90.0f;
		float fAspectRatio = (float)700 / (float)1000;
		float fFovRad = (float)(1.0 / Math.tan(Math.toRadians(fFov * 0.5)));

		return new metrix(
            fAspectRatio*fFovRad, 0         , 0                         ,0,
            0                   , fFovRad   , 0                         ,0,
            0                   , 0         ,fFar/(fFar-fNear)          ,1.0f,
            0                   , 0         ,(-fFar*fNear)/(fFar-fNear) ,0
        );
    }

    public static metrix RotateZ(float fTheta){
        metrix matRotZ=new metrix();
        matRotZ.metr[0][0] = (float)(Math.cos(fTheta));
		matRotZ.metr[0][1] = (float)(Math.sin(fTheta));
		matRotZ.metr[1][0] = (float)-(Math.sin(fTheta));
		matRotZ.metr[1][1] = (float)(Math.cos(fTheta));
		matRotZ.metr[2][2] = 1;
		matRotZ.metr[3][3] = 1;
        return matRotZ;
    }

    public static metrix RotateY(float fAngleRad){

        metrix matrix=new metrix();

		matrix.metr[0][0] = (float)Math.cos(fAngleRad);
		matrix.metr[0][2] = (float)Math.sin(fAngleRad);
		matrix.metr[2][0] = -(float)Math.sin(fAngleRad);
		matrix.metr[1][1] = 1.0f;
		matrix.metr[2][2] = (float)Math.cos(fAngleRad);
		matrix.metr[3][3] = 1.0f;
		return matrix;
    }

    public static metrix RotateX(float fAngleRad){

        metrix matRotX =new metrix();

		matRotX.metr[0][0] = 1;
		matRotX.metr[1][1] = (float)(Math.cos(fAngleRad));
		matRotX.metr[1][2] = (float)(Math.sin(fAngleRad));
		matRotX.metr[2][1] = -(float)(Math.sin(fAngleRad));
		matRotX.metr[2][2] = (float)(Math.cos(fAngleRad));
		matRotX.metr[3][3] = 1;
		return matRotX;
    }

    public static void MultiplyMatrixVector(vec3D i, vec3D o, metrix m)
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
}
