package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.OrderVO;
import Model.WarehousingVO;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class WarehousingDAO {

	// �ű� �԰� ��� ���
	public WarehousingVO inputWarehousing(OrderVO ovo) throws Exception {

		// slq�� �Է�
		StringBuffer sql = new StringBuffer();
		sql.append("insert into WAREHOUSING ");
		sql.append("(no,w_num,w_pnum,w_pname,w_pkind,w_size,w_color,w_price,w_count,w_totalprice,w_date,w_filename)");
		sql.append("values(WAREHOUSING_seq.nextval,?,?,?,?,?,?,?,?,?,sysdate,?)");

		// �����ͺ��̽��� sql�� ����
		Connection con = null;
		PreparedStatement pstmt = null;
		WarehousingVO wvo = null;
		try {
			con = DBUtil.getConnection();

			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, ovo.getO_num());
			pstmt.setString(2, ovo.getO_pnum());
			pstmt.setString(3, ovo.getO_pname());
			pstmt.setString(4, ovo.getO_pkind());
			pstmt.setString(5, ovo.getO_size());
			pstmt.setString(6, ovo.getO_color());
			pstmt.setInt(7, ovo.getO_price());
			pstmt.setInt(8, ovo.getO_count());
			pstmt.setInt(9, ovo.getO_totalPrice());
			pstmt.setString(10, ovo.getO_fileName());

			int i = pstmt.executeUpdate();

		} catch (SQLException se) {
			System.out.println("�ű� �԰� ��� sql ���� ����" + se);

		} catch (Exception e) {
			System.out.println("�ű� �԰� ��� �������" + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {

			}
		}

		return wvo;

	}

	// �԰��� ��ü ����Ʈ
	public ArrayList<WarehousingVO> getWareTotal() {
		ArrayList<WarehousingVO> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		sql.append("select no,w_num,w_pnum,w_pname,w_pkind,w_size,w_color,w_price,w_count, ");
		sql.append("w_totalprice,w_date,w_filename from warehousing order by no");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		WarehousingVO wVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				wVo = new WarehousingVO();
				wVo.setNo(rs.getInt("no"));
				wVo.setW_num(rs.getString("w_num"));
				wVo.setW_pnum(rs.getString("w_pnum"));
				wVo.setW_pname(rs.getString("w_pname"));
				wVo.setW_pkind(rs.getString("w_pkind"));
				wVo.setW_size(rs.getString("w_size"));
				wVo.setW_color(rs.getString("w_color"));
				wVo.setW_price(rs.getInt("w_price"));
				wVo.setW_count(rs.getInt("w_count"));
				wVo.setW_totalPrice(rs.getInt("w_totalPrice"));
				wVo.setW_date(rs.getString("w_date"));
				wVo.setW_fileName(rs.getString("w_filename"));

				list.add(wVo);

			}

		} catch (SQLException se) {
			System.out.println("��ü�԰��� ����Ʈ sql�� ����" + se);

		} catch (Exception e) {
			System.out.println("��ü�԰��� ����Ʈ ���ÿ���" + e);

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}

				if (pstmt != null) {
					pstmt.close();
				}

				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {

			}
		}

		return list;

	}

	/**************************************************
	 * 
	 * ���� �κ� ��ǰ ���̺��� �÷� ���������ϴ� SQL ���úκ� ���� 1�� �ȵ�
	 * 
	 ****************************************************/
	// �����ͺ��̽� �ֹ� ���̺��� �÷�����
	public ArrayList<String> getWareColumnName() {
		ArrayList<String> columnName = new ArrayList<>();

		StringBuffer sql = new StringBuffer();
		sql.append("select * from warehousing");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// �̺κ� 1�� ���ؾȵ�
		ResultSetMetaData rsmd = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();

			for (int i = 1; i <= cols; i++) {
				columnName.add(rsmd.getColumnName(i));
			}

		} catch (SQLException se) {

			System.out.println("�԰������̺��÷����� ���� SQL�� ����" + se);

		} catch (Exception e) {
			System.out.println("�԰��� �÷����� ���� ���� ���� " + e);

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}

				if (pstmt != null) {
					pstmt.close();
				}

				if (con != null) {
					con.close();
				}

			} catch (SQLException se) {

			}

		}
		return columnName;
	}

	// �԰� ����� ���� ������ ��ǰ�� �̹� �԰�Ǿ����� �˻��ϱ����� DB���� �˻�
	public int searchWareWnum(String wnum) throws Exception {

		int i = 1;

		StringBuffer sql = new StringBuffer();
		sql.append("select * from warehousing where w_num=? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		con = DBUtil.getConnection();

		pstmt = con.prepareStatement(sql.toString());
		pstmt.setString(1, wnum);

		rs = pstmt.executeQuery();

		if (!rs.next()) {
			i = 0;
		} else {

		}

		return i;

	}

	// �԰��ϵ� ��ǰ�� ������̺�� ������ �Ѿ� ����
	public void inputOrderToStock(String pnum,int num) {

		StringBuffer sql = new StringBuffer();
		sql.append(" update stock set st_count = (select st_count+o_count ���� ");
		sql.append(" from stock s,orderlist o");
		sql.append(" where st_pnum = o_pnum and o.no=? ), ");
		sql.append(" st_totalprice = (select (st_count+o_count)*st_price �Ѱ��� ");
		sql.append(" from stock s, orderlist o ");
		sql.append(" where st_pnum = o_pnum and o.no=? ) ");
		sql.append(" where st_pnum = ? ");

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, num);
			pstmt.setInt(2, num);
			pstmt.setString(3, pnum);

			int i = pstmt.executeUpdate();

		} catch (SQLException se) {
			System.out.println("�԰��Ͻ� ��� ���� sql������" + se);
		} catch (Exception e) {
			System.out.println("�԰��Ͻ� ��� ���� �������" + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {

			}
		}

	}

	// ������ �԰� ���� ����
	public void getWareListDelete(int no) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("delete from warehousing where no=?");

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DBUtil.getConnection();

			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, no);

			int i = pstmt.executeUpdate();

			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("�԰� �̷� ����");
				alert.setHeaderText("�԰� �̷� ���� �Ϸ�");
				alert.setContentText("���������� �԰��̷��� �����Ǿ����ϴ�.");

				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("�԰� �̷� ����");
				alert.setHeaderText("�԰� �̷� ���� ����");
				alert.setContentText("�԰��̷� ������ �����Ͽ����ϴ�");

				alert.showAndWait();
			}

		} catch (SQLException se) {
			System.out.println("�԰��̷»��� sql ������" + se);

		} catch (Exception e) {

			System.out.println("�԰��̷»��� �������" + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {

			}
		}
	}

	// �԰� ��Ͽ��� ������ ��� ��Ͽ����� �����ǵ��� ����
	public void deleteWareToStock(String pnum,int num) {

		StringBuffer sql = new StringBuffer();
		sql.append(" update stock set st_count = (select st_count-w_count ���� ");
		sql.append(" from stock s,warehousing w");
		sql.append(" where st_pnum = w_pnum and w.no= ?), ");
		sql.append(" st_totalprice = (select (st_count-w_count)*st_price �Ѱ��� ");
		sql.append(" from stock s, warehousing w");
		sql.append(" where st_pnum = w_pnum and w.no= ?) ");
		sql.append(" where st_pnum = ? ");

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, num);
			pstmt.setInt(2, num);
			pstmt.setString(3, pnum);

			int i = pstmt.executeUpdate();

		} catch (SQLException se) {
			System.out.println("�԰��� ������ ��� ���� sql������" + se);
		} catch (Exception e) {
			System.out.println("�԰������ ��� ���� �������" + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {

			}
		}

	}

	// �԰�ǰ �� �ֹ���, ���վ� ���ϱ�
	public WarehousingVO getWareTotalCount(WarehousingVO wvo) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(w_count) �Ѽ���,to_char(sum(w_totalprice),'l999,999,999') ���� from warehousing ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		WarehousingVO wVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());

			rs = pstmt.executeQuery();

			if (rs.next()) {
				wVo = new WarehousingVO();
				wVo.setW_orderTotalCount(rs.getInt("�Ѽ���"));
				wVo.setW_orderTotalPrice(rs.getString("����"));
			}
		} catch (SQLException se) {
			System.out.println("�԰� �Ѽ���/���� SQL���� ����" + se);
		} catch (Exception e) {
			System.out.println("�԰� �Ѽ���/���� ������� ����" + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {

			}
		}

		return wVo;
	}

	// �⵵ �˻����� ��ȸ�� ��ǰ�� �� ���/���հ� ���ϱ�
	public WarehousingVO getWareYearTotalCount(String year) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(w_count) �Ѽ���,to_char(sum(w_totalprice),'l999,999,999') ���� from warehousing ");
		sql.append("where to_char(w_date,'yyyy') like ? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		WarehousingVO wVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, year);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				wVo = new WarehousingVO();
				wVo.setW_orderTotalCount(rs.getInt("�Ѽ���"));
				wVo.setW_orderTotalPrice(rs.getString("����"));
			}
		} catch (SQLException se) {
			System.out.println("�԰��� �⵵�˻� �Ѽ���/���� SQL���� ����" + se);
		} catch (Exception e) {
			System.out.println("�԰��� �⵵�˻� �Ѽ���/���� ������� ����" + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {

			}
		}

		return wVo;
	}

	// �� �˻����� ��ȸ�� ��ǰ�� �� ���/���հ� ���ϱ�
	public WarehousingVO getWareMonthTotalCount(String month) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(w_count) �Ѽ���,to_char(sum(w_totalprice),'l999,999,999') ���� from warehousing ");
		sql.append("where to_char(w_date,'mm') like ? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		WarehousingVO wVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, month);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				wVo = new WarehousingVO();
				wVo.setW_orderTotalCount(rs.getInt("�Ѽ���"));
				wVo.setW_orderTotalPrice(rs.getString("����"));
			}
		} catch (SQLException se) {
			System.out.println("�԰��� ���˻� �Ѽ���/���� SQL���� ����" + se);
		} catch (Exception e) {
			System.out.println("�԰��� ���˻� �Ѽ���/���� ������� ����" + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {

			}
		}

		return wVo;
	}

	// �⵵ + �� �˻����� ��ȸ�� ��ǰ�� �� ���/���հ� ���ϱ�
	public WarehousingVO getWareYearMonthTotalCount(String year, String month) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(w_count) �Ѽ���,to_char(sum(w_totalprice),'l999,999,999') ���� from warehousing ");
		sql.append("where to_char(w_date,'yyyy-mm') like ? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		WarehousingVO wVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, year + "-" + month);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				wVo = new WarehousingVO();
				wVo.setW_orderTotalCount(rs.getInt("�Ѽ���"));
				wVo.setW_orderTotalPrice(rs.getString("����"));
			}
		} catch (SQLException se) {
			System.out.println("�԰��� ��+���˻� �Ѽ���/���� SQL���� ����" + se);
		} catch (Exception e) {
			System.out.println("�԰��� ��+���˻� �Ѽ���/���� ������� ����" + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {

			}
		}

		return wVo;
	}

	// �⵵������ �԰��̷¸�Ͽ��� �˻�
	public WarehousingVO getWareListSearchYear(String year) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from warehousing where to_char(w_date,'yyyy') like ? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		WarehousingVO wVo = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, year);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				wVo = new WarehousingVO();
				wVo.setNo(rs.getInt("no"));
				wVo.setW_num(rs.getString("w_num"));
				wVo.setW_pnum(rs.getString("w_pnum"));
				wVo.setW_pname(rs.getString("w_pname"));
				wVo.setW_pkind(rs.getString("w_pkind"));
				wVo.setW_size(rs.getString("w_size"));
				wVo.setW_color(rs.getString("w_color"));
				wVo.setW_price(rs.getInt("w_price"));
				wVo.setW_count(rs.getInt("w_count"));
				wVo.setW_totalPrice(rs.getInt("w_totalPrice"));
				wVo.setW_date(rs.getString("w_date"));
				wVo.setW_fileName(rs.getString("w_filename"));
			}
		} catch (SQLException se) {
			System.out.println("�⵵�� �԰�����ȸ�κ� SQL�� ����" + se);
		} catch (Exception e) {
			System.out.println("�⵵�� �԰��Ϻκ� �������" + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {

			}
		}

		return wVo;

	}

	// �������� �ֹ��̷¸�Ͽ��� �˻�
	public WarehousingVO getWareListSearchMonth(String month) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from warehousing where to_char(w_date,'mm') like ? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		WarehousingVO wVo = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, month);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				wVo = new WarehousingVO();
				wVo.setNo(rs.getInt("no"));
				wVo.setW_num(rs.getString("w_num"));
				wVo.setW_pnum(rs.getString("w_pnum"));
				wVo.setW_pname(rs.getString("w_pname"));
				wVo.setW_pkind(rs.getString("w_pkind"));
				wVo.setW_size(rs.getString("w_size"));
				wVo.setW_color(rs.getString("w_color"));
				wVo.setW_price(rs.getInt("w_price"));
				wVo.setW_count(rs.getInt("w_count"));
				wVo.setW_totalPrice(rs.getInt("w_totalPrice"));
				wVo.setW_date(rs.getString("w_date"));
				wVo.setW_fileName(rs.getString("w_filename"));
			}
		} catch (SQLException se) {
			System.out.println("���� �԰�����ȸ�κ� SQL�� ����" + se);
		} catch (Exception e) {
			System.out.println("���� �԰�����ȸ�κ� �������" + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {

			}
		}

		return wVo;

	}

	// �⵵�� �� ��� �����ϴ� �ֹ����� �ֹ��̷¸�Ͽ��� �˻�
	public WarehousingVO getWareListSearchYearMonth(String year, String month) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from warehousing where to_char(w_date,'yyyy-mm') like ? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		WarehousingVO wVo = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, year + "-" + month);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				wVo = new WarehousingVO();
				wVo.setNo(rs.getInt("no"));
				wVo.setW_num(rs.getString("w_num"));
				wVo.setW_pnum(rs.getString("w_pnum"));
				wVo.setW_pname(rs.getString("w_pname"));
				wVo.setW_pkind(rs.getString("w_pkind"));
				wVo.setW_size(rs.getString("w_size"));
				wVo.setW_color(rs.getString("w_color"));
				wVo.setW_price(rs.getInt("w_price"));
				wVo.setW_count(rs.getInt("w_count"));
				wVo.setW_totalPrice(rs.getInt("w_totalPrice"));
				wVo.setW_date(rs.getString("w_date"));
				wVo.setW_fileName(rs.getString("w_filename"));
			}
		} catch (SQLException se) {
			System.out.println("��+���� �԰�����ȸ�κ� SQL�� ����" + se);
		} catch (Exception e) {
			System.out.println("��+���� �԰�����ȸ�κ� �������" + e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {

			}
		}

		return wVo;

	}

}
