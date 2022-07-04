public class Triangle {
    mPoint[] vertices = new mPoint[3];
    vec2d[] tex = new vec2d[3];
    double[] vert1, vert2, vert3;

    public Triangle(double[] v1, double[] v2, double[] v3, double[] t1, double[] t2, double[] t3) {
        this.vertices[0] = new mPoint(v1[0], v1[1], v1[2]);
        this.vertices[1] = new mPoint(v2[0], v2[1], v2[2]);
        this.vertices[2] = new mPoint(v3[0], v3[1], v3[2]);

        this.tex[0] = new vec2d(t1[0], t1[1]);
        this.tex[1] = new vec2d(t2[0], t2[1]);
        this.tex[2] = new vec2d(t3[0], t3[1]);

        this.vert1 = v1;
        this.vert2 = v2;
        this.vert3 = v3;
    }
}
