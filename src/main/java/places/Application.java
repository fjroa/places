package places;

import java.util.Arrays;
import java.util.Map;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Bean
	Facebook facebook(@Value("${facebook.appID}") String appId,
			@Value("${facebook.appSecret}") String appSecret) {
		return new FacebookTemplate(appId + " | " + appSecret);
	}

	@Bean
	RestTemplate restTemplate(@Value("${http.proxy.host}") String host,
			@Value("${http.proxy.port}") Integer port,
			@Value("${http.proxy.username}") String username,
			@Value("${http.proxy.password}") String password) {
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(host, port),
				new UsernamePasswordCredentials(username, password));
		HttpClient httpClient = HttpClients.custom()
				.setDefaultCredentialsProvider(credsProvider).build();
		return new RestTemplate(new HttpComponentsClientHttpRequestFactory(
				httpClient));
	}

	@Bean
	CommandLineRunner init(RestTemplate restTemplate, Facebook facebook,
			PlaceRepository placeRepository) {

		return args -> {

			String ip = restTemplate.getForObject("http://icanhazip.com",
					String.class).trim();

			Map<?, ?> loc = restTemplate.getForObject(
					"http://freegeoip.net/json/{ip}", Map.class, ip);

			double longitude = (Double) loc.get("longitude");
			double latitude = (Double) loc.get("latitude");

			logger.info("the IP of the current machine is: " + ip);
			logger.info("latitude & longitude: " + loc.toString());

			placeRepository.deleteAll();

			logger.info("all records near current IP:");

			Arrays.asList("Starbucks", "Philz", "Ike's Place", "Bite", "Umami")
					.forEach(
							q -> facebook
									.placesOperations()
									.search(q, latitude, longitude, 50000 - 1)
									.stream()
									.map(p -> placeRepository
											.save(new Place(p)))
									.forEach(System.out::println));

			logger.info("zooming in.. =");
			placeRepository.findByPositionNear(new Point(longitude, latitude),
					new Distance(2, Metrics.KILOMETERS)).forEach(
					System.out::println);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
