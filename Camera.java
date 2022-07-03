public class Camera {

    double x, y, z, aspectRatio;
    double fov;

    public Camera(double _x, double _y, double _z, double _fov, double ar) {
        this.x = _x;
        this.y = _y;
        this.z = _z;
        this.aspectRatio = ar;

        changeFov(fov);
    }

    public void moveX(double _x) {
        this.x += _x;
        //System.out.println(this.x);
    }
    public void moveY(double _y) {
        this.y -= _y;
        //System.out.println(this.y);
    }
    public void moveZ(double _z) {
        //if (this.z >= 0) {
            this.z += _z;
            //System.out.println(this.z);
        /*}
        else {
            this.z = 0;
        }*/
    }
    public void changeFov(double angle) {
        this.fov = 2*arctan(tan(angle/2.0)*aspectRatio);
    }

    public double tan(double input) {
        return(Math.tan(Math.toRadians(input)));
    }
    public double arctan(double input) {
        return(Math.atan(input));
    }
}