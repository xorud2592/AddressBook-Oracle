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
	private static final String INPUT_FORM_ERROR = "[올바른 전화번호 형식이 아닙니다]";
	private static final String DRIVE_ROAD_ERROR = "[드라이버 로드 실패!]";

	private static final String jdbc_driver = "oracle.jdbc.driver.OracleDriver";
	private static final String db_url = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String db_id = "C##LTK";
	private static final String db_pass = "1234";

	private static final String DB_TABLE_NAME = "phone_book";
	private static final String TABLE_SEQUENCE_NAME = "seq_phone_book.NEXTVAL";
	private static final String TABLE_ID_NAME = "id";
	private static final String TABLE_NAME_NAME = "name";
	private static final String TABLE_TEL_NAME = "tel";
	private static final String TABLE_HP_NAME = "hp";
	
	private static final String show_sql = "SELECT " + TABLE_ID_NAME +", " + TABLE_NAME_NAME + ", " + TABLE_TEL_NAME + ", " + TABLE_HP_NAME + 
											" FROM " + DB_TABLE_NAME + " ORDER BY " + TABLE_ID_NAME;
	
	private static final String insert_sql = "INSERT INTO " + DB_TABLE_NAME + 
											" VALUES(" + TABLE_SEQUENCE_NAME + ", ?, ?, ?)";
	
	private static final String delete_sql = "DELETE FROM " + DB_TABLE_NAME + 
											" WHERE " + TABLE_ID_NAME + "=?";
	
	private static final String search_sql = "SELECT " + TABLE_ID_NAME + ", " + TABLE_NAME_NAME + ", " + TABLE_TEL_NAME + ", " + TABLE_HP_NAME +
											" FROM " + DB_TABLE_NAME + " WHERE " + TABLE_NAME_NAME +" LIKE ? ORDER BY " + TABLE_ID_NAME;

	private Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			Class.forName(jdbc_driver);
			conn = DriverManager.getConnection(db_url, db_id, db_pass);
		} catch (ClassNotFoundException e) {
			System.err.println(DRIVE_ROAD_ERROR);
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

			String sql = show_sql;
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				Long id = rs.getLong(TABLE_ID_NAME);
				String name = rs.getString(TABLE_NAME_NAME);
				String tel = rs.getString(TABLE_TEL_NAME);
				String hp = rs.getString(TABLE_HP_NAME);

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
			String sql = insert_sql;

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
		Pattern telPattern = Pattern.compile("^[0-9]{3}-[0-9]{3,4}-[0-9]{4}$");
		Pattern hpPattern = Pattern.compile("^[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}$");

		Matcher telMatcher = telPattern.matcher(vo.getTel());
		Matcher hpMatcher = hpPattern.matcher(vo.getHp());

		if (telMatcher.find() && hpMatcher.find())
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
			String sql = delete_sql;
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
			String sql = search_sql;
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
