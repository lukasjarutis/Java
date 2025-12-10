package org.example;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.foodapp.*;
import org.foodapp.User;
import org.foodapp.AppUserRow;


import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

public class MainController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private TabPane mainTabPane;

    @FXML
    private TableView<Restaurant> restaurantTable;

    @FXML
    private TableColumn<Restaurant, String> colName;

    @FXML
    private TableColumn<Restaurant, String> colAddress;

    @FXML
    private TableColumn<Restaurant, Double> colPopularity;

    private final RestaurantDAO restaurantDAO = new RestaurantDAO();
    private ObservableList<Restaurant> restaurantList = FXCollections.observableArrayList();

    private User loggedInUser;

    @FXML
    private Label dbStatusLabel;

    @FXML
    private ToolBar restaurantToolbar;

    @FXML
    private TextField nameFilterField;

    @FXML
    private CheckBox openOnlyCheckBox;

    @FXML
    private TextField minPopularityField;

    @FXML
    private TableView<Order> ordersTable;
    @FXML
    private TableColumn<Order, Long> colOrderId;
    @FXML
    private TableColumn<Order, String> colOrderRestaurant;
    @FXML
    private TableColumn<Order, String> colOrderCustomer;
    @FXML
    private TableColumn<Order, String> colOrderStatus;
    @FXML
    private TableColumn<Order, Double> colOrderTotal;
    @FXML
    private TableColumn<Order, String> colOrderCreated;
    @FXML
    private ComboBox<OrderStatus> orderStatusFilter;
    @FXML
    private DatePicker orderFromDatePicker;
    @FXML
    private DatePicker orderToDatePicker;
    @FXML
    private TextField orderCustomerFilterField;
    @FXML
    private TextField orderRestaurantFilterField;

    private final ObservableList<Order> orderList = FXCollections.observableArrayList();
    private final OrderDAO orderDAO = new OrderDAO();

    @FXML
    private Tab usersTab;
    @FXML
    private ToolBar usersToolbar;
    @FXML
    private TableView<AppUserRow> usersTable;
    @FXML
    private TableColumn<AppUserRow, Long> colUserId;
    @FXML
    private TableColumn<AppUserRow, String> colUserUsername;
    @FXML
    private TableColumn<AppUserRow, String> colUserFullName;
    @FXML
    private TableColumn<AppUserRow, String> colUserEmail;
    @FXML
    private TableColumn<AppUserRow, String> colUserRole;
    @FXML
    private TextField userUsernameFilterField;
    @FXML
    private TextField userFullNameFilterField;
    @FXML
    private TextField userEmailFilterField;
    @FXML
    private TextField userPhoneFilterField;

    private final ObservableList<AppUserRow> userList = FXCollections.observableArrayList();
    private final AppUserDAO appUserDAO = new AppUserDAO();

    @FXML
    private void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPopularity.setCellValueFactory(new PropertyValueFactory<>("popularityScore"));
        restaurantTable.setItems(restaurantList);

        colOrderId.setCellValueFactory(data -> new SimpleLongProperty(
                data.getValue().getId() != null ? data.getValue().getId() : 0L
        ).asObject());

        colOrderRestaurant.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getRestaurant() != null
                                ? data.getValue().getRestaurant().getName()
                                : ""
                )
        );

        colOrderCustomer.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getCustomer() != null
                                ? data.getValue().getCustomer().getUsername()
                                : ""
                )
        );

        colOrderStatus.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getStatus() != null
                                ? data.getValue().getStatus().name()
                                : ""
                )
        );

        colOrderTotal.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getTotalPrice()).asObject()
        );

        colOrderCreated.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getCreatedAt() != null
                                ? data.getValue().getCreatedAt().toString()
                                : ""
                )
        );

        ordersTable.setItems(orderList);

        if (orderStatusFilter != null) {
            orderStatusFilter.setItems(FXCollections.observableArrayList(OrderStatus.values()));
        }

        if (colUserId != null) {
            colUserId.setCellValueFactory(data ->
                    new SimpleLongProperty(
                            data.getValue().getId() != null ? data.getValue().getId() : 0L
                    ).asObject());
        }

        if (colUserUsername != null) {
            colUserUsername.setCellValueFactory(data ->
                    new SimpleStringProperty(data.getValue().getUsername()));
        }

        if (colUserFullName != null) {
            colUserFullName.setCellValueFactory(data ->
                    new SimpleStringProperty(data.getValue().getFullName()));
        }

        if (colUserEmail != null) {
            colUserEmail.setCellValueFactory(data ->
                    new SimpleStringProperty(data.getValue().getEmail()));
        }

        if (colUserRole != null) {
            colUserRole.setCellValueFactory(data ->
                    new SimpleStringProperty(data.getValue().getRole()));
        }

        if (usersTable != null) {
            usersTable.setItems(userList);
        }
    }

    @FXML
    private void onExit() {
        try {
            DBUtil.disconnect();
        } catch (SQLException e) {
        }
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onNewRestaurant() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/restaurant-dialog.fxml")
            );
            Scene scene = new Scene(loader.load());

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Naujas restoranas");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(rootPane.getScene().getWindow());
            dialogStage.setScene(scene);

            RestaurantDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTargetList(restaurantList);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Nepavyko atidaryti lango", e.getMessage());
        }
    }

    @FXML
    private void onDeleteRestaurant() {
        Restaurant selected = restaurantTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Nepasirinktas restoranas", "Pasirinkite restoraną iš sąrašo.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Patvirtinimas");
        confirm.setHeaderText("Ar tikrai norite pašalinti restoraną?");
        confirm.setContentText(selected.getName());
        confirm.initOwner(rootPane.getScene().getWindow());

        confirm.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    restaurantDAO.delete(selected.getId());
                    restaurantList.remove(selected);
                } catch (SQLException e) {
                    showError("Nepavyko pašalinti restorano iš DB", e.getMessage());
                }
            }
        });
    }

    @FXML
    private void onEditRestaurant() {
        Restaurant selected = restaurantTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Nepasirinktas restoranas", "Pasirinkite restoraną iš sąrašo.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/restaurant-dialog.fxml")
            );
            Scene scene = new Scene(loader.load());

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Redaguoti restoraną");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(rootPane.getScene().getWindow());
            dialogStage.setScene(scene);

            RestaurantDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTargetList(restaurantList);
            controller.setEditingRestaurant(selected);

            dialogStage.showAndWait();

            restaurantTable.refresh();

        } catch (IOException e) {
            showError("Nepavyko atidaryti redagavimo lango", e.getMessage());
        }
    }

    @FXML
    private void onConnectDb() {
        try {
            DBUtil.connect();
            dbStatusLabel.setText("DB: prisijungta");
            loadRestaurantsFromDb();
        } catch (SQLException e) {
            showError("Nepavyko prisijungti prie DB", e.getMessage());
        }
    }

    @FXML
    private void onDisconnectDb() {
        try {
            DBUtil.disconnect();
            dbStatusLabel.setText("DB: atsijungta");
            restaurantList.clear();
        } catch (SQLException e) {
            showError("Nepavyko atsijungti nuo DB", e.getMessage());
        }
    }

    private void loadRestaurantsFromDb() {
        try {
            restaurantList.clear();
            restaurantList.addAll(restaurantDAO.findAll());
        } catch (SQLException e) {
            showError("Nepavyko užkrauti restoranų iš DB", e.getMessage());

        }
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;

        if (dbStatusLabel != null) {
            dbStatusLabel.setText(
                    "DB: " + (DBUtil.isConnected() ? "prisijungta" : "neprisijungta")
                            + " | Vartotojas: " + user.getUsername()
                            + " (" + user.getRole() + ")"
            );
        }

        loadRestaurantsFromDb();
        loadOrdersFromDb(null, null, null, null, null);

        if (user.getRole() == UserRole.ADMIN) {
            loadUsersFromDb();
            if (usersTab != null) {
                usersTab.setDisable(false);
            }
            if (usersToolbar != null) {
                usersToolbar.setDisable(false);
            }
        } else {
            if (usersTab != null) {
                usersTab.setDisable(true);
            }
            if (usersToolbar != null) {
                usersToolbar.setDisable(true);
            }
        }


        UserRole role = user.getRole();
        switch (role) {
            case ADMIN -> {}
            case OWNER -> {}
            case DRIVER, CUSTOMER -> {
                if (restaurantToolbar != null) {
                    restaurantToolbar.setDisable(true);
                }
            }
        }
    }

    @FXML
    private void onApplyFilter() {
        String nameFilter = nameFilterField != null ? nameFilterField.getText() : null;
        Boolean openOnly = openOnlyCheckBox != null && openOnlyCheckBox.isSelected();

        Double minPopularity = null;
        if (minPopularityField != null) {
            String text = minPopularityField.getText();
            if (text != null && !text.isBlank()) {
                try {
                    minPopularity = Double.parseDouble(text);
                } catch (NumberFormatException e) {
                    showError("Netinkama min. populiarumo reikšmė (pvz. 4.0)", e.getMessage());
                    return;
                }
            }
        }

        try {
            restaurantList.clear();
            restaurantList.addAll(
                    restaurantDAO.findFiltered(
                            nameFilter,
                            openOnly,
                            minPopularity
                    )
            );
        } catch (SQLException e) {
            showError("Nepavyko pritaikyti filtrų: ", e.getMessage());
        }
    }

    @FXML
    private void onClearFilter() {
        if (nameFilterField != null) {
            nameFilterField.clear();
        }
        if (openOnlyCheckBox != null) {
            openOnlyCheckBox.setSelected(false);
        }
        if (minPopularityField != null) {
            minPopularityField.clear();
        }

        loadRestaurantsFromDb();
    }

    private void loadOrdersFromDb() {
        loadOrdersFromDb(null, null, null, null, null);
    }

    private void loadOrdersFromDb(OrderStatus status,
                                  LocalDateTime from,
                                  LocalDateTime to,
                                  String customer,
                                  String restaurantName) {
        try {
            orderList.clear();
            orderList.addAll(orderDAO.findFiltered(status, from, to, customer, restaurantName));
        } catch (SQLException e) {
            showError("Nepavyko užkrauti užsakymų", e.getMessage());
        }
    }

    @FXML
    private void onNewOrder() {
        if (loggedInUser == null) {
            showError("Vartotojas neprisijungęs", "Prisijunkite iš naujo.");
            return;
        }

        // Для простоты: берём первый ресторан из списка
        if (restaurantList.isEmpty()) {
            showError("Nėra restoranų", "Pridėkite bent vieną restoraną.");
            return;
        }

        Restaurant restaurant = restaurantList.get(0);

        TextInputDialog dialog = new TextInputDialog("10.0");
        dialog.setTitle("Naujas užsakymas");
        dialog.setHeaderText("Įveskite užsakymo sumą");
        dialog.setContentText("Suma:");

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) {
            return;
        }

        double total;
        try {
            total = Double.parseDouble(result.get());
        } catch (NumberFormatException e) {
            showError("Netinkama suma", "Įveskite teisingą skaičių.");
            return;
        }

        Order order = new Order();
        order.setRestaurant(restaurant);

        if (loggedInUser instanceof Customer) {
            order.setCustomer((Customer) loggedInUser);
        } else {
            showError("Klaida", "Tik klientai gali kurti užsakymus!");
            return;
        }

        order.setDriver(null);
        order.setStatus(OrderStatus.CREATED);
        order.setTotalPrice(total);
        order.setCreatedAt(LocalDateTime.now());

        try {
            orderDAO.insert(order);
            orderList.add(order);
        } catch (SQLException e) {
            showError("Nepavyko išsaugoti užsakymo", e.getMessage());
        }
    }

    @FXML
    private void onChangeOrderStatus() {
        Order selected = ordersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Nepasirinktas užsakymas", "Pasirinkite užsakymą iš sąrašo.");
            return;
        }

        ChoiceDialog<OrderStatus> dialog = new ChoiceDialog<>(
                selected.getStatus(), OrderStatus.values()
        );
        dialog.setTitle("Keisti statusą");
        dialog.setHeaderText("Pasirinkite naują užsakymo statusą");
        dialog.setContentText("Statusas:");

        Optional<OrderStatus> result = dialog.showAndWait();
        if (result.isEmpty()) {
            return;
        }

        selected.setStatus(result.get());
        try {
            orderDAO.update(selected);
            ordersTable.refresh();
        } catch (SQLException e) {
            showError("Nepavyko atnaujinti užsakymo", e.getMessage());
        }
    }

    @FXML
    private void onDeleteOrder() {
        Order selected = ordersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Nepasirinktas užsakymas", "Pasirinkite užsakymą iš sąrašo.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Patvirtinimas");
        confirm.setHeaderText("Ar tikrai norite pašalinti užsakymą?");
        confirm.setContentText("ID: " + selected.getId());
        confirm.initOwner(rootPane.getScene().getWindow());

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                orderDAO.delete(selected.getId());
                orderList.remove(selected);
            } catch (SQLException e) {
                showError("Nepavyko pašalinti užsakymo", e.getMessage());
            }
        }
    }

    @FXML
    private void onApplyOrderFilter() {
        OrderStatus status = orderStatusFilter != null ? orderStatusFilter.getValue() : null;

        LocalDate fromDate = orderFromDatePicker != null ? orderFromDatePicker.getValue() : null;
        LocalDate toDate = orderToDatePicker != null ? orderToDatePicker.getValue() : null;

        LocalDateTime fromDateTime = fromDate != null ? fromDate.atStartOfDay() : null;
        LocalDateTime toDateTime = null;
        if (toDate != null) {
            toDateTime = toDate.atTime(LocalTime.MAX);
        }

        String customer = orderCustomerFilterField != null ? orderCustomerFilterField.getText() : null;
        String restaurantName = orderRestaurantFilterField != null ? orderRestaurantFilterField.getText() : null;

        loadOrdersFromDb(status, fromDateTime, toDateTime, customer, restaurantName);
    }

    @FXML
    private void onClearOrderFilter() {
        if (orderStatusFilter != null) {
            orderStatusFilter.setValue(null);
        }
        if (orderFromDatePicker != null) {
            orderFromDatePicker.setValue(null);
        }
        if (orderToDatePicker != null) {
            orderToDatePicker.setValue(null);
        }
        if (orderCustomerFilterField != null) {
            orderCustomerFilterField.clear();
        }
        if (orderRestaurantFilterField != null) {
            orderRestaurantFilterField.clear();
        }

        loadOrdersFromDb(null, null, null, null, null);
    }

    private void loadUsersFromDb() {
        try {
            userList.clear();
            userList.addAll(appUserDAO.findAll());
        } catch (SQLException e) {
            showError("Nepavyko užkrauti vartotojų", e.getMessage());
        }
    }

    private void loadUsersFromDb(String username,
                                 String fullName,
                                 String email,
                                 String phone) {
        try {
            userList.clear();
            userList.addAll(appUserDAO.findFiltered(username, fullName, email, phone));
        } catch (SQLException e) {
            showError("Nepavyko užkrauti vartotojų", e.getMessage());
        }
    }

    @FXML
    private void onNewUser() {
        AppUserRow user = new AppUserRow();
        user.setRole("CUSTOMER");

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/user-dialog.fxml")
            );
            Scene scene = new Scene(loader.load());

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Naujas vartotojas");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(rootPane.getScene().getWindow());
            dialogStage.setScene(scene);

            UserDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setUser(user);

            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
                    showError("Slaptažodis privalomas naujam vartotojui", "");
                    return;
                }
                appUserDAO.insert(user);
                userList.add(user);
            }

        } catch (IOException | SQLException e) {
            showError("Nepavyko sukurti vartotojo", e.getMessage());
        }
    }

    @FXML
    private void onEditUser() {
        AppUserRow selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Nepasirinktas vartotojas", "Pasirinkite vartotoją iš sąrašo.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/user-dialog.fxml")
            );
            Scene scene = new Scene(loader.load());

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Redaguoti vartotoją");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(rootPane.getScene().getWindow());
            dialogStage.setScene(scene);

            UserDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setUser(selected);

            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                appUserDAO.update(selected);
                usersTable.refresh();
            }

        } catch (IOException | SQLException e) {
            showError("Nepavyko atnaujinti vartotojo", e.getMessage());
        }
    }

    @FXML
    private void onDeleteUser() {
        AppUserRow selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Nepasirinktas vartotojas", "Pasirinkite vartotoją iš sąrašo.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Patvirtinimas");
        confirm.setHeaderText("Ar tikrai norite pašalinti vartotoją?");
        confirm.setContentText(selected.getUsername());
        confirm.initOwner(rootPane.getScene().getWindow());

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                appUserDAO.delete(selected.getId());
                userList.remove(selected);
            } catch (SQLException e) {
                showError("Nepavyko pašalinti vartotojo", e.getMessage());
            }
        }
    }

    @FXML
    private void onApplyUserFilter() {
        String username = userUsernameFilterField != null ? userUsernameFilterField.getText() : null;
        String fullName = userFullNameFilterField != null ? userFullNameFilterField.getText() : null;
        String email = userEmailFilterField != null ? userEmailFilterField.getText() : null;
        String phone = userPhoneFilterField != null ? userPhoneFilterField.getText() : null;

        loadUsersFromDb(username, fullName, email, phone);
    }

    @FXML
    private void onClearUserFilter() {
        if (userUsernameFilterField != null) {
            userUsernameFilterField.clear();
        }
        if (userFullNameFilterField != null) {
            userFullNameFilterField.clear();
        }
        if (userEmailFilterField != null) {
            userEmailFilterField.clear();
        }
        if (userPhoneFilterField != null) {
            userPhoneFilterField.clear();
        }

        loadUsersFromDb();
    }

    @FXML
    private void onOrderChat() {
        Order selected = ordersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Pasirinkite užsakymą", "Reikia pasirinkti užsakymą iš sąrašo.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/order-chat.fxml"));
            Scene scene = new Scene(loader.load());

            OrderChatController controller = loader.getController();
            controller.setOrder(selected);
            controller.setLoggedInUser(loggedInUser);

            Stage stage = new Stage();
            stage.setTitle("Užsakymo chat");
            stage.initOwner(rootPane.getScene().getWindow());
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            showError("Nepavyko atidaryti chato", e.getMessage());
        }
    }


    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Klaida");
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showWarning(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Perspėjimas");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initOwner(rootPane.getScene().getWindow());
        alert.showAndWait();
    }
}
