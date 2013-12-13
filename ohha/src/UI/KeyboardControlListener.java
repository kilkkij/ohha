package UI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Näppäimistönkuuntelija.
 * @author juho
 */
public class KeyboardControlListener implements KeyListener {
    
    UI ui;

    public KeyboardControlListener(UI ui) {
        this.ui = ui;
    } 

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        
        // tuhoa esineitä
        if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
            ui.getCanvas().removeItems();
        }
        
        // nuolinäppäimet
        else if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
            ui.moveViewLeft();
        } else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
            ui.moveViewRight();
        } else if (ke.getKeyCode() == KeyEvent.VK_UP) {
            ui.moveViewUp();
        } else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
            ui.moveViewDown();
        }
        
        // lähennys- ja etäännysnäppäimet
        else if (ke.getKeyCode() == KeyEvent.VK_PLUS) {
            ui.zoomIn();
        } else if (ke.getKeyCode() == KeyEvent.VK_MINUS) {
            ui.zoomOut();
        }
        
        // kappaleiden pyöritys
        else if (ke.getKeyCode() == KeyEvent.VK_Z) {
            ui.getCanvas().conditionalRotate(ui.rotateUnit);
        } else if (ke.getKeyCode() == KeyEvent.VK_X) {
            ui.getCanvas().conditionalRotate(-ui.rotateUnit);
        
        // kappaleiden staattisuus
        } else if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
            ui.getCanvas().toggleSelectedStatic();
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

} 