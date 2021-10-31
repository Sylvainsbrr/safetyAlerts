package com.sylvain.safetyAlerts.service;

import com.sylvain.safetyAlerts.models.MedicalRecord;

public interface IMedicalRecordService {
    void createMedicalRecord(MedicalRecord medicalRecord);

    void updateMedicalRecord(MedicalRecord medicalRecord);

    void deleteMedicalRecord(MedicalRecord medicalRecord);
}
