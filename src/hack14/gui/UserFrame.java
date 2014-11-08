package hack14.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
    private JPanel framePanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private DefaultTableModel model;
    private InputEvent inEvent;
    private GridBagConstraints constraints;
    public boolean hasEvent = false;
    public double openingPrice;

    public UserFrame() {
        framePanel = new JPanel();
        topPanel = new JPanel();
        // Create columns names
        String columnNames[] = { "Column 1", "Column 2", "Column 3" };
        // Create some data
        String dataValues[][] =
                {
                        { "SYMBOL", "OWNED", "VALUE" },
                        { "IBM", "2", "143.3" },
                        { "MSFT", "4", "246.7" },

                };

//        DefaultTableModel model = new DefaultTableModel(data, columnNames)
//        table = new JTable( model );
        // Create a new table instance
        table = getJTable();
        table.setEnabled(false);

        topPanel.setLayout( new FlowLayout() );
        getContentPane().add( topPanel );
        inEvent = new InputEvent();
        getScreenSize();
        addKeyListener(this);
//        this.setUndecorated(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(windowSizeX, windowSizeY);
        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridheight = 4;
        constraints.gridwidth = 2;
        this.setVisible(true);
        initGraph();

    }

    //http://stackoverflow.com/questions/3179136/jtable-how-to-refresh-table-model-after-insert-delete-or-update-the-data
    private JTable getJTable() {
        JTable jTable = null;
        String[] colName = { "SYMBOL", "OWNED", "VALUE" };
        if (jTable == null) {
            jTable = new JTable() {
                public boolean isCellEditable(int nRow, int nCol) {
                    return false;
                }
            };
        }
        DefaultTableModel contactTableModel = (DefaultTableModel) jTable
                .getModel();
        contactTableModel.setColumnIdentifiers(colName);
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return jTable;
    }


    private void initGraph() {
        graph = new BufferedImage(graphSizeX, graphSizeY, BufferedImage.TYPE_INT_ARGB);
        graphLabel = new JLabel(new ImageIcon(graph));
        topPanel.add(graphLabel);
        topPanel.add(table);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.5;
        constraints.gridx = 1;
        constraints.gridy = 0;
        topPanel.add(new JButton(),constraints);
        topPanel.add(new JButton(),constraints);
        topPanel.add(new JButton(),constraints);
//        table.setModel(new DefaultTableModel());
        model = (DefaultTableModel) table.getModel();
        model.addRow(new Object[]{"AAPL", "8", "155.4"});
        model.addRow(new Object[]{"MSFT", "2", "234"});
        model.addRow(new Object[]{"GOOG", "1", "142"});
        model.removeRow(1);
        model.fireTableDataChanged();//call this whenever data changes in table
        graphGraphics = graph.createGraphics();
        graphGraphics.setColor(Color.BLACK);
        graphGraphics.fillRect(0, 0, graphSizeX-1, graphSizeY-1);
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
        graphGraphics.setColor(Color.GREEN);
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