import java.io.*;
import java.net.Socket;

/**
 * Created by roman on 26.07.16.
 */
public class ClientHandler implements Runnable {
    private Server server;
    private Socket socket;


    private DataInputStream input;
    private DataOutputStream output;

    private volatile String nickname;

    public String getNickname() {
        return nickname;
    }

    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
            authorize();
            if (isAuthorize()) {
                startProcessingMessage();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            server.removeClient(this);
            closeConnect();
        }
    }

    private boolean isAuthorize() {
        return nickname != null;
    }

    private void startProcessingMessage() throws IOException {
        while (true) {
            String msg = input.readUTF();
            if (msg.equalsIgnoreCase("end")) {
                server.broadcast(prepMsg("покинул чат"));
                break;
            } else {
                server.broadcast(prepMsg(msg));
            }
        }
    }

    private void authorize() throws IOException {
        int errorCode = 0;
        while (!isAuthorize()) {
            String msg = input.readUTF();
                try {
                    String curNick = getClientFromDB(msg);
                    if (server.isInChat(curNick)) {
                        throw new HandshakeException("Пользователь с ником " + curNick + " уже авторизован!");
                    } else  {
                        nickname = curNick;
                        server.addClient(this);
                        sendMsg("Authorize successfully");
                        server.broadcast(prepMsg("подключился"));
                    }
                } catch (HandshakeException e) {
                    errorCode++;
                    if (errorCode >= 3) {
                        break;
                    }
                }
            }
    }

    private String prepMsg(String msg) {
        return nickname + ": " + msg;
    }

    private String getClientFromDB(String msg) {
        // login <login> <pass>
        String[] autorize = msg.split(" ");
        String login = autorize[0];
        String pass = autorize[1];
        return nickname = new DBHandler().getNickNameByLoginAndPassword(login, pass);
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

    public void sendMsg(String msg) {
        System.out.println("sending... " + msg + "\n");
        try {
            output.writeUTF(msg);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
