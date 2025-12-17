package org.foodapp.web.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class UpdateOrderStatusRequest {
    @JsonAlias({"orderStatus", "newStatus"})
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
