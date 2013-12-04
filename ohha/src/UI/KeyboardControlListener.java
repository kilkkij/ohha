package UI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import physics.Item;
import physics.SimulationEnvironment;

class KeyboardControlListener implements KeyListener {
    
    UI ui;
    private final SimulationEnvironment simEnv;

    public KeyboardControlListener(UI ui, SimulationEnvironment simEnv) {
        this.ui = ui;
        this.simEnv = simEnv;
    } 

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        
        // tuhoa esineitä
        if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
            removeItems();
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
            conditionalRotate(ui.rotateUnit);
        } else if (ke.getKeyCode() == KeyEvent.VK_X) {
            conditionalRotate(-ui.rotateUnit);
        
        // kappaleiden staattisuus
        } else if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
            toggleSelectedStatic();
        }
    }
    
    private void removeItems() {
        boolean atLeastOneChosen = false;
        for (Iterator<Item> it = ui.getItems().iterator(); it.hasNext();) {
            Item item = it.next();
            if (ui.isSelected(item)) {
                it.remove();
                ui.unSelect(item);
                atLeastOneChosen = true;
            }
        }
        if (!atLeastOneChosen) {
            simEnv.getSim().clear();
            ui.unSelectAll();
        }
    }
    
    private void conditionalRotate(double angle) {
        for (Item item: ui.getItems()) {
            if (ui.isSelected(item)) {
                item.rotate(angle);
            }
        }
    }
    
    private void toggleSelectedStatic() {
        for (Item item: ui.getItems()) {
            if (ui.isSelected(item)) {
                if (!item.static_()) {
                    setAllItemsStatic();
                    return;
                }
            }
        }
        setAllItemsFree();
    }
    
    private void setAllItemsStatic() {
        for (Item item: ui.getItems()) {
            if (ui.isSelected(item)) {
                item.setStatic();
            }
        }
    }
    
    private void setAllItemsFree() {
        for (Item item: ui.getItems()) {
            if (ui.isSelected(item)) {
                item.setFree();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

} 