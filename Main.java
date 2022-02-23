package invoices;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] a) {
        JFrame frame = new JFrame("Invoices");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JSplitPane splitPane = new JSplitPane();

        InvoicesTableModel invoicesTableModel = new InvoicesTableModel();
        InvoicesView invoicesView = new InvoicesView(invoicesTableModel);
        invoicesTableModel.setInvoices(loadInvoices().getInvoices());

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadFileMenuItem = new JMenuItem("Load File");
        loadFileMenuItem.addActionListener((ActionEvent e) -> {
            splitPane.setRightComponent(null);
            invoicesTableModel.setInvoices(loadInvoices().getInvoices());
        });
        JMenuItem saveFileMenuItem = new JMenuItem("Save File");
        saveFileMenuItem.addActionListener((ActionEvent e) -> {
            saveInvoices(invoicesTableModel.getInvoices());
        });
        fileMenu.add(loadFileMenuItem);
        fileMenu.add(saveFileMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        OneInvoiceView oneInvoiceView = new OneInvoiceView();
        oneInvoiceView.setOnCancelListener(() -> splitPane.setRightComponent(null));
        oneInvoiceView.setOnSaveListener((Invoice invoice) -> {
            invoice.removeUnnamedItems();
            invoicesTableModel.addOrUpdateInvoice(invoice);
            splitPane.setRightComponent(null);
        });

        invoicesView.setInvoiceClickListener((Invoice clickedInvoice) -> {
            oneInvoiceView.setInvoice(new Invoice(clickedInvoice));
            splitPane.setRightComponent(oneInvoiceView.toJPanel());
        });
        invoicesView.setInvoiceRemoveListener((Invoice clickedInvoice) -> {
            splitPane.setRightComponent(null);
        });
        invoicesView.setInvoiceAddListener((Invoice clickedInvoice) -> {
            oneInvoiceView.setInvoice(clickedInvoice);
            splitPane.setRightComponent(oneInvoiceView.toJPanel());
        });


        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(invoicesView.toJPanel());
        splitPane.setRightComponent(null);

        frame.getContentPane().add(splitPane, BorderLayout.CENTER);

        frame.setSize(1024, 768);
        frame.setVisible(true);
    }

    private static InvoicesFiles loadInvoices() {
        InvoicesFiles invoicesLoader = new InvoicesFiles();

        try {
            invoicesLoader.load();
            InvoiceNumber.set(invoicesLoader.getMaxInvoiceNumber());
        }
        catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "I am unable to load invoices");
        }

        return invoicesLoader;
    }

    private static void saveInvoices(List<Invoice> invoices) {
        try {
            InvoicesFiles.save(invoices);
        }
        catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "I am unable to save invoices");
        }
    }
}

