package org.example;



import java.awt.desktop.PrintFilesEvent;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.print.Doc;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;


import org.example.Patient;
import org.example.Doctor;
import org.example.Department;
import org.example.Appointment;

public class App {
    private static Session session;

    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Department.class);
        configuration.addAnnotatedClass(Doctor.class);
        configuration.addAnnotatedClass(Patient.class);
        configuration.addAnnotatedClass(Appointment.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

    private static void initializeData() throws Exception {


        Doctor Oren = new Doctor("Oren", "Weiman", "oren@gmail.com", "blabla");
        Doctor Moti = new Doctor("Moti", "Makiato", "moti@gmail.com", "blabla");
        Doctor Yakov = new Doctor("Yakov", "Rega", "yakov@gmail.com", "blabla");
        Doctor Sima = new Doctor("Stav", "Vaknin", "sima@gmail.com", "blabla");
        Doctor Stav = new Doctor("Stav", "Solo", "stav@gmail.com", "blabla");
        Doctor Toto = new Doctor("Toto", "Alice", "toto@gmail.com", "blabla");

        session.flush();

        session.save(Oren);
        session.save(Moti);
        session.save(Yakov);
        session.save(Sima);
        session.save(Stav);
        session.save(Toto);

        Department A = new Department("Eyes",Oren);
        Department B = new Department("General",Stav);
        Department C = new Department("Ears",Toto);

        session.flush();
        session.save(A);
        session.save(B);
        session.save(C);

        Patient Shahar = new Patient(7213, "Shahar", "Weiss", LocalDateTime.of(2015,
                Month.JULY, 29, 19, 30, 40),A);

        Patient Noy = new Patient(45, "Noy", "Blitzblau", LocalDateTime.of(2016,
                Month.JULY, 19, 12, 20, 10), B);

        Patient Daniel = new Patient(96, "Daniel", "Glazeman", LocalDateTime.of(2022,
                Month.JULY, 1, 2, 20, 50), C);


        session.save(Shahar);
        session.save(Noy);
        session.save(Daniel);


        session.flush();
        Shahar.addAppointment((LocalDateTime.of(2020, Month.JULY, 1, 2, 20, 50)), "Flue", Oren);
        Oren.addAppointment((LocalDateTime.of(2020, Month.JULY, 1, 2, 20, 50)), "Stomach", Noy);
        session.save(Oren);
        session.save(Shahar);

//        Appointment F = new Appointment(LocalDateTime.of(2020, Month.JULY, 1, 2, 20, 50), "broken arm", Oren, Shahar);
//        Appointment G = new Appointment(LocalDateTime.of(2021, Month.JULY, 1, 2, 20, 50), "surgery", Stav, Shahar);
//        Appointment H = new Appointment(LocalDateTime.of(2022, Month.JULY, 1, 2, 20, 50), "blood test", Toto, Shahar);
//        session.save(H);
//        session.save(G);
//        session.save(F);

        session.getTransaction().commit();
    }

    public static <T> List<T> getAll(Class<T> object) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(object);
        Root<T> rootEntry = criteriaQuery.from(object);
        CriteriaQuery<T> allCriteriaQuery = criteriaQuery.select(rootEntry);

        TypedQuery<T> allQuery = session.createQuery(allCriteriaQuery);
        return allQuery.getResultList();
    }

    public static void main(String[] args) {
        try {

            //Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            initializeData();

            List<Appointment> a = getAll(Appointment.class);

            String hql = "FROM Patient f ORDER BY patientNumber";
            Query query = session.createQuery(hql);
            List<Patient> patients  = query.getResultList();

            System.out.println("Patients list:");
            for (Patient patient : patients) {
                System.out.format("ID: %d, PatientNumber: %d, Name: %s %s, Date: %s Department:%s\n",
                        patient.getId(),patient.getPatientNumber(), patient.getFirstName(),
                        patient.getLastName(), patient.getDateOfIshpuz(),patient.getDepartment().getDepName());
            }

            List<Department> departments = getAll(Department.class);
            System.out.println("\n\nDepartments list:");
            for (Department department:departments)
            {
                System.out.format("ID: %d, department Name: %s, Head Doctor: %s %s,\n",
                        department.getId(),department.getDepName(), department.getDoctor().getFirstName(),
                        department.getDoctor().getLastName());

            }

            hql = "FROM Doctor ORDER BY firstName ASC , lastName ASC ";
            query = session.createQuery(hql);
            List<Doctor> doctors  = query.getResultList();

            System.out.println("\n\nDoctors list:");
            for (Doctor doctor :doctors)
            {
                System.out.format("ID: %d, Name: %s %s, Email: %s, password %s\n",
                        doctor.getId(), doctor.getFirstName(),doctor.getLastName(),doctor.getEmail(),
                        doctor.getPassword());
            }

            System.out.format("\n\n");
            System.out.println("Done!");


        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }

            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
                session.getSessionFactory().close();
            }
        }
    }
}