package hack14.data;

/**
 * Created by bean on 11/8/2014.
 */
import com.bloomberglp.blpapi.*;
import hack14.gui.InputEvent;
import hack14.gui.UserFrame;
import hack14.net.RequestResponse;
import java.awt.EventQueue;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class DataController {
    Element data;
    UserFrame Frame;
    RequestResponse rr;
    Message message;
    boolean paused = false;
    int minutesElapsed = 0;
    public DataController(UserFrame frame) throws Exception {
        Frame = frame;
        rr = new RequestResponse();

    }
    public void startTimer(){
        changeData();
        Element data = message.getElement("barData").getElement("barTickData");
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                ActionListener actionListener = new ActionListener() {

                    public void actionPerformed(ActionEvent actionEvent) {
                        InputEvent tempEvent = Frame.getEvent();

                        if(tempEvent.eventType.equals("none")) {

                        }
                        else if (tempEvent.eventType.equals("play")){
                            paused = false;
                        }
                        else if (tempEvent.eventType.equals("pause")){
                            paused = true;
                        }
                        else if (tempEvent.eventType.equals("buy")){
                            System.out.println("BUYING");
                        }
                        else if (tempEvent.eventType.equals("sell")){
                            System.out.println("SELL");
                        }
                        else if (tempEvent.eventType.equals("next day")){
                            System.out.println("NEXT DAY");
                        }


                        if(!paused) {
                            advanceMinute();





                        }

                    }
                };
                Timer timer = new Timer(50, actionListener);
                timer.start();
            }
        });
    }
    public void advanceMinute(){
        if(data.numValues() < minutesElapsed) return;
        Element bar = data.getValueAsElement(minutesElapsed);
        Datetime time = bar.getElementAsDate("time");
        String date = getDate(time.toString());
        String current = getTime(time.toString());
        double open = bar.getElementAsFloat64("high");
        System.out.println(open);
        if( minutesElapsed == 0) {
            Frame.openingPrice = open;
        }
        Frame.enterData(open, minutesElapsed);
        Frame.updateDate(date);
        Frame.updateTime(current);
        minutesElapsed++;
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
    public void changeData(){
        data = message.getElement("barData").getElement("barTickData");
    }

    int timer = 0;
    public void timer(){
        Element data = message.getElement("barData").getElement("barTickData");
        int numbars = data.numValues();
        if(paused = false){

        }
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
