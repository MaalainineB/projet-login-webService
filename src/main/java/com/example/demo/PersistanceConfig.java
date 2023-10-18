package com.example.demo;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
@MapperScan("com.example.demo.mappers") //indique à Spring de rechercher les interfaces de mappage MyBatis dans le package 
public class PersistanceConfig {

	@Value("classpath:schema.sql")
	private Resource schemaScript;

	@Value("classpath:data.sql")
	private Resource dataScript;

	@Autowired
	private DataSource dataSource;

	@Bean
	public DataSource dataSource() { //il s'agit d'une source de données JDBC (JDBC DriverManagerDataSource) configurée pour se connecter à une base de données PostgreSQL.
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl("jdbc:postgresql://localhost:5432/loginAuthJwt-mybatis");
		dataSource.setUsername("postgres");
		dataSource.setPassword("1234");
		return dataSource;
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception { // Cette méthode configure et retourne une instance de l'usine de session MyBatis (SqlSessionFactory). Elle est configurée pour utiliser la source de données précédemment configurée.
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource());
		return factoryBean.getObject();
	}

	@Bean
	public SqlSession sqlSession() throws Exception { //Cette méthode configure et retourne une session MyBatis (SqlSession). La session MyBatis est configurée pour utiliser l'usine de session précédemment configurée.
		return new SqlSessionTemplate(sqlSessionFactory());
	}

	@Bean
	public DataSourceInitializer dataSourceInitializer() { //Cette méthode configure un initialisateur de source de données (DataSourceInitializer). Cet initialisateur est utilisé pour exécuter des scripts SQL pour initialiser la base de données.
		DataSourceInitializer initializer = new DataSourceInitializer();
		initializer.setDataSource(dataSource);

		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
		databasePopulator.addScript(schemaScript); //Ces scripts SQL seront exécutés au démarrage de l'application pour créer et peupler la base de données.
		databasePopulator.addScript(dataScript);

		initializer.setDatabasePopulator(databasePopulator);

		return initializer;
	}
}
