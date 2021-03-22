package com.ltk.phone.addressbook.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressDaoOraclempl implements AddressDao {
	static final String INPUT_NUMBER_ERROR = "숫자만 입력해주세요";
	static final String INPUT_FORM_ERROR = "[올바른 전화번호 형식이 아닙니다]";

	private Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String dburl = "jdbc:oracle:thin:@localhost:1521:xe";
			conn = DriverManager.getConnection(dburl, "C##LTK", "1234");
		} catch (ClassNotFoundException e) {
			System.err.println("드라이버 로드 실패!");
		}
		return conn;
	}

	@Override
	public List<AddressVo> getList() {
		List<AddressVo> list = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			stmt = conn.createStatement();

			String sql = "SELECT id, name, tel, hp FROM phone_book ORDER BY id";
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				Long id = rs.getLong("id");
				String name = rs.getString("name");
				String tel = rs.getString("tel");
				String hp = rs.getString("hp");

				AddressVo vo = new AddressVo(id, name, tel, hp);
				list.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				stmt.close();
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public boolean insert(AddressVo vo) {
		int insertedCount = 0;
		if (checkNumber(vo)) {
			Connection conn = null;
			String sql = "INSERT INTO phone_book VALUES(seq_phone_book.NEXTVAL, ?, ?, ?)";

			try {
				conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, vo.getName());
				pstmt.setString(2, vo.getHp());
				pstmt.setString(3, vo.getTel());

				insertedCount = pstmt.executeUpdate();
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return insertedCount == 1;
	}

	public boolean checkNumber(AddressVo vo) {
		Pattern phoneNumberPattern = Pattern.compile("^[0-9]{3}-[0-9]{3,4}-[0-9]{4}$");
		Pattern houseNumberPattern = Pattern.compile("^[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}$");

		Matcher phoneNumberMatcher = phoneNumberPattern.matcher(vo.getTel());
		Matcher houseNumberMatcher = houseNumberPattern.matcher(vo.getHp());

		if (phoneNumberMatcher.find() && houseNumberMatcher.find()) 
				return true;
		
		System.out.println(INPUT_FORM_ERROR);
		return false;
	}
	

	@Override
	public boolean delete(Long id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int deletedCount = 0;

		try {
			conn = getConnection();
			String sql = "DELETE FROM phone_book WHERE id=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, id);
			deletedCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return deletedCount == 1;
	}

	@Override
	public List<AddressVo> search(String target) {
		List<AddressVo> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			String sql = "SELECT id, name, tel, hp FROM phone_book WHERE name LIKE ? ORDER BY id";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + target + "%");

			rs = pstmt.executeQuery();

			while (rs.next()) {
				AddressVo vo = new AddressVo();
				vo.setId(rs.getLong(1));
				vo.setName(rs.getString(2));
				vo.setTel(rs.getString(3));
				vo.setHp(rs.getString(4));

				list.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				pstmt.close();
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}