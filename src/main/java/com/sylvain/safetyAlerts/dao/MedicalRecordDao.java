package com.sylvain.safetyAlerts.dao;

import com.sylvain.safetyAlerts.models.MedicalRecord;

public interface MedicalRecordDao {
    boolean createMedicalRecord(MedicalRecord medicalRecord);

    boolean updateMedicalRecord(MedicalRecord medicalRecord);

    boolean deleteMedicalRecord(MedicalRecord medicalRecord);
}
