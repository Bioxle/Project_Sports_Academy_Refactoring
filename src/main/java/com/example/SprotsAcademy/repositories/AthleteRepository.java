package com.example.SprotsAcademy.repositories;

import com.example.SprotsAcademy.Athlete;

public interface AthleteRepository extends ExcelRepository<Athlete> {

    Athlete findAthleteBySerialNumber(int athleteSerialNumber);

}
