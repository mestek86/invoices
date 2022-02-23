package invoices;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OneInvoiceView {
    public OneInvoiceView() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        panel.add(topPanel);

        JLabel invoiceNumberLabel = new JLabel("Invoice Number");
        invoiceNumberField = new JTextField();
        invoiceNumberField.setEditable(false);
        addLabelToPanel(topPanel, 0, 0, invoiceNumberLabel);
        addFieldToPanel(topPanel, 1, 0, invoiceNumberField);

        JLabel invoiceDateLabel = new JLabel("Invoice Date");
        invoiceDateField = new JTextField();
        addLabelToPanel(topPanel, 0, 1, invoiceDateLabel);
        addFieldToPanel(topPanel, 1, 1, invoiceDateField);

        JLabel invoiceCustomerNameLabel = new JLabel("Customer Name");
        invoiceCustomerNameField = new JTextField();
        addLabelToPanel(topPanel, 0, 2, invoiceCustomerNameLabel);
        addFieldToPanel(topPanel, 1, 2, invoiceCustomerNameField);

        JLabel invoiceTotalLabel = new JLabel("Invoice Total");
        invoiceTotalField = new JTextField();
        invoiceTotalField.setEditable(false);
        addLabelToPanel(topPanel, 0, 3, invoiceTotalLabel);
        addFieldToPanel(topPanel, 1, 3, invoiceTotalField);

        JLabel tableHeader = new JLabel("Invoice Items");
        panel.add(tableHeader);

        itemsTableModel = new ItemsTableModel();
        itemsTableModel.addTableModelListener((TableModelEvent e) -> {
            invoiceTotalField.setText(String.valueOf(invoice.getTotalAmount()));
        });
        itemsTable = new JTable();
        itemsTable.setModel(itemsTableModel);
        itemsTable.createDefaultColumnsFromModel();
        itemsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemsTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    int column = itemsTable.getSelectedColumn();

                    if (column == 0) {
                        InvoiceItem item = new InvoiceItem();
                        item.setInvoiceNumber(invoice.getNumber());
                        itemsTableModel.addItem(item);
                    }
                }
            }
        });
        panel.add(new JScrollPane(itemsTable));

        JPanel buttonsPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener((ActionEvent e) -> {;
            if (itemsTable.isEditing()) {
                itemsTable.getCellEditor().stopCellEditing();
            }
            invoice.setCustomerName(invoiceCustomerNameField.getText());
            invoice.setDate(invoiceDateField.getText());
            onSaveListener.onAction(invoice);
        });
        buttonsPanel.add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener((ActionEvent e) -> onCancelListener.run());
        buttonsPanel.add(cancelButton);
        panel.add(buttonsPanel);
    }

    public JPanel toJPanel() {
        return panel;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;

        invoiceNumberField.setText(String.valueOf(invoice.getNumber()));
        invoiceDateField.setText(invoice.getDate());
        invoiceCustomerNameField.setText(invoice.getCustomerName());
        invoiceTotalField.setText(String.valueOf(invoice.getTotalAmount()));

        if (invoice.getItems().size() == 0) {
            InvoiceItem item = new InvoiceItem();
            item.setInvoiceNumber(invoice.getNumber());
            invoice.getItems().add(item);
        }

        if (itemsTable.isEditing()) {
            itemsTable.getCellEditor().stopCellEditing();
        }

        itemsTableModel.setItems(invoice.getItems());
    }

    public void setOnCancelListener(Runnable onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    public void setOnSaveListener(InvoiceListener onSaveListener) {
        this.onSaveListener = onSaveListener;
    }

    private void addLabelToPanel(JPanel panel, int gridx, int gridy, JLabel label) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.weightx = 0.0;
        panel.add(label, constraints);
    }

    private void addFieldToPanel(JPanel panel, int gridx, int gridy, JTextField field) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.weightx = 1.0;
        panel.add(field, constraints);
    }

    private Invoice invoice;
    private JPanel panel;
    private JTextField invoiceNumberField;
    private JTextField invoiceDateField;
    private JTextField invoiceCustomerNameField;
    private JTextField invoiceTotalField;
    private ItemsTableModel itemsTableModel;
    private JTable itemsTable;
    private Runnable onCancelListener;
    private InvoiceListener onSaveListener;
}
