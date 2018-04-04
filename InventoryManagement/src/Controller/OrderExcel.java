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

import Model.OrderVO;

public class OrderExcel {
	public boolean xlsxOrderWiter(List<OrderVO> list, String saveDir) {
		// ��ũ�� ����
		XSSFWorkbook workbook = new XSSFWorkbook();
		// ��ũ��Ʈ ����
		XSSFSheet sheet = workbook.createSheet();
		// �� ����
		XSSFRow row = sheet.createRow(0);
		// �� ����
		XSSFCell cell;
		// ��� ���� ����
		cell = row.createCell(0);
		cell.setCellValue("��ȣ");
		cell = row.createCell(1);
		cell.setCellValue("�ֹ���ȣ");
		cell = row.createCell(2);
		cell.setCellValue("�ֹ�ǰ��");
		cell = row.createCell(3);
		cell.setCellValue("�ֹ�ǰ��");
		cell = row.createCell(4);
		cell.setCellValue("�ֹ�ǰ��");
		cell = row.createCell(5);
		cell.setCellValue("������");
		cell = row.createCell(6);
		cell.setCellValue("�ֹ�ǰ�÷�");
		cell = row.createCell(7);
		cell.setCellValue("�ֹ�ǰ�ܰ�");
		cell = row.createCell(8);
		cell.setCellValue("�ֹ�����");
		cell = row.createCell(9);
		cell.setCellValue("�ֹ��ݾ�");
		cell = row.createCell(10);
		cell.setCellValue("�ֹ���¥");
		cell = row.createCell(11);
		cell.setCellValue("�̹�����");

		// ����Ʈ�� size��ŭ row����
		OrderVO ovo;
		for (int rowldx = 0; rowldx < list.size(); rowldx++) {
			ovo = list.get(rowldx);

			// �� ����
			row = sheet.createRow(rowldx + 1);

			cell = row.createCell(0);
			cell.setCellValue(ovo.getNo());
			cell = row.createCell(1);
			cell.setCellValue(ovo.getO_num());
			cell = row.createCell(2);
			cell.setCellValue(ovo.getO_pnum());
			cell = row.createCell(3);
			cell.setCellValue(ovo.getO_pname());
			cell = row.createCell(4);
			cell.setCellValue(ovo.getO_pkind());
			cell = row.createCell(5);
			cell.setCellValue(ovo.getO_size());
			cell = row.createCell(6);
			cell.setCellValue(ovo.getO_color());
			cell = row.createCell(7);
			cell.setCellValue(ovo.getO_price());
			cell = row.createCell(8);
			cell.setCellValue(ovo.getO_count());
			cell = row.createCell(9);
			cell.setCellValue(ovo.getO_totalPrice());
			cell = row.createCell(10);
			cell.setCellValue(ovo.getO_date());
			cell = row.createCell(11);
			cell.setCellValue(ovo.getO_fileName());

		}

		// �Էµ� ���� ���Ϸ� �ۼ�
		String strReportPDFName = "order_" + System.currentTimeMillis() + ".xlsx";
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
