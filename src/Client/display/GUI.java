package Client.display;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JPanel implements ActionListener {
    private JButton sendButton;
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
        sendButton = new JButton("Send");
        sendButton.addActionListener(this);
        add(sendButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            System.out.printf("%s\n", inputField.getText());
        }
    }
}
