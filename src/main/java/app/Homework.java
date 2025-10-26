package app;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "homework")
public class Homework {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @Column(name = "deadline", nullable = false)
    private LocalDate deadline;

    @Column(name = "mark", nullable = false)
    private int mark;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_homework_student"))
    private Student student;

    // --- getters/setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public int getMark() { return mark; }
    public void setMark(int mark) { this.mark = mark; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    // --- equals/hashCode по id ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Homework)) return false;
        Homework that = (Homework) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Homework{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", mark=" + mark +
                ", studentId=" + (student != null ? student.getId() : null) +
                '}';
    }
}
