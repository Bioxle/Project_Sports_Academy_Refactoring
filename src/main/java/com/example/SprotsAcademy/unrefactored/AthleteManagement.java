package com.example.SprotsAcademy.unrefactored;

import com.example.SprotsAcademy.model.Athlete;
import javafx.application.Application;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

import static com.example.SprotsAcademy.Database.getAthleteList;

public class AthleteManagement extends Application {
    private Stage stage;
    private TableView<Athlete> athleteTable;

    public AthleteManagement() {
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        BorderPane borderPane = new BorderPane();

        HBox navigationBar = new HBox();
        navigationBar.setAlignment(Pos.CENTER);
        navigationBar.setPadding(new Insets(5));
        navigationBar.setSpacing(50); // Add spacing between buttons
        navigationBar.setMinWidth(300);




        Button mainMenuBtn = new Button("Back\n");
        mainMenuBtn.setBackground(new Background(new BackgroundFill(Color.SILVER, CornerRadii.EMPTY, Insets.EMPTY)));
        mainMenuBtn.setOnMouseEntered(e -> mainMenuBtn.setStyle("-fx-background-color: RED;"));
        mainMenuBtn.setOnMouseExited(e -> mainMenuBtn.setStyle("-fx-background-color: SILVER;"));;

        Button athleteReg = new Button("Athlete Registration\n");
        athleteReg.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        athleteReg.setOnMouseEntered(e -> athleteReg.setStyle("-fx-background-color: gray;"));
        athleteReg.setOnMouseExited(e -> athleteReg.setStyle("-fx-background-color: LIGHTBLUE;"));

        Button athleteRegFee = new Button("Registration Fee\n");
        athleteRegFee.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        athleteRegFee.setOnMouseEntered(e -> athleteRegFee.setStyle("-fx-background-color: gray;"));
        athleteRegFee.setOnMouseExited(e -> athleteRegFee.setStyle("-fx-background-color: LIGHTGREEN;"));

        Button athleteInfoChange = new Button("Change Athlete Details\n");
        athleteInfoChange.setBackground(new Background(new BackgroundFill(Color.GOLD, CornerRadii.EMPTY, Insets.EMPTY)));
        athleteInfoChange.setOnMouseEntered(e -> athleteInfoChange.setStyle("-fx-background-color: gray;"));
        athleteInfoChange.setOnMouseExited(e -> athleteInfoChange.setStyle("-fx-background-color: GOLD;"));




        // Set button width to be equal and align center
        mainMenuBtn.setMaxWidth(Double.MAX_VALUE);
        athleteReg.setMaxWidth(Double.MAX_VALUE);
        athleteRegFee.setMaxWidth(Double.MAX_VALUE);
        athleteInfoChange.setMaxWidth(Double.MAX_VALUE);
        mainMenuBtn.setMinWidth(100);
        athleteReg.setMinWidth(100);
        athleteRegFee.setMinWidth(100);
        athleteInfoChange.setMinWidth(100);
        // Add buttons to the navigation bar
        navigationBar.getChildren().addAll(athleteReg, athleteRegFee, athleteInfoChange, mainMenuBtn);

        Label welcome = new Label("Athlete Management");
        welcome.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        welcome.setAlignment(Pos.CENTER);

        welcome.setFont(Font.font(20));
        VBox menubar = new VBox(welcome, navigationBar);
        menubar.setAlignment(Pos.CENTER);
        menubar.setSpacing(20);

        // Create the athlete table
        athleteTable = new TableView<>();
        athleteTable.setMaxWidth(600);
        athleteTable.setMaxHeight(400);
        setupAthleteTable();
        Label tableNameLabel = new Label("List of Athletes");
        tableNameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: normal;");


        VBox tableContainer = new VBox(tableNameLabel,athleteTable);
        tableContainer.setAlignment(Pos.CENTER);
        tableContainer.setPadding(new Insets(10));

        // Add the navigation bar and table to a new VBox
        VBox mainContainer = new VBox(menubar, tableContainer);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setSpacing(20);
        borderPane.setCenter(mainContainer);

        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        // Create scene
        Scene scene = new Scene(borderPane, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.setTitle("Athlete Management");
        stage.show();

        // Set button actions
        athleteReg.setOnAction(event -> {
            try {
                AthleteRegistration display = new AthleteRegistration();
                display.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        mainMenuBtn.setOnAction(event -> {
            try {
                MainMenu display = new MainMenu();
                display.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        athleteInfoChange.setOnAction(event -> {
            try {
                ChangeAthleteInformation display = new ChangeAthleteInformation();
                display.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        athleteRegFee.setOnAction(event -> {
            try {
                PaymentApp display = new PaymentApp();
                display.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Populate the table with sample data
        populateAthleteTable();
    }

    private void setupAthleteTable() {

        TableColumn<Athlete, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        nameCol.setMinWidth(150);

        TableColumn<Athlete, String> surnameCol = new TableColumn<>("Surname");
        surnameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSurname()));
        surnameCol.setMinWidth(100);

        TableColumn<Athlete, LocalDate> dobCol = new TableColumn<>("Date of Birth");
        dobCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateOfBirth()));
        dobCol.setMinWidth(100);

        TableColumn<Athlete, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContact()));
        contactCol.setMinWidth(100);

        TableColumn<Athlete, Boolean> professionalCol = new TableColumn<>("Professional");
        professionalCol.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getProfessional()));
        professionalCol.setMinWidth(50);

        TableColumn<Athlete, Number> experienceLevelCol = new TableColumn<>("Experience Level");
        experienceLevelCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getExperienceLevel()));
        experienceLevelCol.setMinWidth(50);

        athleteTable.getColumns().addAll(nameCol, surnameCol, dobCol, contactCol, professionalCol, experienceLevelCol);
    }

    private void populateAthleteTable() {
        List<Athlete> athleteList = getAthleteList();
        ObservableList<Athlete> athletes = FXCollections.observableArrayList(athleteList);
        athleteTable.setItems(athletes);
    }

    private void setInitialBackground(Button button) {
        button.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: lightblue;"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
