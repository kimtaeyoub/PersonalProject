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

	// 신규 주문 상품 주문이력 DB로 등록
	public OrderVO inputNewOrder(StockVO svo, int count) throws Exception {

		// 오라클에서 데이터 처리를 하기위한 sql문설정
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
			System.out.println("주문목록 입력 SQL문 수행오류" + e);

		} catch (Exception e) {
			System.out.println("주문목록 입력 진행오류" + e);
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

	// 주문목록 전체 리스트
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
			System.out.println("전체재고 리스트 sql문 오류" + se);

		} catch (Exception e) {
			System.out.println("전체재고 리스트 관련오류" + e);

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
	public ArrayList<String> getOrderColumnName() {
		ArrayList<String> columnName = new ArrayList<>();

		StringBuffer sql = new StringBuffer();
		sql.append("select * from orderlist");

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

			System.out.println("주문테이블컬럼갯수 산정 SQL문 오류" + se);

		} catch (Exception e) {
			System.out.println("주문테이블 컬럼갯수 산정 관련 오류 " + e);

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

	// 년도만으로 주문이력목록에서 검색
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
			System.out.println("년도로 주문조회부분 SQL문 오류" + se);
		} catch (Exception e) {
			System.out.println("년도로 주문조회부분 진행오류" + e);
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

	// 월만으로 주문이력목록에서 검색
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
			System.out.println("월로 주문조회부분 SQL문 오류" + se);
		} catch (Exception e) {
			System.out.println("월로 주문조회부분 진행오류" + e);
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

	// 년도와 월 모두 만족하는 주문내역 주문이력목록에서 검색
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
			System.out.println("년+월로 주문조회부분 SQL문 오류" + se);
		} catch (Exception e) {
			System.out.println("년+월로 주문조회부분 진행오류" + e);
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

	// 총 주문량, 총합액 구하기
	public OrderVO getStockTotalCount(OrderVO ovo) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(o_count) 총수량,to_char(sum(o_totalprice),'l999,999,999') 총합 from orderlist ");

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
				oVo.setO_orderTotalCount(rs.getInt("총수량"));
				oVo.setO_orderTotalPrice(rs.getString("총합"));
			}
		} catch (SQLException se) {
			System.out.println("주문 총수량/총합 SQL관련 오류" + se);
		} catch (Exception e) {
			System.out.println("주문 총수량/총합 진행관련 오류" + e);
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

	// 년도 검색으로 조회된 물품의 총 재고량/총합계 구하기
	public OrderVO getOrderYearTotalCount(String year) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(o_count) 총수량, to_char(sum(o_totalprice),'l999,999,999') 총합 from orderlist ");
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
				oVo.setO_orderTotalCount(rs.getInt("총수량"));
				oVo.setO_orderTotalPrice(rs.getString("총합"));
			}
		} catch (SQLException se) {
			System.out.println("주문내역 년도검색 총수량/총합 SQL관련 오류" + se);
		} catch (Exception e) {
			System.out.println("주문내역 년도검색 총수량/총합 진행관련 오류" + e);
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

	// 월 검색으로 조회된 물품의 총 재고량/총합계 구하기
	public OrderVO getOrderMonthTotalCount(String month) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(o_count) 총수량, to_char(sum(o_totalprice),'l999,999,999') 총합 from orderlist ");
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
				oVo.setO_orderTotalCount(rs.getInt("총수량"));
				oVo.setO_orderTotalPrice(rs.getString("총합"));
			}
		} catch (SQLException se) {
			System.out.println("주문내역 월검색 총수량/총합 SQL관련 오류" + se);
		} catch (Exception e) {
			System.out.println("주문내역 월검색 총수량/총합 진행관련 오류" + e);
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

	// 년도 + 월 검색으로 조회된 물품의 총 재고량/총합계 구하기
	public OrderVO getOrderYearMonthTotalCount(String year, String month) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(o_count) 총수량, to_char(sum(o_totalprice),'l999,999,999') 총합 from orderlist ");
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
				oVo.setO_orderTotalCount(rs.getInt("총수량"));
				oVo.setO_orderTotalPrice(rs.getString("총합"));
			}
		} catch (SQLException se) {
			System.out.println("주문내역 년+월검색 총수량/총합 SQL관련 오류" + se);
		} catch (Exception e) {
			System.out.println("주문내역 년+월검색 총수량/총합 진행관련 오류" + e);
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

	// 선택한 주문 정보 수량수정
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
				alert.setTitle("주문 수량 수정");
				alert.setHeaderText("주문 수량 수정 완료");
				alert.setContentText("주문 수량 수정이 성공적으로 완료되었습니다.");
				alert.showAndWait();

				oVo = new OrderVO();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("주문 수량 수정");
				alert.setHeaderText("주문 수량 수정 실패");
				alert.setContentText("주문 수량 수정에 실패하였습니다.");

				alert.showAndWait();
			}
		} catch (SQLException se) {
			System.out.println("주문 수량 수정 sql문 오류" + se);
		} catch (Exception e) {
			System.out.println("주문 수량 수정 진행오류" + e);
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

	// 선택한 주문 정보 삭제
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
				alert.setTitle("주문 이력 삭제");
				alert.setHeaderText("주문 이력 삭제 완료");
				alert.setContentText("성공적으로 주문이력이 삭제되었습니다.");

				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("주문 이력 삭제");
				alert.setHeaderText("주문 이력 삭제 실패");
				alert.setContentText("주문이력 삭제가 실패하였습니다");

				alert.showAndWait();
			}
		} catch (SQLException se) {
			System.out.println("주문이력 삭제 sql문 오류" + se);
		} catch (Exception e) {
			System.out.println("주문 이력 삭제 진행 오류" + e);
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
