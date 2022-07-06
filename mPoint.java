public class mPoint {
    double x;
    double y;
    double z;
    double w;

    public mPoint(double _x, double _y, double _z, double _w) {
        this.x = _x;
        this.y = _y;
        this.z = _z;
        this.w = _w;
    }
    public mPoint(double _x, double _y, double _z) {
        this(_x, _y, _z, 1);
    }
    public mPoint() {
        this(-1,-1, -1, -1);
    }
}