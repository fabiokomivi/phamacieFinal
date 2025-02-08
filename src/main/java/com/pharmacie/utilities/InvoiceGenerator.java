package com.pharmacie.utilities;

import java.io.File;
import java.text.SimpleDateFormat;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.pharmacie.models.MedicinePurchase;
import com.pharmacie.models.Purchase;

import java.text.DecimalFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class InvoiceGenerator {

    public void generateInvoice(Purchase purchase) throws Exception {
        // Define the base directory for invoices
        String baseDir = "resources/com/pharmacie/invoices";

        // Ensure the directory exists
        File directory = new File(baseDir);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it doesn't exist
        }

        // Generate the file path with a unique name
        String fileName = "invoice_" + purchase.getId() + ".pdf";
        String filePath = baseDir + "/" + fileName;

        // Initialize PDF writer
        PdfWriter writer = new PdfWriter(filePath);

        // Define custom page size (80mm width and auto height)
        PageSize ticketSize = new PageSize(226, PageSize.A6.getHeight()); // Width fixed at 80mm (226 points) and height auto
        PdfDocument pdf = new PdfDocument(writer);
        pdf.setDefaultPageSize(ticketSize);

        // Initialize document layout with small margins
        Document document = new Document(pdf);
        document.setMargins(5, 5, 5, 5); // Reduced margins for ticket size

        // Load fonts
        PdfFont boldFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD);
        PdfFont normalFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA);

        // Add pharmacy name at the top, centered
        Paragraph pharmacyName = new Paragraph("Pharmacie X")
                .setFont(boldFont)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(pharmacyName);

        // Add title
        Paragraph title = new Paragraph("Facture")
                .setFont(boldFont)
                .setFontSize(10) // Smaller font size for ticket
                .setTextAlignment(TextAlignment.CENTER);
        document.add(title);

        // Add formatted date
        String formattedDate = formatDate(purchase.getCreatedAt());
        document.add(new Paragraph("Date: " + formattedDate)
                .setFont(normalFont).setFontSize(8));

        // Add client and employee details on the same line
        Table clientEmployeeTable = new Table(2); // 2 columns
        clientEmployeeTable.setWidth(UnitValue.createPercentValue(100));

        // Left cell: Client name
        String clientName = (purchase.getClient() != null) ? purchase.getClient().getName() : "Anonyme";
        clientEmployeeTable.addCell(new Cell().add(new Paragraph("Client: " + clientName).setFont(normalFont).setFontSize(8))
                .setBorder(Border.NO_BORDER)); // No border

        // Right cell: Employee name
        String employeeName = (purchase.getUser() != null) ? purchase.getUser().getName() : "Non spécifié";
        clientEmployeeTable.addCell(new Cell().add(new Paragraph("Employee: " + employeeName).setFont(normalFont).setFontSize(8))
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(Border.NO_BORDER)); // No border

        // Add table to document
        document.add(clientEmployeeTable);

        document.add(new Paragraph(" ").setFontSize(6)); // Empty line

        // Add table for purchase details
        Table table = new Table(new float[]{3, 1, 1}); // Adjusted column widths
        table.setWidth(UnitValue.createPercentValue(100));

        // Add table headers
        table.addHeaderCell(createHeaderCell("Produit", boldFont));
        table.addHeaderCell(createHeaderCell("Qte", boldFont));
        table.addHeaderCell(createHeaderCell("Prix", boldFont));

        // Add each medicine purchase
        for (MedicinePurchase mp : purchase.getMedicinesLink()) {
            table.addCell(new Cell().add(new Paragraph(mp.getMedicine().getName()).setFont(normalFont).setFontSize(8)));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(mp.getQuantity())).setFont(normalFont).setFontSize(8)));
            table.addCell(new Cell().add(new Paragraph(formatPrice(mp.getTotalPrice())).setFont(normalFont).setFontSize(8)));
        }

        // Add table to document
        document.add(table);

        // Add total amount
        document.add(new Paragraph(" ").setFontSize(6)); // Empty line
        Paragraph total = new Paragraph("Total: " + formatPrice(purchase.getTotal()))
                .setFont(boldFont)
                .setFontSize(10) // Slightly larger for total
                .setTextAlignment(TextAlignment.RIGHT);
        document.add(total);

        // Add message at the bottom
        document.add(new Paragraph("Pharmacie X vous souhaite une bonne guérison.")
                .setFont(normalFont)
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER));

        // Close the document
        document.close();

        System.out.println("Invoice generated: " + filePath);
    }

    // Helper method to format price to 2 decimal places
    private String formatPrice(double price) {
        DecimalFormat df = new DecimalFormat("###,###.##");
        return df.format(price);
    }

    // Helper method to format date (for LocalDateTime or Date)
    private String formatDate(Object createdAt) {
        if (createdAt instanceof LocalDateTime) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yy");
            return ((LocalDateTime) createdAt).format(formatter);
        } else if (createdAt instanceof Date) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yy");
            return dateFormat.format((Date) createdAt);
        }
        return "Unknown Date";
    }

    private Cell createHeaderCell(String content, PdfFont font) {
        return new Cell()
                .add(new Paragraph(content).setFont(font).setFontSize(8)) // Reduced font size for headers
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER);
    }
}
