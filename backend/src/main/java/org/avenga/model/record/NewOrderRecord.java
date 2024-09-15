package org.avenga.model.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.avenga.model.enumeration.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record NewOrderRecord(

        @JsonProperty("contact") String contact,
        @JsonProperty("orderDate") LocalDateTime orderDate,

        @JsonProperty("products") List<NewOrderProductRecord> products
) {
}
