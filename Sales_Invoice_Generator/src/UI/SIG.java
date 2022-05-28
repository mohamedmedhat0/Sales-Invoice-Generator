package UI;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import javax.swing.event.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SIG extends JFrame implements ActionListener, TableModelListener {


    JButton saveButton = new JButton("Save");
    private final JTable rightTable;
    int invoiceDataPtr = 0;
    int currentInvoice = 1;

    private JButton JBCreate;
    private JButton JBDelete;
    private JButton JBcancel;

    private JLabel lblStart;
    private JLabel lblNo;
    private JLabel lblDate;
    private JLabel lblName;
    private JLabel lblShowTotal;
    private JLabel lblTotal;

    private JTextField txtDate;
    private JTextField txtName;


    public SIG() {

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);

        JMenuBar menuBar;
        JMenu fileMenu;
        JMenuItem loadItem;
        JMenuItem saveItem;

        String[][] tableData =
                {
                        {"", "", "", ""},
                        {"", "", "", ""},
                        {"", "", "", ""},
                        {"", "", "", ""}
                };
        String[] tableColumn = {"No", "Date", "Customer", "Total"};


        String[][] tableData1 =
                {
                        {"", "", "", "", ""},
                        {"", "", "", "", ""},
                        {"", "", "", "", ""},
                        {"", "", "", "", ""}
                };
        String[] tableColumn1 = {"No", "Item Name", "Item Price", "Count", "Item Total"};


        List invoiceItemsList = new List();
        List invoiceDataList = new List();
        java.util.List<Itm> invoiceItemsList_1 = new ArrayList<>();
        java.util.List<Inv> invoiceDataList_1 = new ArrayList<>();


        JSplitPane splitPane = new JSplitPane();

        JPanel leftPanel = new JPanel();
        leftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Invoices Table",
                TitledBorder.LEFT,
                TitledBorder.TOP));

        JTable leftTable = new JTable(tableData, tableColumn);
        JScrollPane jScrollPane = new JScrollPane(leftTable);

        JBCreate = new JButton("Create New Invoice");
        JBDelete = new JButton("Delete Invoice");

        JPanel rightPanel = new JPanel(new GridLayout(3, 1));

        JPanel panel1 = new JPanel(new GridLayout(4, 2, 20, 20));
        JPanel panel2 = new JPanel(new GridLayout(1, 1));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Invoice Items",
                TitledBorder.LEFT,
                TitledBorder.TOP));
        JPanel panel3 = new JPanel(new FlowLayout());
        panel3.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 20));

        rightTable = new JTable(tableData1, tableColumn1);
        JScrollPane jScrollPane_1 = new JScrollPane(rightTable);

         JBcancel = new JButton("Cancel");
        lblNo = new JLabel("Invoice Number");     //
        lblDate = new JLabel("Invoice Date");
        lblName = new JLabel("Customer Name");
        lblTotal = new JLabel("Invoice Total");
        lblStart = new JLabel("1");
        txtDate = new JTextField();
        txtName = new JTextField();
        lblShowTotal = new JLabel("0.00");


        leftTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                currentInvoice = Integer.parseInt(lblStart.getText());
                int colIndex = 0;
                int rowIndex = leftTable.getSelectedRow();
                String invoiceIdSt = (String) leftTable.getValueAt(rowIndex, colIndex);

                if (invoiceIdSt.equals("")) return;
                int invoiceId = Integer.parseInt(invoiceIdSt);


                //RESET RIGHT TABLE
                for (int row = 0; row < rightTable.getRowCount(); row++) {
                    for (int column = 0; column < rightTable.getColumnCount(); column++) {
                        rightTable.setValueAt("", row, column);
                    }
                }

                int tableRow = 0;
                for (Itm item : invoiceItemsList_1) {
                    if (item.invoiceId == invoiceId) {
                        rightTable.setValueAt(String.valueOf(item.itemId), tableRow, 0);
                        rightTable.setValueAt(item.itemName, tableRow, 1);
                        rightTable.setValueAt(String.valueOf(item.itemPrice), tableRow, 2);
                        rightTable.setValueAt(String.valueOf(item.itemCount), tableRow, 3);
                        tableRow++;
                    }
                }


                //INSERT DATA TEXT FIELDS
                lblStart.setText((String) leftTable.getValueAt(rowIndex, 0));
                txtDate.setText((String) leftTable.getValueAt(rowIndex, 1));
                txtName.setText((String) leftTable.getValueAt(rowIndex, 2));
                lblShowTotal.setText((String) leftTable.getValueAt(rowIndex, 3));

            }

        });

        rightTable.getModel().addTableModelListener(e -> {

            int row = e.getFirstRow();
            int column = e.getColumn();

            if (column == 2 || column == 3) {
                String price = (String) rightTable.getValueAt(row, 2);
                if (price.equals("")) return;
                String count = (String) rightTable.getValueAt(row, 3);
                if (count.equals("")) return;
                double convertedPrice = Double.parseDouble(price);
                double convertedCount = Double.parseDouble(count);
                double total = convertedPrice * convertedCount;
                rightTable.setValueAt(String.valueOf(total), row, 4);

                double totalVal = Double.parseDouble(lblShowTotal.getText());
                totalVal = totalVal + total;
                lblShowTotal.setText(String.valueOf(totalVal));
            }

        });

        JBDelete.addActionListener(e -> {
            int rowIndex = leftTable.getSelectedRow();
            //      int colIndex = 0;
            int invoiceId = Integer.parseInt(lblStart.getText());

            //RESET RIGHT PANEL
            txtDate.setText("");
            txtName.setText("");
            lblShowTotal.setText("0.00");
            for (int row = 0; row < rightTable.getRowCount(); row++) {
                for (int column = 0; column < rightTable.getColumnCount(); column++) {
                    rightTable.setValueAt("", row, column);
                }
            }

            //CLEAR DATA FROM LEFT TABLE BASED ON ID
            leftTable.setValueAt("", rowIndex, 0);
            leftTable.setValueAt("", rowIndex, 1);
            leftTable.setValueAt("", rowIndex, 2);
            leftTable.setValueAt("", rowIndex, 3);

            //CLEAR DATA FROM, INVOICE ITEMS List BASED ON ID
            java.util.List<Itm> toRemoveItems = new ArrayList<>();
            for (Itm item : invoiceItemsList_1) {
                if (item.invoiceId == invoiceId) {
                    toRemoveItems.add(item);
                }
            }
            invoiceItemsList_1.removeAll(toRemoveItems);

            //CLEAR DATA FROM INVOICE DATA List BASED ON ID
            Inv removedItem = new Inv();
            for (Inv invoice : invoiceDataList_1) {
                if (invoice.invoiceId == invoiceId)
                    removedItem = invoice;
            }
            invoiceDataList_1.remove(removedItem);

        });

        JBCreate.addActionListener(e -> {

            //INSERT ITEMS INTO INVOICE ITEMS LIST
            for (int row = 0; row < rightTable.getRowCount(); row++) {
                if (rightTable.getValueAt(row, 0) != "") {
                    int invoiceId = Integer.parseInt(lblStart.getText());
                    int itemId = Integer.parseInt((String) rightTable.getValueAt(row, 0));
                    String itemName = (String) rightTable.getValueAt(row, 1);
                    double itemPrice = Double.parseDouble((String) rightTable.getValueAt(row, 2));
                    double itemCount = Double.parseDouble((String) rightTable.getValueAt(row, 3));
                    double itemTotal = Double.parseDouble((String) rightTable.getValueAt(row, 4));
                    Itm item = new Itm(invoiceId, itemId, itemName, itemPrice, itemCount, itemTotal);
                    invoiceItemsList_1.add(item);
                }
            }

            int invoiceId = Integer.parseInt(lblStart.getText());
            String date = txtDate.getText();
            String customerName = txtName.getText();
            double total = Double.parseDouble(lblShowTotal.getText());
            Inv invoice = new Inv(invoiceId, date, customerName, total);
            invoiceDataList_1.add(invoice);


            //UPDATE LEFT TABLE
            leftTable.setValueAt(lblStart.getText(), invoiceDataPtr, 0);
            leftTable.setValueAt(txtDate.getText(), invoiceDataPtr, 1);
            leftTable.setValueAt(txtName.getText(), invoiceDataPtr, 2);
            leftTable.setValueAt(lblShowTotal.getText(), invoiceDataPtr, 3);

            //INCREMENT INVOICE DATA POINTER
            invoiceDataPtr++;

            //INCREMENT INVOICE NUMBER
            int num = Integer.parseInt(lblStart.getText());
            num++;
            lblStart.setText(String.valueOf(num));


            //RESET RIGHT TABLE AND ALL FIELDS
            txtDate.setText("");
            txtName.setText("");
            lblShowTotal.setText("0.00");
            for (int row = 0; row < rightTable.getRowCount(); row++) {
                for (int column = 0; column < rightTable.getColumnCount(); column++) {
                    rightTable.setValueAt("", row, column);
                }
            }


        });

        saveButton.addActionListener(e -> {
            Inv updatedInvoice = new Inv();
            int rowIndex = leftTable.getSelectedRow();
            int invoiceId = Integer.parseInt(lblShowTotal.getText());
            //FETCH UPDATED ITEM
            for (Inv invoice : invoiceDataList_1) {
                if (invoice.invoiceId == invoiceId) {
                    updatedInvoice = invoice;
                    break;
                }
            }
            //ONCE ITEM FETCHED UPDATE THAT ITEM
            updatedInvoice.date = txtDate.getText();
            updatedInvoice.customerName = txtName.getText();
            updatedInvoice.total = Double.parseDouble(lblShowTotal.getText());

            //UPDATE LEFT TABLE
            leftTable.setValueAt(updatedInvoice.date, rowIndex, 1);
            leftTable.setValueAt(updatedInvoice.customerName, rowIndex, 2);
            leftTable.setValueAt(String.valueOf(updatedInvoice.total), rowIndex, 3);

            //DELETE ALL ITEMS RELATED TO THIS INVOICE ID
            java.util.List<Itm> toRemoveItems = new ArrayList<>();
            for (Itm item : invoiceItemsList_1) {
                if (item.invoiceId == invoiceId) {
                    toRemoveItems.add(item);
                }
            }
            invoiceItemsList_1.removeAll(toRemoveItems);

            //INSERT NEW ITEMS THIS INVOICE
            for (int row = 0; row < rightTable.getRowCount(); row++) {
                if (rightTable.getValueAt(row, 0) != "") {
                    int itemId = Integer.parseInt((String) rightTable.getValueAt(row, 0));
                    String itemName = (String) rightTable.getValueAt(row, 1);
                    double itemPrice = Double.parseDouble((String) rightTable.getValueAt(row, 2));
                    double itemCount = Double.parseDouble((String) rightTable.getValueAt(row, 3));
                    double itemTotal = Double.parseDouble((String) rightTable.getValueAt(row, 4));
                    Itm item = new Itm(invoiceId, itemId, itemName, itemPrice, itemCount, itemTotal);
                    invoiceItemsList_1.add(item);
                }
            }
        });

        JBcancel.addActionListener(e -> {

            //RESET RIGHT PANEL
            lblShowTotal.setText(String.valueOf(currentInvoice));
            txtDate.setText("");
            txtName.setText("");
            lblShowTotal.setText("0.00");
            for (int row = 0; row < rightTable.getRowCount(); row++) {
                for (int column = 0; column < rightTable.getColumnCount(); column++) {
                    rightTable.setValueAt("", row, column);
                }
            }

        });


        int width = 1200;
        int height = 600;
        setPreferredSize(new Dimension(width, height));
        getContentPane().setLayout(new GridLayout());
        getContentPane().add(splitPane);
        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(width / 2);
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        leftPanel.add(jScrollPane);
        leftPanel.add(JBCreate);
        leftPanel.add(JBDelete);

        rightPanel.add(panel1);
        rightPanel.add(panel2);
        rightPanel.add(panel3);

        panel1.add(lblNo);
        panel1.add(lblStart);
        panel1.add(lblDate);
        panel1.add(txtDate);
        panel1.add(lblName);
        panel1.add(txtName);
        panel1.add(lblTotal);
        panel1.add(lblShowTotal);



        panel2.add(jScrollPane_1);

        panel3.add(saveButton);
        panel3.add(JBcancel);

        pack();

        menuBar = new JMenuBar();
        loadItem = new JMenuItem("Load File", 'L');

        loadItem.addActionListener(e -> {

            //IMPORT INVOICE DATA
            importInvoiceData(invoiceDataList_1, leftTable, SIG.this);
            //IMPORT INVOICE ITEMS
            importInvoiceItems(invoiceItemsList_1, SIG.this);

        });

        loadItem.setActionCommand("L");
        saveItem = new JMenuItem("Save File", 'S');
        saveItem.addActionListener(e -> {
            exportInvoiceItems(invoiceItemsList_1, SIG.this);
            exportInvoiceData(invoiceDataList_1, SIG.this);
        });
        saveItem.setActionCommand("S");
        fileMenu = new JMenu("File");
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(saveItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocation(x - width / 2, y - height / 2);
        this.setVisible(true);
        this.setSize(width, height);


    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }

    @Override
    public void tableChanged(TableModelEvent tableModelEvent) {

    }


    public static void exportInvoiceData(java.util.List<Inv> invoiceData, SIG i) {
        JFileChooser fchoose = new JFileChooser();
        int option = fchoose.showSaveDialog(i);
        if (option == JFileChooser.APPROVE_OPTION) {
            //SELECT FILE PATH
            String name = fchoose.getSelectedFile().getName();
            String path = fchoose.getSelectedFile().getParentFile().getPath();
            String file = path + "\\" + name + ".xls";

            //CREATE FILE
            try {
                FileWriter fw = new FileWriter(file);
                fw.write("Invoice num" + "\t");
                fw.write("Date" + "\t");
                fw.write("Customer Name" + "\t");
                fw.write("Total" + "\t");
                fw.write("\n");

                //INSERT ITEMS INTO INVOICE ITEMS ARRAY TEST
                for (Inv invoice : invoiceData) {
                    fw.write(invoice.invoiceId + "\t");
                    fw.write(invoice.date + "\t");
                    fw.write(invoice.customerName + "\t");
                    fw.write(invoice.total + "\t");
                    fw.write("\n");
                }

                fw.close();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    public static void importInvoiceItems(java.util.List<Itm> invoiceItems, SIG i) {
        try {
            JFileChooser fchoose = new JFileChooser();
            int option = fchoose.showOpenDialog(i);
            if (option == JFileChooser.APPROVE_OPTION) {
                //SELECT FILE PATH
                File selectedFile = fchoose.getSelectedFile();
                String filePath = selectedFile.getAbsolutePath();
                File file = new File(filePath);
                FileInputStream fis = new FileInputStream(file);
                XSSFWorkbook wb = new XSSFWorkbook(fis);

                XSSFSheet sheet = wb.getSheetAt(0);
                FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
                String[] invoiceItemsArray = new String[6];
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue;
                    Itm item = new Itm();
                    for (Cell cell : row) {
                        CellType c = formulaEvaluator.evaluateInCell(cell).getCellType();
                        if (formulaEvaluator.evaluateInCell(cell).getCellType().name().equals("STRING")) {
                            String st = cell.getStringCellValue();
                            invoiceItemsArray[cell.getColumnIndex()] = st;
                        } else {

                            String st = String.valueOf(cell.getNumericCellValue());
                            invoiceItemsArray[cell.getColumnIndex()] = st;
                        }
                    }

                    item.invoiceId = (int) Double.parseDouble(invoiceItemsArray[0]);
                    item.itemId = (int) Double.parseDouble(invoiceItemsArray[1]);
                    item.itemName = invoiceItemsArray[2];
                    item.itemPrice = Double.parseDouble(invoiceItemsArray[3]);
                    item.itemCount = Double.parseDouble(invoiceItemsArray[4]);
                    item.total = Double.parseDouble(invoiceItemsArray[5]);
                    invoiceItems.add(item);
                }

            }
        } catch (Exception ignored) {
        }
    }


    public static void importInvoiceData(java.util.List<Inv> invoiceDataList, JTable table, SIG i) {
        try {
            JFileChooser fchoose = new JFileChooser();
            int option = fchoose.showOpenDialog(i);
            if (option == JFileChooser.APPROVE_OPTION) {

                //FIRST IMPORT IS INVOICE DATA

                File selectedFile = fchoose.getSelectedFile();
                String filePath = selectedFile.getAbsolutePath();
                File file = new File(filePath);
                FileInputStream fis = new FileInputStream(file);
                XSSFWorkbook wb = new XSSFWorkbook(fis);
                XSSFSheet sheet = wb.getSheetAt(0);
                FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
                String[] invoiceData = new String[4];
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue;
                    Inv invoice = new Inv();
                    for (Cell cell : row) {
                        CellType c = formulaEvaluator.evaluateInCell(cell).getCellType();
                        if (formulaEvaluator.evaluateInCell(cell).getCellType().name().equals("STRING")) {
                            String st = cell.getStringCellValue();
                            invoiceData[cell.getColumnIndex()] = st;
                        } else {

                            String st = String.valueOf(cell.getNumericCellValue());
                            invoiceData[cell.getColumnIndex()] = st;
                        }
                    }

                    invoice.invoiceId = (int) Double.parseDouble(invoiceData[0]);
                    invoice.date = invoiceData[1];
                    invoice.customerName = invoiceData[2];
                    invoice.total = Double.parseDouble(invoiceData[3]);
                    invoiceDataList.add(invoice);
                }
                int rowId = 0;
                for (Inv invoice : invoiceDataList) {
                    table.setValueAt(String.valueOf(invoice.invoiceId), rowId, 0);
                    table.setValueAt(invoice.date, rowId, 1);
                    table.setValueAt(invoice.customerName, rowId, 2);
                    table.setValueAt(String.valueOf(invoice.total), rowId, 3);
                    rowId++;
                }
            }
        } catch (Exception ignored) {
        }
    }

    public static void exportInvoiceItems(java.util.List<Itm> invoiceItems, SIG i) {
        JFileChooser fchoose = new JFileChooser();
        int option = fchoose.showSaveDialog(i);
        if (option == JFileChooser.APPROVE_OPTION) {
            //SELECT FILE PATH
            String name = fchoose.getSelectedFile().getName();
            String path = fchoose.getSelectedFile().getParentFile().getPath();
            String file = path + "\\" + name + ".xls";

            //CREATE FILE
            try {
                FileWriter fw = new FileWriter(file);
                fw.write("Invoice num" + "\t");
                fw.write("Item num" + "\t");
                fw.write("item Name" + "\t");
                fw.write("Item price" + "\t");
                fw.write("Item count" + "\t");
                fw.write("Total" + "\t");
                fw.write("\n");

                //INSERT ITEMS INTO INVOICE ITEMS ARRAY TEST
                for (Itm item : invoiceItems) {
                    fw.write(item.invoiceId + "\t");
                    fw.write(item.itemId + "\t");
                    fw.write(item.itemName + "\t");
                    fw.write(item.itemPrice + "\t");
                    fw.write(item.itemCount + "\t");
                    fw.write(item.total + "\t");
                    fw.write("\n");
                }

                fw.close();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }


    }

}