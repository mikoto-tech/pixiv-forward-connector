package net.mikoto.pixiv.forward.connector.forward;

import net.mikoto.pixiv.api.model.Artwork;
import net.mikoto.pixiv.api.model.ForwardServer;
import net.mikoto.pixiv.forward.connector.exception.GetArtworkInformationException;

import java.io.IOException;

/**
 * @author mikoto
 * @date 2022/6/18 23:03
 */
public interface ForwardConnector {
    /**
     * Get the artwork.
     *
     * @param artworkId The id of the series.
     * @return A series object.
     * @throws IOException                    An exception.
     * @throws GetArtworkInformationException An exception.
     */
    Artwork getArtworkInformation(int artworkId) throws Exception;

    /**
     * Add a forward server.
     *
     * @param forwardServer The forward server address.
     */
    void addForwardServer(ForwardServer forwardServer);
}
