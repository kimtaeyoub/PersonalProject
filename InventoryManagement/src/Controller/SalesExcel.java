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

import Model.SalesVO;

public class SalesExcel {
	public boolean xlsxWiter(List<SalesVO> list, String saveDir) {
		// ��ũ�� ����
		XSSFWorkbook workbook = new XSSFWorkbook();
		// ��ũ ��Ʈ ����
		XSSFSheet sheet = workbook.createSheet();
		// �� ����
		XSSFRow row = sheet.createRow(0);
		// �� ����
		XSSFCell cell;
		// ��� ���� ����
		cell = row.createCell(0);
		cell.setCellValue("��ȣ");
		cell = row.createCell(1);
		cell.setCellValue("�ǸŹ�ȣ");
		cell = row.createCell(2);
		cell.setCellValue("�Ǹ�ǰ��");
		cell = row.createCell(3);
		cell.setCellValue("�Ǹ�ǰ��");
		cell = row.createCell(4);
		cell.setCellValue("�Ǹ�ǰ��");
		cell = row.createCell(5);
		cell.setCellValue("������");
		cell = row.createCell(6);
		cell.setCellValue("�÷�");
		cell = row.createCell(7);
		cell.setCellValue("�ǸŴܰ�");
		cell = row.createCell(8);
		cell.setCellValue("�Ǹż���");
		cell = row.createCell(9);
		cell.setCellValue("�Ǹűݾ�");
		cell = row.createCell(10);
		cell.setCellValue("�Ǹų�¥");
		cell = row.createCell(11);
		cell.setCellValue("�̹�����");
		cell = row.createCell(12);
		cell.setCellValue("����");
		cell = row.createCell(13);
		cell.setCellValue("������ó");

		// ����Ʈ�� size��ŭ row����
		SalesVO svo;
		for (int rowldx = 0; rowldx < list.size(); rowldx++) {
			svo = list.get(rowldx);

			// �� ����
			row = sheet.createRow(rowldx + 1);

			cell = row.createCell(0);
			cell.setCellValue(svo.getNo());
			cell = row.createCell(1);
			cell.setCellValue(svo.getSa_num());
			cell = row.createCell(2);
			cell.setCellValue(svo.getSa_pnum());
			cell = row.createCell(3);
			cell.setCellValue(svo.getSa_pname());
			cell = row.createCell(4);
			cell.setCellValue(svo.getSa_pkind());
			cell = row.createCell(5);
			cell.setCellValue(svo.getSa_size());
			cell = row.createCell(6);
			cell.setCellValue(svo.getSa_color());
			cell = row.createCell(7);
			cell.setCellValue(svo.getSa_price());
			cell = row.createCell(8);
			cell.setCellValue(svo.getSa_count());
			cell = row.createCell(9);
			cell.setCellValue(svo.getSa_totalPrice());
			cell = row.createCell(10);
			cell.setCellValue(svo.getSa_date());
			cell = row.createCell(11);
			cell.setCellValue(svo.getSa_fileName());
			cell = row.createCell(12);
			cell.setCellValue(svo.getSa_CusName());
			cell = row.createCell(13);
			cell.setCellValue(svo.getSa_CusNum());

		}

		// �Էµ� ���� ���Ϸ� �ۼ�
		String strReportExcelName = "sales_" + System.currentTimeMillis() + ".xlsx";
		File file = new File(saveDir + "\\" + strReportExcelName);
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
