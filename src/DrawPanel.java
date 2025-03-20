import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;


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
    private Color[][] container;

    public DrawPanel(JTextField rgbCodeField, int width, int height) {
        this.rgbCodeField = rgbCodeField;
        this.width = width;
        this.height = height;

        setSize(width,height);

        container = new Color[width][height];



        addMouseMotionListener(this);
        addMouseListener(this);
    }

    public static String rgbCodeFormate(String code){
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
                        return "#000000";
                    }
                }
                return code;
            } else {
                return "#000000";
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
                    return "#000000";
                }
            }
            return "#"+code;
        } else {
            return "#000000";
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
//                if (weight == 1){
//                    container[oldX][oldY] = color;
//                    printContainer(container);
//                }
            }
        }

        oldX= e.getX();
        oldY= e.getY();
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

                System.out.println("Координаты экрана: " + screenPoint.x + ", " + screenPoint.y);
                System.out.println("Цвет: " + hexColor);

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
            case "pipette":
                if (smileTurn) {
                    smileTurn = false;
                }
                pipetteTurn = !pipetteTurn;
                addMouseListener(this);
                break;
            case "rgbCode":
                color = Color.decode(rgbCodeFormate(rgbCodeField.getText()));
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
