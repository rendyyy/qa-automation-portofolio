package io.github.rendyyy.qa.common.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreatedBooking(
        @JsonProperty("bookingid") int bookingId,
        @JsonProperty("booking") Booking booking) {
}