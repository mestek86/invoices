package invoices;

import java.math.BigDecimal;

public class InvoiceItem {
    public InvoiceItem() {
        name = "";
        price = BigDecimal.ZERO;
    }

    //It creates a deep copy of the passed object
    public InvoiceItem(InvoiceItem other) {
        invoiceNumber = other.invoiceNumber;
        name = other.name;
        price = BigDecimal.ZERO.add(other.price);
        count = other.count;
    }

    public void setInvoiceNumber(int invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public int getInvoiceNumber() {
        return invoiceNumber;
    }

    //It assigns the passed string after replacing all commas with spaces
    public void setName(String name) {
        this.name = name.replace(',', ' ');
    }

    public String getName() {
        return name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    //It returns getPrice() * getCount()
    public BigDecimal getTotal() {
        return getPrice().multiply(BigDecimal.valueOf(getCount()));
    }

    @Override
    public String toString() {
        return invoiceNumber + ", " + name + ", " + price.toString() + ", " + count;
    }

    private int invoiceNumber;
    private String name;
    public BigDecimal price;
    public int count;
}
