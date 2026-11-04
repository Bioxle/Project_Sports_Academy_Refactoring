package com.example.SprotsAcademy;

import com.example.SprotsAcademy.model.Athlete;
import com.example.SprotsAcademy.model.Subscription;
import com.example.SprotsAcademy.model.TrainingProgram;
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
import java.util.ArrayList;
import java.util.List;

import static com.example.SprotsAcademy.Database.getAthleteList;
import static com.example.SprotsAcademy.Database.saveSubscriptionsToExcel;
import static javafx.application.Application.launch;

public class AddSubscription extends Application {

    private Stage stage;
    private TableView<Athlete> athleteTableView;
    private TableView<TrainingProgram> trainingProgramTableView;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private TextField costField;
    private TextField discountField;
    private Label messageLabel;


    List<Subscription> subscriptions;

    @Override
    public void start(Stage primaryStage) {


        this.stage = primaryStage;
        messageLabel = new Label("");

        stage.setTitle("Subscription Management");

        // Create a GridPane layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Headline Label
        Label headlineLabel = new Label("Subscription Creation");
        headlineLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        headlineLabel.setAlignment(Pos.CENTER);

        // Athlete TableView
        athleteTableView = createAthleteTableView();

        Label tableLabel = new Label("Select an Athlete:");
        tableLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: normal;");

        VBox athleteTable = new VBox(tableLabel,athleteTableView);
        athleteTable.setSpacing(5);
        athleteTable.setAlignment(Pos.CENTER);

        GridPane.setConstraints(athleteTable, 0, 1);

        // Training Program TableView
        trainingProgramTableView = createProgramTableView();

        Label trainingTableLabel = new Label("Select a Program:");
        trainingTableLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: normal;");

        VBox trainingTable = new VBox(trainingTableLabel,trainingProgramTableView);
        trainingTable.setSpacing(5);
        trainingTable.setAlignment(Pos.CENTER);

        GridPane.setConstraints(trainingTable, 1, 1);

        // Start Date Picker
        Label startDateLabel = new Label("Start Date:");
        startDatePicker = new DatePicker();
        GridPane.setConstraints(startDateLabel, 0, 2);
        GridPane.setConstraints(startDatePicker, 1, 2);

        // End Date Picker
        Label endDateLabel = new Label("End Date:");
        endDatePicker = new DatePicker();
        GridPane.setConstraints(endDateLabel, 0, 3);
        GridPane.setConstraints(endDatePicker, 1, 3);

        // Cost Field
        Label costLabel = new Label("Monthly Cost:");
        costField = new TextField();
        GridPane.setConstraints(costLabel, 0, 4);
        GridPane.setConstraints(costField, 1, 4);

        // Discount Field
        Label discountLabel = new Label("Discount Percentage:");
        discountField = new TextField();
        GridPane.setConstraints(discountLabel, 0, 5);
        GridPane.setConstraints(discountField, 1, 5);

        // Payment Button
        Button paymentButton = new Button("Make Payment");
        paymentButton.setBackground(new Background(new BackgroundFill(Color.SILVER, CornerRadii.EMPTY, Insets.EMPTY)));
        paymentButton.setOnMouseEntered(e -> paymentButton.setStyle("-fx-background-color: LIMEGREEN"));
        paymentButton.setOnMouseExited(e -> paymentButton.setStyle("-fx-background-color: SILVER;"));
        paymentButton.setOnAction(e -> makePayment());

        // Back Button
        Button backButton = new Button("Back");
        backButton.setBackground(new Background(new BackgroundFill(Color.SILVER, CornerRadii.EMPTY, Insets.EMPTY)));
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: RED;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: SILVER;"));
        backButton.setOnAction(e -> goBack());

        // Add all elements to the grid
        HBox buttonBoxes = new HBox(10, paymentButton, backButton);
        buttonBoxes.setAlignment(Pos.CENTER);
        GridPane.setConstraints(buttonBoxes, 0, 6);
        GridPane.setColumnSpan(buttonBoxes, 2);

        grid.getChildren().addAll(
                athleteTable, trainingTable,
                startDateLabel, startDatePicker,
                endDateLabel, endDatePicker,
                costLabel, costField,
                discountLabel, discountField,
                buttonBoxes
        );

        grid.setAlignment(Pos.CENTER);
        VBox vbox = new VBox(10, headlineLabel, grid, messageLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));

        // Create a scene with the VBox
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        Scene scene = new Scene(vbox, screenWidth, screenHeight);

        stage.setScene(scene);
        stage.show();
        getSubscriptions();
    }

    private void  getSubscriptions(){
        subscriptions = new ArrayList<>();

        Path dataDir = Paths.get("data");
        if (!Files.exists(dataDir)) {
            try {
                Files.createDirectories(dataDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        File file = new File(dataDir.toFile(), "subscriptions.xlsx");

        if (!file.exists()) {
            return; // No subscriptions file found
        }

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }

                int serialNumber = (int) row.getCell(0).getNumericCellValue();
                int athleteSerialNumber = (int) row.getCell(1).getNumericCellValue();
                int trainingProgramSerialNumber = (int) row.getCell(2).getNumericCellValue();
                LocalDate startDate = LocalDate.parse(row.getCell(3).getStringCellValue());
                LocalDate endDate = LocalDate.parse(row.getCell(4).getStringCellValue());
                double monthlyCost = row.getCell(5).getNumericCellValue();
                double discountPercentage = row.getCell(6).getNumericCellValue();

                Athlete athlete = findAthleteBySerialNumber(athleteSerialNumber);
                TrainingProgram trainingProgram = findTrainingProgramBySerialNumber(trainingProgramSerialNumber);

                if (athlete != null && trainingProgram != null) {
                    Subscription subscription = new Subscription(serialNumber, athlete, trainingProgram, startDate, endDate, monthlyCost, discountPercentage);
                    subscriptions.add(subscription);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    Athlete findAthleteBySerialNumber(int athleteSerialNumber){
        ObservableList<Athlete> athletes = athleteTableView.getItems();
        for (Athlete athlete : athletes) {
            if (athlete.getSerialNumber() == athleteSerialNumber) {
                return athlete;
            }
        }
        return null;
    }

    TrainingProgram findTrainingProgramBySerialNumber(int trainingProgramSerialNumber){
        ObservableList<TrainingProgram> programs = trainingProgramTableView.getItems();
        for (TrainingProgram program : programs) {
            if (program.getSerialNumber() == trainingProgramSerialNumber) {
                return program;
            }
        }
        return null;
    }

    private void makePayment() {
        Athlete selectedAthlete = athleteTableView.getSelectionModel().getSelectedItem();
        TrainingProgram selectedProgram = trainingProgramTableView.getSelectionModel().getSelectedItem();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String cost = costField.getText();
        String discount = discountField.getText();

        if (selectedAthlete == null || selectedProgram == null || startDate == null || endDate == null || cost.isEmpty() || discount.isEmpty()) {
            messageLabel.setText("Please select Athlete, Training Program, and enter all details.");
            messageLabel.setTextFill(Color.RED);
            return; // Exit method without creating subscription if any field is empty
        }

        try {
            System.out.println("here");
            double costValue = Double.parseDouble(cost);
            double discountValue = Double.parseDouble(discount);

            System.out.println("here2");
            // Create Subscription object
            int serialNumberForSubscription=getUniqeSerialNumberForSubsription();
            Subscription subscription = new Subscription(serialNumberForSubscription,selectedAthlete, selectedProgram, startDate, endDate, costValue, discountValue);

            subscriptions.add(subscription);

            Path dataDir = Paths.get("data");
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }
            File file = new File(dataDir.toFile(), "subscriptions.xlsx");

            saveSubscriptionsToExcel(subscriptions);

            // Create Payment object (implementation not shown here)
            // Payment payment = new Payment(subscription.getSerialNumber(), LocalDate.now(), "Payment Method", subscription);


            messageLabel.setText("Subscription and Payment successful.");
            messageLabel.setTextFill(Color.GREEN);
        } catch (Exception e) {
            messageLabel.setText("Invalid input. Please check the numbers.");
            messageLabel.setTextFill(Color.RED);
        }
    }


    private int getUniqeSerialNumberForSubsription(){
        int maxSerial=1;
        for(Subscription subscription:subscriptions){
            if(subscription.getSerialNumber()>=maxSerial){
                maxSerial = subscription.getSerialNumber()+1;
            }
        }
        return maxSerial;
    }

    private void goBack() {
        try {
            // Navigate back to previous screen (if applicable)
            SubscriptionManagement display = new SubscriptionManagement();
            display.start(stage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private TableView<Athlete> createAthleteTableView() {
        TableView<Athlete> tableView = new TableView<>();
        tableView.setPrefWidth(350);

        TableColumn<Athlete, Integer> serialNumberColumn = new TableColumn<>("Serial Number");
        serialNumberColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSerialNumber()));
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

//        TableColumn<TrainingProgram, Integer> serialNumberCol = new TableColumn<>("Serial Number");
//        serialNumberCol.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));

        TableColumn<TrainingProgram, String> sportCol = new TableColumn<>("Sport");
        sportCol.setCellValueFactory(new PropertyValueFactory<>("sport"));
        sportCol.setMinWidth(100);

//        TableColumn<TrainingProgram, Integer> facilityCol = new TableColumn<>("Facility");
//        facilityCol.setCellValueFactory(new PropertyValueFactory<>("facility"));

        TableColumn<TrainingProgram, String> coachCol = new TableColumn<>("Coach");
        coachCol.setCellValueFactory(new PropertyValueFactory<>("coach"));
        coachCol.setMinWidth(150);

        TableColumn<TrainingProgram, Integer> minExpCol = new TableColumn<>("Minimum Experience Level");
        minExpCol.setCellValueFactory(new PropertyValueFactory<>("minimumExp"));
        minExpCol.setMinWidth(100);

//        TableColumn<TrainingProgram, Boolean> weekOfAthleteCol = new TableColumn<>("Week of the Athlete");
//        weekOfAthleteCol.setCellValueFactory(new PropertyValueFactory<>("weekOfTheAthlete"));

        TableColumn<TrainingProgram, String> sheetCol = new TableColumn<>("Sheet");
        sheetCol.setCellValueFactory(new PropertyValueFactory<>("sheet"));
        sheetCol.setMinWidth(100);

        TableColumn<TrainingProgram, Integer> durationCol = new TableColumn<>("Duration (minutes)");
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        durationCol.setMinWidth(100);

        TableColumn<TrainingProgram, String> dayOfTheWeekCol = new TableColumn<>("Day of the Week");
        dayOfTheWeekCol.setCellValueFactory(new PropertyValueFactory<>("dayOfTheWeek"));
        dayOfTheWeekCol.setMinWidth(100);

//        tableView.getColumns().addAll(serialNumberCol, sportCol, facilityCol, coachCol, minExpCol, weekOfAthleteCol, sheetCol, durationCol, dayOfTheWeekCol);
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
                if (row.getRowNum() == 0) continue; // Skip header row

                int serialNumber = (int) row.getCell(0).getNumericCellValue();
                String sport = row.getCell(1).getStringCellValue();
                int facility = (int) row.getCell(2).getNumericCellValue();
                String coach = row.getCell(3).getStringCellValue();
                int minimumExp = (int) row.getCell(4).getNumericCellValue();
                boolean weekOfTheAthlete = row.getCell(5).getBooleanCellValue();
                String sheetValue = row.getCell(6).getStringCellValue();
                int duration = (int) row.getCell(7).getNumericCellValue();
                String dayOfTheWeek = row.getCell(8).getStringCellValue();

                programs.add(new TrainingProgram(serialNumber, sport, facility, coach, minimumExp, weekOfTheAthlete, sheetValue, duration, dayOfTheWeek));
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
