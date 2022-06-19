import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class Main extends JPanel implements KeyListener {

    static boolean doneCreate = false;

    double moveX = 0, moveY = 0, moveZ = 0;
    double speedX = 0.04, speedY = 0.04, speedZ = 0.04;
    static HashMap<String, Boolean> pressed = new HashMap<String, Boolean>();

    static int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    static double aspectRatio = (width*1.0)/height;
    static double rotationX = 0, rotationY = 0;

    static double fNear = 0.1;
    static double fFar = 1000.0;
    static double fFov = 90.0;
    static double fAspectRatio = 1.0 / aspectRatio;
    static double fFovRad = 1.0 / Math.tan(Math.toRadians(fFov * 0.5));

    static Camera cam = new Camera(0, -2, 0, 120, aspectRatio);

    static ArrayList<ThreeDPolygon> polygons = new ArrayList<ThreeDPolygon>();

    static int[] lineWidth = new int[1];

    static Mesh meshCube = new Mesh();

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        super.paintComponent(g2); 
        /*g2.setStroke(new BasicStroke(2));
        Polygon p = new Polygon();
        p.xpoints = new int[] {width/2, width/2, width, width*2};
        p.ypoints = new int[] {height/2, height*2, height*2, height/2};
        p.npoints = 4;
        g2.setColor(Color.gray);
        g2.drawPolygon(p);*/
        if (doneCreate){
            sortPolygons(0, polygons.size() - 1);
            for (int i = polygons.size() - 1; i >= 0; i--) {
                polygons.get(i).updatePolygon();
                polygons.get(i).newPolygon.drawPolygon(g);
            }
            /*for (int i = 0; i < meshCube.triangles.size(); i++) {

            }*/
        }
        move();
        repaint();
    }

    public void move() {
        double rotX = -1 * Math.toRadians(rotationX);
        cam.moveX(moveX*(speedX * Math.cos(rotX)) + moveZ*(-speedZ * Math.sin(rotX)));
        cam.moveY(moveY*(-speedY));
        cam.moveZ(moveX*(speedX * Math.sin(rotX)) + moveZ*(speedZ * Math.cos(rotX)));
    }

    public static double getRotationX() {
        Point info = MouseInfo.getPointerInfo().getLocation();
        rotationX += ((int)info.getX() - width/2)/2;
        try {
            (new Robot()).mouseMove((int)(width/2), (int)(info.getY()));
        }
        catch (AWTException e) {}
        return(rotationX);
    }
    public static double getRotationY() {
        Point info = MouseInfo.getPointerInfo().getLocation();
        rotationY += ((int)info.getY() - height/2)/2;
        if (rotationY < -90) {
            rotationY = -90;
        }
        if (rotationY > 90) {
            rotationY = 90;
        }
        try {
            (new Robot()).mouseMove((int)(info.getX()), (int)(height/2));
        }
        catch (AWTException e) {}
        return(rotationY);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D && !pressed.get("d")) {
            moveX += 1;
            pressed.put("d",true);
        }
        if (e.getKeyCode() == KeyEvent.VK_A && !pressed.get("a")) {
            moveX += -1;
            pressed.put("a",true);
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE && !pressed.get("space")) {
            moveY += 1;
            pressed.put("space",true);
        }
        if (e.getKeyCode() == KeyEvent.VK_SHIFT && !pressed.get("shift")) {
            moveY += -1;
            pressed.put("shift",true);
        }
        
        if (e.getKeyCode() == KeyEvent.VK_W && !pressed.get("w")) {
            moveZ += 1;
            pressed.put("w",true);
        }
        if (e.getKeyCode() == KeyEvent.VK_S && !pressed.get("s")) {
            moveZ += -1;
            pressed.put("s",true);
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D) {
            moveX -= 1;
            pressed.put("d",false);
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            moveX += 1;
            pressed.put("a",false);
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            moveY -= 1;
            pressed.put("space",false);
        }
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            moveY += 1;
            pressed.put("shift",false);
        }
        
        if (e.getKeyCode() == KeyEvent.VK_W) {
            moveZ += -1;
            pressed.put("w",false);
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            moveZ += 1;
            pressed.put("s",false);
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    //Quicksort
    public void sortPolygons(int low, int high) {
        if (low < high) {
            int part = partition(low, high);
            sortPolygons(low, part - 1);
            sortPolygons(part + 1, high);
        }
    }
    public int partition(int low, int high) {
        double pivot = polygons.get(high).avgDist();
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (polygons.get(j).avgDist() < pivot) {
                i++;
                ThreeDPolygon temp = polygons.get(i);
                polygons.set(i, polygons.get(j));
                polygons.set(j, temp);
            }
        }
        ThreeDPolygon temp = polygons.get(i + 1);
        polygons.set(i + 1, polygons.get(high));
        polygons.set(high, temp);
        return(i + 1);
    }

    public static void main(String[] args) {
        Main _main = new Main();
        JFrame frame = new JFrame("3D Simulation");
        try {
            (new Robot()).mouseMove((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
        }
        catch (AWTException e) {}
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setUndecorated(true);
        frame.setLayout(null);
        frame.setContentPane(_main);
        frame.setVisible(true);
        frame.addKeyListener(_main);
        // Transparent 16 x 16 pixel cursor image.
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
        frame.getContentPane().setCursor(blankCursor);

        pressed.put("d", false);
        pressed.put("a", false);
        pressed.put("space", false);
        pressed.put("shift", false);
        pressed.put("w", false);
        pressed.put("s", false);

        int startX = 0;
        int startY = 0;
        int startZ = 0;
        int width = 1;
        int height = 1;
        int depth = 1;

        double[] _x = {startX,  (startX + width),  (startX + width), startX, startX, (startX + width), (startX + width), startX, startX,  (startX + width), (startX + width), startX, startX,  (startX + width), (startX + width), startX, startX, startX, startX, startX,  (startX + width),  (startX + width), (startX + width), (startX + width)};
        double[] _y = {startY, startY, startY, startY,  (startY + height), (startY + height), (startY + height),  (startY + height), startY, startY, (startY + height),  (startY + height), startY, startY, (startY + height),  (startY + height), startY, startY,  (startY + height),  (startY + height), startY, startY, (startY + height), (startY + height)};
        double[] _z = { startZ,  startZ,  (startZ + depth),  (startZ + depth),  startZ, startZ, (startZ + depth),  (startZ + depth),  startZ,  startZ, startZ,  startZ,  (startZ + depth),  (startZ + depth), (startZ + depth),  (startZ + depth),  (startZ + depth),  startZ,  startZ,  (startZ + depth),  (startZ + depth),  startZ, startZ, (startZ + depth)};

        for (int i = 0; i < _x.length; i += 4) {
            double[] x1 = new double[4];
            double[] y1 = new double[4];
            double[] z1 = new double[4];
            for (int j = 0; j < 4; j++) {
                x1[j] = _x[j + i];
                y1[j] = _y[j + i];
                z1[j] = _z[j + i];
            }
            polygons.add(new ThreeDPolygon(x1, y1, z1, new Color(160 + 10*(i/4), 130, 255 - 20*(i/4), 255)));
        }
        //polygons.add(new ThreeDPolygon(new double[] {-5, 4, 4, -5}, new double[] {0, 0, 0, 0}, new double[] {1, 1, 10, 10}, new Color(160, 255, 160, 255)));
        

        /*meshCube.triangles = new ArrayList<>();

        meshCube.triangles.add(new ThreeDPolygon(new Triangle(new double[]{0, 0, 0}, new double[]{0, 1, 0}, new double[]{1, 1, 0}), Color.BLACK));
        meshCube.triangles.add(new ThreeDPolygon(new Triangle(new double[]{0, 0, 0}, new double[]{1, 1, 0}, new double[]{1, 0, 0}), Color.BLACK));
        meshCube.triangles.add(new ThreeDPolygon(new Triangle(new double[]{1, 0, 0}, new double[]{1, 1, 0}, new double[]{1, 1, 1}), Color.BLACK));
        meshCube.triangles.add(new ThreeDPolygon(new Triangle(new double[]{1, 0, 0}, new double[]{1, 1, 1}, new double[]{1, 0, 1}), Color.BLACK));
        meshCube.triangles.add(new ThreeDPolygon(new Triangle(new double[]{1, 0, 0}, new double[]{1, 1, 1}, new double[]{0, 1, 1}), Color.BLACK));
        meshCube.triangles.add(new ThreeDPolygon(new Triangle(new double[]{1, 0, 1}, new double[]{0, 1, 1}, new double[]{0, 0, 1}), Color.BLACK));
        meshCube.triangles.add(new ThreeDPolygon(new Triangle(new double[]{0, 0, 1}, new double[]{0, 1, 1}, new double[]{0, 1, 0}), Color.BLACK));
        meshCube.triangles.add(new ThreeDPolygon(new Triangle(new double[]{0, 0, 1}, new double[]{0, 1, 0}, new double[]{0, 0, 0}), Color.BLACK));
        meshCube.triangles.add(new ThreeDPolygon(new Triangle(new double[]{0, 1, 0}, new double[]{0, 1, 1}, new double[]{1, 1, 1}), Color.BLACK));
        meshCube.triangles.add(new ThreeDPolygon(new Triangle(new double[]{0, 1, 0}, new double[]{1, 1, 1}, new double[]{1, 1, 0}), Color.BLACK));
        meshCube.triangles.add(new ThreeDPolygon(new Triangle(new double[]{1, 0, 1}, new double[]{0, 1, 1}, new double[]{1, 1, 1}), Color.BLACK));
        meshCube.triangles.add(new ThreeDPolygon(new Triangle(new double[]{1, 0, 1}, new double[]{0, 0, 0}, new double[]{1, 0, 0}), Color.BLACK));
        */

        doneCreate = true;
    }
}