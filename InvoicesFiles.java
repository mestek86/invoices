package invoices;

import java.io.*;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InvoicesFiles {
    public InvoicesFiles() {
        uniqueInvoices = new HashMap<>();
        invoices = new ArrayList<>();
        maxInvoiceNumber = Integer.MIN_VALUE;
    }

    public void load()
    throws IOException
    {
        uniqueInvoices.clear();
        invoices.clear();
        maxInvoiceNumber = Integer.MIN_VALUE;
        loadHeaders();
        loadItems();
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public int getMaxInvoiceNumber() {
        return maxInvoiceNumber;
    }

    public static void save(List<Invoice> invoices)
    throws IOException
    {
        try (
                FileWriter hfw = new FileWriter(new File(InvoicesFiles.class.getResource(HEADERS).toURI()));
                BufferedWriter headersWriter = new BufferedWriter(hfw);
                FileWriter ifw = new FileWriter(new File(InvoicesFiles.class.getResource(ITEMS).toURI()));
                BufferedWriter itemsWriter = new BufferedWriter(ifw);
        ) {
            for (Invoice invoice : invoices) {
                headersWriter.write(invoice.toString());
                headersWriter.newLine();

                for (InvoiceItem item : invoice.getItems()) {
                    itemsWriter.write(item.toString());
                    itemsWriter.newLine();
                }
            }
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            throw new IOException("I am unable to save invoices");
        }
    }


    private void loadHeaders()
    throws IOException
    {
        System.out.println("Loaded headers: ");
        InputStream stream = Main.class.getResourceAsStream(HEADERS);

        if (stream == null) {
            throw new IOException(String.format("%s: I cannot find the file", HEADERS));
        }

        try (BufferedReader headerReader = new BufferedReader(new InputStreamReader(stream))) {
            for (String line = headerReader.readLine(); line != null; line = headerReader.readLine()) {
                String[] fields = line.split(COMMA_DELIMITER);

                if (fields.length != 3) {
                    throw new IOException(String.format("%s: An invalid number of fields", HEADERS));
                }

                int invoiceNumber = Integer.parseInt(fields[0].trim());
                Invoice newInvoice = uniqueInvoices.get(invoiceNumber);

                if (newInvoice != null) {
                    throw new IOException(String.format("%s: Invoices with the same number", HEADERS));
                }

                maxInvoiceNumber = Math.max(maxInvoiceNumber, invoiceNumber);

                newInvoice = new Invoice(invoiceNumber);
                newInvoice.setDate(fields[1].trim());
                newInvoice.setCustomerName(fields[2].trim());
                uniqueInvoices.put(invoiceNumber, newInvoice);
                invoices.add(newInvoice);
                System.out.println(newInvoice);
            }
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            throw new IOException(String.format("%s: An invalid number of an invoice", HEADERS));
        }
    }

    private void loadItems()
    throws IOException
    {
        System.out.println("Loaded items: ");
        InputStream stream = Main.class.getResourceAsStream(ITEMS);

        if (stream == null) {
            throw new IOException(String.format("%s: I cannot find the file", ITEMS));
        }

        try (BufferedReader itemsReader = new BufferedReader(new InputStreamReader(stream))) {
            for (String line = itemsReader.readLine(); line != null; line = itemsReader.readLine()) {
                String[] fields = line.split(COMMA_DELIMITER);

                if (fields.length != 4) {
                    throw new IOException(String.format("%s: An invalid number of fields", ITEMS));
                }

                int invoiceNumber = Integer.parseInt(fields[0].trim());
                Invoice invoice = uniqueInvoices.get(invoiceNumber);

                if (invoice == null) {
                    throw new IOException(String.format("%s: An invoice is missing", ITEMS));
                }

                InvoiceItem item = new InvoiceItem();
                item.setInvoiceNumber(invoiceNumber);
                item.setName(fields[1].trim());
                item.setPrice(new BigDecimal(fields[2].trim()));
                item.setCount(Integer.parseInt(fields[3].trim()));
                invoice.getItems().add(item);
                System.out.println(item);
            }
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            throw new IOException(String.format("%s: Check the number, price and count fields!", ITEMS));
        }
    }

    private static final String COMMA_DELIMITER = ",";
    private static final String HEADERS = "InvoiceHeader.csv";
    private static final String ITEMS = "InvoiceLine.csv";

    HashMap<Integer, Invoice> uniqueInvoices;
    private List<Invoice> invoices;
    private int maxInvoiceNumber;
}
