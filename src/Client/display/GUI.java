package Client.display;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JPanel implements ActionListener {

    public GUI(){
        initGUI();
    }

    public void initGUI(){
        setFocusable(true);
        createLabels();
    }

    private void createLabels(){
        //Title at the top
        JLabel label = new JLabel();
        label.setText("News Feed");
        add(label);

        //Input field
        JTextField field = new JTextField(20);
        add(field);

        //button
        JButton button = new JButton("Send");
        add(button);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
