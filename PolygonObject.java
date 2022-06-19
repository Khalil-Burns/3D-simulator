import java.awt.*;

public class PolygonObject {

    Polygon p;
    Color c;

    public PolygonObject(int[] x, int[] y, Color _c) {
        this.p = new Polygon();
        this.p.xpoints = x;
        this.p.ypoints = y;
        this.p.npoints = x.length;
        this.c = _c;
    }

    public void drawPolygon(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawPolygon(this.p);
        g.setColor(this.c);
        g.fillPolygon(this.p);
    }
}
