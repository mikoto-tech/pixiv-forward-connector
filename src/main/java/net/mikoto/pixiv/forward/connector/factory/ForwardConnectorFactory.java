package net.mikoto.pixiv.forward.connector.factory;

import net.mikoto.pixiv.forward.connector.ForwardConnector;
import net.mikoto.pixiv.forward.connector.SimpleForwardConnector;

/**
 * @author mikoto
 * @date 2022/4/4 1:33
 */
public class ForwardConnectorFactory {
    private static final ForwardConnectorFactory INSTANCE = new ForwardConnectorFactory();

    public static ForwardConnectorFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Create a forward connector.
     *
     * @return The forward connector object.
     */
    public ForwardConnector create() {
        return new SimpleForwardConnector();
    }
}
