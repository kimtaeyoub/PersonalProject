package Controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import org.omg.Messaging.SyncScopeHelper;
import org.omg.PortableServer.ServantActivatorOperations;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.log.SysoCounter;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import Model.OrderVO;
import Model.SalesVO;
import Model.StockVO;
import Model.WarehousingVO;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.DateTimeStringConverter;

public class RootController implements Initializable {

	@FXML
	private Button btnOrder; // 메인창 주문 버튼
	@FXML
	private Button btnSales; // 메인창 판매 버튼
	@FXML
	private Button btnStock; // 메인창 재고 버튼
	@FXML
	private Button btnExit; // 메인창 종료버튼
	@FXML
	private ImageView imageView; // 메인창 이미지 뷰

	private Stage primaryStage; // 이미지파일 찾을때 사용되는 스테이지 정의
	String selectFileName = "";// 이미지파일명
	String localUrl = ""; // 이미지 파일 경로
	Image localImage; // 이미지 정의
	File selectedFile = null; // 선택된 파일 정의
	// 이미지 저장 폴더 설정
	private File dirSave = new File("C:/imagesStock");
	// 이미지 불러올 파일을 저장할 파일 객체 선언
	private File file = null;

	// 입력받은내용 저장소
	ObservableList<StockVO> data = FXCollections.observableArrayList();
	ObservableList<OrderVO> orderData = FXCollections.observableArrayList();
	ObservableList<WarehousingVO> wareData = FXCollections.observableArrayList();
	ObservableList<SalesVO> preSalesData = FXCollections.observableArrayList();
	ObservableList<SalesVO> salesData = FXCollections.observableArrayList();

	// 재고 총수량 저장소
	int totalCountData = 0;
	// 재고 총액 저장소
	String totalPriceData = null;
	// 주문 총수량 저장소
	int totalOrderCountData = 0;
	// 주문 총액 저장소
	String totalOrderPriceData = null;
	// 입고 총수량 저장소
	int totalWareCountData = 0;
	// 입고 총액 저장소
	String totalWarePriceData = null;
	// 판매대기 총수량 저장소
	int totalPreSalesCountData = 0;
	// 판매대기 총액 저장소
	int totalPreSalesPriceData = 0;
	// 판매목록 총수량 저장소
	int totalSalesCountData = 0;
	// 판매목록 총액 저장소
	String totalSalesPriceData = null;

	// 재고 목록 테이블뷰에서 선택한 정보 저장소 (재고창 관련)
	ObservableList<StockVO> selectStock = null;
	// 주문목록 검색 테이블뷰에서 선택한 정보 저장소 (주문창 관련)
	ObservableList<StockVO> selectOrder = null;
	// 주문 목록 테이블뷰 에서 선택한 정보저장소 관련
	ObservableList<OrderVO> selectOrderList = null;
	// 입고 목록 테이블뷰 에서 선택한 정보 저장 관련
	ObservableList<WarehousingVO> selectWareList = null;
	// 판매 검색 테이블뷰에서 선택한 정보 저장소 (판매창 관련)
	ObservableList<StockVO> selectSales = null;
	// 판매대기 테이블뷰 에서 선택한 정보 저장소 (판매창)
	ObservableList<SalesVO> selectPreSales = null;
	// 판매 목록 테이블뷰에서 선택한 정보 저장소
	ObservableList<SalesVO> selectSalesList = null;

	// 선택한 번호저장
	int no;

	// 테이블에서 선택한 정보 인덱스 저장
	int selectedIndex;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		/****************************
		 * 메인뷰 이미지 디폴트 이미지 설정
		 ***************************/

		localUrl = "/image/bluedog.jpg"; // 디폴트 이미지 설정
		localImage = new Image(localUrl, false);
		imageView.setImage(localImage);

		/*****************************
		 * 메인뷰 버튼 기능설정
		 *****************************/

		btnOrder.setOnAction(event -> handleBtnOrderAction(event));
		btnSales.setOnAction(event -> handleBtnSalesAction(event));
		btnStock.setOnAction(event -> handleBtnStockAction(event));
		btnExit.setOnAction(event -> Platform.exit());

	}

	// 재고버튼 이벤트처리 / 다이얼로그창 생성
	public void handleBtnStockAction(ActionEvent event) {

		data.removeAll(data);

		try {

			/********************************
			 * 화면띄우기 위한 코드
			 ****************************/

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/View/stock.fxml"));

			Stage dialog = new Stage(StageStyle.DECORATED);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(btnStock.getScene().getWindow());
			dialog.setTitle("재고관리");

			Parent parentStock = (Parent) loader.load();
			Scene scene = new Scene(parentStock);
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.show();

			/******************************************
			 * 재고 모달창 버튼및 텍스트필드, 이미지 뷰, 테이블뷰 정의
			 ******************************************/

			TextField txtStockPnumSearch = (TextField) parentStock.lookup("#txtStockPnumSearch");
			TextField txtStockPkindSearch = (TextField) parentStock.lookup("#txtStockPkindSearch");
			TextField txtStockTotalCount = (TextField) parentStock.lookup("#txtStockTotalCount");
			TextField txtStockTotalPrice = (TextField) parentStock.lookup("#txtStockTotalPrice");
			TextField txtStockSaveFileDir = (TextField) parentStock.lookup("#txtStockSaveFileDir");

			Button btnStockSaveFileDir = (Button) parentStock.lookup("#btnStockSaveFileDir");
			Button btnStockExcel = (Button) parentStock.lookup("#btnStockExcel");
			Button btnStockPDF = (Button) parentStock.lookup("#btnStockPDF");
			Button btnOrderSalesRate = (Button) parentStock.lookup("#btnOrderSalesRate");
			Button btnStockRate = (Button) parentStock.lookup("#btnStockRate");
			Button btnNewGoods = (Button) parentStock.lookup("#btnNewGoods");
			Button btnStockExit = (Button) parentStock.lookup("#btnStockExit");
			Button btnStockPnumSearch = (Button) parentStock.lookup("#btnStockPnumSearch");
			Button btnStockPkindSearch = (Button) parentStock.lookup("#btnStockPkindSearch");
			Button btnEditGoods = (Button) parentStock.lookup("#btnEditGoods");
			Button btnDeleteGoods = (Button) parentStock.lookup("#btnDeleteGoods");
			Button btnStockTotalSearch = (Button) parentStock.lookup("#btnStockTotalSearch");

			ImageView stockImageView = (ImageView) parentStock.lookup("#stockImageView");

			TableView<StockVO> stockTableView = (TableView) parentStock.lookup("#stockTableView");

			/************************
			 * 재고 이미지뷰 디폴트 이미지 설정
			 ************************/

			localUrl = "/image/default.gif"; // 디폴트 이미지 설정
			localImage = new Image(localUrl, false);
			stockImageView.setFitWidth(300);
			stockImageView.setFitHeight(300);
			stockImageView.setImage(localImage);

			// 테이블뷰 수정불가 상태 설정
			stockTableView.setEditable(false);
			// 수정 삭제 버튼 사용불가상태설정
			btnEditGoods.setDisable(true);
			btnDeleteGoods.setDisable(true);
			btnOrderSalesRate.setDisable(true);
			// 엑셀 pdf버튼 사용불가 상태 설정
			btnStockExcel.setDisable(true);
			btnStockPDF.setDisable(true);

			/******************************************
			 * 재고 모달창 버튼, 텍스트필드, 이미지뷰, 테이블뷰 기능설정
			 ******************************************/

			// 엑셀, pdf파일 생성 기능처리
			// 엑셀 파일생성
			btnStockExcel.setOnAction(e -> {

				StockDAO sDao9 = new StockDAO();
				boolean saveSuccess;

				ArrayList<StockVO> list;
				list = sDao9.getStockTotal();
				StockExcel excelWriter = new StockExcel();

				// xlsx 파일쓰기
				saveSuccess = excelWriter.xlsxWiter(list, txtStockSaveFileDir.getText());
				if (saveSuccess) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("엑셀 파일 생성");
					alert.setHeaderText("재고 목록 엑셀 파일 생성 성공");
					alert.setContentText("재고 목록 엑셀 파일");

					alert.showAndWait();
				}
				txtStockSaveFileDir.clear();

				btnStockExcel.setDisable(true);
				btnStockPDF.setDisable(true);

			});

			// PDF파일 생성
			btnStockPDF.setOnAction(e -> {

				try {
					// pdf document 선언
					// (Rectangle pageSize, float marginLeft,float marginRight,float marginTop,
					// float marginBotton)
					Document document = new Document(PageSize.A4, 0, 0, 30, 30);

					// pdf파일을 저장할 공간을 선언, pdf파일이 생성된다. 그후 스트림으로 저장.
					String strReportPDFName = "stock_" + System.currentTimeMillis() + ".pdf";
					PdfWriter.getInstance(document,
							new FileOutputStream(txtStockSaveFileDir.getText() + "\\" + strReportPDFName));

					// document를 열어 pdf문서를 쓸수 있도록한다.
					document.open();

					// 한글 지원 폰트 설정
					BaseFont bf = BaseFont.createFont("font/MALGUN.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
					Font font = new Font(bf, 8, Font.NORMAL);
					Font font2 = new Font(bf, 14, Font.BOLD);

					// 타이틀
					Paragraph title = new Paragraph("재고 목록", font2);

					// 가운데 정렬
					title.setAlignment(Element.ALIGN_CENTER);

					// 문서에 추가
					document.add(title);
					document.add(new Paragraph("\r\n"));

					// 생성 날짜
					LocalDate date = LocalDate.now();
					Paragraph writeDay = new Paragraph(date.toString(), font);

					// 오른쪽 정렬
					writeDay.setAlignment(Element.ALIGN_RIGHT);

					// 문서에 추가
					document.add(writeDay);
					document.add(new Paragraph("\r\n"));

					// 테이블 생성 Table객체 보다 PdfTable객체가 더 정교하게 테이블을 만들 수 있다.
					// 생성자에 컬럼수를 적어준다
					PdfPTable table = new PdfPTable(10);
					// 각각의 컬럼에 width를 정한다. 간격설정
					table.setWidths(new int[] { 30, 50, 50, 40, 40, 40, 50, 50, 60, 100 });

					// 컬럼 타이틀설정
					PdfPCell header1 = new PdfPCell(new Paragraph("번호", font));
					PdfPCell header2 = new PdfPCell(new Paragraph("상품번호", font));
					PdfPCell header3 = new PdfPCell(new Paragraph("상품명", font));
					PdfPCell header4 = new PdfPCell(new Paragraph("상품종류", font));
					PdfPCell header5 = new PdfPCell(new Paragraph("사이즈", font));
					PdfPCell header6 = new PdfPCell(new Paragraph("상품컬러", font));
					PdfPCell header7 = new PdfPCell(new Paragraph("상품단가", font));
					PdfPCell header8 = new PdfPCell(new Paragraph("재고수량", font));
					PdfPCell header9 = new PdfPCell(new Paragraph("재고합계", font));
					PdfPCell header10 = new PdfPCell(new Paragraph("이미지파일명", font));

					// 가로 정렬
					header1.setHorizontalAlignment(Element.ALIGN_CENTER);
					header2.setHorizontalAlignment(Element.ALIGN_CENTER);
					header3.setHorizontalAlignment(Element.ALIGN_CENTER);
					header4.setHorizontalAlignment(Element.ALIGN_CENTER);
					header5.setHorizontalAlignment(Element.ALIGN_CENTER);
					header6.setHorizontalAlignment(Element.ALIGN_CENTER);
					header7.setHorizontalAlignment(Element.ALIGN_CENTER);
					header8.setHorizontalAlignment(Element.ALIGN_CENTER);
					header9.setHorizontalAlignment(Element.ALIGN_CENTER);
					header10.setHorizontalAlignment(Element.ALIGN_CENTER);

					// 세로 정렬
					header1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header4.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header5.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header6.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header7.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header8.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header9.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header10.setVerticalAlignment(Element.ALIGN_MIDDLE);

					// 테이블에 추가
					table.addCell(header1);
					table.addCell(header2);
					table.addCell(header3);
					table.addCell(header4);
					table.addCell(header5);
					table.addCell(header6);
					table.addCell(header7);
					table.addCell(header8);
					table.addCell(header9);
					table.addCell(header10);

					// DB연결 및 리스트 선택
					StockDAO sDao = new StockDAO();
					StockVO sVo = new StockVO();
					ArrayList<StockVO> list;
					list = sDao.getStockTotal();
					int rowCount = list.size();

					PdfPCell cell1 = null;
					PdfPCell cell2 = null;
					PdfPCell cell3 = null;
					PdfPCell cell4 = null;
					PdfPCell cell5 = null;
					PdfPCell cell6 = null;
					PdfPCell cell7 = null;
					PdfPCell cell8 = null;
					PdfPCell cell9 = null;
					PdfPCell cell10 = null;

					for (int index = 0; index < rowCount; index++) {
						sVo = list.get(index);
						cell1 = new PdfPCell(new Paragraph(sVo.getNo() + "", font));
						cell2 = new PdfPCell(new Paragraph(sVo.getSt_pnum() + "", font));
						cell3 = new PdfPCell(new Paragraph(sVo.getSt_pname() + "", font));
						cell4 = new PdfPCell(new Paragraph(sVo.getSt_pkind() + "", font));
						cell5 = new PdfPCell(new Paragraph(sVo.getSt_size() + "", font));
						cell6 = new PdfPCell(new Paragraph(sVo.getSt_color() + "", font));
						cell7 = new PdfPCell(new Paragraph(sVo.getSt_price() + "", font));
						cell8 = new PdfPCell(new Paragraph(sVo.getSt_count() + "", font));
						cell9 = new PdfPCell(new Paragraph(sVo.getSt_totalPrice() + "", font));
						cell10 = new PdfPCell(new Paragraph(sVo.getSt_filename() + "", font));

						// 가로 정렬
						cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell10.setHorizontalAlignment(Element.ALIGN_CENTER);

						// 세로 정렬
						cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);

						// 테이블에 셀 추가
						table.addCell(cell1);
						table.addCell(cell2);
						table.addCell(cell3);
						table.addCell(cell4);
						table.addCell(cell5);
						table.addCell(cell6);
						table.addCell(cell7);
						table.addCell(cell8);
						table.addCell(cell9);
						table.addCell(cell10);

					}

					// 문서에 테이블 추가
					document.add(table);

					// 문서를 닫는다. 쓰기 종료
					document.close();

					txtStockSaveFileDir.clear();
					btnStockPDF.setDisable(true);
					btnStockExcel.setDisable(true);

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("PDF 파일 생성");
					alert.setHeaderText("재고 목록 PDF 파일 생성 성공");
					alert.setContentText("재고 목록 PDF 파일");
					alert.showAndWait();

				} catch (FileNotFoundException e8) {
					e8.printStackTrace();
				} catch (DocumentException e8) {
					e8.printStackTrace();
				} catch (IOException e8) {
					e8.printStackTrace();
				}

			});

			// 파일 저장 폴더
			btnStockSaveFileDir.setOnAction(e -> {
				final DirectoryChooser stockDirectoryChooser = new DirectoryChooser();
				final File selectedStockDirectory = stockDirectoryChooser.showDialog(primaryStage);

				if (selectedStockDirectory != null) {
					txtStockSaveFileDir.setText(selectedStockDirectory.getAbsolutePath());
					btnStockExcel.setDisable(false);
					btnStockPDF.setDisable(false);
				}
			});

			// 신규상품등록 모달창 이벤트처리
			btnNewGoods.setOnAction(e -> {

				try {

					/**********************
					 * 신규 상품등록 모달창 화면생성 코드
					 **********************/
					FXMLLoader newGoodsloader = new FXMLLoader();
					newGoodsloader.setLocation(getClass().getResource("/View/newGoods.fxml"));

					Stage newGoodsDialog = new Stage(StageStyle.DECORATED);
					newGoodsDialog.initModality(Modality.WINDOW_MODAL);
					newGoodsDialog.initOwner(btnNewGoods.getScene().getWindow());
					newGoodsDialog.setTitle("신규 상품 등록");

					Parent parentNewGoods = (Parent) newGoodsloader.load();
					Scene newGoodsScene = new Scene(parentNewGoods);
					newGoodsDialog.setScene(newGoodsScene);
					newGoodsDialog.setResizable(false);
					newGoodsDialog.show();

					/***************************************
					 * 신규 상품등록 모달창 텍스트필드, 버튼, 이미지뷰 정의
					 ***************************************/

					TextField txtNewGoodsPnum = (TextField) parentNewGoods.lookup("#txtNewGoodsPnum");
					TextField txtNewGoodsPname = (TextField) parentNewGoods.lookup("#txtNewGoodsPname");
					TextField txtNewGoodsPkind = (TextField) parentNewGoods.lookup("#txtNewGoodsPkind");
					TextField txtNewGoodsPrice = (TextField) parentNewGoods.lookup("#txtNewGoodsPrice");
					TextField txtNewGoodsSize = (TextField) parentNewGoods.lookup("#txtNewGoodsSize");
					TextField txtNewGoodsColor = (TextField) parentNewGoods.lookup("#txtNewGoodsColor");

					Button btnNewGoodsOk = (Button) parentNewGoods.lookup("#btnNewGoodsOk");
					Button btnNewGoodsExit = (Button) parentNewGoods.lookup("#btnNewGoodsExit");
					Button btnNewGoodsImage = (Button) parentNewGoods.lookup("#btnNewGoodsImage");

					ImageView NewGoodsImageView = (ImageView) parentNewGoods.lookup("#NewGoodsImageView");

					/*****************************************
					 * 신규 상품등록 모달창 버튼, 이미지뷰, 텍스트필드 기능설정
					 *****************************************/

					// 신규 상품 선택창 등록 버튼 기능설정
					btnNewGoodsOk.setOnAction(ev -> {
						StockDAO sDao5 = new StockDAO();
						try {

							if (txtNewGoodsPnum.getText().equals("") || txtNewGoodsPname.getText().equals("")
									|| txtNewGoodsPkind.getText().equals("") || txtNewGoodsSize.getText().equals("")
									|| txtNewGoodsColor.getText().equals("") || txtNewGoodsPrice.getText().equals("")) {

								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("상품 정보 입력오류");
								alert.setHeaderText("상품정보입력이 누락되었습니다.");
								alert.setContentText("확인후 누락없이 전부 입력해주세요");
								alert.showAndWait();

							} else if (Integer.parseInt(txtNewGoodsPrice.getText()) <= 0) {

								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("상품 정보 입력오류");
								alert.setHeaderText("단가 책정이 잘못되었습니다.");
								alert.setContentText("단가는 0원 혹은 - 가 될수 없습니다.");
								alert.showAndWait();
							} else if (sDao5.isStockPnum(txtNewGoodsPnum.getText())) {

								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("상품 정보 입력오류");
								alert.setHeaderText("품번입력이 잘못되었습니다.");
								alert.setContentText("해당 품번은 이미 존재합니다. 확인후 다시 입력하세요.");
								alert.showAndWait();

							} else {

								data.removeAll(data);
								StockVO sVo = null;
								StockDAO sDao = new StockDAO();
								File dirMake = new File(dirSave.getAbsolutePath());

								// 이미지 저장
								if (!dirMake.exists()) {
									dirMake.mkdir();
								}

								// 이미지 파일 저장
								String fileName = imageSave(selectedFile);

								// 재고 정보 저장
								if (ev.getSource().equals(btnNewGoodsOk)) {
									sVo = new StockVO(txtNewGoodsPnum.getText(), txtNewGoodsPname.getText(),
											txtNewGoodsPkind.getText(), txtNewGoodsSize.getText(),
											txtNewGoodsColor.getText(), Integer.parseInt(txtNewGoodsPrice.getText()),
											fileName);
									sDao = new StockDAO();
									sDao.inputNewGoods(sVo);

									if (sDao != null) {
										totalList();
										Alert alert = new Alert(AlertType.INFORMATION);
										alert.setTitle("신규 상품정보 등록");
										alert.setHeaderText(txtNewGoodsPnum.getText() + " 번의 상품이 성공적으로 추가 되었습니다.");
										alert.setContentText("다음 상품정보를 입력하세요");

										alert.showAndWait();

										// 기본이미지로 복구
										localUrl = "/image/default.gif";
										localImage = new Image(localUrl, false);
										NewGoodsImageView.setImage(localImage);
										selectedFile = null;

										txtNewGoodsPnum.setEditable(true);
										txtNewGoodsPname.setEditable(true);
										txtNewGoodsPkind.setEditable(true);
										txtNewGoodsSize.setEditable(true);
										txtNewGoodsColor.setEditable(true);
										txtNewGoodsPrice.setEditable(true);

										txtNewGoodsPnum.clear();
										txtNewGoodsPname.clear();
										txtNewGoodsPkind.clear();
										txtNewGoodsSize.clear();
										txtNewGoodsColor.clear();
										txtNewGoodsPrice.clear();

									}

								}
							}

						} catch (Exception e3) {

							data.removeAll(data);
							totalList();

							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("상품정보 입력");
							alert.setHeaderText("상품 정보를 정확히 확인후 입력하세요.");
							alert.setContentText("다음에는 주의하세요.");
							alert.showAndWait();

						}

					});

					// 이미지뷰 설정 (디폴트상태설정)
					localUrl = "/image/default.gif";
					localImage = new Image(localUrl, false);
					NewGoodsImageView.setImage(localImage);

					// 이미지 선택 버튼 기능설정
					btnNewGoodsImage.setOnAction(ev -> {
						FileChooser fileChooser = new FileChooser();
						fileChooser.getExtensionFilters()
								.addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));

						try {
							selectedFile = fileChooser.showOpenDialog(primaryStage);
							if (selectedFile != null) {
								// 이미지 파일경로
								localUrl = selectedFile.toURI().toURL().toString();
							}
						} catch (MalformedURLException e2) {
							e2.printStackTrace();
						}

						localImage = new Image(localUrl, false);
						NewGoodsImageView.setImage(localImage);

						if (selectedFile != null) {
							selectFileName = selectedFile.getName();
						}

					});

					// 신규 상품선택창 종료버튼 기능설정
					btnNewGoodsExit.setOnAction(ev -> {
						newGoodsDialog.close();
					});

				} catch (Exception e1) {
					System.out.println("신규 상품 등록창 관련 오류!!!!!\n" + e1.toString());
				}

			});

			// 테이블뷰컬럼 설정

			TableColumn colNo = new TableColumn("NO");
			colNo.setMinWidth(40);
			colNo.setStyle("-fx-alignment:CENTER");
			colNo.setCellValueFactory(new PropertyValueFactory<>("no"));

			TableColumn colst_pnum = new TableColumn("품번");
			colst_pnum.setMinWidth(140);
			colst_pnum.setStyle("-fx-alignment:CENTER");
			colst_pnum.setCellValueFactory(new PropertyValueFactory<>("st_pnum"));

			TableColumn colst_pname = new TableColumn("품명");
			colst_pname.setMinWidth(160);
			colst_pname.setStyle("-fx-alignment:CENTER");
			colst_pname.setCellValueFactory(new PropertyValueFactory<>("st_pname"));

			TableColumn colst_pkind = new TableColumn("품종");
			colst_pkind.setMinWidth(100);
			colst_pkind.setStyle("-fx-alignment:CENTER");
			colst_pkind.setCellValueFactory(new PropertyValueFactory<>("st_pkind"));

			TableColumn colst_size = new TableColumn("사이즈");
			colst_size.setMinWidth(80);
			colst_size.setStyle("-fx-alignment:CENTER");
			colst_size.setCellValueFactory(new PropertyValueFactory<>("st_size"));

			TableColumn colst_color = new TableColumn("컬러");
			colst_color.setMinWidth(60);
			colst_color.setStyle("-fx-alignment:CENTER");
			colst_color.setCellValueFactory(new PropertyValueFactory<>("st_color"));

			TableColumn colst_price = new TableColumn("단가");
			colst_price.setMinWidth(80);
			colst_price.setStyle("-fx-alignment:CENTER");
			colst_price.setCellValueFactory(new PropertyValueFactory<>("st_price"));

			TableColumn colst_count = new TableColumn("수량");
			colst_count.setMinWidth(40);
			colst_count.setStyle("-fx-alignment:CENTER");
			colst_count.setCellValueFactory(new PropertyValueFactory<>("st_count"));

			TableColumn colst_totalPrice = new TableColumn("총금액");
			colst_totalPrice.setMinWidth(100);
			colst_totalPrice.setStyle("-fx-alignment:CENTER");
			colst_totalPrice.setCellValueFactory(new PropertyValueFactory<>("st_totalPrice"));

			TableColumn colst_filename = new TableColumn("파일명");
			colst_filename.setMinWidth(200);
			colst_filename.setCellValueFactory(new PropertyValueFactory<>("st_filename"));

			// 테이블뷰에 열 추가
			stockTableView.getColumns().addAll(colNo, colst_pnum, colst_pname, colst_pkind, colst_size, colst_color,
					colst_price, colst_count, colst_totalPrice, colst_filename);

			/*****************************************************************
			 * 입력받은 총수량을 텍스트필드로 적용 (임시작)
			 *****************************************************************/
			StockVO svo1 = new StockVO();
			StockDAO sDao1 = new StockDAO();

			totalCountData = sDao1.getStockTotalCount(svo1).getSt_stockTotalCount();
			totalPriceData = sDao1.getStockTotalCount(svo1).getSt_stockTotalPrice();

			txtStockTotalCount.setText(totalCountData + "");
			txtStockTotalPrice.setText(totalPriceData + "");

			// 상품 전체 정보 입력
			totalList();

			// 입력받은 값을 data에서 테이블뷰로 적용
			stockTableView.setItems(data);

			// 테이블뷰 상품 선택시 이미지가 화면에 송출, 수정 삭제 버튼 활성화 설정
			stockTableView.setOnMouseClicked(e -> {

				if (e.getClickCount() != 2) {
					try {
						selectStock = stockTableView.getSelectionModel().getSelectedItems();

						no = selectStock.get(0).getNo();

						selectFileName = selectStock.get(0).getSt_filename();
						localUrl = "file:/C:/imagesStock/" + selectFileName;
						localImage = new Image(localUrl, false);

						stockImageView.setFitWidth(300);
						stockImageView.setFitHeight(300);
						stockImageView.setImage(localImage);

						// 수정 삭제버튼 활성화
						btnEditGoods.setDisable(false);
						btnDeleteGoods.setDisable(false);

						btnOrderSalesRate.setDisable(false);

					} catch (Exception e1) {

					}
				}

			});

			// 주문/판매비율 확인 버튼 기능설정(바차트)
			btnOrderSalesRate.setOnAction(ev -> {
				try {
					/***********************
					 * 바차트 모달창 화면 생성 코드
					 *********************/
					FXMLLoader barchartloader = new FXMLLoader();
					barchartloader.setLocation(getClass().getResource("/View/stockbarchart.fxml"));

					Stage barchartDialog = new Stage(StageStyle.DECORATED);
					barchartDialog.initModality(Modality.WINDOW_MODAL);
					barchartDialog.initOwner(btnOrderSalesRate.getScene().getWindow());
					barchartDialog.setTitle("주문/판매 비율 바 차트");

					Parent parentbarchart = (Parent) barchartloader.load();
					Scene barchartScene = new Scene(parentbarchart);
					barchartDialog.setScene(barchartScene);
					barchartDialog.setResizable(false);
					barchartDialog.show();

					/**********************
					 * 모달창 화면 버튼 정의
					 **********************/

					BarChart stockbarchart = (BarChart) parentbarchart.lookup("#stockbarchart");
					Button btnbarchartexit = (Button) parentbarchart.lookup("#btnbarchartexit");

					/*********************
					 * 버튼 기능 설정
					 *********************/

					// 바차트 데이터 입력설정
					XYChart.Series seriesOrder = new XYChart.Series();
					seriesOrder.setName("총 주문수량");

					ObservableList orderCountList = FXCollections.observableArrayList();

					selectStock = stockTableView.getSelectionModel().getSelectedItems();

					orderCountList.add(new XYChart.Data(selectStock.get(0).getSt_pnum(),
							sDao1.countTotalOrder(selectStock.get(0).getSt_pnum())));

					seriesOrder.setData(orderCountList);
					stockbarchart.getData().add(seriesOrder);

					XYChart.Series seriesSales = new XYChart.Series();
					seriesSales.setName("총 판매수량");

					ObservableList salesCountList = FXCollections.observableArrayList();

					selectStock = stockTableView.getSelectionModel().getSelectedItems();

					salesCountList.add(new XYChart.Data(selectStock.get(0).getSt_pnum(),
							sDao1.countTotalSales(selectStock.get(0).getSt_pnum())));

					seriesSales.setData(salesCountList);
					stockbarchart.getData().add(seriesSales);

					// 바차트 모달창 종료 버튼 설정
					btnbarchartexit.setOnAction(e -> {
						barchartDialog.close();
					});

				} catch (Exception e) {

					System.out.println("오류" + e);

				}
			});

			// 재고 비율 확인 버튼 기능설정 (파이차트)
			btnStockRate.setOnAction(ev -> {
				try {
					/**********************
					 * 파이차트 모달창 화면생성 코드
					 **********************/
					FXMLLoader piechartloader = new FXMLLoader();
					piechartloader.setLocation(getClass().getResource("/View/stockpiechart.fxml"));

					Stage piechartDialog = new Stage(StageStyle.DECORATED);
					piechartDialog.initModality(Modality.WINDOW_MODAL);
					piechartDialog.initOwner(btnStockRate.getScene().getWindow());
					piechartDialog.setTitle("현 재고 비율 파이 차트");

					Parent parentpiechart = (Parent) piechartloader.load();
					Scene piechartScene = new Scene(parentpiechart);
					piechartDialog.setScene(piechartScene);
					piechartDialog.setResizable(false);
					piechartDialog.show();

					/*********************
					 * 파이차트 모달창 버튼 정의
					 *****************/

					PieChart stockpiechart = (PieChart) parentpiechart.lookup("#stockpiechart");

					Button btnpiechartexit = (Button) parentpiechart.lookup("#btnpiechartexit");

					/**********************
					 * 기능설정
					 ********************/

					// 파이차트 기능설정 (데이터 설정)
					ObservableList<PieChart.Data> piechartData = FXCollections.observableArrayList();

					for (int i = 0; i < sDao1.countStockList().size(); i++) {

						piechartData.add(new PieChart.Data(sDao1.countStockList().get(i).getSt_pnum(),
								sDao1.countStockList().get(i).getSt_count()));

					}

					stockpiechart.setData(piechartData);

					// 종료버튼 기능설정
					btnpiechartexit.setOnAction(e -> piechartDialog.close());

				} catch (Exception e) {

				}

			});

			// 품번으로 조회 버튼 기능설정
			btnStockPnumSearch.setOnAction(ev -> {

				StockVO sVo = new StockVO();
				StockDAO sDao = null;

				Object[][] totalData = null;

				String searchPnum = null;
				boolean searchResult = false;

				try {
					searchPnum = txtStockPnumSearch.getText().trim();
					sDao = new StockDAO();
					sVo = sDao.getStockPnumCheck(searchPnum);

					if (searchPnum.equals("")) {
						searchResult = true;

						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("상품 정보 품번 조회");
						alert.setHeaderText("상품의 품번을 입력하시오");
						alert.setContentText("다음엔 주의하세요");
						alert.showAndWait();

					}

					if (!searchPnum.equals("") && (sVo != null)) {
						ArrayList<String> title;
						ArrayList<StockVO> list;

						title = sDao.getColumnName();
						int columnCount = title.size();

						list = sDao.getStockTotal();
						int rowCount = list.size();

						totalData = new Object[rowCount][columnCount];

						if (sVo.getSt_pnum().equals(searchPnum)) {
							txtStockPnumSearch.clear();
							data.removeAll(data);
							for (int index = 0; index < rowCount; index++) {
								System.out.println(index);
								sVo = list.get(index);
								if (sVo.getSt_pnum().equals(searchPnum)) {
									data.add(sVo);
									searchResult = true;
								}
							}
						}

						// 조회한 결과값의 총 수량과 총액 송출
						totalCountData = sDao.getStockPnumTotalCount(searchPnum).getSt_stockTotalCount();
						totalPriceData = sDao.getStockPnumTotalCount(searchPnum).getSt_stockTotalPrice();

						txtStockTotalCount.setText(totalCountData + "");
						txtStockTotalPrice.setText(totalPriceData + "");

					}

					if (!searchResult) {
						txtStockPnumSearch.clear();

						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("상품 정보 품번 조회");
						alert.setHeaderText(searchPnum + " 번의 상품이 리스트에 없습니다.");
						alert.setContentText("다시 검색하세요");
						alert.showAndWait();
					}

				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("상품 정보 품번 조회");
					alert.setHeaderText("상품 정보 검색에 오류가 발생하였습니다.");
					alert.setContentText("다시하세요");
					alert.showAndWait();

				}
			});

			// 품종으로 조회 버튼 기능설정
			btnStockPkindSearch.setOnAction(ev -> {
				StockVO sVo = new StockVO();
				StockDAO sDao = null;

				Object[][] totalData = null;

				String searchPkind = null;
				boolean searchResult = false;

				try {
					searchPkind = txtStockPkindSearch.getText().trim();
					sDao = new StockDAO();
					sVo = sDao.getStockPkindCheck(searchPkind);

					if (searchPkind.equals("")) {
						searchResult = true;

						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("상품 정보 품종 조회");
						alert.setHeaderText("상품의 품종을 입력하시오");
						alert.setContentText("다음엔 주의하세요");
						alert.showAndWait();

					}

					if (!searchPkind.equals("") && (sVo != null)) {
						ArrayList<String> title;
						ArrayList<StockVO> list;

						title = sDao.getColumnName();
						int columnCount = title.size();

						list = sDao.getStockTotal();
						int rowCount = list.size();

						totalData = new Object[rowCount][columnCount];

						// if (sVo.getSt_pkind().equals(searchPkind)) {
						txtStockPkindSearch.clear();
						data.removeAll(data);
						for (int index = 0; index < rowCount; index++) {
							System.out.println(index);
							sVo = list.get(index);
							if (sVo.getSt_pkind().equals(searchPkind)) {
								data.add(sVo);
								searchResult = true;
							}
						}
						// }

						// 조회한 결과값의 총 수량과 총액 송출
						totalCountData = sDao.getStockPkindTotalCount(searchPkind).getSt_stockTotalCount();
						totalPriceData = sDao.getStockPkindTotalCount(searchPkind).getSt_stockTotalPrice();

						txtStockTotalCount.setText(totalCountData + "");
						txtStockTotalPrice.setText(totalPriceData + "");
					}

					if (!searchResult) {
						txtStockPkindSearch.clear();

						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("상품 정보 품종 조회");
						alert.setHeaderText(searchPkind + " 인 종류의 상품이 리스트에 없습니다.");
						alert.setContentText("다시 검색하세요");
						alert.showAndWait();
					}

				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("상품 정보 품종 조회");
					alert.setHeaderText("상품 정보 검색에 오류가 발생하였습니다.");
					alert.setContentText("다시하세요");
					alert.showAndWait();

				}
			});

			// 재고 전체 리스트/ 초기화 버튼 기능설정
			btnStockTotalSearch.setOnAction(e -> {
				try {

					data.removeAll(data);
					totalList();
					localUrl = "/image/default.gif";
					localImage = new Image(localUrl, false);

					stockImageView.setFitWidth(300);
					stockImageView.setFitHeight(300);
					stockImageView.setImage(localImage);

					// 총량 , 총액 텍스트필드 표시
					totalCountData = sDao1.getStockTotalCount(svo1).getSt_stockTotalCount();
					totalPriceData = sDao1.getStockTotalCount(svo1).getSt_stockTotalPrice();

					txtStockTotalCount.setText(totalCountData + "");
					txtStockTotalPrice.setText(totalPriceData + "");

					// 수정 삭제 버튼 사용불가상태설정
					btnEditGoods.setDisable(true);
					btnDeleteGoods.setDisable(true);
					btnOrderSalesRate.setDisable(true);

				} catch (Exception e1) {

				}
			});

			// 재고창 수정 버튼 이벤트 처리
			btnEditGoods.setOnAction(e -> {

				try {
					/**************************
					 * 재고 수정 화면 띄우기
					 ***********************/
					FXMLLoader editLoader = new FXMLLoader();
					editLoader.setLocation(getClass().getResource("/View/editGoods.fxml"));

					Stage editDialog = new Stage(StageStyle.DECORATED);
					editDialog.initModality(Modality.WINDOW_MODAL);
					editDialog.initOwner(btnEditGoods.getScene().getWindow());
					editDialog.setTitle("상품 정보 수정");

					Parent parentEdit = (Parent) editLoader.load();

					Scene editScene = new Scene(parentEdit);
					editDialog.setScene(editScene);
					editDialog.setResizable(false);
					editDialog.show();

					/*******************************
					 * 수정창 버튼, 텍스트필드 정의
					 ****************************/

					TextField txtStockEditPnum = (TextField) parentEdit.lookup("#txtStockEditPnum");
					TextField txtStockEditPname = (TextField) parentEdit.lookup("#txtStockEditPname");
					TextField txtStockEditPkind = (TextField) parentEdit.lookup("#txtStockEditPkind");
					TextField txtStockEditSize = (TextField) parentEdit.lookup("#txtStockEditSize");
					TextField txtStockEditColor = (TextField) parentEdit.lookup("#txtStockEditColor");
					TextField txtStockEditPrice = (TextField) parentEdit.lookup("#txtStockEditPrice");

					Button btnStockEditOk = (Button) parentEdit.lookup("#btnStockEditOk");
					Button btnStockEditExit = (Button) parentEdit.lookup("#btnStockEditExit");

					/******************************
					 * 수정창 버튼 기능설정
					 ***************************/
					// 수정 처리를 위해 선택된 상품정보와 인덱스 정의
					StockVO stockEdit = stockTableView.getSelectionModel().getSelectedItem();
					selectedIndex = stockTableView.getSelectionModel().getSelectedIndex();

					txtStockEditPnum.setText(stockEdit.getSt_pnum());
					txtStockEditPname.setText(stockEdit.getSt_pname());
					txtStockEditPkind.setText(stockEdit.getSt_pkind());
					txtStockEditSize.setText(stockEdit.getSt_size());
					txtStockEditColor.setText(stockEdit.getSt_color());
					txtStockEditPrice.setText(stockEdit.getSt_price() + "");

					// 수정창 수정 버튼 기능
					btnStockEditOk.setOnAction(e1 -> {

						try {

							StockVO sVo = null;
							StockDAO sDao = null;

							if (txtStockEditPnum.getText().equals("") || txtStockEditPname.getText().equals("")
									|| txtStockEditPkind.getText().equals("") || txtStockEditSize.getText().equals("")
									|| txtStockEditColor.getText().equals("")
									|| txtStockEditPrice.getText().equals("")) {

								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("상품 정보 수정");
								alert.setHeaderText("상품 정보를 입력하세요");
								alert.setContentText("상품 정보가 누락되었습니다.");
								alert.showAndWait();

							} else if (Integer.parseInt(txtStockEditPrice.getText()) <= 0) {

								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("상품 정보 수정");
								alert.setHeaderText("상품 단가를 확인하세요");
								alert.setContentText("상품단가는 0원 혹은 - 가 될수 없습니다.");
								alert.showAndWait();

							} else {

								data.remove(selectedIndex);

								sVo = new StockVO(txtStockEditPnum.getText(), txtStockEditPname.getText(),
										txtStockEditPkind.getText(), txtStockEditSize.getText(),
										txtStockEditColor.getText(), Integer.parseInt(txtStockEditPrice.getText()));

								sDao = new StockDAO();
								sDao.getStockUpdate(sVo, stockEdit.getNo(), stockEdit.getSt_count());
								data.removeAll(data);
								totalList();

								try {

									// 총량 , 총액 텍스트필드 표시
									totalCountData = sDao1.getStockTotalCount(svo1).getSt_stockTotalCount();
									totalPriceData = sDao1.getStockTotalCount(svo1).getSt_stockTotalPrice();

									txtStockTotalCount.setText(totalCountData + "");
									txtStockTotalPrice.setText(totalPriceData + "");

								} catch (Exception e4) {

								}

								editDialog.close();

								// 기본이미지 표출
								localUrl = "/image/default.gif";
								localImage = new Image(localUrl, false);
								stockImageView.setFitWidth(300);
								stockImageView.setFitHeight(300);
								stockImageView.setImage(localImage);

							}

						} catch (Exception e2) {

							data.removeAll(data);
							totalList();

							try {

								// 총량 , 총액 텍스트필드 표시
								totalCountData = sDao1.getStockTotalCount(svo1).getSt_stockTotalCount();
								totalPriceData = sDao1.getStockTotalCount(svo1).getSt_stockTotalPrice();

								txtStockTotalCount.setText(totalCountData + "");
								txtStockTotalPrice.setText(totalPriceData + "");

							} catch (Exception e4) {

							}

							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("수정 정보 입력오류");
							alert.setHeaderText("수정할 정보를 확인후 다시 시도해주세요");
							alert.setContentText("단가는 숫자만 기입가능합니다");
							alert.showAndWait();

						}

					});

					// 수정창 취소 버튼 기능
					btnStockEditExit.setOnAction(e1 -> {
						editDialog.close();
					});

				} catch (IOException e1) {
					System.out.println("재고 수정창 관련 오류 " + e1);

				}

			});

			// 재고창 삭제 버튼 이벤트 처리
			btnDeleteGoods.setOnAction(e -> {
				StockDAO sDao = null;
				sDao = new StockDAO();

				try {
					// 삭제 버튼 누를시 알람메세지 팝업설정
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("삭제메세지");
					alert.setHeaderText("삭제한 데이터는 복구하실수 없습니다.");
					alert.setContentText("삭제를 진행하시겠습니까?");

					Optional<ButtonType> result = alert.showAndWait();

					if (result.get() == ButtonType.OK) {
						sDao.getStockDelete(no);
						data.removeAll(data);

						// 학생 전체정보
						totalList();

						// 이미지 파일 삭제
						stockImageDelete(selectFileName);

						// 기본이미지 표출
						localUrl = "/image/default.gif";
						localImage = new Image(localUrl, false);
						stockImageView.setFitWidth(300);
						stockImageView.setFitHeight(300);
						stockImageView.setImage(localImage);

						// 총 수량, 총액 적용
						totalCountData = sDao1.getStockTotalCount(svo1).getSt_stockTotalCount();
						totalPriceData = sDao1.getStockTotalCount(svo1).getSt_stockTotalPrice();

						txtStockTotalCount.setText(totalCountData + "");
						txtStockTotalPrice.setText(totalPriceData + "");

					} else {

					}
				} catch (Exception e1) {
					System.out.println("삭제버튼 관련 오류" + e1);
				}

			});

			// 재고창 종료 버튼 이벤트처리
			btnStockExit.setOnAction(e -> {
				dialog.close();
			});

		} catch (

		Exception e) {
			System.out.println("재고 모달창 관련 오류!!!!!\n" + e);

		}
	}

	/***************************************
	 * 이미지 삭제 메소드
	 * 
	 * @param (삭제할
	 *            파일명)
	 * @return 삭제 여부를 리턴
	 ****************************************/
	public boolean stockImageDelete(String fileName) {
		boolean result = false;

		try {
			// 삭제할 이미지 파일
			File stockFileDelete = new File(dirSave.getAbsolutePath() + "\\" + fileName);
			if (stockFileDelete.exists() && stockFileDelete.isFile()) {

				result = stockFileDelete.delete();

			}

		} catch (Exception ie) {
			System.out.println("삭제 메소드 오류" + ie);
			result = false;
		}

		return result;

	}

	// 상품 전체 리스트 메소드
	public void totalList() {
		Object[][] totalData;

		StockDAO sDao = new StockDAO();
		StockVO sVo = null;
		ArrayList<String> title;
		ArrayList<StockVO> list;

		title = sDao.getColumnName();
		int columnCount = title.size();

		list = sDao.getStockTotal();
		int rowCount = list.size();

		totalData = new Object[rowCount][columnCount];

		for (int index = 0; index < rowCount; index++) {
			sVo = list.get(index);
			data.add(sVo);
		}

	}

	// 이미지저장 메소드
	public String imageSave(File file) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		int data = -1;
		String fileName = null;
		try {
			// 이미지 파일명 생성
			fileName = "Stock" + System.currentTimeMillis() + "_" + file.getName();
			bis = new BufferedInputStream(new FileInputStream(file));
			bos = new BufferedOutputStream(new FileOutputStream(dirSave.getAbsolutePath() + "\\" + fileName));

			// 선택한 이미지파일 InputStream 의 마지막에 이르렀을경우는 -1
			while ((data = bis.read()) != -1) {
				bos.write(data);
				bos.flush();
			}
		} catch (Exception e) {
			e.getMessage();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				e.getMessage();
			}
		}
		return fileName;
	}

	// 주문창 다이얼로그 생성
	public void handleBtnOrderAction(ActionEvent event) {

		data.removeAll(data);
		orderData.removeAll(orderData);
		wareData.removeAll(wareData);

		try {

			/************************************
			 * 주문/ 입고 화면 띄우기
			 ****************************/
			FXMLLoader orderLoader = new FXMLLoader();
			orderLoader.setLocation(getClass().getResource("/View/order.fxml"));

			Stage orderdialog = new Stage(StageStyle.DECORATED);
			orderdialog.initModality(Modality.WINDOW_MODAL);
			orderdialog.initOwner(btnOrder.getScene().getWindow());
			orderdialog.setTitle("주문 / 입고 관리");

			Parent parentOrder = (Parent) orderLoader.load();
			Scene scene = new Scene(parentOrder);
			orderdialog.setScene(scene);
			orderdialog.setResizable(false);
			orderdialog.show();

			/***********************************
			 * 주문 화면 버튼, 텍스트필드, 스크롤바 정의
			 **********************************/

			TextField txtOrderSearch = (TextField) parentOrder.lookup("#txtOrderSearch");
			TextField txtOrderListTotalCount = (TextField) parentOrder.lookup("#txtOrderListTotalCount");
			TextField txtOrderListTotalPrice = (TextField) parentOrder.lookup("#txtOrderListTotalPrice");
			TextField txtWarehousingListTotalCount = (TextField) parentOrder.lookup("#txtWarehousingListTotalCount");
			TextField txtWarehousingListTotalPrice = (TextField) parentOrder.lookup("#txtWarehousingListTotalPrice");
			TextField txtOrderSaveFileDir = (TextField) parentOrder.lookup("#txtOrderSaveFileDir");
			TextField txtWareSaveFileDir = (TextField) parentOrder.lookup("#txtWareSaveFileDir");

			Button btnOrderSaveFileDir = (Button) parentOrder.lookup("#btnOrderSaveFileDir");
			Button btnOrderExcel = (Button) parentOrder.lookup("#btnOrderExcel");
			Button btnOrderPDF = (Button) parentOrder.lookup("#btnOrderPDF");
			Button btnWareSaveFileDir = (Button) parentOrder.lookup("#btnWareSaveFileDir");
			Button btnWareExcel = (Button) parentOrder.lookup("#btnWareExcel");
			Button btnWarePDF = (Button) parentOrder.lookup("#btnWarePDF");
			Button btnOrderSearch = (Button) parentOrder.lookup("#btnOrderSearch");
			Button btnOrderSearchClear = (Button) parentOrder.lookup("#btnOrderSearchClear");
			Button btnOrderCount = (Button) parentOrder.lookup("#btnOrderCount");
			Button btnOrderEdit = (Button) parentOrder.lookup("#btnOrderEdit");
			Button btnOrderDelete = (Button) parentOrder.lookup("#btnOrderDelete");
			Button btnWarehousingEdit = (Button) parentOrder.lookup("#btnWarehousingEdit");
			Button btnWarehousing = (Button) parentOrder.lookup("#btnWarehousing");
			Button btnOrderExit = (Button) parentOrder.lookup("#btnOrderExit");
			Button btnOrderListSearch = (Button) parentOrder.lookup("#btnOrderListSearch");
			Button btnOrderListTotal = (Button) parentOrder.lookup("#btnOrderListTotal");
			Button btnWarehousingListSearch = (Button) parentOrder.lookup("#btnWarehousingListSearch");
			Button btnWarehousingListTotal = (Button) parentOrder.lookup("#btnWarehousingListTotal");

			TableView<StockVO> OrderSearchTableView = (TableView) parentOrder.lookup("#OrderSearchTableView");
			TableView<OrderVO> OrderListTableView = (TableView) parentOrder.lookup("#OrderListTableView");
			TableView<WarehousingVO> WarehousingListTableView = (TableView) parentOrder
					.lookup("#WarehousingListTableView");

			ImageView OrderImageView = (ImageView) parentOrder.lookup("#OrderImageView");

			StockDAO sDao1 = new StockDAO();
			// 주문탭 상품번호 검색 콤보박스 정의
			ComboBox<String> cbOrderSearch = (ComboBox) parentOrder.lookup("#cbOrderSearch");
			cbOrderSearch.setItems(FXCollections.observableArrayList(sDao1.choiceStockPnum()));

			// 주문목록 년도 콤보박스 정의, 선택가능 선택지 입력
			ComboBox<String> cbOrderListYear = (ComboBox) parentOrder.lookup("#cbOrderListYear");
			cbOrderListYear.setItems(FXCollections.observableArrayList("2015", "2016", "2017"));

			// 주문목록 월 콤보박스 정의, 선택가능 선택지 입력
			ComboBox<String> cbOrderListMonth = (ComboBox) parentOrder.lookup("#cbOrderListMonth");
			cbOrderListMonth.setItems(FXCollections.observableArrayList("01", "02", "03", "04", "05", "06", "07", "08",
					"09", "10", "11", "12"));

			// 입고목록 년도 콤보박스 정의, 선택가능 선택지 입력
			ComboBox<String> cbWarehousingListYear = (ComboBox) parentOrder.lookup("#cbWarehousingListYear");
			cbWarehousingListYear.setItems(FXCollections.observableArrayList("2015", "2016", "2017"));

			// 입고목록 월 콤보박스 정의, 선택가능 선택지 입력
			ComboBox<String> cbWarehousingListMonth = (ComboBox) parentOrder.lookup("#cbWarehousingListMonth");
			cbWarehousingListMonth.setItems(FXCollections.observableArrayList("01", "02", "03", "04", "05", "06", "07",
					"08", "09", "10", "11", "12"));

			/*************************************
			 * 테이블뷰 칼럼 설정
			 ************************************/

			// 주문 상품 검색창 테이블뷰 컬럼설정

			TableColumn colst_pnum = new TableColumn("품번");
			colst_pnum.setMinWidth(110);
			colst_pnum.setStyle("-fx-alignment:CENTER");
			colst_pnum.setCellValueFactory(new PropertyValueFactory<>("st_pnum"));

			TableColumn colst_pname = new TableColumn("품명");
			colst_pname.setMinWidth(130);
			colst_pname.setStyle("-fx-alignment:CENTER");
			colst_pname.setCellValueFactory(new PropertyValueFactory<>("st_pname"));

			TableColumn colst_pkind = new TableColumn("품종");
			colst_pkind.setMinWidth(80);
			colst_pkind.setStyle("-fx-alignment:CENTER");
			colst_pkind.setCellValueFactory(new PropertyValueFactory<>("st_pkind"));

			TableColumn colst_size = new TableColumn("사이즈");
			colst_size.setMinWidth(70);
			colst_size.setStyle("-fx-alignment:CENTER");
			colst_size.setCellValueFactory(new PropertyValueFactory<>("st_size"));

			TableColumn colst_color = new TableColumn("컬러");
			colst_color.setMinWidth(50);
			colst_color.setStyle("-fx-alignment:CENTER");
			colst_color.setCellValueFactory(new PropertyValueFactory<>("st_color"));

			TableColumn colst_price = new TableColumn("단가");
			colst_price.setMinWidth(70);
			colst_price.setStyle("-fx-alignment:CENTER");
			colst_price.setCellValueFactory(new PropertyValueFactory<>("st_price"));

			TableColumn colst_count = new TableColumn("재고수량");
			colst_count.setMinWidth(40);
			colst_count.setStyle("-fx-alignment:CENTER");
			colst_count.setCellValueFactory(new PropertyValueFactory<>("st_count"));

			TableColumn colst_filename = new TableColumn("파일명");
			colst_filename.setMinWidth(200);
			colst_filename.setCellValueFactory(new PropertyValueFactory<>("st_filename"));

			OrderSearchTableView.getColumns().addAll(colst_pnum, colst_pname, colst_pkind, colst_size, colst_color,
					colst_price, colst_count, colst_filename);

			OrderSearchTableView.setItems(data);

			// 주문 목록 테이블 설정

			/*
			 * TableColumn colOrderNo = new TableColumn("NO"); colOrderNo.setMinWidth(40);
			 * colOrderNo.setStyle("-fx-alignment:CENTER");
			 * colOrderNo.setCellValueFactory(new PropertyValueFactory<>("no"));
			 */

			TableColumn colo_num = new TableColumn("주문번호");
			colo_num.setMinWidth(100);
			colo_num.setStyle("-fx-alignment:CENTER");
			colo_num.setCellValueFactory(new PropertyValueFactory<>("o_num"));

			TableColumn colo_pnum = new TableColumn("주문품번");
			colo_pnum.setMinWidth(120);
			colo_pnum.setStyle("-fx-alignment:CENTER");
			colo_pnum.setCellValueFactory(new PropertyValueFactory<>("o_pnum"));

			TableColumn colo_pname = new TableColumn("주문품명");
			colo_pname.setMinWidth(140);
			colo_pname.setStyle("-fx-alignment:CENTER");
			colo_pname.setCellValueFactory(new PropertyValueFactory<>("o_pname"));

			TableColumn colo_pkind = new TableColumn("주문품종");
			colo_pkind.setMinWidth(80);
			colo_pkind.setStyle("-fx-alignment:CENTER");
			colo_pkind.setCellValueFactory(new PropertyValueFactory<>("o_pkind"));

			TableColumn colo_size = new TableColumn("사이즈");
			colo_size.setMinWidth(100);
			colo_size.setStyle("-fx-alignment:CENTER");
			colo_size.setCellValueFactory(new PropertyValueFactory<>("o_size"));

			TableColumn colo_color = new TableColumn("컬러");
			colo_color.setMinWidth(60);
			colo_color.setStyle("-fx-alignment:CENTER");
			colo_color.setCellValueFactory(new PropertyValueFactory<>("o_color"));

			TableColumn colo_price = new TableColumn("단가");
			colo_price.setMinWidth(80);
			colo_price.setStyle("-fx-alignment:CENTER");
			colo_price.setCellValueFactory(new PropertyValueFactory<>("o_price"));

			TableColumn colo_count = new TableColumn("주문수량");
			colo_count.setMinWidth(60);
			colo_count.setStyle("-fx-alignment:CENTER");
			colo_count.setCellValueFactory(new PropertyValueFactory<>("o_count"));

			TableColumn colo_totalPrice = new TableColumn("총금액");
			colo_totalPrice.setMinWidth(100);
			colo_totalPrice.setStyle("-fx-alignment:CENTER");
			colo_totalPrice.setCellValueFactory(new PropertyValueFactory<>("o_totalPrice"));

			TableColumn colo_date = new TableColumn("주문날짜");
			colo_date.setMinWidth(100);
			colo_date.setStyle("-fx-alignment:CENTER");
			colo_date.setCellValueFactory(new PropertyValueFactory<>("o_date"));

			TableColumn colo_filename = new TableColumn("파일명");
			colo_filename.setMinWidth(200);
			colo_filename.setCellValueFactory(new PropertyValueFactory<>("o_fileName"));

			// 테이블뷰에 열 추가
			OrderListTableView.getColumns().addAll(colo_date, colo_num, colo_pnum, colo_pname, colo_count, colo_pkind,
					colo_size, colo_color, colo_price, colo_totalPrice, colo_filename);

			OrderListTableView.setItems(orderData);

			// 입고 목록 테이블 설정

			/*
			 * TableColumn colOrderNo = new TableColumn("NO"); colOrderNo.setMinWidth(40);
			 * colOrderNo.setStyle("-fx-alignment:CENTER");
			 * colOrderNo.setCellValueFactory(new PropertyValueFactory<>("no"));
			 */

			TableColumn colw_num = new TableColumn("입고번호");
			colw_num.setMinWidth(100);
			colw_num.setStyle("-fx-alignment:CENTER");
			colw_num.setCellValueFactory(new PropertyValueFactory<>("w_num"));

			TableColumn colw_pnum = new TableColumn("입고품번");
			colw_pnum.setMinWidth(120);
			colw_pnum.setStyle("-fx-alignment:CENTER");
			colw_pnum.setCellValueFactory(new PropertyValueFactory<>("w_pnum"));

			TableColumn colw_pname = new TableColumn("입고품명");
			colw_pname.setMinWidth(140);
			colw_pname.setStyle("-fx-alignment:CENTER");
			colw_pname.setCellValueFactory(new PropertyValueFactory<>("w_pname"));

			TableColumn colw_pkind = new TableColumn("입고품종");
			colw_pkind.setMinWidth(80);
			colw_pkind.setStyle("-fx-alignment:CENTER");
			colw_pkind.setCellValueFactory(new PropertyValueFactory<>("w_pkind"));

			TableColumn colw_size = new TableColumn("사이즈");
			colw_size.setMinWidth(100);
			colw_size.setStyle("-fx-alignment:CENTER");
			colw_size.setCellValueFactory(new PropertyValueFactory<>("w_size"));

			TableColumn colw_color = new TableColumn("컬러");
			colw_color.setMinWidth(60);
			colw_color.setStyle("-fx-alignment:CENTER");
			colw_color.setCellValueFactory(new PropertyValueFactory<>("w_color"));

			TableColumn colw_price = new TableColumn("단가");
			colw_price.setMinWidth(80);
			colw_price.setStyle("-fx-alignment:CENTER");
			colw_price.setCellValueFactory(new PropertyValueFactory<>("w_price"));

			TableColumn colw_count = new TableColumn("입고수량");
			colw_count.setMinWidth(60);
			colw_count.setStyle("-fx-alignment:CENTER");
			colw_count.setCellValueFactory(new PropertyValueFactory<>("w_count"));

			TableColumn colw_totalPrice = new TableColumn("총금액");
			colw_totalPrice.setMinWidth(100);
			colw_totalPrice.setStyle("-fx-alignment:CENTER");
			colw_totalPrice.setCellValueFactory(new PropertyValueFactory<>("w_totalPrice"));

			TableColumn colw_date = new TableColumn("입고날짜");
			colw_date.setMinWidth(100);
			colw_date.setStyle("-fx-alignment:CENTER");
			colw_date.setCellValueFactory(new PropertyValueFactory<>("w_date"));

			TableColumn colw_filename = new TableColumn("파일명");
			colw_filename.setMinWidth(200);
			colw_filename.setCellValueFactory(new PropertyValueFactory<>("w_fileName"));

			// 테이블뷰에 열 추가
			WarehousingListTableView.getColumns().addAll(colw_date, colw_num, colw_pnum, colw_pname, colw_count,
					colw_pkind, colw_size, colw_color, colw_price, colw_totalPrice, colw_filename);

			WarehousingListTableView.setItems(wareData);

			/***********************************
			 * 기능설정
			 **********************************/
			/*
			 * // 검색텍스트 필드 클릭시 초기화 설정 txtOrderSearch.setOnMouseClicked(e -> {
			 * 
			 * // 디폴트 이미지 설정 localUrl = "/image/default.gif"; localImage = new
			 * Image(localUrl, false); OrderImageView.setFitWidth(300);
			 * OrderImageView.setFitHeight(300); OrderImageView.setImage(localImage);
			 * 
			 * // 수량 설정 버튼 비활성화 btnOrderCount.setDisable(true);
			 * 
			 * // 주문목록 수정 삭제 , 입고등록 버튼 비활성화 btnOrderEdit.setDisable(true);
			 * btnOrderDelete.setDisable(true); btnWarehousing.setDisable(true);
			 * btnWarehousingEdit.setDisable(true);
			 * 
			 * });
			 */

			// 초기 이미지 설정 , 초기 테이블값설정
			localUrl = "/image/default.gif"; // 디폴트 이미지 설정
			localImage = new Image(localUrl, false);
			OrderImageView.setFitWidth(300);
			OrderImageView.setFitHeight(300);
			OrderImageView.setImage(localImage);

			// 초기 화면 주문 목록, 입고목록 전체 리스트 출력
			orderListTotalList();
			warehousingTotalList();

			OrderVO oVo1 = new OrderVO();
			OrderDAO oDao1 = new OrderDAO();
			WarehousingVO wVo1 = new WarehousingVO();
			WarehousingDAO wDao1 = new WarehousingDAO();

			// 주문 총 수량, 총합계
			totalOrderCountData = oDao1.getStockTotalCount(oVo1).getO_orderTotalCount();
			totalOrderPriceData = oDao1.getStockTotalCount(oVo1).getO_orderTotalPrice();

			txtOrderListTotalCount.setText(totalOrderCountData + "");
			txtOrderListTotalPrice.setText(totalOrderPriceData + "");

			// 입고 총 수량, 총합계
			totalWareCountData = wDao1.getWareTotalCount(wVo1).getW_orderTotalCount();
			totalWarePriceData = wDao1.getWareTotalCount(wVo1).getW_orderTotalPrice();

			txtWarehousingListTotalCount.setText(totalWareCountData + "");
			txtWarehousingListTotalPrice.setText(totalWarePriceData + "");

			// 수량설정 초기 비활성화
			btnOrderCount.setDisable(true);

			// 주문목록 입고목록 엑셀, pdf파일 만들기 초기 비활성화
			btnOrderExcel.setDisable(true);
			btnOrderPDF.setDisable(true);
			btnWareExcel.setDisable(true);
			btnWarePDF.setDisable(true);

			// 주문목록 수정 삭제 , 입고등록,삭제 버튼 초기 비활성화
			btnOrderEdit.setDisable(true);
			btnOrderDelete.setDisable(true);
			btnWarehousing.setDisable(true);
			btnWarehousingEdit.setDisable(true);

			// 검색 초기화 버튼 기능설정
			btnOrderSearchClear.setOnAction(e -> {
				data.removeAll(data);
				// txtOrderSearch.clear();
				btnOrderCount.setDisable(true);
				cbOrderSearch.getSelectionModel().clearSelection();

				localUrl = "/image/default.gif"; // 디폴트 이미지 설정
				localImage = new Image(localUrl, false);
				OrderImageView.setFitWidth(300);
				OrderImageView.setFitHeight(300);
				OrderImageView.setImage(localImage);
			});

			// 파일 저장 폴더 선택(주문 목록)
			btnOrderSaveFileDir.setOnAction(e -> {
				final DirectoryChooser directoryChooser = new DirectoryChooser();
				final File selectedDirectory = directoryChooser.showDialog(primaryStage);

				if (selectedDirectory != null) {
					txtOrderSaveFileDir.setText(selectedDirectory.getAbsolutePath());
					btnOrderExcel.setDisable(false);
					btnOrderPDF.setDisable(false);

				}

			});

			// 파일 저장 폴더 선택(입고 목록)
			btnWareSaveFileDir.setOnAction(e -> {
				final DirectoryChooser directoryChooser = new DirectoryChooser();
				final File selectedDirectory = directoryChooser.showDialog(primaryStage);

				if (selectedDirectory != null) {
					txtWareSaveFileDir.setText(selectedDirectory.getAbsolutePath());
					btnWareExcel.setDisable(false);
					btnWarePDF.setDisable(false);

				}

			});

			// 엑셀 파일 생성버튼 (주문목록)
			btnOrderExcel.setOnAction(e -> {
				OrderDAO oDao8 = new OrderDAO();
				boolean saveSuccess;

				ArrayList<OrderVO> list;
				list = oDao8.getOrderTotal();
				OrderExcel excelWriter = new OrderExcel();

				// xlsx파일 쓰기
				saveSuccess = excelWriter.xlsxOrderWiter(list, txtOrderSaveFileDir.getText());
				if (saveSuccess) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("엑셀 파일 생성");
					alert.setHeaderText("주문 목록 엑셀 파일 생성 성공");
					alert.setContentText("주문 목록 엑셀 파일");
					alert.showAndWait();

				}

				txtOrderSaveFileDir.clear();
				btnOrderExcel.setDisable(true);
				btnOrderPDF.setDisable(true);

			});

			// 엑셀 파일 생성버튼 (입고목록)
			btnWareExcel.setOnAction(e -> {
				WarehousingDAO wDao8 = new WarehousingDAO();
				boolean saveSuccess;

				ArrayList<WarehousingVO> list;
				list = wDao8.getWareTotal();
				WareExcel excelWriter = new WareExcel();

				// xlsx파일 쓰기
				saveSuccess = excelWriter.xlsxWiter(list, txtWareSaveFileDir.getText());
				if (saveSuccess) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("엑셀 파일 생성");
					alert.setHeaderText("입고 목록 엑셀 파일 생성 성공");
					alert.setContentText("입고 목록 엑셀 파일");
					alert.showAndWait();

				}

				txtWareSaveFileDir.clear();
				btnWareExcel.setDisable(true);
				btnWarePDF.setDisable(true);

			});

			// pdf파일 생성 (주문목록)
			btnOrderPDF.setOnAction(e -> {
				try {
					// pdf document 선언
					Document document = new Document(PageSize.A4, 0, 0, 30, 30);
					// pdf파일을 저장할 공간선언, pdf파일이 생성되고 그후 스트림으로 저장됨
					String strReportPDFName = "order_" + System.currentTimeMillis() + ".pdf";
					PdfWriter.getInstance(document,
							new FileOutputStream(txtOrderSaveFileDir.getText() + "\\" + strReportPDFName));
					// document를 열어 pdf문서를 작성할수 있도록한다.
					document.open();
					// 한글 지원 폰트 설정
					BaseFont bf = BaseFont.createFont("font/MALGUN.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
					Font font = new Font(bf, 8, Font.NORMAL);
					Font font2 = new Font(bf, 14, Font.BOLD);

					// 타이틀 설정
					Paragraph title = new Paragraph("주문 목록", font2);
					// 가운데 정렬
					title.setAlignment(Element.ALIGN_CENTER);
					// 문서에 추가
					document.add(title);
					document.add(new Paragraph("\r\n"));

					// 생성 날짜
					LocalDate date = LocalDate.now();
					Paragraph writeDay = new Paragraph(date.toString(), font);

					// 오른쪽 정렬
					writeDay.setAlignment(Element.ALIGN_RIGHT);
					// 문서에 추가
					document.add(writeDay);
					document.add(new Paragraph("\r\n"));

					// 테이블 생성, Table객체 보다 pdftable객체가 더 정교하게 테이블을 구성할수 있다.
					// 생성자 수에 컬럼수를 적어준다
					PdfPTable table = new PdfPTable(12);
					// 컬럼의 폭을 정한다.
					table.setWidths(new int[] { 30, 70, 70, 70, 50, 50, 40, 60, 40, 60, 70, 100 });

					// 컬럼 타이틀 설정
					PdfPCell header1 = new PdfPCell(new Paragraph("번호", font));
					PdfPCell header2 = new PdfPCell(new Paragraph("주문번호", font));
					PdfPCell header3 = new PdfPCell(new Paragraph("주문품번", font));
					PdfPCell header4 = new PdfPCell(new Paragraph("주문품명", font));
					PdfPCell header5 = new PdfPCell(new Paragraph("품종", font));
					PdfPCell header6 = new PdfPCell(new Paragraph("사이즈", font));
					PdfPCell header7 = new PdfPCell(new Paragraph("컬러", font));
					PdfPCell header8 = new PdfPCell(new Paragraph("주문단가", font));
					PdfPCell header9 = new PdfPCell(new Paragraph("주문수량", font));
					PdfPCell header10 = new PdfPCell(new Paragraph("주문금액", font));
					PdfPCell header11 = new PdfPCell(new Paragraph("주문날짜", font));
					PdfPCell header12 = new PdfPCell(new Paragraph("이미지명", font));

					// 가로정렬
					header1.setHorizontalAlignment(Element.ALIGN_CENTER);
					header2.setHorizontalAlignment(Element.ALIGN_CENTER);
					header3.setHorizontalAlignment(Element.ALIGN_CENTER);
					header4.setHorizontalAlignment(Element.ALIGN_CENTER);
					header5.setHorizontalAlignment(Element.ALIGN_CENTER);
					header6.setHorizontalAlignment(Element.ALIGN_CENTER);
					header7.setHorizontalAlignment(Element.ALIGN_CENTER);
					header8.setHorizontalAlignment(Element.ALIGN_CENTER);
					header9.setHorizontalAlignment(Element.ALIGN_CENTER);
					header10.setHorizontalAlignment(Element.ALIGN_CENTER);
					header11.setHorizontalAlignment(Element.ALIGN_CENTER);
					header12.setHorizontalAlignment(Element.ALIGN_CENTER);

					// 세로정렬
					header1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header4.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header5.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header6.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header7.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header8.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header9.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header10.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header11.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header12.setVerticalAlignment(Element.ALIGN_MIDDLE);

					// 테이블에 셀 추가
					table.addCell(header1);
					table.addCell(header2);
					table.addCell(header3);
					table.addCell(header4);
					table.addCell(header5);
					table.addCell(header6);
					table.addCell(header7);
					table.addCell(header8);
					table.addCell(header9);
					table.addCell(header10);
					table.addCell(header11);
					table.addCell(header12);

					// DB연결 및 리스트 선택
					OrderDAO odao8 = new OrderDAO();
					OrderVO ovo8 = new OrderVO();
					ArrayList<OrderVO> list;
					list = odao8.getOrderTotal();
					int rowCount = list.size();

					PdfPCell cell1 = null;
					PdfPCell cell2 = null;
					PdfPCell cell3 = null;
					PdfPCell cell4 = null;
					PdfPCell cell5 = null;
					PdfPCell cell6 = null;
					PdfPCell cell7 = null;
					PdfPCell cell8 = null;
					PdfPCell cell9 = null;
					PdfPCell cell10 = null;
					PdfPCell cell11 = null;
					PdfPCell cell12 = null;

					for (int index = 0; index < rowCount; index++) {
						ovo8 = list.get(index);
						cell1 = new PdfPCell(new Paragraph(ovo8.getNo() + "", font));
						cell2 = new PdfPCell(new Paragraph(ovo8.getO_num() + "", font));
						cell3 = new PdfPCell(new Paragraph(ovo8.getO_pnum() + "", font));
						cell4 = new PdfPCell(new Paragraph(ovo8.getO_pname() + "", font));
						cell5 = new PdfPCell(new Paragraph(ovo8.getO_pkind() + "", font));
						cell6 = new PdfPCell(new Paragraph(ovo8.getO_size() + "", font));
						cell7 = new PdfPCell(new Paragraph(ovo8.getO_color() + "", font));
						cell8 = new PdfPCell(new Paragraph(ovo8.getO_price() + "", font));
						cell9 = new PdfPCell(new Paragraph(ovo8.getO_count() + "", font));
						cell10 = new PdfPCell(new Paragraph(ovo8.getO_totalPrice() + "", font));
						cell11 = new PdfPCell(new Paragraph(ovo8.getO_date() + "", font));
						cell12 = new PdfPCell(new Paragraph(ovo8.getO_fileName() + "", font));

						// 가로 정렬
						cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell12.setHorizontalAlignment(Element.ALIGN_CENTER);

						// 세로 정렬
						cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell12.setVerticalAlignment(Element.ALIGN_MIDDLE);

						// 테이블에 셀 추가
						table.addCell(cell1);
						table.addCell(cell2);
						table.addCell(cell3);
						table.addCell(cell4);
						table.addCell(cell5);
						table.addCell(cell6);
						table.addCell(cell7);
						table.addCell(cell8);
						table.addCell(cell9);
						table.addCell(cell10);
						table.addCell(cell11);
						table.addCell(cell12);
					}
					// document 에 테이블 추가
					document.add(table);
					// 닫기 쓰기 종료
					document.close();

					txtOrderSaveFileDir.clear();
					btnOrderPDF.setDisable(true);
					btnOrderExcel.setDisable(true);

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("PDF파일 생성");
					alert.setHeaderText("주문 목록 pdf파일 생성 성공");
					alert.setContentText("주문 목록 PDF파일");
					alert.showAndWait();

				} catch (FileNotFoundException e0) {
					e0.printStackTrace();

				} catch (DocumentException e0) {
					e0.printStackTrace();
				} catch (IOException e0) {
					e0.printStackTrace();
				}
			});

			// PDF파일 생성 (입고 목록)
			btnWarePDF.setOnAction(e -> {

				try {
					// pdf document 선언
					Document document = new Document(PageSize.A4, 0, 0, 30, 30);
					// pdf파일을 저장할 공간선언, pdf파일이 생성되고 그후 스트림으로 저장됨
					String strReportPDFName = "warehousing_" + System.currentTimeMillis() + ".pdf";
					PdfWriter.getInstance(document,
							new FileOutputStream(txtWareSaveFileDir.getText() + "\\" + strReportPDFName));
					// document를 열어 pdf문서를 작성할수 있도록한다.
					document.open();
					// 한글 지원 폰트 설정
					BaseFont bf = BaseFont.createFont("font/MALGUN.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
					Font font = new Font(bf, 8, Font.NORMAL);
					Font font2 = new Font(bf, 14, Font.BOLD);

					// 타이틀 설정
					Paragraph title = new Paragraph("입고 목록", font2);
					// 가운데 정렬
					title.setAlignment(Element.ALIGN_CENTER);
					// 문서에 추가
					document.add(title);
					document.add(new Paragraph("\r\n"));

					// 생성 날짜
					LocalDate date = LocalDate.now();
					Paragraph writeDay = new Paragraph(date.toString(), font);

					// 오른쪽 정렬
					writeDay.setAlignment(Element.ALIGN_RIGHT);
					// 문서에 추가
					document.add(writeDay);
					document.add(new Paragraph("\r\n"));

					// 테이블 생성, Table객체 보다 pdftable객체가 더 정교하게 테이블을 구성할수 있다.
					// 생성자 수에 컬럼수를 적어준다
					PdfPTable table = new PdfPTable(12);
					// 컬럼의 폭을 정한다.
					table.setWidths(new int[] { 30, 70, 70, 70, 50, 50, 40, 60, 40, 60, 70, 100 });

					// 컬럼 타이틀 설정
					PdfPCell header1 = new PdfPCell(new Paragraph("번호", font));
					PdfPCell header2 = new PdfPCell(new Paragraph("입고번호", font));
					PdfPCell header3 = new PdfPCell(new Paragraph("입고품번", font));
					PdfPCell header4 = new PdfPCell(new Paragraph("입고품명", font));
					PdfPCell header5 = new PdfPCell(new Paragraph("품종", font));
					PdfPCell header6 = new PdfPCell(new Paragraph("사이즈", font));
					PdfPCell header7 = new PdfPCell(new Paragraph("컬러", font));
					PdfPCell header8 = new PdfPCell(new Paragraph("입고단가", font));
					PdfPCell header9 = new PdfPCell(new Paragraph("입고수량", font));
					PdfPCell header10 = new PdfPCell(new Paragraph("입고금액", font));
					PdfPCell header11 = new PdfPCell(new Paragraph("입고날짜", font));
					PdfPCell header12 = new PdfPCell(new Paragraph("이미지명", font));

					// 가로정렬
					header1.setHorizontalAlignment(Element.ALIGN_CENTER);
					header2.setHorizontalAlignment(Element.ALIGN_CENTER);
					header3.setHorizontalAlignment(Element.ALIGN_CENTER);
					header4.setHorizontalAlignment(Element.ALIGN_CENTER);
					header5.setHorizontalAlignment(Element.ALIGN_CENTER);
					header6.setHorizontalAlignment(Element.ALIGN_CENTER);
					header7.setHorizontalAlignment(Element.ALIGN_CENTER);
					header8.setHorizontalAlignment(Element.ALIGN_CENTER);
					header9.setHorizontalAlignment(Element.ALIGN_CENTER);
					header10.setHorizontalAlignment(Element.ALIGN_CENTER);
					header11.setHorizontalAlignment(Element.ALIGN_CENTER);
					header12.setHorizontalAlignment(Element.ALIGN_CENTER);

					// 세로정렬
					header1.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header2.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header4.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header5.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header6.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header7.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header8.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header9.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header10.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header11.setVerticalAlignment(Element.ALIGN_MIDDLE);
					header12.setVerticalAlignment(Element.ALIGN_MIDDLE);

					// 테이블에 셀 추가
					table.addCell(header1);
					table.addCell(header2);
					table.addCell(header3);
					table.addCell(header4);
					table.addCell(header5);
					table.addCell(header6);
					table.addCell(header7);
					table.addCell(header8);
					table.addCell(header9);
					table.addCell(header10);
					table.addCell(header11);
					table.addCell(header12);

					// DB연결 및 리스트 선택
					WarehousingDAO wdao8 = new WarehousingDAO();
					WarehousingVO wvo8 = new WarehousingVO();
					ArrayList<WarehousingVO> list;
					list = wdao8.getWareTotal();
					int rowCount = list.size();

					PdfPCell cell1 = null;
					PdfPCell cell2 = null;
					PdfPCell cell3 = null;
					PdfPCell cell4 = null;
					PdfPCell cell5 = null;
					PdfPCell cell6 = null;
					PdfPCell cell7 = null;
					PdfPCell cell8 = null;
					PdfPCell cell9 = null;
					PdfPCell cell10 = null;
					PdfPCell cell11 = null;
					PdfPCell cell12 = null;

					for (int index = 0; index < rowCount; index++) {
						wvo8 = list.get(index);
						cell1 = new PdfPCell(new Paragraph(wvo8.getNo() + "", font));
						cell2 = new PdfPCell(new Paragraph(wvo8.getW_num() + "", font));
						cell3 = new PdfPCell(new Paragraph(wvo8.getW_pnum() + "", font));
						cell4 = new PdfPCell(new Paragraph(wvo8.getW_pname() + "", font));
						cell5 = new PdfPCell(new Paragraph(wvo8.getW_pkind() + "", font));
						cell6 = new PdfPCell(new Paragraph(wvo8.getW_size() + "", font));
						cell7 = new PdfPCell(new Paragraph(wvo8.getW_color() + "", font));
						cell8 = new PdfPCell(new Paragraph(wvo8.getW_price() + "", font));
						cell9 = new PdfPCell(new Paragraph(wvo8.getW_count() + "", font));
						cell10 = new PdfPCell(new Paragraph(wvo8.getW_totalPrice() + "", font));
						cell11 = new PdfPCell(new Paragraph(wvo8.getW_date() + "", font));
						cell12 = new PdfPCell(new Paragraph(wvo8.getW_fileName() + "", font));

						// 가로 정렬
						cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell12.setHorizontalAlignment(Element.ALIGN_CENTER);

						// 세로 정렬
						cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell12.setVerticalAlignment(Element.ALIGN_MIDDLE);

						// 테이블에 셀 추가
						table.addCell(cell1);
						table.addCell(cell2);
						table.addCell(cell3);
						table.addCell(cell4);
						table.addCell(cell5);
						table.addCell(cell6);
						table.addCell(cell7);
						table.addCell(cell8);
						table.addCell(cell9);
						table.addCell(cell10);
						table.addCell(cell11);
						table.addCell(cell12);
					}
					// document 에 테이블 추가
					document.add(table);
					// 닫기 쓰기 종료
					document.close();

					txtWareSaveFileDir.clear();
					btnWarePDF.setDisable(true);
					btnWareExcel.setDisable(true);

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("PDF파일 생성");
					alert.setHeaderText("입고 목록 pdf파일 생성 성공");
					alert.setContentText("입고 목록 PDF파일");
					alert.showAndWait();

				} catch (FileNotFoundException e0) {
					e0.printStackTrace();

				} catch (DocumentException e0) {
					e0.printStackTrace();
				} catch (IOException e0) {
					e0.printStackTrace();
				}

			});

			// 품번으로 조회 버튼 기능설정
			btnOrderSearch.setOnAction(e -> {
				StockVO sVo = new StockVO();
				StockDAO sDao = null;

				Object[][] totalData = null;

				String orderSearch = null;
				boolean searchResult = false;

				try {
					// orderSearch = txtOrderSearch.getText().trim();
					orderSearch = cbOrderSearch.getSelectionModel().getSelectedItem() + "";
					sDao = new StockDAO();
					sVo = sDao.getStockPnumCheck(orderSearch);

					if (orderSearch == null) {
						searchResult = true;

						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("상품 정보 품번 조회");
						alert.setHeaderText("상품의 품번을 선택하시오");
						alert.setContentText("다음엔 주의하세요");
						alert.showAndWait();

					}

					if ((orderSearch != null) && (sVo != null)) {
						ArrayList<String> title;
						ArrayList<StockVO> list;

						title = sDao.getColumnName();
						int columnCount = title.size();

						list = sDao.getStockTotal();
						int rowCount = list.size();

						totalData = new Object[rowCount][columnCount];

						if (sVo.getSt_pnum().equals(orderSearch)) {
							cbOrderSearch.getSelectionModel().clearSelection();
							data.removeAll(data);
							for (int index = 0; index < rowCount; index++) {
								System.out.println(index);
								sVo = list.get(index);
								if (sVo.getSt_pnum().equals(orderSearch)) {
									data.add(sVo);
									searchResult = true;
								}
							}
						}

					}

					if (!searchResult) {
						txtOrderSearch.clear();
						data.removeAll(data);

						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("상품 정보 품번 조회");
						alert.setHeaderText("이게 뜰리가 없는데? ㅋ");
						alert.setContentText("다시 검색하세요");
						alert.showAndWait();
					}

				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("상품 정보 품번 조회");
					alert.setHeaderText("상품 정보 검색에 오류가 발생하였습니다.");
					alert.setContentText("다시하세요");
					alert.showAndWait();
					e1.printStackTrace();

				}

			});

			// 주문목록 날짜로 조회 버튼 기능설정
			btnOrderListSearch.setOnAction(e -> {
				OrderVO oVo = new OrderVO();
				OrderDAO oDao = null;

				Object[][] totalData = null;

				String orderListYear = null;
				String orderListMonth = null;
				boolean searchResult = false;

				try {
					// 콤보박스에서 선택한 년도를 불러옴
					orderListYear = cbOrderListYear.getSelectionModel().getSelectedItem();
					orderListMonth = cbOrderListMonth.getSelectionModel().getSelectedItem();

					oDao = new OrderDAO();

					// 아무것도 선택 안햇을때 경고창 알람
					if (orderListYear == null && orderListMonth == null) {
						searchResult = true;

						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("주문목록 년도별,월별 조회");
						alert.setHeaderText("주문목록중 검색할 년도와 월을 선택하세요");
						alert.setContentText("다음엔 주의하세요");
						alert.showAndWait();

					}

					// 년도만 선택해서 검색했을때 수행과정
					if (orderListYear != null && orderListMonth == null) {
						ArrayList<String> title;
						ArrayList<OrderVO> list;

						oVo = oDao.getOrderListSearchYear(orderListYear);

						title = oDao.getOrderColumnName();
						int columnCount = title.size();

						list = oDao.getOrderTotal();
						int rowCount = list.size();

						totalData = new Object[rowCount][columnCount];

						if (oVo != null) {
							if (oVo.getO_date().equals(orderListYear)) {
								cbOrderListYear.getSelectionModel().clearSelection();
								orderData.removeAll(orderData);
								for (int index = 0; index < rowCount; index++) {
									System.out.println(index);
									oVo = list.get(index);
									if (oVo.getO_date().equals(orderListYear)) {
										orderData.add(oVo);
										searchResult = true;
									}
								}
							}

							// 년도로 조회한 결과값의 총 수량과 총액 송출
							totalOrderCountData = oDao.getOrderYearTotalCount(orderListYear).getO_orderTotalCount();
							totalOrderPriceData = oDao.getOrderYearTotalCount(orderListYear).getO_orderTotalPrice();

							txtOrderListTotalCount.setText(totalOrderCountData + "");
							txtOrderListTotalPrice.setText(totalOrderPriceData + "");

						} else {
							cbOrderListYear.getSelectionModel().clearSelection();

							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("주문 이력 조회");
							alert.setHeaderText(orderListYear + "년도의 주문이 리스트에 없습니다.");
							alert.setContentText("다시 검색하세요");
							alert.showAndWait();

						}

					}

					// 월만 선택해서 검색햇을때 진행과정
					if (orderListMonth != null && orderListYear == null) {
						ArrayList<String> title;
						ArrayList<OrderVO> list;

						oVo = oDao.getOrderListSearchMonth(orderListMonth);

						title = oDao.getOrderColumnName();
						int columnCount = title.size();

						list = oDao.getOrderTotal();
						int rowCount = list.size();

						totalData = new Object[rowCount][columnCount];

						if (oVo != null) {

							if (oVo.getO_date().contains(orderListMonth)) {

								cbOrderListMonth.getSelectionModel().clearSelection();
								orderData.removeAll(orderData);
								for (int index = 0; index < rowCount; index++) {
									System.out.println(index);
									oVo = list.get(index);
									if (oVo.getO_date().contains(orderListMonth)) {
										orderData.add(oVo);
										searchResult = true;
									}
								}
							}

							// 월로 조회한 결과값의 총 수량과 총액 송출
							totalOrderCountData = oDao.getOrderMonthTotalCount(orderListMonth).getO_orderTotalCount();
							totalOrderPriceData = oDao.getOrderMonthTotalCount(orderListMonth).getO_orderTotalPrice();

							txtOrderListTotalCount.setText(totalOrderCountData + "");
							txtOrderListTotalPrice.setText(totalOrderPriceData + "");

						} else {

							cbOrderListMonth.getSelectionModel().clearSelection();

							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("주문 이력 조회");
							alert.setHeaderText(orderListMonth + "월의 주문이 리스트에 없습니다.");
							alert.setContentText("다시 검색하세요");
							alert.showAndWait();

						}

					}

					// 년도와 월을 둘다 선택해서 검색햇을때 진행과정
					if (orderListMonth != null && orderListYear != null) {
						ArrayList<String> title;
						ArrayList<OrderVO> list;

						oVo = oDao.getOrderListSearchYearMonth(orderListYear, orderListMonth);

						title = oDao.getOrderColumnName();
						int columnCount = title.size();

						list = oDao.getOrderTotal();
						int rowCount = list.size();

						totalData = new Object[rowCount][columnCount];

						if (oVo != null) {

							if (oVo.getO_date().contains(orderListYear + "-" + orderListMonth)) {

								cbOrderListYear.getSelectionModel().clearSelection();
								cbOrderListMonth.getSelectionModel().clearSelection();
								orderData.removeAll(orderData);
								for (int index = 0; index < rowCount; index++) {
									System.out.println(index);
									oVo = list.get(index);
									if (oVo.getO_date().contains(orderListYear + "-" + orderListMonth)) {
										orderData.add(oVo);
										searchResult = true;
									}
								}
							}

							// 년 + 월로 조회한 결과값의 총 수량과 총액 송출
							totalOrderCountData = oDao.getOrderYearMonthTotalCount(orderListYear, orderListMonth)
									.getO_orderTotalCount();
							totalOrderPriceData = oDao.getOrderYearMonthTotalCount(orderListYear, orderListMonth)
									.getO_orderTotalPrice();

							txtOrderListTotalCount.setText(totalOrderCountData + "");
							txtOrderListTotalPrice.setText(totalOrderPriceData + "");

						} else {

							cbOrderListYear.getSelectionModel().clearSelection();
							cbOrderListMonth.getSelectionModel().clearSelection();

							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("주문 이력 조회");
							alert.setHeaderText(orderListYear + "년 " + orderListMonth + "월의 주문이 리스트에 없습니다.");
							alert.setContentText("다시 검색하세요");
							alert.showAndWait();

						}

					}

				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("주문 이력 년도 조회");
					alert.setHeaderText("주문 이력 검색에 오류가 발생하였습니다.");
					alert.setContentText("다시하세요");
					alert.showAndWait();
					System.out.println(e1);

				}

			});

			// 주문목록 전체보기 버튼 기능설정
			btnOrderListTotal.setOnAction(e -> {

				orderData.removeAll(orderData);

				// 기본이미지로 복구
				localUrl = "/image/default.gif";
				localImage = new Image(localUrl, false);
				OrderImageView.setImage(localImage);

				orderListTotalList();

				btnOrderEdit.setDisable(true);
				btnOrderDelete.setDisable(true);
				btnWarehousing.setDisable(true);
				btnWarehousingEdit.setDisable(true);

				try {

					totalOrderCountData = oDao1.getStockTotalCount(oVo1).getO_orderTotalCount();
					totalOrderPriceData = oDao1.getStockTotalCount(oVo1).getO_orderTotalPrice();

					txtOrderListTotalCount.setText(totalOrderCountData + "");
					txtOrderListTotalPrice.setText(totalOrderPriceData + "");

				} catch (Exception e1) {
					System.out.println("전체보기 총수량,총액 애러" + e1);
				}

			});

			// 입고목록 날짜로 조회 버튼 기능설정
			btnWarehousingListSearch.setOnAction(e -> {
				WarehousingVO wVo = new WarehousingVO();
				WarehousingDAO wDao = null;

				Object[][] totalData = null;

				String wareListYear = null;
				String wareListMonth = null;
				boolean searchResult = false;

				try {
					// 콤보박스에서 선택한 년도를 불러옴
					wareListYear = cbWarehousingListYear.getSelectionModel().getSelectedItem();
					wareListMonth = cbWarehousingListMonth.getSelectionModel().getSelectedItem();

					wDao = new WarehousingDAO();

					// 아무것도 선택 안햇을때 경고창 알람
					if (wareListYear == null && wareListMonth == null) {
						searchResult = true;

						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("입고목록 년도별,월별 조회");
						alert.setHeaderText("입고목록중 검색할 년도와 월을 선택하세요");
						alert.setContentText("다음엔 주의하세요");
						alert.showAndWait();

					}

					// 년도만 선택해서 검색했을때 수행과정
					if (wareListYear != null && wareListMonth == null) {
						ArrayList<String> title;
						ArrayList<WarehousingVO> list;

						wVo = wDao.getWareListSearchYear(wareListYear);

						title = wDao.getWareColumnName();
						int columnCount = title.size();

						list = wDao.getWareTotal();
						int rowCount = list.size();

						totalData = new Object[rowCount][columnCount];

						if (wVo != null) {
							if (wVo.getW_date().contains(wareListYear)) {
								cbWarehousingListYear.getSelectionModel().clearSelection();
								wareData.removeAll(wareData);
								for (int index = 0; index < rowCount; index++) {
									System.out.println(index);
									wVo = list.get(index);
									if (wVo.getW_date().contains(wareListYear)) {
										wareData.add(wVo);
										searchResult = true;
									}
								}
							}

							// 년도로 조회한 결과값의 총 수량과 총액 송출
							totalWareCountData = wDao.getWareYearTotalCount(wareListYear).getW_orderTotalCount();
							totalWarePriceData = wDao.getWareYearTotalCount(wareListYear).getW_orderTotalPrice();

							txtWarehousingListTotalCount.setText(totalWareCountData + "");
							txtWarehousingListTotalPrice.setText(totalWarePriceData + "");

						} else {
							cbWarehousingListYear.getSelectionModel().clearSelection();

							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("입고 이력 조회");
							alert.setHeaderText(wareListYear + "년도의 입고내역이 리스트에 없습니다.");
							alert.setContentText("다시 검색하세요");
							alert.showAndWait();

						}

					}

					// 월만 선택해서 검색햇을때 진행과정
					if (wareListMonth != null && wareListYear == null) {
						ArrayList<String> title;
						ArrayList<WarehousingVO> list;

						wVo = wDao.getWareListSearchMonth(wareListMonth);

						title = wDao.getWareColumnName();
						int columnCount = title.size();

						list = wDao.getWareTotal();
						int rowCount = list.size();

						totalData = new Object[rowCount][columnCount];

						if (wVo != null) {

							if (wVo.getW_date().contains(wareListMonth)) {

								cbWarehousingListMonth.getSelectionModel().clearSelection();
								wareData.removeAll(wareData);
								for (int index = 0; index < rowCount; index++) {
									System.out.println(index);
									wVo = list.get(index);
									if (wVo.getW_date().contains(wareListMonth)) {
										wareData.add(wVo);
										searchResult = true;
									}
								}
							}

							// 월로 조회한 결과값의 총 수량과 총액 송출
							totalWareCountData = wDao.getWareMonthTotalCount(wareListMonth).getW_orderTotalCount();
							totalWarePriceData = wDao.getWareMonthTotalCount(wareListMonth).getW_orderTotalPrice();

							txtWarehousingListTotalCount.setText(totalWareCountData + "");
							txtWarehousingListTotalPrice.setText(totalWarePriceData + "");

						} else {

							cbWarehousingListMonth.getSelectionModel().clearSelection();

							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("입고 이력 조회");
							alert.setHeaderText(wareListMonth + "월의 입고가 리스트에 없습니다.");
							alert.setContentText("다시 검색하세요");
							alert.showAndWait();

						}

					}

					// 년도와 월을 둘다 선택해서 검색햇을때 진행과정
					if (wareListMonth != null && wareListYear != null) {
						ArrayList<String> title;
						ArrayList<WarehousingVO> list;

						wVo = wDao.getWareListSearchYearMonth(wareListYear, wareListMonth);

						title = wDao.getWareColumnName();
						int columnCount = title.size();

						list = wDao.getWareTotal();
						int rowCount = list.size();

						totalData = new Object[rowCount][columnCount];

						if (wVo != null) {

							if (wVo.getW_date().contains(wareListYear + "-" + wareListMonth)) {

								cbWarehousingListYear.getSelectionModel().clearSelection();
								cbWarehousingListMonth.getSelectionModel().clearSelection();
								wareData.removeAll(wareData);
								for (int index = 0; index < rowCount; index++) {
									System.out.println(index);
									wVo = list.get(index);
									if (wVo.getW_date().contains(wareListYear + "-" + wareListMonth)) {
										wareData.add(wVo);
										searchResult = true;
									}
								}
							}

							// 년 + 월로 조회한 결과값의 총 수량과 총액 송출
							totalWareCountData = wDao.getWareYearMonthTotalCount(wareListYear, wareListMonth)
									.getW_orderTotalCount();
							totalWarePriceData = wDao.getWareYearMonthTotalCount(wareListYear, wareListMonth)
									.getW_orderTotalPrice();

							txtWarehousingListTotalCount.setText(totalWareCountData + "");
							txtWarehousingListTotalPrice.setText(totalWarePriceData + "");

						} else {

							cbWarehousingListYear.getSelectionModel().clearSelection();
							cbWarehousingListMonth.getSelectionModel().clearSelection();

							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("입고 이력 조회");
							alert.setHeaderText(wareListYear + "년 " + wareListMonth + "월의 입고가 리스트에 없습니다.");
							alert.setContentText("다시 검색하세요");
							alert.showAndWait();

						}

					}

				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("입고 이력 년도 조회");
					alert.setHeaderText("입고 이력 검색에 오류가 발생하였습니다.");
					alert.setContentText("다시하세요");
					alert.showAndWait();
					System.out.println(e1);

				}

			});

			// 입고목록 전체보기 버튼 기능설정
			btnWarehousingListTotal.setOnAction(e -> {

				wareData.removeAll(wareData);

				// 기본이미지로 복구
				localUrl = "/image/default.gif";
				localImage = new Image(localUrl, false);
				OrderImageView.setImage(localImage);

				warehousingTotalList();

				btnOrderEdit.setDisable(true);
				btnOrderDelete.setDisable(true);
				btnWarehousing.setDisable(true);
				btnWarehousingEdit.setDisable(true);

				try {

					totalWareCountData = wDao1.getWareTotalCount(wVo1).getW_orderTotalCount();
					totalWarePriceData = wDao1.getWareTotalCount(wVo1).getW_orderTotalPrice();

					txtWarehousingListTotalCount.setText(totalWareCountData + "");
					txtWarehousingListTotalPrice.setText(totalWarePriceData + "");

				} catch (Exception e1) {
					System.out.println("입고목록 전체보기 총수량,총액 애러" + e1);
				}

			});

			// 주문목록 상품 선택시 이미지가 화면에 송출, 수정 삭제 버튼 활성화 설정
			OrderListTableView.setOnMouseClicked(e -> {

				if (e.getClickCount() != 2) {
					try {
						selectOrderList = OrderListTableView.getSelectionModel().getSelectedItems();

						no = selectOrderList.get(0).getNo();

						selectFileName = selectOrderList.get(0).getO_fileName();
						localUrl = "file:/C:/imagesStock/" + selectFileName;
						localImage = new Image(localUrl, false);

						OrderImageView.setFitWidth(300);
						OrderImageView.setFitHeight(300);
						OrderImageView.setImage(localImage);

						// 주문 목록 수정 삭제 , 입고등록 버튼 활성화
						btnOrderEdit.setDisable(false);
						btnOrderDelete.setDisable(false);
						btnWarehousing.setDisable(false);

						// 주문 수량 설정, 입고 삭제 버튼 비활성화
						btnOrderCount.setDisable(true);
						btnWarehousingEdit.setDisable(true);

					} catch (Exception e1) {

					}
				}

			});

			// 주문테이블 상품검색 테이블뷰 상품 선택시 이미지가 화면에 송출, 수정 삭제 버튼 활성화 설정
			OrderSearchTableView.setOnMouseClicked(e -> {

				if (e.getClickCount() != 2) {
					try {
						selectOrder = OrderSearchTableView.getSelectionModel().getSelectedItems();

						no = selectOrder.get(0).getNo();

						selectFileName = selectOrder.get(0).getSt_filename();
						localUrl = "file:/C:/imagesStock/" + selectFileName;
						localImage = new Image(localUrl, false);

						OrderImageView.setFitWidth(300);
						OrderImageView.setFitHeight(300);
						OrderImageView.setImage(localImage);

						// 수량설정 버튼 활성화
						btnOrderCount.setDisable(false);

						// 주문 목록 수정 삭제 , 입고등록 버튼 비활성화
						btnOrderEdit.setDisable(true);
						btnOrderDelete.setDisable(true);
						btnWarehousing.setDisable(true);
						btnWarehousingEdit.setDisable(true);

					} catch (Exception e1) {

					}
				}

			});

			// 입고목록 테이블뷰 상품 선택시 이미지가 화면에 송출, 수정 삭제 버튼 비활성화 설정
			WarehousingListTableView.setOnMouseClicked(e -> {

				if (e.getClickCount() != 2) {
					try {
						selectWareList = WarehousingListTableView.getSelectionModel().getSelectedItems();

						no = selectWareList.get(0).getNo();

						selectFileName = selectWareList.get(0).getW_fileName();
						localUrl = "file:/C:/imagesStock/" + selectFileName;
						localImage = new Image(localUrl, false);

						OrderImageView.setFitWidth(300);
						OrderImageView.setFitHeight(300);
						OrderImageView.setImage(localImage);

						// 수량설정 버튼 활성화
						btnOrderCount.setDisable(true);

						// 주문 목록 수정 삭제 , 입고등록 버튼 비활성화
						btnOrderEdit.setDisable(true);
						btnOrderDelete.setDisable(true);
						btnWarehousing.setDisable(true);

						// 입고목록 삭제 버튼 활성화
						btnWarehousingEdit.setDisable(false);

					} catch (Exception e1) {

					}
				}

			});

			// 주문 수량 설정 버튼 기능설정
			btnOrderCount.setOnAction(e -> {
				try {

					/*******************************
					 * 주문 수량 설정 팝업 띄우기
					 ************************/
					FXMLLoader orderCountLoader = new FXMLLoader();
					orderCountLoader.setLocation(getClass().getResource("/View/orderCount.fxml"));

					Stage orderCountdialog = new Stage(StageStyle.DECORATED);
					orderCountdialog.initModality(Modality.WINDOW_MODAL);
					orderCountdialog.initOwner(btnOrder.getScene().getWindow());
					orderCountdialog.setTitle("주문 수량 설정");

					Parent parentOrderCount = (Parent) orderCountLoader.load();
					Scene countScene = new Scene(parentOrderCount);
					orderCountdialog.setScene(countScene);
					orderCountdialog.setResizable(false);
					orderCountdialog.show();

					/*******************************
					 * 주문 수량설정 팝업 텍스트필드 버튼 정의
					 *****************************/

					TextField txtOrderCount = (TextField) parentOrderCount.lookup("#txtOrderCount");
					Button btnOrderCountOK = (Button) parentOrderCount.lookup("#btnOrderCountOK");
					Button btnOrderCountExit = (Button) parentOrderCount.lookup("#btnOrderCountExit");

					/****************************
					 * 버튼 기능설정
					 **************************/
					// 등록 버튼 기능설정
					btnOrderCountOK.setOnAction(ev -> {
						try {
							orderData.removeAll(orderData);
							StockVO sVo = null;
							OrderDAO oDao = new OrderDAO();
							/*
							 * File dirMake = new File(dirSave.getAbsolutePath());
							 * 
							 * // 이미지 저장 if (!dirMake.exists()) { dirMake.mkdir(); }
							 * 
							 * // 이미지 파일 저장 String fileName = imageSave(selectedFile);
							 */

							// 주문 정보 저장
							if (ev.getSource().equals(btnOrderCountOK)) {

								if (Integer.parseInt(txtOrderCount.getText()) <= 0) {

									orderData.removeAll(orderData);
									orderListTotalList();

									Alert alert = new Alert(AlertType.WARNING);
									alert.setTitle("주문 수량 입력 오류");
									alert.setHeaderText("주문 할 수량을 정확히 입력하시오");
									alert.setContentText("주문수량이 음수 혹은 0은 불가능합니다.");
									alert.showAndWait();

								} else {

									selectOrder = OrderSearchTableView.getSelectionModel().getSelectedItems();

									sVo = new StockVO(selectOrder.get(0).getSt_pnum(), selectOrder.get(0).getSt_pname(),
											selectOrder.get(0).getSt_pkind(), selectOrder.get(0).getSt_size(),
											selectOrder.get(0).getSt_color(), selectOrder.get(0).getSt_price(),
											selectOrder.get(0).getSt_filename());
									oDao = new OrderDAO();

									int count = Integer.parseInt(txtOrderCount.getText());

									oDao.inputNewOrder(sVo, count);

									orderListTotalList();

									Alert alert = new Alert(AlertType.INFORMATION);
									alert.setTitle("주문 목록 등록");
									alert.setHeaderText(selectOrder.get(0).getSt_pnum() + " 번의 상품이 성공적으로 주문완료 되었습니다.");
									alert.setContentText("주문 목록 입력 성공");

									alert.showAndWait();

									data.removeAll(data);
									// txtOrderSearch.clear();
									btnOrderCount.setDisable(true);
									cbOrderSearch.getSelectionModel().clearSelection();

									localUrl = "/image/default.gif"; // 디폴트 이미지 설정
									localImage = new Image(localUrl, false);
									OrderImageView.setFitWidth(300);
									OrderImageView.setFitHeight(300);
									OrderImageView.setImage(localImage);

									try {

										totalOrderCountData = oDao1.getStockTotalCount(oVo1).getO_orderTotalCount();
										totalOrderPriceData = oDao1.getStockTotalCount(oVo1).getO_orderTotalPrice();

										txtOrderListTotalCount.setText(totalOrderCountData + "");
										txtOrderListTotalPrice.setText(totalOrderPriceData + "");

									} catch (Exception e1) {
										System.out.println("주문 등록 후 총수량,총액 애러" + e1);
									}

									orderCountdialog.close();

								}
							}

						} catch (NumberFormatException ne) {

							orderData.removeAll(orderData);
							orderListTotalList();

							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("주문 수량 입력 오류");
							alert.setHeaderText("주문 수량을 숫자로 정확히 입력하시오");
							alert.setContentText("주문 수량이 공백 혹은 문자로 입력되었습니다.");
							alert.showAndWait();

						} catch (Exception e3) {

							orderData.removeAll(orderData);
							orderListTotalList();

							e3.printStackTrace();

						}

					});

					// 취소 버튼 기능설정
					btnOrderCountExit.setOnAction(ev -> {
						orderCountdialog.close();
					});

				} catch (Exception e1) {
					System.out.println("주문수량 설정 팝업창 관련오류" + e1);
				}

			});

			// 주문 수량 수정 버튼 기능설정
			btnOrderEdit.setOnAction(e -> {
				try {
					WarehousingDAO wDao = new WarehousingDAO();

					selectOrderList = OrderListTableView.getSelectionModel().getSelectedItems();

					String wnum = selectOrderList.get(0).getO_num();

					if (wDao.searchWareWnum(wnum) == 0) {

						/*******************************
						 * 주문 수량 수정 설정 팝업 띄우기
						 ************************/
						FXMLLoader orderCountEditLoader = new FXMLLoader();
						orderCountEditLoader.setLocation(getClass().getResource("/View/orderCountEdit.fxml"));

						Stage orderCountEditdialog = new Stage(StageStyle.DECORATED);
						orderCountEditdialog.initModality(Modality.WINDOW_MODAL);
						orderCountEditdialog.initOwner(btnOrderEdit.getScene().getWindow());
						orderCountEditdialog.setTitle("주문 수량 수정");

						Parent parentOrderCountEdit = (Parent) orderCountEditLoader.load();
						Scene countEditScene = new Scene(parentOrderCountEdit);
						orderCountEditdialog.setScene(countEditScene);
						orderCountEditdialog.setResizable(false);
						orderCountEditdialog.show();

						/*******************************
						 * 주문 수량설정 팝업 텍스트필드 버튼 정의
						 *****************************/

						TextField txtOrderCountEdit = (TextField) parentOrderCountEdit.lookup("#txtOrderCountEdit");
						Button btnOrderCountEditOK = (Button) parentOrderCountEdit.lookup("#btnOrderCountEditOK");
						Button btnOrderCountEditExit = (Button) parentOrderCountEdit.lookup("#btnOrderCountEditExit");

						/****************************
						 * 버튼 기능설정
						 **************************/
						// 수정 처리를 위해 선택된 상품정보와 인덱스 정의

						OrderVO OrderCountEdit = OrderListTableView.getSelectionModel().getSelectedItem();
						selectedIndex = OrderListTableView.getSelectionModel().getSelectedIndex();

						txtOrderCountEdit.setText(OrderCountEdit.getO_count() + "");

						// 주문수량 수정 버튼 기능설정
						btnOrderCountEditOK.setOnAction(ev -> {

							OrderVO oVo = null;
							OrderDAO oDao = null;

							try {

								if (txtOrderCountEdit.getText().equals("")) {

									Alert alert = new Alert(AlertType.WARNING);
									alert.setTitle("주문 수량 수정");
									alert.setHeaderText("수정할 주문수량을 입력하세요.");
									alert.setContentText("주문수량이 누락되었습니다.");
									alert.showAndWait();

								} else if (Integer.parseInt(txtOrderCountEdit.getText()) <= 0) {

									// orderListTotalList();

									Alert alert = new Alert(AlertType.WARNING);
									alert.setTitle("주문 수량 수정");
									alert.setHeaderText("수량이 - 혹은 0으로 입력되었습니다.");
									alert.setContentText("수정 할 수량을 정확히 입력하세요.");
									alert.showAndWait();

								} else {

									orderData.remove(selectedIndex);

									try {
										oVo = new OrderVO(OrderCountEdit.getNo(), OrderCountEdit.getO_price(),
												Integer.parseInt(txtOrderCountEdit.getText()));

										oDao = new OrderDAO();
										oDao.getOrderUpdate(oVo, oVo.getNo());
										orderData.removeAll(orderData);
										orderListTotalList();

										orderCountEditdialog.close();

										// 기본이미지 표출
										localUrl = "/image/default.gif";
										localImage = new Image(localUrl, false);
										OrderImageView.setFitWidth(300);
										OrderImageView.setFitHeight(300);
										OrderImageView.setImage(localImage);

										try {

											totalOrderCountData = oDao1.getStockTotalCount(oVo1).getO_orderTotalCount();
											totalOrderPriceData = oDao1.getStockTotalCount(oVo1).getO_orderTotalPrice();

											txtOrderListTotalCount.setText(totalOrderCountData + "");
											txtOrderListTotalPrice.setText(totalOrderPriceData + "");

										} catch (Exception e1) {
											System.out.println(" 총수량,총액 애러" + e1);
										}

									} catch (Exception e2) {

										System.out.println("주문 수량 수정창 수정버튼 관련 오류 " + e2);

									}

								}

							} catch (Exception e3) {

								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("주문 수량 수정");
								alert.setHeaderText("수정 할 수량을 숫자로 정확히 입력하세요.");
								alert.setContentText("다음에는 주의하세요.");
								alert.showAndWait();

							}

						});

						// 취소 버튼 기능설정
						btnOrderCountEditExit.setOnAction(ev -> {
							orderCountEditdialog.close();
						});

					} else {

						Alert alert1 = new Alert(AlertType.WARNING);
						alert1.setTitle("주문 수량 수정 오류");
						alert1.setHeaderText("해당 물품은 입고완료처리로 인해 수정이 불가능합니다.");
						alert1.setContentText("입고 목록을 확인후 다시 시도해주세요.");
						alert1.showAndWait();

					}

				} catch (Exception e1) {
					System.out.println("주문수량 설정 팝업창 관련오류" + e1);
				}

			});

			// 주문 목록 삭제 버튼 기능설정
			btnOrderDelete.setOnAction(e -> {

				OrderDAO oDao = null;
				oDao = new OrderDAO();
				WarehousingDAO wDao = new WarehousingDAO();

				try {
					selectOrderList = OrderListTableView.getSelectionModel().getSelectedItems();

					String wnum = selectOrderList.get(0).getO_num();

					if (wDao.searchWareWnum(wnum) == 0) {
						// 삭제 버튼 누를시 알람메세지 팝업설정
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("삭제메세지");
						alert.setHeaderText("삭제한 데이터는 복구하실수 없습니다.");
						alert.setContentText("삭제를 진행하시겠습니까?");

						Optional<ButtonType> result = alert.showAndWait();

						if (result.get() == ButtonType.OK) {

							oDao.getOrderListDelete(no);
							orderData.removeAll(orderData);

							// 주문 이력 전체정보
							orderListTotalList();

							/*
							 * // 이미지 파일 삭제 stockImageDelete(selectFileName);
							 */

							// 기본이미지 표출
							localUrl = "/image/default.gif";
							localImage = new Image(localUrl, false);
							OrderImageView.setFitWidth(300);
							OrderImageView.setFitHeight(300);
							OrderImageView.setImage(localImage);

							// 총 수량, 총액 적용
							totalOrderCountData = oDao1.getStockTotalCount(oVo1).getO_orderTotalCount();
							totalOrderPriceData = oDao1.getStockTotalCount(oVo1).getO_orderTotalPrice();

							txtOrderListTotalCount.setText(totalOrderCountData + "");
							txtOrderListTotalPrice.setText(totalOrderPriceData + "");

						} else {

						}
					} else {

						Alert alert1 = new Alert(AlertType.WARNING);
						alert1.setTitle("주문 목록 삭제 오류");
						alert1.setHeaderText("해당 물품은 입고완료처리로 인해 삭제가 불가능합니다.");
						alert1.setContentText("입고목록을 확인후 다시 시도해주세요.");
						alert1.showAndWait();

					}

				} catch (Exception e1) {
					System.out.println("주문 이력 삭제버튼 관련 오류" + e1);
				}

			});

			// 입고 등록 버튼 기능설정
			btnWarehousing.setOnAction(e -> {

				try {
					wareData.removeAll(wareData);
					OrderVO oVo = null;
					WarehousingDAO wDao = new WarehousingDAO();
					/*
					 * File dirMake = new File(dirSave.getAbsolutePath());
					 * 
					 * // 이미지 저장 if (!dirMake.exists()) { dirMake.mkdir(); }
					 * 
					 * // 이미지 파일 저장 String fileName = imageSave(selectedFile);
					 */

					// 입고등록 정보 저장
					if (e.getSource().equals(btnWarehousing)) {

						selectOrderList = OrderListTableView.getSelectionModel().getSelectedItems();

						String wnum = selectOrderList.get(0).getO_num();
						String selectGoods = selectOrderList.get(0).getO_pnum();
						int swnum = selectOrderList.get(0).getNo();

						// 선택한 정보의 주문번호가 이미 입고되었는지확인후 입고되지 않았으면 진행하도록 설정
						if (wDao.searchWareWnum(wnum) == 0) {

							oVo = new OrderVO(selectOrderList.get(0).getNo(), selectOrderList.get(0).getO_num(),
									selectOrderList.get(0).getO_pnum(), selectOrderList.get(0).getO_pname(),
									selectOrderList.get(0).getO_pkind(), selectOrderList.get(0).getO_size(),
									selectOrderList.get(0).getO_color(), selectOrderList.get(0).getO_price(),
									selectOrderList.get(0).getO_count(), selectOrderList.get(0).getO_totalPrice(),
									selectOrderList.get(0).getO_fileName());
							wDao = new WarehousingDAO();

							wDao.inputWarehousing(oVo);

							wDao.inputOrderToStock(selectGoods, swnum);

							if (wDao != null) {

								warehousingTotalList();

								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("입고 등록");
								alert.setHeaderText(selectOrderList.get(0).getO_pnum() + " 번의 상품이 성공적으로 입고완료 되었습니다.");
								alert.setContentText("입고 목록 입력 성공");

								alert.showAndWait();

								// 기본이미지로 복구
								localUrl = "/image/default.gif";
								localImage = new Image(localUrl, false);
								OrderImageView.setImage(localImage);
								// selectedFile = null;

								try {

									// 입고 총 수량, 총합계
									totalWareCountData = wDao1.getWareTotalCount(wVo1).getW_orderTotalCount();
									totalWarePriceData = wDao1.getWareTotalCount(wVo1).getW_orderTotalPrice();

									txtWarehousingListTotalCount.setText(totalWareCountData + "");
									txtWarehousingListTotalPrice.setText(totalWarePriceData + "");

								} catch (Exception e1) {
									System.out.println("입고 등록 후 총수량,총액 애러" + e1);
								}

							}

						} else {

							warehousingTotalList();

							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("입고 등록 오류");
							alert.setHeaderText("해당 물품은 이미 입고완료처리 되었습니다.");
							alert.setContentText("입고 목록을 확인후 재시도 해주세요.");
							alert.showAndWait();

						}
					}
				} catch (Exception e3) {

					warehousingTotalList();

					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("입고 등록 오류");
					alert.setHeaderText("등록할 입고정보를 정확히 확인하시오");
					alert.setContentText("다음에는 주의하세요");
					alert.showAndWait();

					System.out.println(e3);

				}

			});

			// 입고 목록 삭제 버튼 기능설정
			btnWarehousingEdit.setOnAction(e -> {

				WarehousingDAO wDao = null;
				wDao = new WarehousingDAO();
				selectWareList = WarehousingListTableView.getSelectionModel().getSelectedItems();
				String deleteWare = selectWareList.get(0).getW_pnum();
				int deleteNum = selectWareList.get(0).getNo();

				try {
					// 삭제 버튼 누를시 알람메세지 팝업설정
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("삭제메세지");
					alert.setHeaderText("삭제한 데이터는 복구하실수 없습니다.");
					alert.setContentText("삭제를 진행하시겠습니까?");

					Optional<ButtonType> result = alert.showAndWait();

					if (result.get() == ButtonType.OK) {

						wDao.deleteWareToStock(deleteWare, deleteNum);
						wDao.getWareListDelete(no);
						wareData.removeAll(wareData);

						// 입고 이력 전체정보
						warehousingTotalList();

						/*
						 * // 이미지 파일 삭제 stockImageDelete(selectFileName);
						 */

						// 기본이미지 표출
						localUrl = "/image/default.gif";
						localImage = new Image(localUrl, false);
						OrderImageView.setFitWidth(300);
						OrderImageView.setFitHeight(300);
						OrderImageView.setImage(localImage);

						// 입고 총 수량, 총합계
						totalWareCountData = wDao1.getWareTotalCount(wVo1).getW_orderTotalCount();
						totalWarePriceData = wDao1.getWareTotalCount(wVo1).getW_orderTotalPrice();

						txtWarehousingListTotalCount.setText(totalWareCountData + "");
						txtWarehousingListTotalPrice.setText(totalWarePriceData + "");

					} else {

					}
				} catch (Exception e1) {
					System.out.println("입고 이력 삭제버튼 관련 오류" + e1);
				}

			});

			// 종료 버튼 기능설정
			btnOrderExit.setOnAction(e -> {

				orderdialog.close();
			});

		} catch (Exception e) {

		}
	}

	// 판매창 다이얼로그 생성
	public void handleBtnSalesAction(ActionEvent event) {

		data.removeAll(data);
		orderData.removeAll(orderData);
		wareData.removeAll(wareData);
		salesData.removeAll(salesData);
		preSalesData.removeAll(preSalesData);

		StockDAO sDao2 = new StockDAO();

		try {

			/****************************
			 * 판매 화면 띄우기
			 ****************************/
			FXMLLoader salesLoader = new FXMLLoader();
			salesLoader.setLocation(getClass().getResource("/View/sales.fxml"));

			Stage salesdialog = new Stage(StageStyle.DECORATED);
			salesdialog.initModality(Modality.WINDOW_MODAL);
			salesdialog.initOwner(btnSales.getScene().getWindow());
			salesdialog.setTitle("판매");

			Parent parentSales = (Parent) salesLoader.load();
			Scene scene = new Scene(parentSales);
			salesdialog.setScene(scene);
			salesdialog.setResizable(false);
			salesdialog.show();

			/*****************************
			 * 판매창 버튼및 텍스트필드 정의
			 *****************************/
			TextField txtSalesCusName = (TextField) parentSales.lookup("#txtSalesCusName");
			TextField txtSalesCusNum = (TextField) parentSales.lookup("#txtSalesCusNum");
			// TextField txtSalesSearch = (TextField) parentSales.lookup("#txtSalesSearch");
			TextField txtPreSalesToTalCount = (TextField) parentSales.lookup("#txtPreSalesToTalCount");
			TextField txtPreSalesToTalPrice = (TextField) parentSales.lookup("#txtPreSalesToTalPrice");

			Button btnSalesListView = (Button) parentSales.lookup("#btnSalesListView");
			Button btnSalesSearch = (Button) parentSales.lookup("#btnSalesSearch");
			Button btnSalesExit = (Button) parentSales.lookup("#btnSalesExit");
			Button btnSalesSearchClear = (Button) parentSales.lookup("#btnSalesSearchClear");
			Button btnSalesCount = (Button) parentSales.lookup("#btnSalesCount");
			Button btnPreSalesEdit = (Button) parentSales.lookup("#btnPreSalesEdit");
			Button btnPreSalesDelete = (Button) parentSales.lookup("#btnPreSalesDelete");
			Button btnPreSalesOk = (Button) parentSales.lookup("#btnPreSalesOk");

			TableView<StockVO> SalesSearchTableView = (TableView) parentSales.lookup("#SalesSearchTableView");
			TableView<SalesVO> PreSalesTableView = (TableView) parentSales.lookup("#PreSalesTableView");

			ImageView SalesImageView = (ImageView) parentSales.lookup("#SalesImageView");

			ComboBox<String> cbSalesSearch = (ComboBox) parentSales.lookup("#cbSalesSearch");
			cbSalesSearch.setItems(FXCollections.observableArrayList(sDao2.choiceStockPnum()));

			/*******************************
			 * 테이블뷰 칼럼설정
			 *******************************/

			// 판매 상품 검색창 테이블뷰 컬럼설정

			TableColumn colsst_pnum = new TableColumn("품번");
			colsst_pnum.setMinWidth(110);
			colsst_pnum.setStyle("-fx-alignment:CENTER");
			colsst_pnum.setCellValueFactory(new PropertyValueFactory<>("st_pnum"));

			TableColumn colsst_pname = new TableColumn("품명");
			colsst_pname.setMinWidth(130);
			colsst_pname.setStyle("-fx-alignment:CENTER");
			colsst_pname.setCellValueFactory(new PropertyValueFactory<>("st_pname"));

			TableColumn colsst_pkind = new TableColumn("품종");
			colsst_pkind.setMinWidth(80);
			colsst_pkind.setStyle("-fx-alignment:CENTER");
			colsst_pkind.setCellValueFactory(new PropertyValueFactory<>("st_pkind"));

			TableColumn colsst_size = new TableColumn("사이즈");
			colsst_size.setMinWidth(70);
			colsst_size.setStyle("-fx-alignment:CENTER");
			colsst_size.setCellValueFactory(new PropertyValueFactory<>("st_size"));

			TableColumn colsst_color = new TableColumn("컬러");
			colsst_color.setMinWidth(50);
			colsst_color.setStyle("-fx-alignment:CENTER");
			colsst_color.setCellValueFactory(new PropertyValueFactory<>("st_color"));

			TableColumn colsst_price = new TableColumn("단가");
			colsst_price.setMinWidth(70);
			colsst_price.setStyle("-fx-alignment:CENTER");
			colsst_price.setCellValueFactory(new PropertyValueFactory<>("st_price"));

			TableColumn colsst_count = new TableColumn("재고수량");
			colsst_count.setMinWidth(40);
			colsst_count.setStyle("-fx-alignment:CENTER");
			colsst_count.setCellValueFactory(new PropertyValueFactory<>("st_count"));

			TableColumn colsst_filename = new TableColumn("파일명");
			colsst_filename.setMinWidth(200);
			colsst_filename.setCellValueFactory(new PropertyValueFactory<>("st_filename"));

			SalesSearchTableView.getColumns().addAll(colsst_pnum, colsst_pname, colsst_pkind, colsst_size, colsst_color,
					colsst_price, colsst_count, colsst_filename);

			SalesSearchTableView.setItems(data);

			// 판매대기 테이블뷰 컬럼설정

			TableColumn colprsa_pnum = new TableColumn("품번");
			colprsa_pnum.setMinWidth(110);
			colprsa_pnum.setStyle("-fx-alignment:CENTER");
			colprsa_pnum.setCellValueFactory(new PropertyValueFactory<>("sa_pnum"));

			TableColumn colprsa_pname = new TableColumn("품명");
			colprsa_pname.setMinWidth(130);
			colprsa_pname.setStyle("-fx-alignment:CENTER");
			colprsa_pname.setCellValueFactory(new PropertyValueFactory<>("sa_pname"));

			TableColumn colprsa_pkind = new TableColumn("품종");
			colprsa_pkind.setMinWidth(80);
			colprsa_pkind.setStyle("-fx-alignment:CENTER");
			colprsa_pkind.setCellValueFactory(new PropertyValueFactory<>("sa_pkind"));

			TableColumn colprsa_size = new TableColumn("사이즈");
			colprsa_size.setMinWidth(70);
			colprsa_size.setStyle("-fx-alignment:CENTER");
			colprsa_size.setCellValueFactory(new PropertyValueFactory<>("sa_size"));

			TableColumn colprsa_color = new TableColumn("컬러");
			colprsa_color.setMinWidth(50);
			colprsa_color.setStyle("-fx-alignment:CENTER");
			colprsa_color.setCellValueFactory(new PropertyValueFactory<>("sa_color"));

			TableColumn colprsa_price = new TableColumn("단가");
			colprsa_price.setMinWidth(70);
			colprsa_price.setStyle("-fx-alignment:CENTER");
			colprsa_price.setCellValueFactory(new PropertyValueFactory<>("sa_price"));

			TableColumn colprsa_count = new TableColumn("판매수량");
			colprsa_count.setMinWidth(40);
			colprsa_count.setStyle("-fx-alignment:CENTER");
			colprsa_count.setCellValueFactory(new PropertyValueFactory<>("sa_count"));

			TableColumn colprsa_totalPrice = new TableColumn("총액");
			colprsa_totalPrice.setMinWidth(40);
			colprsa_totalPrice.setStyle("-fx-alignment:CENTER");
			colprsa_totalPrice.setCellValueFactory(new PropertyValueFactory<>("sa_totalPrice"));

			TableColumn colprsa_CusName = new TableColumn("고객명");
			colprsa_CusName.setMinWidth(80);
			colprsa_CusName.setStyle("-fx-alignment:CENTER");
			colprsa_CusName.setCellValueFactory(new PropertyValueFactory<>("sa_CusName"));

			TableColumn colprsa_CusNum = new TableColumn("고객연락처");
			colprsa_CusNum.setMinWidth(120);
			colprsa_CusNum.setStyle("-fx-alignment:CENTER");
			colprsa_CusNum.setCellValueFactory(new PropertyValueFactory<>("sa_CusNum"));

			TableColumn colprsa_fileName = new TableColumn("파일명");
			colprsa_fileName.setMinWidth(200);
			colprsa_fileName.setCellValueFactory(new PropertyValueFactory<>("sa_fileName"));

			PreSalesTableView.getColumns().addAll(colprsa_pnum, colprsa_pname, colprsa_pkind, colprsa_size,
					colprsa_color, colprsa_price, colprsa_count, colprsa_totalPrice, colprsa_CusName, colprsa_CusNum,
					colprsa_fileName);

			PreSalesTableView.setItems(preSalesData);

			/*********************************
			 * 판매창 초기값 설정
			 ********************************/

			// 판매 수량설정, 판매대기 수정 삭제 판매 버튼 초기 비활성화
			btnSalesCount.setDisable(true);
			btnPreSalesEdit.setDisable(true);
			btnPreSalesDelete.setDisable(true);
			btnPreSalesOk.setDisable(true);

			// 기본이미지 표출
			localUrl = "/image/default.gif";
			localImage = new Image(localUrl, false);
			SalesImageView.setFitWidth(300);
			SalesImageView.setFitHeight(300);
			SalesImageView.setImage(localImage);

			/*******************************
			 * 각버튼 및 이미지뷰 테이블뷰 기능설정
			 ******************************/

			// 판매창 상품검색 테이블뷰 상품 선택시 이미지가 화면에 송출, 수정 삭제 버튼 활성화 설정
			SalesSearchTableView.setOnMouseClicked(e -> {

				if (e.getClickCount() != 2) {
					try {
						selectSales = SalesSearchTableView.getSelectionModel().getSelectedItems();

						no = selectSales.get(0).getNo();

						selectFileName = selectSales.get(0).getSt_filename();
						localUrl = "file:/C:/imagesStock/" + selectFileName;
						localImage = new Image(localUrl, false);

						SalesImageView.setFitWidth(300);
						SalesImageView.setFitHeight(300);
						SalesImageView.setImage(localImage);

						// 판매 수량설정 버튼 활성화
						btnSalesCount.setDisable(false);

						// 판매 대기 수정 삭제 판매 버튼 비활성화
						btnPreSalesEdit.setDisable(true);
						btnPreSalesDelete.setDisable(true);

					} catch (Exception e1) {

					}
				}

			});

			// 판매대기창 테이블뷰 상품 선택시 이미지가 화면에 송출, 수정 삭제 버튼 활성화 설정
			PreSalesTableView.setOnMouseClicked(e -> {

				if (e.getClickCount() != 2) {
					try {
						selectPreSales = PreSalesTableView.getSelectionModel().getSelectedItems();

						no = selectPreSales.get(0).getNo();

						selectFileName = selectPreSales.get(0).getSa_fileName();
						localUrl = "file:/C:/imagesStock/" + selectFileName;
						localImage = new Image(localUrl, false);

						SalesImageView.setFitWidth(300);
						SalesImageView.setFitHeight(300);
						SalesImageView.setImage(localImage);

						// 판매 수량설정 버튼 비활성화
						btnSalesCount.setDisable(true);

						// 판매 대기 수정 삭제 버튼 활성화
						btnPreSalesEdit.setDisable(false);
						btnPreSalesDelete.setDisable(false);

					} catch (Exception e1) {

					}
				}

			});

			// 품번으로 상품조회 버튼 기능설정(판매)
			btnSalesSearch.setOnAction(e -> {

				StockVO sVo = new StockVO();
				StockDAO sDao = null;

				Object[][] totalData = null;

				String salesSearch = null;
				boolean searchResult = false;

				try {
					salesSearch = cbSalesSearch.getSelectionModel().getSelectedItem() + "";
					sDao = new StockDAO();
					sVo = sDao.getStockPnumCheck(salesSearch);

					if (salesSearch.equals("")) {
						searchResult = true;

						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("상품 정보 품번 조회");
						alert.setHeaderText("상품의 품번을 입력하시오");
						alert.setContentText("다음엔 주의하세요");
						alert.showAndWait();

					}

					if (!salesSearch.equals("") && (sVo != null)) {
						ArrayList<String> title;
						ArrayList<StockVO> list;

						title = sDao.getColumnName();
						int columnCount = title.size();

						list = sDao.getStockTotal();
						int rowCount = list.size();

						totalData = new Object[rowCount][columnCount];

						if (sVo.getSt_pnum().equals(salesSearch)) {
							// txtSalesSearch.clear();
							cbSalesSearch.getSelectionModel().clearSelection();
							data.removeAll(data);
							for (int index = 0; index < rowCount; index++) {
								System.out.println(index);
								sVo = list.get(index);
								if (sVo.getSt_pnum().equals(salesSearch)) {
									data.add(sVo);
									searchResult = true;
								}
							}
						}

					}

					if (!searchResult) {
						// txtSalesSearch.clear();
						cbSalesSearch.getSelectionModel().clearSelection();
						data.removeAll(data);
						// 기본이미지 표출
						localUrl = "/image/default.gif";
						localImage = new Image(localUrl, false);
						SalesImageView.setFitWidth(300);
						SalesImageView.setFitHeight(300);
						SalesImageView.setImage(localImage);

						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("상품 정보 품번 조회");
						alert.setHeaderText(salesSearch + "번의 상품이 리스트에 없습니다.");
						alert.setContentText("다시 검색하세요");
						alert.showAndWait();

					}

				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("상품 정보 품번 조회");
					alert.setHeaderText("상품 정보 검색에 오류가 발생하였습니다.");
					alert.setContentText("다시하세요");
					alert.showAndWait();

				}

			});

			// 검색 초기화 버튼 기능설정
			btnSalesSearchClear.setOnAction(e -> {
				data.removeAll(data);
				// txtSalesSearch.clear();
				cbSalesSearch.getSelectionModel().clearSelection();
				btnSalesCount.setDisable(true);

				localUrl = "/image/default.gif"; // 디폴트 이미지 설정
				localImage = new Image(localUrl, false);
				SalesImageView.setFitWidth(300);
				SalesImageView.setFitHeight(300);
				SalesImageView.setImage(localImage);
			});

			// 판매 수량 설정 버튼 기능설정
			btnSalesCount.setOnAction(e -> {

				selectSales = SalesSearchTableView.getSelectionModel().getSelectedItems();
				try {

					/*******************************
					 * 판매 수량 설정 팝업 띄우기
					 ************************/
					FXMLLoader salesCountLoader = new FXMLLoader();
					salesCountLoader.setLocation(getClass().getResource("/View/salesCount.fxml"));

					Stage salesCountdialog = new Stage(StageStyle.DECORATED);
					salesCountdialog.initModality(Modality.WINDOW_MODAL);
					salesCountdialog.initOwner(btnSalesCount.getScene().getWindow());
					salesCountdialog.setTitle("판매 수량 설정");

					Parent parentSalesCount = (Parent) salesCountLoader.load();
					Scene salesScene = new Scene(parentSalesCount);
					salesCountdialog.setScene(salesScene);
					salesCountdialog.setResizable(false);
					salesCountdialog.show();

					/*******************************
					 * 판매 수량설정 팝업 텍스트필드 버튼 정의
					 *****************************/

					TextField txtCurrentCount = (TextField) parentSalesCount.lookup("#txtCurrentCount");
					TextField txtSalesCount = (TextField) parentSalesCount.lookup("#txtSalesCount");

					Button btnSalesCountOK = (Button) parentSalesCount.lookup("#btnSalesCountOK");
					Button btnSalesCountExit = (Button) parentSalesCount.lookup("#btnSalesCountExit");

					txtCurrentCount.setText(selectSales.get(0).getSt_count() + "");
					txtCurrentCount.setEditable(false);

					/****************************
					 * 버튼 기능설정
					 **************************/
					// 등록 버튼 기능설정
					btnSalesCountOK.setOnAction(ev -> {

						// 판매대기 총수량 초기화
						int totalX = 0;
						// 판매대기 총액 초기화
						int totalY = 0;

						SalesVO sVo = null;
						SalesDAO saDao = new SalesDAO();

						// 판매대기 목록으로 전송
						if (ev.getSource().equals(btnSalesCountOK)) {

							try {

								if (txtSalesCount.getText() != null && Integer.parseInt(txtSalesCount.getText()) > 0
										&& (Integer.parseInt(txtCurrentCount.getText()) >= Integer
												.parseInt(txtSalesCount.getText()))) {

									sVo = new SalesVO(selectSales.get(0).getSt_pnum(), selectSales.get(0).getSt_pname(),
											selectSales.get(0).getSt_pkind(), selectSales.get(0).getSt_size(),
											selectSales.get(0).getSt_color(), selectSales.get(0).getSt_price(),
											Integer.parseInt(txtSalesCount.getText()),
											(Integer.parseInt(txtSalesCount.getText())
													* selectSales.get(0).getSt_price()),
											selectSales.get(0).getSt_filename(), txtSalesCusName.getText(),
											txtSalesCusNum.getText());

									preSalesData.add(sVo);
									PreSalesTableView.setItems(preSalesData);

									saDao.deletePreSalesToStock(selectSales.get(0).getSt_pnum(),
											Integer.parseInt(txtSalesCount.getText()));

									int datasize = preSalesData.size();

									for (int i = 0; i < datasize; i++) {
										int x = PreSalesTableView.getItems().get(i).getSa_count();
										totalX = totalX + x;

										int y = PreSalesTableView.getItems().get(i).getSa_price()
												* PreSalesTableView.getItems().get(i).getSa_count();
										totalY = totalY + y;
									}

									txtPreSalesToTalCount.setText(totalX + "");
									txtPreSalesToTalPrice.setText(totalY + "");

									// 기본이미지로 복구
									localUrl = "/image/default.gif";
									localImage = new Image(localUrl, false);
									SalesImageView.setImage(localImage);
									// selectedFile = null;

									data.removeAll(data);
									btnSalesCount.setDisable(true);

									localUrl = "/image/default.gif"; // 디폴트 이미지 설정
									localImage = new Image(localUrl, false);
									SalesImageView.setFitWidth(300);
									SalesImageView.setFitHeight(300);
									SalesImageView.setImage(localImage);

									btnPreSalesOk.setDisable(false);

									salesCountdialog.close();
								} else {
									Alert alert = new Alert(AlertType.WARNING);
									alert.setTitle("판매물품 수량 선택");
									alert.setHeaderText("판매할 수량을 정확히 입력하시오");
									alert.setContentText("다음에는 주의하세요");
									alert.showAndWait();
								}

							} catch (Exception e1) {

								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("판매물품 수량 선택");
								alert.setHeaderText("판매할 수량을 숫자만 입력하시오");
								alert.setContentText("다음에는 주의하세요");
								alert.showAndWait();
							}

						}

					});

					// 취소 버튼 기능설정
					btnSalesCountExit.setOnAction(ev -> {
						salesCountdialog.close();
					});

				} catch (Exception e1) {
					System.out.println("판매 수량 설정 팝업창 관련오류" + e1);
				}

			});

			// 판매현황목록보기 버튼 기능설정
			btnSalesListView.setOnAction(e -> {

				salesData.removeAll(salesData);

				try {
					/****************************
					 * 판매 목록 현황 화면 띄우기
					 ****************************/
					FXMLLoader salesListLoader = new FXMLLoader();
					salesListLoader.setLocation(getClass().getResource("/View/salesList.fxml"));

					Stage salesListdialog = new Stage(StageStyle.DECORATED);
					salesListdialog.initModality(Modality.WINDOW_MODAL);
					salesListdialog.initOwner(btnSalesListView.getScene().getWindow());
					salesListdialog.setTitle("판매 목록");

					Parent parentSalesList = (Parent) salesListLoader.load();
					Scene listScene = new Scene(parentSalesList);
					salesListdialog.setScene(listScene);
					salesListdialog.setResizable(false);
					salesListdialog.show();

					/**********************************
					 * 판매 현황 탭 버튼 정의
					 *******************************/

					DatePicker dpSalesListDateStart = (DatePicker) parentSalesList.lookup("#dpSalesListDateStart");
					DatePicker dpSalesListDateEnd = (DatePicker) parentSalesList.lookup("#dpSalesListDateEnd");

					TextField txtSalesListNameSearch = (TextField) parentSalesList.lookup("#txtSalesListNameSearch");
					TextField txtSalesListCallSearch = (TextField) parentSalesList.lookup("#txtSalesListCallSearch");
					TextField txtSalesListTotalCount = (TextField) parentSalesList.lookup("#txtSalesListTotalCount");
					TextField txtSalesListTotalPrice = (TextField) parentSalesList.lookup("#txtSalesListTotalPrice");
					TextField txtSalesSaveFileDir = (TextField) parentSalesList.lookup("#txtSalesSaveFileDir");

					Button btnSalesSaveFileDir = (Button) parentSalesList.lookup("#btnSalesSaveFileDir");
					Button btnSalesExcel = (Button) parentSalesList.lookup("#btnSalesExcel");
					Button btnSalesPDF = (Button) parentSalesList.lookup("#btnSalesPDF");
					Button btnSalesListNameSearch = (Button) parentSalesList.lookup("#btnSalesListNameSearch");
					Button btnSalesListDateSearch = (Button) parentSalesList.lookup("#btnSalesListDateSearch");
					Button btnSalesTotalList = (Button) parentSalesList.lookup("#btnSalesTotalList");
					Button btnSalesListDelete = (Button) parentSalesList.lookup("#btnSalesListDelete");
					Button btnSalesListExit = (Button) parentSalesList.lookup("#btnSalesListExit");

					TableView<SalesVO> SalesListTableView = (TableView) parentSalesList.lookup("#SalesListTableView");

					ImageView SalesListImageView = (ImageView) parentSalesList.lookup("#SalesListImageView");

					/*****************************
					 * 테이블뷰 칼럼설정
					 *****************************/

					TableColumn colsalesno = new TableColumn("NO");
					colsalesno.setMinWidth(40);
					colsalesno.setStyle("-fx-alignment:CENTER");
					colsalesno.setCellValueFactory(new PropertyValueFactory<>("no"));

					TableColumn colsa_num = new TableColumn("판매번호");
					colsa_num.setMinWidth(100);
					colsa_num.setStyle("-fx-alignment:CENTER");
					colsa_num.setCellValueFactory(new PropertyValueFactory<>("sa_num"));

					TableColumn colsa_pnum = new TableColumn("판매품번");
					colsa_pnum.setMinWidth(120);
					colsa_pnum.setStyle("-fx-alignment:CENTER");
					colsa_pnum.setCellValueFactory(new PropertyValueFactory<>("sa_pnum"));

					TableColumn colsa_pname = new TableColumn("판매품명");
					colsa_pname.setMinWidth(140);
					colsa_pname.setStyle("-fx-alignment:CENTER");
					colsa_pname.setCellValueFactory(new PropertyValueFactory<>("sa_pname"));

					TableColumn colsa_pkind = new TableColumn("판매품종");
					colsa_pkind.setMinWidth(80);
					colsa_pkind.setStyle("-fx-alignment:CENTER");
					colsa_pkind.setCellValueFactory(new PropertyValueFactory<>("sa_pkind"));

					TableColumn colsa_size = new TableColumn("사이즈");
					colsa_size.setMinWidth(100);
					colsa_size.setStyle("-fx-alignment:CENTER");
					colsa_size.setCellValueFactory(new PropertyValueFactory<>("sa_size"));

					TableColumn colsa_color = new TableColumn("컬러");
					colsa_color.setMinWidth(60);
					colsa_color.setStyle("-fx-alignment:CENTER");
					colsa_color.setCellValueFactory(new PropertyValueFactory<>("sa_color"));

					TableColumn colsa_price = new TableColumn("단가");
					colsa_price.setMinWidth(80);
					colsa_price.setStyle("-fx-alignment:CENTER");
					colsa_price.setCellValueFactory(new PropertyValueFactory<>("sa_price"));

					TableColumn colsa_count = new TableColumn("판매수량");
					colsa_count.setMinWidth(60);
					colsa_count.setStyle("-fx-alignment:CENTER");
					colsa_count.setCellValueFactory(new PropertyValueFactory<>("sa_count"));

					TableColumn colsa_totalPrice = new TableColumn("총액");
					colsa_totalPrice.setMinWidth(100);
					colsa_totalPrice.setStyle("-fx-alignment:CENTER");
					colsa_totalPrice.setCellValueFactory(new PropertyValueFactory<>("sa_totalPrice"));

					TableColumn colsa_date = new TableColumn("판매날짜");
					colsa_date.setMinWidth(100);
					colsa_date.setStyle("-fx-alignment:CENTER");
					colsa_date.setCellValueFactory(new PropertyValueFactory<>("sa_date"));

					TableColumn colsa_fileName = new TableColumn("이미지");
					colsa_fileName.setMinWidth(200);
					colsa_fileName.setStyle("-fx-alignment:CENTER");
					colsa_fileName.setCellValueFactory(new PropertyValueFactory<>("sa_fileName"));

					TableColumn colsa_CusName = new TableColumn("고객명");
					colsa_CusName.setMinWidth(100);
					colsa_CusName.setStyle("-fx-alignment:CENTER");
					colsa_CusName.setCellValueFactory(new PropertyValueFactory<>("sa_CusName"));

					TableColumn colsa_CusNum = new TableColumn("고객연락처");
					colsa_CusNum.setMinWidth(140);
					colsa_CusNum.setStyle("-fx-alignment:CENTER");
					colsa_CusNum.setCellValueFactory(new PropertyValueFactory<>("sa_CusNum"));

					SalesListTableView.getColumns().addAll(colsalesno, colsa_date, colsa_num, colsa_pnum, colsa_pname,
							colsa_pkind, colsa_size, colsa_color, colsa_price, colsa_count, colsa_totalPrice,
							colsa_CusName, colsa_CusNum, colsa_fileName);

					SalesListTableView.setItems(salesData);

					/******************************
					 * 초기설정
					 ****************************/

					SalesVO saVo = new SalesVO();
					SalesDAO saDao = new SalesDAO();

					dpSalesListDateStart.setValue(LocalDate.parse(saDao.startDateSales()));
					// dpSalesListDateStart.setValue(LocalDate.now());
					dpSalesListDateEnd.setValue(LocalDate.now());

					SalesTotalList();

					btnSalesListDelete.setDisable(true);
					btnSalesExcel.setDisable(true);
					btnSalesPDF.setDisable(true);

					// 기본이미지 표출
					localUrl = "/image/default.gif";
					localImage = new Image(localUrl, false);
					SalesListImageView.setFitWidth(300);
					SalesListImageView.setFitHeight(300);
					SalesListImageView.setImage(localImage);

					// 총 판매량, 총판매액 띄우기
					totalSalesCountData = saDao.getSalesTotalCount(saVo).getSa_salesTotalCount();
					totalSalesPriceData = saDao.getSalesTotalCount(saVo).getSa_salesTotalPrice();

					txtSalesListTotalCount.setText(totalSalesCountData + "");
					txtSalesListTotalPrice.setText(totalSalesPriceData + "");

					/******************************
					 * 판매 현황탭 기능설정
					 *****************************/

					// 파일 저장 폴더 선택(판매 목록)
					btnSalesSaveFileDir.setOnAction(e1 -> {

						final DirectoryChooser directoryChooser = new DirectoryChooser();
						final File selectedDirectory = directoryChooser.showDialog(primaryStage);

						if (selectedDirectory != null) {
							txtSalesSaveFileDir.setText(selectedDirectory.getAbsolutePath());
							btnSalesExcel.setDisable(false);
							btnSalesPDF.setDisable(false);

						}

					});

					// 엑셀 파일 생성 버튼(판매 목록)
					btnSalesExcel.setOnAction(e1 -> {
						SalesDAO sDao8 = new SalesDAO();
						boolean saveSuccess;

						ArrayList<SalesVO> list;
						list = sDao8.getSalesTotal();
						SalesExcel excelWriter = new SalesExcel();

						// xlsx파일 쓰기
						saveSuccess = excelWriter.xlsxWiter(list, txtSalesSaveFileDir.getText());
						if (saveSuccess) {
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("엑셀 파일 생성");
							alert.setHeaderText("판매 목록 엑셀 파일 생성 성공");
							alert.setContentText("판매 목록 엑셀 파일");
							alert.showAndWait();

						}

						txtSalesSaveFileDir.clear();
						btnSalesExcel.setDisable(true);
						btnSalesPDF.setDisable(true);

					});

					// pdf파일 생성 (판매목록)
					btnSalesPDF.setOnAction(e1 -> {
						try {
							// pdf document 선언
							Document document = new Document(PageSize.A4, 0, 0, 30, 30);
							// pdf파일을 저장할 공간선언, pdf파일이 생성되고 그후 스트림으로 저장됨
							String strReportPDFName = "sales_" + System.currentTimeMillis() + ".pdf";
							PdfWriter.getInstance(document,
									new FileOutputStream(txtSalesSaveFileDir.getText() + "\\" + strReportPDFName));
							// document를 열어 pdf문서를 작성할수 있도록한다.
							document.open();
							// 한글 지원 폰트 설정
							BaseFont bf = BaseFont.createFont("font/MALGUN.TTF", BaseFont.IDENTITY_H,
									BaseFont.EMBEDDED);
							Font font = new Font(bf, 8, Font.NORMAL);
							Font font2 = new Font(bf, 14, Font.BOLD);

							// 타이틀 설정
							Paragraph title = new Paragraph("판매 목록", font2);
							// 가운데 정렬
							title.setAlignment(Element.ALIGN_CENTER);
							// 문서에 추가
							document.add(title);
							document.add(new Paragraph("\r\n"));

							// 생성 날짜
							LocalDate date = LocalDate.now();
							Paragraph writeDay = new Paragraph(date.toString(), font);

							// 오른쪽 정렬
							writeDay.setAlignment(Element.ALIGN_RIGHT);
							// 문서에 추가
							document.add(writeDay);
							document.add(new Paragraph("\r\n"));

							// 테이블 생성, Table객체 보다 pdftable객체가 더 정교하게 테이블을 구성할수 있다.
							// 생성자 수에 컬럼수를 적어준다
							PdfPTable table = new PdfPTable(14);
							// 컬럼의 폭을 정한다.
							table.setWidths(new int[] { 30, 70, 70, 70, 50, 50, 40, 60, 40, 60, 70, 100, 60, 100 });

							// 컬럼 타이틀 설정
							PdfPCell header1 = new PdfPCell(new Paragraph("번호", font));
							PdfPCell header2 = new PdfPCell(new Paragraph("주문번호", font));
							PdfPCell header3 = new PdfPCell(new Paragraph("주문품번", font));
							PdfPCell header4 = new PdfPCell(new Paragraph("주문품명", font));
							PdfPCell header5 = new PdfPCell(new Paragraph("품종", font));
							PdfPCell header6 = new PdfPCell(new Paragraph("사이즈", font));
							PdfPCell header7 = new PdfPCell(new Paragraph("컬러", font));
							PdfPCell header8 = new PdfPCell(new Paragraph("주문단가", font));
							PdfPCell header9 = new PdfPCell(new Paragraph("주문수량", font));
							PdfPCell header10 = new PdfPCell(new Paragraph("주문금액", font));
							PdfPCell header11 = new PdfPCell(new Paragraph("주문날짜", font));
							PdfPCell header12 = new PdfPCell(new Paragraph("이미지명", font));
							PdfPCell header13 = new PdfPCell(new Paragraph("고객명", font));
							PdfPCell header14 = new PdfPCell(new Paragraph("고객연락처", font));

							// 가로정렬
							header1.setHorizontalAlignment(Element.ALIGN_CENTER);
							header2.setHorizontalAlignment(Element.ALIGN_CENTER);
							header3.setHorizontalAlignment(Element.ALIGN_CENTER);
							header4.setHorizontalAlignment(Element.ALIGN_CENTER);
							header5.setHorizontalAlignment(Element.ALIGN_CENTER);
							header6.setHorizontalAlignment(Element.ALIGN_CENTER);
							header7.setHorizontalAlignment(Element.ALIGN_CENTER);
							header8.setHorizontalAlignment(Element.ALIGN_CENTER);
							header9.setHorizontalAlignment(Element.ALIGN_CENTER);
							header10.setHorizontalAlignment(Element.ALIGN_CENTER);
							header11.setHorizontalAlignment(Element.ALIGN_CENTER);
							header12.setHorizontalAlignment(Element.ALIGN_CENTER);
							header13.setHorizontalAlignment(Element.ALIGN_CENTER);
							header14.setHorizontalAlignment(Element.ALIGN_CENTER);

							// 세로정렬
							header1.setVerticalAlignment(Element.ALIGN_MIDDLE);
							header2.setVerticalAlignment(Element.ALIGN_MIDDLE);
							header3.setVerticalAlignment(Element.ALIGN_MIDDLE);
							header4.setVerticalAlignment(Element.ALIGN_MIDDLE);
							header5.setVerticalAlignment(Element.ALIGN_MIDDLE);
							header6.setVerticalAlignment(Element.ALIGN_MIDDLE);
							header7.setVerticalAlignment(Element.ALIGN_MIDDLE);
							header8.setVerticalAlignment(Element.ALIGN_MIDDLE);
							header9.setVerticalAlignment(Element.ALIGN_MIDDLE);
							header10.setVerticalAlignment(Element.ALIGN_MIDDLE);
							header11.setVerticalAlignment(Element.ALIGN_MIDDLE);
							header12.setVerticalAlignment(Element.ALIGN_MIDDLE);
							header13.setVerticalAlignment(Element.ALIGN_MIDDLE);
							header14.setVerticalAlignment(Element.ALIGN_MIDDLE);

							// 테이블에 셀 추가
							table.addCell(header1);
							table.addCell(header2);
							table.addCell(header3);
							table.addCell(header4);
							table.addCell(header5);
							table.addCell(header6);
							table.addCell(header7);
							table.addCell(header8);
							table.addCell(header9);
							table.addCell(header10);
							table.addCell(header11);
							table.addCell(header12);
							table.addCell(header13);
							table.addCell(header14);

							// DB연결 및 리스트 선택
							SalesDAO sdao8 = new SalesDAO();
							SalesVO svo8 = new SalesVO();
							ArrayList<SalesVO> list;
							list = sdao8.getSalesTotal();
							int rowCount = list.size();

							PdfPCell cell1 = null;
							PdfPCell cell2 = null;
							PdfPCell cell3 = null;
							PdfPCell cell4 = null;
							PdfPCell cell5 = null;
							PdfPCell cell6 = null;
							PdfPCell cell7 = null;
							PdfPCell cell8 = null;
							PdfPCell cell9 = null;
							PdfPCell cell10 = null;
							PdfPCell cell11 = null;
							PdfPCell cell12 = null;
							PdfPCell cell13 = null;
							PdfPCell cell14 = null;

							for (int index = 0; index < rowCount; index++) {
								svo8 = list.get(index);
								cell1 = new PdfPCell(new Paragraph(svo8.getNo() + "", font));
								cell2 = new PdfPCell(new Paragraph(svo8.getSa_num() + "", font));
								cell3 = new PdfPCell(new Paragraph(svo8.getSa_pnum() + "", font));
								cell4 = new PdfPCell(new Paragraph(svo8.getSa_pname() + "", font));
								cell5 = new PdfPCell(new Paragraph(svo8.getSa_pkind() + "", font));
								cell6 = new PdfPCell(new Paragraph(svo8.getSa_size() + "", font));
								cell7 = new PdfPCell(new Paragraph(svo8.getSa_color() + "", font));
								cell8 = new PdfPCell(new Paragraph(svo8.getSa_price() + "", font));
								cell9 = new PdfPCell(new Paragraph(svo8.getSa_count() + "", font));
								cell10 = new PdfPCell(new Paragraph(svo8.getSa_totalPrice() + "", font));
								cell11 = new PdfPCell(new Paragraph(svo8.getSa_date() + "", font));
								cell12 = new PdfPCell(new Paragraph(svo8.getSa_fileName() + "", font));
								cell13 = new PdfPCell(new Paragraph(svo8.getSa_CusName() + "", font));
								cell14 = new PdfPCell(new Paragraph(svo8.getSa_CusNum() + "", font));

								// 가로 정렬
								cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
								cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
								cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
								cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
								cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
								cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
								cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
								cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
								cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
								cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
								cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
								cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
								cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
								cell14.setHorizontalAlignment(Element.ALIGN_CENTER);

								// 세로 정렬
								cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
								cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
								cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
								cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
								cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
								cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
								cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
								cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
								cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);
								cell10.setVerticalAlignment(Element.ALIGN_MIDDLE);
								cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
								cell12.setVerticalAlignment(Element.ALIGN_MIDDLE);
								cell13.setVerticalAlignment(Element.ALIGN_MIDDLE);
								cell14.setVerticalAlignment(Element.ALIGN_MIDDLE);

								// 테이블에 셀 추가
								table.addCell(cell1);
								table.addCell(cell2);
								table.addCell(cell3);
								table.addCell(cell4);
								table.addCell(cell5);
								table.addCell(cell6);
								table.addCell(cell7);
								table.addCell(cell8);
								table.addCell(cell9);
								table.addCell(cell10);
								table.addCell(cell11);
								table.addCell(cell12);
								table.addCell(cell13);
								table.addCell(cell14);
							}
							// document 에 테이블 추가
							document.add(table);
							// 닫기 쓰기 종료
							document.close();

							txtSalesSaveFileDir.clear();
							btnSalesPDF.setDisable(true);
							btnSalesExcel.setDisable(true);

							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("PDF파일 생성");
							alert.setHeaderText("판매 목록 pdf파일 생성 성공");
							alert.setContentText("판매 목록 PDF파일");
							alert.showAndWait();

						} catch (FileNotFoundException e0) {
							e0.printStackTrace();

						} catch (DocumentException e0) {
							e0.printStackTrace();
						} catch (IOException e0) {
							e0.printStackTrace();
						}
					});

					// 데이터 피커 선택시 삭제버튼 비활성화 설정
					dpSalesListDateStart.setOnMouseClicked(e1 -> {
						btnSalesListDelete.setDisable(true);
					});
					dpSalesListDateEnd.setOnMouseClicked(e1 -> {
						btnSalesListDelete.setDisable(true);
					});

					// 고객명, 연락처 검색 텍스트 필드 선택시 삭제버튼 비활성화 설정
					txtSalesListNameSearch.setOnMouseClicked(e1 -> {
						btnSalesListDelete.setDisable(true);
					});
					txtSalesListCallSearch.setOnMouseClicked(e1 -> {
						btnSalesListDelete.setDisable(true);
					});

					// 판매 현황창 테이블뷰 상품 선택시 이미지가 화면에 송출, 수정 삭제 버튼 활성화 설정
					SalesListTableView.setOnMouseClicked(e1 -> {

						if (e1.getClickCount() != 2) {
							try {
								selectSalesList = SalesListTableView.getSelectionModel().getSelectedItems();

								no = selectSalesList.get(0).getNo();

								selectFileName = selectSalesList.get(0).getSa_fileName();
								localUrl = "file:/C:/imagesStock/" + selectFileName;
								localImage = new Image(localUrl, false);

								SalesListImageView.setFitWidth(300);
								SalesListImageView.setFitHeight(300);
								SalesListImageView.setImage(localImage);

								btnSalesListDelete.setDisable(false);

							} catch (Exception e2) {

							}
						}

					});

					// 판매 목록 삭제 버튼 기능 설정
					btnSalesListDelete.setOnAction(e1 -> {

						selectSalesList = SalesListTableView.getSelectionModel().getSelectedItems();
						selectedIndex = SalesListTableView.getSelectionModel().getSelectedIndex();
						String deleteSales = selectSalesList.get(0).getSa_pnum();
						int deleteSalesCount = selectSalesList.get(0).getSa_count();
						int deleteSalesNo = selectSalesList.get(0).getNo();

						try {
							Alert alert = new Alert(AlertType.CONFIRMATION);
							alert.setTitle("판매 이력 삭제");
							alert.setHeaderText("삭제한 데이터는 복구하실수 없습니다.");
							alert.setContentText("삭제를 진행하시겠습니까?");

							Optional<ButtonType> result = alert.showAndWait();

							if (result.get() == ButtonType.OK) {

								// 판매 이력에서 지워지는 품번의 갯수만큼 재고에 재등록
								saDao.inputPreSalesToStock(deleteSales, deleteSalesCount);

								// 판매이력 DB에서 삭제
								saDao.getSalesDelete(deleteSalesNo);

								salesData.removeAll(salesData);

								SalesTotalList();

								// 기본이미지 표출
								localUrl = "/image/default.gif";
								localImage = new Image(localUrl, false);
								SalesListImageView.setFitWidth(300);
								SalesListImageView.setFitHeight(300);
								SalesListImageView.setImage(localImage);

								// 총 판매량, 총판매액 띄우기
								totalSalesCountData = saDao.getSalesTotalCount(saVo).getSa_salesTotalCount();
								totalSalesPriceData = saDao.getSalesTotalCount(saVo).getSa_salesTotalPrice();

								txtSalesListTotalCount.setText(totalSalesCountData + "");
								txtSalesListTotalPrice.setText(totalSalesPriceData + "");

							} else {

							}

						} catch (Exception e2) {
							System.out.println("판매 이력 목록 삭제버튼 관련 오류" + e2);
						}

					});

					// 기간으로 검색 버튼 기능설정
					btnSalesListDateSearch.setOnAction(e1 -> {

						String startDay = dpSalesListDateStart.getValue() + ""; // 처음 선택된 시간값
						String endDay = dpSalesListDateEnd.getValue() + ""; // 두번째 선택된 시간값
						String nowDate = LocalDate.now() + ""; // 현재시간
						// 처음선택한 날짜와 두번째 날짜 비교
						// compare 값이 양수이면 첫번째 날짜가 큰값
						// 숫자 상으로 큰 값이 크다고 결과가 나옴
						int compare = startDay.compareTo(endDay); // 시작과 끝 중에 시작이 크면안됨
						int compare1 = startDay.compareTo(nowDate);// 시작과 현재중에 시작이 더 크면안됨
						int compare2 = endDay.compareTo(nowDate);// 끝과 현재중에 끝이 더 크면안됨

						if (startDay.equals("") || endDay.equals("")) {

							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("기간 조회 오류");
							alert.setHeaderText("조회할 기간을 선택하세요");
							alert.setContentText("기간 선택후 다시 시도하세요");
							alert.showAndWait();

						} else if (compare1 > 0 || compare2 > 0) {

							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("기간 조회 오류");
							alert.setHeaderText("기간이 미래로 설정되었습니다.");
							alert.setContentText("확인후 재시도하세요");
							alert.showAndWait();

						} else if (compare > 0) {

							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("기간 조회 오류");
							alert.setHeaderText("기간 선택이 잘못되었습니다.");
							alert.setContentText("조회를 시작할 날짜가 끝날짜보다 미래로 설정되었습니다.");
							alert.showAndWait();
						} else {

							/*
							 * dpSalesListDateStart.setValue(LocalDate.now());
							 * dpSalesListDateEnd.setValue(LocalDate.now());
							 */

							salesData.removeAll(salesData);

							SalesVO saVo1 = null;

							Object[][] totalData;

							ArrayList<String> title;
							ArrayList<SalesVO> list;

							title = saDao.getSalesColumnName();
							int columnCount = title.size();

							list = saDao.dateSalesListSearch(startDay, endDay);
							int rowCount = list.size();

							totalData = new Object[rowCount][columnCount];

							for (int index = 0; index < rowCount; index++) {
								saVo1 = list.get(index);
								salesData.add(saVo1);
							}
							try {
								// 기간으로 조회한 결과값의 총 수량과 총액 송출
								totalSalesCountData = saDao.getSalesDateTotalCount(startDay, endDay)
										.getSa_salesTotalCount();
								totalSalesPriceData = saDao.getSalesDateTotalCount(startDay, endDay)
										.getSa_salesTotalPrice();

								txtSalesListTotalCount.setText(totalSalesCountData + "");
								txtSalesListTotalPrice.setText(totalSalesPriceData + "");
							} catch (Exception e4) {

							}

							btnSalesListDelete.setDisable(true);

						}

					});

					// 판매목록 전체보기
					btnSalesTotalList.setOnAction(e1 -> {

						salesData.removeAll(salesData);

						SalesTotalList();

						btnSalesListDelete.setDisable(true);

						/*
						 * SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd"); Date date =
						 * null; try { date = format.parse(saDao.startDateSales()); } catch
						 * (ParseException pe) {
						 * 
						 * }
						 */

						dpSalesListDateStart.setValue(LocalDate.parse(saDao.startDateSales()));
						dpSalesListDateEnd.setValue(LocalDate.now());

						// 기본이미지 표출
						localUrl = "/image/default.gif";
						localImage = new Image(localUrl, false);
						SalesListImageView.setFitWidth(300);
						SalesListImageView.setFitHeight(300);
						SalesListImageView.setImage(localImage);

						try {
							// 총 판매량, 총판매액 띄우기
							totalSalesCountData = saDao.getSalesTotalCount(saVo).getSa_salesTotalCount();
							totalSalesPriceData = saDao.getSalesTotalCount(saVo).getSa_salesTotalPrice();

							txtSalesListTotalCount.setText(totalSalesCountData + "");
							txtSalesListTotalPrice.setText(totalSalesPriceData + "");
						} catch (Exception e3) {

						}

					});

					// 판매목록 이름과 번호로 조회 버튼 기능설정
					btnSalesListNameSearch.setOnAction(e1 -> {

						salesData.removeAll(salesData);

						SalesVO saVo1 = new SalesVO();
						SalesDAO saDao1 = null;

						Object[][] totalData = null;

						String salesListName = null;
						String salesListNum = null;
						boolean searchResult = false;

						ArrayList<SalesVO> saList = new ArrayList<>();

						try {
							// 입력한 이름과 번호 불러옴
							salesListName = txtSalesListNameSearch.getText().trim();
							salesListNum = txtSalesListCallSearch.getText().trim();

							saDao1 = new SalesDAO();
							saList = saDao1.getArraySalesListSearchCusName(salesListName, salesListNum);

							// 한곳이라도 입력 안햇을때 경고창 알람
							if (salesListName.equals("") || salesListNum.equals("")) {

								searchResult = true;

								salesData.removeAll(salesData);
								SalesTotalList();

								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("판매목록 고객명,고객번호 조회");
								alert.setHeaderText("판매목록중 검색할 고객명과 고객번호를 입력하세요");
								alert.setContentText("다음엔 주의하세요");
								alert.showAndWait();

							}

							// 둘다 빈칸이 아니고 데이터 베이스 결과값이 존재할때
							if (!salesListName.equals("") && !salesListNum.equals("") && (saList != null)) {
								ArrayList<String> title;

								title = saDao1.getSalesColumnName();
								int columnCount = title.size();

								saList = saDao1.getArraySalesListSearchCusName(salesListName, salesListNum);
								int rowCount = saList.size();

								totalData = new Object[rowCount][columnCount];

								salesData.removeAll(salesData);

								txtSalesListNameSearch.clear();
								txtSalesListCallSearch.clear();

								for (int index = 0; index < rowCount; index++) {

									System.out.println(index);

									SalesVO saVo2 = saList.get(index);

									salesData.add(saVo2);
									searchResult = true;

								}

								// 이름과 번호로 조회한 결과값의 총 수량과 총액 송출
								totalSalesCountData = saDao1.getSalesCusNameTotalCount(salesListName, salesListNum)
										.getSa_salesTotalCount();
								totalSalesPriceData = saDao1.getSalesCusNameTotalCount(salesListName, salesListNum)
										.getSa_salesTotalPrice();

								txtSalesListTotalCount.setText(totalSalesCountData + "");
								txtSalesListTotalPrice.setText(totalSalesPriceData + "");

							}

							if (!searchResult) {

								salesData.removeAll(salesData);
								txtSalesListNameSearch.clear();
								txtSalesListCallSearch.clear();

								SalesTotalList();

								// 총 판매량, 총판매액 띄우기
								totalSalesCountData = saDao.getSalesTotalCount(saVo).getSa_salesTotalCount();
								totalSalesPriceData = saDao.getSalesTotalCount(saVo).getSa_salesTotalPrice();

								txtSalesListTotalCount.setText(totalSalesCountData + "");
								txtSalesListTotalPrice.setText(totalSalesPriceData + "");

								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("판매 이력 조회");
								alert.setHeaderText(
										"이름 :" + salesListName + "번호 : " + salesListNum + "의 판매가 리스트에 없습니다.");
								alert.setContentText("다시 검색하세요");
								alert.showAndWait();

							}

						} catch (Exception e3) {

							SalesTotalList();

							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("판매 이력 고객명, 연락처 조회");
							alert.setHeaderText("판매 이력 검색에 오류가 발생하였습니다.");
							alert.setContentText("다시하세요");
							alert.showAndWait();
							e3.printStackTrace();

						}

					});

					// 판매 현황 탭 종료
					btnSalesListExit.setOnAction(e1 -> {
						salesListdialog.close();
					});

				} catch (Exception ex) {

				}
			});

			// 판매 대기 목록 수정
			btnPreSalesEdit.setOnAction(e -> {

				selectPreSales = PreSalesTableView.getSelectionModel().getSelectedItems();
				selectedIndex = PreSalesTableView.getSelectionModel().getSelectedIndex();

				try {
					/****************************
					 * 판매 대기 목록 수량 수정 화면 띄우기
					 ****************************/
					FXMLLoader salesEditLoader = new FXMLLoader();
					salesEditLoader.setLocation(getClass().getResource("/View/salesCountEdit.fxml"));

					Stage salesEditdialog = new Stage(StageStyle.DECORATED);
					salesEditdialog.initModality(Modality.WINDOW_MODAL);
					salesEditdialog.initOwner(btnSalesListView.getScene().getWindow());
					salesEditdialog.setTitle("판매 수량 수정");

					Parent parentSalesEdit = (Parent) salesEditLoader.load();
					Scene salesEditScene = new Scene(parentSalesEdit);
					salesEditdialog.setScene(salesEditScene);
					salesEditdialog.setResizable(false);
					salesEditdialog.show();

					/************************
					 * 화면 버튼및 텍스트필드 정의
					 ***********************/

					TextField txtCurrentSalesCount = (TextField) parentSalesEdit.lookup("#txtCurrentSalesCount");
					TextField txtSalesCountEdit = (TextField) parentSalesEdit.lookup("#txtSalesCountEdit");

					Button btnSalesCountEditOK = (Button) parentSalesEdit.lookup("#btnSalesCountEditOK");
					Button btnSalesCountEditExit = (Button) parentSalesEdit.lookup("#btnSalesCountEditExit");

					/************************
					 * 기능 설정
					 ************************/
					txtCurrentSalesCount.setText(selectPreSales.get(0).getSa_count() + "");
					txtCurrentSalesCount.setEditable(false);

					// 수정 버튼 설정
					btnSalesCountEditOK.setOnAction(e2 -> {

						SalesVO saVO = new SalesVO();
						SalesDAO saDao = new SalesDAO();
						StockDAO sDao = new StockDAO();

						try {
							if (txtSalesCountEdit.getText().equals("")
									|| Integer.parseInt(txtSalesCountEdit.getText()) <= 0) {

								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("판매 수량 수정");
								alert.setHeaderText("수정할 판매수량을 입력하세요");
								alert.setContentText("수정할 판매수량이 누락 혹은 0,음수로 설정되었습니다.");
								alert.showAndWait();

							} else if (Integer.parseInt(txtCurrentSalesCount.getText()) == Integer
									.parseInt(txtSalesCountEdit.getText())) {

								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("판매 수량 수정");
								alert.setHeaderText("기존의 수량과 다른 값을 입력하세요");
								alert.setContentText("판매수량 수정값이 잘못되었습니다.");
								alert.showAndWait();

							} else if ((sDao.countStockGoods(selectPreSales.get(0).getSa_pnum())
									+ Integer.parseInt(txtCurrentSalesCount.getText())) < Integer
											.parseInt(txtSalesCountEdit.getText())) {

								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("판매 수량 수정");
								alert.setHeaderText("판매수량 수정값 오류");
								alert.setContentText("판매수량 수정값이 재고량을 초과하였습니다.");
								alert.showAndWait();

							} else {

								// 판매대기 총수량 초기화
								int totalX = 0;
								// 판매대기 총액 초기화
								int totalY = 0;

								// 수정할 수량이 기존에 등록한 수량보다 작을경우
								if (Integer.parseInt(txtCurrentSalesCount.getText()) > Integer
										.parseInt(txtSalesCountEdit.getText())) {
									int x = Integer.parseInt(txtCurrentSalesCount.getText())
											- Integer.parseInt(txtSalesCountEdit.getText());

									saDao.inputPreSalesToStock(selectPreSales.get(0).getSa_pnum(), x);

									saVO = new SalesVO(selectPreSales.get(0).getSa_pnum(),
											selectPreSales.get(0).getSa_pname(), selectPreSales.get(0).getSa_pkind(),
											selectPreSales.get(0).getSa_size(), selectPreSales.get(0).getSa_color(),
											selectPreSales.get(0).getSa_price(),
											Integer.parseInt(txtSalesCountEdit.getText()),
											(Integer.parseInt(txtSalesCountEdit.getText())
													* selectPreSales.get(0).getSa_price()),
											selectPreSales.get(0).getSa_fileName(),
											selectPreSales.get(0).getSa_CusName(),
											selectPreSales.get(0).getSa_CusNum());

									preSalesData.set(selectedIndex, saVO);

								}
								// 수정할 수량이 기존에 등록한 수량보다 많을경우
								else {
									int y = Integer.parseInt(txtSalesCountEdit.getText())
											- Integer.parseInt(txtCurrentSalesCount.getText());

									saDao.deletePreSalesToStock(selectPreSales.get(0).getSa_pnum(), y);

									saVO = new SalesVO(selectPreSales.get(0).getSa_pnum(),
											selectPreSales.get(0).getSa_pname(), selectPreSales.get(0).getSa_pkind(),
											selectPreSales.get(0).getSa_size(), selectPreSales.get(0).getSa_color(),
											selectPreSales.get(0).getSa_price(),
											Integer.parseInt(txtSalesCountEdit.getText()),
											(Integer.parseInt(txtSalesCountEdit.getText())
													* selectPreSales.get(0).getSa_price()),
											selectPreSales.get(0).getSa_fileName(),
											selectPreSales.get(0).getSa_CusName(),
											selectPreSales.get(0).getSa_CusNum());

									preSalesData.set(selectedIndex, saVO);

								}

								int datasize = preSalesData.size();

								for (int i = 0; i < datasize; i++) {
									int x = PreSalesTableView.getItems().get(i).getSa_count();
									totalX = totalX + x;

									int y = PreSalesTableView.getItems().get(i).getSa_price()
											* PreSalesTableView.getItems().get(i).getSa_count();
									totalY = totalY + y;
								}

								txtPreSalesToTalCount.setText(totalX + "");
								txtPreSalesToTalPrice.setText(totalY + "");

								salesEditdialog.close();

							}
						} catch (Exception e3) {

							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("판매 수량 수정");
							alert.setHeaderText("수정할 판매수량을 숫자로 입력하세요");
							alert.setContentText("수정할 수량이 잘못 입력되었습니다.");
							alert.showAndWait();

						}

					});

					// 취소 버튼 설정
					btnSalesCountEditExit.setOnAction(e2 -> {

						salesEditdialog.close();
					});
				} catch (Exception e1) {

				}

			});

			// 판매 대기 목록 삭제
			btnPreSalesDelete.setOnAction(e -> {

				// 판매대기 총수량 초기화
				int totalX = 0;
				// 판매대기 총액 초기화
				int totalY = 0;

				SalesDAO saDao = null;
				saDao = new SalesDAO();
				selectPreSales = PreSalesTableView.getSelectionModel().getSelectedItems();
				selectedIndex = PreSalesTableView.getSelectionModel().getSelectedIndex();
				String deletePreSales = selectPreSales.get(0).getSa_pnum();
				int deletePreSalesCount = selectPreSales.get(0).getSa_count();

				// int deleteNum = selectWareList.get(0).getNo();

				try {
					// 삭제 버튼 누를시 알람메세지 팝업설정
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("삭제메세지");
					alert.setHeaderText("삭제한 데이터는 복구하실수 없습니다.");
					alert.setContentText("삭제를 진행하시겠습니까?");

					Optional<ButtonType> result = alert.showAndWait();

					if (result.get() == ButtonType.OK) {

						saDao.inputPreSalesToStock(deletePreSales, deletePreSalesCount);
						preSalesData.remove(selectedIndex);

						if (preSalesData.size() == 0) {
							btnPreSalesOk.setDisable(true);
							btnPreSalesEdit.setDisable(true);
							btnPreSalesDelete.setDisable(true);
						} else {
							btnPreSalesOk.setDisable(false);
						}

						// 기본이미지 표출
						localUrl = "/image/default.gif";
						localImage = new Image(localUrl, false);
						SalesImageView.setFitWidth(300);
						SalesImageView.setFitHeight(300);
						SalesImageView.setImage(localImage);

						int datasize = preSalesData.size();

						for (int i = 0; i < datasize; i++) {
							int x = PreSalesTableView.getItems().get(i).getSa_count();
							totalX = totalX + x;

							int y = PreSalesTableView.getItems().get(i).getSa_price()
									* PreSalesTableView.getItems().get(i).getSa_count();
							totalY = totalY + y;
						}

						txtPreSalesToTalCount.setText(totalX + "");
						txtPreSalesToTalPrice.setText(totalY + "");

					} else {

					}
				} catch (Exception e1) {
					System.out.println("판매대기목록 삭제버튼 관련 오류" + e1);
				}

			});

			// 판매 버튼 기능설정
			btnPreSalesOk.setOnAction(e -> {

				SalesDAO saDao = new SalesDAO();
				SalesVO saVo = null;

				for (int i = 0; i < preSalesData.size(); i++) {

					saVo = new SalesVO(preSalesData.get(i).getSa_pnum(), preSalesData.get(i).getSa_pname(),
							preSalesData.get(i).getSa_pkind(), preSalesData.get(i).getSa_size(),
							preSalesData.get(i).getSa_color(), preSalesData.get(i).getSa_price(),
							preSalesData.get(i).getSa_count(), preSalesData.get(i).getSa_totalPrice(),
							preSalesData.get(i).getSa_fileName(), preSalesData.get(i).getSa_CusName(),
							preSalesData.get(i).getSa_CusNum());

					try {

						saDao.inputNewSales(saVo);

					} catch (Exception e1) {

						System.out.println("판매버튼실행시 판매 테이블 데이터 입력오류 룻컨창" + e1);

					}

				}

				preSalesData.removeAll(preSalesData);
				txtPreSalesToTalCount.clear();
				txtPreSalesToTalPrice.clear();

				btnPreSalesOk.setDisable(true);
				btnPreSalesDelete.setDisable(true);
				btnPreSalesEdit.setDisable(true);

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("판매 완료");
				alert.setHeaderText("판매 / 판매 목록 등록완료");
				alert.setContentText("판매 / 판매목록등록이 정상적으로 완료되었습니다.");
				alert.showAndWait();

				// 기본이미지 표출
				localUrl = "/image/default.gif";
				localImage = new Image(localUrl, false);
				SalesImageView.setFitWidth(300);
				SalesImageView.setFitHeight(300);
				SalesImageView.setImage(localImage);

			});

			// 판매창 종료
			btnSalesExit.setOnAction(e -> {

				if (preSalesData.size() == 0) {

					salesdialog.close();

				} else {

					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("판매 종료 오류");
					alert.setHeaderText("판매 대기중인 물품이 존재합니다.");
					alert.setContentText("판매 대기중인 목록을 전부 삭제후 종료해주세요");

					alert.showAndWait();
				}

			});

		} catch (

		Exception e) {

		}
	}

	// 판매목록 전체 리스트
	public void SalesTotalList() {

		Object[][] totalData;

		SalesDAO saDao = new SalesDAO();
		SalesVO saVo = null;
		ArrayList<String> title;
		ArrayList<SalesVO> list;

		title = saDao.getSalesColumnName();
		int columnCount = title.size();

		list = saDao.getSalesTotal();
		int rowCount = list.size();

		totalData = new Object[rowCount][columnCount];

		for (int index = 0; index < rowCount; index++) {
			saVo = list.get(index);
			salesData.add(saVo);
		}

	}

	// 입고목록 전체 리스트
	public void warehousingTotalList() {

		Object[][] totalData;

		WarehousingDAO wDao = new WarehousingDAO();
		WarehousingVO wVo = null;
		ArrayList<String> title;
		ArrayList<WarehousingVO> list;

		title = wDao.getWareColumnName();
		int columnCount = title.size();

		list = wDao.getWareTotal();
		int rowCount = list.size();

		totalData = new Object[rowCount][columnCount];

		for (int index = 0; index < rowCount; index++) {
			wVo = list.get(index);
			wareData.add(wVo);
		}

	}

	// 주문 목록 전체 리스트
	public void orderListTotalList() {
		Object[][] totalData;

		OrderDAO oDao = new OrderDAO();
		OrderVO oVo = null;
		ArrayList<String> title;
		ArrayList<OrderVO> list;

		title = oDao.getOrderColumnName();
		int columnCount = title.size();

		list = oDao.getOrderTotal();
		int rowCount = list.size();

		totalData = new Object[rowCount][columnCount];

		for (int index = 0; index < rowCount; index++) {
			oVo = list.get(index);
			orderData.add(oVo);
		}
	}

}
