package org.avenga.model.record;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NewOrderProductRecord(
        @JsonProperty("productId") Long productId,
        @JsonProperty("quantity") Integer quantity
) {
}
