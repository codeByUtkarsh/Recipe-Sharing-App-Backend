package com.feature.recipesharingapp.exception;
import lombok.Data;

@Data
public class ErrorResponse {

    private int errorId;
    private String errorCode;
    private String errorMessage;

    public ErrorResponse(ErrorCode error) {
        this.errorId = error.getErrorId();
        this.errorCode = error.getErrorCode();
        this.errorMessage = error.getErrorMessage();
    }
}
