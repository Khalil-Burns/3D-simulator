import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.imageio.ImageIO;
import java.io.*;

import java.util.*;

public class Main extends JPanel implements KeyListener {

    static boolean doneCreate = false;

    double moveX = 0, moveY = 0, moveZ = 0;
    double speedX = 0.5, speedY = 0.5, speedZ = 0.5;
    static HashMap<String, Boolean> pressed = new HashMap<String, Boolean>();

    static int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    static double aspectRatio = (width*1.0)/height;
    static double rotationX = -180, rotationY = -10;

    static double fNear = 0.1;
    static double fFar = 1000.0;
    static double fFov = 120.0;
    static double fAspectRatio = 1.0 / aspectRatio;
    static double fFovRad = 1.0 / Math.tan(Math.toRadians(fFov * 0.5));

    static Camera cam = new Camera(-3, 13, 17, fFov, fAspectRatio);

    static ArrayList<ThreeDPolygon> polygons = new ArrayList<ThreeDPolygon>();

    static int[] lineWidth = new int[1];

    static Mesh mesh = new Mesh();

    static mPoint lightSource = new mPoint(0, 1, 2);

    static JFrame frame;
    static long last;
    static int crossHairLength = 7;

    static Point lastX, lastY;
    static double rotX = 0, rotY = 0, rotZ = 0;
    static int cX = 0, cY = 0;

    static BufferedImage canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    static int[] pixels = ((DataBufferInt)canvas.getRaster().getDataBuffer()).getData();
    
    static double[] depthBuffer = new double[width * height];

    static double speed = 0.5;

    static Color[][] vals;
    static Color[] disVals = {Color.BLACK, Color.GRAY, Color.PINK, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED};
    static BufferedImage[] image;

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
            //sortPolygons(0, polygons.size() - 1);
            for (int i = polygons.size() - 1; i >= 0; i--) {
                polygons.get(i).updatePolygon(g);
            }
            // g.drawImage(image[0], 0, 0, image[0].getWidth(), image[0].getHeight(), null);
            // g.drawImage(image[1], 0, 0, image[1].getWidth(), image[1].getHeight(), null);
            // g.drawImage(image[2], 0, 0, image[2].getWidth(), image[2].getHeight(), null);
            // g.drawImage(image[3], 0, 0, image[3].getWidth(), image[3].getHeight(), null);
            // g.drawImage(image[4], 0, 0, image[4].getWidth(), image[4].getHeight(), null);
        }

        /*BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}*/
		g.drawImage(canvas, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
        for (int i = 0; i < width * height; i++) {
            pixels[i] = Color.WHITE.getRGB();
            depthBuffer[i] = 0;
        }

        g.setColor(Color.BLACK);
        g.drawString("X: " + Math.round(cam.x*1000)/1000.0, 10, 20);
        g.drawString("Y: " + Math.round(cam.y*1000)/1000.0, 10, 30);
        g.drawString("Z: " + Math.round(cam.z*1000)/1000.0, 10, 40);
        g.drawString("FOV: " + fFov + " degrees", 10, 60);
        g.drawString("Facing: " + rotationX + " " + rotationY, 10, 80);

        g2.setStroke(new BasicStroke(2));
        g.setColor(Color.gray);
        g.drawLine((int)(width / 2.0 - crossHairLength), (int)(height / 2.0), (int)(width / 2.0 + crossHairLength), (int)(height / 2.0));
        g.drawLine((int)(width / 2.0), (int)(height / 2.0 - crossHairLength), (int)(width / 2.0), (int)(height / 2.0 + crossHairLength));
        move();
        if (System.nanoTime() - last > 10000000) {
            frame.setTitle("3D Simulation, " + (1000000000 / (System.nanoTime() - last)) + "fps");
            last = System.nanoTime();
        }

        Point info = MouseInfo.getPointerInfo().getLocation();
        rotationX -= ((int)info.getX() - width/2)/2;
        //rotationX = (int)lastX.getX() - (int)info.getX();
        //lastX = MouseInfo.getPointerInfo().getLocation();
        if (rotationX > 360.0) {
            rotationX -= 360.0;
        }
        if (rotationX < -360.0) {
            rotationX += 360.0;
        }

        rotationY -= ((int)info.getY() - height/2)/2;
        //rotationY = (int)lastY.getY() - (int)info.getY();
        //lastY = MouseInfo.getPointerInfo().getLocation();
        if (rotationY < -90) {
            rotationY = -90;
        }
        if (rotationY > 90) {
            rotationY = 90;
        }
        try {
            (new Robot()).mouseMove((int)(width/2), (int)(height/2));
        }
        catch (AWTException e) {}

        rotX = 0;
        rotY = 0;
        rotZ = 0;
        fFovRad = 1.0 / Math.tan(Math.toRadians(fFov * 0.5));
        repaint();
    }

    public void move() {
        double rotX = -1 * Math.toRadians(rotationX);
        cam.moveX(moveX*(-speedX * Math.cos(rotX)) + moveZ*(-speedZ * Math.sin(rotX)));
        cam.moveY(moveY*(-speedY));
        cam.moveZ(moveX*(-speedX * Math.sin(rotX)) + moveZ*(speedZ * Math.cos(rotX)));
    }

    public static double getRotationX() {
        /*Point info = MouseInfo.getPointerInfo().getLocation();
        //rotationX += ((int)info.getX() - width/2)/2;
        rotationX = (int)lastX.getX() - (int)info.getX();
        lastX = MouseInfo.getPointerInfo().getLocation();
        /*try {
            (new Robot()).mouseMove((int)(width/2), (int)(info.getY()));
        }
        catch (AWTException e) {}*/
        return(rotationX);
    }
    public static double getRotationY() {
        /*Point info = MouseInfo.getPointerInfo().getLocation();
        //rotationY -= ((int)info.getY() - height/2)/2;
        rotationY = (int)lastY.getY() - (int)info.getY();
        lastY = MouseInfo.getPointerInfo().getLocation();
        if (rotationY < -90) {
            rotationY = -90;
        }
        if (rotationY > 90) {
            rotationY = 90;
        }
        /*try {
            (new Robot()).mouseMove((int)(info.getX()), (int)(height/2));
        }
        catch (AWTException e) {}*/
        return(rotationY);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F) {
            fFov += 5;
        }
        if (e.getKeyCode() == KeyEvent.VK_G) {
            fFov -= 5;
        }
        if (e.getKeyCode() == KeyEvent.VK_D && !pressed.get("d")) {
            moveX += speed;
            pressed.put("d",true);
        }
        if (e.getKeyCode() == KeyEvent.VK_A && !pressed.get("a")) {
            moveX -= speed;
            pressed.put("a",true);
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE && !pressed.get("space")) {
            moveY += speed;
            pressed.put("space",true);
        }
        if (e.getKeyCode() == KeyEvent.VK_SHIFT && !pressed.get("shift")) {
            moveY -= speed;
            pressed.put("shift",true);
        }
        
        if (e.getKeyCode() == KeyEvent.VK_W && !pressed.get("w")) {
            moveZ += speed;
            pressed.put("w",true);
        }
        if (e.getKeyCode() == KeyEvent.VK_S && !pressed.get("s")) {
            moveZ -= speed;
            pressed.put("s",true);
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D) {
            moveX -= speed;
            pressed.put("d",false);
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            moveX += speed;
            pressed.put("a",false);
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            moveY -= speed;
            pressed.put("space",false);
        }
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            moveY += speed;
            pressed.put("shift",false);
        }
        
        if (e.getKeyCode() == KeyEvent.VK_W) {
            moveZ -= speed;
            pressed.put("w",false);
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            moveZ += speed;
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
        double pivot = polygons.get(high).avgDistToCam;
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
        frame = new JFrame("3D Simulation");
        lastX = MouseInfo.getPointerInfo().getLocation();
        lastY = MouseInfo.getPointerInfo().getLocation();
        try {
            (new Robot()).mouseMove((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
        }
        catch (AWTException e) {}
        
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
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
        
        lightSource = Matrix.vecNormalise(lightSource);

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
            //polygons.add(new ThreeDPolygon(x1, y1, z1, new Color(160 + 10*(i/4), 130, 255 - 20*(i/4), 255)));
        }
        //polygons.add(new ThreeDPolygon(new double[] {-5, 4, 4, -5}, new double[] {0, 0, 0, 0}, new double[] {1, 1, 10, 10}, new Color(160, 255, 160, 255)));
        

        /*mesh.triangles = new ArrayList<>();
        //South
        mesh.triangles.add(new ThreeDPolygon(new Triangle(new double[]{0, 0, 0}, new double[]{0, 1, 0}, new double[]{1, 1, 0}), Color.BLACK));
        mesh.triangles.add(new ThreeDPolygon(new Triangle(new double[]{0, 0, 0}, new double[]{1, 1, 0}, new double[]{1, 0, 0}), Color.BLACK));
        //East
        mesh.triangles.add(new ThreeDPolygon(new Triangle(new double[]{1, 0, 0}, new double[]{1, 1, 0}, new double[]{1, 1, 1}), Color.BLACK));
        mesh.triangles.add(new ThreeDPolygon(new Triangle(new double[]{1, 0, 0}, new double[]{1, 1, 1}, new double[]{1, 0, 1}), Color.BLACK));
        //North
        mesh.triangles.add(new ThreeDPolygon(new Triangle(new double[]{1, 0, 1}, new double[]{1, 1, 1}, new double[]{0, 1, 1}), Color.BLACK));
        mesh.triangles.add(new ThreeDPolygon(new Triangle(new double[]{1, 0, 1}, new double[]{0, 1, 1}, new double[]{0, 0, 1}), Color.BLACK));
        //West
        mesh.triangles.add(new ThreeDPolygon(new Triangle(new double[]{0, 0, 1}, new double[]{0, 1, 1}, new double[]{0, 1, 0}), Color.BLACK));
        mesh.triangles.add(new ThreeDPolygon(new Triangle(new double[]{0, 0, 1}, new double[]{0, 1, 0}, new double[]{0, 0, 0}), Color.BLACK));
        //Top
        mesh.triangles.add(new ThreeDPolygon(new Triangle(new double[]{0, 1, 0}, new double[]{0, 1, 1}, new double[]{1, 1, 1}), Color.BLACK));
        mesh.triangles.add(new ThreeDPolygon(new Triangle(new double[]{0, 1, 0}, new double[]{1, 1, 1}, new double[]{1, 1, 0}), Color.BLACK));
        //Bottom
        mesh.triangles.add(new ThreeDPolygon(new Triangle(new double[]{1, 0, 1}, new double[]{0, 0, 1}, new double[]{0, 0, 0}), Color.BLACK));
        mesh.triangles.add(new ThreeDPolygon(new Triangle(new double[]{1, 0, 1}, new double[]{0, 0, 0}, new double[]{1, 0, 0}), Color.BLACK));
        */
        Color cubeColor = new Color(200, 200, 255);
        //South
        // polygons.add(new ThreeDPolygon(new Triangle(new double[]{0, 0, 3, 1}, new double[]{0, 1, 3, 1}, new double[]{1, 1, 3, 1}, new double[]{0, 1, 1}, new double[]{0, 0, 1}, new double[]{1, 0, 1}), cubeColor));
        // polygons.add(new ThreeDPolygon(new Triangle(new double[]{0, 0, 3, 1}, new double[]{1, 1, 3, 1}, new double[]{1, 0, 3, 1}, new double[]{0, 1, 1}, new double[]{1, 0, 1}, new double[]{1, 1, 1}), cubeColor));
        // //East
        // polygons.add(new ThreeDPolygon(new Triangle(new double[]{1, 0, 3, 1}, new double[]{1, 1, 3, 1}, new double[]{1, 1, 4, 1}, new double[]{0, 1, 1}, new double[]{0, 0, 1}, new double[]{1, 0, 1}), cubeColor));
        // polygons.add(new ThreeDPolygon(new Triangle(new double[]{1, 0, 3, 1}, new double[]{1, 1, 4, 1}, new double[]{1, 0, 4, 1}, new double[]{0, 1, 1}, new double[]{1, 0, 1}, new double[]{1, 1, 1}), cubeColor));
        // //North
        // polygons.add(new ThreeDPolygon(new Triangle(new double[]{1, 0, 4, 1}, new double[]{1, 1, 4, 1}, new double[]{0, 1, 4, 1}, new double[]{0, 1, 1}, new double[]{0, 0, 1}, new double[]{1, 0, 1}), cubeColor));
        // polygons.add(new ThreeDPolygon(new Triangle(new double[]{1, 0, 4, 1}, new double[]{0, 1, 4, 1}, new double[]{0, 0, 4, 1}, new double[]{0, 1, 1}, new double[]{1, 0, 1}, new double[]{1, 1, 1}), cubeColor));
        // //West
        // polygons.add(new ThreeDPolygon(new Triangle(new double[]{0, 0, 4, 1}, new double[]{0, 1, 4, 1}, new double[]{0, 1, 3, 1}, new double[]{0, 1, 1}, new double[]{0, 0, 1}, new double[]{1, 0, 1}), cubeColor));
        // polygons.add(new ThreeDPolygon(new Triangle(new double[]{0, 0, 4, 1}, new double[]{0, 1, 3, 1}, new double[]{0, 0, 3, 1}, new double[]{0, 1, 1}, new double[]{1, 0, 1}, new double[]{1, 1, 1}), cubeColor));
        // //Top
        // polygons.add(new ThreeDPolygon(new Triangle(new double[]{0, 1, 3, 1}, new double[]{0, 1, 4, 1}, new double[]{1, 1, 4, 1}, new double[]{0, 1, 1}, new double[]{0, 0, 1}, new double[]{1, 0, 1}), cubeColor));
        // polygons.add(new ThreeDPolygon(new Triangle(new double[]{0, 1, 3, 1}, new double[]{1, 1, 4, 1}, new double[]{1, 1, 3, 1}, new double[]{0, 1, 1}, new double[]{1, 0, 1}, new double[]{1, 1, 1}), cubeColor));
        // //Bottom
        // polygons.add(new ThreeDPolygon(new Triangle(new double[]{1, 0, 4, 1}, new double[]{0, 0, 4, 1}, new double[]{0, 0, 3, 1}, new double[]{0, 1, 1}, new double[]{0, 0, 1}, new double[]{1, 0, 1}), cubeColor));
        // polygons.add(new ThreeDPolygon(new Triangle(new double[]{1, 0, 4, 1}, new double[]{0, 0, 3, 1}, new double[]{1, 0, 3, 1}, new double[]{0, 1, 1}, new double[]{1, 0, 1}, new double[]{1, 1, 1}), cubeColor));
        //BufferedImage[] image;
        try {
            /*//image = ImageIO.read(new File("cottage_diffuse.png"));
			//image = ImageIO.read(new File("handgun_C.jpg"));
            vals = new Color[image.getWidth() * image.getHeight()];
            for (int i = 0; i < image.getHeight(); i++) {
                for (int j = 0; j < image.getWidth(); j++) {
                    vals[i * image.getWidth() + j] = new Color(image.getRGB(j, i));
                }
            }
            loadObjectFromFile("cottage_blender.obj", vals, Color.BLACK, image, true);
            //loadObjectFromFile("gun.obj", vals, Color.BLACK, image, true);

            image = ImageIO.read(new File("blue.png"));
            vals = new Color[image.getWidth() * image.getHeight()];
            for (int i = 0; i < image.getHeight(); i++) {
                for (int j = 0; j < image.getWidth(); j++) {
                    vals[i * image.getWidth() + j] = new Color(image.getRGB(j, i));
                }
            }
            loadObjectFromFile("lambo.obj", vals, Color.BLUE, image, false, 6, 0, 0);*/
            image = new BufferedImage[1];
            image[0] = ImageIO.read(new File("gray.png"));
            //image[0] = ImageIO.read(new File("testGrid.png"));
            //image[0] = ImageIO.read(new File("cottage_diffuse.png"));
            //image[0] = ImageIO.read(new File("handgun_C.jpg"));
            for (int i = 1; i < image.length; i++) {
                image[i] = new BufferedImage(image[i - 1].getWidth() / 2, image[i - 1].getHeight() / 2, BufferedImage.TYPE_INT_RGB);
                for (int j = 0; j < image[i].getWidth(); j++) {
                    for (int k = 0; k < image[i].getHeight(); k++) {
                        Color temp = new Color(image[i - 1].getRGB(2 * j, 2 * k));
                        int red = (temp).getRed(), green = (temp).getGreen(), blue = (temp).getBlue();
                        for (int l = 0; l < 3; l++) {
                            switch(l) {
                                case 0:
                                    temp = new Color(image[i - 1].getRGB(2 * j + 1, 2 * k));
                                    break;
                                case 1:
                                    temp = new Color(image[i - 1].getRGB(2 * j, 2 * k + 1));
                                    break;
                                case 2:
                                    temp = new Color(image[i - 1].getRGB(2 * j + 1, 2 * k + 1));
                                    break;
                            }
                            red += (temp).getRed(); green += (temp).getGreen(); blue += (temp).getBlue();
                        }
                        red /= 4; green /= 4; blue /= 4;
                        temp = new Color(red, green, blue);
                        image[i].setRGB(j, k, temp.getRGB());
                    }
                }
            }
            
            vals = new Color[image.length][];
            for (int i = 0; i < image.length; i++) {
                vals[i] = new Color[image[i].getWidth() * image[i].getHeight()];
                for (int j = 0; j < image[i].getHeight(); j++) {
                    for (int k = 0; k < image[i].getWidth(); k++) {
                        vals[i][j * image[i].getWidth() + k] = new Color(image[i].getRGB(k, j));
                    }
                }
            }

            //loadObjectFromFile("cube.obj", vals, image, true);
            //loadObjectFromFile("cottage_blender.obj", vals, image, true);
            //loadObjectFromFile("gun.obj", vals, image, true);
            loadObjectFromFile("landscape.obj", vals, Color.gray, image, false);
            /*image = new BufferedImage[1];
            image[0] = ImageIO.read(new File("checkerboard.png"));
            vals = new Color[image.length][];
            for (int i = 0; i < image.length; i++) {
                vals[i] = new Color[image[i].getWidth() * image[i].getHeight()];
                for (int j = 0; j < image[i].getHeight(); j++) {
                    for (int k = 0; k < image[i].getWidth(); k++) {
                        vals[i][j * image[i].getWidth() + k] = new Color(image[i].getRGB(k, j));
                    }
                }
            }
            loadObjectFromFile("cube.obj", vals, image, true);*/
		} catch (IOException e) {
            vals = new Color[0][0];
			e.printStackTrace();
		}

        last = System.nanoTime();
        System.out.println("done create");
        doneCreate = true;
    }
    public static void loadObjectFromFile(String fileName, Color[][] c, BufferedImage img[], boolean flip) { 
        loadObjectFromFile(fileName, c, Color.BLACK, img, flip, 0, 0, 0);
    }
    public static void loadObjectFromFile(String fileName, Color[][] c, Color backup, BufferedImage img[], boolean flip) { 
        loadObjectFromFile(fileName, c, backup, img, flip, 0, 0, 0);
    }
    public static void loadObjectFromFile(String fileName, Color[][] c, Color backup, BufferedImage img[], boolean flip, double xOff, double yOff, double zOff) {
        ArrayList<double[]> verticeList = new ArrayList<double[]>();
        ArrayList<double[]> texPos = new ArrayList<double[]>();
        verticeList.add(new double[]{});
        texPos.add(new double[]{});
        int cnt = 0;
        Color[][] temp = {{backup}};
        try {
            File obj = new File(fileName);
            Scanner fileReader = new Scanner(obj);
            while (fileReader.hasNextLine()) {
                String data = fileReader.nextLine();
                double v1, v2, v3, v4;
                double vt1, vt2, vt3, vt4;
                /*if (data.charAt(0) == '#') {
                    fileReader.nextLine();
                }
                else */if (data.charAt(0) == 'v') {
                    if (data.charAt(1) == ' ') {
                        data = data.substring(data.indexOf(" ") + 1);
                        v1 = Double.parseDouble(data.substring(0, data.indexOf(" ")));
                        v1 += xOff;

                        data = data.substring(data.indexOf(" ") + 1);
                        v2 = Double.parseDouble(data.substring(0, data.indexOf(" ")));
                        v2 += yOff;

                        data = data.substring(data.indexOf(" ") + 1);
                        v3 = Double.parseDouble(data.substring(0));
                        verticeList.add(new double[]{v1, v2, v3});
                        v3 += zOff;
                    }
                    else if (data.charAt(1) == 't') {
                        data = data.substring(data.indexOf(" ") + 1);
                        vt1 = Double.parseDouble(data.substring(0, data.indexOf(" ")));
                        //vt1 = 1.0 - vt1;
                        data = data.substring(data.indexOf(" ") + 1);
                        vt2 = Double.parseDouble(data.substring(0));
                        if (flip) {
                            vt2 = 1.0 - vt2;
                        }
                        texPos.add(new double[]{vt1, vt2});
                    }
                }
                else if (data.charAt(0) == 'f') {
                    if (data.contains("/")) {
                        data = data.substring(data.indexOf(" ") + 1);
                        v1 = Double.parseDouble(data.substring(0, data.indexOf("/")));
                        data = data.substring(data.indexOf("/") + 1);
                        if (data.substring(0, data.indexOf("/")).length() > 0) {
                            vt1 = Double.parseDouble(data.substring(0, data.indexOf("/")));
                        }
                        else {
                            vt1 = 0;
                        }

                        data = data.substring(data.indexOf(" ") + 1);
                        v2 = Double.parseDouble(data.substring(0, data.indexOf("/")));
                        data = data.substring(data.indexOf("/") + 1);
                        if (data.substring(0, data.indexOf("/")).length() > 0) {
                            vt2 = Double.parseDouble(data.substring(0, data.indexOf("/")));
                        }
                        else {
                            vt2 = 0;
                        }

                        data = data.substring(data.indexOf(" ") + 1);
                        v3 = Double.parseDouble(data.substring(0, data.indexOf("/")));
                        data = data.substring(data.indexOf("/")  +1);
                        if (data.substring(0, data.indexOf("/")).length() > 0) {
                            vt3 = Double.parseDouble(data.substring(0, data.indexOf("/")));
                        }
                        else {
                            vt3 = -1;
                        }

                        if (data.indexOf(" ") != -1) {
                            data = data.substring(data.indexOf(" ") + 1);
                            v4 = Double.parseDouble(data.substring(0, data.indexOf("/")));
                            data = data.substring(data.indexOf("/")  +1);
                            if (data.substring(0, data.indexOf("/")).length() > 0) {
                                vt4 = Double.parseDouble(data.substring(0, data.indexOf("/")));
                            }
                            else {
                                vt4 = 0;
                            }
                            if (vt3 == -1) {
                                vt3 = 0;
                                //if (cnt++ == 0)
                                polygons.add(new ThreeDPolygon(new Triangle(verticeList.get((int)v1), verticeList.get((int)v3), verticeList.get((int)v4), texPos.get((int)vt1), texPos.get((int)vt3), texPos.get((int)vt4)), temp, img));
                                vt3 = -1;
                            }
                            else {
                                //if (cnt++ == 0)
                                polygons.add(new ThreeDPolygon(new Triangle(verticeList.get((int)v1), verticeList.get((int)v3), verticeList.get((int)v4), texPos.get((int)vt1), texPos.get((int)vt3), texPos.get((int)vt4)), c, img));
                            }
                        }
                    }
                    else {
                        data = data.substring(data.indexOf(" ") + 1);
                        v1 = Double.parseDouble(data.substring(0, data.indexOf(" ")));
                        vt1 = 0;

                        data = data.substring(data.indexOf(" ") + 1);
                        v2 = Double.parseDouble(data.substring(0, data.indexOf(" ")));
                        vt2 = 0;

                        data = data.substring(data.indexOf(" ") + 1);
                        v3 = Double.parseDouble(data.substring(0));
                        vt3 = -1;

                        if (data.indexOf(" ") != -1) {
                            data = data.substring(data.indexOf(" ") + 1);
                            v4 = Double.parseDouble(data.substring(0));
                            vt4 = 0;

                            if (vt3 == -1) {
                                vt3 = 0;
                                //if (cnt++ == 0)
                                polygons.add(new ThreeDPolygon(new Triangle(verticeList.get((int)v1), verticeList.get((int)v3), verticeList.get((int)v4), texPos.get((int)vt1), texPos.get((int)vt3), texPos.get((int)vt4)), temp, img));
                                vt3 = -1;
                            }
                            else {
                                //if (cnt++ == 0)
                                polygons.add(new ThreeDPolygon(new Triangle(verticeList.get((int)v1), verticeList.get((int)v3), verticeList.get((int)v4), texPos.get((int)vt1), texPos.get((int)vt3), texPos.get((int)vt4)), c, img));
                            }
                        }
                    }
                    if (vt3 == -1) {
                        vt3 = 0;
                        //if (cnt++ == 0)
                        polygons.add(new ThreeDPolygon(new Triangle(verticeList.get((int)v1), verticeList.get((int)v2), verticeList.get((int)v3), texPos.get((int)vt1), texPos.get((int)vt2), texPos.get((int)vt3)), temp, img));
                    }
                    else {
                        //if (cnt++ == 0)
                        polygons.add(new ThreeDPolygon(new Triangle(verticeList.get((int)v1), verticeList.get((int)v2), verticeList.get((int)v3), texPos.get((int)vt1), texPos.get((int)vt2), texPos.get((int)vt3)), c, img));
                    }
                }
            }
            fileReader.close();
        }
        catch (FileNotFoundException e) {}
    }
}