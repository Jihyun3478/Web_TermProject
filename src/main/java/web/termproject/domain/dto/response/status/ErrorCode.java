package web.termproject.domain.dto.response.status;

import lombok.Getter;
@Getter
public enum ErrorCode {
    FORBIDDEN(402000, "Forbidden"),
    BAD_REQUEST(404000, "Bad Request"),
    SERVER_ERROR(500000, "Server error");


    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
