package com.example.SprotsAcademy.repositories;

import com.example.SprotsAcademy.model.TrainingProgram;
import javafx.collections.ObservableList;

public interface TrainingProgramRepository extends Repository<TrainingProgram> {

    ObservableList<TrainingProgram> findAllObservable();

    TrainingProgram findTrainingProgramBySerialNumber(int trainingProgramSerialNumber);
}
