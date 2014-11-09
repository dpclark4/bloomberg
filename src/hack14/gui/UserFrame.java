package hack14.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class UserFrame extends JFrame implements KeyListener {
    private int windowSizeX, windowSizeY, graphSizeX, graphSizeY;
    private int graphBottom, graphLeft;
    private int minutesElapsed;
    private int MINUTES_IN_DAY = (int) (60 * 6.5);
    private int lastX, lastY;//x and y coordinates for most recently graphed point
    private BufferedImage graph;
    private Graphics graphGraphics;
    private JLabel graphLabel;
    private JButton currentStock,stockPrice;
    private JTable table;
    private JPanel framePanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JPanel bottomLeftTopPanel,bottomLeftBottomPanel;
    private JPanel bottomLeftPanel,topLeftPanel;
    private JPanel topRightLeftPanel,topRightRightPanel;
    private JPanel bottomRightPanel,topRightPanel;
    private JLabel dateLabel,timeLabel,moneyLabel;
    private DefaultTableModel model;
    private ActionListener buyListener,sellListener,nextDayListener,playListener,pauseListener;
    private InputEvent inEvent;
    private GridBagConstraints constraints;
    public boolean hasEvent = false;
    public double openingPrice;
    public int graphVariance = 5;
//stuf
    public UserFrame() {
        framePanel = new JPanel();
        topPanel = new JPanel();
        bottomPanel = new JPanel();
        bottomLeftPanel = new JPanel();
        bottomLeftTopPanel = new JPanel();
        bottomLeftBottomPanel = new JPanel();
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
                        {"SYMBOL", "OWNED" , "VALUE"},
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
        inEvent.eventType = "";
        inEvent.field1 = "";
        inEvent.field2 = "";
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
        b1.setPreferredSize((new Dimension(100,50)));
        JButton b2 = new JButton("Pause");
        b2.addActionListener(pauseListener);
        b2.setPreferredSize((new Dimension(100,50)));
        JButton b3 = new JButton("Next Day");
        b3.addActionListener(nextDayListener);
        b3.setPreferredSize((new Dimension(100,50)));
        JButton b4 = new JButton("Buy");
        b4.addActionListener(buyListener);
        b4.setPreferredSize((new Dimension(100,50)));
        JButton b5 = new JButton("Sell");
        b5.addActionListener(sellListener);
        b5.setPreferredSize((new Dimension(100,50)));
        bottomLeftPanel.setLayout(new GridLayout(0,1));
        bottomLeftPanel.add(bottomLeftTopPanel);
        bottomLeftPanel.add(bottomLeftBottomPanel);
        currentStock = new JButton("STOCK:");
        currentStock.setPreferredSize(new Dimension(100,50));
        currentStock.setFont(new Font("Serif", Font.PLAIN, 16));
        stockPrice = new JButton("VALUE:");
        stockPrice.setPreferredSize(new Dimension(100,50));
        stockPrice.setFont(new Font("Serif", Font.PLAIN, 16));
        bottomLeftTopPanel.add(currentStock);
        bottomLeftTopPanel.add(stockPrice);
        bottomLeftTopPanel.add(b1);
        bottomLeftTopPanel.add(b2);
        bottomLeftTopPanel.add(b3);
        bottomRightPanel.add(b4);
        bottomRightPanel.add(b5);
        dateLabel = new JLabel("Date:");
        dateLabel.setPreferredSize(new Dimension(300,75));
        dateLabel.setFont(new Font("Serif", Font.PLAIN, 26));
        timeLabel = new JLabel("Time:");
        timeLabel.setPreferredSize(new Dimension(300,75));
        timeLabel.setFont(new Font("Serif", Font.PLAIN, 26));
        moneyLabel = new JLabel("$:");
        moneyLabel.setPreferredSize(new Dimension(300,75));
        moneyLabel.setFont(new Font("Serif", Font.PLAIN, 26));
        bottomRightPanel.add(moneyLabel);
        bottomLeftBottomPanel.setLayout(new GridLayout(1, 0));

        bottomLeftBottomPanel.add(dateLabel);
        bottomLeftBottomPanel.add(timeLabel);
//        table.setModel(new DefaultTableModel());
        model = (DefaultTableModel) table.getModel();
        model.addRow(new Object[]{"SYMBOL", "OWNED", "VALUE","TOTAL"});
        model.addRow(new Object[]{"AAPL", "8", "155.4","2"});
        model.addRow(new Object[]{"MSFT", "2", "234","5"});
        model.addRow(new Object[]{"GOOG", "1", "142","1"});
//        this.removeTableEntry(1);
//        this.insertTableEntry(1,"a","b","c","d");
        model.fireTableDataChanged();//call this whenever data changes in table
        graphGraphics = graph.createGraphics();
        draw55Graph();
        this.pack();
    }
    public void updateDate(String date){
        dateLabel.setText("DATE: "+ date);
    }
    public void updateTime(String time){
        timeLabel.setText("TIME: " + time);
    }
    public void updateCash(String money){
        timeLabel.setText("$: " + money);
    }
    public void updateCurrentStock(String ID){
        timeLabel.setText("STOCK: " + ID);
    }
    public void updateStockPrice(String money){
        timeLabel.setText("VALUE: " + money);
    }
    public void removeTableEntry(int index){
        model.removeRow(index);
    }
    public void insertTableEntry(int index, String s1,String s2,String s3,String s4){
        model.insertRow(index,new Object[]{s1, s2, s3, s4});
    }
    private void draw55Graph(){
        graphGraphics.setColor(Color.BLACK);
        graphGraphics.fillRect(0, 0, graphSizeX - 1, graphSizeY - 1);
        graphGraphics.setColor(Color.YELLOW);
        graphGraphics.drawString("+"+graphVariance+"%",2,10);
        graphGraphics.drawString("-"+graphVariance+"%",2,graphSizeY-5);
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

    public void resetGraph() {
        //reset graph
        graphGraphics.setColor(Color.BLACK);
        graphGraphics.fillRect(0, 0, graphSizeX, graphSizeY);
        graphLabel.imageUpdate(graph, 0, 0, 0, windowSizeX, windowSizeY);
        draw55Graph();
        this.repaint();
    }

    public void graphNewStock(double openingPrice) {
        openingPrice = this.openingPrice;
    }

    //converts a double of the stock price to a scale of +- 5% of opening price. returns 0 for -5, 100 for +5, etc.
    public double valTo55Graph(double value) {
        double maxheight = (1.00 + (float)graphVariance*.01)*openingPrice;
        double minheight = (1.00 - (float)graphVariance*.01)*openingPrice;
        double p = (value - minheight)/(maxheight-minheight);
        return p;

//        double p = 100 * value / openingPrice;
//        if (p > 100+graphVariance) return 101;//off the charts high
//        if (p < 100-graphVariance) return -1;//off the charts low
//        return (100 - ((p - (100-graphVariance)) * 10));
    }

    public double minuteToXPercentage(int minutesElapsed) {
        if (minutesElapsed == 0) return 0;
//        System.out.printf("min elapsed %d, %d.\n", minutesElapsed,MINUTES_IN_DAY);
        return ((float) minutesElapsed * 100.0f / (float) MINUTES_IN_DAY);
    }

    public int convertToGraphPixelsY(double heightPercentage) {
//        System.out.printf("width percentage is %f.\n", heightPercentage);
        return (int) ((float)graphSizeY - (heightPercentage) * (float) graphSizeY);
    }

    public int convertToGraphPixelsX(double widthPercentage) {
        //System.out.printf("width percentage is %f.\n", widthPercentage);
        return (int) (graphLeft + (widthPercentage / 100.0f) * (float) graphSizeX);
    }

    public void enterData(double value, int minutesElapsed) {
        graphGraphics.setColor(Color.BLUE);
        int curX = convertToGraphPixelsX(minuteToXPercentage(minutesElapsed));
        int curY = convertToGraphPixelsY(valTo55Graph(value));

        //System.out.printf("drawing at %d, %d\n", curX, curY);
        if(value >= openingPrice) {
            graphGraphics.setColor(Color.GREEN);
        }
        else {
            graphGraphics.setColor(Color.RED);
        }
        graphGraphics.fillRect(curX, curY, 4, 4);
        if(curX > lastX) {
            graphGraphics.drawLine(lastX, lastY, curX, curY);
        }
        graphLabel.imageUpdate(graph, 0, 0, 0, windowSizeX, windowSizeY);
        this.repaint();
        lastX = curX;
        lastY = curY;
    }

    public InputEvent getEvent() {
        if (!hasEvent){
            inEvent.eventType = "none";
        }
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