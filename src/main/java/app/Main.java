package app;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Hibernate...");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hillel-persistence-unit");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // Створюємо нового студента
            Student student = new Student("Denys");
            em.persist(student);

            // Комітимо, щоб запис з'явився в БД
            em.getTransaction().commit();

            // Перевіряємо, що можна зчитати назад
            Student found = em.find(Student.class, student.getId());
            System.out.println("Found student: " + found);

        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
            emf.close();
        }

        System.out.println("Shutdown complete.");
    }
}
