package Client.display;

import javax.swing.*;

public class GUI extends JPanel {

    public GUI(){
        initGUI();
    }

    public void initGUI(){
        setFocusable(true);
        add(createLabels());
    }

    private JLabel createLabels(){
        JLabel label = new JLabel();
        label.setText("News Feed");

        return label;
    }
}
