public class Triangle {
    mPoint[] vertices = new mPoint[3];

    public Triangle(double[] v1, double[] v2, double[] v3) {
        this.vertices[0] = new mPoint(v1[0], v1[1], v1[2]);
        this.vertices[1] = new mPoint(v2[0], v2[1], v2[2]);
        this.vertices[2] = new mPoint(v3[0], v3[1], v3[2]);
    }
}
