package server;

import com.badlogic.gdx.Game;
import server.connection.TcpServer;
import server.connection.UdpServer;

import java.io.IOException;

class Server extends Game {
    public Server(){

    }
    @Override
    public void create() {
        TcpServer tcpServer;
        UdpServer udpServer;
        try {
            udpServer = new UdpServer();
            tcpServer = new TcpServer();
            tcpServer.start();
            udpServer.start();
        } catch (IOException e) {
            System.out.println("Porty zajęte, prawdopodobnie serwer już jest włączony!");
        }
    }
}