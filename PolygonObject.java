import java.awt.*;
import java.awt.image.BufferedImage;

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

    public void texturedTriangle(Graphics g, BufferedImage img, int x1, int y1, double u1, double v1, int x2, int y2, double u2, double v2, int x3, int y3, double u3, double v3) {
        int temp;
        double dTemp;
        if (y2 < y1) {
            temp = y1;
            y1 = y2;
            y2 = temp;

            temp = x1;
            x1 = x2;
            x2 = temp;

            dTemp = u1;
            u1 = u2;
            u2 = dTemp;

            dTemp = v1;
            v1 = v2;
            v2 = dTemp;
        }
        if (y3 < y1) {
            temp = y1;
            y1 = y3;
            y3 = temp;

            temp = x1;
            x1 = x3;
            x3 = temp;

            dTemp = u1;
            u1 = u3;
            u3 = dTemp;

            dTemp = v1;
            v1 = v3;
            v3 = dTemp;
        }
        if (y3 < y2) {
            temp = y2;
            y2 = y3;
            y3 = temp;

            temp = x2;
            x2 = x3;
            x3 = temp;

            dTemp = u2;
            u2 = u3;
            u3 = dTemp;

            dTemp = v2;
            v2 = v3;
            v3 = dTemp;
        }

        int dy1 = y2 - y1;
        int dx1 = x2 - x1;
        double du1 = u2 - u1;
        double dv1 = v2 - v1;

        int dy2 = y3 - y1;
        int dx2 = x3 - x1;
        double du2 = u3 - u1;
        double dv2 = v3 - v1;

        double daxStep = 0, dbxStep = 0;
        double du1Step = 0, dv1Step = 0;
        double du2Step = 0, dv2Step = 0;

        double texU, texV;

        if (dy1 != 0) {
            daxStep = dx1 / (double)Math.abs(dy1);
        }
        if (dy2 != 0) {
            dbxStep = dx2 / (double)Math.abs(dy2);
        }
        if (dy1 != 0) {
            du1Step = du1 / (double)Math.abs(dy1);
        }
        if (dy1 != 0) {
            dv1Step = dv1 / (double)Math.abs(dy1);
        }
        if (dy2 != 0) {
            du2Step = du2 / (double)Math.abs(dy2);
        }
        if (dy2 != 0) {
            dv2Step = dv2 / (double)Math.abs(dy2);
        }

        if (dy1 != 0) {
            for (int i = y1; i <= y2; i++) {
                int ax = (int)(x1 + (double)(i - y1) * daxStep);
                int bx = (int)(x1 + (double)(i - y1) * dbxStep);

                double texSU = (double)(u1 + (double)(i - y1) * du1Step);
                double texSV = (double)(v1 + (double)(i - y1) * dv1Step);

                double texEU = (double)(u1 + (double)(i - y1) * du2Step);
                double texEV = (double)(v1 + (double)(i - y1) * dv2Step);

                if (ax > bx) {
                    temp = ax;
                    ax = bx;
                    bx = temp;

                    dTemp = texSU;
                    texSU = texEU;
                    texEU = temp;

                    dTemp = texSV;
                    texSV = texEV;
                    texEV = temp;
                }
                texU = texSU;
                texV = texSV;

                double tStep = 1.0 / (double)(bx - ax);
                double t = 0;

                for (int j = ax; j < bx; j++) {
                    texU = (1.0 - t) * texSU + t * texEU;
                    texV = (1.0 - t) * texSV + t * texEV;
                    t += tStep;
                    g.setColor(new Color(img.getRGB((int)texU, (int)texV)));
                    g.drawLine(j, i, j, i);
                    //g.fillRect(j, i, 2, 2);
                }
            }
        }
        
        dy1 = y3 - y2;
        dx1 = x3 - x2;
        du1 = u3 - u2;
        dv1 = v3 - v2;

        if (dy1 != 0) {
            daxStep = dx1 / (double)Math.abs(dy1);
        }
        if (dy2 != 0) {
            dbxStep = dx2 / (double)Math.abs(dy2);
        }
        du1Step = 0;
        dv1Step = 0;
        if (dy1 != 0) {
            du1Step = du1 / (double)Math.abs(dy1);
        }
        if (dy1 != 0) {
            dv1Step = dv1 / (double)Math.abs(dy1);
        }

        for (int i = y2; i <= y3; i++) {
            int ax = (int)(x2 + (double)(i - y2) * daxStep);
            int bx = (int)(x1 + (double)(i - y1) * dbxStep);

            double texSU = (double)(u2 + (double)(i - y2) * du1Step);
            double texSV = (double)(v2 + (double)(i - y2) * dv1Step);

            double texEU = (double)(u1 + (double)(i - y1) * du2Step);
            double texEV = (double)(v1 + (double)(i - y1) * dv2Step);

            if (ax > bx) {
                temp = ax;
                ax = bx;
                bx = temp;

                dTemp = texSU;
                texSU = texEU;
                texEU = temp;

                dTemp = texSV;
                texSV = texEV;
                texEV = temp;
            }
            texU = texSU;
            texV = texSV;

            double tStep = 1.0 / (double)(bx - ax);
            double t = 0;

            for (int j = ax; j < bx; j++) {
                texU = (1.0 - t) * texSU + t * texEU;
                texV = (1.0 - t) * texSV + t * texEV;
                t += tStep;
                g.setColor(new Color(img.getRGB((int)texU, (int)texV)));
                g.drawLine(j, i, j, i);
                //g.fillRect(j, i, 2, 2);
            }
        }
    }

    public void drawPolygon(Graphics g, boolean crossHair/*, double[] x, double[] y, double[] z*/) {
        if (crossHair) {
            g.setColor(Color.BLACK);
            g.drawPolygon(this.p);

            int newRed = this.c.getRed(), newGreen = this.c.getGreen(), newBlue = this.c.getBlue();
            newRed += 100;
            newGreen += 100;
            newBlue += 100;

            if (newRed > 255) {
                newRed = 255;
            }

            if (newGreen > 255) {
                newGreen = 255;
            }

            if (newBlue > 255) {
                newBlue = 255;
            }

            g.setColor(new Color(newRed, newGreen, newBlue, this.c.getAlpha()));
        }
        else {
            g.setColor(this.c);
        }

        g.fillPolygon(this.p);

        /*g.setColor(Color.BLACK);
        for (int i = 0; i < 3; i++) {
            g.drawString("("+round(x[i])+", "+round(y[i]) + ", "+ round(z[i])+")", this.xPoints[i], this.yPoints[i]);
        }*/

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
