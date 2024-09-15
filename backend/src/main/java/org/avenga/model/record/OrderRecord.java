package org.avenga.model.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.avenga.model.enumeration.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderRecord(
        @JsonProperty("id") Long id,
        @JsonProperty("orderDate") LocalDateTime orderDate,
        @JsonProperty("totalPrice") Double totalPrice,
        @JsonProperty("contact") String contact,
        @JsonProperty("status") OrderStatus status,
        @JsonProperty("orderProducts") List<OrderProductRecord> orderProducts
) {
}
