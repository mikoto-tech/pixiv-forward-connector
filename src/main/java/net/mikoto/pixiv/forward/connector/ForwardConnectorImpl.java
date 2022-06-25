package net.mikoto.pixiv.forward.connector;

import com.alibaba.fastjson2.JSONObject;
import net.mikoto.pixiv.api.model.Artwork;
import net.mikoto.pixiv.api.model.ForwardServer;
import net.mikoto.pixiv.api.source.SmoothWeightedSource;
import net.mikoto.pixiv.api.source.Source;
import net.mikoto.pixiv.forward.connector.client.PixivForwardClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author mikoto
 * @date 2022/4/3 2:28
 */
@Component("pixivForwardConnector")
public class ForwardConnectorImpl implements ForwardConnector {
    private static final Source<ForwardServer> FORWARD_SERVER_SOURCE = new SmoothWeightedSource<>();
    @Qualifier
    private final PixivForwardClient pixivForwardClient;

    @Autowired
    public ForwardConnectorImpl(PixivForwardClient pixivForwardClient) {
        this.pixivForwardClient = pixivForwardClient;
    }

    /**
     * Get the artwork.
     *
     * @param artworkId The id of the series.
     * @return A series object.
     */
    @Override
    public Artwork getArtworkInformation(int artworkId) {
        ForwardServer forwardServer = FORWARD_SERVER_SOURCE.getServer();
        JSONObject artworkJsonObject = JSONObject.parseObject(pixivForwardClient.getArtwork(forwardServer.getAddress(), forwardServer.getKey(), artworkId));
        if (artworkJsonObject.getBooleanValue("success")) {
            return artworkJsonObject.getJSONObject("body").to(Artwork.class);
        } else {
            return null;
        }
    }

    /**
     * Add a forward server.
     *
     * @param forwardServer The forward server address.
     */
    @Override
    public void addForwardServer(ForwardServer forwardServer) {
        FORWARD_SERVER_SOURCE.addServer(forwardServer);
    }
}
