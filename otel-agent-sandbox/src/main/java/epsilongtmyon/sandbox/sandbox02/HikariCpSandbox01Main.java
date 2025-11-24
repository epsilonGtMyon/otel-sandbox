package epsilongtmyon.sandbox.sandbox02;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import epsilongtmyon.common.db.DbInitializer;
import io.opentelemetry.instrumentation.annotations.WithSpan;

public class HikariCpSandbox01Main {

	public static void main(String[] args) throws Exception {

		var main = new HikariCpSandbox01Main();
		main.start1();

		System.out.println("wait..");
		System.in.read();
	}

	@WithSpan
	private void start1() throws SQLException {

		try (HikariDataSource ds = getDataSource()) {

			try (Connection con = ds.getConnection()) {
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

					while (rs.next()) {
						System.out.println();
						System.out.println(rs.getBigDecimal(1));
						System.out.println(rs.getString(2));
					}

				}

			}
		}

	}

	private static HikariDataSource getDataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
		config.setUsername("sa");
		config.setPassword("");

		HikariDataSource ds = new HikariDataSource(config);
		return ds;

	}
}
