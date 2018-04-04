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
		// 워크북 생성
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 워크시트 생성
		XSSFSheet sheet = workbook.createSheet();
		// 행생성
		XSSFRow row = sheet.createRow(0);
		// 셀생성
		XSSFCell cell;
		// 헤더 정보 구성
		cell = row.createCell(0);
		cell.setCellValue("번호");
		cell = row.createCell(1);
		cell.setCellValue("상품번호");
		cell = row.createCell(2);
		cell.setCellValue("상품명");
		cell = row.createCell(3);
		cell.setCellValue("상품종류");
		cell = row.createCell(4);
		cell.setCellValue("사이즈");
		cell = row.createCell(5);
		cell.setCellValue("상품컬러");
		cell = row.createCell(6);
		cell.setCellValue("상품단가");
		cell = row.createCell(7);
		cell.setCellValue("재고수량");
		cell = row.createCell(8);
		cell.setCellValue("재고가격");
		cell = row.createCell(9);
		cell.setCellValue("이미지파일명");

		// 리스트의 size만큼 row를 생성
		StockVO vo;
		for (int rowldx = 0; rowldx < list.size(); rowldx++) {
			vo = list.get(rowldx);

			// 행 생성
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

		// 입력된 내용 파일로 쓰기
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
