package com.softex.figo.walletapp.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString

public class DataDTO<T> implements MResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL )
    protected T data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected ErrorDTO error;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected boolean success;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected Integer total;

    public DataDTO(boolean success) {
        this.success = success;
    }

    public DataDTO(T body) {
        this(body, null);
    }
    @JsonCreator
    public DataDTO(T body, Integer total) {
        this.data = body;
        this.total = total;
        this.success = true;
    }

    public DataDTO(ErrorDTO error) {
        this.error = error;
        this.success = false;
    }

}