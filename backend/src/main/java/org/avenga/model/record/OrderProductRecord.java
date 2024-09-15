package org.avenga.model.record;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OrderProductRecord(
        @JsonProperty("id") Long id,
        @JsonProperty("productId") Long productId,
        @JsonProperty("quantity") Integer quantity
) {
}
