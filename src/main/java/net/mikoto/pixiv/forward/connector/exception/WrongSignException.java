package net.mikoto.pixiv.forward.connector.exception;

/**
 * @author mikoto
 * @date 2022/4/3 13:03
 */
public class WrongSignException extends Exception {
    public WrongSignException(String data, String publicKey, String sign) {
        super("data:" + data + "\npublicKey:" + publicKey + "\nsign:" + sign);
    }
}
