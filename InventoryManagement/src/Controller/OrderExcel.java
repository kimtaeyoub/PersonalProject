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
		// 워크북 생성
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 워크시트 생성
		XSSFSheet sheet = workbook.createSheet();
		// 행 생성
		XSSFRow row = sheet.createRow(0);
		// 셀 생성
		XSSFCell cell;
		// 헤더 정보 구성
		cell = row.createCell(0);
		cell.setCellValue("번호");
		cell = row.createCell(1);
		cell.setCellValue("주문번호");
		cell = row.createCell(2);
		cell.setCellValue("주문품번");
		cell = row.createCell(3);
		cell.setCellValue("주문품명");
		cell = row.createCell(4);
		cell.setCellValue("주문품종");
		cell = row.createCell(5);
		cell.setCellValue("사이즈");
		cell = row.createCell(6);
		cell.setCellValue("주문품컬러");
		cell = row.createCell(7);
		cell.setCellValue("주문품단가");
		cell = row.createCell(8);
		cell.setCellValue("주문수량");
		cell = row.createCell(9);
		cell.setCellValue("주문금액");
		cell = row.createCell(10);
		cell.setCellValue("주문날짜");
		cell = row.createCell(11);
		cell.setCellValue("이미지명");

		// 리스트의 size만큼 row생성
		OrderVO ovo;
		for (int rowldx = 0; rowldx < list.size(); rowldx++) {
			ovo = list.get(rowldx);

			// 행 생성
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

		// 입력된 내용 파일로 작성
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
