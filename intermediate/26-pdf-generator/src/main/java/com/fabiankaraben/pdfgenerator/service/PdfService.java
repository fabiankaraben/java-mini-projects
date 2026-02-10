package com.fabiankaraben.pdfgenerator.service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PdfService {

    public byte[] generatePdf(String title, String content) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);

            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLUE);
            Paragraph titlePara = new Paragraph(title, titleFont);
            titlePara.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(titlePara);

            document.add(new Paragraph("\n"));

            Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);
            Paragraph contentPara = new Paragraph(content, contentFont);
            document.add(contentPara);

            document.close();
            return out.toByteArray();
        } catch (DocumentException e) {
            throw new IOException("Error generating PDF", e);
        }
    }
}
