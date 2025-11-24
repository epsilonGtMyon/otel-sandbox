package epsilongtmyon.sandbox.sandbox02;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import epsilongtmyon.common.db.DbInitializer;
import io.opentelemetry.instrumentation.annotations.WithSpan;

public class JdbcSandbox01Main {

	public static void main(String[] args) throws Exception {

		var main = new JdbcSandbox01Main();
		main.start1();

		System.out.println("wait..");
		System.in.read();
	}

	@WithSpan
	private void start1() throws SQLException {

		try (Connection con = getConnection()) {
			con.setAutoCommit(false);

			DbInitializer.initialize(con, "init-db.sql");
			con.commit();

			try (Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("""
select
  *
from
  MY_TABLE
order by
  iD
							""")) {
				
				while(rs.next()) {
					System.out.println();
					System.out.println(rs.getBigDecimal(1));
					System.out.println(rs.getString(2));
				}
				
			}

		}

	}

	private static Connection getConnection() throws SQLException {

		return DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
	}
}
