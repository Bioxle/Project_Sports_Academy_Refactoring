package com.example.SprotsAcademy;

import com.example.SprotsAcademy.model.Athlete;
import javafx.application.Application;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

import static com.example.SprotsAcademy.Database.*;

public class ChangeAthleteInformation extends Application {
    private Stage stage;
    private TableView<Athlete> athleteTable;
    private TextField nameInput;
    private TextField surnameInput;
    private TextField contactInput;
    private DatePicker dobInput;
    private TextField sheetInput;
    private RadioButton yes;
    private RadioButton no;
    private TextField expLevelField;
    private Label messageLabel;

    public ChangeAthleteInformation() {}

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        messageLabel = new Label("");

        stage.setTitle("Update Information");

        // Create a GridPane layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Headline Label
        Label headlineLabel = new Label("Update Athlete Information");
        headlineLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        headlineLabel.setAlignment(Pos.CENTER);

        // Name Label - constraints use (column, row)
        Label nameLabel = new Label("Name:");
        GridPane.setConstraints(nameLabel, 0, 0);

        // Name Input
        nameInput = new TextField();
        nameInput.setPromptText("Enter your name");
        GridPane.setConstraints(nameInput, 1, 0);

        // Surname Label
        Label surnameLabel = new Label("Surname:");
        GridPane.setConstraints(surnameLabel, 0, 1);

        // Surname Input
        surnameInput = new TextField();
        surnameInput.setPromptText("Enter your surname");
        GridPane.setConstraints(surnameInput, 1, 1);

        // Contact Information Label
        Label contactLabel = new Label("Contact Info:");
        GridPane.setConstraints(contactLabel, 0, 2);

        // Contact Information Input
        contactInput = new TextField();
        contactInput.setPromptText("Enter your contact info");
        GridPane.setConstraints(contactInput, 1, 2);

        // Date of Birth Label
        Label dobLabel = new Label("Date of Birth:");
        GridPane.setConstraints(dobLabel, 0, 3);

        // Date of Birth Input
        dobInput = new DatePicker();
        GridPane.setConstraints(dobInput, 1, 3);

        // Email Label (assuming 'sheet' means email from your code)
        Label sheetLabel = new Label("Sheet:");
        GridPane.setConstraints(sheetLabel, 0, 4);

        // Email Input
        sheetInput = new TextField();
        sheetInput.setPromptText("Male/Female/Mixed");
        GridPane.setConstraints(sheetInput, 1, 4);

        // Professional Label
        Label professionalLabel = new Label("Professional:");
        GridPane.setConstraints(professionalLabel, 0, 5);

        // Professional Input
        ToggleGroup professionalGroup = new ToggleGroup();
        yes = new RadioButton("Yes");
        yes.setToggleGroup(professionalGroup);
        no = new RadioButton("No");
        no.setToggleGroup(professionalGroup);
        HBox professionBox = new HBox(10, yes, no);
        GridPane.setConstraints(professionBox, 1, 5);

        // Experience Level Label
        Label expLevel = new Label("Experience Level:");
        GridPane.setConstraints(expLevel, 0, 6);
        expLevelField = new TextField();
        expLevelField.setPromptText("1-5");
        GridPane.setConstraints(expLevelField, 1, 6);

        // Register Button
        Button registerButton = new Button("Update");
        registerButton.setBackground(new Background(new BackgroundFill(Color.SILVER, CornerRadii.EMPTY, Insets.EMPTY)));
        registerButton.setOnMouseEntered(e -> registerButton.setStyle("-fx-background-color: LIMEGREEN"));
        registerButton.setOnMouseExited(e -> registerButton.setStyle("-fx-background-color: SILVER;"));

        registerButton.setOnAction(e -> {
            // Retrieve all input values
            String name = nameInput.getText().trim();
            String surname = surnameInput.getText().trim();
            String contact = contactInput.getText().trim();
            LocalDate dob = dobInput.getValue();
            String sheet = sheetInput.getText().trim(); // Assuming 'emailInput' is meant for 'Sheet'
            boolean isProfessional = yes.isSelected();
            String experienceLevel = expLevelField.getText().trim();

            if (name.isEmpty() || surname.isEmpty() || contact.isEmpty() || dob == null ||
                    sheet.isEmpty() || experienceLevel.isEmpty()) {
                messageLabel.setText("Please fill out all fields.");
                messageLabel.setTextFill(Color.RED);
                return; // Exit method without updating athlete if any field is empty
            } else {
                messageLabel.setText("");
            }

            Athlete selectedAthlete = athleteTable.getSelectionModel().getSelectedItem();
            if (selectedAthlete != null) {
                // Update selected athlete with new data
                selectedAthlete.setName(name);
                selectedAthlete.setSurname(surname);
                selectedAthlete.setContact(contact);
                selectedAthlete.setDateOfBirth(dob);
                selectedAthlete.setSheet(sheet);
                selectedAthlete.setProfessional(isProfessional);
                selectedAthlete.setExperienceLevel(Integer.parseInt(experienceLevel));

                // Example: Print all updated data (you can modify this part to suit your needs)
                System.out.println("Serial Number: " + selectedAthlete.getSerialNumber());
                System.out.println("Updated Name: " + name);
                System.out.println("Updated Surname: " + surname);
                System.out.println("Updated Contact: " + contact);
                System.out.println("Updated Date of Birth: " + dob);
                System.out.println("Updated Sheet: " + sheet);
                System.out.println("Updated Professional: " + (isProfessional ? "Yes" : "No"));
                System.out.println("Updated Experience Level: " + experienceLevel);

                // You can also call your 'updateAthlete' method here with the updated data
                System.out.println("Data processing to update");

                System.out.println("data processing to save");
                boolean success = updateAthlete(selectedAthlete);
                System.out.println("register function done");


                // Clear form fields after update
                clearFields();
                populateAthleteTable();
            } else {
                messageLabel.setText("No athlete selected for update.");
                messageLabel.setTextFill(Color.RED);
            }
        });

        // Back Button (assuming you have navigation logic)
        Button backButton = new Button("Back");
        backButton.setBackground(new Background(new BackgroundFill(Color.SILVER, CornerRadii.EMPTY, Insets.EMPTY)));
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: RED;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: SILVER;"));
        backButton.setOnAction(e -> {
            try {
                AthleteManagement display = new AthleteManagement();
                display.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox buttonBoxes = new HBox(registerButton, backButton);
        buttonBoxes.setSpacing(10);
        buttonBoxes.setAlignment(Pos.CENTER);

        GridPane.setConstraints(buttonBoxes, 0, 9);
        GridPane.setColumnSpan(buttonBoxes, 2);

        // Add all elements to the grid
        grid.getChildren().addAll(
                nameLabel, nameInput,
                surnameLabel, surnameInput,
                contactLabel, contactInput,
                dobLabel, dobInput,
                sheetLabel, sheetInput,
                professionalLabel, professionBox,
                expLevel, expLevelField,
                buttonBoxes
        );

        athleteTable = new TableView<>();
        athleteTable.setMaxWidth(600);
        athleteTable.setMaxHeight(300);
        setupAthleteTable();
        populateAthleteTable();

        // Add listener to populate form fields on selection change
        athleteTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            } else {
                clearFields();
            }
        });



        Label tableLabel = new Label("Select an Athlete");
        tableLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: normal;");

        VBox tableContainer = new VBox(tableLabel, athleteTable);
        tableContainer.setAlignment(Pos.CENTER);
        tableContainer.setSpacing(5);

        VBox tableWithHeading = new VBox(headlineLabel, tableContainer);
        tableWithHeading.setAlignment(Pos.CENTER);
        tableWithHeading.setSpacing(20);






        grid.setAlignment(Pos.CENTER);
        VBox vbox = new VBox(10, tableWithHeading, grid, messageLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));

        // Create a scene with the VBox
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        Scene scene = new Scene(vbox, screenWidth, screenHeight);

        stage.setScene(scene);
        stage.show();
    }

    private void populateForm(Athlete athlete) {
        nameInput.setText(athlete.getName());
        surnameInput.setText(athlete.getSurname());
        contactInput.setText(athlete.getContact());
        dobInput.setValue(athlete.getDateOfBirth());
        sheetInput.setText(athlete.getSheet());
        if (athlete.getProfessional()) {
            yes.setSelected(true);
        } else {
            no.setSelected(true);
        }
        expLevelField.setText(String.valueOf(athlete.getExperienceLevel()));
    }

    private void clearFields() {
        nameInput.clear();
        surnameInput.clear();
        contactInput.clear();
        dobInput.getEditor().clear();
        sheetInput.clear();
        yes.setSelected(false);
        no.setSelected(false);
        expLevelField.clear();
    }

    public static void main(String[] args) {
        launch(args);
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
}
