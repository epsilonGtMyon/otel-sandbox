package epsilongtmyon.sandbox.sandbox02;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import com.zaxxer.hikari.HikariDataSource;

import epsilongtmyon.common.db.DbInitializer;
import epsilongtmyon.common.db.mybatis.MyLog;
import epsilongtmyon.common.db.mybatis.MyTable;
import epsilongtmyon.sandbox.sandbox02.mapper.MyLogAnnotationMapper;
import epsilongtmyon.sandbox.sandbox02.mapper.MyTableAnnotationMapper;
import io.opentelemetry.instrumentation.annotations.WithSpan;

public class MyBatis3Sandbox01Main {
	private static Logger logger = Logger.getLogger(MyBatis3Sandbox01Main.class.getName());

	public static void main(String[] args) throws Exception {

		var main = new MyBatis3Sandbox01Main();
		main.doMain();

		System.out.println("wait..");
		System.in.read();
	}

	@WithSpan
	private void doMain() {

		try (HikariDataSource ds = Sandbox02Util.createHikariDataSource()) {

			SqlSessionFactory sqlSessionFactory = createSqlSessionFactory(ds);

			try (SqlSession sqlSession = sqlSessionFactory.openSession(false)) {
				Connection con = sqlSession.getConnection();
				DbInitializer.initialize(con, "init-db.sql");
				sqlSession.commit();

				start1(sqlSession);

			}
		}
	}

	private void start1(SqlSession sqlSession) {
		MyTableAnnotationMapper myTableMapper = sqlSession.getMapper(MyTableAnnotationMapper.class);
		MyLogAnnotationMapper myLogMapper = sqlSession.getMapper(MyLogAnnotationMapper.class);

		List<MyTable> myTables = myTableMapper.findAll();
		myTables.stream().forEach(System.out::println);

		MyLog myLog1 = new MyLog();
		myLog1.setLogMessage("てすと");
		myLogMapper.insert(myLog1);
	}

	// xmlを使わず手動でSqlSessionFactoryを作る
	private static SqlSessionFactory createSqlSessionFactory(DataSource dataSource) {
		Environment environment = new Environment("deployment", new JdbcTransactionFactory(), dataSource);
		Configuration config = new Configuration(environment);

		config.setMapUnderscoreToCamelCase(true);
		config.setLogImpl(Slf4jImpl.class);
		config.addMappers(MyBatis3Sandbox01Main.class.getPackageName() + ".mapper");

		Collection<Class<?>> mappers = config.getMapperRegistry().getMappers();
		logger.log(Level.INFO, "mappers={0}", new Object[] { mappers });

		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(config);

		return sqlSessionFactory;
	}

}
