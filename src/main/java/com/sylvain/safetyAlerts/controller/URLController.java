package com.sylvain.safetyAlerts.controller;

import com.sylvain.safetyAlerts.dto.*;
import com.sylvain.safetyAlerts.service.IFireStationService;
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
    @Autowired
    private IFireStationService fireStationService;


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

    @GetMapping("phoneAlert")
    public List<String> getPhoneByStation(@RequestParam String firestation){
        return fireStationService.getPhoneByStation(firestation);
    }

    @GetMapping("firestation")
    public List<CoverageDTO> getCoverageByFireStation(@RequestParam String stationNumber){
        return fireStationService.getCoverageByFireStation(stationNumber);
    }

    @GetMapping("flood/stations")
    public List<FoyerDTO> getFoyerByFireStation(@RequestParam List<String> stations){
        return fireStationService.getFoyerByFireStation(stations);
    }



}
