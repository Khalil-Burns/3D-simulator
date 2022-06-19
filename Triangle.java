public class Triangle {
    mPoint[] vertices = new mPoint[3];

    public Triangle(double[] _x, double[] _y, double[] _z) {
        for (int i = 0; i < 3; i++) {
            this.vertices[i].x = _x[i];
            this.vertices[i].y = _y[i];
            this.vertices[i].z = _z[i];
        }
    }
}
