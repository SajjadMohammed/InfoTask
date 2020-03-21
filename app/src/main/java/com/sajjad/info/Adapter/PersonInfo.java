package com.sajjad.info.Adapter;

public class PersonInfo {
    private int id;
    private String personName,personSaying;
    private byte[] personPicture;

    public PersonInfo(int id, String personName, String personSaying, byte[] personPicture) {
        this.id = id;
        this.personName = personName;
        this.personSaying = personSaying;
        this.personPicture = personPicture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonSaying() {
        return personSaying;
    }

    public void setPersonSaying(String personSaying) {
        this.personSaying = personSaying;
    }

    public byte[] getPersonPicture() {
        return personPicture;
    }

    public void setPersonPicture(byte[] personPicture) {
        this.personPicture = personPicture;
    }
}
