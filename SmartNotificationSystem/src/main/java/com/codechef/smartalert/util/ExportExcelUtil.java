package com.codechef.smartalert.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExportExcelUtil {

	public static byte[] getFileBytes(final List<List<String>> tableJson, final String workSheetName) throws IOException, Exception {
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		final WritableWorkbook writableWorkbook = Workbook.createWorkbook(byteArrayOutputStream);
		final WritableSheet writableSheet = writableWorkbook.createSheet(workSheetName, 0);

		for (int index = 0; index < tableJson.size(); index++) {
			List<String> tableRows = tableJson.get(index);
			for (int rowIndex = 0; rowIndex < tableRows.size(); rowIndex++) {
				WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
				WritableCellFormat cellFormat;
				if (index == 0) {
					cellFont.setBoldStyle(WritableFont.BOLD);
					cellFormat = new WritableCellFormat(cellFont);
				} else {
					cellFont.setBoldStyle(WritableFont.NO_BOLD);
					cellFormat = new WritableCellFormat(cellFont);
				}

				if (tableRows.size() == 1) {
					Colour customColor = new Colour(150, "1", 249, 220, 191) {};
					cellFormat.setBackground(customColor);
					writableSheet.mergeCells(0, index, 10, index);
					Label label = new Label(rowIndex, index, tableRows.get(rowIndex), cellFormat);
					writableSheet.setColumnView(rowIndex, 18);
					writableSheet.addCell(label);

				} else {
					Label label = new Label(rowIndex, index, tableRows.get(rowIndex), cellFormat);
					writableSheet.setColumnView(rowIndex, 18);
					writableSheet.addCell(label);
				}
			}
		}
		writableWorkbook.write();
		writableWorkbook.close();
		return byteArrayOutputStream.toByteArray();
	}
	
	public static void main(String[] args) {
		//byte excel = new ExportExcelUtil().getFileBytes();
		
	}
}
