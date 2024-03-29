package server.players;

import server.User;
import server.connection.GameController;

public class PlayersFactory {
    public static Player createPlayer(String type, User user, GameController gameController) {
        Player resultPlayer;
        switch (type) {
            case "Ground":
                resultPlayer = new Ground(user, gameController);
                break;
            case "Water":
                resultPlayer = new Water(user, gameController);
                break;
            default:
                resultPlayer = null;
                break;
        }
        return resultPlayer;
    }
}
