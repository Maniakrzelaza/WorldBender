package server.pickups;

public class PickupFactory {
    public static APickup createPickup(int x, int y, String type){
        APickup pickup = null;
        switch (type){
            case "Hp":
                pickup = new HpPickup(x, y);
                break;
            case "InnerEye":
                pickup = new InnerEye(x, y);
                break;
            case "SadOnion":
                pickup = new SadOnion(x, y);
                break;
            case "Warp":
                pickup = new Warp(x, y);
                break;
            case "InvisibleWarp":
                pickup = new InvisibleWarp(x, y);
                break;
            case "Mana":
                //TODO make mana pickup
                break;
        }
        return pickup;
    }
}
