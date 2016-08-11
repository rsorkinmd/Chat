import javax.imageio.IIOException;
import javax.swing.*;
import java.io.*;
import java.net.Socket;

/**
 * Created by roman on 26.07.16.
 */
public class Client {
    private Listener listener;
    private Listener handShakeExceptionListener;
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;
    private boolean isAuthorize = false;

    public boolean isAuthorize() {
        return isAuthorize;
    }

    public Client(Listener listener, Listener handShakeExceptionListener) {
        this.listener = listener;
        this.handShakeExceptionListener = handShakeExceptionListener;
    }

    public void connect(String login, String pass) {
        try {
            socket = new Socket("localhost", Server.PORT);
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
            if (autorize(login, pass))
                processReponse();
        } catch (IOException e) {
            throw new CanNotConnectToServer(e.getMessage(), e);
        }
    }

    private void processReponse() {
        new Thread(() -> {
            try {
                String s = null;
                while (true) {
                    s = input.readUTF();
                    if (isAuthorize) {
                        notifyListener(s);
                    } else notifyHandShakeExceptionListener(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeConnect();
            }
        }).start();
    }

    private void notifyHandShakeExceptionListener(String s) {
        handShakeExceptionListener.notify();
    }

    private void closeConnect() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean autorize (String login, String pass) {
        sendMsg(login + " " + pass);
        try {
            String msg = input.readUTF();
            if (msg.equals("Authorize successfully")) {
                isAuthorize = true;
                return true;
            } else handShakeExceptionListener.processMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void notifyListener(String s) {
        listener.processMessage(s);
    }

    public void sendMsg(String msg) {
        try {
            output.writeUTF(msg);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
