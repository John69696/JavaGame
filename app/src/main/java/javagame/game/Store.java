package javagame.game;

import java.awt.*;
import java.util.ArrayList;

public class Store {
    private boolean open;
    private ArrayList<StoreItem> items;
    private ArrayList<Rectangle> buttons;

    public Store() {
        open = false;
        items = new ArrayList<>();
        buttons = new ArrayList<>();
        initializeItems();
    }

    private void initializeItems() {
        // Add some example items
        items.add(new StoreItem("Extra Health", 50, "Increases your health by 20."));
        items.add(new StoreItem("Damage Upgrade", 100, "Increases bullet damage by 1."));
        items.add(new StoreItem("Speed Boost", 75, "Increases movement speed temporarily."));

        // Initialize button positions
        for (int i = 0; i < items.size(); i++) {
            buttons.add(new Rectangle(500, 160 + i * 50, 100, 30)); // Button size and position
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void toggle() {
        open = !open;
    }

    public void draw(Graphics g) {
        if (!open) return;

        // Draw the store background
        g.setColor(new Color(50, 50, 50, 200)); // Semi-transparent background
        g.fillRect(100, 100, 600, 400);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("STORE", 350, 140);

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        for (int i = 0; i < items.size(); i++) {
            StoreItem item = items.get(i);
            int yPosition = 180 + i * 50;
            g.drawString(item.getName() + " - " + item.getDescription(), 150, yPosition);
            g.drawString("Cost: " + item.getCost() + " coins", 400, yPosition);

            // Draw button
            g.setColor(Color.GRAY);
            Rectangle button = buttons.get(i);
            g.fillRect(button.x, button.y, button.width, button.height);
            g.setColor(Color.WHITE);
            g.drawString("Buy", button.x + 25, button.y + 20);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Press E to close", 320, 480);
    }

    public void handleMouseClick(int mouseX, int mouseY, Player player) {
        for (int i = 0; i < buttons.size(); i++) {
            Rectangle button = buttons.get(i);
            if (button.contains(mouseX, mouseY)) {
                purchaseItem(player, i);
            }
        }
    }

    private void purchaseItem(Player player, int itemIndex) {
        if (itemIndex < 0 || itemIndex >= items.size()) return;

        StoreItem item = items.get(itemIndex);
        if (player.getCoins() >= item.getCost()) {
            player.subtractCoins(item.getCost());
            applyItemEffect(player, item);
        }
    }

    private void applyItemEffect(Player player, StoreItem item) {
        switch (item.getName()) {
            case "Extra Health" -> player.heal(20);
            case "Damage Upgrade" -> player.increaseDamage(1); // Assuming the player class has a method for damage upgrade
            case "Speed Boost" -> player.activateSpeedBoost(5000); // Temporary speed boost for 5 seconds
        }
    }
}
