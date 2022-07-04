import java.awt.*;
import java.awt.image.BufferedImage;
/*import java.util.LinkedList;
import java.util.Queue;*/

import javax.imageio.ImageIO;
import java.io.*;

public class ThreeDPolygon {

    PolygonObject newPolygon;
    Color c;
    int red, green, blue;
    double[] x;
    double[] y;
    double[] z;
    double[] distToCam;
    double avgDistToCam;
    mPoint normal, line1, line2;
    double normalLength;
    mPoint origin;
    double colorShade;
    boolean created = false;
    vec2d[] tex;
    BufferedImage image;

    public ThreeDPolygon(double[] _x, double[] _y, double[] _z, Triangle t, Color _c) {
        this.x = _x;
        this.y = _y;
        this.z = _z;
        this.c = _c;
        this.red = this.c.getRed();
        this.green = this.c.getGreen();
        this.blue = this.c.getBlue();
        this.tex = t.tex;
        this.distToCam = new double[this.x.length];
        try {
			this.image = ImageIO.read(new File("wood.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}


        //this.updatePolygon();
    }
    public ThreeDPolygon(Triangle tri, Color _c) {
        this(
            new double[]{tri.vertices[0].x, tri.vertices[1].x, tri.vertices[2].x},
            new double[]{tri.vertices[0].y, tri.vertices[1].y, tri.vertices[2].y},
            new double[]{tri.vertices[0].z, tri.vertices[1].z, tri.vertices[2].z},
            tri,
            _c
        );
    }

    public double avgDist() {
        double avg = 0;
        for (int i = 0; i < this.distToCam.length; i++) {
            avg += this.distToCam[i];
        }
        avg = avg / this.distToCam.length;
        return(avg);
    }

    public void updatePolygon(Graphics g) {
        this.avgDistToCam = avgDist();

        int[] newX = new int[this.x.length];
        int[] newY = new int[this.y.length];
        this.origin = new mPoint(this.x[0], this.y[0], this.z[0]);

        normal = new mPoint();
        line1 = new mPoint();
        line2 = new mPoint();

        PointConvert newPoint;
        double[] transformX = new double[3], transformY = new double[3], transformZ = new double[3];
        for (int i = 0; i < this.x.length; i++) {
            double tempX = this.x[i], tempY = this.y[i], tempZ = this.z[i];
            this.x[i] = PointConvert.applyStillRotationsX(tempX, tempY, tempZ);
            this.y[i] = PointConvert.applyStillRotationsY(tempX, tempY, tempZ);
            this.z[i] = PointConvert.applyStillRotationsZ(tempX, tempY, tempZ);
            newPoint = new PointConvert(this.x[i], this.y[i], this.z[i]);
            newX[i] = (int)newPoint.newX;
            newY[i] = (int)newPoint.newY;
            transformX[i] = newPoint.tX;
            transformY[i] = newPoint.tY;
            transformZ[i] = newPoint.tZ;
            distToCam[i] = Math.sqrt(newPoint.point.x*newPoint.point.x + newPoint.point.y*newPoint.point.y + newPoint.point.z*newPoint.point.z);
            
            if (i == 1) {
                // this.line1.x = newPoint.x - this.origin.x;
                // this.line1.y = newPoint.y - this.origin.y;
                // this.line1.z = newPoint.z - this.origin.z;

                this.line1.x = this.x[1] - this.x[0];
                this.line1.y = this.y[1] - this.y[0];
                this.line1.z = this.z[1] - this.z[0];
            }
            else if (i == 2) {
                // this.line2.x = newPoint.x - this.origin.x;
                // this.line2.y = newPoint.y - this.origin.y;
                // this.line2.z = newPoint.z - this.origin.z;

                this.line2.x = this.x[2] - this.x[0];
                this.line2.y = this.y[2] - this.y[0];
                this.line2.z = this.z[2] - this.z[0];
            }
        }

        this.normal.x = this.line1.y * this.line2.z - this.line1.z * this.line2.y;
        this.normal.y = this.line1.z * this.line2.x - this.line1.x * this.line2.z;
        this.normal.z = this.line1.x * this.line2.y - this.line1.y * this.line2.x;
        
        this.normalLength = Math.sqrt(this.normal.x * this.normal.x + this.normal.y * this.normal.y + this.normal.z * this.normal.z);
        this.normal = Matrix.vecNormalise(this.normal);
        // this.normal.x /= this.normalLength;
        // this.normal.y /= this.normalLength;
        // this.normal.z /= this.normalLength;

        //System.out.println("Origin: " + this.origin.x + " " + this.origin.y + " " + this.origin.z + " P1: " + this.x[1] + " " + this.y[1] + " " + this.z[1] + " P2: " + this.x[2] + " " + this.y[2] + " " + this.z[2]);
        //if (!this.created) {
            //this.created = true;
            int newRed = this.red, newGreen = this.green, newBlue = this.blue;
            //mPoint lightRay = Matrix.vecSub(new mPoint(this.origin.x, this.origin.y, this.origin.z), new mPoint(Main.cam.x, Main.cam.y, Main.cam.z));
            //mPoint lightRay = Matrix.vecSub(new mPoint(this.origin.x, this.origin.y, this.origin.z), Main.lightSource);
            //this.colorShade = Matrix.dotProduct(this.normal, lightRay);
            this.colorShade = Matrix.dotProduct(this.normal, Main.lightSource);
            //this.colorShade = Matrix.dotProduct(this.normal, new mPoint(Main.cam.x, Main.cam.y, Main.cam.z));
            //System.out.println(this.colorShade);
            this.colorShade -= 1;
            newRed += this.colorShade * 80;
            newGreen += this.colorShade * 80;
            newBlue += this.colorShade * 80;

            if (newRed > 255) {
                newRed = 255;
            }
            if (newRed < 0) {
                newRed = 0;
            }

            if (newGreen > 255) {
                newGreen = 255;
            }
            if (newGreen < 0) {
                newGreen = 0;
            }

            if (newBlue > 255) {
                newBlue = 255;
            }
            if (newBlue < 0) {
                newBlue = 0;
            }
            //System.out.println(this.colorShade + " " + this.red + " " + this.green + " " + this.blue);
            c = new Color(newRed, newGreen, newBlue, c.getAlpha());
        //}

        //this.newPolygon = new PolygonObject(newX, newY, this.c);
        //this.newPolygon.drawPolygon(g);
        
        Triangle[] clipped = clip(new mPoint(0, 0, 0.1), new mPoint(0, 0, 1), new Triangle(
            new double[]{transformX[0], transformY[0], transformZ[0]},
            new double[]{transformX[1], transformY[1], transformZ[1]}, 
            new double[]{transformX[2], transformY[2], transformZ[2]},
            new double[]{this.tex[0].u, this.tex[0].v},
            new double[]{this.tex[1].u, this.tex[1].v},
            new double[]{this.tex[2].u, this.tex[2].v}
        ));
        //System.out.println(clipped.length);
        mPoint camRay = Matrix.vecSub(new mPoint(this.origin.x, this.origin.y, this.origin.z), new mPoint(Main.cam.x, Main.cam.y, Main.cam.z));
        double[] tempX = new double[3], tempY = new double[3], tempZ = new double[3];
        double[] newV1 = new double[3], newV2 = new double[3], newV3 = new double[3];
        if (Matrix.dotProduct(this.normal, camRay) < 0) {
            for (int i = 0; i < clipped.length; i++) {
                int[] newXPoints = new int[3], newYPoints = new int[3];
                for (int j = 0; j < 3; j++) {
                    mPoint newNewPoint = Matrix.transformPoint(clipped[i].vertices[j]);
                    tempX[j] = newNewPoint.x;
                    tempY[j] = newNewPoint.y;
                    tempZ[i] = newNewPoint.z;
                    newXPoints[j] = PointConvert.scaleX(newNewPoint.x);
                    newYPoints[j] = PointConvert.scaleY(-newNewPoint.y);
                    switch(j) {
                        case 0:
                            newV1[0] = newXPoints[j];
                            newV1[1] = newYPoints[j];
                            newV1[2] = newNewPoint.z;
                            break;
                        case 1:
                            newV2[0] = newXPoints[j];
                            newV2[1] = newYPoints[j];
                            newV2[2] = newNewPoint.z;
                            break;
                        case 2:
                            newV3[0] = newXPoints[j];
                            newV3[1] = newYPoints[j];
                            newV3[2] = newNewPoint.z;
                            break;
                    }
                    //System.out.println(newX[j] + " " + newY[j] + " " + clipped.length + c.toString());
                }
                boolean crossHairTouching = pointInsideTri((Main.width / 2), (Main.height / 2), newXPoints[0], newYPoints[0], newXPoints[1], newYPoints[1], newXPoints[2], newYPoints[2]);
                //System.out.println(" " + crossHairTouching);
                //System.out.println("V1: " + newXPoints[0] + ", " + newYPoints[0] + ", V2: " + newXPoints[1] + ", " + newYPoints[1] + ", V3: " + newXPoints[2] + ", " + newYPoints[2] + ", " + i);
                PolygonObject newNewPolygon = new PolygonObject(newXPoints, newYPoints, this.c);
                //newNewPolygon.drawPolygon(g, crossHairTouching/*, tempX, tempY, tempZ*/);
                newNewPolygon.texturedTriangle(g, this.image, newXPoints[0], newYPoints[0], this.tex[0].u, this.tex[0].v, newXPoints[1], newYPoints[1], this.tex[1].u, this.tex[0].v, newXPoints[2], newYPoints[2], this.tex[2].u, this.tex[0].v);


                /*  this currently does not work, and doesn't seem to have a large effect
                Queue<Triangle> listTris = new LinkedList<Triangle>();
                listTris.add(new Triangle(newV1, newV2, newV3));
                int nNewTris = 1;
                for (int p = 0; p < 4; p++) {
                    int nTrisToAdd = 0;
                    while (nNewTris > 0) {
                        Triangle temp = listTris.remove();
                        nNewTris--;
                        Triangle[] reClipped;
                        switch(p) {
                            case 0:
                                reClipped = clip(new mPoint(0, 0, 0), new mPoint(0, 1, 0), temp);
                                break;
                            case 1:
                                reClipped = clip(new mPoint(0, Main.height - 1.0, 0), new mPoint(0, -1, 0), temp);
                                break;
                            case 2:
                                reClipped = clip(new mPoint(0, 0, 0), new mPoint(1, 0, 0), temp);
                                break;
                            default:
                                reClipped = clip(new mPoint(Main.width - 1.0, 0, 0), new mPoint(-1, 0, 0), temp);
                                break;
                        }
                        nTrisToAdd = reClipped.length;
                        
                        for (int w = 0; w < nTrisToAdd; w++) {
                            listTris.add(reClipped[w]);
                        }
                    }
                    nNewTris = listTris.size();
                }
                for (int h = 0; h < listTris.size(); h++) {
                    Triangle newTemp = listTris.remove();

                    PolygonObject newNewPolygon = new PolygonObject(new int[]{(int)newTemp.vertices[0].x, (int)newTemp.vertices[1].x, (int)newTemp.vertices[2].x}, new int[]{(int)newTemp.vertices[0].y, (int)newTemp.vertices[1].y, (int)newTemp.vertices[2].y}, this.c);
                    newNewPolygon.drawPolygon(g, tempX, tempY, tempZ);
                }*/
            }
        }
    }

    public Triangle[] clip(mPoint pPoint, mPoint pNorm, Triangle inTri) {
        pNorm = Matrix.vecNormalise(pNorm);

        mPoint[] insidePoints = new mPoint[3];
        mPoint[] outsidePoints = new mPoint[3];
        vec2d[] insideTex = new vec2d[3];
        vec2d[] outsideTex = new vec2d[3];
        int nInsidePoints = 0;
        int nOutsidePoints = 0;
        int nInsideTex = 0;
        int nOutsideTex = 0;

        double d0 = Matrix.distance(inTri.vertices[0], pPoint, pNorm);
        double d1 = Matrix.distance(inTri.vertices[1], pPoint, pNorm);
        double d2 = Matrix.distance(inTri.vertices[2], pPoint, pNorm);
        //System.out.println(d0 + " " + d1 + " " + d2);

        if (d0 >= 0) {
            insidePoints[nInsidePoints++] = inTri.vertices[0];
            insideTex[nInsideTex++] = inTri.tex[0];
        }
        else {
            outsidePoints[nOutsidePoints++] = inTri.vertices[0];
            outsideTex[nOutsideTex++] = inTri.tex[0];
        }

        if (d1 >= 0) {
            insidePoints[nInsidePoints++] = inTri.vertices[1];
            insideTex[nInsideTex++] = inTri.tex[1];
        }
        else {
            outsidePoints[nOutsidePoints++] = inTri.vertices[1];
            outsideTex[nOutsideTex++] = inTri.tex[1];
        }

        if (d2 >= 0) {
            insidePoints[nInsidePoints++] = inTri.vertices[2];
            insideTex[nInsideTex++] = inTri.tex[2];
        }
        else {
            outsidePoints[nOutsidePoints++] = inTri.vertices[2];
            outsideTex[nOutsideTex++] = inTri.tex[2];
        }
        //System.out.println(nInsidePoints);
        if (nInsidePoints == 0) {
            //System.out.println("none inside");
            return(new Triangle[]{});
        }
        if (nInsidePoints == 3) {
            //System.out.println("three inside");
            return(new Triangle[]{inTri});
        }
        if (nInsidePoints == 1 && nOutsidePoints == 2) {
            //System.out.println("one inside");
            Triangle out = new Triangle(new double[]{-1, -1, -1}, new double[]{-1, -1, -1}, new double[]{-1, -1, -1}, new double[]{-1, -1}, new double[]{-1, -1}, new double[]{-1, -1});
            
            double[] tVals = new double[1];
            out.vertices[0] = insidePoints[0];
            out.tex[0] = insideTex[0];

            out.vertices[1] = Matrix.intersectVectors(pPoint, pNorm, insidePoints[0], outsidePoints[0], tVals);
            out.tex[1].u = tVals[0] * (outsideTex[0].u - insideTex[0].u) + insideTex[0].u;
            out.tex[1].v = tVals[0] * (outsideTex[0].v - insideTex[0].v) + insideTex[0].v;

            out.vertices[2] = Matrix.intersectVectors(pPoint, pNorm, insidePoints[0], outsidePoints[1], tVals);
            out.tex[2].u = tVals[0] * (outsideTex[1].u - insideTex[0].u) + insideTex[0].u;
            out.tex[2].v = tVals[0] * (outsideTex[1].v - insideTex[0].v) + insideTex[0].v;

            //this.distToCam[idxOfOutside[0]] = out.vertices[1].x * out.vertices[1].x + out.vertices[1].y * out.vertices[1].y + out.vertices[1].z * out.vertices[1].z;
            //this.distToCam[idxOfOutside[1]] = out.vertices[2].x * out.vertices[2].x + out.vertices[2].y * out.vertices[2].y + out.vertices[2].z * out.vertices[2].z;
            return(new Triangle[]{out});
        }
        if (nInsidePoints == 2 && nOutsidePoints == 1) {
            //System.out.println(insidePoints[0].x + " " + insidePoints[0].y + " " + insidePoints[0].z + ",   " + insidePoints[1].x + " " + insidePoints[1].y + " " + insidePoints[1].z);
            Triangle out1 = new Triangle(new double[]{-1, -1, -1}, new double[]{-1, -1, -1}, new double[]{-1, -1, -1}, new double[]{-1, -1}, new double[]{-1, -1}, new double[]{-1, -1}), out2 =new Triangle(new double[]{-1, -1, -1}, new double[]{-1, -1, -1}, new double[]{-1, -1, -1}, new double[]{-1, -1}, new double[]{-1, -1}, new double[]{-1, -1});

            double[] tVals = new double[1];
            out1.vertices[0] = insidePoints[0];
            out1.tex[0] = insideTex[0];
            out1.vertices[1] = insidePoints[1];
            out1.tex[0] = insideTex[1];
            out1.vertices[2] = Matrix.intersectVectors(pPoint, pNorm, insidePoints[0], outsidePoints[0], tVals);
            out1.tex[1].u = tVals[0] * (outsideTex[0].u - insideTex[0].u) + insideTex[0].u;
            out1.tex[1].v = tVals[0] * (outsideTex[0].v - insideTex[0].v) + insideTex[0].v;
            
            out2.vertices[0] = insidePoints[1];
            out2.tex[0] = insideTex[1];
            out2.vertices[1] = out1.vertices[2];
            out2.tex[1] = out1.tex[2];
            out2.vertices[2] = Matrix.intersectVectors(pPoint, pNorm, insidePoints[1], outsidePoints[0], tVals);
            out2.tex[1].u = tVals[0] * (outsideTex[0].u - insideTex[1].u) + insideTex[1].u;
            out2.tex[1].v = tVals[0] * (outsideTex[0].v - insideTex[1].v) + insideTex[1].v;

            // System.out.println(out1.vertices[0].x + " " + out1.vertices[0].y + " " + out1.vertices[0].z);
            // System.out.println(out1.vertices[1].x + " " + out1.vertices[1].y + " " + out1.vertices[1].z);
            // System.out.println(out1.vertices[2].x + " " + out1.vertices[2].y + " " + out1.vertices[2].z);
            // System.out.println(out2.vertices[0].x + " " + out2.vertices[0].y + " " + out2.vertices[0].z);
            // System.out.println(out2.vertices[1].x + " " + out2.vertices[1].y + " " + out2.vertices[1].z);
            // System.out.println(out2.vertices[2].x + " " + out2.vertices[2].y + " " + out2.vertices[2].z);
            //double temp1 = out1.vertices[2].x * out1.vertices[2].x + out1.vertices[2].y * out1.vertices[2].y + out1.vertices[2].z * out1.vertices[2].z;
            //double temp2 = out2.vertices[2].x * out2.vertices[2].x + out2.vertices[2].y * out2.vertices[2].y + out2.vertices[2].z * out2.vertices[2].z;
            //this.distToCam[idxOfOutside[0]] = temp1 + temp2 / 2.0;
            return(new Triangle[]{out1, out2});
        }
        return(new Triangle[]{});
    }

    public static boolean pointInsideTri(double pointX, double pointY, double v0X, double v0Y, double v1X, double v1Y, double v2X, double v2Y) {
        //System.out.print(pointX + " " + pointY + " " + v0X + " " + v0Y + " " + v1X + " " + v1Y + " " + v2X + " " + v2Y);
        /*double dX = pointX - v2X;
        double dY = pointY - v2Y;
        double dX21 = v2X - v1X;
        double dY12 = v1Y - v2Y;
        double D = dY12*(v0X - v2X) + dX21*(v0Y - v2Y);
        double s = dY12*dX + dX21*dY;
        double t = dX*(v2Y - v0Y) + dY*(v0X - v2Y);*/
        double A = 1/2.0 * (-v1Y * v2X + v0Y * (-v1X + v2X) + v0X * (v1Y - v2Y) + v1X * v2Y);
        double sign = A < 0? -1:1;
        double s = (v0Y * v2X - v0X * v2Y + (v2Y - v0Y) * pointX + (v0X - v2X) * pointY) * sign;
        double t = (v0X * v1Y - v0Y * v1X + (v0Y - v1Y) * pointX + (v1X - v0X) * pointY) * sign;
        if (s > 0 && t > 0 && (s + t) < (2 * A * sign)) {
            return(true);
        }
        else {
            return(false);
        }
        /*if (D>0) {
            if (s<=0 && t<=0 && (s+t)>=D) {
                return(true);
            }
            else {
                return(false);
            }
        }
        else {
            if (s>=0 && t>=0 && (s+t)<=D) {
                return(true);
            }
            else {
                return(false);
            }
        }*/
    }
}