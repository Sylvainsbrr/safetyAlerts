package com.sylvain.safetyAlerts.dto;

import java.util.List;

public class FoyerDTO {
    private String address;
    private List<FireDTO> firePerson;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<FireDTO> getFirePerson() {
        return firePerson;
    }

    public void setFirePerson(List<FireDTO> firePerson) {
        this.firePerson = firePerson;
    }
}
