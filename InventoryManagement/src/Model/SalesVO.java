package Model;

public class SalesVO {

	private int no; // 시퀀스번호
	private String sa_num; // 판매 번호
	private String sa_pnum; // 판매 품번
	private String sa_pname; // 판매 품명
	private String sa_pkind; // 판매 품종
	private String sa_size; // 판매품 사이즈
	private String sa_color; // 판매품 색상
	private int sa_price; // 판매품 단가
	private int sa_count; // 판매품 수량
	private int sa_totalPrice; // 판매품 총 가격
	private String sa_date; // 판매날짜
	private String sa_fileName; // 판매품 이미지 파일명
	private String sa_CusName; // 구입 고객명
	private String sa_CusNum; // 구입 고객 연락처
	private int sa_salesTotalCount; // 총 판매 수량
	private String sa_salesTotalPrice; // 총 판매 합계금액

	public SalesVO() {
		super();
	}

	public SalesVO(String sa_pnum, String sa_pname, String sa_pkind, String sa_size, String sa_color, int sa_price,
			int sa_count, int sa_totalPrice, String sa_fileName, String sa_CusName, String sa_CusNum) {
		super();
		this.sa_pnum = sa_pnum;
		this.sa_pname = sa_pname;
		this.sa_pkind = sa_pkind;
		this.sa_size = sa_size;
		this.sa_color = sa_color;
		this.sa_price = sa_price;
		this.sa_count = sa_count;
		this.sa_totalPrice = sa_totalPrice;
		this.sa_fileName = sa_fileName;
		this.sa_CusName = sa_CusName;
		this.sa_CusNum = sa_CusNum;
	}

	public SalesVO(String sa_pnum, String sa_pname, String sa_pkind, String sa_size, String sa_color, int sa_price,
			String sa_fileName, String sa_CusName, String sa_CusNum) {
		super();
		this.sa_pnum = sa_pnum;
		this.sa_pname = sa_pname;
		this.sa_pkind = sa_pkind;
		this.sa_size = sa_size;
		this.sa_color = sa_color;
		this.sa_price = sa_price;
		this.sa_fileName = sa_fileName;
		this.sa_CusName = sa_CusName;
		this.sa_CusNum = sa_CusNum;
	}

	public SalesVO(int sa_salesTotalCount, String sa_salesTotalPrice) {
		super();
		this.sa_salesTotalCount = sa_salesTotalCount;
		this.sa_salesTotalPrice = sa_salesTotalPrice;
	}

	public SalesVO(int no, String sa_num, String sa_pnum, String sa_pname, String sa_pkind, String sa_size,
			String sa_color, int sa_price, int sa_count, int sa_totalPrice, String sa_date, String sa_fileName,
			String sa_CusName, String sa_CusNum) {
		super();
		this.no = no;
		this.sa_num = sa_num;
		this.sa_pnum = sa_pnum;
		this.sa_pname = sa_pname;
		this.sa_pkind = sa_pkind;
		this.sa_size = sa_size;
		this.sa_color = sa_color;
		this.sa_price = sa_price;
		this.sa_count = sa_count;
		this.sa_totalPrice = sa_totalPrice;
		this.sa_date = sa_date;
		this.sa_fileName = sa_fileName;
		this.sa_CusName = sa_CusName;
		this.sa_CusNum = sa_CusNum;
	}

	public SalesVO(int no, String sa_num, String sa_pnum, String sa_pname, String sa_pkind, String sa_size,
			String sa_color, int sa_price, int sa_count, int sa_totalPrice, String sa_date, String sa_fileName,
			int sa_salesTotalCount, String sa_salesTotalPrice) {
		super();
		this.no = no;
		this.sa_num = sa_num;
		this.sa_pnum = sa_pnum;
		this.sa_pname = sa_pname;
		this.sa_pkind = sa_pkind;
		this.sa_size = sa_size;
		this.sa_color = sa_color;
		this.sa_price = sa_price;
		this.sa_count = sa_count;
		this.sa_totalPrice = sa_totalPrice;
		this.sa_date = sa_date;
		this.sa_fileName = sa_fileName;
		this.sa_salesTotalCount = sa_salesTotalCount;
		this.sa_salesTotalPrice = sa_salesTotalPrice;
	}

	public SalesVO(int no, String sa_num, String sa_pnum, String sa_pname, String sa_pkind, String sa_size,
			String sa_color, int sa_price, int sa_count, int sa_totalPrice, String sa_date, String sa_fileName,
			String sa_CusName, String sa_CusNum, int sa_salesTotalCount, String sa_salesTotalPrice) {
		super();
		this.no = no;
		this.sa_num = sa_num;
		this.sa_pnum = sa_pnum;
		this.sa_pname = sa_pname;
		this.sa_pkind = sa_pkind;
		this.sa_size = sa_size;
		this.sa_color = sa_color;
		this.sa_price = sa_price;
		this.sa_count = sa_count;
		this.sa_totalPrice = sa_totalPrice;
		this.sa_date = sa_date;
		this.sa_fileName = sa_fileName;
		this.sa_CusName = sa_CusName;
		this.sa_CusNum = sa_CusNum;
		this.sa_salesTotalCount = sa_salesTotalCount;
		this.sa_salesTotalPrice = sa_salesTotalPrice;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getSa_num() {
		return sa_num;
	}

	public void setSa_num(String sa_num) {
		this.sa_num = sa_num;
	}

	public String getSa_pnum() {
		return sa_pnum;
	}

	public void setSa_pnum(String sa_pnum) {
		this.sa_pnum = sa_pnum;
	}

	public String getSa_pname() {
		return sa_pname;
	}

	public void setSa_pname(String sa_pname) {
		this.sa_pname = sa_pname;
	}

	public String getSa_pkind() {
		return sa_pkind;
	}

	public void setSa_pkind(String sa_pkind) {
		this.sa_pkind = sa_pkind;
	}

	public String getSa_size() {
		return sa_size;
	}

	public void setSa_size(String sa_size) {
		this.sa_size = sa_size;
	}

	public String getSa_color() {
		return sa_color;
	}

	public void setSa_color(String sa_color) {
		this.sa_color = sa_color;
	}

	public int getSa_price() {
		return sa_price;
	}

	public void setSa_price(int sa_price) {
		this.sa_price = sa_price;
	}

	public int getSa_count() {
		return sa_count;
	}

	public void setSa_count(int sa_count) {
		this.sa_count = sa_count;
	}

	public int getSa_totalPrice() {
		return sa_totalPrice;
	}

	public void setSa_totalPrice(int sa_totalPrice) {
		this.sa_totalPrice = sa_totalPrice;
	}

	public String getSa_date() {
		return sa_date;
	}

	public void setSa_date(String sa_date) {
		this.sa_date = sa_date;
	}

	public String getSa_fileName() {
		return sa_fileName;
	}

	public void setSa_fileName(String sa_fileName) {
		this.sa_fileName = sa_fileName;
	}

	public int getSa_salesTotalCount() {
		return sa_salesTotalCount;
	}

	public void setSa_salesTotalCount(int sa_salesTotalCount) {
		this.sa_salesTotalCount = sa_salesTotalCount;
	}

	public String getSa_salesTotalPrice() {
		return sa_salesTotalPrice;
	}

	public void setSa_salesTotalPrice(String sa_salesTotalPrice) {
		this.sa_salesTotalPrice = sa_salesTotalPrice;
	}

	public String getSa_CusName() {
		return sa_CusName;
	}

	public void setSa_CusName(String sa_CusName) {
		this.sa_CusName = sa_CusName;
	}

	public String getSa_CusNum() {
		return sa_CusNum;
	}

	public void setSa_CusNum(String sa_CusNum) {
		this.sa_CusNum = sa_CusNum;
	}

}
