package hack14.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

/**
 * Created by bean on 11/7/2014.
 */
public class UserFrame extends JFrame implements KeyListener {
    private int windowSizeX, windowSizeY, graphSizeX, graphSizeY;
    private int graphBottom, graphLeft;
    private int minutesElapsed;
    private int MINUTES_IN_DAY = (int) (60 * 6.5);
    private int lastX, lastY;//x and y coordiates for most recently graphed point
    private BufferedImage graph;
    private Graphics graphGraphics;
    private JLabel graphLabel;
    private	JTable table;
    private InputEvent inEvent;
    public boolean hasEvent = false;
    public double openingPrice;

    public UserFrame() {
        inEvent = new InputEvent();
        getScreenSize();
        addKeyListener(this);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(windowSizeX, windowSizeY);
        this.setLayout(new FlowLayout());
        this.setVisible(true);
        initGraph();

    }

    private void initGraph() {
        graph = new BufferedImage(windowSizeX, windowSizeY, BufferedImage.TYPE_INT_ARGB);
        graphLabel = new JLabel(new ImageIcon(graph));
        this.getContentPane().add(graphLabel);
//        this.getContentPane().add(new JButton());
        graphGraphics = graph.createGraphics();
        graphGraphics.setColor(Color.BLUE);
        graphGraphics.drawRect(0, 0, graphSizeX, graphSizeY);
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

    public void startNewDay() {

    }

    public void graphNewStock(double openingPrice) {
        openingPrice = this.openingPrice;
    }

    //converts a double of the stock price to a scale of +- 5% of opening price. returns 0 for -5, 100 for +5, etc.
    public double valTo55Graph(double value) {

        double p = 100 * value / openingPrice;
//        System.out.printf(" p is %f.\n", p);
        if (p > 105) return 101;//off the charts high
        if (p < 95) return -1;//off the charts low
        return (100 - ((p - 95) * 10));
    }

    public double minuteToXPercentage(int minutesElapsed) {
        if (minutesElapsed == 0) return 0;
//        System.out.printf("min elapsed %d, %d.\n", minutesElapsed,MINUTES_IN_DAY);
        return ((float) minutesElapsed * 100.0f / (float) MINUTES_IN_DAY);
    }

    public int convertToGraphPixelsY(double heightPercentage) {
//        System.out.printf("width percentage is %f.\n", heightPercentage);
        return (int) (graphBottom + (heightPercentage / 100) * graphSizeY);
    }

    public int convertToGraphPixelsX(double widthPercentage) {
        System.out.printf("width percentage is %f.\n", widthPercentage);
        return (int) (graphLeft + (widthPercentage / 100.0f) * (float) graphSizeX);
    }

    public void enterData(double value, int minutesElapsed) {
        graphGraphics.setColor(Color.BLUE);
        int curX = convertToGraphPixelsX(minuteToXPercentage(minutesElapsed));
        int curY = convertToGraphPixelsY(valTo55Graph(value));

        System.out.printf("drawing at %d, %d\n", curX, curY);
        graphGraphics.fillRect(curX, curY, 2, 2);
        graphGraphics.drawLine(lastX, lastY, curX, curY);
        graphLabel.imageUpdate(graph, 0, 0, 0, windowSizeX, windowSizeY);
        this.repaint();
        lastX = curX;
        lastY = curY;
    }

    public InputEvent getEvent() {
        if(!hasEvent) return null;
        hasEvent = false;
        return inEvent;

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
        if (hasEvent) return;
        else if (e.getKeyCode() == KeyEvent.VK_B) {
            String s = JOptionPane.showInputDialog(this, "which stock");
            inEvent.eventType = "buy";
            inEvent.field1 = s;
            System.out.printf("buying the stock'%s'.\n", s);
            String s2 = JOptionPane.showInputDialog(this, "how much");
            inEvent.field2 = s2;
            System.out.printf("quantity is '%s'.\n", s2);
            hasEvent = true;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void drawBackground() {
    }
//    @Override
//    public void paint(Graphics g){
//        g.drawImage(graph, 0, 0, null);
//    }
}