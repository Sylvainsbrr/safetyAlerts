package com.sylvain.safetyAlerts.controller;

import com.sylvain.safetyAlerts.dto.ChildAlertDTO;
import com.sylvain.safetyAlerts.dto.FireDTO;
import com.sylvain.safetyAlerts.dto.PersonInfoDTO;
import com.sylvain.safetyAlerts.service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class URLController {

    @Autowired
    private IPersonService personService;

    @GetMapping("communityEmail")
    public List<String> getCommunityEmail(@RequestParam String city){
        return personService.getCommunityEmail(city);
    }

    @GetMapping("childAlert")
    public List<ChildAlertDTO> getChildAlert(@RequestParam String address){
        return personService.getChildAlert(address);
    }

    @GetMapping("personInfo")
    public List<PersonInfoDTO> getPersonInfo(@RequestParam String firstName, String lastName){
        return personService.getPersonInfo(firstName, lastName);
    }

    @GetMapping("fire")
    public List<FireDTO> getPersonByAddress(@RequestParam String address){
        return personService.getPersonByAddress(address);
    }


}
