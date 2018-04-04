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

	// 신규 입고 목록 등록
	public WarehousingVO inputWarehousing(OrderVO ovo) throws Exception {

		// slq문 입력
		StringBuffer sql = new StringBuffer();
		sql.append("insert into WAREHOUSING ");
		sql.append("(no,w_num,w_pnum,w_pname,w_pkind,w_size,w_color,w_price,w_count,w_totalprice,w_date,w_filename)");
		sql.append("values(WAREHOUSING_seq.nextval,?,?,?,?,?,?,?,?,?,sysdate,?)");

		// 데이터베이스에 sql문 전송
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
			System.out.println("신규 입고 등록 sql 관련 오류" + se);

		} catch (Exception e) {
			System.out.println("신규 입고 등록 진행오류" + e);
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

	// 입고목록 전체 리스트
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
			System.out.println("전체입고목록 리스트 sql문 오류" + se);

		} catch (Exception e) {
			System.out.println("전체입고목록 리스트 관련오류" + e);

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
	public ArrayList<String> getWareColumnName() {
		ArrayList<String> columnName = new ArrayList<>();

		StringBuffer sql = new StringBuffer();
		sql.append("select * from warehousing");

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

			System.out.println("입고목록테이블컬럼갯수 산정 SQL문 오류" + se);

		} catch (Exception e) {
			System.out.println("입고목록 컬럼갯수 산정 관련 오류 " + e);

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

	// 입고 등록을 위해 선택한 물품이 이미 입고되었는지 검사하기위해 DB에서 검색
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

	// 입고등록된 물품을 재고테이블로 수량과 총액 전달
	public void inputOrderToStock(String pnum,int num) {

		StringBuffer sql = new StringBuffer();
		sql.append(" update stock set st_count = (select st_count+o_count 수량 ");
		sql.append(" from stock s,orderlist o");
		sql.append(" where st_pnum = o_pnum and o.no=? ), ");
		sql.append(" st_totalprice = (select (st_count+o_count)*st_price 총가격 ");
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
			System.out.println("입고등록시 재고량 변동 sql문오류" + se);
		} catch (Exception e) {
			System.out.println("입고등록시 재고량 변동 진행오류" + e);
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

	// 선택한 입고 정보 삭제
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
				alert.setTitle("입고 이력 삭제");
				alert.setHeaderText("입고 이력 삭제 완료");
				alert.setContentText("성공적으로 입고이력이 삭제되었습니다.");

				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("입고 이력 삭제");
				alert.setHeaderText("입고 이력 삭제 실패");
				alert.setContentText("입고이력 삭제가 실패하였습니다");

				alert.showAndWait();
			}

		} catch (SQLException se) {
			System.out.println("입고이력삭제 sql 문오류" + se);

		} catch (Exception e) {

			System.out.println("입고이력삭제 진행오류" + e);
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

	// 입고 목록에서 삭제시 재고 목록에서도 연동되도록 설정
	public void deleteWareToStock(String pnum,int num) {

		StringBuffer sql = new StringBuffer();
		sql.append(" update stock set st_count = (select st_count-w_count 수량 ");
		sql.append(" from stock s,warehousing w");
		sql.append(" where st_pnum = w_pnum and w.no= ?), ");
		sql.append(" st_totalprice = (select (st_count-w_count)*st_price 총가격 ");
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
			System.out.println("입고목록 삭제시 재고량 변동 sql문오류" + se);
		} catch (Exception e) {
			System.out.println("입고삭제시 재고량 변동 진행오류" + e);
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

	// 입고물품 총 주문량, 총합액 구하기
	public WarehousingVO getWareTotalCount(WarehousingVO wvo) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(w_count) 총수량,to_char(sum(w_totalprice),'l999,999,999') 총합 from warehousing ");

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
				wVo.setW_orderTotalCount(rs.getInt("총수량"));
				wVo.setW_orderTotalPrice(rs.getString("총합"));
			}
		} catch (SQLException se) {
			System.out.println("입고 총수량/총합 SQL관련 오류" + se);
		} catch (Exception e) {
			System.out.println("입고 총수량/총합 진행관련 오류" + e);
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

	// 년도 검색으로 조회된 물품의 총 재고량/총합계 구하기
	public WarehousingVO getWareYearTotalCount(String year) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(w_count) 총수량,to_char(sum(w_totalprice),'l999,999,999') 총합 from warehousing ");
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
				wVo.setW_orderTotalCount(rs.getInt("총수량"));
				wVo.setW_orderTotalPrice(rs.getString("총합"));
			}
		} catch (SQLException se) {
			System.out.println("입고목록 년도검색 총수량/총합 SQL관련 오류" + se);
		} catch (Exception e) {
			System.out.println("입고목록 년도검색 총수량/총합 진행관련 오류" + e);
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

	// 월 검색으로 조회된 물품의 총 재고량/총합계 구하기
	public WarehousingVO getWareMonthTotalCount(String month) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(w_count) 총수량,to_char(sum(w_totalprice),'l999,999,999') 총합 from warehousing ");
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
				wVo.setW_orderTotalCount(rs.getInt("총수량"));
				wVo.setW_orderTotalPrice(rs.getString("총합"));
			}
		} catch (SQLException se) {
			System.out.println("입고목록 월검색 총수량/총합 SQL관련 오류" + se);
		} catch (Exception e) {
			System.out.println("입고목록 월검색 총수량/총합 진행관련 오류" + e);
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

	// 년도 + 월 검색으로 조회된 물품의 총 재고량/총합계 구하기
	public WarehousingVO getWareYearMonthTotalCount(String year, String month) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(w_count) 총수량,to_char(sum(w_totalprice),'l999,999,999') 총합 from warehousing ");
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
				wVo.setW_orderTotalCount(rs.getInt("총수량"));
				wVo.setW_orderTotalPrice(rs.getString("총합"));
			}
		} catch (SQLException se) {
			System.out.println("입고내역 년+월검색 총수량/총합 SQL관련 오류" + se);
		} catch (Exception e) {
			System.out.println("입고내역 년+월검색 총수량/총합 진행관련 오류" + e);
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

	// 년도만으로 입고이력목록에서 검색
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
			System.out.println("년도로 입고목록조회부분 SQL문 오류" + se);
		} catch (Exception e) {
			System.out.println("년도로 입고목록부분 진행오류" + e);
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

	// 월만으로 주문이력목록에서 검색
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
			System.out.println("월로 입고목록조회부분 SQL문 오류" + se);
		} catch (Exception e) {
			System.out.println("월로 입고목록조회부분 진행오류" + e);
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

	// 년도와 월 모두 만족하는 주문내역 주문이력목록에서 검색
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
			System.out.println("년+월로 입고목록조회부분 SQL문 오류" + se);
		} catch (Exception e) {
			System.out.println("년+월로 입고목록조회부분 진행오류" + e);
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
