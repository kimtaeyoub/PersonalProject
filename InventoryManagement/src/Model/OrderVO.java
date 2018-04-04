package Model;

public class OrderVO {

	private int no; // 시퀀스번호
	private String o_num; // 주문 번호
	private String o_pnum; // 주문 품번
	private String o_pname; // 주문 품명
	private String o_pkind; // 주문 품종
	private String o_size; // 주문품 사이즈
	private String o_color; // 주문품 색상
	private int o_price; // 주문품 단가
	private int o_count; // 주문품 수량
	private int o_totalPrice; // 주문품 총 가격
	private String o_date; // 주문날짜
	private String o_fileName; // 주문품 이미지 파일명
	private int o_orderTotalCount; // 총 주문 수량
	private String o_orderTotalPrice; // 총 주문 합계금액

	public OrderVO() {
		super();
	}

	public OrderVO(int no, String o_num, String o_pnum, String o_pname, String o_pkind, String o_size, String o_color,
			int o_price, int o_count, int o_totalPrice, String o_fileName) {
		super();
		this.no = no;
		this.o_num = o_num;
		this.o_pnum = o_pnum;
		this.o_pname = o_pname;
		this.o_pkind = o_pkind;
		this.o_size = o_size;
		this.o_color = o_color;
		this.o_price = o_price;
		this.o_count = o_count;
		this.o_totalPrice = o_totalPrice;
		this.o_fileName = o_fileName;
	}

	public OrderVO(int no, String o_num, String o_pnum, String o_pname, String o_pkind, String o_size, String o_color,
			int o_price, int o_count, int o_totalPrice, String o_date, String o_fileName, int o_orderTotalCount,
			String o_orderTotalPrice) {
		super();
		this.no = no;
		this.o_num = o_num;
		this.o_pnum = o_pnum;
		this.o_pname = o_pname;
		this.o_pkind = o_pkind;
		this.o_size = o_size;
		this.o_color = o_color;
		this.o_price = o_price;
		this.o_count = o_count;
		this.o_totalPrice = o_totalPrice;
		this.o_date = o_date;
		this.o_fileName = o_fileName;
		this.o_orderTotalCount = o_orderTotalCount;
		this.o_orderTotalPrice = o_orderTotalPrice;
	}

	public OrderVO(int o_orderTotalCount, String o_orderTotalPrice) {
		super();
		this.o_orderTotalCount = o_orderTotalCount;
		this.o_orderTotalPrice = o_orderTotalPrice;
	}

	public OrderVO(int o_count) {
		super();
		this.o_count = o_count;
	}

	public OrderVO(int no, int o_price, int o_count) {
		super();
		this.no = no;
		this.o_price = o_price;
		this.o_count = o_count;
	}

	public OrderVO(int no, String o_num, String o_pnum, String o_pname, String o_pkind, String o_size, String o_color,
			int o_price, int o_count, int o_totalPrice, String o_date, String o_fileName) {
		super();
		this.no = no;
		this.o_num = o_num;
		this.o_pnum = o_pnum;
		this.o_pname = o_pname;
		this.o_pkind = o_pkind;
		this.o_size = o_size;
		this.o_color = o_color;
		this.o_price = o_price;
		this.o_count = o_count;
		this.o_totalPrice = o_totalPrice;
		this.o_date = o_date;
		this.o_fileName = o_fileName;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getO_num() {
		return o_num;
	}

	public void setO_num(String o_num) {
		this.o_num = o_num;
	}

	public String getO_pnum() {
		return o_pnum;
	}

	public void setO_pnum(String o_pnum) {
		this.o_pnum = o_pnum;
	}

	public String getO_pname() {
		return o_pname;
	}

	public void setO_pname(String o_pname) {
		this.o_pname = o_pname;
	}

	public String getO_pkind() {
		return o_pkind;
	}

	public void setO_pkind(String o_pkind) {
		this.o_pkind = o_pkind;
	}

	public String getO_size() {
		return o_size;
	}

	public void setO_size(String o_size) {
		this.o_size = o_size;
	}

	public String getO_color() {
		return o_color;
	}

	public void setO_color(String o_color) {
		this.o_color = o_color;
	}

	public String getO_date() {
		return o_date;
	}

	public void setO_date(String o_date) {
		this.o_date = o_date;
	}

	public String getO_fileName() {
		return o_fileName;
	}

	public void setO_fileName(String o_fileName) {
		this.o_fileName = o_fileName;
	}

	public int getO_orderTotalCount() {
		return o_orderTotalCount;
	}

	public void setO_orderTotalCount(int o_orderTotalCount) {
		this.o_orderTotalCount = o_orderTotalCount;
	}

	public String getO_orderTotalPrice() {
		return o_orderTotalPrice;
	}

	public void setO_orderTotalPrice(String o_orderTotalPrice) {
		this.o_orderTotalPrice = o_orderTotalPrice;
	}

	public int getO_price() {
		return o_price;
	}

	public void setO_price(int o_price) {
		this.o_price = o_price;
	}

	public int getO_count() {
		return o_count;
	}

	public void setO_count(int o_count) {
		this.o_count = o_count;
	}

	public int getO_totalPrice() {
		return o_totalPrice;
	}

	public void setO_totalPrice(int o_totalPrice) {
		this.o_totalPrice = o_totalPrice;
	}

}
