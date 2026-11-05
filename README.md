### Target Package Structure (after refactoring)

```
com.example.SprotsAcademy
├── model/                  ← pure domain objects (no I/O)
│   ├── User.java
│   ├── Athlete.java
│   ├── Coach.java
│   ├── Sport.java
│   ├── Facility.java
│   ├── TrainingProgram.java
│   ├── Enrollment.java
│   ├── Subscription.java
│   ├── TrainingProgramReservation.java
│   └── Payment.java
│
├── repository/             ← I/O layer (Excel via POI)
│   ├── AthleteRepository.java      (interface)
│   ├── SubscriptionRepository.java (interface)
│   ├── ReservationRepository.java  (interface)
│   ├── PaymentRepository.java      (interface)
│   ├── TrainingProgramRepository.java (interface)
│   └── excel/
│       ├── AthleteExcelRepository.java
│       ├── SubscriptionExcelRepository.java
│       ├── ReservationExcelRepository.java
│       ├── PaymentExcelRepository.java
│       └── TrainingProgramExcelRepository.java
│
├── service/                ← business logic (SRP, no I/O)
│   ├── AthleteService.java
│   ├── SubscriptionService.java
│   └── ReservationService.java
│
├── ui/
│   ├── App.java            ← JavaFX entry point (replaces MainMenu extends Application)
│   └── controller/         ← FXML Controllers
│       ├── MainMenuController.java
│       ├── AthleteManagementController.java
│       ├── AthleteRegistrationController.java
│       ├── ChangeAthleteController.java
│       ├── PaymentController.java
│       ├── SubscriptionManagementController.java
│       ├── AddSubscriptionController.java
│       ├── CancelSubscriptionController.java
│       ├── ReservationManagementController.java
│       ├── AddReservationController.java
│       └── CancelReservationController.java
│
└── util/
    └── PriceList.java      ← moved interface

src/main/resources/
└── com/example/SprotsAcademy/ui/
    ├── main_menu.fxml
    ├── athlete_management.fxml
    ├── athlete_registration.fxml
    ├── change_athlete.fxml
    ├── payment.fxml
    ├── subscription_management.fxml
    ├── add_subscription.fxml
    ├── cancel_subscription.fxml
    ├── reservation_management.fxml
    ├── add_reservation.fxml
    └── cancel_reservation.fxml

src/test/java/com/example/SprotsAcademy/
├── model/
│   ├── AthleteTest.java
│   ├── SubscriptionTest.java
│   ├── EnrollmentTest.java
│   └── PaymentTest.java
└── service/
    ├── AthleteServiceTest.java
    ├── SubscriptionServiceTest.java
    └── ReservationServiceTest.java
```

---

# Part 1 : model package
- [] User class made abstract (instantiating user makes no sense).
- [-] getCurrentSerial() method removed from user (SRP).
- [] TrainingProgram fields are primitives instead of dedicated objects (keep em?).