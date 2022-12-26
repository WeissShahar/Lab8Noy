package org.example;

import javax.persistence.*;
import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String depName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "department")
    private List<Patient> patients;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Head_Doctor")
    private Doctor doctor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public Department(String depName, Doctor doctor)
    {
        this.depName = depName;
        this.patients = new ArrayList<Patient>();
        setDoctor(doctor);
    }

    public Department() {
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
