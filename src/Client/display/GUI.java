package Client.display;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JPanel implements ActionListener {
    private JButton button;
    private JTextField inputField;

    public GUI() {
        initGUI();
    }

    public void initGUI() {
        setFocusable(true);
        createLabels();
    }

    private void createLabels() {
        //Title at the top
        JLabel label = new JLabel();
        label.setText("News Feed");
        add(label);

        //Input field
        inputField = new JTextField(20);
        add(inputField);

        //button
        button = new JButton("Send");
        button.addActionListener(this);
        add(button);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            System.out.printf("%s\n", inputField.getText());
        }
    }
}
