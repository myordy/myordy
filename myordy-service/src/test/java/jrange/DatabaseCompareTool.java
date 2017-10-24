package jrange;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public final class DatabaseCompareTool {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "";

	private static void compareTable(final Statement stmt, final String tableName) throws Exception {
		System.out.println("Comparing: " + tableName);
		ResultSet rs = stmt.executeQuery(
			"SELECT column_name,ordinal_position,data_type,column_type FROM"
				+ " ("
				+ "     SELECT"
				+ "         column_name,ordinal_position,"
				+ "         data_type,column_type,COUNT(1) rowcount"
				+ "     FROM information_schema.columns"
				+ "     WHERE"
				+ "     ("
				+ "         (table_schema='myordy' AND table_name='" + tableName + "') OR"
				+ "         (table_schema='myordy_pizzamax' AND table_name='" + tableName + "')"
				+ "     )"
				+ "     AND table_name IN ('" + tableName + "')"
				+ "     GROUP BY"
				+ "         column_name,ordinal_position,"
				+ "         data_type,column_type"
				+ "     HAVING COUNT(1)=1" + " ) A");

		while (rs.next()) {
			System.out.println("\t" + rs.getString(1) + ", " + rs.getString(2)
					+ ", " + rs.getString(3) + ", " + rs.getString(4));
		}

		rs.close();
	}

	public static void main(String[] args) throws Exception {
		Connection conn = null;
		Statement stmt = null;

		// STEP 2: Register JDBC driver
		Class.forName("com.mysql.jdbc.Driver");

		// STEP 3: Open a connection
		conn = DriverManager.getConnection(DB_URL, USER, PASS);

		stmt = conn.createStatement();
		String [] tables = {
			"customernumbersequence",
			"customer",
			"theusercontact_theuseraddress",
			"theusercontact_theuserphone",
			"theuseraddress",
			"theuserphone",
			"theuser",
			"posoperator",
			"theusercontact",
			"ordynumbersequence",
			"ordyitemextraoptionadd",
			"ordyitemextraoptionremove",
			"ordyitemextraoption",
			"ordyitemcombosub",
			"ordyitemcombo",
			"ordyitem",
			"ordy"
		};
		for (String tableName : tables) {
			compareTable(stmt, tableName);
		}

		stmt.close();
		conn.close();

	}// end main}
}