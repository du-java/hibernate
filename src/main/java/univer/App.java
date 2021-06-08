package univer;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import univer.model.Group;
import univer.model.Student;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure("persistence.xml");
        configuration.addAnnotatedClass(Group.class);
        configuration.addAnnotatedClass(Student.class);

        final SessionFactory sessionFactory = configuration.buildSessionFactory();
        final Session session = sessionFactory.openSession();

//        final NativeQuery nativeQuery = session.createNativeQuery("select 1+1");
//        System.out.println(nativeQuery.getResultList());
//
//        final Query<Integer> cq = session.createQuery("select 3+3", Integer.class);
//        final Integer singleResult = cq.getSingleResult();
//        System.out.println(singleResult);

//        addGroups(session);
//        addStudents(session);
//        updateStud(session);
//        ex(session);


        final Query<Group> from_group = session.createQuery("from Group", Group.class);
        System.out.println(from_group.getResultList());


//        final Query<Group> from_group1 = session.createQuery("from Group g where g.id = 3", Group.class);
        final Query<Group> from_group1 = session.createQuery("from Group where id = 3", Group.class);
        System.out.println(from_group1.getResultList());

//        final Criteria criteria = session.createCriteria(Student.class);
//        criteria.add(Restrictions.like("firstname", "Ivan"));
//        for (Object o : criteria.list()) {
//            System.out.println(o);
//        }

        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<Student> cq = cb.createQuery(Student.class);
        final Root<Student> root = cq.from(Student.class);
        cq.where(cb.like(root.get("firstname"), "Ivan"));
        List<Student> results = session.createQuery(cq).getResultList();

        System.out.println(results);

        sessionFactory.close();
    }

    private static void addGroups(Session session) {
        Transaction transaction = session.beginTransaction();
        try {

            final Group gr1 = Group.builder().name("gr1").build();
            final Group gr2 = Group.builder().name("gr2").build();

            session.persist(gr1);
            session.persist(gr2);

            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
        }
    }

    private static void addStudents(Session session) {
        Transaction transaction = session.beginTransaction();
        try {


            final Student s1 = Student.builder()
                    .firstname("Ivan")
                    .lastname("Ivanov")
                    .build();
            final Student s2 = Student.builder()
                    .firstname("Petr")
                    .lastname("Petrov")
                    .build();

            session.save(s1);
            session.save(s2);

            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
        }
    }

    private static void updateStud(Session session) {
        Transaction transaction = session.beginTransaction();
        try {
            final Group group1 = session.get(Group.class, 3L);
//            final Group group2 = session.find(Group.class, 4);


            final Student s1 = session.get(Student.class, 1L);
            final Student s2 = session.get(Student.class, 2L);

            if (s1 != null) {
                s1.setGroup(group1);
            }
            if (s2 != null) {
                s2.setGroup(group1);
            }

            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
        }
    }

    private static void ex(Session session) {
        Transaction transaction = session.beginTransaction();
        try {
            final Group group1 = session.get(Group.class, 4L);

            final Student s1 = session.get(Student.class, 1L);
            final Student s2 = session.get(Student.class, 2L);

            if (s1 != null) {
                s1.setGroup(group1);
            }
            if (s2 != null) {
                s2.setGroup(group1);
            }

            final Group group = session.get(Group.class, 1L);
            throw new IllegalStateException();

        } catch (Exception ex) {
            transaction.rollback();
        }
    }
}
