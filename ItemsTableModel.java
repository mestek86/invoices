package invoices;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class ItemsTableModel extends AbstractTableModel {
    public ItemsTableModel() {
        columnTitles = new String[]{"No.", "Item Name", "Item Price", "Count", "Item Total"};
        this.items = new ArrayList<>();
    }

    @Override
    public int getRowCount()  {
        return items.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        InvoiceItem item = items.get(row);
        String fields[] = {
                String.valueOf(item.getInvoiceNumber()),
                item.getName(),
                String.valueOf(item.getPrice()),
                String.valueOf(item.getCount()),
                String.valueOf(item.getTotal())
        };
        return fields[column];
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        InvoiceItem item = items.get(row);

        switch (col) {
        case 1:
            item.setName((String)value);
            //fireTableDataChanged();
            break;
        case 2:
            item.setPrice(parseBigDecimal((String)value));
            //we have to update total
            fireTableDataChanged();
            break;
        case 3:
             item.setCount(parseInt((String)value));
            //we have to update total
             fireTableDataChanged();
             break;
        default:
            break;
        }
    }

    public void addItem(InvoiceItem item) {
        items.add(item);
        fireTableDataChanged();
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
        fireTableDataChanged();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == 0 || column == 4) {
            return false;
        }
        return true;
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

    private BigDecimal parseBigDecimal(String number) {
        try {
            return new BigDecimal(number);
        }
        catch (NumberFormatException ex) {
            return BigDecimal.ZERO;
        }
    }

    private int parseInt(String number) {
        try {
            return Integer.parseInt(number);
        }
        catch (NumberFormatException ex) {
            return 0;
        }
    }

    private String[] columnTitles;
    private List<InvoiceItem> items;
}