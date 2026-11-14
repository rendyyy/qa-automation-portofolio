package io.github.rendyyy.qa.common.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/** Dates are ISO-8601 strings (yyyy-MM-dd), exactly as the API sends them. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record BookingDates(
        @JsonProperty("checkin") String checkin,
        @JsonProperty("checkout") String checkout) {
}