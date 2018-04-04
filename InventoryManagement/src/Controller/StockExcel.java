package Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Model.StockVO;

public class StockExcel {
	public boolean xlsxWiter(List<StockVO> list, String saveDir) {
		// ��ũ�� ����
		XSSFWorkbook workbook = new XSSFWorkbook();
		// ��ũ��Ʈ ����
		XSSFSheet sheet = workbook.createSheet();
		// �����
		XSSFRow row = sheet.createRow(0);
		// ������
		XSSFCell cell;
		// ��� ���� ����
		cell = row.createCell(0);
		cell.setCellValue("��ȣ");
		cell = row.createCell(1);
		cell.setCellValue("��ǰ��ȣ");
		cell = row.createCell(2);
		cell.setCellValue("��ǰ��");
		cell = row.createCell(3);
		cell.setCellValue("��ǰ����");
		cell = row.createCell(4);
		cell.setCellValue("������");
		cell = row.createCell(5);
		cell.setCellValue("��ǰ�÷�");
		cell = row.createCell(6);
		cell.setCellValue("��ǰ�ܰ�");
		cell = row.createCell(7);
		cell.setCellValue("������");
		cell = row.createCell(8);
		cell.setCellValue("�����");
		cell = row.createCell(9);
		cell.setCellValue("�̹������ϸ�");

		// ����Ʈ�� size��ŭ row�� ����
		StockVO vo;
		for (int rowldx = 0; rowldx < list.size(); rowldx++) {
			vo = list.get(rowldx);

			// �� ����
			row = sheet.createRow(rowldx + 1);

			cell = row.createCell(0);
			cell.setCellValue(vo.getNo());
			cell = row.createCell(1);
			cell.setCellValue(vo.getSt_pnum());
			cell = row.createCell(2);
			cell.setCellValue(vo.getSt_pname());
			cell = row.createCell(3);
			cell.setCellValue(vo.getSt_pkind());
			cell = row.createCell(4);
			cell.setCellValue(vo.getSt_size());
			cell = row.createCell(5);
			cell.setCellValue(vo.getSt_color());
			cell = row.createCell(6);
			cell.setCellValue(vo.getSt_price());
			cell = row.createCell(7);
			cell.setCellValue(vo.getSt_count());
			cell = row.createCell(8);
			cell.setCellValue(vo.getSt_totalPrice());
			cell = row.createCell(9);
			cell.setCellValue(vo.getSt_filename());
		}

		// �Էµ� ���� ���Ϸ� ����
		String strReportPDFName = "Stock_" + System.currentTimeMillis() + ".xlsx";
		File file = new File(saveDir + "\\" + strReportPDFName);
		FileOutputStream fos = null;

		boolean saveSuccess;
		saveSuccess = false;

		try {
			fos = new FileOutputStream(file);
			if (fos != null) {
				workbook.write(fos);
				saveSuccess = true;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (workbook != null) {
					workbook.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return saveSuccess;

	}

}
