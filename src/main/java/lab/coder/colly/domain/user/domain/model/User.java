package lab.coder.colly.domain.user.domain.model;

public class User {

    private final Long id;
    private final String email;
    private final String name;

    private User(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public static User create(String email, String name) {
        return new User(null, email, name);
    }

    public static User restore(Long id, String email, String name) {
        return new User(id, email, name);
    }

    public User withId(Long newId) {
        return new User(newId, email, name);
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
