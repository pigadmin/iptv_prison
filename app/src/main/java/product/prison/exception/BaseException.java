package product.prison.exception;

import java.net.URISyntaxException;

public class BaseException extends URISyntaxException {

    public BaseException(String input, String reason, int index) {
        super(input, reason, index);
    }

    public BaseException(String input, String reason) {
        super(input, reason);
    }
}
