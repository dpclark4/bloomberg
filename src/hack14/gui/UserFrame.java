package hack14.gui;

import javax.swing.*;

/**
 * Created by bean on 11/7/2014.
 */
public class UserFrame extends JFrame{
    public UserFrame() {
        this.setVisible(true);
        this.setUndecorated(true);
    }
}
    private void getScreenSize(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        windowSizeX = (int)screenSize.getWidth();
        windowSizeY = (int)screenSize.getHeight();
        graphSizeX = windowSizeX - leftLabelSize;
        graphSizeY = windowSizeY - bottomLabelSize;
    }