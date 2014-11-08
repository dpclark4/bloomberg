package hack14;

import hack14.data.DataController;
import hack14.gui.UserFrame;
import hack14.gui.UserFrame;
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
//        frame.openingPrice = 50;
//        frame.enterData(50.0f,10);
//        frame.enterData(50.1f,20);
//        frame.enterData(50.2f,330);
//        frame.enterData(50.3f,40);
//        frame.enterData(49.6f,50);
//        frame.enterData(49.5f,60);
//        frame.enterData(49.7f,70);
        DataController controller;
        controller = new DataController(frame);
        controller.getData("INTC US Equity",11,7);
        controller.startTimer();
        //RequestResponse rr = new RequestResponse();
        //rr.getSecurityData("F US Equity", 11, 7);
        //ArrayList<Message> temp = rr.getMessages();
        //Element data = temp.get(0).getElement("barData").getElement("barTickData");
        //Element bar = data.getValueAsElement(0);
        //System.out.0println("open: " + bar.getElementAsFloat64("open"));

    }

    public static void main(String[] args) throws Exception {
        new mainDriver();
    }
}
