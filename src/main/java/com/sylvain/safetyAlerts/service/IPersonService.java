package com.sylvain.safetyAlerts.service;

import com.sylvain.safetyAlerts.dto.ChildAlertDTO;
import com.sylvain.safetyAlerts.dto.FireDTO;
import com.sylvain.safetyAlerts.dto.PersonInfoDTO;
import com.sylvain.safetyAlerts.models.Person;

import javax.validation.Valid;
import java.util.List;

public interface IPersonService {
    List<String> getCommunityEmail(String city);
    List<ChildAlertDTO> getChildAlert(String address);
    List<PersonInfoDTO> getPersonInfo(String firstName, String lastName);
    List<FireDTO> getPersonByAddress(String address);

    boolean createPerson(@Valid Person person);

    boolean updatePerson(@Valid Person person);

    boolean deletePerson(@Valid Person person);

}
