import javax.swing.*;
import java.awt.*;

public class ErrorUI extends JFrame {
    public ErrorUI(String msg, Class Er, UI ui){

        setSize(690,200);
        setLayout(new GridLayout(1,1));
        setTitle(Er.toString().substring(6));
        setLocationRelativeTo(ui);


        JLabel error = new JLabel(msg);
        error.setForeground(Color.RED);
        error.setFont(new Font("Arial", Font.BOLD, 25));

        add(error);

        setVisible(true);
    }
}
