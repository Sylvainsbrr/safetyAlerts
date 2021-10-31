package com.sylvain.safetyAlerts.service;

import com.sylvain.safetyAlerts.models.FireStation;

public interface IFireStationService {
    boolean createFireStation(FireStation fireStation);

    boolean updateFireStation(FireStation fireStation);

    boolean deleteFireStation(FireStation fireStation);
}
