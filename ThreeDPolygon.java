import java.awt.*;

public class ThreeDPolygon {

    PolygonObject newPolygon;
    Color c;
    double[] x;
    double[] y;
    double[] z;
    double[] distToCam;

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
            Color.BLACK
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
        for (int i = 0; i < this.x.length; i++) {
            PointConvert point = new PointConvert(this.x[i], this.y[i], this.z[i]);
            if (point.z > 0) {
                newX[i] = (int)point.newX;
                newY[i] = (int)point.newY;
                distToCam[i] = Math.sqrt(point.x*point.x + point.y*point.y + point.z*point.z);
            }
            else {
                distToCam[i] = Double.MAX_VALUE;
            }
        }
        this.newPolygon = new PolygonObject(newX, newY, this.c);
    }
}
