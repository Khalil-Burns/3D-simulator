import java.awt.*;
import java.awt.image.BufferedImage;

public class PolygonObject {

    Polygon p;
    double shade;
    double colChange = 30;
    Color[][] cVals;
    int[] xPoints;
    int[] yPoints;
    BufferedImage img[];
    int cnt = 0;

    public PolygonObject(int[] x, int[] y, Color[][] vals, double _shade, BufferedImage imag[]) {
        this.p = new Polygon();
        this.p.xpoints = x;
        this.p.ypoints = y;
        this.p.npoints = x.length;
        this.shade = _shade;
        this.cVals = vals;
        this.img = imag;

        this.xPoints = x;
        this.yPoints = y;
        cnt = 0;
    }

    /*
     * HUGE SHOUTOUT to Javidx9 for his video on textures. This function was referenced from his video: https://www.youtube.com/watch?v=nBzCS-Y0FcY
     */
    public void texturedTriangle(Graphics g, int x1, int y1, double u1, double v1, double w1, int x2, int y2, double u2, double v2, double w2, int x3, int y3, double u3, double v3, double w3) {
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

            dTemp = w1;
            w1 = w2;
            w2 = dTemp;
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

            dTemp = w1;
            w1 = w3;
            w3 = dTemp;
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

            dTemp = w2;
            w2 = w3;
            w3 = dTemp;
        }
        int dy1 = y2 - y1;
        int dx1 = x2 - x1;
        double du1 = u2 - u1;
        double dv1 = v2 - v1;
        double dw1 = w2 - w1;

        int dy2 = y3 - y1;
        int dx2 = x3 - x1;
        double du2 = u3 - u1;
        double dv2 = v3 - v1;
        double dw2 = w3 - w1;

        double daxStep = 0, dbxStep = 0;
        double du1Step = 0, dv1Step = 0;
        double du2Step = 0, dv2Step = 0;
        double dw1Step = 0, dw2Step = 0;

        double texU, texV, texW;

        if (dy1 != 0) {
            daxStep = dx1 / (double)Math.abs(dy1);

            du1Step = du1 / (double)Math.abs(dy1);
            dv1Step = dv1 / (double)Math.abs(dy1);
            dw1Step = dw1 / (double)Math.abs(dy1);
        }
        if (dy2 != 0) {
            dbxStep = dx2 / (double)Math.abs(dy2);

            du2Step = du2 / (double)Math.abs(dy2);
            dv2Step = dv2 / (double)Math.abs(dy2);
            dw2Step = dw2 / (double)Math.abs(dy2);
        }

        if (dy1 != 0) {
            for (int i = y1; i <= y2; i++) {
                int ax = (int)(x1 + (double)(i - y1) * daxStep);
                int bx = (int)(x1 + (double)(i - y1) * dbxStep);

                double texSU = (double)(u1 + (double)(i - y1) * du1Step);
                double texSV = (double)(v1 + (double)(i - y1) * dv1Step);
                double texSW = (double)(w1 + (double)(i - y1) * dw1Step);

                double texEU = (double)(u1 + (double)(i - y1) * du2Step);
                double texEV = (double)(v1 + (double)(i - y1) * dv2Step);
                double texEW = (double)(w1 + (double)(i - y1) * dw2Step);

                if (ax > bx) {
                    temp = ax;
                    ax = bx;
                    bx = temp;

                    dTemp = texSU;
                    texSU = texEU;
                    texEU = dTemp;

                    dTemp = texSV;
                    texSV = texEV;
                    texEV = dTemp;

                    dTemp = texSW;
                    texSW = texEW;
                    texEW = dTemp;
                }
                texU = texSU;
                texV = texSV;
                texW = texSW;

                double tStep = 1.0 / ((double)(bx - ax));
                //System.out.println(tStep * this.img[0].getWidth());
                double t = 0;
                cnt = 0;
                for (int j = ax; j < bx; j++) {
                    if (i * Main.width + j >= 0 && i * Main.width + j < (Main.width * Main.height)) {
                        texU = (1.0 - t) * (double)texSU + t * (double)texEU;
                        texV = (1.0 - t) * (double)texSV + t * (double)texEV;
                        texW = (1.0 - t) * (double)texSW + t * (double)texEW;
                        if (cnt++ == 0 && (i - y1) == 12) {
                            //System.out.println((Math.sqrt((this.img[0].getWidth() * (((texEU - texSU) * (texEU - texSU)) / ((texEW - texSW) * (texEW - texSW)))) + (this.img[0].getHeight() * (((texEV - texSV) * (texEV - texSV)) / ((texEW - texSW) * (texEW - texSW))))) / (ax - bx)));
                            //System.out.println(((tStep * this.img[0].getWidth()) / (double)(bx - ax)));
                            //System.out.println("U: " + texSU + " " + texEU + "V: " + texSV + " " + texEV + "W: " + texSW + " " + texEW);
                            //System.out.println(((texEU * this.img[0].getWidth() - texSU * this.img[0].getWidth())) + " " + ((bx - ax)) + " " + ((((texEU * this.img[0].getWidth() - texSU * this.img[0].getWidth()))) / ((bx - ax))));
                            //System.out.println(((bx - ax) / texEU) + " ");
                            //System.out.println(((texEU * this.img[0].getWidth() - texSU * this.img[0].getWidth())) + " " + ((bx - ax)) + " " + (((texEU * this.img[0].getWidth() - texSU * this.img[0].getWidth())) / ((bx - ax))));
                            //break;
                        }
                        double step = (tStep * this.img[0].getWidth());
                        step = ((((texEV/texEW) * this.img[0].getWidth() - (texSV/texSW) * this.img[0].getWidth())) / ((bx - ax)));
                        step = (Math.sqrt((this.img[0].getWidth() * ((texEU/texEW) - (texSU/texSW))) * (this.img[0].getWidth() * ((texEU/texEW) - (texSU/texSW))) + (this.img[0].getHeight() * ((texEV/texEW) - (texSV/texSW))) * (this.img[0].getHeight() * ((texEV/texEW) - (texSV/texSW))))) / (bx - ax);
                        //step = (Math.sqrt((this.img[0].getWidth() * (((texEU - texSU) * (texEU - texSU)) / ((texEW - texSW) * (texEW - texSW)))) + (this.img[0].getHeight() * (((texEV - texSV) * (texEV - texSV)) / ((texEW - texSW) * (texEW - texSW)))))) / (ax - bx);
                        //step = (Math.sqrt((this.img[0].getWidth() * (((texEU) - (texSU)) / ((texEW) - (texSW)))) * (this.img[0].getWidth() * (((texEU) - (texSU)) / ((texEW) - (texSW)))) + (this.img[0].getHeight() * (((texEV) - (texSV)) / ((texEW) - (texSW)))) * (this.img[0].getHeight() * (((texEV) - (texSV)) / ((texEW) - (texSW)))))) / (bx - ax);
                        //step = (Math.sqrt((this.img[0].getWidth() * ((texEU) - (texSU))) * (this.img[0].getWidth() * ((texEU) - (texSU))) + (this.img[0].getHeight() * ((texEV) - (texSV))) * (this.img[0].getHeight() * ((texEV) - (texSV))) + (((texEW) - (texSW))) * (((texEW) - (texSW))))) / (bx - ax);
                        draw(i, j, texU, texV, texW, step);
                        t += tStep;
                    }
                }
            }
        }
        
        dy1 = y3 - y2;
        dx1 = x3 - x2;
        du1 = u3 - u2;
        dv1 = v3 - v2;
        dw1 = w3 - w2;

        if (dy1 != 0) {
            daxStep = dx1 / (double)Math.abs(dy1);
        }
        if (dy2 != 0) {
            dbxStep = dx2 / (double)Math.abs(dy2);
        }
        du1Step = 0;
        dv1Step = 0;
        dw1Step = 0;
        if (dy1 != 0) {
            du1Step = du1 / (double)Math.abs(dy1);
        }
        if (dy1 != 0) {
            dv1Step = dv1 / (double)Math.abs(dy1);
        }
        if (dy1 != 0) {
            dw1Step = dw1 / (double)Math.abs(dy1);
        }

        for (int i = y2; i <= y3; i++) {
            int ax = (int)(x2 + (double)(i - y2) * daxStep);
            int bx = (int)(x1 + (double)(i - y1) * dbxStep);

            double texSU = (double)(u2 + (double)(i - y2) * du1Step);
            double texSV = (double)(v2 + (double)(i - y2) * dv1Step);
            double texSW = (double)(w2 + (double)(i - y2) * dw1Step);

            double texEU = (double)(u1 + (double)(i - y1) * du2Step);
            double texEV = (double)(v1 + (double)(i - y1) * dv2Step);
            double texEW = (double)(w1 + (double)(i - y1) * dw2Step);

            if (ax > bx) {
                temp = ax;
                ax = bx;
                bx = temp;

                dTemp = texSU;
                texSU = texEU;
                texEU = dTemp;

                dTemp = texSV;
                texSV = texEV;
                texEV = dTemp;

                dTemp = texSW;
                texSW = texEW;
                texEW = dTemp;
            }
            texU = texSU;
            texV = texSV;
            texW = texSW;

            double tStep = 1.0 / (double)(bx - ax);
            double t = 0;

            for (int j = ax; j < bx; j++) {
                if (i * Main.width + j >= 0 && i * Main.width + j < (Main.width * Main.height)) {
                    texU = (1.0 - t) * texSU + t * texEU;
                    texV = (1.0 - t) * texSV + t * texEV;
                    texW = (1.0 - t) * texSW + t * texEW;
                    double step = (tStep * this.img[0].getWidth());
                    step = ((((texEV/texEW) * this.img[0].getWidth() - (texSV/texSW) * this.img[0].getWidth())) / ((bx - ax)));
                    step = (Math.sqrt((this.img[0].getWidth() * ((texEU/texEW) - (texSU/texSW))) * (this.img[0].getWidth() * ((texEU/texEW) - (texSU/texSW))) + (this.img[0].getHeight() * ((texEV/texEW) - (texSV/texSW))) * (this.img[0].getHeight() * ((texEV/texEW) - (texSV/texSW))))) / (bx - ax);
                    //step = (Math.sqrt((this.img[0].getWidth() * (((texEU - texSU) * (texEU - texSU)) / ((texEW - texSW) * (texEW - texSW)))) + (this.img[0].getHeight() * (((texEV - texSV) * (texEV - texSV)) / ((texEW - texSW) * (texEW - texSW)))))) / (ax - bx);
                    //step = (Math.sqrt((this.img[0].getWidth() * (((texEU) - (texSU)) / ((texEW) - (texSW)))) * (this.img[0].getWidth() * (((texEU) - (texSU)) / ((texEW) - (texSW)))) + (this.img[0].getHeight() * (((texEV) - (texSV)) / ((texEW) - (texSW)))) * (this.img[0].getHeight() * (((texEV) - (texSV)) / ((texEW) - (texSW)))))) / (bx - ax);
                    //step = (Math.sqrt((this.img[0].getWidth() * ((texEU) - (texSU))) * (this.img[0].getWidth() * ((texEU) - (texSU))) + (this.img[0].getHeight() * ((texEV) - (texSV))) * (this.img[0].getHeight() * ((texEV) - (texSV))) + (((texEW) - (texSW))) * (((texEW) - (texSW))))) / (bx - ax);
                    draw(i, j, texU, texV, texW, step);
                    t += tStep;
                }
            }
        }
    }

    public void draw(int i, int j, double u, double v, double w, double step) {
        //double expI = (Math.log(step) / Math.log(2));
        double expI = step;
        int tempI = (int)expI;
        double cUsed = 1.0 - (double)(expI - (double)tempI);
        //int tempI = (int)(10 * (Math.log(w) + 3));
        if (tempI >= this.img.length) {
            tempI = this.img.length - 2;
            expI = tempI + 1;
        }
        else if (tempI >= this.img.length - 1) {
            tempI = this.img.length - 2;
            expI = tempI + 1;
        }
        if (tempI < 0) {
            tempI = 0;
            expI = 1;
        }
        //int width = this.img[tempI].getWidth(), height = this.img[tempI].getHeight();
        //int width = this.img[tempI < 3? 0:(tempI - 3)].getWidth(), height = this.img[tempI < 3? 0:(tempI - 3)].getHeight();
        int width = this.img[0].getWidth(), height = this.img[0].getHeight();
        int idxU = (int)((u/w) * width), idxV = (int)((v/w) * height);
        if (idxU >= 0 && idxU < width && idxV >= 0 && idxV < height) {
            if (Main.depthBuffer[i * Main.width + j] < w) {
                //System.out.println((idxV * this.img.getWidth() + idxU) + " " + this.cVals[idxV * this.img.getWidth() + idxU]);
                //Color c = this.cVals[idxV * this.img.getWidth() + idxU];
                //Color c = Main.disVals[tempI];

                Color c = this.cVals[0][idxV * width + idxU];
                int newRed = c.getRed(), newGreen = c.getGreen(), newBlue = c.getBlue();

                int power = 1;
                for (int k = 1; k < this.cVals.length; k++) {
                    power *= 2;
                    c = this.cVals[k][(idxV/power) * (width/power) + (idxU/power)];
                    newRed += c.getRed(); newGreen += c.getGreen(); newBlue += c.getBlue();
                }

                newRed /= this.cVals.length; newGreen /= this.cVals.length; newBlue /= this.cVals.length;

                // //cUsed = 1.0;
                // //Color c = this.cVals[tempI][idxV * width + idxU];
                // Color c = Main.disVals[tempI];
                // int newRed = (int)(c.getRed() * cUsed), newGreen = (int)(c.getGreen() * cUsed), newBlue = (int)(c.getBlue() * cUsed);
                // //c = this.cVals[tempI + 1][(idxV/2) * (width/2) + (idxU/2)];
                // //c = Main.disVals[tempI];
                // c = Main.disVals[tempI + 1];
                // cUsed = 1.0 - cUsed;
                // //cUsed = 1.0;
                // newRed += (int)(c.getRed() * cUsed); newGreen += (int)(c.getGreen() * cUsed); newBlue += (int)(c.getBlue() * cUsed);
                // //newRed /= 2; newGreen /= 2; newBlue /= 2;

                // Color c = this.cVals[tempI < 3? 0:(tempI - 3)][idxV * width + idxU];
                // int newRed = c.getRed(), newGreen = c.getGreen(), newBlue = c.getBlue();
                // int power = 1;
                // for (int k = 1; k < tempI + 1; k++) {
                //     power *= 2;
                //     c = this.cVals[k][(idxV/power) * (width/power) + (idxU/power)];
                //     newRed += c.getRed(); newGreen += c.getGreen(); newBlue += c.getBlue();
                // }
                // newRed /= tempI + 1; newGreen /= tempI + 1; newBlue /= tempI + 1;

                newRed += this.shade * this.colChange;
                newGreen += this.shade * this.colChange;
                newBlue += this.shade * this.colChange;

                if (newRed > 255) {
                    newRed = 255;
                }
                if (newRed < 0) {
                    newRed = 0;
                }

                if (newGreen > 255) {
                    newGreen = 255;
                }
                if (newGreen < 0) {
                    newGreen = 0;
                }

                if (newBlue > 255) {
                    newBlue = 255;
                }
                if (newBlue < 0) {
                    newBlue = 0;
                }
                c = new Color(newRed, newGreen, newBlue, c.getAlpha());
                Main.pixels[i * Main.width + j] = c.getRGB();
                Main.depthBuffer[i * Main.width + j] = w;
            }
        }
        else {
            Color c = this.cVals[9][0];
            Main.pixels[i * Main.width + j] = c.getRGB();
        }
    }

    public void drawPolygon(Graphics g, boolean crossHair/*, double[] x, double[] y, double[] z*/) {
        //Graphics2D g2 = (Graphics2D)g;
        //g2.setStroke(new BasicStroke(3));
        g.setColor(this.cVals[9][0]);
        g.drawPolygon(this.p);
        g.fillPolygon(this.p);
    }
    public static double round(double x) {
        return(Math.round(x * 1000)/1000.0);
    }
}
