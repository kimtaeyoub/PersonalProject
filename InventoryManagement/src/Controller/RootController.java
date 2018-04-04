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
	private Button btnOrder; // ����â �ֹ� ��ư
	@FXML
	private Button btnSales; // ����â �Ǹ� ��ư
	@FXML
	private Button btnStock; // ����â ��� ��ư
	@FXML
	private Button btnExit; // ����â �����ư
	@FXML
	private ImageView imageView; // ����â �̹��� ��

	private Stage primaryStage; // �̹������� ã���� ���Ǵ� �������� ����
	String selectFileName = "";// �̹������ϸ�
	String localUrl = ""; // �̹��� ���� ���
	Image localImage; // �̹��� ����
	File selectedFile = null; // ���õ� ���� ����
	// �̹��� ���� ���� ����
	private File dirSave = new File("C:/imagesStock");
	// �̹��� �ҷ��� ������ ������ ���� ��ü ����
	private File file = null;

	// �Է¹������� �����
	ObservableList<StockVO> data = FXCollections.observableArrayList();
	ObservableList<OrderVO> orderData = FXCollections.observableArrayList();
	ObservableList<WarehousingVO> wareData = FXCollections.observableArrayList();
	ObservableList<SalesVO> preSalesData = FXCollections.observableArrayList();
	ObservableList<SalesVO> salesData = FXCollections.observableArrayList();

	// ��� �Ѽ��� �����
	int totalCountData = 0;
	// ��� �Ѿ� �����
	String totalPriceData = null;
	// �ֹ� �Ѽ��� �����
	int totalOrderCountData = 0;
	// �ֹ� �Ѿ� �����
	String totalOrderPriceData = null;
	// �԰� �Ѽ��� �����
	int totalWareCountData = 0;
	// �԰� �Ѿ� �����
	String totalWarePriceData = null;
	// �ǸŴ�� �Ѽ��� �����
	int totalPreSalesCountData = 0;
	// �ǸŴ�� �Ѿ� �����
	int totalPreSalesPriceData = 0;
	// �ǸŸ�� �Ѽ��� �����
	int totalSalesCountData = 0;
	// �ǸŸ�� �Ѿ� �����
	String totalSalesPriceData = null;

	// ��� ��� ���̺�信�� ������ ���� ����� (���â ����)
	ObservableList<StockVO> selectStock = null;
	// �ֹ���� �˻� ���̺�信�� ������ ���� ����� (�ֹ�â ����)
	ObservableList<StockVO> selectOrder = null;
	// �ֹ� ��� ���̺�� ���� ������ ��������� ����
	ObservableList<OrderVO> selectOrderList = null;
	// �԰� ��� ���̺�� ���� ������ ���� ���� ����
	ObservableList<WarehousingVO> selectWareList = null;
	// �Ǹ� �˻� ���̺�信�� ������ ���� ����� (�Ǹ�â ����)
	ObservableList<StockVO> selectSales = null;
	// �ǸŴ�� ���̺�� ���� ������ ���� ����� (�Ǹ�â)
	ObservableList<SalesVO> selectPreSales = null;
	// �Ǹ� ��� ���̺�信�� ������ ���� �����
	ObservableList<SalesVO> selectSalesList = null;

	// ������ ��ȣ����
	int no;

	// ���̺��� ������ ���� �ε��� ����
	int selectedIndex;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		/****************************
		 * ���κ� �̹��� ����Ʈ �̹��� ����
		 ***************************/

		localUrl = "/image/bluedog.jpg"; // ����Ʈ �̹��� ����
		localImage = new Image(localUrl, false);
		imageView.setImage(localImage);

		/*****************************
		 * ���κ� ��ư ��ɼ���
		 *****************************/

		btnOrder.setOnAction(event -> handleBtnOrderAction(event));
		btnSales.setOnAction(event -> handleBtnSalesAction(event));
		btnStock.setOnAction(event -> handleBtnStockAction(event));
		btnExit.setOnAction(event -> Platform.exit());

	}

	// ����ư �̺�Ʈó�� / ���̾�α�â ����
	public void handleBtnStockAction(ActionEvent event) {

		data.removeAll(data);

		try {

			/********************************
			 * ȭ����� ���� �ڵ�
			 ****************************/

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/View/stock.fxml"));

			Stage dialog = new Stage(StageStyle.DECORATED);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(btnStock.getScene().getWindow());
			dialog.setTitle("������");

			Parent parentStock = (Parent) loader.load();
			Scene scene = new Scene(parentStock);
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.show();

			/******************************************
			 * ��� ���â ��ư�� �ؽ�Ʈ�ʵ�, �̹��� ��, ���̺�� ����
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
			 * ��� �̹����� ����Ʈ �̹��� ����
			 ************************/

			localUrl = "/image/default.gif"; // ����Ʈ �̹��� ����
			localImage = new Image(localUrl, false);
			stockImageView.setFitWidth(300);
			stockImageView.setFitHeight(300);
			stockImageView.setImage(localImage);

			// ���̺�� �����Ұ� ���� ����
			stockTableView.setEditable(false);
			// ���� ���� ��ư ���Ұ����¼���
			btnEditGoods.setDisable(true);
			btnDeleteGoods.setDisable(true);
			btnOrderSalesRate.setDisable(true);
			// ���� pdf��ư ���Ұ� ���� ����
			btnStockExcel.setDisable(true);
			btnStockPDF.setDisable(true);

			/******************************************
			 * ��� ���â ��ư, �ؽ�Ʈ�ʵ�, �̹�����, ���̺�� ��ɼ���
			 ******************************************/

			// ����, pdf���� ���� ���ó��
			// ���� ���ϻ���
			btnStockExcel.setOnAction(e -> {

				StockDAO sDao9 = new StockDAO();
				boolean saveSuccess;

				ArrayList<StockVO> list;
				list = sDao9.getStockTotal();
				StockExcel excelWriter = new StockExcel();

				// xlsx ���Ͼ���
				saveSuccess = excelWriter.xlsxWiter(list, txtStockSaveFileDir.getText());
				if (saveSuccess) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("���� ���� ����");
					alert.setHeaderText("��� ��� ���� ���� ���� ����");
					alert.setContentText("��� ��� ���� ����");

					alert.showAndWait();
				}
				txtStockSaveFileDir.clear();

				btnStockExcel.setDisable(true);
				btnStockPDF.setDisable(true);

			});

			// PDF���� ����
			btnStockPDF.setOnAction(e -> {

				try {
					// pdf document ����
					// (Rectangle pageSize, float marginLeft,float marginRight,float marginTop,
					// float marginBotton)
					Document document = new Document(PageSize.A4, 0, 0, 30, 30);

					// pdf������ ������ ������ ����, pdf������ �����ȴ�. ���� ��Ʈ������ ����.
					String strReportPDFName = "stock_" + System.currentTimeMillis() + ".pdf";
					PdfWriter.getInstance(document,
							new FileOutputStream(txtStockSaveFileDir.getText() + "\\" + strReportPDFName));

					// document�� ���� pdf������ ���� �ֵ����Ѵ�.
					document.open();

					// �ѱ� ���� ��Ʈ ����
					BaseFont bf = BaseFont.createFont("font/MALGUN.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
					Font font = new Font(bf, 8, Font.NORMAL);
					Font font2 = new Font(bf, 14, Font.BOLD);

					// Ÿ��Ʋ
					Paragraph title = new Paragraph("��� ���", font2);

					// ��� ����
					title.setAlignment(Element.ALIGN_CENTER);

					// ������ �߰�
					document.add(title);
					document.add(new Paragraph("\r\n"));

					// ���� ��¥
					LocalDate date = LocalDate.now();
					Paragraph writeDay = new Paragraph(date.toString(), font);

					// ������ ����
					writeDay.setAlignment(Element.ALIGN_RIGHT);

					// ������ �߰�
					document.add(writeDay);
					document.add(new Paragraph("\r\n"));

					// ���̺� ���� Table��ü ���� PdfTable��ü�� �� �����ϰ� ���̺��� ���� �� �ִ�.
					// �����ڿ� �÷����� �����ش�
					PdfPTable table = new PdfPTable(10);
					// ������ �÷��� width�� ���Ѵ�. ���ݼ���
					table.setWidths(new int[] { 30, 50, 50, 40, 40, 40, 50, 50, 60, 100 });

					// �÷� Ÿ��Ʋ����
					PdfPCell header1 = new PdfPCell(new Paragraph("��ȣ", font));
					PdfPCell header2 = new PdfPCell(new Paragraph("��ǰ��ȣ", font));
					PdfPCell header3 = new PdfPCell(new Paragraph("��ǰ��", font));
					PdfPCell header4 = new PdfPCell(new Paragraph("��ǰ����", font));
					PdfPCell header5 = new PdfPCell(new Paragraph("������", font));
					PdfPCell header6 = new PdfPCell(new Paragraph("��ǰ�÷�", font));
					PdfPCell header7 = new PdfPCell(new Paragraph("��ǰ�ܰ�", font));
					PdfPCell header8 = new PdfPCell(new Paragraph("������", font));
					PdfPCell header9 = new PdfPCell(new Paragraph("����հ�", font));
					PdfPCell header10 = new PdfPCell(new Paragraph("�̹������ϸ�", font));

					// ���� ����
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

					// ���� ����
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

					// ���̺� �߰�
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

					// DB���� �� ����Ʈ ����
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

						// ���� ����
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

						// ���� ����
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

						// ���̺� �� �߰�
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

					// ������ ���̺� �߰�
					document.add(table);

					// ������ �ݴ´�. ���� ����
					document.close();

					txtStockSaveFileDir.clear();
					btnStockPDF.setDisable(true);
					btnStockExcel.setDisable(true);

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("PDF ���� ����");
					alert.setHeaderText("��� ��� PDF ���� ���� ����");
					alert.setContentText("��� ��� PDF ����");
					alert.showAndWait();

				} catch (FileNotFoundException e8) {
					e8.printStackTrace();
				} catch (DocumentException e8) {
					e8.printStackTrace();
				} catch (IOException e8) {
					e8.printStackTrace();
				}

			});

			// ���� ���� ����
			btnStockSaveFileDir.setOnAction(e -> {
				final DirectoryChooser stockDirectoryChooser = new DirectoryChooser();
				final File selectedStockDirectory = stockDirectoryChooser.showDialog(primaryStage);

				if (selectedStockDirectory != null) {
					txtStockSaveFileDir.setText(selectedStockDirectory.getAbsolutePath());
					btnStockExcel.setDisable(false);
					btnStockPDF.setDisable(false);
				}
			});

			// �űԻ�ǰ��� ���â �̺�Ʈó��
			btnNewGoods.setOnAction(e -> {

				try {

					/**********************
					 * �ű� ��ǰ��� ���â ȭ����� �ڵ�
					 **********************/
					FXMLLoader newGoodsloader = new FXMLLoader();
					newGoodsloader.setLocation(getClass().getResource("/View/newGoods.fxml"));

					Stage newGoodsDialog = new Stage(StageStyle.DECORATED);
					newGoodsDialog.initModality(Modality.WINDOW_MODAL);
					newGoodsDialog.initOwner(btnNewGoods.getScene().getWindow());
					newGoodsDialog.setTitle("�ű� ��ǰ ���");

					Parent parentNewGoods = (Parent) newGoodsloader.load();
					Scene newGoodsScene = new Scene(parentNewGoods);
					newGoodsDialog.setScene(newGoodsScene);
					newGoodsDialog.setResizable(false);
					newGoodsDialog.show();

					/***************************************
					 * �ű� ��ǰ��� ���â �ؽ�Ʈ�ʵ�, ��ư, �̹����� ����
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
					 * �ű� ��ǰ��� ���â ��ư, �̹�����, �ؽ�Ʈ�ʵ� ��ɼ���
					 *****************************************/

					// �ű� ��ǰ ����â ��� ��ư ��ɼ���
					btnNewGoodsOk.setOnAction(ev -> {
						StockDAO sDao5 = new StockDAO();
						try {

							if (txtNewGoodsPnum.getText().equals("") || txtNewGoodsPname.getText().equals("")
									|| txtNewGoodsPkind.getText().equals("") || txtNewGoodsSize.getText().equals("")
									|| txtNewGoodsColor.getText().equals("") || txtNewGoodsPrice.getText().equals("")) {

								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("��ǰ ���� �Է¿���");
								alert.setHeaderText("��ǰ�����Է��� �����Ǿ����ϴ�.");
								alert.setContentText("Ȯ���� �������� ���� �Է����ּ���");
								alert.showAndWait();

							} else if (Integer.parseInt(txtNewGoodsPrice.getText()) <= 0) {

								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("��ǰ ���� �Է¿���");
								alert.setHeaderText("�ܰ� å���� �߸��Ǿ����ϴ�.");
								alert.setContentText("�ܰ��� 0�� Ȥ�� - �� �ɼ� �����ϴ�.");
								alert.showAndWait();
							} else if (sDao5.isStockPnum(txtNewGoodsPnum.getText())) {

								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("��ǰ ���� �Է¿���");
								alert.setHeaderText("ǰ���Է��� �߸��Ǿ����ϴ�.");
								alert.setContentText("�ش� ǰ���� �̹� �����մϴ�. Ȯ���� �ٽ� �Է��ϼ���.");
								alert.showAndWait();

							} else {

								data.removeAll(data);
								StockVO sVo = null;
								StockDAO sDao = new StockDAO();
								File dirMake = new File(dirSave.getAbsolutePath());

								// �̹��� ����
								if (!dirMake.exists()) {
									dirMake.mkdir();
								}

								// �̹��� ���� ����
								String fileName = imageSave(selectedFile);

								// ��� ���� ����
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
										alert.setTitle("�ű� ��ǰ���� ���");
										alert.setHeaderText(txtNewGoodsPnum.getText() + " ���� ��ǰ�� ���������� �߰� �Ǿ����ϴ�.");
										alert.setContentText("���� ��ǰ������ �Է��ϼ���");

										alert.showAndWait();

										// �⺻�̹����� ����
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
							alert.setTitle("��ǰ���� �Է�");
							alert.setHeaderText("��ǰ ������ ��Ȯ�� Ȯ���� �Է��ϼ���.");
							alert.setContentText("�������� �����ϼ���.");
							alert.showAndWait();

						}

					});

					// �̹����� ���� (����Ʈ���¼���)
					localUrl = "/image/default.gif";
					localImage = new Image(localUrl, false);
					NewGoodsImageView.setImage(localImage);

					// �̹��� ���� ��ư ��ɼ���
					btnNewGoodsImage.setOnAction(ev -> {
						FileChooser fileChooser = new FileChooser();
						fileChooser.getExtensionFilters()
								.addAll(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.gif"));

						try {
							selectedFile = fileChooser.showOpenDialog(primaryStage);
							if (selectedFile != null) {
								// �̹��� ���ϰ��
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

					// �ű� ��ǰ����â �����ư ��ɼ���
					btnNewGoodsExit.setOnAction(ev -> {
						newGoodsDialog.close();
					});

				} catch (Exception e1) {
					System.out.println("�ű� ��ǰ ���â ���� ����!!!!!\n" + e1.toString());
				}

			});

			// ���̺���÷� ����

			TableColumn colNo = new TableColumn("NO");
			colNo.setMinWidth(40);
			colNo.setStyle("-fx-alignment:CENTER");
			colNo.setCellValueFactory(new PropertyValueFactory<>("no"));

			TableColumn colst_pnum = new TableColumn("ǰ��");
			colst_pnum.setMinWidth(140);
			colst_pnum.setStyle("-fx-alignment:CENTER");
			colst_pnum.setCellValueFactory(new PropertyValueFactory<>("st_pnum"));

			TableColumn colst_pname = new TableColumn("ǰ��");
			colst_pname.setMinWidth(160);
			colst_pname.setStyle("-fx-alignment:CENTER");
			colst_pname.setCellValueFactory(new PropertyValueFactory<>("st_pname"));

			TableColumn colst_pkind = new TableColumn("ǰ��");
			colst_pkind.setMinWidth(100);
			colst_pkind.setStyle("-fx-alignment:CENTER");
			colst_pkind.setCellValueFactory(new PropertyValueFactory<>("st_pkind"));

			TableColumn colst_size = new TableColumn("������");
			colst_size.setMinWidth(80);
			colst_size.setStyle("-fx-alignment:CENTER");
			colst_size.setCellValueFactory(new PropertyValueFactory<>("st_size"));

			TableColumn colst_color = new TableColumn("�÷�");
			colst_color.setMinWidth(60);
			colst_color.setStyle("-fx-alignment:CENTER");
			colst_color.setCellValueFactory(new PropertyValueFactory<>("st_color"));

			TableColumn colst_price = new TableColumn("�ܰ�");
			colst_price.setMinWidth(80);
			colst_price.setStyle("-fx-alignment:CENTER");
			colst_price.setCellValueFactory(new PropertyValueFactory<>("st_price"));

			TableColumn colst_count = new TableColumn("����");
			colst_count.setMinWidth(40);
			colst_count.setStyle("-fx-alignment:CENTER");
			colst_count.setCellValueFactory(new PropertyValueFactory<>("st_count"));

			TableColumn colst_totalPrice = new TableColumn("�ѱݾ�");
			colst_totalPrice.setMinWidth(100);
			colst_totalPrice.setStyle("-fx-alignment:CENTER");
			colst_totalPrice.setCellValueFactory(new PropertyValueFactory<>("st_totalPrice"));

			TableColumn colst_filename = new TableColumn("���ϸ�");
			colst_filename.setMinWidth(200);
			colst_filename.setCellValueFactory(new PropertyValueFactory<>("st_filename"));

			// ���̺�信 �� �߰�
			stockTableView.getColumns().addAll(colNo, colst_pnum, colst_pname, colst_pkind, colst_size, colst_color,
					colst_price, colst_count, colst_totalPrice, colst_filename);

			/*****************************************************************
			 * �Է¹��� �Ѽ����� �ؽ�Ʈ�ʵ�� ���� (�ӽ���)
			 *****************************************************************/
			StockVO svo1 = new StockVO();
			StockDAO sDao1 = new StockDAO();

			totalCountData = sDao1.getStockTotalCount(svo1).getSt_stockTotalCount();
			totalPriceData = sDao1.getStockTotalCount(svo1).getSt_stockTotalPrice();

			txtStockTotalCount.setText(totalCountData + "");
			txtStockTotalPrice.setText(totalPriceData + "");

			// ��ǰ ��ü ���� �Է�
			totalList();

			// �Է¹��� ���� data���� ���̺��� ����
			stockTableView.setItems(data);

			// ���̺�� ��ǰ ���ý� �̹����� ȭ�鿡 ����, ���� ���� ��ư Ȱ��ȭ ����
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

						// ���� ������ư Ȱ��ȭ
						btnEditGoods.setDisable(false);
						btnDeleteGoods.setDisable(false);

						btnOrderSalesRate.setDisable(false);

					} catch (Exception e1) {

					}
				}

			});

			// �ֹ�/�Ǹź��� Ȯ�� ��ư ��ɼ���(����Ʈ)
			btnOrderSalesRate.setOnAction(ev -> {
				try {
					/***********************
					 * ����Ʈ ���â ȭ�� ���� �ڵ�
					 *********************/
					FXMLLoader barchartloader = new FXMLLoader();
					barchartloader.setLocation(getClass().getResource("/View/stockbarchart.fxml"));

					Stage barchartDialog = new Stage(StageStyle.DECORATED);
					barchartDialog.initModality(Modality.WINDOW_MODAL);
					barchartDialog.initOwner(btnOrderSalesRate.getScene().getWindow());
					barchartDialog.setTitle("�ֹ�/�Ǹ� ���� �� ��Ʈ");

					Parent parentbarchart = (Parent) barchartloader.load();
					Scene barchartScene = new Scene(parentbarchart);
					barchartDialog.setScene(barchartScene);
					barchartDialog.setResizable(false);
					barchartDialog.show();

					/**********************
					 * ���â ȭ�� ��ư ����
					 **********************/

					BarChart stockbarchart = (BarChart) parentbarchart.lookup("#stockbarchart");
					Button btnbarchartexit = (Button) parentbarchart.lookup("#btnbarchartexit");

					/*********************
					 * ��ư ��� ����
					 *********************/

					// ����Ʈ ������ �Է¼���
					XYChart.Series seriesOrder = new XYChart.Series();
					seriesOrder.setName("�� �ֹ�����");

					ObservableList orderCountList = FXCollections.observableArrayList();

					selectStock = stockTableView.getSelectionModel().getSelectedItems();

					orderCountList.add(new XYChart.Data(selectStock.get(0).getSt_pnum(),
							sDao1.countTotalOrder(selectStock.get(0).getSt_pnum())));

					seriesOrder.setData(orderCountList);
					stockbarchart.getData().add(seriesOrder);

					XYChart.Series seriesSales = new XYChart.Series();
					seriesSales.setName("�� �Ǹż���");

					ObservableList salesCountList = FXCollections.observableArrayList();

					selectStock = stockTableView.getSelectionModel().getSelectedItems();

					salesCountList.add(new XYChart.Data(selectStock.get(0).getSt_pnum(),
							sDao1.countTotalSales(selectStock.get(0).getSt_pnum())));

					seriesSales.setData(salesCountList);
					stockbarchart.getData().add(seriesSales);

					// ����Ʈ ���â ���� ��ư ����
					btnbarchartexit.setOnAction(e -> {
						barchartDialog.close();
					});

				} catch (Exception e) {

					System.out.println("����" + e);

				}
			});

			// ��� ���� Ȯ�� ��ư ��ɼ��� (������Ʈ)
			btnStockRate.setOnAction(ev -> {
				try {
					/**********************
					 * ������Ʈ ���â ȭ����� �ڵ�
					 **********************/
					FXMLLoader piechartloader = new FXMLLoader();
					piechartloader.setLocation(getClass().getResource("/View/stockpiechart.fxml"));

					Stage piechartDialog = new Stage(StageStyle.DECORATED);
					piechartDialog.initModality(Modality.WINDOW_MODAL);
					piechartDialog.initOwner(btnStockRate.getScene().getWindow());
					piechartDialog.setTitle("�� ��� ���� ���� ��Ʈ");

					Parent parentpiechart = (Parent) piechartloader.load();
					Scene piechartScene = new Scene(parentpiechart);
					piechartDialog.setScene(piechartScene);
					piechartDialog.setResizable(false);
					piechartDialog.show();

					/*********************
					 * ������Ʈ ���â ��ư ����
					 *****************/

					PieChart stockpiechart = (PieChart) parentpiechart.lookup("#stockpiechart");

					Button btnpiechartexit = (Button) parentpiechart.lookup("#btnpiechartexit");

					/**********************
					 * ��ɼ���
					 ********************/

					// ������Ʈ ��ɼ��� (������ ����)
					ObservableList<PieChart.Data> piechartData = FXCollections.observableArrayList();

					for (int i = 0; i < sDao1.countStockList().size(); i++) {

						piechartData.add(new PieChart.Data(sDao1.countStockList().get(i).getSt_pnum(),
								sDao1.countStockList().get(i).getSt_count()));

					}

					stockpiechart.setData(piechartData);

					// �����ư ��ɼ���
					btnpiechartexit.setOnAction(e -> piechartDialog.close());

				} catch (Exception e) {

				}

			});

			// ǰ������ ��ȸ ��ư ��ɼ���
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
						alert.setTitle("��ǰ ���� ǰ�� ��ȸ");
						alert.setHeaderText("��ǰ�� ǰ���� �Է��Ͻÿ�");
						alert.setContentText("������ �����ϼ���");
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

						// ��ȸ�� ������� �� ������ �Ѿ� ����
						totalCountData = sDao.getStockPnumTotalCount(searchPnum).getSt_stockTotalCount();
						totalPriceData = sDao.getStockPnumTotalCount(searchPnum).getSt_stockTotalPrice();

						txtStockTotalCount.setText(totalCountData + "");
						txtStockTotalPrice.setText(totalPriceData + "");

					}

					if (!searchResult) {
						txtStockPnumSearch.clear();

						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("��ǰ ���� ǰ�� ��ȸ");
						alert.setHeaderText(searchPnum + " ���� ��ǰ�� ����Ʈ�� �����ϴ�.");
						alert.setContentText("�ٽ� �˻��ϼ���");
						alert.showAndWait();
					}

				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("��ǰ ���� ǰ�� ��ȸ");
					alert.setHeaderText("��ǰ ���� �˻��� ������ �߻��Ͽ����ϴ�.");
					alert.setContentText("�ٽ��ϼ���");
					alert.showAndWait();

				}
			});

			// ǰ������ ��ȸ ��ư ��ɼ���
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
						alert.setTitle("��ǰ ���� ǰ�� ��ȸ");
						alert.setHeaderText("��ǰ�� ǰ���� �Է��Ͻÿ�");
						alert.setContentText("������ �����ϼ���");
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

						// ��ȸ�� ������� �� ������ �Ѿ� ����
						totalCountData = sDao.getStockPkindTotalCount(searchPkind).getSt_stockTotalCount();
						totalPriceData = sDao.getStockPkindTotalCount(searchPkind).getSt_stockTotalPrice();

						txtStockTotalCount.setText(totalCountData + "");
						txtStockTotalPrice.setText(totalPriceData + "");
					}

					if (!searchResult) {
						txtStockPkindSearch.clear();

						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("��ǰ ���� ǰ�� ��ȸ");
						alert.setHeaderText(searchPkind + " �� ������ ��ǰ�� ����Ʈ�� �����ϴ�.");
						alert.setContentText("�ٽ� �˻��ϼ���");
						alert.showAndWait();
					}

				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("��ǰ ���� ǰ�� ��ȸ");
					alert.setHeaderText("��ǰ ���� �˻��� ������ �߻��Ͽ����ϴ�.");
					alert.setContentText("�ٽ��ϼ���");
					alert.showAndWait();

				}
			});

			// ��� ��ü ����Ʈ/ �ʱ�ȭ ��ư ��ɼ���
			btnStockTotalSearch.setOnAction(e -> {
				try {

					data.removeAll(data);
					totalList();
					localUrl = "/image/default.gif";
					localImage = new Image(localUrl, false);

					stockImageView.setFitWidth(300);
					stockImageView.setFitHeight(300);
					stockImageView.setImage(localImage);

					// �ѷ� , �Ѿ� �ؽ�Ʈ�ʵ� ǥ��
					totalCountData = sDao1.getStockTotalCount(svo1).getSt_stockTotalCount();
					totalPriceData = sDao1.getStockTotalCount(svo1).getSt_stockTotalPrice();

					txtStockTotalCount.setText(totalCountData + "");
					txtStockTotalPrice.setText(totalPriceData + "");

					// ���� ���� ��ư ���Ұ����¼���
					btnEditGoods.setDisable(true);
					btnDeleteGoods.setDisable(true);
					btnOrderSalesRate.setDisable(true);

				} catch (Exception e1) {

				}
			});

			// ���â ���� ��ư �̺�Ʈ ó��
			btnEditGoods.setOnAction(e -> {

				try {
					/**************************
					 * ��� ���� ȭ�� ����
					 ***********************/
					FXMLLoader editLoader = new FXMLLoader();
					editLoader.setLocation(getClass().getResource("/View/editGoods.fxml"));

					Stage editDialog = new Stage(StageStyle.DECORATED);
					editDialog.initModality(Modality.WINDOW_MODAL);
					editDialog.initOwner(btnEditGoods.getScene().getWindow());
					editDialog.setTitle("��ǰ ���� ����");

					Parent parentEdit = (Parent) editLoader.load();

					Scene editScene = new Scene(parentEdit);
					editDialog.setScene(editScene);
					editDialog.setResizable(false);
					editDialog.show();

					/*******************************
					 * ����â ��ư, �ؽ�Ʈ�ʵ� ����
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
					 * ����â ��ư ��ɼ���
					 ***************************/
					// ���� ó���� ���� ���õ� ��ǰ������ �ε��� ����
					StockVO stockEdit = stockTableView.getSelectionModel().getSelectedItem();
					selectedIndex = stockTableView.getSelectionModel().getSelectedIndex();

					txtStockEditPnum.setText(stockEdit.getSt_pnum());
					txtStockEditPname.setText(stockEdit.getSt_pname());
					txtStockEditPkind.setText(stockEdit.getSt_pkind());
					txtStockEditSize.setText(stockEdit.getSt_size());
					txtStockEditColor.setText(stockEdit.getSt_color());
					txtStockEditPrice.setText(stockEdit.getSt_price() + "");

					// ����â ���� ��ư ���
					btnStockEditOk.setOnAction(e1 -> {

						try {

							StockVO sVo = null;
							StockDAO sDao = null;

							if (txtStockEditPnum.getText().equals("") || txtStockEditPname.getText().equals("")
									|| txtStockEditPkind.getText().equals("") || txtStockEditSize.getText().equals("")
									|| txtStockEditColor.getText().equals("")
									|| txtStockEditPrice.getText().equals("")) {

								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("��ǰ ���� ����");
								alert.setHeaderText("��ǰ ������ �Է��ϼ���");
								alert.setContentText("��ǰ ������ �����Ǿ����ϴ�.");
								alert.showAndWait();

							} else if (Integer.parseInt(txtStockEditPrice.getText()) <= 0) {

								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("��ǰ ���� ����");
								alert.setHeaderText("��ǰ �ܰ��� Ȯ���ϼ���");
								alert.setContentText("��ǰ�ܰ��� 0�� Ȥ�� - �� �ɼ� �����ϴ�.");
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

									// �ѷ� , �Ѿ� �ؽ�Ʈ�ʵ� ǥ��
									totalCountData = sDao1.getStockTotalCount(svo1).getSt_stockTotalCount();
									totalPriceData = sDao1.getStockTotalCount(svo1).getSt_stockTotalPrice();

									txtStockTotalCount.setText(totalCountData + "");
									txtStockTotalPrice.setText(totalPriceData + "");

								} catch (Exception e4) {

								}

								editDialog.close();

								// �⺻�̹��� ǥ��
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

								// �ѷ� , �Ѿ� �ؽ�Ʈ�ʵ� ǥ��
								totalCountData = sDao1.getStockTotalCount(svo1).getSt_stockTotalCount();
								totalPriceData = sDao1.getStockTotalCount(svo1).getSt_stockTotalPrice();

								txtStockTotalCount.setText(totalCountData + "");
								txtStockTotalPrice.setText(totalPriceData + "");

							} catch (Exception e4) {

							}

							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("���� ���� �Է¿���");
							alert.setHeaderText("������ ������ Ȯ���� �ٽ� �õ����ּ���");
							alert.setContentText("�ܰ��� ���ڸ� ���԰����մϴ�");
							alert.showAndWait();

						}

					});

					// ����â ��� ��ư ���
					btnStockEditExit.setOnAction(e1 -> {
						editDialog.close();
					});

				} catch (IOException e1) {
					System.out.println("��� ����â ���� ���� " + e1);

				}

			});

			// ���â ���� ��ư �̺�Ʈ ó��
			btnDeleteGoods.setOnAction(e -> {
				StockDAO sDao = null;
				sDao = new StockDAO();

				try {
					// ���� ��ư ������ �˶��޼��� �˾�����
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("�����޼���");
					alert.setHeaderText("������ �����ʹ� �����ϽǼ� �����ϴ�.");
					alert.setContentText("������ �����Ͻðڽ��ϱ�?");

					Optional<ButtonType> result = alert.showAndWait();

					if (result.get() == ButtonType.OK) {
						sDao.getStockDelete(no);
						data.removeAll(data);

						// �л� ��ü����
						totalList();

						// �̹��� ���� ����
						stockImageDelete(selectFileName);

						// �⺻�̹��� ǥ��
						localUrl = "/image/default.gif";
						localImage = new Image(localUrl, false);
						stockImageView.setFitWidth(300);
						stockImageView.setFitHeight(300);
						stockImageView.setImage(localImage);

						// �� ����, �Ѿ� ����
						totalCountData = sDao1.getStockTotalCount(svo1).getSt_stockTotalCount();
						totalPriceData = sDao1.getStockTotalCount(svo1).getSt_stockTotalPrice();

						txtStockTotalCount.setText(totalCountData + "");
						txtStockTotalPrice.setText(totalPriceData + "");

					} else {

					}
				} catch (Exception e1) {
					System.out.println("������ư ���� ����" + e1);
				}

			});

			// ���â ���� ��ư �̺�Ʈó��
			btnStockExit.setOnAction(e -> {
				dialog.close();
			});

		} catch (

		Exception e) {
			System.out.println("��� ���â ���� ����!!!!!\n" + e);

		}
	}

	/***************************************
	 * �̹��� ���� �޼ҵ�
	 * 
	 * @param (������
	 *            ���ϸ�)
	 * @return ���� ���θ� ����
	 ****************************************/
	public boolean stockImageDelete(String fileName) {
		boolean result = false;

		try {
			// ������ �̹��� ����
			File stockFileDelete = new File(dirSave.getAbsolutePath() + "\\" + fileName);
			if (stockFileDelete.exists() && stockFileDelete.isFile()) {

				result = stockFileDelete.delete();

			}

		} catch (Exception ie) {
			System.out.println("���� �޼ҵ� ����" + ie);
			result = false;
		}

		return result;

	}

	// ��ǰ ��ü ����Ʈ �޼ҵ�
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

	// �̹������� �޼ҵ�
	public String imageSave(File file) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		int data = -1;
		String fileName = null;
		try {
			// �̹��� ���ϸ� ����
			fileName = "Stock" + System.currentTimeMillis() + "_" + file.getName();
			bis = new BufferedInputStream(new FileInputStream(file));
			bos = new BufferedOutputStream(new FileOutputStream(dirSave.getAbsolutePath() + "\\" + fileName));

			// ������ �̹������� InputStream �� �������� �̸��������� -1
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

	// �ֹ�â ���̾�α� ����
	public void handleBtnOrderAction(ActionEvent event) {

		data.removeAll(data);
		orderData.removeAll(orderData);
		wareData.removeAll(wareData);

		try {

			/************************************
			 * �ֹ�/ �԰� ȭ�� ����
			 ****************************/
			FXMLLoader orderLoader = new FXMLLoader();
			orderLoader.setLocation(getClass().getResource("/View/order.fxml"));

			Stage orderdialog = new Stage(StageStyle.DECORATED);
			orderdialog.initModality(Modality.WINDOW_MODAL);
			orderdialog.initOwner(btnOrder.getScene().getWindow());
			orderdialog.setTitle("�ֹ� / �԰� ����");

			Parent parentOrder = (Parent) orderLoader.load();
			Scene scene = new Scene(parentOrder);
			orderdialog.setScene(scene);
			orderdialog.setResizable(false);
			orderdialog.show();

			/***********************************
			 * �ֹ� ȭ�� ��ư, �ؽ�Ʈ�ʵ�, ��ũ�ѹ� ����
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
			// �ֹ��� ��ǰ��ȣ �˻� �޺��ڽ� ����
			ComboBox<String> cbOrderSearch = (ComboBox) parentOrder.lookup("#cbOrderSearch");
			cbOrderSearch.setItems(FXCollections.observableArrayList(sDao1.choiceStockPnum()));

			// �ֹ���� �⵵ �޺��ڽ� ����, ���ð��� ������ �Է�
			ComboBox<String> cbOrderListYear = (ComboBox) parentOrder.lookup("#cbOrderListYear");
			cbOrderListYear.setItems(FXCollections.observableArrayList("2015", "2016", "2017"));

			// �ֹ���� �� �޺��ڽ� ����, ���ð��� ������ �Է�
			ComboBox<String> cbOrderListMonth = (ComboBox) parentOrder.lookup("#cbOrderListMonth");
			cbOrderListMonth.setItems(FXCollections.observableArrayList("01", "02", "03", "04", "05", "06", "07", "08",
					"09", "10", "11", "12"));

			// �԰��� �⵵ �޺��ڽ� ����, ���ð��� ������ �Է�
			ComboBox<String> cbWarehousingListYear = (ComboBox) parentOrder.lookup("#cbWarehousingListYear");
			cbWarehousingListYear.setItems(FXCollections.observableArrayList("2015", "2016", "2017"));

			// �԰��� �� �޺��ڽ� ����, ���ð��� ������ �Է�
			ComboBox<String> cbWarehousingListMonth = (ComboBox) parentOrder.lookup("#cbWarehousingListMonth");
			cbWarehousingListMonth.setItems(FXCollections.observableArrayList("01", "02", "03", "04", "05", "06", "07",
					"08", "09", "10", "11", "12"));

			/*************************************
			 * ���̺�� Į�� ����
			 ************************************/

			// �ֹ� ��ǰ �˻�â ���̺�� �÷�����

			TableColumn colst_pnum = new TableColumn("ǰ��");
			colst_pnum.setMinWidth(110);
			colst_pnum.setStyle("-fx-alignment:CENTER");
			colst_pnum.setCellValueFactory(new PropertyValueFactory<>("st_pnum"));

			TableColumn colst_pname = new TableColumn("ǰ��");
			colst_pname.setMinWidth(130);
			colst_pname.setStyle("-fx-alignment:CENTER");
			colst_pname.setCellValueFactory(new PropertyValueFactory<>("st_pname"));

			TableColumn colst_pkind = new TableColumn("ǰ��");
			colst_pkind.setMinWidth(80);
			colst_pkind.setStyle("-fx-alignment:CENTER");
			colst_pkind.setCellValueFactory(new PropertyValueFactory<>("st_pkind"));

			TableColumn colst_size = new TableColumn("������");
			colst_size.setMinWidth(70);
			colst_size.setStyle("-fx-alignment:CENTER");
			colst_size.setCellValueFactory(new PropertyValueFactory<>("st_size"));

			TableColumn colst_color = new TableColumn("�÷�");
			colst_color.setMinWidth(50);
			colst_color.setStyle("-fx-alignment:CENTER");
			colst_color.setCellValueFactory(new PropertyValueFactory<>("st_color"));

			TableColumn colst_price = new TableColumn("�ܰ�");
			colst_price.setMinWidth(70);
			colst_price.setStyle("-fx-alignment:CENTER");
			colst_price.setCellValueFactory(new PropertyValueFactory<>("st_price"));

			TableColumn colst_count = new TableColumn("������");
			colst_count.setMinWidth(40);
			colst_count.setStyle("-fx-alignment:CENTER");
			colst_count.setCellValueFactory(new PropertyValueFactory<>("st_count"));

			TableColumn colst_filename = new TableColumn("���ϸ�");
			colst_filename.setMinWidth(200);
			colst_filename.setCellValueFactory(new PropertyValueFactory<>("st_filename"));

			OrderSearchTableView.getColumns().addAll(colst_pnum, colst_pname, colst_pkind, colst_size, colst_color,
					colst_price, colst_count, colst_filename);

			OrderSearchTableView.setItems(data);

			// �ֹ� ��� ���̺� ����

			/*
			 * TableColumn colOrderNo = new TableColumn("NO"); colOrderNo.setMinWidth(40);
			 * colOrderNo.setStyle("-fx-alignment:CENTER");
			 * colOrderNo.setCellValueFactory(new PropertyValueFactory<>("no"));
			 */

			TableColumn colo_num = new TableColumn("�ֹ���ȣ");
			colo_num.setMinWidth(100);
			colo_num.setStyle("-fx-alignment:CENTER");
			colo_num.setCellValueFactory(new PropertyValueFactory<>("o_num"));

			TableColumn colo_pnum = new TableColumn("�ֹ�ǰ��");
			colo_pnum.setMinWidth(120);
			colo_pnum.setStyle("-fx-alignment:CENTER");
			colo_pnum.setCellValueFactory(new PropertyValueFactory<>("o_pnum"));

			TableColumn colo_pname = new TableColumn("�ֹ�ǰ��");
			colo_pname.setMinWidth(140);
			colo_pname.setStyle("-fx-alignment:CENTER");
			colo_pname.setCellValueFactory(new PropertyValueFactory<>("o_pname"));

			TableColumn colo_pkind = new TableColumn("�ֹ�ǰ��");
			colo_pkind.setMinWidth(80);
			colo_pkind.setStyle("-fx-alignment:CENTER");
			colo_pkind.setCellValueFactory(new PropertyValueFactory<>("o_pkind"));

			TableColumn colo_size = new TableColumn("������");
			colo_size.setMinWidth(100);
			colo_size.setStyle("-fx-alignment:CENTER");
			colo_size.setCellValueFactory(new PropertyValueFactory<>("o_size"));

			TableColumn colo_color = new TableColumn("�÷�");
			colo_color.setMinWidth(60);
			colo_color.setStyle("-fx-alignment:CENTER");
			colo_color.setCellValueFactory(new PropertyValueFactory<>("o_color"));

			TableColumn colo_price = new TableColumn("�ܰ�");
			colo_price.setMinWidth(80);
			colo_price.setStyle("-fx-alignment:CENTER");
			colo_price.setCellValueFactory(new PropertyValueFactory<>("o_price"));

			TableColumn colo_count = new TableColumn("�ֹ�����");
			colo_count.setMinWidth(60);
			colo_count.setStyle("-fx-alignment:CENTER");
			colo_count.setCellValueFactory(new PropertyValueFactory<>("o_count"));

			TableColumn colo_totalPrice = new TableColumn("�ѱݾ�");
			colo_totalPrice.setMinWidth(100);
			colo_totalPrice.setStyle("-fx-alignment:CENTER");
			colo_totalPrice.setCellValueFactory(new PropertyValueFactory<>("o_totalPrice"));

			TableColumn colo_date = new TableColumn("�ֹ���¥");
			colo_date.setMinWidth(100);
			colo_date.setStyle("-fx-alignment:CENTER");
			colo_date.setCellValueFactory(new PropertyValueFactory<>("o_date"));

			TableColumn colo_filename = new TableColumn("���ϸ�");
			colo_filename.setMinWidth(200);
			colo_filename.setCellValueFactory(new PropertyValueFactory<>("o_fileName"));

			// ���̺�信 �� �߰�
			OrderListTableView.getColumns().addAll(colo_date, colo_num, colo_pnum, colo_pname, colo_count, colo_pkind,
					colo_size, colo_color, colo_price, colo_totalPrice, colo_filename);

			OrderListTableView.setItems(orderData);

			// �԰� ��� ���̺� ����

			/*
			 * TableColumn colOrderNo = new TableColumn("NO"); colOrderNo.setMinWidth(40);
			 * colOrderNo.setStyle("-fx-alignment:CENTER");
			 * colOrderNo.setCellValueFactory(new PropertyValueFactory<>("no"));
			 */

			TableColumn colw_num = new TableColumn("�԰��ȣ");
			colw_num.setMinWidth(100);
			colw_num.setStyle("-fx-alignment:CENTER");
			colw_num.setCellValueFactory(new PropertyValueFactory<>("w_num"));

			TableColumn colw_pnum = new TableColumn("�԰�ǰ��");
			colw_pnum.setMinWidth(120);
			colw_pnum.setStyle("-fx-alignment:CENTER");
			colw_pnum.setCellValueFactory(new PropertyValueFactory<>("w_pnum"));

			TableColumn colw_pname = new TableColumn("�԰�ǰ��");
			colw_pname.setMinWidth(140);
			colw_pname.setStyle("-fx-alignment:CENTER");
			colw_pname.setCellValueFactory(new PropertyValueFactory<>("w_pname"));

			TableColumn colw_pkind = new TableColumn("�԰�ǰ��");
			colw_pkind.setMinWidth(80);
			colw_pkind.setStyle("-fx-alignment:CENTER");
			colw_pkind.setCellValueFactory(new PropertyValueFactory<>("w_pkind"));

			TableColumn colw_size = new TableColumn("������");
			colw_size.setMinWidth(100);
			colw_size.setStyle("-fx-alignment:CENTER");
			colw_size.setCellValueFactory(new PropertyValueFactory<>("w_size"));

			TableColumn colw_color = new TableColumn("�÷�");
			colw_color.setMinWidth(60);
			colw_color.setStyle("-fx-alignment:CENTER");
			colw_color.setCellValueFactory(new PropertyValueFactory<>("w_color"));

			TableColumn colw_price = new TableColumn("�ܰ�");
			colw_price.setMinWidth(80);
			colw_price.setStyle("-fx-alignment:CENTER");
			colw_price.setCellValueFactory(new PropertyValueFactory<>("w_price"));

			TableColumn colw_count = new TableColumn("�԰����");
			colw_count.setMinWidth(60);
			colw_count.setStyle("-fx-alignment:CENTER");
			colw_count.setCellValueFactory(new PropertyValueFactory<>("w_count"));

			TableColumn colw_totalPrice = new TableColumn("�ѱݾ�");
			colw_totalPrice.setMinWidth(100);
			colw_totalPrice.setStyle("-fx-alignment:CENTER");
			colw_totalPrice.setCellValueFactory(new PropertyValueFactory<>("w_totalPrice"));

			TableColumn colw_date = new TableColumn("�԰�¥");
			colw_date.setMinWidth(100);
			colw_date.setStyle("-fx-alignment:CENTER");
			colw_date.setCellValueFactory(new PropertyValueFactory<>("w_date"));

			TableColumn colw_filename = new TableColumn("���ϸ�");
			colw_filename.setMinWidth(200);
			colw_filename.setCellValueFactory(new PropertyValueFactory<>("w_fileName"));

			// ���̺�信 �� �߰�
			WarehousingListTableView.getColumns().addAll(colw_date, colw_num, colw_pnum, colw_pname, colw_count,
					colw_pkind, colw_size, colw_color, colw_price, colw_totalPrice, colw_filename);

			WarehousingListTableView.setItems(wareData);

			/***********************************
			 * ��ɼ���
			 **********************************/
			/*
			 * // �˻��ؽ�Ʈ �ʵ� Ŭ���� �ʱ�ȭ ���� txtOrderSearch.setOnMouseClicked(e -> {
			 * 
			 * // ����Ʈ �̹��� ���� localUrl = "/image/default.gif"; localImage = new
			 * Image(localUrl, false); OrderImageView.setFitWidth(300);
			 * OrderImageView.setFitHeight(300); OrderImageView.setImage(localImage);
			 * 
			 * // ���� ���� ��ư ��Ȱ��ȭ btnOrderCount.setDisable(true);
			 * 
			 * // �ֹ���� ���� ���� , �԰��� ��ư ��Ȱ��ȭ btnOrderEdit.setDisable(true);
			 * btnOrderDelete.setDisable(true); btnWarehousing.setDisable(true);
			 * btnWarehousingEdit.setDisable(true);
			 * 
			 * });
			 */

			// �ʱ� �̹��� ���� , �ʱ� ���̺�����
			localUrl = "/image/default.gif"; // ����Ʈ �̹��� ����
			localImage = new Image(localUrl, false);
			OrderImageView.setFitWidth(300);
			OrderImageView.setFitHeight(300);
			OrderImageView.setImage(localImage);

			// �ʱ� ȭ�� �ֹ� ���, �԰��� ��ü ����Ʈ ���
			orderListTotalList();
			warehousingTotalList();

			OrderVO oVo1 = new OrderVO();
			OrderDAO oDao1 = new OrderDAO();
			WarehousingVO wVo1 = new WarehousingVO();
			WarehousingDAO wDao1 = new WarehousingDAO();

			// �ֹ� �� ����, ���հ�
			totalOrderCountData = oDao1.getStockTotalCount(oVo1).getO_orderTotalCount();
			totalOrderPriceData = oDao1.getStockTotalCount(oVo1).getO_orderTotalPrice();

			txtOrderListTotalCount.setText(totalOrderCountData + "");
			txtOrderListTotalPrice.setText(totalOrderPriceData + "");

			// �԰� �� ����, ���հ�
			totalWareCountData = wDao1.getWareTotalCount(wVo1).getW_orderTotalCount();
			totalWarePriceData = wDao1.getWareTotalCount(wVo1).getW_orderTotalPrice();

			txtWarehousingListTotalCount.setText(totalWareCountData + "");
			txtWarehousingListTotalPrice.setText(totalWarePriceData + "");

			// �������� �ʱ� ��Ȱ��ȭ
			btnOrderCount.setDisable(true);

			// �ֹ���� �԰��� ����, pdf���� ����� �ʱ� ��Ȱ��ȭ
			btnOrderExcel.setDisable(true);
			btnOrderPDF.setDisable(true);
			btnWareExcel.setDisable(true);
			btnWarePDF.setDisable(true);

			// �ֹ���� ���� ���� , �԰���,���� ��ư �ʱ� ��Ȱ��ȭ
			btnOrderEdit.setDisable(true);
			btnOrderDelete.setDisable(true);
			btnWarehousing.setDisable(true);
			btnWarehousingEdit.setDisable(true);

			// �˻� �ʱ�ȭ ��ư ��ɼ���
			btnOrderSearchClear.setOnAction(e -> {
				data.removeAll(data);
				// txtOrderSearch.clear();
				btnOrderCount.setDisable(true);
				cbOrderSearch.getSelectionModel().clearSelection();

				localUrl = "/image/default.gif"; // ����Ʈ �̹��� ����
				localImage = new Image(localUrl, false);
				OrderImageView.setFitWidth(300);
				OrderImageView.setFitHeight(300);
				OrderImageView.setImage(localImage);
			});

			// ���� ���� ���� ����(�ֹ� ���)
			btnOrderSaveFileDir.setOnAction(e -> {
				final DirectoryChooser directoryChooser = new DirectoryChooser();
				final File selectedDirectory = directoryChooser.showDialog(primaryStage);

				if (selectedDirectory != null) {
					txtOrderSaveFileDir.setText(selectedDirectory.getAbsolutePath());
					btnOrderExcel.setDisable(false);
					btnOrderPDF.setDisable(false);

				}

			});

			// ���� ���� ���� ����(�԰� ���)
			btnWareSaveFileDir.setOnAction(e -> {
				final DirectoryChooser directoryChooser = new DirectoryChooser();
				final File selectedDirectory = directoryChooser.showDialog(primaryStage);

				if (selectedDirectory != null) {
					txtWareSaveFileDir.setText(selectedDirectory.getAbsolutePath());
					btnWareExcel.setDisable(false);
					btnWarePDF.setDisable(false);

				}

			});

			// ���� ���� ������ư (�ֹ����)
			btnOrderExcel.setOnAction(e -> {
				OrderDAO oDao8 = new OrderDAO();
				boolean saveSuccess;

				ArrayList<OrderVO> list;
				list = oDao8.getOrderTotal();
				OrderExcel excelWriter = new OrderExcel();

				// xlsx���� ����
				saveSuccess = excelWriter.xlsxOrderWiter(list, txtOrderSaveFileDir.getText());
				if (saveSuccess) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("���� ���� ����");
					alert.setHeaderText("�ֹ� ��� ���� ���� ���� ����");
					alert.setContentText("�ֹ� ��� ���� ����");
					alert.showAndWait();

				}

				txtOrderSaveFileDir.clear();
				btnOrderExcel.setDisable(true);
				btnOrderPDF.setDisable(true);

			});

			// ���� ���� ������ư (�԰���)
			btnWareExcel.setOnAction(e -> {
				WarehousingDAO wDao8 = new WarehousingDAO();
				boolean saveSuccess;

				ArrayList<WarehousingVO> list;
				list = wDao8.getWareTotal();
				WareExcel excelWriter = new WareExcel();

				// xlsx���� ����
				saveSuccess = excelWriter.xlsxWiter(list, txtWareSaveFileDir.getText());
				if (saveSuccess) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("���� ���� ����");
					alert.setHeaderText("�԰� ��� ���� ���� ���� ����");
					alert.setContentText("�԰� ��� ���� ����");
					alert.showAndWait();

				}

				txtWareSaveFileDir.clear();
				btnWareExcel.setDisable(true);
				btnWarePDF.setDisable(true);

			});

			// pdf���� ���� (�ֹ����)
			btnOrderPDF.setOnAction(e -> {
				try {
					// pdf document ����
					Document document = new Document(PageSize.A4, 0, 0, 30, 30);
					// pdf������ ������ ��������, pdf������ �����ǰ� ���� ��Ʈ������ �����
					String strReportPDFName = "order_" + System.currentTimeMillis() + ".pdf";
					PdfWriter.getInstance(document,
							new FileOutputStream(txtOrderSaveFileDir.getText() + "\\" + strReportPDFName));
					// document�� ���� pdf������ �ۼ��Ҽ� �ֵ����Ѵ�.
					document.open();
					// �ѱ� ���� ��Ʈ ����
					BaseFont bf = BaseFont.createFont("font/MALGUN.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
					Font font = new Font(bf, 8, Font.NORMAL);
					Font font2 = new Font(bf, 14, Font.BOLD);

					// Ÿ��Ʋ ����
					Paragraph title = new Paragraph("�ֹ� ���", font2);
					// ��� ����
					title.setAlignment(Element.ALIGN_CENTER);
					// ������ �߰�
					document.add(title);
					document.add(new Paragraph("\r\n"));

					// ���� ��¥
					LocalDate date = LocalDate.now();
					Paragraph writeDay = new Paragraph(date.toString(), font);

					// ������ ����
					writeDay.setAlignment(Element.ALIGN_RIGHT);
					// ������ �߰�
					document.add(writeDay);
					document.add(new Paragraph("\r\n"));

					// ���̺� ����, Table��ü ���� pdftable��ü�� �� �����ϰ� ���̺��� �����Ҽ� �ִ�.
					// ������ ���� �÷����� �����ش�
					PdfPTable table = new PdfPTable(12);
					// �÷��� ���� ���Ѵ�.
					table.setWidths(new int[] { 30, 70, 70, 70, 50, 50, 40, 60, 40, 60, 70, 100 });

					// �÷� Ÿ��Ʋ ����
					PdfPCell header1 = new PdfPCell(new Paragraph("��ȣ", font));
					PdfPCell header2 = new PdfPCell(new Paragraph("�ֹ���ȣ", font));
					PdfPCell header3 = new PdfPCell(new Paragraph("�ֹ�ǰ��", font));
					PdfPCell header4 = new PdfPCell(new Paragraph("�ֹ�ǰ��", font));
					PdfPCell header5 = new PdfPCell(new Paragraph("ǰ��", font));
					PdfPCell header6 = new PdfPCell(new Paragraph("������", font));
					PdfPCell header7 = new PdfPCell(new Paragraph("�÷�", font));
					PdfPCell header8 = new PdfPCell(new Paragraph("�ֹ��ܰ�", font));
					PdfPCell header9 = new PdfPCell(new Paragraph("�ֹ�����", font));
					PdfPCell header10 = new PdfPCell(new Paragraph("�ֹ��ݾ�", font));
					PdfPCell header11 = new PdfPCell(new Paragraph("�ֹ���¥", font));
					PdfPCell header12 = new PdfPCell(new Paragraph("�̹�����", font));

					// ��������
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

					// ��������
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

					// ���̺� �� �߰�
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

					// DB���� �� ����Ʈ ����
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

						// ���� ����
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

						// ���� ����
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

						// ���̺� �� �߰�
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
					// document �� ���̺� �߰�
					document.add(table);
					// �ݱ� ���� ����
					document.close();

					txtOrderSaveFileDir.clear();
					btnOrderPDF.setDisable(true);
					btnOrderExcel.setDisable(true);

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("PDF���� ����");
					alert.setHeaderText("�ֹ� ��� pdf���� ���� ����");
					alert.setContentText("�ֹ� ��� PDF����");
					alert.showAndWait();

				} catch (FileNotFoundException e0) {
					e0.printStackTrace();

				} catch (DocumentException e0) {
					e0.printStackTrace();
				} catch (IOException e0) {
					e0.printStackTrace();
				}
			});

			// PDF���� ���� (�԰� ���)
			btnWarePDF.setOnAction(e -> {

				try {
					// pdf document ����
					Document document = new Document(PageSize.A4, 0, 0, 30, 30);
					// pdf������ ������ ��������, pdf������ �����ǰ� ���� ��Ʈ������ �����
					String strReportPDFName = "warehousing_" + System.currentTimeMillis() + ".pdf";
					PdfWriter.getInstance(document,
							new FileOutputStream(txtWareSaveFileDir.getText() + "\\" + strReportPDFName));
					// document�� ���� pdf������ �ۼ��Ҽ� �ֵ����Ѵ�.
					document.open();
					// �ѱ� ���� ��Ʈ ����
					BaseFont bf = BaseFont.createFont("font/MALGUN.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
					Font font = new Font(bf, 8, Font.NORMAL);
					Font font2 = new Font(bf, 14, Font.BOLD);

					// Ÿ��Ʋ ����
					Paragraph title = new Paragraph("�԰� ���", font2);
					// ��� ����
					title.setAlignment(Element.ALIGN_CENTER);
					// ������ �߰�
					document.add(title);
					document.add(new Paragraph("\r\n"));

					// ���� ��¥
					LocalDate date = LocalDate.now();
					Paragraph writeDay = new Paragraph(date.toString(), font);

					// ������ ����
					writeDay.setAlignment(Element.ALIGN_RIGHT);
					// ������ �߰�
					document.add(writeDay);
					document.add(new Paragraph("\r\n"));

					// ���̺� ����, Table��ü ���� pdftable��ü�� �� �����ϰ� ���̺��� �����Ҽ� �ִ�.
					// ������ ���� �÷����� �����ش�
					PdfPTable table = new PdfPTable(12);
					// �÷��� ���� ���Ѵ�.
					table.setWidths(new int[] { 30, 70, 70, 70, 50, 50, 40, 60, 40, 60, 70, 100 });

					// �÷� Ÿ��Ʋ ����
					PdfPCell header1 = new PdfPCell(new Paragraph("��ȣ", font));
					PdfPCell header2 = new PdfPCell(new Paragraph("�԰��ȣ", font));
					PdfPCell header3 = new PdfPCell(new Paragraph("�԰�ǰ��", font));
					PdfPCell header4 = new PdfPCell(new Paragraph("�԰�ǰ��", font));
					PdfPCell header5 = new PdfPCell(new Paragraph("ǰ��", font));
					PdfPCell header6 = new PdfPCell(new Paragraph("������", font));
					PdfPCell header7 = new PdfPCell(new Paragraph("�÷�", font));
					PdfPCell header8 = new PdfPCell(new Paragraph("�԰�ܰ�", font));
					PdfPCell header9 = new PdfPCell(new Paragraph("�԰����", font));
					PdfPCell header10 = new PdfPCell(new Paragraph("�԰�ݾ�", font));
					PdfPCell header11 = new PdfPCell(new Paragraph("�԰�¥", font));
					PdfPCell header12 = new PdfPCell(new Paragraph("�̹�����", font));

					// ��������
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

					// ��������
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

					// ���̺� �� �߰�
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

					// DB���� �� ����Ʈ ����
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

						// ���� ����
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

						// ���� ����
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

						// ���̺� �� �߰�
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
					// document �� ���̺� �߰�
					document.add(table);
					// �ݱ� ���� ����
					document.close();

					txtWareSaveFileDir.clear();
					btnWarePDF.setDisable(true);
					btnWareExcel.setDisable(true);

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("PDF���� ����");
					alert.setHeaderText("�԰� ��� pdf���� ���� ����");
					alert.setContentText("�԰� ��� PDF����");
					alert.showAndWait();

				} catch (FileNotFoundException e0) {
					e0.printStackTrace();

				} catch (DocumentException e0) {
					e0.printStackTrace();
				} catch (IOException e0) {
					e0.printStackTrace();
				}

			});

			// ǰ������ ��ȸ ��ư ��ɼ���
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
						alert.setTitle("��ǰ ���� ǰ�� ��ȸ");
						alert.setHeaderText("��ǰ�� ǰ���� �����Ͻÿ�");
						alert.setContentText("������ �����ϼ���");
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
						alert.setTitle("��ǰ ���� ǰ�� ��ȸ");
						alert.setHeaderText("�̰� �㸮�� ���µ�? ��");
						alert.setContentText("�ٽ� �˻��ϼ���");
						alert.showAndWait();
					}

				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("��ǰ ���� ǰ�� ��ȸ");
					alert.setHeaderText("��ǰ ���� �˻��� ������ �߻��Ͽ����ϴ�.");
					alert.setContentText("�ٽ��ϼ���");
					alert.showAndWait();
					e1.printStackTrace();

				}

			});

			// �ֹ���� ��¥�� ��ȸ ��ư ��ɼ���
			btnOrderListSearch.setOnAction(e -> {
				OrderVO oVo = new OrderVO();
				OrderDAO oDao = null;

				Object[][] totalData = null;

				String orderListYear = null;
				String orderListMonth = null;
				boolean searchResult = false;

				try {
					// �޺��ڽ����� ������ �⵵�� �ҷ���
					orderListYear = cbOrderListYear.getSelectionModel().getSelectedItem();
					orderListMonth = cbOrderListMonth.getSelectionModel().getSelectedItem();

					oDao = new OrderDAO();

					// �ƹ��͵� ���� �������� ���â �˶�
					if (orderListYear == null && orderListMonth == null) {
						searchResult = true;

						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("�ֹ���� �⵵��,���� ��ȸ");
						alert.setHeaderText("�ֹ������ �˻��� �⵵�� ���� �����ϼ���");
						alert.setContentText("������ �����ϼ���");
						alert.showAndWait();

					}

					// �⵵�� �����ؼ� �˻������� �������
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

							// �⵵�� ��ȸ�� ������� �� ������ �Ѿ� ����
							totalOrderCountData = oDao.getOrderYearTotalCount(orderListYear).getO_orderTotalCount();
							totalOrderPriceData = oDao.getOrderYearTotalCount(orderListYear).getO_orderTotalPrice();

							txtOrderListTotalCount.setText(totalOrderCountData + "");
							txtOrderListTotalPrice.setText(totalOrderPriceData + "");

						} else {
							cbOrderListYear.getSelectionModel().clearSelection();

							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("�ֹ� �̷� ��ȸ");
							alert.setHeaderText(orderListYear + "�⵵�� �ֹ��� ����Ʈ�� �����ϴ�.");
							alert.setContentText("�ٽ� �˻��ϼ���");
							alert.showAndWait();

						}

					}

					// ���� �����ؼ� �˻������� �������
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

							// ���� ��ȸ�� ������� �� ������ �Ѿ� ����
							totalOrderCountData = oDao.getOrderMonthTotalCount(orderListMonth).getO_orderTotalCount();
							totalOrderPriceData = oDao.getOrderMonthTotalCount(orderListMonth).getO_orderTotalPrice();

							txtOrderListTotalCount.setText(totalOrderCountData + "");
							txtOrderListTotalPrice.setText(totalOrderPriceData + "");

						} else {

							cbOrderListMonth.getSelectionModel().clearSelection();

							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("�ֹ� �̷� ��ȸ");
							alert.setHeaderText(orderListMonth + "���� �ֹ��� ����Ʈ�� �����ϴ�.");
							alert.setContentText("�ٽ� �˻��ϼ���");
							alert.showAndWait();

						}

					}

					// �⵵�� ���� �Ѵ� �����ؼ� �˻������� �������
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

							// �� + ���� ��ȸ�� ������� �� ������ �Ѿ� ����
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
							alert.setTitle("�ֹ� �̷� ��ȸ");
							alert.setHeaderText(orderListYear + "�� " + orderListMonth + "���� �ֹ��� ����Ʈ�� �����ϴ�.");
							alert.setContentText("�ٽ� �˻��ϼ���");
							alert.showAndWait();

						}

					}

				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("�ֹ� �̷� �⵵ ��ȸ");
					alert.setHeaderText("�ֹ� �̷� �˻��� ������ �߻��Ͽ����ϴ�.");
					alert.setContentText("�ٽ��ϼ���");
					alert.showAndWait();
					System.out.println(e1);

				}

			});

			// �ֹ���� ��ü���� ��ư ��ɼ���
			btnOrderListTotal.setOnAction(e -> {

				orderData.removeAll(orderData);

				// �⺻�̹����� ����
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
					System.out.println("��ü���� �Ѽ���,�Ѿ� �ַ�" + e1);
				}

			});

			// �԰��� ��¥�� ��ȸ ��ư ��ɼ���
			btnWarehousingListSearch.setOnAction(e -> {
				WarehousingVO wVo = new WarehousingVO();
				WarehousingDAO wDao = null;

				Object[][] totalData = null;

				String wareListYear = null;
				String wareListMonth = null;
				boolean searchResult = false;

				try {
					// �޺��ڽ����� ������ �⵵�� �ҷ���
					wareListYear = cbWarehousingListYear.getSelectionModel().getSelectedItem();
					wareListMonth = cbWarehousingListMonth.getSelectionModel().getSelectedItem();

					wDao = new WarehousingDAO();

					// �ƹ��͵� ���� �������� ���â �˶�
					if (wareListYear == null && wareListMonth == null) {
						searchResult = true;

						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("�԰��� �⵵��,���� ��ȸ");
						alert.setHeaderText("�԰����� �˻��� �⵵�� ���� �����ϼ���");
						alert.setContentText("������ �����ϼ���");
						alert.showAndWait();

					}

					// �⵵�� �����ؼ� �˻������� �������
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

							// �⵵�� ��ȸ�� ������� �� ������ �Ѿ� ����
							totalWareCountData = wDao.getWareYearTotalCount(wareListYear).getW_orderTotalCount();
							totalWarePriceData = wDao.getWareYearTotalCount(wareListYear).getW_orderTotalPrice();

							txtWarehousingListTotalCount.setText(totalWareCountData + "");
							txtWarehousingListTotalPrice.setText(totalWarePriceData + "");

						} else {
							cbWarehousingListYear.getSelectionModel().clearSelection();

							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("�԰� �̷� ��ȸ");
							alert.setHeaderText(wareListYear + "�⵵�� �԰����� ����Ʈ�� �����ϴ�.");
							alert.setContentText("�ٽ� �˻��ϼ���");
							alert.showAndWait();

						}

					}

					// ���� �����ؼ� �˻������� �������
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

							// ���� ��ȸ�� ������� �� ������ �Ѿ� ����
							totalWareCountData = wDao.getWareMonthTotalCount(wareListMonth).getW_orderTotalCount();
							totalWarePriceData = wDao.getWareMonthTotalCount(wareListMonth).getW_orderTotalPrice();

							txtWarehousingListTotalCount.setText(totalWareCountData + "");
							txtWarehousingListTotalPrice.setText(totalWarePriceData + "");

						} else {

							cbWarehousingListMonth.getSelectionModel().clearSelection();

							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("�԰� �̷� ��ȸ");
							alert.setHeaderText(wareListMonth + "���� �԰� ����Ʈ�� �����ϴ�.");
							alert.setContentText("�ٽ� �˻��ϼ���");
							alert.showAndWait();

						}

					}

					// �⵵�� ���� �Ѵ� �����ؼ� �˻������� �������
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

							// �� + ���� ��ȸ�� ������� �� ������ �Ѿ� ����
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
							alert.setTitle("�԰� �̷� ��ȸ");
							alert.setHeaderText(wareListYear + "�� " + wareListMonth + "���� �԰� ����Ʈ�� �����ϴ�.");
							alert.setContentText("�ٽ� �˻��ϼ���");
							alert.showAndWait();

						}

					}

				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("�԰� �̷� �⵵ ��ȸ");
					alert.setHeaderText("�԰� �̷� �˻��� ������ �߻��Ͽ����ϴ�.");
					alert.setContentText("�ٽ��ϼ���");
					alert.showAndWait();
					System.out.println(e1);

				}

			});

			// �԰��� ��ü���� ��ư ��ɼ���
			btnWarehousingListTotal.setOnAction(e -> {

				wareData.removeAll(wareData);

				// �⺻�̹����� ����
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
					System.out.println("�԰��� ��ü���� �Ѽ���,�Ѿ� �ַ�" + e1);
				}

			});

			// �ֹ���� ��ǰ ���ý� �̹����� ȭ�鿡 ����, ���� ���� ��ư Ȱ��ȭ ����
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

						// �ֹ� ��� ���� ���� , �԰��� ��ư Ȱ��ȭ
						btnOrderEdit.setDisable(false);
						btnOrderDelete.setDisable(false);
						btnWarehousing.setDisable(false);

						// �ֹ� ���� ����, �԰� ���� ��ư ��Ȱ��ȭ
						btnOrderCount.setDisable(true);
						btnWarehousingEdit.setDisable(true);

					} catch (Exception e1) {

					}
				}

			});

			// �ֹ����̺� ��ǰ�˻� ���̺�� ��ǰ ���ý� �̹����� ȭ�鿡 ����, ���� ���� ��ư Ȱ��ȭ ����
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

						// �������� ��ư Ȱ��ȭ
						btnOrderCount.setDisable(false);

						// �ֹ� ��� ���� ���� , �԰��� ��ư ��Ȱ��ȭ
						btnOrderEdit.setDisable(true);
						btnOrderDelete.setDisable(true);
						btnWarehousing.setDisable(true);
						btnWarehousingEdit.setDisable(true);

					} catch (Exception e1) {

					}
				}

			});

			// �԰��� ���̺�� ��ǰ ���ý� �̹����� ȭ�鿡 ����, ���� ���� ��ư ��Ȱ��ȭ ����
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

						// �������� ��ư Ȱ��ȭ
						btnOrderCount.setDisable(true);

						// �ֹ� ��� ���� ���� , �԰��� ��ư ��Ȱ��ȭ
						btnOrderEdit.setDisable(true);
						btnOrderDelete.setDisable(true);
						btnWarehousing.setDisable(true);

						// �԰��� ���� ��ư Ȱ��ȭ
						btnWarehousingEdit.setDisable(false);

					} catch (Exception e1) {

					}
				}

			});

			// �ֹ� ���� ���� ��ư ��ɼ���
			btnOrderCount.setOnAction(e -> {
				try {

					/*******************************
					 * �ֹ� ���� ���� �˾� ����
					 ************************/
					FXMLLoader orderCountLoader = new FXMLLoader();
					orderCountLoader.setLocation(getClass().getResource("/View/orderCount.fxml"));

					Stage orderCountdialog = new Stage(StageStyle.DECORATED);
					orderCountdialog.initModality(Modality.WINDOW_MODAL);
					orderCountdialog.initOwner(btnOrder.getScene().getWindow());
					orderCountdialog.setTitle("�ֹ� ���� ����");

					Parent parentOrderCount = (Parent) orderCountLoader.load();
					Scene countScene = new Scene(parentOrderCount);
					orderCountdialog.setScene(countScene);
					orderCountdialog.setResizable(false);
					orderCountdialog.show();

					/*******************************
					 * �ֹ� �������� �˾� �ؽ�Ʈ�ʵ� ��ư ����
					 *****************************/

					TextField txtOrderCount = (TextField) parentOrderCount.lookup("#txtOrderCount");
					Button btnOrderCountOK = (Button) parentOrderCount.lookup("#btnOrderCountOK");
					Button btnOrderCountExit = (Button) parentOrderCount.lookup("#btnOrderCountExit");

					/****************************
					 * ��ư ��ɼ���
					 **************************/
					// ��� ��ư ��ɼ���
					btnOrderCountOK.setOnAction(ev -> {
						try {
							orderData.removeAll(orderData);
							StockVO sVo = null;
							OrderDAO oDao = new OrderDAO();
							/*
							 * File dirMake = new File(dirSave.getAbsolutePath());
							 * 
							 * // �̹��� ���� if (!dirMake.exists()) { dirMake.mkdir(); }
							 * 
							 * // �̹��� ���� ���� String fileName = imageSave(selectedFile);
							 */

							// �ֹ� ���� ����
							if (ev.getSource().equals(btnOrderCountOK)) {

								if (Integer.parseInt(txtOrderCount.getText()) <= 0) {

									orderData.removeAll(orderData);
									orderListTotalList();

									Alert alert = new Alert(AlertType.WARNING);
									alert.setTitle("�ֹ� ���� �Է� ����");
									alert.setHeaderText("�ֹ� �� ������ ��Ȯ�� �Է��Ͻÿ�");
									alert.setContentText("�ֹ������� ���� Ȥ�� 0�� �Ұ����մϴ�.");
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
									alert.setTitle("�ֹ� ��� ���");
									alert.setHeaderText(selectOrder.get(0).getSt_pnum() + " ���� ��ǰ�� ���������� �ֹ��Ϸ� �Ǿ����ϴ�.");
									alert.setContentText("�ֹ� ��� �Է� ����");

									alert.showAndWait();

									data.removeAll(data);
									// txtOrderSearch.clear();
									btnOrderCount.setDisable(true);
									cbOrderSearch.getSelectionModel().clearSelection();

									localUrl = "/image/default.gif"; // ����Ʈ �̹��� ����
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
										System.out.println("�ֹ� ��� �� �Ѽ���,�Ѿ� �ַ�" + e1);
									}

									orderCountdialog.close();

								}
							}

						} catch (NumberFormatException ne) {

							orderData.removeAll(orderData);
							orderListTotalList();

							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("�ֹ� ���� �Է� ����");
							alert.setHeaderText("�ֹ� ������ ���ڷ� ��Ȯ�� �Է��Ͻÿ�");
							alert.setContentText("�ֹ� ������ ���� Ȥ�� ���ڷ� �ԷµǾ����ϴ�.");
							alert.showAndWait();

						} catch (Exception e3) {

							orderData.removeAll(orderData);
							orderListTotalList();

							e3.printStackTrace();

						}

					});

					// ��� ��ư ��ɼ���
					btnOrderCountExit.setOnAction(ev -> {
						orderCountdialog.close();
					});

				} catch (Exception e1) {
					System.out.println("�ֹ����� ���� �˾�â ���ÿ���" + e1);
				}

			});

			// �ֹ� ���� ���� ��ư ��ɼ���
			btnOrderEdit.setOnAction(e -> {
				try {
					WarehousingDAO wDao = new WarehousingDAO();

					selectOrderList = OrderListTableView.getSelectionModel().getSelectedItems();

					String wnum = selectOrderList.get(0).getO_num();

					if (wDao.searchWareWnum(wnum) == 0) {

						/*******************************
						 * �ֹ� ���� ���� ���� �˾� ����
						 ************************/
						FXMLLoader orderCountEditLoader = new FXMLLoader();
						orderCountEditLoader.setLocation(getClass().getResource("/View/orderCountEdit.fxml"));

						Stage orderCountEditdialog = new Stage(StageStyle.DECORATED);
						orderCountEditdialog.initModality(Modality.WINDOW_MODAL);
						orderCountEditdialog.initOwner(btnOrderEdit.getScene().getWindow());
						orderCountEditdialog.setTitle("�ֹ� ���� ����");

						Parent parentOrderCountEdit = (Parent) orderCountEditLoader.load();
						Scene countEditScene = new Scene(parentOrderCountEdit);
						orderCountEditdialog.setScene(countEditScene);
						orderCountEditdialog.setResizable(false);
						orderCountEditdialog.show();

						/*******************************
						 * �ֹ� �������� �˾� �ؽ�Ʈ�ʵ� ��ư ����
						 *****************************/

						TextField txtOrderCountEdit = (TextField) parentOrderCountEdit.lookup("#txtOrderCountEdit");
						Button btnOrderCountEditOK = (Button) parentOrderCountEdit.lookup("#btnOrderCountEditOK");
						Button btnOrderCountEditExit = (Button) parentOrderCountEdit.lookup("#btnOrderCountEditExit");

						/****************************
						 * ��ư ��ɼ���
						 **************************/
						// ���� ó���� ���� ���õ� ��ǰ������ �ε��� ����

						OrderVO OrderCountEdit = OrderListTableView.getSelectionModel().getSelectedItem();
						selectedIndex = OrderListTableView.getSelectionModel().getSelectedIndex();

						txtOrderCountEdit.setText(OrderCountEdit.getO_count() + "");

						// �ֹ����� ���� ��ư ��ɼ���
						btnOrderCountEditOK.setOnAction(ev -> {

							OrderVO oVo = null;
							OrderDAO oDao = null;

							try {

								if (txtOrderCountEdit.getText().equals("")) {

									Alert alert = new Alert(AlertType.WARNING);
									alert.setTitle("�ֹ� ���� ����");
									alert.setHeaderText("������ �ֹ������� �Է��ϼ���.");
									alert.setContentText("�ֹ������� �����Ǿ����ϴ�.");
									alert.showAndWait();

								} else if (Integer.parseInt(txtOrderCountEdit.getText()) <= 0) {

									// orderListTotalList();

									Alert alert = new Alert(AlertType.WARNING);
									alert.setTitle("�ֹ� ���� ����");
									alert.setHeaderText("������ - Ȥ�� 0���� �ԷµǾ����ϴ�.");
									alert.setContentText("���� �� ������ ��Ȯ�� �Է��ϼ���.");
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

										// �⺻�̹��� ǥ��
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
											System.out.println(" �Ѽ���,�Ѿ� �ַ�" + e1);
										}

									} catch (Exception e2) {

										System.out.println("�ֹ� ���� ����â ������ư ���� ���� " + e2);

									}

								}

							} catch (Exception e3) {

								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("�ֹ� ���� ����");
								alert.setHeaderText("���� �� ������ ���ڷ� ��Ȯ�� �Է��ϼ���.");
								alert.setContentText("�������� �����ϼ���.");
								alert.showAndWait();

							}

						});

						// ��� ��ư ��ɼ���
						btnOrderCountEditExit.setOnAction(ev -> {
							orderCountEditdialog.close();
						});

					} else {

						Alert alert1 = new Alert(AlertType.WARNING);
						alert1.setTitle("�ֹ� ���� ���� ����");
						alert1.setHeaderText("�ش� ��ǰ�� �԰�Ϸ�ó���� ���� ������ �Ұ����մϴ�.");
						alert1.setContentText("�԰� ����� Ȯ���� �ٽ� �õ����ּ���.");
						alert1.showAndWait();

					}

				} catch (Exception e1) {
					System.out.println("�ֹ����� ���� �˾�â ���ÿ���" + e1);
				}

			});

			// �ֹ� ��� ���� ��ư ��ɼ���
			btnOrderDelete.setOnAction(e -> {

				OrderDAO oDao = null;
				oDao = new OrderDAO();
				WarehousingDAO wDao = new WarehousingDAO();

				try {
					selectOrderList = OrderListTableView.getSelectionModel().getSelectedItems();

					String wnum = selectOrderList.get(0).getO_num();

					if (wDao.searchWareWnum(wnum) == 0) {
						// ���� ��ư ������ �˶��޼��� �˾�����
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("�����޼���");
						alert.setHeaderText("������ �����ʹ� �����ϽǼ� �����ϴ�.");
						alert.setContentText("������ �����Ͻðڽ��ϱ�?");

						Optional<ButtonType> result = alert.showAndWait();

						if (result.get() == ButtonType.OK) {

							oDao.getOrderListDelete(no);
							orderData.removeAll(orderData);

							// �ֹ� �̷� ��ü����
							orderListTotalList();

							/*
							 * // �̹��� ���� ���� stockImageDelete(selectFileName);
							 */

							// �⺻�̹��� ǥ��
							localUrl = "/image/default.gif";
							localImage = new Image(localUrl, false);
							OrderImageView.setFitWidth(300);
							OrderImageView.setFitHeight(300);
							OrderImageView.setImage(localImage);

							// �� ����, �Ѿ� ����
							totalOrderCountData = oDao1.getStockTotalCount(oVo1).getO_orderTotalCount();
							totalOrderPriceData = oDao1.getStockTotalCount(oVo1).getO_orderTotalPrice();

							txtOrderListTotalCount.setText(totalOrderCountData + "");
							txtOrderListTotalPrice.setText(totalOrderPriceData + "");

						} else {

						}
					} else {

						Alert alert1 = new Alert(AlertType.WARNING);
						alert1.setTitle("�ֹ� ��� ���� ����");
						alert1.setHeaderText("�ش� ��ǰ�� �԰�Ϸ�ó���� ���� ������ �Ұ����մϴ�.");
						alert1.setContentText("�԰����� Ȯ���� �ٽ� �õ����ּ���.");
						alert1.showAndWait();

					}

				} catch (Exception e1) {
					System.out.println("�ֹ� �̷� ������ư ���� ����" + e1);
				}

			});

			// �԰� ��� ��ư ��ɼ���
			btnWarehousing.setOnAction(e -> {

				try {
					wareData.removeAll(wareData);
					OrderVO oVo = null;
					WarehousingDAO wDao = new WarehousingDAO();
					/*
					 * File dirMake = new File(dirSave.getAbsolutePath());
					 * 
					 * // �̹��� ���� if (!dirMake.exists()) { dirMake.mkdir(); }
					 * 
					 * // �̹��� ���� ���� String fileName = imageSave(selectedFile);
					 */

					// �԰��� ���� ����
					if (e.getSource().equals(btnWarehousing)) {

						selectOrderList = OrderListTableView.getSelectionModel().getSelectedItems();

						String wnum = selectOrderList.get(0).getO_num();
						String selectGoods = selectOrderList.get(0).getO_pnum();
						int swnum = selectOrderList.get(0).getNo();

						// ������ ������ �ֹ���ȣ�� �̹� �԰�Ǿ�����Ȯ���� �԰���� �ʾ����� �����ϵ��� ����
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
								alert.setTitle("�԰� ���");
								alert.setHeaderText(selectOrderList.get(0).getO_pnum() + " ���� ��ǰ�� ���������� �԰�Ϸ� �Ǿ����ϴ�.");
								alert.setContentText("�԰� ��� �Է� ����");

								alert.showAndWait();

								// �⺻�̹����� ����
								localUrl = "/image/default.gif";
								localImage = new Image(localUrl, false);
								OrderImageView.setImage(localImage);
								// selectedFile = null;

								try {

									// �԰� �� ����, ���հ�
									totalWareCountData = wDao1.getWareTotalCount(wVo1).getW_orderTotalCount();
									totalWarePriceData = wDao1.getWareTotalCount(wVo1).getW_orderTotalPrice();

									txtWarehousingListTotalCount.setText(totalWareCountData + "");
									txtWarehousingListTotalPrice.setText(totalWarePriceData + "");

								} catch (Exception e1) {
									System.out.println("�԰� ��� �� �Ѽ���,�Ѿ� �ַ�" + e1);
								}

							}

						} else {

							warehousingTotalList();

							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("�԰� ��� ����");
							alert.setHeaderText("�ش� ��ǰ�� �̹� �԰�Ϸ�ó�� �Ǿ����ϴ�.");
							alert.setContentText("�԰� ����� Ȯ���� ��õ� ���ּ���.");
							alert.showAndWait();

						}
					}
				} catch (Exception e3) {

					warehousingTotalList();

					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("�԰� ��� ����");
					alert.setHeaderText("����� �԰������� ��Ȯ�� Ȯ���Ͻÿ�");
					alert.setContentText("�������� �����ϼ���");
					alert.showAndWait();

					System.out.println(e3);

				}

			});

			// �԰� ��� ���� ��ư ��ɼ���
			btnWarehousingEdit.setOnAction(e -> {

				WarehousingDAO wDao = null;
				wDao = new WarehousingDAO();
				selectWareList = WarehousingListTableView.getSelectionModel().getSelectedItems();
				String deleteWare = selectWareList.get(0).getW_pnum();
				int deleteNum = selectWareList.get(0).getNo();

				try {
					// ���� ��ư ������ �˶��޼��� �˾�����
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("�����޼���");
					alert.setHeaderText("������ �����ʹ� �����ϽǼ� �����ϴ�.");
					alert.setContentText("������ �����Ͻðڽ��ϱ�?");

					Optional<ButtonType> result = alert.showAndWait();

					if (result.get() == ButtonType.OK) {

						wDao.deleteWareToStock(deleteWare, deleteNum);
						wDao.getWareListDelete(no);
						wareData.removeAll(wareData);

						// �԰� �̷� ��ü����
						warehousingTotalList();

						/*
						 * // �̹��� ���� ���� stockImageDelete(selectFileName);
						 */

						// �⺻�̹��� ǥ��
						localUrl = "/image/default.gif";
						localImage = new Image(localUrl, false);
						OrderImageView.setFitWidth(300);
						OrderImageView.setFitHeight(300);
						OrderImageView.setImage(localImage);

						// �԰� �� ����, ���հ�
						totalWareCountData = wDao1.getWareTotalCount(wVo1).getW_orderTotalCount();
						totalWarePriceData = wDao1.getWareTotalCount(wVo1).getW_orderTotalPrice();

						txtWarehousingListTotalCount.setText(totalWareCountData + "");
						txtWarehousingListTotalPrice.setText(totalWarePriceData + "");

					} else {

					}
				} catch (Exception e1) {
					System.out.println("�԰� �̷� ������ư ���� ����" + e1);
				}

			});

			// ���� ��ư ��ɼ���
			btnOrderExit.setOnAction(e -> {

				orderdialog.close();
			});

		} catch (Exception e) {

		}
	}

	// �Ǹ�â ���̾�α� ����
	public void handleBtnSalesAction(ActionEvent event) {

		data.removeAll(data);
		orderData.removeAll(orderData);
		wareData.removeAll(wareData);
		salesData.removeAll(salesData);
		preSalesData.removeAll(preSalesData);

		StockDAO sDao2 = new StockDAO();

		try {

			/****************************
			 * �Ǹ� ȭ�� ����
			 ****************************/
			FXMLLoader salesLoader = new FXMLLoader();
			salesLoader.setLocation(getClass().getResource("/View/sales.fxml"));

			Stage salesdialog = new Stage(StageStyle.DECORATED);
			salesdialog.initModality(Modality.WINDOW_MODAL);
			salesdialog.initOwner(btnSales.getScene().getWindow());
			salesdialog.setTitle("�Ǹ�");

			Parent parentSales = (Parent) salesLoader.load();
			Scene scene = new Scene(parentSales);
			salesdialog.setScene(scene);
			salesdialog.setResizable(false);
			salesdialog.show();

			/*****************************
			 * �Ǹ�â ��ư�� �ؽ�Ʈ�ʵ� ����
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
			 * ���̺�� Į������
			 *******************************/

			// �Ǹ� ��ǰ �˻�â ���̺�� �÷�����

			TableColumn colsst_pnum = new TableColumn("ǰ��");
			colsst_pnum.setMinWidth(110);
			colsst_pnum.setStyle("-fx-alignment:CENTER");
			colsst_pnum.setCellValueFactory(new PropertyValueFactory<>("st_pnum"));

			TableColumn colsst_pname = new TableColumn("ǰ��");
			colsst_pname.setMinWidth(130);
			colsst_pname.setStyle("-fx-alignment:CENTER");
			colsst_pname.setCellValueFactory(new PropertyValueFactory<>("st_pname"));

			TableColumn colsst_pkind = new TableColumn("ǰ��");
			colsst_pkind.setMinWidth(80);
			colsst_pkind.setStyle("-fx-alignment:CENTER");
			colsst_pkind.setCellValueFactory(new PropertyValueFactory<>("st_pkind"));

			TableColumn colsst_size = new TableColumn("������");
			colsst_size.setMinWidth(70);
			colsst_size.setStyle("-fx-alignment:CENTER");
			colsst_size.setCellValueFactory(new PropertyValueFactory<>("st_size"));

			TableColumn colsst_color = new TableColumn("�÷�");
			colsst_color.setMinWidth(50);
			colsst_color.setStyle("-fx-alignment:CENTER");
			colsst_color.setCellValueFactory(new PropertyValueFactory<>("st_color"));

			TableColumn colsst_price = new TableColumn("�ܰ�");
			colsst_price.setMinWidth(70);
			colsst_price.setStyle("-fx-alignment:CENTER");
			colsst_price.setCellValueFactory(new PropertyValueFactory<>("st_price"));

			TableColumn colsst_count = new TableColumn("������");
			colsst_count.setMinWidth(40);
			colsst_count.setStyle("-fx-alignment:CENTER");
			colsst_count.setCellValueFactory(new PropertyValueFactory<>("st_count"));

			TableColumn colsst_filename = new TableColumn("���ϸ�");
			colsst_filename.setMinWidth(200);
			colsst_filename.setCellValueFactory(new PropertyValueFactory<>("st_filename"));

			SalesSearchTableView.getColumns().addAll(colsst_pnum, colsst_pname, colsst_pkind, colsst_size, colsst_color,
					colsst_price, colsst_count, colsst_filename);

			SalesSearchTableView.setItems(data);

			// �ǸŴ�� ���̺�� �÷�����

			TableColumn colprsa_pnum = new TableColumn("ǰ��");
			colprsa_pnum.setMinWidth(110);
			colprsa_pnum.setStyle("-fx-alignment:CENTER");
			colprsa_pnum.setCellValueFactory(new PropertyValueFactory<>("sa_pnum"));

			TableColumn colprsa_pname = new TableColumn("ǰ��");
			colprsa_pname.setMinWidth(130);
			colprsa_pname.setStyle("-fx-alignment:CENTER");
			colprsa_pname.setCellValueFactory(new PropertyValueFactory<>("sa_pname"));

			TableColumn colprsa_pkind = new TableColumn("ǰ��");
			colprsa_pkind.setMinWidth(80);
			colprsa_pkind.setStyle("-fx-alignment:CENTER");
			colprsa_pkind.setCellValueFactory(new PropertyValueFactory<>("sa_pkind"));

			TableColumn colprsa_size = new TableColumn("������");
			colprsa_size.setMinWidth(70);
			colprsa_size.setStyle("-fx-alignment:CENTER");
			colprsa_size.setCellValueFactory(new PropertyValueFactory<>("sa_size"));

			TableColumn colprsa_color = new TableColumn("�÷�");
			colprsa_color.setMinWidth(50);
			colprsa_color.setStyle("-fx-alignment:CENTER");
			colprsa_color.setCellValueFactory(new PropertyValueFactory<>("sa_color"));

			TableColumn colprsa_price = new TableColumn("�ܰ�");
			colprsa_price.setMinWidth(70);
			colprsa_price.setStyle("-fx-alignment:CENTER");
			colprsa_price.setCellValueFactory(new PropertyValueFactory<>("sa_price"));

			TableColumn colprsa_count = new TableColumn("�Ǹż���");
			colprsa_count.setMinWidth(40);
			colprsa_count.setStyle("-fx-alignment:CENTER");
			colprsa_count.setCellValueFactory(new PropertyValueFactory<>("sa_count"));

			TableColumn colprsa_totalPrice = new TableColumn("�Ѿ�");
			colprsa_totalPrice.setMinWidth(40);
			colprsa_totalPrice.setStyle("-fx-alignment:CENTER");
			colprsa_totalPrice.setCellValueFactory(new PropertyValueFactory<>("sa_totalPrice"));

			TableColumn colprsa_CusName = new TableColumn("����");
			colprsa_CusName.setMinWidth(80);
			colprsa_CusName.setStyle("-fx-alignment:CENTER");
			colprsa_CusName.setCellValueFactory(new PropertyValueFactory<>("sa_CusName"));

			TableColumn colprsa_CusNum = new TableColumn("������ó");
			colprsa_CusNum.setMinWidth(120);
			colprsa_CusNum.setStyle("-fx-alignment:CENTER");
			colprsa_CusNum.setCellValueFactory(new PropertyValueFactory<>("sa_CusNum"));

			TableColumn colprsa_fileName = new TableColumn("���ϸ�");
			colprsa_fileName.setMinWidth(200);
			colprsa_fileName.setCellValueFactory(new PropertyValueFactory<>("sa_fileName"));

			PreSalesTableView.getColumns().addAll(colprsa_pnum, colprsa_pname, colprsa_pkind, colprsa_size,
					colprsa_color, colprsa_price, colprsa_count, colprsa_totalPrice, colprsa_CusName, colprsa_CusNum,
					colprsa_fileName);

			PreSalesTableView.setItems(preSalesData);

			/*********************************
			 * �Ǹ�â �ʱⰪ ����
			 ********************************/

			// �Ǹ� ��������, �ǸŴ�� ���� ���� �Ǹ� ��ư �ʱ� ��Ȱ��ȭ
			btnSalesCount.setDisable(true);
			btnPreSalesEdit.setDisable(true);
			btnPreSalesDelete.setDisable(true);
			btnPreSalesOk.setDisable(true);

			// �⺻�̹��� ǥ��
			localUrl = "/image/default.gif";
			localImage = new Image(localUrl, false);
			SalesImageView.setFitWidth(300);
			SalesImageView.setFitHeight(300);
			SalesImageView.setImage(localImage);

			/*******************************
			 * ����ư �� �̹����� ���̺�� ��ɼ���
			 ******************************/

			// �Ǹ�â ��ǰ�˻� ���̺�� ��ǰ ���ý� �̹����� ȭ�鿡 ����, ���� ���� ��ư Ȱ��ȭ ����
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

						// �Ǹ� �������� ��ư Ȱ��ȭ
						btnSalesCount.setDisable(false);

						// �Ǹ� ��� ���� ���� �Ǹ� ��ư ��Ȱ��ȭ
						btnPreSalesEdit.setDisable(true);
						btnPreSalesDelete.setDisable(true);

					} catch (Exception e1) {

					}
				}

			});

			// �ǸŴ��â ���̺�� ��ǰ ���ý� �̹����� ȭ�鿡 ����, ���� ���� ��ư Ȱ��ȭ ����
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

						// �Ǹ� �������� ��ư ��Ȱ��ȭ
						btnSalesCount.setDisable(true);

						// �Ǹ� ��� ���� ���� ��ư Ȱ��ȭ
						btnPreSalesEdit.setDisable(false);
						btnPreSalesDelete.setDisable(false);

					} catch (Exception e1) {

					}
				}

			});

			// ǰ������ ��ǰ��ȸ ��ư ��ɼ���(�Ǹ�)
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
						alert.setTitle("��ǰ ���� ǰ�� ��ȸ");
						alert.setHeaderText("��ǰ�� ǰ���� �Է��Ͻÿ�");
						alert.setContentText("������ �����ϼ���");
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
						// �⺻�̹��� ǥ��
						localUrl = "/image/default.gif";
						localImage = new Image(localUrl, false);
						SalesImageView.setFitWidth(300);
						SalesImageView.setFitHeight(300);
						SalesImageView.setImage(localImage);

						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("��ǰ ���� ǰ�� ��ȸ");
						alert.setHeaderText(salesSearch + "���� ��ǰ�� ����Ʈ�� �����ϴ�.");
						alert.setContentText("�ٽ� �˻��ϼ���");
						alert.showAndWait();

					}

				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("��ǰ ���� ǰ�� ��ȸ");
					alert.setHeaderText("��ǰ ���� �˻��� ������ �߻��Ͽ����ϴ�.");
					alert.setContentText("�ٽ��ϼ���");
					alert.showAndWait();

				}

			});

			// �˻� �ʱ�ȭ ��ư ��ɼ���
			btnSalesSearchClear.setOnAction(e -> {
				data.removeAll(data);
				// txtSalesSearch.clear();
				cbSalesSearch.getSelectionModel().clearSelection();
				btnSalesCount.setDisable(true);

				localUrl = "/image/default.gif"; // ����Ʈ �̹��� ����
				localImage = new Image(localUrl, false);
				SalesImageView.setFitWidth(300);
				SalesImageView.setFitHeight(300);
				SalesImageView.setImage(localImage);
			});

			// �Ǹ� ���� ���� ��ư ��ɼ���
			btnSalesCount.setOnAction(e -> {

				selectSales = SalesSearchTableView.getSelectionModel().getSelectedItems();
				try {

					/*******************************
					 * �Ǹ� ���� ���� �˾� ����
					 ************************/
					FXMLLoader salesCountLoader = new FXMLLoader();
					salesCountLoader.setLocation(getClass().getResource("/View/salesCount.fxml"));

					Stage salesCountdialog = new Stage(StageStyle.DECORATED);
					salesCountdialog.initModality(Modality.WINDOW_MODAL);
					salesCountdialog.initOwner(btnSalesCount.getScene().getWindow());
					salesCountdialog.setTitle("�Ǹ� ���� ����");

					Parent parentSalesCount = (Parent) salesCountLoader.load();
					Scene salesScene = new Scene(parentSalesCount);
					salesCountdialog.setScene(salesScene);
					salesCountdialog.setResizable(false);
					salesCountdialog.show();

					/*******************************
					 * �Ǹ� �������� �˾� �ؽ�Ʈ�ʵ� ��ư ����
					 *****************************/

					TextField txtCurrentCount = (TextField) parentSalesCount.lookup("#txtCurrentCount");
					TextField txtSalesCount = (TextField) parentSalesCount.lookup("#txtSalesCount");

					Button btnSalesCountOK = (Button) parentSalesCount.lookup("#btnSalesCountOK");
					Button btnSalesCountExit = (Button) parentSalesCount.lookup("#btnSalesCountExit");

					txtCurrentCount.setText(selectSales.get(0).getSt_count() + "");
					txtCurrentCount.setEditable(false);

					/****************************
					 * ��ư ��ɼ���
					 **************************/
					// ��� ��ư ��ɼ���
					btnSalesCountOK.setOnAction(ev -> {

						// �ǸŴ�� �Ѽ��� �ʱ�ȭ
						int totalX = 0;
						// �ǸŴ�� �Ѿ� �ʱ�ȭ
						int totalY = 0;

						SalesVO sVo = null;
						SalesDAO saDao = new SalesDAO();

						// �ǸŴ�� ������� ����
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

									// �⺻�̹����� ����
									localUrl = "/image/default.gif";
									localImage = new Image(localUrl, false);
									SalesImageView.setImage(localImage);
									// selectedFile = null;

									data.removeAll(data);
									btnSalesCount.setDisable(true);

									localUrl = "/image/default.gif"; // ����Ʈ �̹��� ����
									localImage = new Image(localUrl, false);
									SalesImageView.setFitWidth(300);
									SalesImageView.setFitHeight(300);
									SalesImageView.setImage(localImage);

									btnPreSalesOk.setDisable(false);

									salesCountdialog.close();
								} else {
									Alert alert = new Alert(AlertType.WARNING);
									alert.setTitle("�ǸŹ�ǰ ���� ����");
									alert.setHeaderText("�Ǹ��� ������ ��Ȯ�� �Է��Ͻÿ�");
									alert.setContentText("�������� �����ϼ���");
									alert.showAndWait();
								}

							} catch (Exception e1) {

								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("�ǸŹ�ǰ ���� ����");
								alert.setHeaderText("�Ǹ��� ������ ���ڸ� �Է��Ͻÿ�");
								alert.setContentText("�������� �����ϼ���");
								alert.showAndWait();
							}

						}

					});

					// ��� ��ư ��ɼ���
					btnSalesCountExit.setOnAction(ev -> {
						salesCountdialog.close();
					});

				} catch (Exception e1) {
					System.out.println("�Ǹ� ���� ���� �˾�â ���ÿ���" + e1);
				}

			});

			// �Ǹ���Ȳ��Ϻ��� ��ư ��ɼ���
			btnSalesListView.setOnAction(e -> {

				salesData.removeAll(salesData);

				try {
					/****************************
					 * �Ǹ� ��� ��Ȳ ȭ�� ����
					 ****************************/
					FXMLLoader salesListLoader = new FXMLLoader();
					salesListLoader.setLocation(getClass().getResource("/View/salesList.fxml"));

					Stage salesListdialog = new Stage(StageStyle.DECORATED);
					salesListdialog.initModality(Modality.WINDOW_MODAL);
					salesListdialog.initOwner(btnSalesListView.getScene().getWindow());
					salesListdialog.setTitle("�Ǹ� ���");

					Parent parentSalesList = (Parent) salesListLoader.load();
					Scene listScene = new Scene(parentSalesList);
					salesListdialog.setScene(listScene);
					salesListdialog.setResizable(false);
					salesListdialog.show();

					/**********************************
					 * �Ǹ� ��Ȳ �� ��ư ����
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
					 * ���̺�� Į������
					 *****************************/

					TableColumn colsalesno = new TableColumn("NO");
					colsalesno.setMinWidth(40);
					colsalesno.setStyle("-fx-alignment:CENTER");
					colsalesno.setCellValueFactory(new PropertyValueFactory<>("no"));

					TableColumn colsa_num = new TableColumn("�ǸŹ�ȣ");
					colsa_num.setMinWidth(100);
					colsa_num.setStyle("-fx-alignment:CENTER");
					colsa_num.setCellValueFactory(new PropertyValueFactory<>("sa_num"));

					TableColumn colsa_pnum = new TableColumn("�Ǹ�ǰ��");
					colsa_pnum.setMinWidth(120);
					colsa_pnum.setStyle("-fx-alignment:CENTER");
					colsa_pnum.setCellValueFactory(new PropertyValueFactory<>("sa_pnum"));

					TableColumn colsa_pname = new TableColumn("�Ǹ�ǰ��");
					colsa_pname.setMinWidth(140);
					colsa_pname.setStyle("-fx-alignment:CENTER");
					colsa_pname.setCellValueFactory(new PropertyValueFactory<>("sa_pname"));

					TableColumn colsa_pkind = new TableColumn("�Ǹ�ǰ��");
					colsa_pkind.setMinWidth(80);
					colsa_pkind.setStyle("-fx-alignment:CENTER");
					colsa_pkind.setCellValueFactory(new PropertyValueFactory<>("sa_pkind"));

					TableColumn colsa_size = new TableColumn("������");
					colsa_size.setMinWidth(100);
					colsa_size.setStyle("-fx-alignment:CENTER");
					colsa_size.setCellValueFactory(new PropertyValueFactory<>("sa_size"));

					TableColumn colsa_color = new TableColumn("�÷�");
					colsa_color.setMinWidth(60);
					colsa_color.setStyle("-fx-alignment:CENTER");
					colsa_color.setCellValueFactory(new PropertyValueFactory<>("sa_color"));

					TableColumn colsa_price = new TableColumn("�ܰ�");
					colsa_price.setMinWidth(80);
					colsa_price.setStyle("-fx-alignment:CENTER");
					colsa_price.setCellValueFactory(new PropertyValueFactory<>("sa_price"));

					TableColumn colsa_count = new TableColumn("�Ǹż���");
					colsa_count.setMinWidth(60);
					colsa_count.setStyle("-fx-alignment:CENTER");
					colsa_count.setCellValueFactory(new PropertyValueFactory<>("sa_count"));

					TableColumn colsa_totalPrice = new TableColumn("�Ѿ�");
					colsa_totalPrice.setMinWidth(100);
					colsa_totalPrice.setStyle("-fx-alignment:CENTER");
					colsa_totalPrice.setCellValueFactory(new PropertyValueFactory<>("sa_totalPrice"));

					TableColumn colsa_date = new TableColumn("�Ǹų�¥");
					colsa_date.setMinWidth(100);
					colsa_date.setStyle("-fx-alignment:CENTER");
					colsa_date.setCellValueFactory(new PropertyValueFactory<>("sa_date"));

					TableColumn colsa_fileName = new TableColumn("�̹���");
					colsa_fileName.setMinWidth(200);
					colsa_fileName.setStyle("-fx-alignment:CENTER");
					colsa_fileName.setCellValueFactory(new PropertyValueFactory<>("sa_fileName"));

					TableColumn colsa_CusName = new TableColumn("����");
					colsa_CusName.setMinWidth(100);
					colsa_CusName.setStyle("-fx-alignment:CENTER");
					colsa_CusName.setCellValueFactory(new PropertyValueFactory<>("sa_CusName"));

					TableColumn colsa_CusNum = new TableColumn("������ó");
					colsa_CusNum.setMinWidth(140);
					colsa_CusNum.setStyle("-fx-alignment:CENTER");
					colsa_CusNum.setCellValueFactory(new PropertyValueFactory<>("sa_CusNum"));

					SalesListTableView.getColumns().addAll(colsalesno, colsa_date, colsa_num, colsa_pnum, colsa_pname,
							colsa_pkind, colsa_size, colsa_color, colsa_price, colsa_count, colsa_totalPrice,
							colsa_CusName, colsa_CusNum, colsa_fileName);

					SalesListTableView.setItems(salesData);

					/******************************
					 * �ʱ⼳��
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

					// �⺻�̹��� ǥ��
					localUrl = "/image/default.gif";
					localImage = new Image(localUrl, false);
					SalesListImageView.setFitWidth(300);
					SalesListImageView.setFitHeight(300);
					SalesListImageView.setImage(localImage);

					// �� �Ǹŷ�, ���Ǹž� ����
					totalSalesCountData = saDao.getSalesTotalCount(saVo).getSa_salesTotalCount();
					totalSalesPriceData = saDao.getSalesTotalCount(saVo).getSa_salesTotalPrice();

					txtSalesListTotalCount.setText(totalSalesCountData + "");
					txtSalesListTotalPrice.setText(totalSalesPriceData + "");

					/******************************
					 * �Ǹ� ��Ȳ�� ��ɼ���
					 *****************************/

					// ���� ���� ���� ����(�Ǹ� ���)
					btnSalesSaveFileDir.setOnAction(e1 -> {

						final DirectoryChooser directoryChooser = new DirectoryChooser();
						final File selectedDirectory = directoryChooser.showDialog(primaryStage);

						if (selectedDirectory != null) {
							txtSalesSaveFileDir.setText(selectedDirectory.getAbsolutePath());
							btnSalesExcel.setDisable(false);
							btnSalesPDF.setDisable(false);

						}

					});

					// ���� ���� ���� ��ư(�Ǹ� ���)
					btnSalesExcel.setOnAction(e1 -> {
						SalesDAO sDao8 = new SalesDAO();
						boolean saveSuccess;

						ArrayList<SalesVO> list;
						list = sDao8.getSalesTotal();
						SalesExcel excelWriter = new SalesExcel();

						// xlsx���� ����
						saveSuccess = excelWriter.xlsxWiter(list, txtSalesSaveFileDir.getText());
						if (saveSuccess) {
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("���� ���� ����");
							alert.setHeaderText("�Ǹ� ��� ���� ���� ���� ����");
							alert.setContentText("�Ǹ� ��� ���� ����");
							alert.showAndWait();

						}

						txtSalesSaveFileDir.clear();
						btnSalesExcel.setDisable(true);
						btnSalesPDF.setDisable(true);

					});

					// pdf���� ���� (�ǸŸ��)
					btnSalesPDF.setOnAction(e1 -> {
						try {
							// pdf document ����
							Document document = new Document(PageSize.A4, 0, 0, 30, 30);
							// pdf������ ������ ��������, pdf������ �����ǰ� ���� ��Ʈ������ �����
							String strReportPDFName = "sales_" + System.currentTimeMillis() + ".pdf";
							PdfWriter.getInstance(document,
									new FileOutputStream(txtSalesSaveFileDir.getText() + "\\" + strReportPDFName));
							// document�� ���� pdf������ �ۼ��Ҽ� �ֵ����Ѵ�.
							document.open();
							// �ѱ� ���� ��Ʈ ����
							BaseFont bf = BaseFont.createFont("font/MALGUN.TTF", BaseFont.IDENTITY_H,
									BaseFont.EMBEDDED);
							Font font = new Font(bf, 8, Font.NORMAL);
							Font font2 = new Font(bf, 14, Font.BOLD);

							// Ÿ��Ʋ ����
							Paragraph title = new Paragraph("�Ǹ� ���", font2);
							// ��� ����
							title.setAlignment(Element.ALIGN_CENTER);
							// ������ �߰�
							document.add(title);
							document.add(new Paragraph("\r\n"));

							// ���� ��¥
							LocalDate date = LocalDate.now();
							Paragraph writeDay = new Paragraph(date.toString(), font);

							// ������ ����
							writeDay.setAlignment(Element.ALIGN_RIGHT);
							// ������ �߰�
							document.add(writeDay);
							document.add(new Paragraph("\r\n"));

							// ���̺� ����, Table��ü ���� pdftable��ü�� �� �����ϰ� ���̺��� �����Ҽ� �ִ�.
							// ������ ���� �÷����� �����ش�
							PdfPTable table = new PdfPTable(14);
							// �÷��� ���� ���Ѵ�.
							table.setWidths(new int[] { 30, 70, 70, 70, 50, 50, 40, 60, 40, 60, 70, 100, 60, 100 });

							// �÷� Ÿ��Ʋ ����
							PdfPCell header1 = new PdfPCell(new Paragraph("��ȣ", font));
							PdfPCell header2 = new PdfPCell(new Paragraph("�ֹ���ȣ", font));
							PdfPCell header3 = new PdfPCell(new Paragraph("�ֹ�ǰ��", font));
							PdfPCell header4 = new PdfPCell(new Paragraph("�ֹ�ǰ��", font));
							PdfPCell header5 = new PdfPCell(new Paragraph("ǰ��", font));
							PdfPCell header6 = new PdfPCell(new Paragraph("������", font));
							PdfPCell header7 = new PdfPCell(new Paragraph("�÷�", font));
							PdfPCell header8 = new PdfPCell(new Paragraph("�ֹ��ܰ�", font));
							PdfPCell header9 = new PdfPCell(new Paragraph("�ֹ�����", font));
							PdfPCell header10 = new PdfPCell(new Paragraph("�ֹ��ݾ�", font));
							PdfPCell header11 = new PdfPCell(new Paragraph("�ֹ���¥", font));
							PdfPCell header12 = new PdfPCell(new Paragraph("�̹�����", font));
							PdfPCell header13 = new PdfPCell(new Paragraph("����", font));
							PdfPCell header14 = new PdfPCell(new Paragraph("������ó", font));

							// ��������
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

							// ��������
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

							// ���̺� �� �߰�
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

							// DB���� �� ����Ʈ ����
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

								// ���� ����
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

								// ���� ����
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

								// ���̺� �� �߰�
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
							// document �� ���̺� �߰�
							document.add(table);
							// �ݱ� ���� ����
							document.close();

							txtSalesSaveFileDir.clear();
							btnSalesPDF.setDisable(true);
							btnSalesExcel.setDisable(true);

							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("PDF���� ����");
							alert.setHeaderText("�Ǹ� ��� pdf���� ���� ����");
							alert.setContentText("�Ǹ� ��� PDF����");
							alert.showAndWait();

						} catch (FileNotFoundException e0) {
							e0.printStackTrace();

						} catch (DocumentException e0) {
							e0.printStackTrace();
						} catch (IOException e0) {
							e0.printStackTrace();
						}
					});

					// ������ ��Ŀ ���ý� ������ư ��Ȱ��ȭ ����
					dpSalesListDateStart.setOnMouseClicked(e1 -> {
						btnSalesListDelete.setDisable(true);
					});
					dpSalesListDateEnd.setOnMouseClicked(e1 -> {
						btnSalesListDelete.setDisable(true);
					});

					// ����, ����ó �˻� �ؽ�Ʈ �ʵ� ���ý� ������ư ��Ȱ��ȭ ����
					txtSalesListNameSearch.setOnMouseClicked(e1 -> {
						btnSalesListDelete.setDisable(true);
					});
					txtSalesListCallSearch.setOnMouseClicked(e1 -> {
						btnSalesListDelete.setDisable(true);
					});

					// �Ǹ� ��Ȳâ ���̺�� ��ǰ ���ý� �̹����� ȭ�鿡 ����, ���� ���� ��ư Ȱ��ȭ ����
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

					// �Ǹ� ��� ���� ��ư ��� ����
					btnSalesListDelete.setOnAction(e1 -> {

						selectSalesList = SalesListTableView.getSelectionModel().getSelectedItems();
						selectedIndex = SalesListTableView.getSelectionModel().getSelectedIndex();
						String deleteSales = selectSalesList.get(0).getSa_pnum();
						int deleteSalesCount = selectSalesList.get(0).getSa_count();
						int deleteSalesNo = selectSalesList.get(0).getNo();

						try {
							Alert alert = new Alert(AlertType.CONFIRMATION);
							alert.setTitle("�Ǹ� �̷� ����");
							alert.setHeaderText("������ �����ʹ� �����ϽǼ� �����ϴ�.");
							alert.setContentText("������ �����Ͻðڽ��ϱ�?");

							Optional<ButtonType> result = alert.showAndWait();

							if (result.get() == ButtonType.OK) {

								// �Ǹ� �̷¿��� �������� ǰ���� ������ŭ ��� ����
								saDao.inputPreSalesToStock(deleteSales, deleteSalesCount);

								// �Ǹ��̷� DB���� ����
								saDao.getSalesDelete(deleteSalesNo);

								salesData.removeAll(salesData);

								SalesTotalList();

								// �⺻�̹��� ǥ��
								localUrl = "/image/default.gif";
								localImage = new Image(localUrl, false);
								SalesListImageView.setFitWidth(300);
								SalesListImageView.setFitHeight(300);
								SalesListImageView.setImage(localImage);

								// �� �Ǹŷ�, ���Ǹž� ����
								totalSalesCountData = saDao.getSalesTotalCount(saVo).getSa_salesTotalCount();
								totalSalesPriceData = saDao.getSalesTotalCount(saVo).getSa_salesTotalPrice();

								txtSalesListTotalCount.setText(totalSalesCountData + "");
								txtSalesListTotalPrice.setText(totalSalesPriceData + "");

							} else {

							}

						} catch (Exception e2) {
							System.out.println("�Ǹ� �̷� ��� ������ư ���� ����" + e2);
						}

					});

					// �Ⱓ���� �˻� ��ư ��ɼ���
					btnSalesListDateSearch.setOnAction(e1 -> {

						String startDay = dpSalesListDateStart.getValue() + ""; // ó�� ���õ� �ð���
						String endDay = dpSalesListDateEnd.getValue() + ""; // �ι�° ���õ� �ð���
						String nowDate = LocalDate.now() + ""; // ����ð�
						// ó�������� ��¥�� �ι�° ��¥ ��
						// compare ���� ����̸� ù��° ��¥�� ū��
						// ���� ������ ū ���� ũ�ٰ� ����� ����
						int compare = startDay.compareTo(endDay); // ���۰� �� �߿� ������ ũ��ȵ�
						int compare1 = startDay.compareTo(nowDate);// ���۰� �����߿� ������ �� ũ��ȵ�
						int compare2 = endDay.compareTo(nowDate);// ���� �����߿� ���� �� ũ��ȵ�

						if (startDay.equals("") || endDay.equals("")) {

							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("�Ⱓ ��ȸ ����");
							alert.setHeaderText("��ȸ�� �Ⱓ�� �����ϼ���");
							alert.setContentText("�Ⱓ ������ �ٽ� �õ��ϼ���");
							alert.showAndWait();

						} else if (compare1 > 0 || compare2 > 0) {

							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("�Ⱓ ��ȸ ����");
							alert.setHeaderText("�Ⱓ�� �̷��� �����Ǿ����ϴ�.");
							alert.setContentText("Ȯ���� ��õ��ϼ���");
							alert.showAndWait();

						} else if (compare > 0) {

							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("�Ⱓ ��ȸ ����");
							alert.setHeaderText("�Ⱓ ������ �߸��Ǿ����ϴ�.");
							alert.setContentText("��ȸ�� ������ ��¥�� ����¥���� �̷��� �����Ǿ����ϴ�.");
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
								// �Ⱓ���� ��ȸ�� ������� �� ������ �Ѿ� ����
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

					// �ǸŸ�� ��ü����
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

						// �⺻�̹��� ǥ��
						localUrl = "/image/default.gif";
						localImage = new Image(localUrl, false);
						SalesListImageView.setFitWidth(300);
						SalesListImageView.setFitHeight(300);
						SalesListImageView.setImage(localImage);

						try {
							// �� �Ǹŷ�, ���Ǹž� ����
							totalSalesCountData = saDao.getSalesTotalCount(saVo).getSa_salesTotalCount();
							totalSalesPriceData = saDao.getSalesTotalCount(saVo).getSa_salesTotalPrice();

							txtSalesListTotalCount.setText(totalSalesCountData + "");
							txtSalesListTotalPrice.setText(totalSalesPriceData + "");
						} catch (Exception e3) {

						}

					});

					// �ǸŸ�� �̸��� ��ȣ�� ��ȸ ��ư ��ɼ���
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
							// �Է��� �̸��� ��ȣ �ҷ���
							salesListName = txtSalesListNameSearch.getText().trim();
							salesListNum = txtSalesListCallSearch.getText().trim();

							saDao1 = new SalesDAO();
							saList = saDao1.getArraySalesListSearchCusName(salesListName, salesListNum);

							// �Ѱ��̶� �Է� �������� ���â �˶�
							if (salesListName.equals("") || salesListNum.equals("")) {

								searchResult = true;

								salesData.removeAll(salesData);
								SalesTotalList();

								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("�ǸŸ�� ����,����ȣ ��ȸ");
								alert.setHeaderText("�ǸŸ���� �˻��� ����� ����ȣ�� �Է��ϼ���");
								alert.setContentText("������ �����ϼ���");
								alert.showAndWait();

							}

							// �Ѵ� ��ĭ�� �ƴϰ� ������ ���̽� ������� �����Ҷ�
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

								// �̸��� ��ȣ�� ��ȸ�� ������� �� ������ �Ѿ� ����
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

								// �� �Ǹŷ�, ���Ǹž� ����
								totalSalesCountData = saDao.getSalesTotalCount(saVo).getSa_salesTotalCount();
								totalSalesPriceData = saDao.getSalesTotalCount(saVo).getSa_salesTotalPrice();

								txtSalesListTotalCount.setText(totalSalesCountData + "");
								txtSalesListTotalPrice.setText(totalSalesPriceData + "");

								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("�Ǹ� �̷� ��ȸ");
								alert.setHeaderText(
										"�̸� :" + salesListName + "��ȣ : " + salesListNum + "�� �ǸŰ� ����Ʈ�� �����ϴ�.");
								alert.setContentText("�ٽ� �˻��ϼ���");
								alert.showAndWait();

							}

						} catch (Exception e3) {

							SalesTotalList();

							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("�Ǹ� �̷� ����, ����ó ��ȸ");
							alert.setHeaderText("�Ǹ� �̷� �˻��� ������ �߻��Ͽ����ϴ�.");
							alert.setContentText("�ٽ��ϼ���");
							alert.showAndWait();
							e3.printStackTrace();

						}

					});

					// �Ǹ� ��Ȳ �� ����
					btnSalesListExit.setOnAction(e1 -> {
						salesListdialog.close();
					});

				} catch (Exception ex) {

				}
			});

			// �Ǹ� ��� ��� ����
			btnPreSalesEdit.setOnAction(e -> {

				selectPreSales = PreSalesTableView.getSelectionModel().getSelectedItems();
				selectedIndex = PreSalesTableView.getSelectionModel().getSelectedIndex();

				try {
					/****************************
					 * �Ǹ� ��� ��� ���� ���� ȭ�� ����
					 ****************************/
					FXMLLoader salesEditLoader = new FXMLLoader();
					salesEditLoader.setLocation(getClass().getResource("/View/salesCountEdit.fxml"));

					Stage salesEditdialog = new Stage(StageStyle.DECORATED);
					salesEditdialog.initModality(Modality.WINDOW_MODAL);
					salesEditdialog.initOwner(btnSalesListView.getScene().getWindow());
					salesEditdialog.setTitle("�Ǹ� ���� ����");

					Parent parentSalesEdit = (Parent) salesEditLoader.load();
					Scene salesEditScene = new Scene(parentSalesEdit);
					salesEditdialog.setScene(salesEditScene);
					salesEditdialog.setResizable(false);
					salesEditdialog.show();

					/************************
					 * ȭ�� ��ư�� �ؽ�Ʈ�ʵ� ����
					 ***********************/

					TextField txtCurrentSalesCount = (TextField) parentSalesEdit.lookup("#txtCurrentSalesCount");
					TextField txtSalesCountEdit = (TextField) parentSalesEdit.lookup("#txtSalesCountEdit");

					Button btnSalesCountEditOK = (Button) parentSalesEdit.lookup("#btnSalesCountEditOK");
					Button btnSalesCountEditExit = (Button) parentSalesEdit.lookup("#btnSalesCountEditExit");

					/************************
					 * ��� ����
					 ************************/
					txtCurrentSalesCount.setText(selectPreSales.get(0).getSa_count() + "");
					txtCurrentSalesCount.setEditable(false);

					// ���� ��ư ����
					btnSalesCountEditOK.setOnAction(e2 -> {

						SalesVO saVO = new SalesVO();
						SalesDAO saDao = new SalesDAO();
						StockDAO sDao = new StockDAO();

						try {
							if (txtSalesCountEdit.getText().equals("")
									|| Integer.parseInt(txtSalesCountEdit.getText()) <= 0) {

								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("�Ǹ� ���� ����");
								alert.setHeaderText("������ �Ǹż����� �Է��ϼ���");
								alert.setContentText("������ �Ǹż����� ���� Ȥ�� 0,������ �����Ǿ����ϴ�.");
								alert.showAndWait();

							} else if (Integer.parseInt(txtCurrentSalesCount.getText()) == Integer
									.parseInt(txtSalesCountEdit.getText())) {

								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("�Ǹ� ���� ����");
								alert.setHeaderText("������ ������ �ٸ� ���� �Է��ϼ���");
								alert.setContentText("�Ǹż��� �������� �߸��Ǿ����ϴ�.");
								alert.showAndWait();

							} else if ((sDao.countStockGoods(selectPreSales.get(0).getSa_pnum())
									+ Integer.parseInt(txtCurrentSalesCount.getText())) < Integer
											.parseInt(txtSalesCountEdit.getText())) {

								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("�Ǹ� ���� ����");
								alert.setHeaderText("�Ǹż��� ������ ����");
								alert.setContentText("�Ǹż��� �������� ����� �ʰ��Ͽ����ϴ�.");
								alert.showAndWait();

							} else {

								// �ǸŴ�� �Ѽ��� �ʱ�ȭ
								int totalX = 0;
								// �ǸŴ�� �Ѿ� �ʱ�ȭ
								int totalY = 0;

								// ������ ������ ������ ����� �������� �������
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
								// ������ ������ ������ ����� �������� �������
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
							alert.setTitle("�Ǹ� ���� ����");
							alert.setHeaderText("������ �Ǹż����� ���ڷ� �Է��ϼ���");
							alert.setContentText("������ ������ �߸� �ԷµǾ����ϴ�.");
							alert.showAndWait();

						}

					});

					// ��� ��ư ����
					btnSalesCountEditExit.setOnAction(e2 -> {

						salesEditdialog.close();
					});
				} catch (Exception e1) {

				}

			});

			// �Ǹ� ��� ��� ����
			btnPreSalesDelete.setOnAction(e -> {

				// �ǸŴ�� �Ѽ��� �ʱ�ȭ
				int totalX = 0;
				// �ǸŴ�� �Ѿ� �ʱ�ȭ
				int totalY = 0;

				SalesDAO saDao = null;
				saDao = new SalesDAO();
				selectPreSales = PreSalesTableView.getSelectionModel().getSelectedItems();
				selectedIndex = PreSalesTableView.getSelectionModel().getSelectedIndex();
				String deletePreSales = selectPreSales.get(0).getSa_pnum();
				int deletePreSalesCount = selectPreSales.get(0).getSa_count();

				// int deleteNum = selectWareList.get(0).getNo();

				try {
					// ���� ��ư ������ �˶��޼��� �˾�����
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("�����޼���");
					alert.setHeaderText("������ �����ʹ� �����ϽǼ� �����ϴ�.");
					alert.setContentText("������ �����Ͻðڽ��ϱ�?");

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

						// �⺻�̹��� ǥ��
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
					System.out.println("�ǸŴ���� ������ư ���� ����" + e1);
				}

			});

			// �Ǹ� ��ư ��ɼ���
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

						System.out.println("�ǸŹ�ư����� �Ǹ� ���̺� ������ �Է¿��� ����â" + e1);

					}

				}

				preSalesData.removeAll(preSalesData);
				txtPreSalesToTalCount.clear();
				txtPreSalesToTalPrice.clear();

				btnPreSalesOk.setDisable(true);
				btnPreSalesDelete.setDisable(true);
				btnPreSalesEdit.setDisable(true);

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("�Ǹ� �Ϸ�");
				alert.setHeaderText("�Ǹ� / �Ǹ� ��� ��ϿϷ�");
				alert.setContentText("�Ǹ� / �ǸŸ�ϵ���� ���������� �Ϸ�Ǿ����ϴ�.");
				alert.showAndWait();

				// �⺻�̹��� ǥ��
				localUrl = "/image/default.gif";
				localImage = new Image(localUrl, false);
				SalesImageView.setFitWidth(300);
				SalesImageView.setFitHeight(300);
				SalesImageView.setImage(localImage);

			});

			// �Ǹ�â ����
			btnSalesExit.setOnAction(e -> {

				if (preSalesData.size() == 0) {

					salesdialog.close();

				} else {

					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("�Ǹ� ���� ����");
					alert.setHeaderText("�Ǹ� ������� ��ǰ�� �����մϴ�.");
					alert.setContentText("�Ǹ� ������� ����� ���� ������ �������ּ���");

					alert.showAndWait();
				}

			});

		} catch (

		Exception e) {

		}
	}

	// �ǸŸ�� ��ü ����Ʈ
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

	// �԰��� ��ü ����Ʈ
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

	// �ֹ� ��� ��ü ����Ʈ
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
