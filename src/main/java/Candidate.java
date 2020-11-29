import javax.persistence.*;

@Entity(name = "candidates")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String name;
    @Column
    private int experience;
    @Column
    private int salary;

    public Candidate() {
    }

    public static Candidate of(String name, int expirience, int salary) {
        Candidate student = new Candidate();
        student.name = name;
        student.experience = expirience;
        student.salary = salary;
        return student;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }
    @Override
    public String toString() {
        return String.format("Candidate: id=%s, name=%s, experience=%s, salary=%s", id, name, experience, salary);
    }
}
