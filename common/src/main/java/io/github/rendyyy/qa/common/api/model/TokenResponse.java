package io.github.rendyyy.qa.common.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** On success only "token" is set; on bad credentials only "reason" is set. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TokenResponse(String token, String reason) {
}