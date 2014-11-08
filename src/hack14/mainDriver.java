package hack14;

import hack14.data.DataController;
import hack14.gui.UserFrame;

/**
 * Created by bean on 11/8/2014.
 */
//stuff
public class mainDriver {
    public mainDriver() {
        UserFrame frame;
        frame = new UserFrame();
        DataController controller;
        controller = new DataController();

    }

    public static void main(String[] args){
        new mainDriver();
    }
}
