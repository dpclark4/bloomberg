package hack14;

import hack14.data.DataController;
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
        frame.openingPrice = 555;
        frame.enterData(543,0);
        frame.enterData(555,11);
        frame.enterData(551,22);
        frame.enterData(552,33);
        frame.enterData(557,44);
        frame.enterData(573,55);
        frame.enterData(554,66);
        frame.enterData(552,77);
        frame.enterData(551,88);
        //DataController controller;
        //controller = new DataController();
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
