package ca.com.arnon.integrationtests.testcontainers;

import java.util.Map;
import java.util.stream.Stream;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)

public class AbstractIntegrationTest {

	public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext>{

		static MySQLContainer<?> mySql = new MySQLContainer<>("mysql:8.0.29");
		
		private static void startContainers() {
			Startables.deepStart(Stream.of(mySql)).join();
		}
		
		private static Map<String, String> createConectionConfiguration() {
			return Map.of(
					"spring.datasource.url", mySql.getJdbcUrl(),
					"spring.datasource.username", mySql.getUsername(),
					"spring.datasource.password", mySql.getPassword()
				);
		}

		@SuppressWarnings({"rawtypes","unchecked"})
		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			startContainers();
			ConfigurableEnvironment enviroment = applicationContext.getEnvironment();
			MapPropertySource testContainers = new MapPropertySource("testcontainers", (Map) createConectionConfiguration());
			enviroment.getPropertySources().addFirst(testContainers);
		}
	}
}