package com.feature.recipesharingapp.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ApplicationErrorCode implements ErrorCode {

    RECIPE_NOT_FOUND(
            1001,"RECIPE_ID_NOT_FOUND","Recipe does not Exist",HttpStatus.BAD_REQUEST),
    INVALID_ROLE(
            1002,"INVALID_ROLE","Invalid Role",HttpStatus.BAD_REQUEST),
    REVIEW_NOT_FOUND(
            1003,"REVIEW_NOT_FOUND","Review does not Exist",HttpStatus.BAD_REQUEST),
    INVALID_SORT_ORDER(
            1004,"INVALID_SORT_ORDER","Invalid Sort Order",HttpStatus.BAD_REQUEST),
    INVALID_PAGE_NUMBER(
            1005,"INVALID_PAGE_NUMBER","Page Number is not valid",HttpStatus.NOT_FOUND),
    INVALID_PAGE_SIZE(
            1006,"INVALID_PAGE_SIZE","Page size is not Valid",HttpStatus.BAD_REQUEST),
    TOKEN_NOT_FOUND(
            1007,"TOKEN_NOT_FOUND","Token does not Exist",HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(
            1008,"INVALID_PASSWORD","Password is not Valid",HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(
            1009,"INVALID_USERNAME","Username is not Valid",HttpStatus.BAD_REQUEST),
    ;

    private final int errorId;
    private final String errorCode;
    private String errorMessage;
    private final HttpStatus httpStatus;

    @Override
    public int getErrorId() {
        return errorId;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}