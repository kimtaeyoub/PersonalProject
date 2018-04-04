package Model;

public class OrderVO {

	private int no; // ��������ȣ
	private String o_num; // �ֹ� ��ȣ
	private String o_pnum; // �ֹ� ǰ��
	private String o_pname; // �ֹ� ǰ��
	private String o_pkind; // �ֹ� ǰ��
	private String o_size; // �ֹ�ǰ ������
	private String o_color; // �ֹ�ǰ ����
	private int o_price; // �ֹ�ǰ �ܰ�
	private int o_count; // �ֹ�ǰ ����
	private int o_totalPrice; // �ֹ�ǰ �� ����
	private String o_date; // �ֹ���¥
	private String o_fileName; // �ֹ�ǰ �̹��� ���ϸ�
	private int o_orderTotalCount; // �� �ֹ� ����
	private String o_orderTotalPrice; // �� �ֹ� �հ�ݾ�

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
