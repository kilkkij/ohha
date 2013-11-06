package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;
import physics.Item;
import physics.Simulation;

public class Canvas extends JPanel{
    
    private UI ui;
    private double dpu;
    private int width;
    private int height;

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