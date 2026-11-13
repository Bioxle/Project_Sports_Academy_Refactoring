package com.example.SprotsAcademy.unrefactored;
import com.example.SprotsAcademy.model.Athlete;
import com.example.SprotsAcademy.model.Enrollment;
import com.example.SprotsAcademy.model.Payment;
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

import static com.example.SprotsAcademy.Database.getAthleteList;
import static com.example.SprotsAcademy.Database.savePaymentToExcel;

public class PaymentApp extends Application {

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

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        // Initializing UI components
        nameInput = new TextField();
        surnameInput = new TextField();
        contactInput = new TextField();
        dobInput = new DatePicker();
        sheetInput = new TextField();
        yes = new RadioButton("Yes");
        no = new RadioButton("No");
        ToggleGroup professionalGroup = new ToggleGroup();
        yes.setToggleGroup(professionalGroup);
        no.setToggleGroup(professionalGroup);
        expLevelField = new TextField();

        TextField registrationCostField = new TextField();
        TextField discountField = new TextField();

        Button enrollButton = new Button("Enroll");
        enrollButton.setBackground(new Background(new BackgroundFill(Color.SILVER, CornerRadii.EMPTY, Insets.EMPTY)));
        enrollButton.setOnMouseEntered(e -> enrollButton.setStyle("-fx-background-color: LIMEGREEN"));
        enrollButton.setOnMouseExited(e -> enrollButton.setStyle("-fx-background-color: SILVER;"));

        TextArea resultArea = new TextArea();
        resultArea.setVisible(false);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label regCost = new Label("Registration Cost:");
        Label discountPercent = new Label("Discount Percentage:");

        GridPane.setConstraints(regCost, 0, 0);
        GridPane.setConstraints(registrationCostField, 1, 0);

        GridPane.setConstraints(discountPercent, 0, 1);
        GridPane.setConstraints(discountField, 1, 1);

        grid.getChildren().addAll(
                regCost, registrationCostField,
                discountPercent, discountField
        );
        grid.setAlignment(Pos.CENTER);

        messageLabel = new Label("");
        // Headline Label
        Label headlineLabel = new Label("Registration Fee");
        headlineLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        headlineLabel.setAlignment(Pos.CENTER);

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

        HBox buttonBoxes = new HBox(enrollButton, backButton);
        buttonBoxes.setSpacing(10);
        buttonBoxes.setAlignment(Pos.CENTER);

        // Add all elements to the grid

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

        resultArea.setMaxHeight(450);
        resultArea.setMinHeight(300);
        resultArea.setMaxWidth(500);
        resultArea.setMinWidth(300);

        Label tableLabel = new Label("Select an Athlete");
        tableLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: normal;");


        VBox tableContainer = new VBox(tableLabel, athleteTable);
        tableContainer.setAlignment(Pos.CENTER);
        tableContainer.setSpacing(5);

        VBox tableWithHeading = new VBox(headlineLabel, tableContainer);
        tableWithHeading.setAlignment(Pos.CENTER);
        tableWithHeading.setSpacing(20);


        VBox mainContainer = new VBox(tableWithHeading, grid, buttonBoxes, resultArea);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(10));





        // Enroll button action
        enrollButton.setOnAction(e -> {
            Athlete selectedAthlete = athleteTable.getSelectionModel().getSelectedItem();
            resultArea.setVisible(true);
            if (selectedAthlete == null) {
                resultArea.setText("Please select an athlete to enroll.");
                return;
            }

            try {
                double cost = Double.parseDouble(registrationCostField.getText());
                double discountPercentage = Double.parseDouble(discountField.getText());
                LocalDate date = LocalDate.now();

                Enrollment enrollment = new Enrollment(selectedAthlete.getSerialNumber(), selectedAthlete.getName(), date, cost, discountPercentage);
                Payment payment = new Payment(selectedAthlete.getSerialNumber(), date, "Online Banking", enrollment, "Subscription");
                // this payment needs to be saved in excel, where enrollment saves as serialNumber
                savePaymentToExcel(payment);
                resultArea.setText("Enrollment Details:\n");
                resultArea.appendText("Athlete: " + selectedAthlete.getName() + " " + selectedAthlete.getSurname() + "\n");
                resultArea.appendText("\nRegistration Cost: $" + cost + "\n");
                resultArea.appendText("Discount: " + discountPercentage + "%\n");
                resultArea.appendText("Total Cost after Discount: $" + enrollment.calculateTotalPrice() + "\n\n");

                resultArea.appendText("Payment Details:\n");
                resultArea.appendText("Payment Date: " + date + "\n");
                resultArea.appendText("Total Payment Amount: $" + payment.getTotalCost() + "\n");

            } catch (NumberFormatException ex) {
                resultArea.setText("Please enter valid numbers for cost and discount percentage.");
            }
        });

        // Create a scene with the VBox
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        Scene scene = new Scene(mainContainer, screenWidth, screenHeight);

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
