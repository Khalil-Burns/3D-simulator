import java.awt.*;

public class PolygonObject {

    Polygon p;
    Color c;
    int[] xPoints;
    int[] yPoints;

    public PolygonObject(int[] x, int[] y, Color _c) {
        this.p = new Polygon();
        this.p.xpoints = x;
        this.p.ypoints = y;
        this.p.npoints = x.length;
        this.c = _c;

        this.xPoints = x;
        this.yPoints = y;
    }

    public void drawPolygon(Graphics g) {
        //g.setColor(Color.BLACK);
        g.setColor(this.c);
        g.drawPolygon(this.p);
        //g.setColor(this.c);
        //g.fillPolygon(this.p);

        // for (int i = 0; i < this.xPoints.length; i++) {
        //     if (i == this.xPoints.length - 1) {
        //         g.drawLine(xPoints[i], yPoints[i], xPoints[0], yPoints[0]);
        //     }
        //     else {
        //         g.drawLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
        //     }
        // }
    }
}
