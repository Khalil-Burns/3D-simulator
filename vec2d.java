public class vec2d {
    double u, v, w;
    public vec2d(double _u, double _v, double _w) {
        this.u = _u;
        this.v = _v;
        this.w = _w;
    }
    public vec2d(double _u, double _v) {
        this(_u, _v, 1);
    }
}