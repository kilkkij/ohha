package UI;

import physics.Material;

/**
 * Piirtäjäolio. Käsittelee uuden suorakaiteen muodostamisen vaiheet.
 * @author juho
 */
public class ItemBuilder {
    
    private final UI userInterface;
    private final Material material;
    private RectangleInProgress rectangle;
    
    /**
     *
     * @param userInterface
     */
    public ItemBuilder(UI userInterface) {
        this.userInterface = userInterface;
        this.material = new Material(1., .3, .5);
    } 

    /**
     * Päivitä rakenteilla oleva suorakaide.
     * @param x kulman x-koordinaatti
     * @param y kulman y-koordinaatti
     */
    public void update(int x, int y) {
        if (!userInterface.isBuilding()) {
            startBuilding(x, y);
        }
        rectangle.updateSize(x, y);
    }

    private void startBuilding(int x, int y) {
        // Tee uusi suorakaideolio.
        rectangle = new RectangleInProgress(userInterface.getCanvas(), x, y);
        // Ilmoita käyttöliittymälle, jotta se osaa näyttää esineen ruudulla.
        userInterface.startBuilding(rectangle);
    }

    /**
     * Jos esine oli tekeillä, tämä metodi lopettaa tekovaiheen ja luo
     * uuden esineen käyttliittymälle.
     * @param finishX viimeinen x-koordinaatti
     * @param finishY viimeinen y-koordinaatti
     */
    public void finishBuilding(int finishX, int finishY) {
        if (!userInterface.isBuilding()) {
            return;
        }
        userInterface.stopBuilding();
        userInterface.addItem(rectangle.toSimulationItem(material));
    }

} 