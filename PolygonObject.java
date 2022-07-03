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

    public void drawPolygon(Graphics g, double[] x, double[] y, double[] z) {
        g.setColor(Color.BLACK);
        g.drawPolygon(this.p);
        g.setColor(this.c);
        g.fillPolygon(this.p);
        g.setColor(Color.BLACK);
        for (int i = 0; i < 3; i++) {
            g.drawString("("+round(x[i])+", "+round(y[i]) + ", "+ round(z[i])+")", this.xPoints[i], this.yPoints[i]);
        }

        // for (int i = 0; i < this.xPoints.length; i++) {
        //     if (i == this.xPoints.length - 1) {
        //         g.drawLine(xPoints[i], yPoints[i], xPoints[0], yPoints[0]);
        //     }
        //     else {
        //         g.drawLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
        //     }
        // }
    }
    public static double round(double x) {
        return(Math.round(x * 1000)/1000.0);
    }
}
