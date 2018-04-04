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

	// 신규 상품등록
	public StockVO inputNewGoods(StockVO svo) throws Exception {
		// 오라클로 데이터 처리하기위한 SQL문
		StringBuffer sql = new StringBuffer();
		sql.append("insert into stock");
		sql.append("(no,st_pnum,st_pname,st_pkind,st_size,st_color,st_price,st_count,st_totalprice,st_filename)");
		sql.append("values(stock_seq.nextval,?,?,?,?,?,?,0,?,?)");

		Connection con = null;
		PreparedStatement pstmt = null;
		StockVO sVo = svo;

		try {
			// DBUtil 의 메소드를 이용하여 접속
			con = DBUtil.getConnection();

			// 입력받은 정보를 처리하기위한 SQL문
			pstmt = con.prepareStatement(sql.toString());

			pstmt.setString(1, sVo.getSt_pnum());
			pstmt.setString(2, sVo.getSt_pname());
			pstmt.setString(3, sVo.getSt_pkind());
			pstmt.setString(4, sVo.getSt_size());
			pstmt.setString(5, sVo.getSt_color());
			pstmt.setInt(6, sVo.getSt_price());
			pstmt.setInt(7, sVo.getSt_price() * sVo.getSt_count());
			pstmt.setString(8, sVo.getSt_filename());

			// SQL문을 수행후 처리 결과를 얻어옴
			int i = pstmt.executeUpdate();

		} catch (SQLException e) {

			System.out.println("입력SQL수행문 처리오류" + e);
		} catch (Exception e) {
			System.out.println("DAO입력 관련 오류" + e);
		} finally {
			try {
				// 데이터베이스와의 연결에 사용되었던 오브젝트 해제
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

	// 전체 재고 리스트
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
	// 데이터베이스 재품 테이블의 컬럼갯수
	public ArrayList<String> getColumnName() {
		ArrayList<String> columnName = new ArrayList<>();

		StringBuffer sql = new StringBuffer();
		sql.append("select * from stock");

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

			System.out.println("테이블컬럼갯수 산정 SQL문 오류" + se);

		} catch (Exception e) {
			System.out.println("테이블 컬럼갯수 산정 관련 오류 " + e);

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

	// 정보조회 상품의 품번으로 정보조회
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
			System.out.println("품명으로 조회부분 SQL문 오류" + se);
		} catch (Exception e) {
			System.out.println("품명으로 조회부분 진행오류" + e);
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

	// 정보조회 상품의 품종으로 정보조회
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
			System.out.println("품종으로 조회부분 SQL문 오류" + se);
		} catch (Exception e) {
			System.out.println("품종으로 조회부분 진행오류" + e);
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

	// 선택한 정보 삭제
	public void getStockDelete(int no) throws Exception {
		// sql문
		StringBuffer sql = new StringBuffer();
		sql.append("delete from stock where no=?");
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
				alert.setTitle("상품 정보 삭제");
				alert.setHeaderText("상품 정보 삭제 완료");
				alert.setContentText("정상적으로 삭제가 완료 되었습니다.");

				alert.showAndWait();
			} else {

				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("상품 정보 삭제");
				alert.setHeaderText("상품 정보 삭제 실패");
				alert.setContentText("상품 정보 삭제에 실패하였습니다.");

				alert.showAndWait();

			}
		} catch (SQLException se) {
			System.out.println("정보 삭제 관련 sql문 오류" + se);
		} catch (Exception e) {
			System.out.println("정보 삭제 관련 진행 오류" + e);
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

	// 선택한 제품 정보 수정
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
				alert.setTitle("상품 정보 수정");
				alert.setHeaderText("상품 정보 수정 완료");
				alert.setContentText("정보수정이 성공적으로 완료되었습니다.");

				alert.showAndWait();

				sVo = new StockVO();
			} else {

				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("상품 정보 수정");
				alert.setHeaderText("상품 정보 수정 실패");
				alert.setContentText("정보수정에 실패하였습니다.");

				alert.showAndWait();
			}

		} catch (SQLException se) {

			System.out.println("정보수정SQL문 오류" + se);

		} catch (Exception e) {
			System.out.println("정보수정 진행 오류" + e);

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

	// 총 재고량 구하기
	public StockVO getStockTotalCount(StockVO svo) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(st_count) 총수량, to_char(sum(st_totalprice),'l999,999,999') 총합 from stock ");

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
				sVo.setSt_stockTotalCount(rs.getInt("총수량"));
				sVo.setSt_stockTotalPrice(rs.getString("총합"));
			}
		} catch (SQLException se) {
			System.out.println("총수량/총합 SQL관련 오류" + se);
		} catch (Exception e) {
			System.out.println("총수량/총합 진행관련 오류" + e);
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

	// 품번 검색으로 조회된 물품의 총 재고량/총합계 구하기
	public StockVO getStockPnumTotalCount(String pnum) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(st_count) 총수량, to_char(sum(st_totalprice),'l999,999,999') 총합 from stock ");
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
				sVo.setSt_stockTotalCount(rs.getInt("총수량"));
				sVo.setSt_stockTotalPrice(rs.getString("총합"));
			}
		} catch (SQLException se) {
			System.out.println("품번검색 총수량/총합 SQL관련 오류" + se);
		} catch (Exception e) {
			System.out.println("품번검색 총수량/총합 진행관련 오류" + e);
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

	// 품종 검색으로 조회된 물품의 총 재고량/총합계 구하기
	public StockVO getStockPkindTotalCount(String pkind) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select sum(st_count) 총수량, to_char(sum(st_totalprice),'l999,999,999') 총합 from stock ");
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
				sVo.setSt_stockTotalCount(rs.getInt("총수량"));
				sVo.setSt_stockTotalPrice(rs.getString("총합"));
			}
		} catch (SQLException se) {
			System.out.println("품종검색 총수량/총합 SQL관련 오류" + se);
		} catch (Exception e) {
			System.out.println("품종검색 총수량/총합 진행관련 오류" + e);
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

	// 해당물품의 갯수 산정
	public int countStockGoods(String pnum) {
		int i = 0;

		StringBuffer sql = new StringBuffer();
		sql.append("select st_count 현수량 from stock where st_pnum = ? ");

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

				i = rs.getInt("현수량");
			}

		} catch (SQLException se) {
			System.out.println("재고 물품갯수 산정 sql문 오류" + se);
		} catch (Exception e) {
			System.out.println("재고 물품갯수 산정 진행오류" + e);
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

	// 현재 상품으로 등록된 상품번호 불러오기
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
			System.out.println("재고등록된 물품량 조회 sql오류" + se);
		} catch (Exception e) {
			System.out.println("재고등록된 물품량 조회 진행오류" + e);
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

	// 데이터 베이스테이블에 입력한 품번이 있는지 조회
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
			System.out.println("동일 품번 조회 sql오류" + se);
		} catch (Exception e) {
			System.out.println("동일 품번 조회 진행 오류" + e);
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

	// 재고의 수량들만 뽑아서 리스트로 저장
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
			System.out.println("재고들 수량 검색 sql오류" + se);
		} catch (Exception e) {
			System.out.println("현 재고 수량 검색 진행오류" + e);
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

	// 선택한 상품번호의 총 주문 량 조회
	public int countTotalOrder(String pnum) {
		int i = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(o_count) 총주문량 from orderlist where o_pnum = ? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, pnum);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				i = rs.getInt("총주문량");
			}
		} catch (SQLException se) {
			System.out.println("재고 품번과 같은 상품 총주문량 산출 sql문 오류" + se);
		} catch (Exception e) {
			System.out.println("재고 품번과 같은 상품 총 주문량 산출 진행오류" + e);
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

	// 선택한 상품번호의 총 판매 량 조회
	public int countTotalSales(String pnum) {
		int i = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(sa_count) 총판매량 from sales where sa_pnum = ? ");

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, pnum);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				i = rs.getInt("총판매량");
			}
		} catch (SQLException se) {
			System.out.println("재고 품번과 같은 상품 총판매량 산출 sql문 오류" + se);
		} catch (Exception e) {
			System.out.println("재고 품번과 같은 상품 총판매량 산출 진행오류" + e);
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
