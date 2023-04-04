package com.softex.figo.walletapp.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ErrorDTO implements MResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String friendlyMessage;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String developerMessage;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Throwable throwable;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer error_code;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public ErrorDTO(@NonNull Throwable throwable , @NonNull Integer error_code) {
        this(throwable.getLocalizedMessage(), throwable.getMessage(), throwable , error_code);
    }

    public ErrorDTO(String friendlyMessage , Integer error_code) {
        this(friendlyMessage, friendlyMessage , error_code);
    }

    public ErrorDTO(String friendlyMessage, String developerMessage , Integer error_code) {
        this(friendlyMessage, friendlyMessage, null, error_code);
    }
    @JsonCreator
    public ErrorDTO(String friendlyMessage, String developerMessage, Throwable throwable, Integer error_code) {
        this.friendlyMessage = friendlyMessage;
        this.developerMessage = developerMessage;
        this.throwable = throwable;
        this.error_code=error_code;
    }
}
