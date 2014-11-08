package hack14;

import hack14.data.DataController;
import hack14.gui.UserFrame;
//import hack14.gui.graph;
import com.bloomberglp.blpapi.*;
import hack14.net.RequestResponse;

import java.util.ArrayList;

/**
 * Created by bean on 11/8/2014.
 */
//stuff
public class mainDriver {
    public mainDriver() throws Exception {
        UserFrame frame;
        frame = new UserFrame();


        DataController controller;
        controller = new DataController(frame);
        //controller.testFrame();
        controller.getData();
        controller.testFrame();



        //ArrayList<Message> temp = rr.getMessages();
        //Element data = temp.get(0).getElement("barData").getElement("barTickData");
        //Element bar = data.getValueAsElement(0);
        //System.out.0println("open: " + bar.getElementAsFloat64("open"));

    }

    public static void main(String[] args) throws Exception {
        new mainDriver();
    }
}
