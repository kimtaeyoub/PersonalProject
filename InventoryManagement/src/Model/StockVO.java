package Model;

public class StockVO {

	private int no; // 재고 일련번호
	private String st_pnum; // 품번
	private String st_pname; // 품명
	private String st_pkind; // 품종
	private String st_size; // 사이즈
	private String st_color; // 컬러
	private int st_price; // 단가
	private int st_count; // 수량
	private int st_totalPrice; // 총가격
	private String st_filename; // 이미지 파일주소
	private int st_stockTotalCount; // 총 재고량
	private String st_stockTotalPrice; // 총 재고 가격

	public StockVO() {
	}

	public StockVO(int st_count) {
		super();
		this.st_count = st_count;
	}

	public StockVO(int st_stockTotalCount, String st_stockTotalPrice) {
		super();
		this.st_stockTotalCount = st_stockTotalCount;
		this.st_stockTotalPrice = st_stockTotalPrice;
	}

	public StockVO(int no, String st_pnum, int st_count) {
		super();
		this.no = no;
		this.st_pnum = st_pnum;
		this.st_count = st_count;
	}

	public StockVO(int no, String st_pnum, String st_pname, String st_pkind, String st_size, String st_color,
			int st_price, int st_count, int st_totalPrice, String st_filename, int st_stockTotalCount,
			String st_stockTotalPrice) {
		super();
		this.no = no;
		this.st_pnum = st_pnum;
		this.st_pname = st_pname;
		this.st_pkind = st_pkind;
		this.st_size = st_size;
		this.st_color = st_color;
		this.st_price = st_price;
		this.st_count = st_count;
		this.st_totalPrice = st_totalPrice;
		this.st_filename = st_filename;
		this.st_stockTotalCount = st_stockTotalCount;
		this.st_stockTotalPrice = st_stockTotalPrice;
	}

	public StockVO(int no, String st_pnum, String st_pname, String st_pkind, String st_size, String st_color,
			int st_price, int st_count, int st_totalPrice) {
		super();
		this.no = no;
		this.st_pnum = st_pnum;
		this.st_pname = st_pname;
		this.st_pkind = st_pkind;
		this.st_size = st_size;
		this.st_color = st_color;
		this.st_price = st_price;
		this.st_count = st_count;
		this.st_totalPrice = st_totalPrice;
	}

	public StockVO(String st_pnum, String st_pname, String st_pkind, String st_size, String st_color, int st_price) {
		super();
		this.st_pnum = st_pnum;
		this.st_pname = st_pname;
		this.st_pkind = st_pkind;
		this.st_size = st_size;
		this.st_color = st_color;
		this.st_price = st_price;
	}

	public StockVO(String st_pnum, String st_pname, String st_pkind, String st_size, String st_color, int st_price,
			String st_filename) {
		super();
		this.st_pnum = st_pnum;
		this.st_pname = st_pname;
		this.st_pkind = st_pkind;
		this.st_size = st_size;
		this.st_color = st_color;
		this.st_price = st_price;
		this.st_filename = st_filename;
	}

	public StockVO(String st_pnum, String st_pname, String st_pkind, String st_size, String st_color, int st_price,
			int st_count, int st_totalPrice, String st_filename) {
		super();
		this.st_pnum = st_pnum;
		this.st_pname = st_pname;
		this.st_pkind = st_pkind;
		this.st_size = st_size;
		this.st_color = st_color;
		this.st_price = st_price;
		this.st_count = st_count;
		this.st_totalPrice = st_totalPrice;
		this.st_filename = st_filename;
	}

	public StockVO(int no, String st_pnum, String st_pname, String st_pkind, String st_size, String st_color,
			int st_price, int st_count, int st_totalPrice, String st_filename) {
		super();
		this.no = no;
		this.st_pnum = st_pnum;
		this.st_pname = st_pname;
		this.st_pkind = st_pkind;
		this.st_size = st_size;
		this.st_color = st_color;
		this.st_price = st_price;
		this.st_count = st_count;
		this.st_totalPrice = st_totalPrice;
		this.st_filename = st_filename;
	}

	public StockVO(int no, String st_pnum, String st_pname, String st_pkind, String st_size, String st_color,
			int st_price) {
		super();
		this.no = no;
		this.st_pnum = st_pnum;
		this.st_pname = st_pname;
		this.st_pkind = st_pkind;
		this.st_size = st_size;
		this.st_color = st_color;
		this.st_price = st_price;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getSt_pnum() {
		return st_pnum;
	}

	public void setSt_pnum(String st_pnum) {
		this.st_pnum = st_pnum;
	}

	public String getSt_pname() {
		return st_pname;
	}

	public void setSt_pname(String st_pname) {
		this.st_pname = st_pname;
	}

	public String getSt_pkind() {
		return st_pkind;
	}

	public void setSt_pkind(String st_pkind) {
		this.st_pkind = st_pkind;
	}

	public String getSt_size() {
		return st_size;
	}

	public void setSt_size(String st_size) {
		this.st_size = st_size;
	}

	public String getSt_color() {
		return st_color;
	}

	public void setSt_color(String st_color) {
		this.st_color = st_color;
	}

	public int getSt_price() {
		return st_price;
	}

	public void setSt_price(int st_price) {
		this.st_price = st_price;
	}

	public int getSt_count() {
		return st_count;
	}

	public void setSt_count(int st_count) {
		this.st_count = st_count;
	}

	public int getSt_totalPrice() {
		return st_totalPrice;
	}

	public void setSt_totalPrice(int st_totalPrice) {
		this.st_totalPrice = st_totalPrice;
	}

	public String getSt_filename() {
		return st_filename;
	}

	public void setSt_filename(String st_filename) {
		this.st_filename = st_filename;
	}

	public int getSt_stockTotalCount() {
		return st_stockTotalCount;
	}

	public void setSt_stockTotalCount(int st_stockTotalCount) {
		this.st_stockTotalCount = st_stockTotalCount;
	}

	public String getSt_stockTotalPrice() {
		return st_stockTotalPrice;
	}

	public void setSt_stockTotalPrice(String st_stockTotalPrice) {
		this.st_stockTotalPrice = st_stockTotalPrice;
	}

}
