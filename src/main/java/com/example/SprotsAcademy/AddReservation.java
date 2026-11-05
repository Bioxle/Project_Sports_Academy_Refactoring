package com.example.SprotsAcademy;

import com.example.SprotsAcademy.model.Athlete;
import com.example.SprotsAcademy.model.TrainingProgram;
import com.example.SprotsAcademy.model.TrainingProgramReservation;
import javafx.application.Application;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static javafx.application.Application.launch;

public class AddReservation extends Application {

    private Stage stage;
    private TableView<Athlete> athleteTableView;
    private TableView<TrainingProgram> trainingProgramTableView;
    private DatePicker startDatePicker;
    private Label messageLabel;

    List<TrainingProgramReservation> reservations;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        messageLabel = new Label("");
        stage.setTitle("Add Reservation");

        // Create a GridPane layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Headline Label
        Label headlineLabel = new Label("Reservation Creation");
        headlineLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        headlineLabel.setAlignment(Pos.CENTER);

        // Initialize TableView components
        athleteTableView = createAthleteTableView();
        trainingProgramTableView = createProgramTableView();

        Label athletetableLabel = new Label("Select an Athlete:");
        athletetableLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: normal;");

        VBox athleteTable = new VBox(athletetableLabel, athleteTableView);
        athleteTable.setSpacing(5);
        athleteTable.setAlignment(Pos.CENTER);
        // Set constraints for Athlete TableView
        GridPane.setConstraints(athleteTable, 0, 1);

        Label trainingTableLabel = new Label("Select a Program:");
        trainingTableLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: normal;");

        VBox trainingTable = new VBox(trainingTableLabel, trainingProgramTableView);
        trainingTable.setSpacing(5);
        trainingTable.setAlignment(Pos.CENTER);
        // Set constraints for Training Program TableView
        GridPane.setConstraints(trainingTable, 1, 1);

        // Other UI components
        Label startDateLabel = new Label("Training Program Date:");
        startDatePicker = new DatePicker();
        GridPane.setConstraints(startDateLabel, 0, 2);
        GridPane.setConstraints(startDatePicker, 1, 2);

        // End Date Picker, Cost Field, etc. (omitted for brevity)

        // Buttons
        Button reserve = new Button("Reserve");
        reserve.setBackground(new Background(new BackgroundFill(Color.SILVER, CornerRadii.EMPTY, Insets.EMPTY)));
        reserve.setOnMouseEntered(e -> reserve.setStyle("-fx-background-color: LIMEGREEN"));
        reserve.setOnMouseExited(e -> reserve.setStyle("-fx-background-color: SILVER;"));

        Button backButton = new Button("Back");
        backButton.setBackground(new Background(new BackgroundFill(Color.SILVER, CornerRadii.EMPTY, Insets.EMPTY)));
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: RED;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: SILVER;"));

        backButton.setOnAction(e -> goBack());
        reserve.setOnAction(e -> takeReserve());

        HBox buttonBoxes = new HBox(10, reserve, backButton);
        buttonBoxes.setAlignment(Pos.CENTER);
        GridPane.setConstraints(buttonBoxes, 0, 6);
        GridPane.setColumnSpan(buttonBoxes, 2);

        grid.getChildren().addAll(
                athleteTable, trainingTable,
                startDateLabel, startDatePicker,
                buttonBoxes);

        grid.setAlignment(Pos.CENTER);
        VBox vbox = new VBox(10, headlineLabel, grid, messageLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));

        // Create scene and set stage
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        Scene scene = new Scene(vbox, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.show();
        reservations = getReservations();
    }

    private void takeReserve() {
        Athlete selectedAthlete = athleteTableView.getSelectionModel().getSelectedItem();
        TrainingProgram selectedProgram = trainingProgramTableView.getSelectionModel().getSelectedItem();
        LocalDate selectedDate = startDatePicker.getValue();

        if (selectedAthlete == null || selectedProgram == null || selectedDate == null) {
            messageLabel.setText("Please select Athlete, Training Program, and enter all details.");
            messageLabel.setTextFill(Color.RED);
            return; // Exit method without creating subscription if any field is empty
        }

        try {
            // Create Subscription object

            String currentDate = selectedDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            // Convert IDs to strings
            String programIdStr = String.valueOf(selectedProgram.getSerialNumber());
            String athleteIdStr = String.valueOf(selectedAthlete.getSerialNumber());

            // Concatenate IDs and date to form unique code
            String uniqueCode = programIdStr + "_" + athleteIdStr + "_" + currentDate;

            System.out.println("here unique: " + uniqueCode);
            TrainingProgramReservation newReservation = new TrainingProgramReservation(uniqueCode, selectedAthlete,
                    selectedProgram, selectedDate);
            reservations.add(newReservation);

            saveReservationsToExcel(reservations);

            messageLabel.setText("Reservation and Payment successful.");
            messageLabel.setTextFill(Color.GREEN);
        } catch (Exception e) {
            messageLabel.setText("Invalid input. Please check the numbers.");
            messageLabel.setTextFill(Color.RED);
        }
    }

    private void goBack() {
        try {
            // Navigate back to previous screen (if applicable)
            ReservationManagement display = new ReservationManagement();
            display.start(stage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private TableView<Athlete> createAthleteTableView() {
        TableView<Athlete> tableView = new TableView<>();
        tableView.setPrefWidth(350);

        TableColumn<Athlete, Integer> serialNumberColumn = new TableColumn<>("Serial Number");
        serialNumberColumn
                .setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSerialNumber()));
        serialNumberColumn.setMinWidth(50);

        TableColumn<Athlete, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getName()));
        nameColumn.setMinWidth(150);

        TableColumn<Athlete, String> surnameColumn = new TableColumn<>("Surname");
        surnameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSurname()));
        surnameColumn.setMinWidth(150);

        tableView.getColumns().addAll(serialNumberColumn, nameColumn, surnameColumn);

        ObservableList<Athlete> athleteList = FXCollections.observableArrayList(getAthleteList());
        tableView.setItems(athleteList);

        return tableView;
    }

    private TableView<TrainingProgram> createProgramTableView() {
        TableView<TrainingProgram> tableView = new TableView<>();
        tableView.setPrefWidth(650);

        // TableColumn<TrainingProgram, Integer> serialNumberCol = new
        // TableColumn<>("Serial Number");
        // serialNumberCol.setCellValueFactory(new
        // PropertyValueFactory<>("serialNumber"));

        TableColumn<TrainingProgram, String> sportCol = new TableColumn<>("Sport");
        sportCol.setCellValueFactory(new PropertyValueFactory<>("sport"));
        sportCol.setMinWidth(100);

        // TableColumn<TrainingProgram, Integer> facilityCol = new
        // TableColumn<>("Facility");
        // facilityCol.setCellValueFactory(new PropertyValueFactory<>("facility"));

        TableColumn<TrainingProgram, String> coachCol = new TableColumn<>("Coach");
        coachCol.setCellValueFactory(new PropertyValueFactory<>("coach"));
        coachCol.setMinWidth(150);

        TableColumn<TrainingProgram, Integer> minExpCol = new TableColumn<>("Minimum Experience Level");
        minExpCol.setCellValueFactory(new PropertyValueFactory<>("minimumExp"));
        minExpCol.setMinWidth(100);

        // TableColumn<TrainingProgram, Boolean> weekOfAthleteCol = new
        // TableColumn<>("Week of the Athlete");
        // weekOfAthleteCol.setCellValueFactory(new
        // PropertyValueFactory<>("weekOfTheAthlete"));

        TableColumn<TrainingProgram, String> sheetCol = new TableColumn<>("Sheet");
        sheetCol.setCellValueFactory(new PropertyValueFactory<>("sheet"));
        sheetCol.setMinWidth(100);

        TableColumn<TrainingProgram, Integer> durationCol = new TableColumn<>("Duration (minutes)");
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        durationCol.setMinWidth(100);

        TableColumn<TrainingProgram, String> dayOfTheWeekCol = new TableColumn<>("Day of the Week");
        dayOfTheWeekCol.setCellValueFactory(new PropertyValueFactory<>("dayOfTheWeek"));
        dayOfTheWeekCol.setMinWidth(100);

        // tableView.getColumns().addAll(serialNumberCol, sportCol, facilityCol,
        // coachCol, minExpCol, weekOfAthleteCol, sheetCol, durationCol,
        // dayOfTheWeekCol);
        tableView.getColumns().addAll(sportCol, coachCol, minExpCol, sheetCol, durationCol, dayOfTheWeekCol);

        ObservableList<TrainingProgram> trainingPrograms = getTrainingPrograms();
        tableView.setItems(trainingPrograms);

        return tableView;
    }

    private ObservableList<TrainingProgram> getTrainingPrograms() {
        ObservableList<TrainingProgram> programs = FXCollections.observableArrayList();

        Path dataDir = Paths.get("data");
        if (!Files.exists(dataDir)) {
            try {
                Files.createDirectories(dataDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        File file = new File(dataDir.toFile(), "trainingProgram.xlsx");

        try (FileInputStream fis = new FileInputStream(file);
                XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue; // Skip header row

                int serialNumber = (int) row.getCell(0).getNumericCellValue();
                String sport = row.getCell(1).getStringCellValue();
                int facility = (int) row.getCell(2).getNumericCellValue();
                String coach = row.getCell(3).getStringCellValue();
                int minimumExp = (int) row.getCell(4).getNumericCellValue();
                boolean weekOfTheAthlete = row.getCell(5).getBooleanCellValue();
                String sheetValue = row.getCell(6).getStringCellValue();
                int duration = (int) row.getCell(7).getNumericCellValue();
                String dayOfTheWeek = row.getCell(8).getStringCellValue();

                programs.add(new TrainingProgram(serialNumber, sport, facility, coach, minimumExp, weekOfTheAthlete,
                        sheetValue, duration, dayOfTheWeek));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return programs;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
