package com.br.todo.exception.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class ErrorHandlerResponse {
    @JsonProperty(value = "status")
    int status;

    @JsonProperty(value = "error")
    String error;

    @JsonProperty(value = "message")
    String message;

    @JsonProperty(value = "timestamp")
    long timestamp;

    @JsonProperty("fieldErrors")
    Map<String, List<String>> fieldErrors;
}
