package invoices;

public class InvoiceNumber {
    private InvoiceNumber() {
    }

    public static void set(int number) {
        invoiceNumber = number;
    }

    public static int get() {
        return invoiceNumber;
    }

    private static int invoiceNumber;
}
