import java.awt.*;

public class ThreeDPolygon {

    PolygonObject newPolygon;
    Color c;
    int red, green, blue;
    double[] x;
    double[] y;
    double[] z;
    double[] distToCam;
    mPoint normal, line1, line2;
    double normalLength;
    mPoint origin;
    double colorShade;
    boolean created = false;

    public ThreeDPolygon(double[] _x, double[] _y, double[] _z, Color _c) {
        this.x = _x;
        this.y = _y;
        this.z = _z;
        this.c = _c;
        this.red = this.c.getRed();
        this.green = this.c.getGreen();
        this.blue = this.c.getBlue();
        this.distToCam = new double[this.x.length];

        //this.updatePolygon();
    }
    public ThreeDPolygon(Triangle tri, Color _c) {
        this(
            new double[]{tri.vertices[0].x, tri.vertices[1].x, tri.vertices[2].x},
            new double[]{tri.vertices[0].y, tri.vertices[1].y, tri.vertices[2].y},
            new double[]{tri.vertices[0].z, tri.vertices[1].z, tri.vertices[2].z},
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
        int[] newX = new int[this.x.length];
        int[] newY = new int[this.y.length];
        this.origin = new mPoint(this.x[0], this.y[0], this.z[0]);

        normal = new mPoint();
        line1 = new mPoint();
        line2 = new mPoint();

        PointConvert newPoint;
        double[] transformX = new double[3], transformY = new double[3], transformZ = new double[3];
        for (int i = 0; i < this.x.length; i++) {
            newPoint = new PointConvert(this.x[i], this.y[i], this.z[i]);
            newX[i] = (int)newPoint.newX;
            newY[i] = (int)newPoint.newY;
            transformX[i] = newPoint.tX;
            transformY[i] = newPoint.tY;
            transformZ[i] = newPoint.tZ;
            if (newPoint.z > 0) {
                // newX[i] = (int)newPoint.newX;
                // newY[i] = (int)newPoint.newY;
                distToCam[i] = Math.sqrt(newPoint.x*newPoint.x + newPoint.y*newPoint.y + newPoint.z*newPoint.z);
            }
            else {
                distToCam[i] = Double.MAX_VALUE;
            }
            
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
        
        Triangle[] clipped = clip(new mPoint(0, 0, 1), new mPoint(0, 0, 1), new Triangle(
            new double[]{transformX[0], transformY[0], transformZ[0]},
            new double[]{transformX[1], transformY[1], transformZ[1]}, 
            new double[]{transformX[2], transformY[2], transformZ[2]}
        ));
        //System.out.println(clipped.length);
        mPoint camRay = Matrix.vecSub(new mPoint(this.origin.x, this.origin.y, this.origin.z), new mPoint(Main.cam.x, Main.cam.y, Main.cam.z));
        if (Matrix.dotProduct(this.normal, camRay) < 0) {
            for (int i = 0; i < clipped.length; i++) {
                int[] newXPoints = new int[3], newYPoints = new int[3];
                for (int j = 0; j < 3; j++) {
                    mPoint newNewPoint = Matrix.transformPoint(clipped[i].vertices[j]);
                    newXPoints[j] = PointConvert.scaleX(newNewPoint.x);
                    newYPoints[j] = PointConvert.scaleY(-newNewPoint.y);
                    //System.out.println(newX[j] + " " + newY[j] + " " + clipped.length + c.toString());
                }
                //System.out.println("V1: " + newXPoints[0] + ", " + newYPoints[0] + ", V2: " + newXPoints[1] + ", " + newYPoints[1] + ", V3: " + newXPoints[2] + ", " + newYPoints[2] + ", " + i);
                PolygonObject newNewPolygon = new PolygonObject(newXPoints, newYPoints, this.c);
                newNewPolygon.drawPolygon(g);
            }
        }
    }

    public Triangle[] clip(mPoint pPoint, mPoint pNorm, Triangle inTri) {
        pNorm = Matrix.vecNormalise(pNorm);

        mPoint[] insidePoints = new mPoint[3];
        mPoint[] outsidePoints = new mPoint[3];
        int nInsidePoints = 0;
        int nOutsidePoints = 0;

        double d0 = Matrix.distance(inTri.vertices[0], pPoint, pNorm);
        double d1 = Matrix.distance(inTri.vertices[1], pPoint, pNorm);
        double d2 = Matrix.distance(inTri.vertices[2], pPoint, pNorm);
        //System.out.println(d0 + " " + d1 + " " + d2);

        if (d0 >= 0) {
            insidePoints[nInsidePoints++] = inTri.vertices[0];
            //nInsidePoints++;
        }
        else {
            outsidePoints[nOutsidePoints++] = inTri.vertices[0];
            //nOutsidePoints++;
        }

        if (d1 >= 0) {
            insidePoints[nInsidePoints++] = inTri.vertices[1];
            //nInsidePoints++;
        }
        else {
            outsidePoints[nOutsidePoints++] = inTri.vertices[1];
            //nOutsidePoints++;
        }

        if (d2 >= 0) {
            insidePoints[nInsidePoints++] = inTri.vertices[2];
            //nInsidePoints++;
        }
        else {
            outsidePoints[nOutsidePoints++] = inTri.vertices[2];
            //nOutsidePoints++;
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
            Triangle out = new Triangle(inTri.vert1, inTri.vert2, inTri.vert3);
            out.vertices[1] = Matrix.intersectVectors(pPoint, pNorm, insidePoints[0], outsidePoints[0]);
            out.vertices[2] = Matrix.intersectVectors(pPoint, pNorm, insidePoints[0], outsidePoints[1]);
            return(new Triangle[]{out});
        }
        if (nInsidePoints == 2 && nOutsidePoints == 1) {
            //System.out.println(insidePoints[0].x + " " + insidePoints[0].y + " " + insidePoints[0].z + ",   " + insidePoints[1].x + " " + insidePoints[1].y + " " + insidePoints[1].z);
            Triangle out1 = new Triangle(inTri.vert1, inTri.vert2, inTri.vert3), out2 = new Triangle(inTri.vert1, inTri.vert2, inTri.vert3);
            out1.vertices[2] = Matrix.intersectVectors(pPoint, pNorm, insidePoints[0], outsidePoints[0]);

            out2.vertices[0] = inTri.vertices[1];
            out2.vertices[1] = out1.vertices[2];
            out2.vertices[2] = Matrix.intersectVectors(pPoint, pNorm, insidePoints[1], outsidePoints[0]);

            // System.out.println(out1.vertices[0].x + " " + out1.vertices[0].y + " " + out1.vertices[0].z);
            // System.out.println(out1.vertices[1].x + " " + out1.vertices[1].y + " " + out1.vertices[1].z);
            // System.out.println(out1.vertices[2].x + " " + out1.vertices[2].y + " " + out1.vertices[2].z);
            // System.out.println(out2.vertices[0].x + " " + out2.vertices[0].y + " " + out2.vertices[0].z);
            // System.out.println(out2.vertices[1].x + " " + out2.vertices[1].y + " " + out2.vertices[1].z);
            // System.out.println(out2.vertices[2].x + " " + out2.vertices[2].y + " " + out2.vertices[2].z);
            return(new Triangle[]{out1, out2});
        }
        return(new Triangle[]{});
    }
}
