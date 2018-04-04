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

	// 판매시 판매 목록 테이블에 신규등록
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
			System.out.println("판매목록 데이터 입력 sql오류" + se);
		} catch (Exception e) {
			System.out.println("판매목록 데이터 입력 진행오류" + e);
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

	// 판매 검색하여 수량설정하면 판매할 수량만큼 재고 목록에서도 연동되도록 설정
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
			System.out.println("판매대기 등록시 재고량변동 변동 sql문오류" + se);
		} catch (Exception e) {
			System.out.println("판매대기 등록시 재고량변동 변동 진행오류" + e);
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

	// 판매대기 등록된 물품을 삭제하면 재고테이블로 수량과 총액 추가하여 전달
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
			System.out.println("판매대기 목록 삭제시 재고량 변동 sql문오류" + se);
		} catch (Exception e) {
			System.out.println("판매대기 목록 삭제시 재고량 변동 진행오류" + e);
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

	// 판매목록 전체 리스트
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
			System.out.println("전체판매목록 리스트 sql문 오류" + se);

		} catch (Exception e) {
			System.out.println("전체판매목록 리스트 관련오류" + e);

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
	 * 여기 부분 제품 테이블의 컬럼 갯수산정하는 SQL 관련부분 이해 1도 안됨
	 * 
	 ****************************************************/
	// 데이터베이스 주문 테이블의 컬럼갯수
	public ArrayList<String> getSalesColumnName() {
		ArrayList<String> columnName = new ArrayList<>();

		StringBuffer sql = new StringBuffer();
		sql.append("select * from sales");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// 이부분 1도 이해안됨
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

			System.out.println("판매목록테이블컬럼갯수 산정 SQL문 오류" + se);

		} catch (Exception e) {
			System.out.println("판매목록 컬럼갯수 산정 관련 오류 " + e);

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

	// 총 판매량, 총 판매액 구하기
	public SalesVO getSalesTotalCount(SalesVO ovo) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(sa_count) 총수량,to_char(sum(sa_totalprice),'l999,999,999') 총합 from sales ");

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
				saVo.setSa_salesTotalCount(rs.getInt("총수량"));
				saVo.setSa_salesTotalPrice(rs.getString("총합"));
			}
		} catch (SQLException se) {
			System.out.println("판매 총수량/총합 SQL관련 오류" + se);
		} catch (Exception e) {
			System.out.println("판매 총수량/총합 진행관련 오류" + e);
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

	// 고객명과 연락처로 조회
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
			System.out.println("고객명과 연락처로 검색 sql오류" + se);
		} catch (Exception e) {
			System.out.println("고객명과 연락처로 검색 진행오류" + e);
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

	// 고객명과 연락처로 조회(리스트 버전)
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
			System.out.println("고객명과 연락처로 검색 sql오류" + se);
		} catch (Exception e) {
			System.out.println("고객명과 연락처로 검색 진행오류" + e);
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

	// 고객명과 연락처로 검색한 값의 총수량과 총 합 구하기
	public SalesVO getSalesCusNameTotalCount(String CName, String CNum) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(sa_count) 총수량,to_char(sum(sa_totalprice),'l999,999,999') 총합 from sales ");
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
				saVo.setSa_salesTotalCount(rs.getInt("총수량"));
				saVo.setSa_salesTotalPrice(rs.getString("총합"));
			}
		} catch (SQLException se) {
			System.out.println("판매량 고객명 검색 총수량/총합 SQL관련 오류" + se);
		} catch (Exception e) {
			System.out.println("판매량 고객명 검색 총수량/총합 진행관련 오류" + e);
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

	// 고객명과 번호로 조회한 판매 테이블의 컬럼갯수
	public ArrayList<String> getSalesSearchColumnName(String pname, String pnum) {
		ArrayList<String> columnName = new ArrayList<>();

		StringBuffer sql = new StringBuffer();
		sql.append("select * from sales where sa_cusname like ? and sa_cusnum = ?");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// 이부분 1도 이해안됨
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

			System.out.println("판매목록테이블컬럼갯수 산정 SQL문 오류" + se);

		} catch (Exception e) {
			System.out.println("판매목록 컬럼갯수 산정 관련 오류 " + e);

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

	// 기간으로 판매 목록 검색
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
			System.out.println("기간 검색 sql오류" + se);
		} catch (Exception e) {
			System.out.println("기간 검색 진행오류" + e);

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

	// 기간으로 검색한 값의 총수량과 총 합 구하기
	public SalesVO getSalesDateTotalCount(String sdate, String edate) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(sa_count) 총수량,to_char(sum(sa_totalprice),'l999,999,999') 총합 from sales ");
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
				saVo.setSa_salesTotalCount(rs.getInt("총수량"));
				saVo.setSa_salesTotalPrice(rs.getString("총합"));
			}

		} catch (SQLException se) {
			System.out.println(" 기간 검색 총수량/총합 SQL관련 오류" + se);
		} catch (Exception e) {
			System.out.println(" 기간 검색 총수량/총합 진행관련 오류" + e);
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

	// 데이터베이스에 저장된 제일 처음 날짜 구하기
	public String startDateSales() {
		String startDate = null;

		StringBuffer sql = new StringBuffer();
		sql.append("select to_char(min(sa_date),'yyyy-mm-dd') 시작일 from sales ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				startDate = rs.getString("시작일");
			}
		} catch (SQLException se) {
			System.out.println("판매 시작일 구하기 sql오류" + se);
		} catch (Exception e) {
			System.out.println("판매 시작일 구하기 진행오류" + e);
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

	// 선택한 정보 삭제
	public void getSalesDelete(int no) throws Exception {
		// sql문
		StringBuffer sql = new StringBuffer();
		sql.append("delete from sales where no=?");
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			// 데이터 베이스 접속
			con = DBUtil.getConnection();

			// sql문 전송
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, no);

			// 수행된 처리결과를 얻어옴
			int i = pstmt.executeUpdate();

			if (i == 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("판매 이력 삭제");
				alert.setHeaderText("판매 이력 삭제 완료");
				alert.setContentText("정상적으로 삭제가 완료 되었습니다.");

				alert.showAndWait();
			} else {

				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("판매 이력 삭제");
				alert.setHeaderText("판매 이력 삭제 실패");
				alert.setContentText("상품 정보 삭제에 실패하였습니다.");

				alert.showAndWait();

			}
		} catch (SQLException se) {
			System.out.println("판매 이력 삭제 관련 sql문 오류" + se);
		} catch (Exception e) {
			System.out.println("판매 이력 삭제 관련 진행 오류" + e);
		} finally {
			// 항상 연결을 하고 끊어줘야 한다. 연결햇던 순서 반대로 종료해야만 정보 손실을 막을수 있다.

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
