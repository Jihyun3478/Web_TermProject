package web.termproject.domain.dto.response.status;

import lombok.Getter;
@Getter
public enum ErrorCode {
    OK(400000, "OK"),
    Created(201000, "Created");


    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
