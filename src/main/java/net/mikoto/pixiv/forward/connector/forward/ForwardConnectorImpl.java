package net.mikoto.pixiv.forward.connector.forward;

import net.mikoto.pixiv.api.model.Artwork;
import net.mikoto.pixiv.api.model.ForwardServer;
import net.mikoto.pixiv.api.patcher.connector.Connector;
import net.mikoto.pixiv.api.patcher.connector.SmoothWeightedConnector;
import net.mikoto.pixiv.api.patcher.source.ArtworkSource;
import net.mikoto.pixiv.forward.connector.exception.GetArtworkInformationException;
import net.mikoto.pixiv.forward.connector.forward.source.ForwardArtworkSource;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author mikoto
 * @date 2022/4/3 2:28
 */
@Component("pixivForwardConnector")
public class ForwardConnectorImpl implements ForwardConnector {
    private static final Connector<ForwardServer> FORWARD_SERVER_CONNECTOR = new SmoothWeightedConnector<>();
    private static final ArtworkSource<ForwardServer> FORWARD_SERVER_ARTWORK_SOURCE = new ForwardArtworkSource();


    /**
     * Get the artwork.
     *
     * @param artworkId The id of the series.
     * @return A series object.
     * @throws IOException                    An exception.
     * @throws GetArtworkInformationException An exception.
     */
    @Override
    public Artwork getArtworkInformation(int artworkId) throws Exception {
        return FORWARD_SERVER_ARTWORK_SOURCE.obtain(artworkId, FORWARD_SERVER_CONNECTOR);
    }

    /**
     * Add a forward server.
     *
     * @param forwardServer The forward server address.
     */
    @Override
    public void addForwardServer(ForwardServer forwardServer) {
        FORWARD_SERVER_CONNECTOR.addServer(forwardServer);
    }
}
