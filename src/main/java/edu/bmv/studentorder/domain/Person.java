package edu.bmv.studentorder.domain;

import java.time.LocalDate;

public  abstract class Person {
    private String surName;
    private String givenName;
    private String patronymicName;
    private LocalDate dateOfBirth;
    private Address address;

    public Person(){}

    public Person(String surName, String givenName, String patronymicName, LocalDate dateOfBirth) {
        this.surName = surName;
        this.givenName = givenName;
        this.patronymicName = patronymicName;
        this.dateOfBirth = dateOfBirth;
    }

    public String getPerson(){
        return surName + " " + givenName + " " + patronymicName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getPatronymicName() {
        return patronymicName;
    }

    public void setPatronymicName(String patronymicName) {
        this.patronymicName = patronymicName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "surName='" + surName + '\'' +
                ", givenName='" + givenName + '\'' +
                ", patronymicName='" + patronymicName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", address=" + address +
                '}';
    }
}
