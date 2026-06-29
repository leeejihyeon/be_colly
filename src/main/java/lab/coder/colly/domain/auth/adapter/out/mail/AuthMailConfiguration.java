package lab.coder.colly.domain.auth.adapter.out.mail;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AuthMailProperties.class)
public class AuthMailConfiguration {
}
