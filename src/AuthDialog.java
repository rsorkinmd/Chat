import javax.swing.*;
import java.awt.*;

/**
 * Created by roman on 31.07.16.
 */
public class AuthDialog extends JDialog {


    public AuthDialog(MainForm owner, boolean modal) {
        super(owner, modal);
        setSize(400, 200);
        setLocation(400, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel panelName = new JPanel(new GridLayout(1, 1));
        JPanel panelAuth = new JPanel(new GridLayout(2, 2));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 1));

        add(panelName, BorderLayout.NORTH);
        add(panelAuth, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        JLabel login = new JLabel("Введите логин");
        JLabel pass = new JLabel("Введите пароль");
        JLabel text = new JLabel("Авторизируйтесь пожалуйста");
        JTextField loginField = new JTextField();
        JTextField passField = new JTextField();
        JButton authButton = new JButton("Авторизироваться");

        panelName.add(text);
        panelAuth.add(login);
        panelAuth.add(loginField);
        panelAuth.add(pass);
        panelAuth.add(passField);
        buttonPanel.add(authButton);
        authButton.addActionListener(e -> {
            String auth = loginField.getText() + " " + passField.getText();
            owner.connectToChat(auth);
            this.dispose();
        });
        setVisible(true);
    }
}
