package hack14.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by bean on 11/7/2014.
 */
public class UserFrame extends JFrame {
    private int windowSizeX, windowSizeY, graphSizeX, graphSizeY;
    private BufferedImage graph;
    private Graphics graphGraphics;
    public UserFrame() {
        getScreenSize();
        this.setVisible(true);
        this.setUndecorated(true);
    }
    private void initGraph(){
        graph = new BufferedImage(windowSizeX,windowSizeY,BufferedImage.TYPE_INT_ARGB);
        graphGraphics = graph.createGraphics();
        //drawBackground();
        this.pack();
    }
    private void getScreenSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        windowSizeX = (int) screenSize.getWidth();
        windowSizeY = (int) screenSize.getHeight();
        graphSizeX = windowSizeX / 2;
        graphSizeY = windowSizeY / 2;
    }
}