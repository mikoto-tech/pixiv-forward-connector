package net.mikoto.pixiv.forward.connector.exception;

/**
 * @author mikoto
 * @date 2022/6/4 19:10
 */
public class NoSuchArtworkException extends GetArtworkInformationException {
    public NoSuchArtworkException(String message) {
        super(message);
    }
}
