package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.foodapp.Order;
import org.foodapp.User;
import org.foodapp.OrderMessage;


import java.sql.SQLException;

public class OrderChatController {

    @FXML
    private ListView<String> messageList;

    @FXML
    private TextField inputField;

    private Order order;
    private User loggedInUser;

    private final OrderMessageDAO messageDAO = new OrderMessageDAO();

    public void setOrder(Order order) {
        this.order = order;
        refreshMessages();
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    @FXML
    private void onSend() {
        String text = inputField.getText();
        if (text == null || text.isBlank()) return;

        try {
            messageDAO.insert(order.getId(), loggedInUser.getId(), text);
            inputField.clear();
            refreshMessages();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshMessages() {
        try {
            var msgs = messageDAO.findByOrderId(order.getId());
            messageList.getItems().clear();
            for (OrderMessage m : msgs) {
                messageList.getItems().add(
                        "[" + m.getCreatedAt() + "] " +
                                m.getUsername() + ": " +
                                m.getMessageText()
                );
            }
            messageList.scrollTo(messageList.getItems().size() - 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
