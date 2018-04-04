package Controller;

import java.awt.print.PrinterAbortException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.spi.DirStateFactory.Result;

import Model.SalesVO;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SalesDAO {

	// �ǸŽ� �Ǹ� ��� ���̺� �űԵ��
	public SalesVO inputNewSales(SalesVO saVo) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append(
				"insert into sales(no,sa_num,sa_pnum,sa_pname,sa_pkind,sa_size,sa_color,sa_price,sa_count,sa_totalprice,sa_date,sa_filename,sa_cusname,sa_cusnum) ");
		sql.append(" values(sales_seq.nextval,?,?,?,?,?,?,?,?,?,sysdate,?,?,?) ");

		Connection con = null;
		PreparedStatement pstmt = null;
		SalesVO savo = null;

		try {
			con = DBUtil.getConnection();

			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, "2017" + (int) (Math.random() * 100) + saVo.getSa_pnum());
			pstmt.setString(2, saVo.getSa_pnum());
			pstmt.setString(3, saVo.getSa_pname());
			pstmt.setString(4, saVo.getSa_pkind());
			pstmt.setString(5, saVo.getSa_size());
			pstmt.setString(6, saVo.getSa_color());
			pstmt.setInt(7, saVo.getSa_price());
			pstmt.setInt(8, saVo.getSa_count());
			pstmt.setInt(9, saVo.getSa_totalPrice());
			pstmt.setString(10, saVo.getSa_fileName());
			pstmt.setString(11, saVo.getSa_CusName());
			pstmt.setString(12, saVo.getSa_CusNum());

			int i = pstmt.executeUpdate();

		} catch (SQLException se) {
			System.out.println("�ǸŸ�� ������ �Է� sql����" + se);
		} catch (Exception e) {
			System.out.println("�ǸŸ�� ������ �Է� �������" + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {

			}
		}

		return savo;
	}

	// �Ǹ� �˻��Ͽ� ���������ϸ� �Ǹ��� ������ŭ ��� ��Ͽ����� �����ǵ��� ����
	public void deletePreSalesToStock(String pnum, int num) {

		StringBuffer sql = new StringBuffer();
		sql.append(" update stock set st_count = st_count - ? , ");
		sql.append(" st_totalprice = (st_count - ? )*st_price ");
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
			System.out.println("�ǸŴ�� ��Ͻ� ������� ���� sql������" + se);
		} catch (Exception e) {
			System.out.println("�ǸŴ�� ��Ͻ� ������� ���� �������" + e);
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

	// �ǸŴ�� ��ϵ� ��ǰ�� �����ϸ� ������̺�� ������ �Ѿ� �߰��Ͽ� ����
	public void inputPreSalesToStock(String pnum, int num) {

		StringBuffer sql = new StringBuffer();
		sql.append(" update stock set st_count = st_count + ? , ");
		sql.append(" st_totalprice = (st_count + ? )*st_price ");
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
			System.out.println("�ǸŴ�� ��� ������ ��� ���� sql������" + se);
		} catch (Exception e) {
			System.out.println("�ǸŴ�� ��� ������ ��� ���� �������" + e);
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

	// �ǸŸ�� ��ü ����Ʈ
	public ArrayList<SalesVO> getSalesTotal() {
		ArrayList<SalesVO> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		sql.append("select no,sa_num,sa_pnum,sa_pname,sa_pkind,sa_size,sa_color,sa_price,sa_count, ");
		sql.append("sa_totalprice,sa_date,sa_filename,sa_cusname,sa_cusnum from sales order by no");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalesVO saVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				saVo = new SalesVO();
				saVo.setNo(rs.getInt("no"));
				saVo.setSa_num(rs.getString("sa_num"));
				saVo.setSa_pnum(rs.getString("sa_pnum"));
				saVo.setSa_pname(rs.getString("sa_pname"));
				saVo.setSa_pkind(rs.getString("sa_pkind"));
				saVo.setSa_size(rs.getString("sa_size"));
				saVo.setSa_color(rs.getString("sa_color"));
				saVo.setSa_price(rs.getInt("sa_price"));
				saVo.setSa_count(rs.getInt("sa_count"));
				saVo.setSa_totalPrice(rs.getInt("sa_totalPrice"));
				saVo.setSa_date(rs.getString("sa_date"));
				saVo.setSa_fileName(rs.getString("sa_filename"));
				saVo.setSa_CusName(rs.getString("sa_cusname"));
				saVo.setSa_CusNum(rs.getString("sa_cusnum"));

				list.add(saVo);

			}

		} catch (SQLException se) {
			System.out.println("��ü�ǸŸ�� ����Ʈ sql�� ����" + se);

		} catch (Exception e) {
			System.out.println("��ü�ǸŸ�� ����Ʈ ���ÿ���" + e);

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
	public ArrayList<String> getSalesColumnName() {
		ArrayList<String> columnName = new ArrayList<>();

		StringBuffer sql = new StringBuffer();
		sql.append("select * from sales");

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

			System.out.println("�ǸŸ�����̺��÷����� ���� SQL�� ����" + se);

		} catch (Exception e) {
			System.out.println("�ǸŸ�� �÷����� ���� ���� ���� " + e);

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

	// �� �Ǹŷ�, �� �Ǹž� ���ϱ�
	public SalesVO getSalesTotalCount(SalesVO ovo) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(sa_count) �Ѽ���,to_char(sum(sa_totalprice),'l999,999,999') ���� from sales ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalesVO saVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());

			rs = pstmt.executeQuery();

			if (rs.next()) {
				saVo = new SalesVO();
				saVo.setSa_salesTotalCount(rs.getInt("�Ѽ���"));
				saVo.setSa_salesTotalPrice(rs.getString("����"));
			}
		} catch (SQLException se) {
			System.out.println("�Ǹ� �Ѽ���/���� SQL���� ����" + se);
		} catch (Exception e) {
			System.out.println("�Ǹ� �Ѽ���/���� ������� ����" + e);
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

		return saVo;
	}

	// ����� ����ó�� ��ȸ
	public SalesVO getSalesListSearchCusName(String CName, String CNum) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from sales where sa_cusname = ? and sa_cusnum = ? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalesVO saVo = null;
		ArrayList<SalesVO> list = new ArrayList<>();

		try {
			con = DBUtil.getConnection();

			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, CName);
			pstmt.setString(2, CNum);

			rs = pstmt.executeQuery();

			if (rs.next()) {

				saVo = new SalesVO();
				saVo.setNo(rs.getInt("no"));
				saVo.setSa_num(rs.getString("sa_num"));
				saVo.setSa_pnum(rs.getString("sa_pnum"));
				saVo.setSa_pname(rs.getString("sa_pname"));
				saVo.setSa_pkind(rs.getString("sa_pkind"));
				saVo.setSa_size(rs.getString("sa_size"));
				saVo.setSa_color(rs.getString("sa_color"));
				saVo.setSa_price(rs.getInt("sa_price"));
				saVo.setSa_count(rs.getInt("sa_count"));
				saVo.setSa_totalPrice(rs.getInt("sa_totalPrice"));
				saVo.setSa_date(rs.getString("sa_date"));
				saVo.setSa_fileName(rs.getString("sa_fileName"));
				saVo.setSa_CusName(rs.getString("sa_CusName"));
				saVo.setSa_CusNum(rs.getString("sa_CusNum"));

			}

			list.add(saVo);

		} catch (SQLException se) {
			System.out.println("����� ����ó�� �˻� sql����" + se);
		} catch (Exception e) {
			System.out.println("����� ����ó�� �˻� �������" + e);
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
			} catch (SQLException se1) {

			}
		}
		return saVo;
	}

	// ����� ����ó�� ��ȸ(����Ʈ ����)
	public ArrayList<SalesVO> getArraySalesListSearchCusName(String CName, String CNum) throws Exception {
		ArrayList<SalesVO> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from sales where sa_cusname = ? and sa_cusnum = ? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalesVO saVo = null;

		try {
			con = DBUtil.getConnection();

			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, CName);
			pstmt.setString(2, CNum);

			rs = pstmt.executeQuery();

			while (rs.next()) {

				saVo = new SalesVO();
				saVo.setNo(rs.getInt("no"));
				saVo.setSa_num(rs.getString("sa_num"));
				saVo.setSa_pnum(rs.getString("sa_pnum"));
				saVo.setSa_pname(rs.getString("sa_pname"));
				saVo.setSa_pkind(rs.getString("sa_pkind"));
				saVo.setSa_size(rs.getString("sa_size"));
				saVo.setSa_color(rs.getString("sa_color"));
				saVo.setSa_price(rs.getInt("sa_price"));
				saVo.setSa_count(rs.getInt("sa_count"));
				saVo.setSa_totalPrice(rs.getInt("sa_totalPrice"));
				saVo.setSa_date(rs.getString("sa_date"));
				saVo.setSa_fileName(rs.getString("sa_fileName"));
				saVo.setSa_CusName(rs.getString("sa_CusName"));
				saVo.setSa_CusNum(rs.getString("sa_CusNum"));

				list.add(saVo);
			}

		} catch (SQLException se) {
			System.out.println("����� ����ó�� �˻� sql����" + se);
		} catch (Exception e) {
			System.out.println("����� ����ó�� �˻� �������" + e);
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
			} catch (SQLException se1) {

			}
		}
		return list;
	}

	// ����� ����ó�� �˻��� ���� �Ѽ����� �� �� ���ϱ�
	public SalesVO getSalesCusNameTotalCount(String CName, String CNum) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(sa_count) �Ѽ���,to_char(sum(sa_totalprice),'l999,999,999') ���� from sales ");
		sql.append(" where sa_cusname like ? and sa_cusnum = ?");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalesVO saVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, CName);
			pstmt.setString(2, CNum);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				saVo = new SalesVO();
				saVo.setSa_salesTotalCount(rs.getInt("�Ѽ���"));
				saVo.setSa_salesTotalPrice(rs.getString("����"));
			}
		} catch (SQLException se) {
			System.out.println("�Ǹŷ� ���� �˻� �Ѽ���/���� SQL���� ����" + se);
		} catch (Exception e) {
			System.out.println("�Ǹŷ� ���� �˻� �Ѽ���/���� ������� ����" + e);
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

		return saVo;
	}

	// ����� ��ȣ�� ��ȸ�� �Ǹ� ���̺��� �÷�����
	public ArrayList<String> getSalesSearchColumnName(String pname, String pnum) {
		ArrayList<String> columnName = new ArrayList<>();

		StringBuffer sql = new StringBuffer();
		sql.append("select * from sales where sa_cusname like ? and sa_cusnum = ?");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// �̺κ� 1�� ���ؾȵ�
		ResultSetMetaData rsmd = null;
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, pname);
			pstmt.setString(2, pnum);

			rs = pstmt.executeQuery();
			rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();

			for (int i = 1; i <= cols; i++) {
				columnName.add(rsmd.getColumnName(i));
			}

		} catch (SQLException se) {

			System.out.println("�ǸŸ�����̺��÷����� ���� SQL�� ����" + se);

		} catch (Exception e) {
			System.out.println("�ǸŸ�� �÷����� ���� ���� ���� " + e);

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

	// �Ⱓ���� �Ǹ� ��� �˻�
	public ArrayList<SalesVO> dateSalesListSearch(String sdate, String edate) {
		ArrayList<SalesVO> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from sales where sa_date between ? and (to_date( ? )+1) order by no ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalesVO saVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, sdate);
			pstmt.setString(2, edate);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				saVo = new SalesVO();
				saVo.setNo(rs.getInt("no"));
				saVo.setSa_num(rs.getString("sa_num"));
				saVo.setSa_pnum(rs.getString("sa_pnum"));
				saVo.setSa_pname(rs.getString("sa_pname"));
				saVo.setSa_pkind(rs.getString("sa_pkind"));
				saVo.setSa_size(rs.getString("sa_size"));
				saVo.setSa_color(rs.getString("sa_color"));
				saVo.setSa_price(rs.getInt("sa_price"));
				saVo.setSa_count(rs.getInt("sa_count"));
				saVo.setSa_totalPrice(rs.getInt("sa_totalprice"));
				saVo.setSa_CusName(rs.getString("sa_cusname"));
				saVo.setSa_CusNum(rs.getString("sa_cusnum"));
				saVo.setSa_date(rs.getString("sa_date"));
				saVo.setSa_fileName(rs.getString("sa_filename"));

				list.add(saVo);
			}
		} catch (SQLException se) {
			System.out.println("�Ⱓ �˻� sql����" + se);
		} catch (Exception e) {
			System.out.println("�Ⱓ �˻� �������" + e);

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

	// �Ⱓ���� �˻��� ���� �Ѽ����� �� �� ���ϱ�
	public SalesVO getSalesDateTotalCount(String sdate, String edate) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(sa_count) �Ѽ���,to_char(sum(sa_totalprice),'l999,999,999') ���� from sales ");
		sql.append(" where sa_date between ? and (to_date( ? )+1) ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalesVO saVo = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, sdate);
			pstmt.setString(2, edate);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				saVo = new SalesVO();
				saVo.setSa_salesTotalCount(rs.getInt("�Ѽ���"));
				saVo.setSa_salesTotalPrice(rs.getString("����"));
			}

		} catch (SQLException se) {
			System.out.println(" �Ⱓ �˻� �Ѽ���/���� SQL���� ����" + se);
		} catch (Exception e) {
			System.out.println(" �Ⱓ �˻� �Ѽ���/���� ������� ����" + e);
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

		return saVo;
	}

	// �����ͺ��̽��� ����� ���� ó�� ��¥ ���ϱ�
	public String startDateSales() {
		String startDate = null;

		StringBuffer sql = new StringBuffer();
		sql.append("select to_char(min(sa_date),'yyyy-mm-dd') ������ from sales ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				startDate = rs.getString("������");
			}
		} catch (SQLException se) {
			System.out.println("�Ǹ� ������ ���ϱ� sql����" + se);
		} catch (Exception e) {
			System.out.println("�Ǹ� ������ ���ϱ� �������" + e);
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

		return startDate;
	}

	// ������ ���� ����
	public void getSalesDelete(int no) throws Exception {
		// sql��
		StringBuffer sql = new StringBuffer();
		sql.append("delete from sales where no=?");
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
				alert.setTitle("�Ǹ� �̷� ����");
				alert.setHeaderText("�Ǹ� �̷� ���� �Ϸ�");
				alert.setContentText("���������� ������ �Ϸ� �Ǿ����ϴ�.");

				alert.showAndWait();
			} else {

				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("�Ǹ� �̷� ����");
				alert.setHeaderText("�Ǹ� �̷� ���� ����");
				alert.setContentText("��ǰ ���� ������ �����Ͽ����ϴ�.");

				alert.showAndWait();

			}
		} catch (SQLException se) {
			System.out.println("�Ǹ� �̷� ���� ���� sql�� ����" + se);
		} catch (Exception e) {
			System.out.println("�Ǹ� �̷� ���� ���� ���� ����" + e);
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

}
