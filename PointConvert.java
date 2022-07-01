public class PointConvert {

    double x, y, z;
    int newX, newY;
    mPoint point;

    public PointConvert(double _x, double _y, double _z) {
        this(new mPoint(_x, _y, _z));
        /*this.x = _x - Main.cam.x;
        this.y = _y - Main.cam.y;
        this.z = _z - Main.cam.z;
        applyRotations(x, y, z);
            //applyTransformations();
            //newX = (int)(((x)/(z))*50) + _main.width/2;
            //newY = (int)(((y)/(z))*50) + _main.height/2;
        this.newX = (int)(((this.x) * Main.height)/(2 * (this.z))) + Main.width/2;
        this.newY = (int)(((this.y) * Main.height)/(2 * (this.z))) + Main.height/2;*/
    }
    public PointConvert(mPoint _point) {
        //this(point.x, point.y, point.z);
        this.point = _point;
        this.point.x -= Main.cam.x;
        this.point.y *= -1;
        this.point.y -= Main.cam.y;
        this.point.z -= Main.cam.z;
        applyRotations(this.point.x, this.point.y, this.point.z);
        mPoint converted = Matrix.transformPoint(new mPoint(this.x, this.y, this.z));
        this.newX = scaleX(converted.x);
        this.newY = scaleY(converted.y);
        //System.out.println("point: " + newX + " " + newY + " cam: " + Main.cam.x + " " + Main.cam.z);
    }

    public int scaleX(double x) {
        x += 1.0;
        x *= (Main.width / 2.0);
        return((int)x);
    }
    public int scaleY(double y) {
        y += 1.0;
        y *= (Main.height / 2.0);
        return((int)y);
    }
    public void applyRotations(double _x, double _y, double _z) {
        double A = 0, B = -1 * Math.toRadians(Main.getRotationX()), C = Math.toRadians(Main.getRotationY());
        this.x = _x*(cos(A)*cos(B) + (-1 * sin(A))*(-1 * sin(B))*(-1 * sin(C))) + _y*cos(C)*(-1 * sin(A)) + _z*(cos(A)*sin(B) + cos(B)*(-1 * sin(A))*(-1 * sin(C)));
        this.y = _x*(sin(A)*cos(B) + cos(A)*(-1 * sin(C))*(-1 * sin(B))) + _y*cos(A)*cos(C) + _z*(sin(A)*sin(B) + cos(A)*(-1 * sin(C))*cos(B));
        this.z = _x*cos(C)*(-1 * sin(B)) + _y*sin(C) + _z*cos(C)*cos(B);
        
        //rotateZAxis(_x, _y, _z, A, B, C);
        //rotateXAxis(_x, _y, _z, A, B, C);
        //rotateYAxis(_x, _y, _z, A, B, C);
    }
    public void applyTransformations() {
        this.x *= (Main.cam.fov * Main.aspectRatio);
        this.y *= (Main.cam.fov);
        //System.out.println(Main.cam.fov +" " + Main.aspectRatio);
    }
    public void rotateXAxis(double _x, double _y, double _z, double A, double B, double C) {
        this.x = _x;
        this.y = (_y * cos(C)) - (_z * sin(C));
        this.z = (_y * sin(C)) + (_z * cos(C));
    }
    public void rotateYAxis(double _x, double _y, double _z, double A, double B, double C) {
        this.x = (_z * sin(B)) + (_x * cos(B));
        this.y = _y;
        this.z = (_z * cos(B)) - (_x * sin(B));
    }
    public void rotateZAxis(double _x, double _y, double _z, double A, double B, double C) {
        this.x = (_x * cos(A)) - (_y * sin(A));
        this.y = (_x * sin(A)) + (_y * cos(A));
        this.z = _z;
    }
    public double sin(double degrees) {
        return(Math.sin(degrees));
    }
    public double cos(double degrees) {
        return(Math.cos(degrees));
    }
}