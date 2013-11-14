package ohha;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Draw extends JPanel implements ActionListener {

    private int x = 100;
    private int y = 100;
    private double theta = Math.PI;

    Rectangle rec = new Rectangle(x,y,25,25);

    Timer timer = new Timer(25,this);

    Draw(){
        setBackground(Color.black);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;     
        g2d.setColor(Color.white);
        rec.x = 100;
        rec.y = 100;
        g2d.rotate(theta);
        g2d.draw(rec);
        g2d.fill(rec);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        x = (int) (x + (Math.cos(theta))*1);
        y = (int) (y + (Math.sin(theta))*1);
        theta = theta - (5*Math.PI/180);
        repaint();
    }
    
}