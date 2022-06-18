package net.mikoto.pixiv.forward.connector.test;

import net.mikoto.pixiv.api.model.Artwork;
import net.mikoto.pixiv.api.model.ForwardServer;
import net.mikoto.pixiv.forward.connector.forward.ForwardConnector;
import net.mikoto.pixiv.forward.connector.forward.ForwardConnectorImpl;
import org.junit.jupiter.api.Test;

/**
 * @author mikoto
 * @date 2022/5/28 18:39
 */
public class ForwardConnectorTest {
    @Test
    public void getArtworkInformationTest() throws Exception {
        ForwardConnector forwardConnector = new ForwardConnectorImpl();

        forwardConnector.addForwardServer(new ForwardServer("https://forward-2.mikoto-pixiv.cc", 10, "08dYksDTtX"));

        Artwork artwork = forwardConnector.getArtworkInformation(91262365);

        System.out.println(artwork);
    }
}
