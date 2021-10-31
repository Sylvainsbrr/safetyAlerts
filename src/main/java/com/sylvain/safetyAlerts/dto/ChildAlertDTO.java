package com.sylvain.safetyAlerts.dto;

import java.util.List;

public class ChildAlertDTO {
    private String fistName;
    private String lastName;
    private int age;
    private List<String> familyMember;

    public String getFistName() {
        return fistName;
    }

    public void setFistName(String fistName) {
        this.fistName = fistName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getFamilyMember() {
        return familyMember;
    }

    public void setFamilyMember(List<String> familyMember) {
        this.familyMember = familyMember;
    }
}
