package hack14.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JTable table;
    private JPanel framePanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JPanel bottomLeftTop,bottomLeftBottom;
    private JPanel bottomLeftPanel,topLeftPanel;
    private JPanel topRightLeftPanel,topRightRightPanel;
    private JPanel bottomRightPanel,topRightPanel;
    private DefaultTableModel model;
    private ActionListener buyListener,sellListener,nextDayListener,playListener,pauseListener;
    private InputEvent inEvent;
    private GridBagConstraints constraints;
    public boolean hasEvent = false;
    public double openingPrice;

    public UserFrame() {
        framePanel = new JPanel();
        topPanel = new JPanel();
        bottomPanel = new JPanel();
        bottomLeftPanel = new JPanel();
        bottomLeftTop = new JPanel();
        bottomLeftBottom = new JPanel();
        topLeftPanel = new JPanel();
        bottomRightPanel = new JPanel();
        topRightPanel = new JPanel();
        topRightLeftPanel = new JPanel();
        topRightRightPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1,0));
        bottomPanel.add(bottomLeftPanel);
        bottomPanel.add(bottomRightPanel);
        framePanel.setLayout(new GridLayout(0, 1));
        framePanel.add(topPanel);
        framePanel.add(bottomPanel);

        String columnNames[] = {"Column 1", "Column 2", "Column 3"};
        String dataValues[][] =
                {
                        {"SYMBOL", "OWNED", "VALUE"},
                        {"IBM", "2", "143.3"},
                        {"MSFT", "4", "246.7"},
                };

//        DefaultTableModel model = new DefaultTableModel(data, columnNames)
//        table = new JTable( model );
        // Create a new table instance
        table = getJTable();
        table.setEnabled(false);

        topPanel.setLayout(new GridLayout(1,0));
//        topPanel.setLayout(new GridBagLayout());
        getContentPane().add(framePanel);
        inEvent = new InputEvent();
        getScreenSize();
        addKeyListener(this);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(windowSizeX, windowSizeY);
        this.setLayout(new FlowLayout());
//        GridBagConstraints constraints = new GridBagConstraints();
//        constraints.gridheight = 4;
//        constraints.gridwidth = 2;
        this.setVisible(true);
        initGraph();

    }

    //http://stackoverflow.com/questions/3179136/jtable-how-to-refresh-table-model-after-insert-delete-or-update-the-data
    private JTable getJTable() {
        JTable jTable = null;
        String[] colName = {"SYMBOL", "OWNED", "VALUE","TOTAL"};
            jTable = new JTable() {
                public boolean isCellEditable(int nRow, int nCol) {
                    return false;
                }

        };
        DefaultTableModel contactTableModel = (DefaultTableModel) jTable.getModel();
        contactTableModel.setColumnIdentifiers(colName);
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return jTable;
    }


    private void initGraph() {
        graph = new BufferedImage(graphSizeX, graphSizeY, BufferedImage.TYPE_INT_ARGB);
        graphLabel = new JLabel(new ImageIcon(graph));
        topLeftPanel.add(graphLabel);
        topRightPanel.setLayout(new GridLayout(1,0));
//        topRightPanel.setLayout(new FlowLayout());
        topRightPanel.add(topRightLeftPanel);
        topRightPanel.add(topRightRightPanel);
        topRightLeftPanel.add(table);
        topPanel.add(topLeftPanel);
        topPanel.add(topRightPanel);

//        constraints.fill = GridBagConstraints.HORIZONTAL;
//        constraints.weightx = 0.5;
//        constraints.gridx = 1;
//        constraints.gridy = 0;
        initListeners();
        JButton b1 = new JButton("Play");
        b1.addActionListener(playListener);
        JButton b2 = new JButton("Pause");
        b2.addActionListener(pauseListener);
        JButton b3 = new JButton("Next Day");
        b3.addActionListener(nextDayListener);
        JButton b4 = new JButton("Buy");
        b2.addActionListener(buyListener);
        JButton b5 = new JButton("Sell");
        b3.addActionListener(sellListener);
        bottomLeftPanel.add(b1);
        bottomLeftPanel.add(b2);
        bottomLeftPanel.add(b3);
        bottomRightPanel.add(b4);
        bottomRightPanel.add(b5);
//        table.setModel(new DefaultTableModel());
        model = (DefaultTableModel) table.getModel();
        model.addRow(new Object[]{"SYMBOL", "OWNED", "VALUE","TOTAL"});
        model.addRow(new Object[]{"AAPL", "8", "155.4","2"});
        model.addRow(new Object[]{"MSFT", "2", "234","5"});
        model.addRow(new Object[]{"GOOG", "1", "142","1"});
        model.removeRow(1);
        model.fireTableDataChanged();//call this whenever data changes in table
        graphGraphics = graph.createGraphics();
        draw55Graph();
        this.pack();
    }

    private void draw55Graph(){
        graphGraphics.setColor(Color.BLACK);
        graphGraphics.fillRect(0, 0, graphSizeX - 1, graphSizeY - 1);
        graphGraphics.setColor(Color.YELLOW);
        graphGraphics.drawString("+5%",2,10);
        graphGraphics.drawString("-5%",2,graphSizeY-5);
        graphGraphics.setColor(Color.DARK_GRAY);

        Graphics2D g2 = (Graphics2D) graphGraphics;
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(0,graphSizeY/2,graphSizeX,graphSizeY/2);
        g2.setStroke(new BasicStroke(1));


    }
    private void getScreenSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        windowSizeX = (int) screenSize.getWidth();
        windowSizeY = (int) screenSize.getHeight();
        graphSizeX = (int)(windowSizeX*.5);
        graphSizeY = (int)(windowSizeY*.5);
    }

    public void startNewDay() {
        //reset graph
        graphGraphics.setColor(Color.BLACK);
        graphGraphics.fillRect(0, 0, graphSizeX, graphSizeY);
        graphLabel.imageUpdate(graph, 0, 0, 0, windowSizeX, windowSizeY);
        this.repaint();
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
        graphGraphics.setColor(Color.GREEN);
        graphGraphics.fillRect(curX, curY, 2, 2);
        graphGraphics.drawLine(lastX, lastY, curX, curY);
        graphLabel.imageUpdate(graph, 0, 0, 0, windowSizeX, windowSizeY);
        this.repaint();
        lastX = curX;
        lastY = curY;
    }

    public InputEvent getEvent() {
        if (!hasEvent) return null;
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
            String s = JOptionPane.showInputDialog(null, "which stock");
            inEvent.eventType = "buy";
            inEvent.field1 = s;
            System.out.printf("buying the stock'%s'.\n", s);
            String s2 = JOptionPane.showInputDialog(null, "how many");
            inEvent.field2 = s2;
            System.out.printf("quantity is '%s'.\n", s2);
            hasEvent = true;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

//    @Override
//    public void paint(Graphics g){
//        g.drawImage(graph, 0, 0, null);
//    }
private void initListeners(){
    buyListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            if(hasEvent) return;
            String s = JOptionPane.showInputDialog(null, "which stock");
            inEvent.eventType = "buy";
            inEvent.field1 = s;
            System.out.printf("buying the stock'%s'.\n", s);
            String s2 = JOptionPane.showInputDialog(null, "how many");
            inEvent.field2 = s2;
            System.out.printf("quantity is '%s'.\n", s2);
            hasEvent = true;
        }
    };
    sellListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            if(hasEvent) return;
            String s = JOptionPane.showInputDialog(null, "which stock");
            inEvent.eventType = "sell";
            inEvent.field1 = s;
            System.out.printf("selling the stock'%s'.\n", s);
            String s2 = JOptionPane.showInputDialog(null, "how many");
            inEvent.field2 = s2;
            System.out.printf("quantity is '%s'.\n", s2);
            hasEvent = true;
        }
    };
    playListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            if(hasEvent) return;
            inEvent.eventType = "play";
            inEvent.field1 = "";
            inEvent.field2 = "";
            hasEvent = true;
        }
    };
    pauseListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            if(hasEvent) return;
            inEvent.eventType = "pause";
            inEvent.field1 = "";
            inEvent.field2 = "";
            hasEvent = true;
        }
    };
    nextDayListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            if(hasEvent) return;
            inEvent.eventType = "next day";
            inEvent.field1 = "";
            inEvent.field2 = "";
            hasEvent = true;
        }
    };

}
}