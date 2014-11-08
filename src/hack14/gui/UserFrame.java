package hack14.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by bean on 11/7/2014.
 */
public class UserFrame extends JFrame {
    private int windowSizeX, windowSizeY, graphSizeX, graphSizeY;
    private int graphBottom, graphLeft, pixelsInGraphX,pixelsInGraphY;
    private int minutesElapsed;
    private int MINUTES_IN_DAY = (int)(60 * 6.5);
    private BufferedImage graph;
    private Graphics graphGraphics;
    public double openingPrice;
    public UserFrame() {
        getScreenSize();
        this.setVisible(true);
        this.setUndecorated(true);
    }
    private void initGraph(){
        graph = new BufferedImage(windowSizeX,windowSizeY,BufferedImage.TYPE_INT_ARGB);
        graphGraphics = graph.createGraphics();
        drawBackground();
        this.pack();
    }
    private void getScreenSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        windowSizeX = (int) screenSize.getWidth();
        windowSizeY = (int) screenSize.getHeight();
        graphSizeX = windowSizeX / 2;
        graphSizeY = windowSizeY / 2;
    }
    public void startNewDay(){

    }
    public void graphNewStock(double openingPrice){
        openingPrice = this.openingPrice;
    }
    //converts a double of the stock price to a scale of +- 5% of opening price. returns 0 for -5, 100 for +5, etc.
    public double valTo55Graph(double value){
        double p = value / openingPrice;
        if( p > 105) return 101;//off the charts high
        if(p < 95) return -1;//off the charts low
        return (100-((p-95)*10));
    }
    public double minuteToXPercentage(int minutesElapsed){
        return minutesElapsed/MINUTES_IN_DAY;
    }
    public int convertToGraphPixelsY(double heightPercentage){
        return (int) (graphBottom + (heightPercentage/100)*pixelsInGraphY);
    }
    public int convertToGraphPixelsX(double widthPercentage){
        return (int) (graphLeft + (widthPercentage/100)*pixelsInGraphX);
    }
    public void enterData(double value, int minutesElapsed){
        graphGraphics.setColor(Color.BLUE);
        graphGraphics.fillRect(convertToGraphPixelsX(minuteToXPercentage(minutesElapsed)),convertToGraphPixelsY(valTo55Graph(value)),2, 2);
    }
    private void drawBackground(){
    }
}