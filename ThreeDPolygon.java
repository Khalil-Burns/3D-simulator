import java.awt.*;

public class ThreeDPolygon {

    PolygonObject newPolygon;
    Color c;
    double[] x;
    double[] y;
    double[] z;
    double[] distToCam;
    mPoint normal, line1, line2;
    double normalLength;
    mPoint origin;

    public ThreeDPolygon(double[] _x, double[] _y, double[] _z, Color _c) {
        this.x = _x;
        this.y = _y;
        this.z = _z;
        this.c = _c;
        this.distToCam = new double[this.x.length];

        this.updatePolygon();
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

    public void updatePolygon() {
        int[] newX = new int[this.x.length];
        int[] newY = new int[this.y.length];
        this.origin = new mPoint(this.x[0], this.y[0], this.z[0]);

        normal = new mPoint();
        line1 = new mPoint();
        line2 = new mPoint();

        for (int i = 0; i < this.x.length; i++) {
            PointConvert newPoint = new PointConvert(this.x[i], this.y[i], this.z[i]);
            // newX[i] = (int)newPoint.newX;
            // newY[i] = (int)newPoint.newY;
            if (newPoint.z > 0) {
                newX[i] = (int)newPoint.newX;
                newY[i] = (int)newPoint.newY;
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
        // this.normal.x /= this.normalLength;
        // this.normal.y /= this.normalLength;
        // this.normal.z /= this.normalLength;

        //System.out.println("Origin: " + this.origin.x + " " + this.origin.y + " " + this.origin.z + " P1: " + this.x[1] + " " + this.y[1] + " " + this.z[1] + " P2: " + this.x[2] + " " + this.y[2] + " " + this.z[2]);
        this.newPolygon = new PolygonObject(newX, newY, this.c);
    }
}
