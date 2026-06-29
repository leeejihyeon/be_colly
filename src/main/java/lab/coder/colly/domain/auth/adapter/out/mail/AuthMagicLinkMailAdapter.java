package lab.coder.colly.domain.auth.adapter.out.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lab.coder.colly.domain.auth.application.port.out.AuthMagicLinkMailPort;
import lab.coder.colly.shared.error.DomainException;
import lab.coder.colly.shared.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthMagicLinkMailAdapter implements AuthMagicLinkMailPort {

    private final JavaMailSender javaMailSender;
    private final AuthMailProperties authMailProperties;

    @Override
    public void sendMagicLink(
            String email,
            String token,
            long expireIn
    ) {
        String appLink = authMailProperties.resolvedAppDeepLinkBase() + "?token=" + token;
        String verifyUrl = authMailProperties.resolvedMagicLinkBaseUrl() + "/api/auth/magic-link/verify?token=" + token;

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

            helper.setTo(email);
            helper.setFrom(authMailProperties.resolvedFrom());
            helper.setSubject("[Colly] Your magic login link");
            helper.setText(buildHtml(appLink, verifyUrl, expireIn), true);

            javaMailSender.send(message);
        } catch (MailException | MessagingException ex) {
            throw new DomainException(
                    ErrorCode.MAGIC_LINK_EMAIL_SEND_FAILED,
                    "Failed to send magic link email"
            );
        }
    }

    private String buildHtml(
            String appLink,
            String verifyUrl,
            long expireIn
    ) {
        return """
                <div style=\"font-family:Arial,sans-serif;line-height:1.5;color:#111\">
                  <h2>Colly sign-in link</h2>
                  <p>Click the button below to sign in. This link expires in %d minutes.</p>
                  <p>
                    <a href=\"%s\" style=\"display:inline-block;padding:10px 14px;background:#4C4DDC;color:#fff;text-decoration:none;border-radius:8px;\">Sign in to Colly</a>
                  </p>
                  <p>If the app does not open, use this token verify URL manually:</p>
                  <p><a href=\"%s\">%s</a></p>
                </div>
                """.formatted(expireIn / 60, appLink, verifyUrl, verifyUrl);
    }
}
