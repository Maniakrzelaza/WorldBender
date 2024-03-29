package server.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import server.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class UdpServer extends Thread {
    private static Logger logger = LogManager.getLogger(UdpServer.class.getName());
    private static final int PORT = Integer.parseInt(Properties.loadConfigFile("PORT_UDP"));
    private static final int BUFFER = 1024;
    private static DatagramSocket socket;
    private Map<String, User> existingUsers;

    public UdpServer() throws IOException {
        socket = new DatagramSocket(PORT);
        this.existingUsers = ExistingUsers.getInstance();
    }

    public void run() {
        byte[] buf = new byte[BUFFER];
        while (true) {
            try {
                Arrays.fill(buf, (byte)0);
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                readPackage(packet, buf);
            } catch(Exception e) {
                logger.error(e.toString(), e);
            }
        }
    }

    private void updatePlayersState(String id, JSONObject content){
        User currentUser = existingUsers.get(id);
        boolean isMoving = content.getBoolean("isMoving");
        String activeMovementKey = content.getString("activeMovementKey");
        currentUser.getPlayer().setMoving(isMoving);
        currentUser.getPlayer().setSpecialKeys((JSONObject)content.get("specialKeys"));
        currentUser.getPlayer().setWSAD((JSONObject)content.get("wsad"));
        currentUser.getPlayer().setActiveMovementKey(activeMovementKey);
        currentUser.getPlayer().setHeadDirection(content.getString("headDirection"));
    }


    public static void sendUdpMsgToAllUsersInRoom(String msg, CopyOnWriteArrayList<User> usersInRoom){
        for(User user : usersInRoom){
            createPackage(msg, user);
        }
    }

    private void readPackage(DatagramPacket packet, byte[] buf){
        String content = new String(buf);
        InetAddress clientAddress = packet.getAddress();
        int clientPort = packet.getPort();
        String id = clientAddress.toString() + "," + clientPort;
        JSONObject json = new JSONObject(content);
        switch (json.getString("msg")){
            case "playerState": updatePlayersState(id, (JSONObject) json.get("content")); break;
            default:
        }
    }

    private static void createPackage(String msg, User user){
        if(user.hasConnection()){
            DatagramPacket packet;
            byte[] data = msg.getBytes();
            packet = new DatagramPacket(data, data.length, user.getAddress(), user.getUdpPort());
            try {
                socket.send(packet);
            } catch (IOException e) {
                logger.error(e.toString(), e);
            }
        }
    }
}
