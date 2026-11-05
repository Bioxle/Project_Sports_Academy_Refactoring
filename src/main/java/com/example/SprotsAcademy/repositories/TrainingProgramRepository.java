package com.example.SprotsAcademy.repositories;

import com.example.SprotsAcademy.TrainingProgram;
import javafx.collections.ObservableList;

public interface TrainingProgramRepository extends ExcelRepository<TrainingProgram> {

    ObservableList<TrainingProgram> findAllObservable();
    TrainingProgram findTrainingProgramBySerialNumber(int trainingProgramSerialNumber);
}
