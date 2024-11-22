package javagame.game;

public class StoreItem {
    private String name;
    private int cost;
    private String description;

    public StoreItem(String name, int cost, String description) {
        this.name = name;
        this.cost = cost;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }
}
