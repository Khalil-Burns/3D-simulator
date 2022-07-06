public class Triangle {
    mPoint[] vertices = new mPoint[3];
    vec2d[] tex = new vec2d[3];
    double[] vert1, vert2, vert3, te1, te2, te3;

    public Triangle(double[] v1, double[] v2, double[] v3, double[] t1, double[] t2, double[] t3) {
        this.vertices[0] = new mPoint(v1[0], v1[1], v1[2]);
        this.vertices[1] = new mPoint(v2[0], v2[1], v2[2]);
        this.vertices[2] = new mPoint(v3[0], v3[1], v3[2]);

        if (t1.length == 3) {
            this.tex[0] = new vec2d(t1[0], t1[1], t1[2]);
            this.tex[1] = new vec2d(t2[0], t2[1], t2[2]);
            this.tex[2] = new vec2d(t3[0], t3[1], t3[2]);
        }
        else { 
            this.tex[0] = new vec2d(t1[0], t1[1]);
            this.tex[1] = new vec2d(t2[0], t2[1]);
            this.tex[2] = new vec2d(t3[0], t3[1]);
        }

        this.vert1 = v1;
        this.vert2 = v2;
        this.vert3 = v3;

        this.te1 = t1;
        this.te2 = t2;
        this.te3 = t3;
    }
    public Triangle(Triangle tri) {
        this(tri.vert1, tri.vert2, tri.vert3, tri.te1, tri.te2, tri.te3);
    }
    public Triangle(double[] v1, double[] v2, double[] v3, vec2d[] texs) {
        this(v1, v2, v3, new double[]{texs[0].u, texs[0].v, texs[0].w}, new double[]{texs[1].u, texs[1].v, texs[1].w}, new double[]{texs[2].u, texs[2].v, texs[2].w});
    }
}