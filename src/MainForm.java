import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by roman on 16.07.16.
 */
public class MainForm extends JFrame {
    private JTextArea textArea;
    private AuthDialog authDialog;
    private String login;
    private String pass;

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    private Client client = new Client((s -> {
        textArea.append(s + "\n");
    }), (s -> {
        JDialog error = new JDialog(this, true);
        error.setLayout(new BorderLayout());
        JLabel errorLabel = new JLabel("Что-то пошло не так: " + s);
        JButton okButton = new JButton("OK");
        add(errorLabel, BorderLayout.CENTER);
        add(okButton, BorderLayout.SOUTH);
        okButton.addActionListener(e -> {
            new AuthDialog(this, true);
        });
    })
    );

    public Client getClient() {
        return client;
    }

    public MainForm() {

        setTitle("Супер-пупер чат");
        setBounds(500, 500, 400, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Создаем массив панелек
        JPanel[] panels = new JPanel[2];
        panels[0] = new JPanel();
        panels[1] = new JPanel();

        //Добавляем панель для полей ввода текста и кнопки Enter
        add(panels[1], BorderLayout.SOUTH);

        textArea = new JTextArea();

        initTextArea(panels[0], textArea, this, client);
        initInputArea(panels[1], client);

        authDialog = new AuthDialog(this, true);

        setVisible(true);
    }

    private void initInputArea(JPanel panel1, Client client) {
        panel1.setLayout(new FlowLayout());
        panel1.setBorder(new EtchedBorder(Color.RED, Color.BLACK));
        panel1.setPreferredSize(new Dimension(400, 90));
        JTextField inp = new JTextField();
        inp.setPreferredSize(new Dimension(300, 40));
        JButton enter = new JButton("Enter");
        enter.setPreferredSize(new Dimension(60, 40));
        inp.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMsg(inp, client);
                }
            }
        });
        enter.addActionListener(e -> {
            sendMsg(inp, client);
        });

        panel1.add(inp);
        panel1.add(enter);
    }

    private void sendMsg(JTextField inp, Client client) {
        inp.setBackground(Color.WHITE);
        client.sendMsg(inp.getText());
        inp.setText("");
    }

    public void sendMsg(String authmsg) {
        client.sendMsg(authmsg);
    }

    private void initTextArea(JPanel panel, JTextArea area, JFrame mainFrame, Client client) {
        panel.setPreferredSize(new Dimension(400, 400));
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EtchedBorder(Color.RED, Color.BLACK));
        Font font = new Font("Arial", Font.ITALIC, 13);
        mainFrame.add(panel, BorderLayout.CENTER);
        area.setBackground(new Color(178, 184, 235));
        area.setFont(font);
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(area, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scroll);
    }

    public void connectToChat(String auth) {
        String curauth[] = auth.split(" ");
        client.connect(curauth[0], curauth[1]);
    }
}
