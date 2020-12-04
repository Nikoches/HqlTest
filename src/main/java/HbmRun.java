import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class HbmRun {
    static Session session;
    static {
        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(Candidate.class);
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
        session = sessionFactory.openSession();
    }
    public static void main(String[] args) {
        HbmRun hbmRun = new HbmRun();
        hbmRun.printAll();
    }
    protected void addFive(){
        session.beginTransaction();
        Candidate one = Candidate.of("Vova", 2, 100);
        Candidate two = Candidate.of("Nikolay", 4, 170);
        Candidate three = Candidate.of("Nikita", 1, 80);
        Candidate four = Candidate.of("Arthur", 3, 150);
        Candidate five = Candidate.of("Evgeniy", 5, 210);

        session.save(one);
        session.save(two);
        session.save(three);
        session.save(four);
        session.save(five);
        session.getTransaction().commit();
        session.close();
    }
    protected void printAll() {
        Query query = session.createQuery("from candidates where salary < 100");
        for (Object st : query.list()) {
            System.out.println(st);
        }

    }
    protected void getByid(){

        Query query = session.createQuery("from Candidate s where s.id = :fId");
        query.setParameter("fId", 1);
        System.out.println(query.uniqueResult());
    }
    protected void update() {
        session.createQuery("update Candidate s set s.salary = :salary, s.name = :newName where s.id = :fId")
                .setParameter("salary", 270)
                .setParameter("newName", "olaf")
                .setParameter("fId", 1)
                .executeUpdate();
    }
    protected void remove() {
        session.createQuery("delete from Candidate where id = :fId")
                .setParameter("fId", 3)
                .executeUpdate();
    }
    protected void insert(){
        session.createQuery("insert into Candidate (name, salary, experience) "
                + "select concat(s.name, 'NEW'), s.salary + 5, s.experience  "
                + "from Candidate s where s.id = :fId")
                .setParameter("fId", 1)
                .executeUpdate();
    }
}
