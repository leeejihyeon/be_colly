package lab.coder.colly.domain.auth.domain.model;

public class AuthUser {

    private final Long id;
    private final String email;
    private final String name;

    private AuthUser(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public static AuthUser create(String email, String name) {
        return new AuthUser(null, email, name);
    }

    public static AuthUser restore(Long id, String email, String name) {
        return new AuthUser(id, email, name);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
