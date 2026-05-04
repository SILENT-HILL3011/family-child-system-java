package com.child.common.utils;

import com.child.common.entity.vo.PhysicalExamVO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class PDFUtil {

    public static void exportExamPdf(PhysicalExamVO vo, HttpServletResponse response)throws IOException, DocumentException{
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=physical_exam_report.pdf");
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        BaseFont bf = BaseFont.createFont("STSong-Light",
                "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
        Font titleFont = new Font(bf, 18, Font.BOLD);
        Font headFont = new Font(bf, 12, Font.BOLD);
        Font textFont = new Font(bf, 11);

        Paragraph title = new Paragraph("体检报告", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingAfter(15);

        addTableCell(table, "儿童姓名", vo.getChildName(), headFont, textFont);
        addTableCell(table, "医生", vo.getDoctor(), headFont, textFont);
        addTableCell(table, "体检日期", vo.getExamDate() != null ? vo.getExamDate().toString() : "-", headFont, textFont);
        addTableCell(table, "身高", getText(vo.getHeight()), headFont, textFont);
        addTableCell(table, "体重", getText(vo.getWeight()), headFont, textFont);
        addTableCell(table, "头围", getText(vo.getHeadCirc()), headFont, textFont);
        addTableCell(table, "视力", getText(vo.getVision()), headFont, textFont);
        addTableCell(table, "听力", getText(vo.getHearing()), headFont, textFont);
        addTableCell(table, "牙齿", getText(vo.getTooth()), headFont, textFont);
        addTableCell(table, "心脏", getText(vo.getHeart()), headFont, textFont);
        addTableCell(table, "腹部", getText(vo.getAbdomen()), headFont, textFont);
        addTableCell(table, "四肢", getText(vo.getLimb()), headFont, textFont);
        addTableCell(table, "皮肤", getText(vo.getSkin()), headFont, textFont);
        addTableCell(table, "神经", getText(vo.getNerve()), headFont, textFont);

        document.add(table);

        Paragraph suggestTitle = new Paragraph("医生建议", headFont);
        suggestTitle.setSpacingBefore(10);
        document.add(suggestTitle);

        Paragraph suggest = new Paragraph(vo.getSuggestion() != null ? vo.getSuggestion() : "无", textFont);
        suggest.setSpacingBefore(5);
        document.add(suggest);

        document.close();

    }

    private static String getText(Integer val){
        if (val == null){
            return "未检查";
        }
        return val == 1 ? "正常" : "异常";
    }

    private static void addTableCell(PdfPTable table, String key, String value, Font headFont, Font textFont){
        PdfPCell cell1  = new PdfPCell(new Phrase(key, headFont));
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell1.setPadding(8);
        table.addCell(cell1);

        PdfPCell cell2 = new PdfPCell(new Phrase(value, textFont));
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2.setPadding(8);
        table.addCell(cell2);
    }
}
