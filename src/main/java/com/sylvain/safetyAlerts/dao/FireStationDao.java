package com.sylvain.safetyAlerts.dao;

import com.sylvain.safetyAlerts.models.FireStation;

public interface FireStationDao {
    boolean createFireStation(FireStation fireStation);

    boolean updateFireStation(FireStation fireStation);

    boolean deleteFireStation(FireStation fireStation);
}
