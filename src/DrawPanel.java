import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class DrawPanel extends JPanel implements MouseMotionListener, MouseListener, ActionListener {

    private JTextField rgbCodeField;
    private int width; private int height;
    private Color color = Color.BLACK;
    private int weight = 1;
    private int weightWhite = 30;
    private boolean smileTurn = false;
    private boolean pipetteTurn = false;
    private int oldX;
    private int oldY;
    private String hexColor;
    private ArrayList<Integer> xCopy = new ArrayList<>();
    private ArrayList<Integer> yCopy = new ArrayList<>();
    private UI ui;

    public DrawPanel(JTextField rgbCodeField, int width, int height, UI ui) {
        this.rgbCodeField = rgbCodeField;
        this.width = width;
        this.height = height;
        this.ui = ui;


        setSize(width,height);

        addMouseMotionListener(this);
        addMouseListener(this);
    }

    public static String rgbCodeFormate(String code) throws ColorCodeException{
        char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        code.toUpperCase();
        if (code.length() == 7) {
            if (code.charAt(0)=='#'){
                for (int i = 1; i < code.length(); i++) {
                    int test = 0;
                    for (int j = 0; j < hexChars.length; j++) {
                        if (code.charAt(i) == hexChars[j]){
                            test++;
                        }
                    }
                    if (test == 0){
                        throw new ColorCodeException("Невірний код. За змовчуванням встановлено чорний.");
                    }
                }
                return code;
            } else {
                throw new ColorCodeException("Невірний код. За змовчуванням встановлено чорний.");
            }
        } else if (code.length() == 6){
            for (int i = 0; i < code.length(); i++) {
                int test = 0;
                for (int j = 0; j < hexChars.length; j++) {
                    if (code.charAt(i) == hexChars[j]){
                        test++;
                    }
                }
                if (test == 0){
                    throw new ColorCodeException("Невірний код. За змовчуванням встановлено чорний.");
                }
            }
            return "#"+code;
        } else {
            throw new ColorCodeException("Невірний код. За змовчуванням встановлено чорний.");
        }
    }

    public static void printContainer(Color[][] a){
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if (a[i][j] == null) {
                    a[i][j] = Color.WHITE;
                }
                if (a[i][j]!= Color.WHITE){
                    System.out.print(a[i][j]+" ");
                }
            }
            System.out.println();
        }
    }

    public void paintSmile(int x,int y){
        Graphics g = getGraphics();
        g.setColor(Color.YELLOW);
        g.fillOval(x, y, 300, 300);
        g.setColor(Color.WHITE);
        g.fillOval(x+90, y+80, 45, 45);
        g.fillOval(x+170, y+80, 45, 45);
        g.setColor(Color.BLACK);
        g.drawOval(x, y, 300, 300);
        g.fillOval(x+100, y+90, 20, 20);
        g.fillOval(x+180, y+90, 20, 20);
        g.drawOval(x+90, y+80, 45, 45);
        g.drawOval(x+170, y+80, 45, 45);
        g.setColor(Color.RED);
        g.fillOval(x+100, y+135, 100,100);
        g.setColor(Color.YELLOW);
        g.fillRect(x+100,y+135,100,60);
    }







    @Override
    public void mouseDragged(MouseEvent e) {
        Graphics g = getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(color);
        int x = e.getX();
        int y = e.getY();
        if (!smileTurn && !pipetteTurn) {
            if (color == Color.WHITE){
                g.fillRect(x -(weightWhite/2), y -(weightWhite/2),weightWhite,weightWhite);
            }else {
                g2.setStroke(new BasicStroke(weight));
                g.drawLine(oldX, oldY, x, y);
            }
        }

        oldX= e.getX();
        oldY= e.getY();
        xCopy.add(e.getX());
        yCopy.add(e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        oldX= e.getX();
        oldY= e.getY();


    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (smileTurn) {
            paintSmile(oldX - 150, oldY - 150);
        } else if (pipetteTurn) {
            try {
                Robot robot = new Robot();

                Point screenPoint = e.getPoint();
                SwingUtilities.convertPointToScreen(screenPoint, e.getComponent());  // Преобразуем координаты окна в координаты экрана

                color = robot.getPixelColor(screenPoint.x, screenPoint.y);

                hexColor = "#" + Integer.toHexString(color.getRGB()).substring(2);

                rgbCodeField.setText(hexColor);
                pipetteTurn = false;
            } catch (AWTException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "redraw":
                Graphics g = getGraphics();
                g.setColor(Color.WHITE);
                g.fillRect(0,0, width, height);
                RedrawThread redrawThread = new RedrawThread(xCopy,yCopy,g, color,weight);
                redrawThread.start();
                if (redrawThread.isDone()){
                    System.out.println("ok");
                    xCopy.clear();
                    yCopy.clear();
                    redrawThread.setDone(false);
                }
                break;
            case "pipette":
                if (smileTurn) {
                    smileTurn = false;
                }
                pipetteTurn = !pipetteTurn;
                addMouseListener(this);
                break;
            case "rgbCode":

                try {
                    color = Color.decode(rgbCodeFormate(rgbCodeField.getText()));
                } catch (ColorCodeException ex) {
                    color = Color.BLACK;
                    new ErrorUI(ex.getMessage(), ex.getClass(), ui);
                }
                break;
            case "trash":
                repaint();
                break;
            case "smile":
                if (pipetteTurn) {
                    pipetteTurn = false;
                }
                smileTurn = !smileTurn;
                break;
            case "1":
                weight = 1;
                weightWhite = 30;
                break;
            case "2":
                weight = 3;
                weightWhite = 50;
                break;
            case "3":
                weight = 6;
                weightWhite = 80;
                break;
            default:
                switch (e.getActionCommand()) {
                    case "blue":
                        color = Color.BLUE;
                        break;
                    case "red":
                        color = Color.RED;
                        break;
                    case "green":
                        color = Color.GREEN;
                        break;
                    case "yellow":
                        color = Color.YELLOW;
                        break;
                    case "orange":
                        color = Color.ORANGE;
                        break;
                    case "pink":
                        color = Color.PINK;
                        break;
                    case "purple":
                        color = Color.MAGENTA;
                        break;
                    case "black":
                        color = Color.BLACK;
                        break;
                    case "gray":
                        color = Color.GRAY;
                        break;
                    case "cyan":
                        color = Color.CYAN;
                        break;
                    case "lightgray":
                        color = Color.LIGHT_GRAY;
                        break;
                    default:
                        color = Color.WHITE;
                        break;
                }
                if (smileTurn) {
                    smileTurn = false;
                }
                if (pipetteTurn) {
                    pipetteTurn = false;
                }
                break;
        }
    }











    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

class RedrawThread extends Thread{

    private boolean Done = false;
    private ArrayList<Integer> xCopy;
    private ArrayList<Integer> yCopy;
    private Graphics g;
    private Color color;
    private int weight;

    RedrawThread(ArrayList<Integer> x,ArrayList<Integer> y, Graphics g, Color color, int weight){
        this.xCopy = x;
        this.yCopy = y;
        this.g = g;
        this.color = color;
        this. weight = weight;
    }

    public boolean isDone() {
        return Done;
    }

    public void setDone(boolean done) {
        Done = done;
    }

    @Override
    public void run() {
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(color);
        for (int i = 0; i < xCopy.size()-2; i++) {
            int x = xCopy.get(i++);
            int y = yCopy.get(i++);
            int tempX = xCopy.get(i);
            int tempY = yCopy.get(i);
            g2.setStroke(new BasicStroke(weight));
            g.drawLine(tempX, tempY, x, y);
            try {
                sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        xCopy.clear();
        yCopy.clear();
        Done = true;
    }
}

class ColorCodeException extends Exception {

    public ColorCodeException(String msg){
        super(msg);
    }
}