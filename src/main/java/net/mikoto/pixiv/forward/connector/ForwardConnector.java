package net.mikoto.pixiv.forward.connector;

import net.mikoto.pixiv.api.model.Artwork;
import net.mikoto.pixiv.api.model.ForwardServer;

/**
 * @author mikoto
 * @date 2022/6/18 23:03
 */
public interface ForwardConnector {
    /**
     * Get the artwork.
     *
     * @param artworkId The id of the series.
     * @return An artwork object.
     */
    Artwork getArtworkInformation(int artworkId);

    /**
     * Add a forward server.
     *
     * @param forwardServer The forward server address.
     */
    void addForwardServer(ForwardServer forwardServer);
}
