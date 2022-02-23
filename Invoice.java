package invoices;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Invoice {
    public Invoice(int number) {
        this.number = number;
        date = "";
        customerName = "";
        items = new ArrayList<>();
    }

    //It creates a deep copy of the passed object
    public Invoice(Invoice other) {
        number = other.number;
        date = other.date;
        customerName = other.customerName;
        items = new ArrayList<>();

        for (InvoiceItem item : other.getItems()) {
            items.add(new InvoiceItem(item));
        }
    }

    public int getNumber() {
        return number;
    }

    //It assigns the actual date if the passed string is invalid
    public void setDate(String date) {
        this.date = DateUtility.toString(DateUtility.fromString(date));
    }

    public String getDate() {
        return date;
    }

    //It assigns the passed string after replacing all commas with spaces
    public void setCustomerName(String customerName) {
        this.customerName = customerName.replace(',', ' ');
    }

    public String getCustomerName() {
        return customerName;
    }

    public void removeUnnamedItems() {
        for (int i = 0; i < items.size(); /*...*/) {
            if (items.get(i).getName().isBlank()) {
                items.remove(i);
            }
            else {
                ++i;
            }
        }
    }

    //It sums up getTotal() from all items
    public BigDecimal getTotalAmount() {
        BigDecimal result = BigDecimal.ZERO;

        for (InvoiceItem item : items) {
            result = result.add(item.getTotal());
        }

        return result;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    //It does not display associated items
    @Override
    public String toString() {
        return number + ", " + date + ", " + customerName;
    }

    private int number;
    private String date;
    private String customerName;
    private List<InvoiceItem> items;
}
