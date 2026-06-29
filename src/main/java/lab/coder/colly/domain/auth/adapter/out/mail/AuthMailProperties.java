package lab.coder.colly.domain.auth.adapter.out.mail;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "colly.auth.mail")
public record AuthMailProperties(
        String from,
        String magicLinkBaseUrl,
        String appDeepLinkBase
) {

    public String resolvedFrom() {
        if (from == null || from.isBlank()) {
            return "no-reply@colly.app";
        }
        return from.trim();
    }

    public String resolvedMagicLinkBaseUrl() {
        if (magicLinkBaseUrl == null || magicLinkBaseUrl.isBlank()) {
            return "http://localhost:18080";
        }
        return magicLinkBaseUrl.trim().replaceAll("/$", "");
    }

    public String resolvedAppDeepLinkBase() {
        if (appDeepLinkBase == null || appDeepLinkBase.isBlank()) {
            return "collyapp://auth/magic-link";
        }
        return appDeepLinkBase.trim().replaceAll("/$", "");
    }
}
