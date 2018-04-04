package Model;

public class WarehousingVO {

	private int no; // ��������ȣ
	private String w_num; // �԰� ��ȣ
	private String w_pnum; // �԰� ǰ��
	private String w_pname; // �԰� ǰ��
	private String w_pkind; // �԰� ǰ��
	private String w_size; // �԰�ǰ ������
	private String w_color; // �԰�ǰ ����
	private int w_price; // �԰�ǰ �ܰ�
	private int w_count; // �԰�ǰ ����
	private int w_totalPrice; // �԰�ǰ �� ����
	private String w_date; // �԰�¥
	private String w_fileName; // �԰�ǰ �̹��� ���ϸ�
	private int w_orderTotalCount; // �� �԰� ����
	private String w_orderTotalPrice; // �� �԰� �հ�ݾ�

	public WarehousingVO() {
		super();
	}

	public WarehousingVO(int w_orderTotalCount, String w_orderTotalPrice) {
		super();
		this.w_orderTotalCount = w_orderTotalCount;
		this.w_orderTotalPrice = w_orderTotalPrice;
	}

	public WarehousingVO(String w_num, String w_pnum, String w_pname, String w_pkind, String w_size, String w_color,
			int w_price, int w_count, int w_totalPrice, String w_date, String w_fileName) {
		super();
		this.w_num = w_num;
		this.w_pnum = w_pnum;
		this.w_pname = w_pname;
		this.w_pkind = w_pkind;
		this.w_size = w_size;
		this.w_color = w_color;
		this.w_price = w_price;
		this.w_count = w_count;
		this.w_totalPrice = w_totalPrice;
		this.w_date = w_date;
		this.w_fileName = w_fileName;
	}

	public WarehousingVO(int no, String w_num, String w_pnum, String w_pname, String w_pkind, String w_size,
			String w_color, int w_price, int w_count, int w_totalPrice, String w_date, String w_fileName) {
		super();
		this.no = no;
		this.w_num = w_num;
		this.w_pnum = w_pnum;
		this.w_pname = w_pname;
		this.w_pkind = w_pkind;
		this.w_size = w_size;
		this.w_color = w_color;
		this.w_price = w_price;
		this.w_count = w_count;
		this.w_totalPrice = w_totalPrice;
		this.w_date = w_date;
		this.w_fileName = w_fileName;
	}

	public WarehousingVO(int no, String w_num, String w_pnum, String w_pname, String w_pkind, String w_size,
			String w_color, int w_price, int w_count, int w_totalPrice, String w_date, String w_fileName,
			int w_orderTotalCount, String w_orderTotalPrice) {
		super();
		this.no = no;
		this.w_num = w_num;
		this.w_pnum = w_pnum;
		this.w_pname = w_pname;
		this.w_pkind = w_pkind;
		this.w_size = w_size;
		this.w_color = w_color;
		this.w_price = w_price;
		this.w_count = w_count;
		this.w_totalPrice = w_totalPrice;
		this.w_date = w_date;
		this.w_fileName = w_fileName;
		this.w_orderTotalCount = w_orderTotalCount;
		this.w_orderTotalPrice = w_orderTotalPrice;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getW_num() {
		return w_num;
	}

	public void setW_num(String w_num) {
		this.w_num = w_num;
	}

	public String getW_pnum() {
		return w_pnum;
	}

	public void setW_pnum(String w_pnum) {
		this.w_pnum = w_pnum;
	}

	public String getW_pname() {
		return w_pname;
	}

	public void setW_pname(String w_pname) {
		this.w_pname = w_pname;
	}

	public String getW_pkind() {
		return w_pkind;
	}

	public void setW_pkind(String w_pkind) {
		this.w_pkind = w_pkind;
	}

	public String getW_size() {
		return w_size;
	}

	public void setW_size(String w_size) {
		this.w_size = w_size;
	}

	public String getW_color() {
		return w_color;
	}

	public void setW_color(String w_color) {
		this.w_color = w_color;
	}

	public int getW_price() {
		return w_price;
	}

	public void setW_price(int w_price) {
		this.w_price = w_price;
	}

	public int getW_count() {
		return w_count;
	}

	public void setW_count(int w_count) {
		this.w_count = w_count;
	}

	public int getW_totalPrice() {
		return w_totalPrice;
	}

	public void setW_totalPrice(int w_totalPrice) {
		this.w_totalPrice = w_totalPrice;
	}

	public String getW_date() {
		return w_date;
	}

	public void setW_date(String w_date) {
		this.w_date = w_date;
	}

	public String getW_fileName() {
		return w_fileName;
	}

	public void setW_fileName(String w_fileName) {
		this.w_fileName = w_fileName;
	}

	public int getW_orderTotalCount() {
		return w_orderTotalCount;
	}

	public void setW_orderTotalCount(int w_orderTotalCount) {
		this.w_orderTotalCount = w_orderTotalCount;
	}

	public String getW_orderTotalPrice() {
		return w_orderTotalPrice;
	}

	public void setW_orderTotalPrice(String w_orderTotalPrice) {
		this.w_orderTotalPrice = w_orderTotalPrice;
	}

}
