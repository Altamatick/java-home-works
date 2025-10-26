package app;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @OneToMany(
            mappedBy = "student",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Homework> homeworks = new HashSet<>();

    // --- getters/setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<Homework> getHomeworks() { return homeworks; }
    public void setHomeworks(Set<Homework> homeworks) {
        // опціонально: перестворити зв'язки коректно
        this.homeworks = homeworks != null ? homeworks : new HashSet<>();
    }

    // --- helpers for bidirectional association ---

    public void addHomework(final Homework homework) {
        if (homework == null) return;
        if (this.homeworks.add(homework)) {
            homework.setStudent(this);
        }
    }

    public void removeHomework(final Homework homework) {
        if (homework == null) return;
        if (this.homeworks.remove(homework)) {
            if (homework.getStudent() == this) {
                homework.setStudent(null);
            }
        }
    }

    // --- equals/hashCode по id ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student that = (Student) o;
        // тільки id: два transient (id == null) не вважаємо однаковими
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", homeworksCount=" + (homeworks != null ? homeworks.size() : 0) +
                '}';
    }
}
