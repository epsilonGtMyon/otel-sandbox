package epsilongtmyon.sandbox.sandbox02;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class Sandbox02Util {

	public static HikariDataSource createHikariDataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
		config.setUsername("sa");
		config.setPassword("");

		HikariDataSource ds = new HikariDataSource(config);
		return ds;

	}
}
