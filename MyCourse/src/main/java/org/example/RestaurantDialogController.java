package org.example;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.foodapp.Restaurant;

import java.sql.SQLException;

public class RestaurantDialogController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField popularityField;

    @FXML
    private CheckBox openCheckBox;

    private Stage dialogStage;
    private ObservableList<Restaurant> targetList;

    private final RestaurantDAO restaurantDAO = new RestaurantDAO();
    private Restaurant editingRestaurant;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setTargetList(ObservableList<Restaurant> targetList) {
        this.targetList = targetList;
    }

    public void setEditingRestaurant(Restaurant restaurant) {
        this.editingRestaurant = restaurant;

        if (restaurant != null) {
            // Заполняем поля текущими значениями
            nameField.setText(restaurant.getName());
            addressField.setText(restaurant.getAddress());
            popularityField.setText(String.valueOf(restaurant.getPopularityScore()));
            openCheckBox.setSelected(restaurant.isOpen());
        }
    }

    @FXML
    private void onSave() {
        String name = nameField.getText();
        String address = addressField.getText();
        String popularityText = popularityField.getText();

        if (name == null || name.isBlank()) {
            showError("Pavadinimas privalomas");
            return;
        }

        if (address == null || address.isBlank()) {
            showError("Adresas privalomas");
            return;
        }

        double popularity;
        try {
            popularity = Double.parseDouble(popularityText);
        } catch (NumberFormatException e) {
            showError("Populiarumas turi būti skaičius (pvz. 4.5)");
            return;
        }

        if (popularity < 0 || popularity > 5) {
            showError("Populiarumas turi būti tarp 0 ir 5");
            return;
        }

        boolean open = openCheckBox.isSelected();

        try {
            if (editingRestaurant == null) {
                Restaurant restaurant = new Restaurant();
                restaurant.setName(name);
                restaurant.setAddress(address);
                restaurant.setPopularityScore(popularity);
                restaurant.setOpen(open);

                restaurantDAO.insert(restaurant);

                if (targetList != null) {
                    targetList.add(restaurant);
                }
            } else {
                editingRestaurant.setName(name);
                editingRestaurant.setAddress(address);
                editingRestaurant.setPopularityScore(popularity);
                editingRestaurant.setOpen(open);

                restaurantDAO.update(editingRestaurant);
            }

            dialogStage.close();

        } catch (SQLException e) {
            showError("Nepavyko išsaugoti restorano DB");
        }
    }

    @FXML
    private void onCancel() {
        dialogStage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Klaida");
        alert.setHeaderText("Netinkami duomenys");
        alert.setContentText(message);
        alert.initOwner(dialogStage);
        alert.showAndWait();
    }
}

