package invoices;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;

public class InvoicesView {
    public InvoicesView(InvoicesTableModel invoicesTableModel) {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel header = new JLabel("Invoices Table");
        panel.add(header);

        this.invoicesTableModel = invoicesTableModel;
        invoicesTable = new JTable();
        invoicesTable.setModel(invoicesTableModel);
        invoicesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        invoicesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                int row = invoicesTable.getSelectedRow();
                if (row != -1) {
                    invoiceClickListener.onAction(invoicesTableModel.getInvoice(row));
                }
            }
        });
        panel.add(new JScrollPane(invoicesTable));

        JPanel buttonsPanel = new JPanel();
        JButton createInvoiceButton = new JButton("Create New Invoice");
        createInvoiceButton.addActionListener((ActionEvent e) -> {
            InvoiceNumber.set(InvoiceNumber.get() + 1);
            Invoice newInvoice = new Invoice(InvoiceNumber.get());
            invoiceAddListener.onAction(newInvoice);
        });
        buttonsPanel.add(createInvoiceButton);

        JButton deleteInvoiceButton = new JButton("Delete Invoice");
        deleteInvoiceButton.addActionListener((ActionEvent e) -> {
            int row = invoicesTable.getSelectedRow();
            if (row != -1) {
                invoiceRemoveListener.onAction(invoicesTableModel.getInvoice(row));
                invoicesTableModel.removeInvoice(row);
            }
        });
        buttonsPanel.add(deleteInvoiceButton);
        
        panel.add(buttonsPanel);
    }

    public JPanel toJPanel() {
        return panel;
    }

    public void setInvoiceClickListener(InvoiceListener invoiceClickListener) {
        this.invoiceClickListener = invoiceClickListener;
    }

    public void setInvoiceRemoveListener(InvoiceListener invoiceRemoveListener) {
        this.invoiceRemoveListener = invoiceRemoveListener;
    }

    public void setInvoiceAddListener(InvoiceListener invoiceAddListener) {
        this.invoiceAddListener = invoiceAddListener;
    }

    private JPanel panel;
    private InvoicesTableModel invoicesTableModel;
    private JTable invoicesTable;
    private InvoiceListener invoiceClickListener;
    private InvoiceListener invoiceRemoveListener;
    private InvoiceListener invoiceAddListener;
}
