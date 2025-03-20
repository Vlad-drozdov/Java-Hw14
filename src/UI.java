import javax.swing.*;
import java.awt.*;

public class UI extends JFrame{
    private DrawPanel drawPanel;

    public UI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Window");
        setSize(900, 800);
        setLayout(null);
        setBackground(Color.WHITE);

        JTextField rgbCode = new JTextField("#");
        rgbCode.setActionCommand("rgbCode");
        rgbCode.addActionListener(drawPanel);

        drawPanel = new DrawPanel(rgbCode,900,670);
        drawPanel.setBackground(Color.WHITE);
        drawPanel.setLocation(0,110);
        add(drawPanel);


        JPanel toolPanel = new JPanel();
        toolPanel.setLayout(null);
        toolPanel.setBackground(Color.WHITE);
        toolPanel.setBounds(0,0,900,130);
        add(toolPanel);

        JPanel weightPanel = new JPanel();
        weightPanel.setLayout(new GridLayout(4,1,5,5));
        weightPanel.setBackground(Color.WHITE);
        weightPanel.setBounds(155,0,100,110);

        JLabel weight = new JLabel("Weight");
        weight.setFont(new Font("Arial", Font.BOLD, 20));
        weightPanel.add(weight);

        String[] w_names = {"1","2","3"};

        for (int i = 0; i < w_names.length; i++) {
            JButton w_button = new JButton(w_names[i]);
            w_button.setForeground(Color.WHITE);
            w_button.setSize(100,10);
            w_button.setBackground(Color.BLACK);
            w_button.addActionListener(drawPanel);
            weightPanel.add(w_button);
        }

        JPanel dopPanel = new JPanel();
        dopPanel.setLayout(new GridLayout(2,2,5,5));
        dopPanel.setBackground(Color.WHITE);
        dopPanel.setBounds(260,0,110,110);



        ImageButton smileButton = new ImageButton("img/smile.png");
        smileButton.setActionCommand("smile");
        smileButton.addActionListener(drawPanel);
        dopPanel.add(smileButton);

        ImageButton trashButton = new ImageButton("img/trash.png");
        trashButton.setActionCommand("trash");
        trashButton.addActionListener(drawPanel);
        dopPanel.add(rgbCode);
        dopPanel.add(trashButton);

        ImageButton pipette = new ImageButton("img/pipette.png");
        pipette.setActionCommand("pipette");
        pipette.addActionListener(drawPanel);
        dopPanel.add(pipette);


        toolPanel.add(dopPanel);
        toolPanel.add(weightPanel);

        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new GridLayout(3,4,5,5));
        colorPanel.setBackground(Color.WHITE);
        colorPanel.setBounds(5,0,145,110);

        String[] button_names = {
                "blue", "red", "green", "yellow", "orange", "pink",
                "purple", "white", "black", "gray", "cyan", "lightgray"
        };

        Color[] button_colors = {
                Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK,
                Color.MAGENTA, Color.WHITE, Color.BLACK, Color.GRAY, Color.CYAN, Color.LIGHT_GRAY
        };

        for (int i = 0; i < button_names.length; i++) {
            JButton button = new JButton("");
            button.setActionCommand(button_names[i]);
            button.setBackground(button_colors[i]);
            button.addActionListener(drawPanel);
            colorPanel.add(button);
        }

        toolPanel.add(colorPanel);






        setVisible(true);
    }
}
