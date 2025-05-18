package controller;

import lombok.Getter;

@Getter
public class ApiResponse {
    private int code;
    private String type;
    private String message;
}
