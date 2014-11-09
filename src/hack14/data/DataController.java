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
import java.util.ArrayList;

public class DataController {
    ArrayList<OwnedStock> myStock;
    Element data;
    UserFrame Frame;
    double open;
    int currentMonth = 11;
    int currentDay = 4;
    int liquidCash = 100000;
    String currentSecurity = "INTC US Equity";
    RequestResponse rr;
    Message message;
    boolean paused = false;
    int minutesElapsed = 0;
    public DataController(UserFrame frame) throws Exception {
        myStock = new ArrayList<OwnedStock>();
        Frame = frame;
        rr = new RequestResponse();

    }
    public void startTimer(){
        changeData();
        //Element data = message.getElement("barData").getElement("barTickData");
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
                            buyStock(Integer.parseInt(tempEvent.field1));
                        }
                        else if (tempEvent.eventType.equals("sell")){
                            System.out.println("SELL");
                        }
                        else if (tempEvent.eventType.equals("next day")){
                            System.out.println("NEXT DAY");
                            advanceDay();
                        }
                        else if (tempEvent.eventType.equals("change stock")){
                            changeStock(tempEvent.field1);
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

    public void buyStock(int quantity){
        if(quantity * open > liquidCash) {
            return;
        }
        OwnedStock os = new OwnedStock();
        os.s1 = currentSecurity;
        os.s2 = Integer.toString(quantity + stockOwned());
        os.s3 = Double.toString(open);
        os.s4 = Double.toString(open*Integer.parseInt(os.s2));
        myStock.add(os);
        Frame.insertTableEntry(myStock.size(),os.s1,os.s2,os.s3,os.s4);
    }

    private int stockOwned(){
        for (OwnedStock s : myStock) {
            if (s.s1.equals(currentSecurity)){
                return Integer.parseInt(s.s2);
            }
        }
        return 0;
    }
    public void changeStock(String server){

        try {
            rr = new RequestResponse();
            currentSecurity = server;
            getData(currentSecurity + " US Equity",currentMonth,currentDay);

            changeData();
            Frame.resetGraph();
            for(int i = 0; i < minutesElapsed; ++i){
                Element bar = data.getValueAsElement(i);
                Datetime time = bar.getElementAsDate("time");
                String date = getDate(time.toString());
                String current = getTime(time.toString());
                open = bar.getElementAsFloat64("high");
                if( i == 0) {
                    Frame.openingPrice = open;
                }
                Frame.updateCurrentStock(currentSecurity);
                Frame.enterData(open, i);
                Frame.updateDate(date);
                Frame.updateTime(current);
            }
        }//t
        catch (Exception e){
            //day+1
        }

    }
    public void advanceDay() {
        minutesElapsed = 0;
        try {
            rr = new RequestResponse();
            getData("MSFT US Equity",11,7);
            changeData();
        }//t
        catch (Exception e){
        }
        Frame.resetGraph();

    }
    public void advanceMinute(){
        if(data.numValues() <= minutesElapsed) {
            advanceDay();
            return;
        }
        Element bar = data.getValueAsElement(minutesElapsed);
        Datetime time = bar.getElementAsDate("time");
        String date = getDate(time.toString());
        String current = getTime(time.toString());
        open = bar.getElementAsFloat64("open");
        System.out.println(open);
        if( minutesElapsed == 0) {
            Frame.openingPrice = open;
        }
        Frame.updateCurrentStock(currentSecurity);
        Frame.enterData(open, minutesElapsed);
        Frame.updateStockPrice(open);
        Frame.updateDate(date);
        Frame.updateTime(current);
        minutesElapsed++;
    }
    public void getData(String security, int month,int day) throws Exception {
        rr.getSecurityData(security, month, day);
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
