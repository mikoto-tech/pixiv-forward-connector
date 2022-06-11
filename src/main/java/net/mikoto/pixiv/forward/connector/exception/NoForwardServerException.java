package net.mikoto.pixiv.forward.connector.exception;

/**
 * @author mikoto
 * @date 2022/6/11 14:29
 */
public class NoForwardServerException extends NullPointerException {
    public NoForwardServerException(String message) {
        super(message);
    }
}
