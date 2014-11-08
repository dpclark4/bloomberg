package hack14.data;

/**
 * Created by bean on 11/8/2014.
 */
import com.bloomberglp.blpapi.*;
import hack14.gui.UserFrame;
import hack14.net.RequestResponse;

import java.io.IOException;

public class DataController {
    UserFrame Frame;
    RequestResponse rr;
    Message message;
    boolean paused = false;
    public DataController(UserFrame frame) throws Exception {
        Frame = frame;
        rr = new RequestResponse();
    }
    public void getData() throws Exception {
        rr.getSecurityData("INTC US Equity", 11, 7);
        message = rr.getMessage();
        //message.print(System.out);
    }
    public void disablePause(){
        paused = true;
    }
    public void enablePause(){
        paused = false;
    }
    public void testFrame() throws InterruptedException, IOException {

        Element data = message.getElement("barData").getElement("barTickData");
        int numbars = data.numValues();
       // System.out.println("numbars" + numbars);
       // message.print(System.out);

        for(int i = 0; i < numbars; i++){
            Element bar = data.getValueAsElement(i);
            Datetime time = bar.getElementAsDate("time");
            String date = getDate(time.toString());
            String current = getTime(time.toString());
            double open = bar.getElementAsFloat64("high");
            System.out.println(open);
            if(i == 0) {
                Frame.openingPrice = open;
            }
            if(Frame.getEvent() == null) {
                Frame.enterData(open, i);
                Frame.updateDate(date);
                Frame.updateTime(current);
                Thread.sleep(50);
            }
            else if(Frame.getEvent().equals("play")) {
                paused = false;
                if(!paused) {
                    Frame.enterData(open, i);
                    Frame.updateDate(date);
                    Frame.updateTime(current);
                    Thread.sleep(50);
                }
            }
            else if(Frame.getEvent().equals("paused")){
                paused = true;
                while(paused){

                }
            }

        }

    }
    int timer = 0;
    public void timer(){

    }
    public String getDate(String input) {
        StringBuilder temp = new StringBuilder();
        temp.append(input.charAt(5));
        temp.append(input.charAt(6));
        temp.append("/");
        temp.append(input.charAt(8));
        temp.append(input.charAt(9));
        temp.append("/");
        temp.append(input.charAt(0));
        temp.append(input.charAt(1));
        temp.append(input.charAt(2));
        temp.append(input.charAt(3));
        return temp.toString();
    }

    public String getTime(String input){
        StringBuilder temp = new StringBuilder();
        //if(input.charAt(12))
        temp.append(input.charAt(11));
        temp.append(input.charAt(12));
        //int sum = input.charAt(11) + input.charAt(12);
        //System.out.print("Sum " + sum);
        //temp.append(sum-5);
        temp.append(input.charAt(13));
        temp.append(input.charAt(14));
        temp.append(input.charAt(15));
        return temp.toString();
    }



}
