package today.seasoning.seasoning.common.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private final String message;
    private final String detail;

    public ErrorResponse(String message) {
        this.message = message;
        this.detail = null;
    }

    public ErrorResponse(String message, String detail) {
        this.message = message;
        this.detail = detail;
    }
}
