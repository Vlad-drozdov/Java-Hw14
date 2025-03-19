import javax.swing.*;
import java.awt.*;

public class ImageButton extends JButton {
    private ImageIcon imageIcon;

    public ImageButton(String imagePath) {
        this.imageIcon = new ImageIcon(imagePath);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
    }
}