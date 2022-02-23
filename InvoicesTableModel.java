package invoices;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

class InvoicesTableModel extends AbstractTableModel {
    public InvoicesTableModel() {
        columnTitles = new String[]{"No.", "Date", "Customer", "Total"};
        this.invoices = new ArrayList<>();
    }

    public Invoice getInvoice(int row) {
        return invoices.get(row);
    }

    public void removeInvoice(int row) {
        invoices.remove(row);
        fireTableDataChanged();
    }

    public void addOrUpdateInvoice(Invoice invoice) {
        boolean updated = false;

        for (int i = 0; i < invoices.size(); ++i) {
            if (invoices.get(i).getNumber() == invoice.getNumber()) {
               invoices.set(i, invoice);
               updated = true;
               break;
            }
        }

        if (!updated) {
            invoices.add(invoice);
        }

        fireTableDataChanged();
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
        fireTableDataChanged();
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    @Override
    public int getRowCount()  {
        return invoices.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        Invoice invoice = invoices.get(row);
        String fields[] = {
            String.valueOf(invoice.getNumber()),
            invoice.getDate(),
            invoice.getCustomerName(),
            String.valueOf(invoice.getTotalAmount())
        };
        return fields[column];
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public int getColumnCount() {
        return columnTitles.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnTitles[column];
    }

    @Override
    public Class getColumnClass(int column) {
        return columnTitles[column].getClass();
    }

    private String[] columnTitles;
    private List<Invoice> invoices;
}