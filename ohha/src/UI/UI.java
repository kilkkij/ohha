package UI;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class UI implements Runnable {
    
    private JFrame frame;
    
    public UI() {
    }
    
    public void log(String tuloste) {
        System.out.println(tuloste);
    }

    @Override
    public void run() {
        frame = new JFrame("Box");
        frame.setPreferredSize(new Dimension(400, 400));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        frame.pack();
        frame.setVisible(true);
    }
    
    public JFrame getFrame() {
        return frame;
    }
    
}
