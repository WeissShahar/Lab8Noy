package org.example;

import javax.persistence.*;
import javax.print.Doc;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table (name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int patientNumber;

    private String firstName;

    private String lastName;

    private LocalDateTime dateOfIshpuz;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;


    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;

//
//    public void addDoctors(Doctor... doctors) {
//        for (Doctor doctor : doctors) {
//            doctorList.add(doctor);
//            doctor.getPatients().add(this); // ESPECIALLY THIS LINE
//        }
//    }


    public Patient() {
    }

    public Patient(int PatientNumber, String firstName, String lastName, LocalDateTime Ishpuz, Department department){
            this.patientNumber = PatientNumber;
            this.firstName = firstName;
            this.lastName = lastName;
            this.dateOfIshpuz = Ishpuz;
//        this.doctorList = new ArrayList<Doctor>();
//        this.appointmentList = new ArrayList<Appoitment>();
            setDepartment(department);
        }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(int patientNumber) {
        this.patientNumber = patientNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getDateOfIshpuz() {
        return dateOfIshpuz;
    }

    public void setDateOfIshpuz(LocalDateTime dateOfIshpuz) {
        this.dateOfIshpuz = dateOfIshpuz;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
        department.getPatients().add(this);
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}