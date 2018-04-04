package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.OrderVO;
import Model.StockVO;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class OrderDAO {

	// �ű� �ֹ� ��ǰ �ֹ��̷� DB�� ���
	public OrderVO inputNewOrder(StockVO svo, int count) throws Exception {

		// ����Ŭ���� ������ ó���� �ϱ����� sql������
		StringBuffer sql = new StringBuffer();
		sql.append("insert into orderlist");
		sql.append("(no,o_num,o_pnum,o_pname,o_pkind,o_size,o_color,o_price,o_count,o_totalprice,o_date,o_filename)");
		sql.append("values(order_seq.nextval,?,?,?,?,?,?,?,?,?,sysdate,?)");

		Connection con = null;
		PreparedStatement pstmt = null;
		OrderVO oVo = null;

		try {
			con = DBUtil.getConnection();

			pstmt = con.prepareStatement(sql.toString());

			pstmt.setString(1, "2017" + (int) (Math.random() * 100) + svo.getSt_pnum());
			pstmt.setString(2, svo.getSt_pnum());
			pstmt.setString(3, svo.getSt_pname());
			pstmt.setString(4, svo.getSt_pkind());
			pstmt.setString(5, svo.getSt_size());
			pstmt.setString(6, svo.getSt_color());
			pstmt.setInt(7, svo.getSt_price());
			pstmt.setInt(8, count);
			pstmt.setInt(9, svo.getSt_price() * count);
			pstmt.setString(10, svo.getSt_filename());

			int i = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("�ֹ���� �Է� SQL�� �������" + e);

		} catch (Exception e) {
			System.out.println("�ֹ���� �Է� �������" + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {

			}
		}

		return oVo;
	}

	// �ֹ���� ��ü ����Ʈ
	public ArrayList<OrderVO> getOrderTotal() {
		ArrayList<OrderVO> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		sql.append("select no,o_num,o_pnum,o_pname,o_pkind,o_size,o_color,o_price,o_count, ");
		sql.append("o_totalprice,o_date,o_filename from orderlist order by no");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OrderVO oVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				oVo = new OrderVO();
				oVo.setNo(rs.getInt("no"));
				oVo.setO_num(rs.getString("o_num"));
				oVo.setO_pnum(rs.getString("o_pnum"));
				oVo.setO_pname(rs.getString("o_pname"));
				oVo.setO_pkind(rs.getString("o_pkind"));
				oVo.setO_size(rs.getString("o_size"));
				oVo.setO_color(rs.getString("o_color"));
				oVo.setO_price(rs.getInt("o_price"));
				oVo.setO_count(rs.getInt("o_count"));
				oVo.setO_totalPrice(rs.getInt("o_totalPrice"));
				oVo.setO_date(rs.getString("o_date"));
				oVo.setO_fileName(rs.getString("o_filename"));

				list.add(oVo);

			}

		} catch (SQLException se) {
			System.out.println("��ü��� ����Ʈ sql�� ����" + se);

		} catch (Exception e) {
			System.out.println("��ü��� ����Ʈ ���ÿ���" + e);

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
	public ArrayList<String> getOrderColumnName() {
		ArrayList<String> columnName = new ArrayList<>();

		StringBuffer sql = new StringBuffer();
		sql.append("select * from orderlist");

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

			System.out.println("�ֹ����̺��÷����� ���� SQL�� ����" + se);

		} catch (Exception e) {
			System.out.println("�ֹ����̺� �÷����� ���� ���� ���� " + e);

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

	// �⵵������ �ֹ��̷¸�Ͽ��� �˻�
	public OrderVO getOrderListSearchYear(String year) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from orderlist where to_char(o_date,'yyyy') like ? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OrderVO oVo = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, year);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				oVo = new OrderVO();
				oVo.setNo(rs.getInt("no"));
				oVo.setO_num(rs.getString("o_num"));
				oVo.setO_pnum(rs.getString("o_pnum"));
				oVo.setO_pname(rs.getString("o_pname"));
				oVo.setO_pkind(rs.getString("o_pkind"));
				oVo.setO_size(rs.getString("o_size"));
				oVo.setO_color(rs.getString("o_color"));
				oVo.setO_price(rs.getInt("o_price"));
				oVo.setO_count(rs.getInt("o_count"));
				oVo.setO_totalPrice(rs.getInt("o_totalPrice"));
				oVo.setO_date(rs.getString("o_date"));
				oVo.setO_fileName(rs.getString("o_filename"));
			}
		} catch (SQLException se) {
			System.out.println("�⵵�� �ֹ���ȸ�κ� SQL�� ����" + se);
		} catch (Exception e) {
			System.out.println("�⵵�� �ֹ���ȸ�κ� �������" + e);
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

		return oVo;

	}

	// �������� �ֹ��̷¸�Ͽ��� �˻�
	public OrderVO getOrderListSearchMonth(String month) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from orderlist where to_char(o_date,'mm') like ? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OrderVO oVo = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, month);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				oVo = new OrderVO();
				oVo.setNo(rs.getInt("no"));
				oVo.setO_num(rs.getString("o_num"));
				oVo.setO_pnum(rs.getString("o_pnum"));
				oVo.setO_pname(rs.getString("o_pname"));
				oVo.setO_pkind(rs.getString("o_pkind"));
				oVo.setO_size(rs.getString("o_size"));
				oVo.setO_color(rs.getString("o_color"));
				oVo.setO_price(rs.getInt("o_price"));
				oVo.setO_count(rs.getInt("o_count"));
				oVo.setO_totalPrice(rs.getInt("o_totalPrice"));
				oVo.setO_date(rs.getString("o_date"));
				oVo.setO_fileName(rs.getString("o_filename"));
			}
		} catch (SQLException se) {
			System.out.println("���� �ֹ���ȸ�κ� SQL�� ����" + se);
		} catch (Exception e) {
			System.out.println("���� �ֹ���ȸ�κ� �������" + e);
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

		return oVo;

	}

	// �⵵�� �� ��� �����ϴ� �ֹ����� �ֹ��̷¸�Ͽ��� �˻�
	public OrderVO getOrderListSearchYearMonth(String year, String month) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from orderlist where to_char(o_date,'yyyy-mm') like ? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OrderVO oVo = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, year + "-" + month);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				oVo = new OrderVO();
				oVo.setNo(rs.getInt("no"));
				oVo.setO_num(rs.getString("o_num"));
				oVo.setO_pnum(rs.getString("o_pnum"));
				oVo.setO_pname(rs.getString("o_pname"));
				oVo.setO_pkind(rs.getString("o_pkind"));
				oVo.setO_size(rs.getString("o_size"));
				oVo.setO_color(rs.getString("o_color"));
				oVo.setO_price(rs.getInt("o_price"));
				oVo.setO_count(rs.getInt("o_count"));
				oVo.setO_totalPrice(rs.getInt("o_totalPrice"));
				oVo.setO_date(rs.getString("o_date"));
				oVo.setO_fileName(rs.getString("o_filename"));
			}
		} catch (SQLException se) {
			System.out.println("��+���� �ֹ���ȸ�κ� SQL�� ����" + se);
		} catch (Exception e) {
			System.out.println("��+���� �ֹ���ȸ�κ� �������" + e);
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

		return oVo;

	}

	// �� �ֹ���, ���վ� ���ϱ�
	public OrderVO getStockTotalCount(OrderVO ovo) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(o_count) �Ѽ���,to_char(sum(o_totalprice),'l999,999,999') ���� from orderlist ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OrderVO oVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());

			rs = pstmt.executeQuery();

			if (rs.next()) {
				oVo = new OrderVO();
				oVo.setO_orderTotalCount(rs.getInt("�Ѽ���"));
				oVo.setO_orderTotalPrice(rs.getString("����"));
			}
		} catch (SQLException se) {
			System.out.println("�ֹ� �Ѽ���/���� SQL���� ����" + se);
		} catch (Exception e) {
			System.out.println("�ֹ� �Ѽ���/���� ������� ����" + e);
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

		return oVo;
	}

	// �⵵ �˻����� ��ȸ�� ��ǰ�� �� ���/���հ� ���ϱ�
	public OrderVO getOrderYearTotalCount(String year) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(o_count) �Ѽ���, to_char(sum(o_totalprice),'l999,999,999') ���� from orderlist ");
		sql.append("where to_char(o_date,'yyyy') like ? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OrderVO oVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, year);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				oVo = new OrderVO();
				oVo.setO_orderTotalCount(rs.getInt("�Ѽ���"));
				oVo.setO_orderTotalPrice(rs.getString("����"));
			}
		} catch (SQLException se) {
			System.out.println("�ֹ����� �⵵�˻� �Ѽ���/���� SQL���� ����" + se);
		} catch (Exception e) {
			System.out.println("�ֹ����� �⵵�˻� �Ѽ���/���� ������� ����" + e);
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

		return oVo;
	}

	// �� �˻����� ��ȸ�� ��ǰ�� �� ���/���հ� ���ϱ�
	public OrderVO getOrderMonthTotalCount(String month) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(o_count) �Ѽ���, to_char(sum(o_totalprice),'l999,999,999') ���� from orderlist ");
		sql.append("where to_char(o_date,'mm') like ? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OrderVO oVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, month);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				oVo = new OrderVO();
				oVo.setO_orderTotalCount(rs.getInt("�Ѽ���"));
				oVo.setO_orderTotalPrice(rs.getString("����"));
			}
		} catch (SQLException se) {
			System.out.println("�ֹ����� ���˻� �Ѽ���/���� SQL���� ����" + se);
		} catch (Exception e) {
			System.out.println("�ֹ����� ���˻� �Ѽ���/���� ������� ����" + e);
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

		return oVo;
	}

	// �⵵ + �� �˻����� ��ȸ�� ��ǰ�� �� ���/���հ� ���ϱ�
	public OrderVO getOrderYearMonthTotalCount(String year, String month) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(o_count) �Ѽ���, to_char(sum(o_totalprice),'l999,999,999') ���� from orderlist ");
		sql.append("where to_char(o_date,'yyyy-mm') like ? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OrderVO oVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, year + "-" + month);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				oVo = new OrderVO();
				oVo.setO_orderTotalCount(rs.getInt("�Ѽ���"));
				oVo.setO_orderTotalPrice(rs.getString("����"));
			}
		} catch (SQLException se) {
			System.out.println("�ֹ����� ��+���˻� �Ѽ���/���� SQL���� ����" + se);
		} catch (Exception e) {
			System.out.println("�ֹ����� ��+���˻� �Ѽ���/���� ������� ����" + e);
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

		return oVo;
	}

	// ������ �ֹ� ���� ��������
	public OrderVO getOrderUpdate(OrderVO ovo, int no) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("update orderlist set ");
		sql.append(" o_count=?, o_totalPrice=? where no=? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		OrderVO oVo = null;

		try {
			con = DBUtil.getConnection();

			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, ovo.getO_count());
			pstmt.setInt(2, ovo.getO_price() * ovo.getO_count());
			pstmt.setInt(3, no);

			int i = pstmt.executeUpdate();

			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("�ֹ� ���� ����");
				alert.setHeaderText("�ֹ� ���� ���� �Ϸ�");
				alert.setContentText("�ֹ� ���� ������ ���������� �Ϸ�Ǿ����ϴ�.");
				alert.showAndWait();

				oVo = new OrderVO();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("�ֹ� ���� ����");
				alert.setHeaderText("�ֹ� ���� ���� ����");
				alert.setContentText("�ֹ� ���� ������ �����Ͽ����ϴ�.");

				alert.showAndWait();
			}
		} catch (SQLException se) {
			System.out.println("�ֹ� ���� ���� sql�� ����" + se);
		} catch (Exception e) {
			System.out.println("�ֹ� ���� ���� �������" + e);
		} finally {

			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {

			}
		}
		return ovo;
	}

	// ������ �ֹ� ���� ����
	public void getOrderListDelete(int no) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("delete from orderlist where no=?");

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DBUtil.getConnection();

			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, no);

			int i = pstmt.executeUpdate();

			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("�ֹ� �̷� ����");
				alert.setHeaderText("�ֹ� �̷� ���� �Ϸ�");
				alert.setContentText("���������� �ֹ��̷��� �����Ǿ����ϴ�.");

				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("�ֹ� �̷� ����");
				alert.setHeaderText("�ֹ� �̷� ���� ����");
				alert.setContentText("�ֹ��̷� ������ �����Ͽ����ϴ�");

				alert.showAndWait();
			}
		} catch (SQLException se) {
			System.out.println("�ֹ��̷� ���� sql�� ����" + se);
		} catch (Exception e) {
			System.out.println("�ֹ� �̷� ���� ���� ����" + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}

				if (con != null) {
					con.close();
				}

			} catch (SQLException e) {

			}

		}
	}

}
