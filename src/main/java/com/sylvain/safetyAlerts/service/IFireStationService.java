package com.sylvain.safetyAlerts.service;

import com.sylvain.safetyAlerts.dto.CoverageDTO;
import com.sylvain.safetyAlerts.dto.FoyerDTO;
import com.sylvain.safetyAlerts.models.FireStation;

import java.util.List;

public interface IFireStationService {
    boolean createFireStation(FireStation fireStation);

    boolean updateFireStation(FireStation fireStation);

    boolean deleteFireStation(FireStation fireStation);

    List<String> getPhoneByStation(String firestation);
    List<CoverageDTO> getCoverageByFireStation(String stationNumber);
    List<FoyerDTO> getFoyerByFireStation(List<String> stations);
}
