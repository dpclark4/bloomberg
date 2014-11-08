package hack14.data;

/**
 * Created by bean on 11/8/2014.
 */
import com.bloomberglp.blpapi.*;
import hack14.gui.UserFrame;
import hack14.net.RequestResponse;

import java.util.ArrayList;

public class DataController {
    UserFrame Frame;
    RequestResponse rr;
    Message message;
    public DataController(UserFrame frame) throws Exception {
        Frame = frame;
        rr = new RequestResponse();
    }
    public void getData() throws Exception {
        rr.getSecurityData("INTC US Equity", 11, 7);
        message = rr.getMessage();
        //message.print(System.out);
    }
    public void testFrame() throws InterruptedException {
        Element data = message.getElement("barData").getElement("barTickData");
        int numbars = data.numValues();
        System.out.println("numbars" + numbars);
        for(int i = 0; i < numbars; i++){
            Element bar = data.getValueAsElement(i);
            double open = bar.getElementAsFloat64("high");
            System.out.println(open);
            if(i == 0) {
                Frame.openingPrice = open;
            }
            Frame.enterData(open, i);
            Thread.sleep(50);


        }
    }


}
