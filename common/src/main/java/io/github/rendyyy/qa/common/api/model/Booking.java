package io.github.rendyyy.qa.common.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record Booking(
        @JsonProperty("firstname") String firstname,
        @JsonProperty("lastname") String lastname,
        @JsonProperty("totalprice") int totalPrice,
        @JsonProperty("depositpaid") boolean depositPaid,
        @JsonProperty("bookingdates") BookingDates bookingDates,
        @JsonProperty("additionalneeds") String additionalNeeds) {

    public Booking withFirstname(String newFirstname) {
        return new Booking(newFirstname, lastname, totalPrice, depositPaid, bookingDates, additionalNeeds);
    }

    public Booking withLastname(String newLastname) {
        return new Booking(firstname, newLastname, totalPrice, depositPaid, bookingDates, additionalNeeds);
    }
}