package net.mikoto.pixiv.forward.connector.test;

import net.mikoto.pixiv.api.model.Artwork;
import net.mikoto.pixiv.api.model.ForwardServer;
import net.mikoto.pixiv.forward.connector.ForwardConnector;
import net.mikoto.pixiv.forward.connector.SimpleForwardConnector;
import org.junit.jupiter.api.Test;

/**
 * @author mikoto
 * @date 2022/5/28 18:39
 */
public class ForwardConnectorTest {
    @Test
    public void getArtworkInformationTest() throws Exception {
        ForwardConnector forwardConnector = new SimpleForwardConnector();

        forwardConnector.addForwardServer(new ForwardServer("https://mikoto-pixiv-forward-1.mikoto-pixiv.cc", 1, "MikotoTestKeyForMikotoPixivForward"));
        Artwork artwork = forwardConnector.getArtworkById(91262365);

        System.out.println(artwork);
    }
}
