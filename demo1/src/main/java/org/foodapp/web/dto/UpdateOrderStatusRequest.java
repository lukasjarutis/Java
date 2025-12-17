package org.foodapp.web.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class UpdateOrderStatusRequest {
    @JsonAlias({"orderStatus", "newStatus"})
    private String status;
    private Long driverId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }
}
