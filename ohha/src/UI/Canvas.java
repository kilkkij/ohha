package UI;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import physics.Item;

public class Canvas extends JPanel{
    
    private final UI ui;
    private final double dpu;
    private final int width;
    private final int height;

    public Canvas(UI ui, double dpu, int width, int height) {
        super.setBackground(Color.WHITE);
        this.ui = ui;
        this.dpu = dpu;
        this.width = width;
        this.height = height;
    } 
    
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        for (Item item: ui.getItems()) {
            item.draw(graphics, dpu, width, height);
        }
    }
} 