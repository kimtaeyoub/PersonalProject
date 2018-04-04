package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.StockVO;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class StockDAO {

	// �ű� ��ǰ���
	public StockVO inputNewGoods(StockVO svo) throws Exception {
		// ����Ŭ�� ������ ó���ϱ����� SQL��
		StringBuffer sql = new StringBuffer();
		sql.append("insert into stock");
		sql.append("(no,st_pnum,st_pname,st_pkind,st_size,st_color,st_price,st_count,st_totalprice,st_filename)");
		sql.append("values(stock_seq.nextval,?,?,?,?,?,?,0,?,?)");

		Connection con = null;
		PreparedStatement pstmt = null;
		StockVO sVo = svo;

		try {
			// DBUtil �� �޼ҵ带 �̿��Ͽ� ����
			con = DBUtil.getConnection();

			// �Է¹��� ������ ó���ϱ����� SQL��
			pstmt = con.prepareStatement(sql.toString());

			pstmt.setString(1, sVo.getSt_pnum());
			pstmt.setString(2, sVo.getSt_pname());
			pstmt.setString(3, sVo.getSt_pkind());
			pstmt.setString(4, sVo.getSt_size());
			pstmt.setString(5, sVo.getSt_color());
			pstmt.setInt(6, sVo.getSt_price());
			pstmt.setInt(7, sVo.getSt_price() * sVo.getSt_count());
			pstmt.setString(8, sVo.getSt_filename());

			// SQL���� ������ ó�� ����� ����
			int i = pstmt.executeUpdate();

		} catch (SQLException e) {

			System.out.println("�Է�SQL���๮ ó������" + e);
		} catch (Exception e) {
			System.out.println("DAO�Է� ���� ����" + e);
		} finally {
			try {
				// �����ͺ��̽����� ���ῡ ���Ǿ��� ������Ʈ ����
				if (pstmt != null) {
					pstmt.close();
				}

				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {

			}
		}

		return sVo;

	}

	// ��ü ��� ����Ʈ
	public ArrayList<StockVO> getStockTotal() {
		ArrayList<StockVO> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		sql.append("select no,st_pnum,st_pname,st_pkind,st_size,st_color,st_price,st_count, ");
		sql.append("st_totalprice,st_filename from stock order by no");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StockVO sVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				sVo = new StockVO();
				sVo.setNo(rs.getInt("no"));
				sVo.setSt_pnum(rs.getString("st_pnum"));
				sVo.setSt_pname(rs.getString("st_pname"));
				sVo.setSt_pkind(rs.getString("st_pkind"));
				sVo.setSt_size(rs.getString("st_size"));
				sVo.setSt_color(rs.getString("st_color"));
				sVo.setSt_price(rs.getInt("st_price"));
				sVo.setSt_count(rs.getInt("st_count"));
				sVo.setSt_totalPrice(rs.getInt("st_totalPrice"));
				sVo.setSt_filename(rs.getString("st_filename"));

				list.add(sVo);

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
	// �����ͺ��̽� ��ǰ ���̺��� �÷�����
	public ArrayList<String> getColumnName() {
		ArrayList<String> columnName = new ArrayList<>();

		StringBuffer sql = new StringBuffer();
		sql.append("select * from stock");

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

			System.out.println("���̺��÷����� ���� SQL�� ����" + se);

		} catch (Exception e) {
			System.out.println("���̺� �÷����� ���� ���� ���� " + e);

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

	// ������ȸ ��ǰ�� ǰ������ ������ȸ
	public StockVO getStockPnumCheck(String pnum) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from stock where st_pnum like ");
		sql.append("? order by no");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StockVO sVo = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, pnum);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				sVo = new StockVO();
				sVo.setNo(rs.getInt("no"));
				sVo.setSt_pnum(rs.getString("st_pnum"));
				sVo.setSt_pname(rs.getString("st_pname"));
				sVo.setSt_pkind(rs.getString("st_pkind"));
				sVo.setSt_size(rs.getString("st_size"));
				sVo.setSt_color(rs.getString("st_color"));
				sVo.setSt_price(rs.getInt("st_price"));
				sVo.setSt_count(rs.getInt("st_count"));
				sVo.setSt_totalPrice(rs.getInt("st_totalPrice"));
				sVo.setSt_filename(rs.getString("st_filename"));
			}
		} catch (SQLException se) {
			System.out.println("ǰ������ ��ȸ�κ� SQL�� ����" + se);
		} catch (Exception e) {
			System.out.println("ǰ������ ��ȸ�κ� �������" + e);
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

		return sVo;

	}

	// ������ȸ ��ǰ�� ǰ������ ������ȸ
	public StockVO getStockPkindCheck(String pkind) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from stock where st_pkind like ");
		sql.append("? order by no");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StockVO sVo = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, pkind);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				sVo = new StockVO();
				sVo.setNo(rs.getInt("no"));
				sVo.setSt_pnum(rs.getString("st_pnum"));
				sVo.setSt_pname(rs.getString("st_pname"));
				sVo.setSt_pkind(rs.getString("st_pkind"));
				sVo.setSt_size(rs.getString("st_size"));
				sVo.setSt_color(rs.getString("st_color"));
				sVo.setSt_price(rs.getInt("st_price"));
				sVo.setSt_count(rs.getInt("st_count"));
				sVo.setSt_totalPrice(rs.getInt("st_totalPrice"));
				sVo.setSt_filename(rs.getString("st_filename"));
			}
		} catch (SQLException se) {
			System.out.println("ǰ������ ��ȸ�κ� SQL�� ����" + se);
		} catch (Exception e) {
			System.out.println("ǰ������ ��ȸ�κ� �������" + e);
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

		return sVo;

	}

	// ������ ���� ����
	public void getStockDelete(int no) throws Exception {
		// sql��
		StringBuffer sql = new StringBuffer();
		sql.append("delete from stock where no=?");
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// ������ ���̽� ����
			con = DBUtil.getConnection();

			// sql�� ����
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, no);

			// ����� ó������� ����
			int i = pstmt.executeUpdate();

			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("��ǰ ���� ����");
				alert.setHeaderText("��ǰ ���� ���� �Ϸ�");
				alert.setContentText("���������� ������ �Ϸ� �Ǿ����ϴ�.");

				alert.showAndWait();
			} else {

				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("��ǰ ���� ����");
				alert.setHeaderText("��ǰ ���� ���� ����");
				alert.setContentText("��ǰ ���� ������ �����Ͽ����ϴ�.");

				alert.showAndWait();

			}
		} catch (SQLException se) {
			System.out.println("���� ���� ���� sql�� ����" + se);
		} catch (Exception e) {
			System.out.println("���� ���� ���� ���� ����" + e);
		} finally {
			// �׻� ������ �ϰ� ������� �Ѵ�. �����޴� ���� �ݴ�� �����ؾ߸� ���� �ս��� ������ �ִ�.

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

	// ������ ��ǰ ���� ����
	public StockVO getStockUpdate(StockVO svo, int no, int count) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("update stock set ");
		sql.append(" st_pnum=?,st_pname=?,st_pkind=?,st_size=?,st_color=?,st_price=?,st_totalprice=? ");
		sql.append(" where no=? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		StockVO sVo = null;

		try {
			con = DBUtil.getConnection();

			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, svo.getSt_pnum());
			pstmt.setString(2, svo.getSt_pname());
			pstmt.setString(3, svo.getSt_pkind());
			pstmt.setString(4, svo.getSt_size());
			pstmt.setString(5, svo.getSt_color());
			pstmt.setInt(6, svo.getSt_price());
			pstmt.setInt(7, svo.getSt_price() * count);
			pstmt.setInt(8, no);

			int i = pstmt.executeUpdate();

			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("��ǰ ���� ����");
				alert.setHeaderText("��ǰ ���� ���� �Ϸ�");
				alert.setContentText("���������� ���������� �Ϸ�Ǿ����ϴ�.");

				alert.showAndWait();

				sVo = new StockVO();
			} else {

				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("��ǰ ���� ����");
				alert.setHeaderText("��ǰ ���� ���� ����");
				alert.setContentText("���������� �����Ͽ����ϴ�.");

				alert.showAndWait();
			}

		} catch (SQLException se) {

			System.out.println("��������SQL�� ����" + se);

		} catch (Exception e) {
			System.out.println("�������� ���� ����" + e);

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

		return svo;

	}

	// �� ��� ���ϱ�
	public StockVO getStockTotalCount(StockVO svo) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(st_count) �Ѽ���, to_char(sum(st_totalprice),'l999,999,999') ���� from stock ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StockVO sVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());

			rs = pstmt.executeQuery();

			if (rs.next()) {
				sVo = new StockVO();
				sVo.setSt_stockTotalCount(rs.getInt("�Ѽ���"));
				sVo.setSt_stockTotalPrice(rs.getString("����"));
			}
		} catch (SQLException se) {
			System.out.println("�Ѽ���/���� SQL���� ����" + se);
		} catch (Exception e) {
			System.out.println("�Ѽ���/���� ������� ����" + e);
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

		return sVo;
	}

	// ǰ�� �˻����� ��ȸ�� ��ǰ�� �� ���/���հ� ���ϱ�
	public StockVO getStockPnumTotalCount(String pnum) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(st_count) �Ѽ���, to_char(sum(st_totalprice),'l999,999,999') ���� from stock ");
		sql.append("where st_pnum like ? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StockVO sVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, pnum);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				sVo = new StockVO();
				sVo.setSt_stockTotalCount(rs.getInt("�Ѽ���"));
				sVo.setSt_stockTotalPrice(rs.getString("����"));
			}
		} catch (SQLException se) {
			System.out.println("ǰ���˻� �Ѽ���/���� SQL���� ����" + se);
		} catch (Exception e) {
			System.out.println("ǰ���˻� �Ѽ���/���� ������� ����" + e);
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

		return sVo;
	}

	// ǰ�� �˻����� ��ȸ�� ��ǰ�� �� ���/���հ� ���ϱ�
	public StockVO getStockPkindTotalCount(String pkind) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(st_count) �Ѽ���, to_char(sum(st_totalprice),'l999,999,999') ���� from stock ");
		sql.append("where st_pkind like ? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StockVO sVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, "%" + pkind + "%");

			rs = pstmt.executeQuery();

			if (rs.next()) {
				sVo = new StockVO();
				sVo.setSt_stockTotalCount(rs.getInt("�Ѽ���"));
				sVo.setSt_stockTotalPrice(rs.getString("����"));
			}
		} catch (SQLException se) {
			System.out.println("ǰ���˻� �Ѽ���/���� SQL���� ����" + se);
		} catch (Exception e) {
			System.out.println("ǰ���˻� �Ѽ���/���� ������� ����" + e);
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

		return sVo;
	}

	// �ش繰ǰ�� ���� ����
	public int countStockGoods(String pnum) {
		int i = 0;

		StringBuffer sql = new StringBuffer();
		sql.append("select st_count ������ from stock where st_pnum = ? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StockVO svo = new StockVO();

		try {
			con = DBUtil.getConnection();

			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, pnum);

			rs = pstmt.executeQuery();

			if (rs.next()) {

				i = rs.getInt("������");
			}

		} catch (SQLException se) {
			System.out.println("��� ��ǰ���� ���� sql�� ����" + se);
		} catch (Exception e) {
			System.out.println("��� ��ǰ���� ���� �������" + e);
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

		return i;

	}

	// ���� ��ǰ���� ��ϵ� ��ǰ��ȣ �ҷ�����
	public ArrayList<String> choiceStockPnum() {
		ArrayList<String> list = new ArrayList<>();

		StringBuffer sql = new StringBuffer();
		sql.append("select st_pnum from stock order by st_pnum ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DBUtil.getConnection();

			pstmt = con.prepareStatement(sql.toString());

			rs = pstmt.executeQuery();

			while (rs.next()) {

				list.add(rs.getString("st_pnum"));

			}
			
		} catch (SQLException se) {
			System.out.println("����ϵ� ��ǰ�� ��ȸ sql����" + se);
		} catch (Exception e) {
			System.out.println("����ϵ� ��ǰ�� ��ȸ �������" + e);
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

	// ������ ���̽����̺� �Է��� ǰ���� �ִ��� ��ȸ
	public boolean isStockPnum(String spnum) {
		boolean i = false;
		StringBuffer sql = new StringBuffer();
		sql.append("select st_pnum from stock where st_pnum=? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, spnum);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				i = true;
			}
		} catch (SQLException se) {
			System.out.println("���� ǰ�� ��ȸ sql����" + se);
		} catch (Exception e) {
			System.out.println("���� ǰ�� ��ȸ ���� ����" + e);
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

		return i;
	}

	// ����� �����鸸 �̾Ƽ� ����Ʈ�� ����
	public ArrayList<StockVO> countStockList() {
		ArrayList<StockVO> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		sql.append("select st_count, st_pnum from stock");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StockVO svo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				svo = new StockVO();
				svo.setSt_count(rs.getInt("st_count"));
				svo.setSt_pnum(rs.getString("st_pnum"));

				list.add(svo);
			}
		} catch (SQLException se) {
			System.out.println("���� ���� �˻� sql����" + se);
		} catch (Exception e) {
			System.out.println("�� ��� ���� �˻� �������" + e);
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

		return list;
	}

	// ������ ��ǰ��ȣ�� �� �ֹ� �� ��ȸ
	public int countTotalOrder(String pnum) {
		int i = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(o_count) ���ֹ��� from orderlist where o_pnum = ? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, pnum);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				i = rs.getInt("���ֹ���");
			}
		} catch (SQLException se) {
			System.out.println("��� ǰ���� ���� ��ǰ ���ֹ��� ���� sql�� ����" + se);
		} catch (Exception e) {
			System.out.println("��� ǰ���� ���� ��ǰ �� �ֹ��� ���� �������" + e);
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

		return i;
	}

	// ������ ��ǰ��ȣ�� �� �Ǹ� �� ��ȸ
	public int countTotalSales(String pnum) {
		int i = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(sa_count) ���Ǹŷ� from sales where sa_pnum = ? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, pnum);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				i = rs.getInt("���Ǹŷ�");
			}
		} catch (SQLException se) {
			System.out.println("��� ǰ���� ���� ��ǰ ���Ǹŷ� ���� sql�� ����" + se);
		} catch (Exception e) {
			System.out.println("��� ǰ���� ���� ��ǰ ���Ǹŷ� ���� �������" + e);
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

		return i;
	}

}
