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
        //UserFrame frame;
        //frame = new UserFrame();
        DataController controller;
        controller = new DataController();
        RequestResponse rr = new RequestResponse();
        rr.getSecurityData("F US Equity", 9, 10);
        Message msg = rr.getMessage();
        Element data = msg.getElement("barData").getElement("barTickData");
        int numbars = data.numValues();
        for(int i = 0; i < numbars; ++i) {
            Element bar = data.getValueAsElement(i);
            //System.out.println("open: " + bar.getElementAsFloat64("open"));
        }


    }

    public static void main(String[] args) throws Exception {
        new mainDriver();
    }
}
