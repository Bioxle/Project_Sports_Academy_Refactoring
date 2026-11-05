package com.example.SprotsAcademy.repositories;

import com.example.SprotsAcademy.Athlete;

public interface AthleteRepository extends Repository<Athlete> {

    Athlete findAthleteBySerialNumber(int athleteSerialNumber);

}
