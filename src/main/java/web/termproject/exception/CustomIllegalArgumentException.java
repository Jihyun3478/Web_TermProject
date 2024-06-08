package web.termproject.exception;

public class CustomIllegalArgumentException extends IllegalArgumentException {
    public CustomIllegalArgumentException(ErrorCode errorCode) {
    }

    public CustomIllegalArgumentException(ErrorCode errorCode, String message) {
    }
}
