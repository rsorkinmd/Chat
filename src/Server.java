import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by roman on 25.07.16.
 */
public class Server {
    public static final int PORT = 8189;

    private HashMap <String, ClientHandler> clients = new HashMap<>();

    public void startServer() {
        try (ServerSocket socket = new ServerSocket(PORT)) {
            while (true) {
                initConnection(socket.accept());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initConnection(Socket socket) {
        ClientHandler handler = new ClientHandler(this, socket);
        new Thread(handler).start();
    }

    public void addClient(ClientHandler clientHandler) {
        clients.put(clientHandler.getNickname(), clientHandler);
    }

    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler.getNickname(), clientHandler);
    }

    public void broadcast(String msg) {
        for (ClientHandler cl: clients.values()) {
            cl.sendMsg(msg);
        }
    }

    public static void main(String[] args) {
        new Server().startServer();
    }

    public boolean isInChat(String curNick) {
        return clients.keySet().contains(curNick);
    }
}

