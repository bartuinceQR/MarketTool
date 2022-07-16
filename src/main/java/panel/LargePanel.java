package main.java.panel;

import main.java.extra.FieldBuilder;
import org.w3c.dom.Text;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LargePanel extends JPanel {

    JFrame mainFrame;

    JPanel topPanel;
    JPanel midPanel;
    JPanel bottomPanel;

    JTextField nameField;
    JTextField phoneField;

    JTextField postalField;
    JTextField provinceField;
    JTextField cityField;

    JTextField deliveryField;

    JTextField dateField;

    JTextField creditField;
    JTextField validationField;

    JTextField catalogNumberField;
    JSpinner catalogQtyField;
    JTextField catalogCostField;
    JTextField catalogTotalField;

    JTextField balanceOwingField;

    JButton catalogItemButton;
    JButton triggerButton;

    public LargePanel(int width, int height){
        mainFrame = new JFrame();
        mainFrame.setSize(width, height);
        mainFrame.setLayout(new GridLayout(3,1));

        SetTopPanel();
        SetMidPanel();
        SetBottomPanel();


        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);

        CalculateTotal();
    }

    private void SetTopPanel(){
        topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(5,1));

        TitledBorder border = new TitledBorder("Purchaser");
        border.setTitleJustification(TitledBorder.LEFT);
        border.setTitlePosition(TitledBorder.TOP);
        border.setTitleFont(new Font("Verdana", 0, 24));

        topPanel.setBorder(border);

        JPanel row1 = new JPanel();
        this.nameField = AddToRow(row1, "Name:");
        this.phoneField = AddToRow(row1, "Phone:");
        topPanel.add(row1);

        JPanel row2 = new JPanel();
        this.postalField = AddToRow(row2, "Postal Code:", 20);
        this.provinceField = AddToRow( row2, "Province:", 15);
        this.cityField = AddToRow( row2, "City:", 15);
        topPanel.add(row2);

        JPanel row3 = new JPanel();
        this.deliveryField = AddToRow(row3, "Delivery Address:", 50);
        topPanel.add(row3);

        JPanel row4 = new JPanel();
        this.dateField = AddToRow(row4, "Today's Date", 50);
        topPanel.add(row4);

        JPanel row5 = new JPanel();
        this.creditField = AddToRow(row5, "Credit Card No.", 30);
        this.validationField = AddToRow(row5, "(For Dept. Use) Validation Id.", 15);
        topPanel.add(row5);

        mainFrame.add(topPanel);
    }

    public void SetMidPanel(){
        midPanel = new JPanel();
        midPanel.setLayout(new GridLayout(1,1));

        TitledBorder border = new TitledBorder("CatalogItem");
        border.setTitleJustification(TitledBorder.LEFT);
        border.setTitlePosition(TitledBorder.TOP);
        border.setTitleFont(new Font("Verdana", 0, 24));

        midPanel.setBorder(border);

        JPanel row1 = new JPanel();
        this.catalogNumberField = AddToRow(row1, "Number:", 10);
        this.catalogNumberField = AddNumberFilter(this.catalogNumberField);
        this.catalogNumberField.setText("1");

        JLabel qty = new JLabel("Quantity");
        SpinnerNumberModel model1 = new SpinnerNumberModel(1, 0, 100, 1);
        this.catalogQtyField = new JSpinner(model1);
        this.catalogQtyField.addChangeListener(e -> CalculateTotal());
        row1.add(qty);
        row1.add(catalogQtyField);

        this.catalogCostField = AddToRow(row1, "Cost/item:", 10);
        this.catalogCostField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                CalculateTotal();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                CalculateTotal();
            }

            public void changedUpdate(DocumentEvent e) {
                CalculateTotal();
            }
        });
        this.catalogCostField.setText("10");
        row1.add(catalogCostField);

        this.catalogTotalField = AddToRow(row1, "Total:", 10);
        this.catalogTotalField.setEditable(false);
        row1.add(catalogTotalField);

        midPanel.add(row1);


        mainFrame.add(midPanel);
    }


    private void SetBottomPanel(){
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1,2));

        JPanel row1 = new JPanel();
        this.balanceOwingField = AddToRow(row1, "Balance Owing:", 20);
        this.balanceOwingField.setText("0");
        this.balanceOwingField.setEditable(false);

        JPanel gridButtons = new JPanel();
        gridButtons.setLayout(new GridLayout(2,1));
        catalogItemButton = new JButton("Next Catalog Item (PF8)");

        catalogItemButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                FullClear();
                CalculateTotal();
            }
        });


        triggerButton = new JButton("Trigger Invoice (PF5)");
        triggerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                UpdateBalance();
                JOptionPane.showMessageDialog(mainFrame, "Invoice triggered!");
            }
        });
        gridButtons.add(catalogItemButton);
        gridButtons.add(triggerButton);
        row1.add(gridButtons);

        bottomPanel.add(row1);

        mainFrame.add(bottomPanel);
    }


    private JTextField AddToRow(JPanel row, String label){
        row.add(new JLabel(label));
        JTextField field = new FieldBuilder().setName("").build();
        row.add(field);
        return field;
    }

    private JTextField AddToRow(JPanel row, String label, int columns){
        row.add(new JLabel(label));
        JTextField field = new FieldBuilder().setName("").setColumns(columns).build();
        row.add(field);
        return field;
    }

    //https://stackoverflow.com/questions/20541230/allow-only-numbers-in-jtextfield
    private JTextField AddNumberFilter(JTextField field) {
        JTextField newField = field;
        ((AbstractDocument)newField.getDocument()).setDocumentFilter(new DocumentFilter(){
            Pattern regEx = Pattern.compile("\\d*");

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException, BadLocationException {
                Matcher matcher = regEx.matcher(text);
                if(!matcher.matches()){
                    return;
                }
                super.replace(fb, offset, length, text, attrs);
            }
        });

        return newField;
    }

    private void FullClear(){
         nameField.setText("");
         phoneField.setText("");

         postalField.setText("");
         provinceField.setText("");
         cityField.setText("");

         deliveryField.setText("");

         dateField.setText("");

         creditField.setText("");
         validationField.setText("");

         catalogNumberField.setText(String.valueOf((Integer.parseInt(catalogNumberField.getText()) + 1)));
         catalogQtyField.setValue(1);
         catalogCostField.setText(String.valueOf(Math.floor(Math.random() * 100)));
         CalculateTotal();

         //balanceOwingField.setText("");
    }

    private void CalculateTotal(){
        if (catalogTotalField == null) return;
        double cost = 0.0;
        String visibleCost = catalogCostField.getText();
        if (!visibleCost.isEmpty())
            cost = Double.parseDouble(visibleCost);
        int qty = (int)(catalogQtyField.getValue());
        double total = cost * qty;
        catalogTotalField.setText(String.valueOf(total));
    }

    private void UpdateBalance(){
        double oldOwing = 0.0;
        if (!balanceOwingField.getText().isEmpty())
            oldOwing = Double.parseDouble(balanceOwingField.getText());
        if (!catalogTotalField.getText().isEmpty())
            oldOwing += Double.parseDouble(catalogTotalField.getText());

        balanceOwingField.setText(String.valueOf(oldOwing));
    }

}
