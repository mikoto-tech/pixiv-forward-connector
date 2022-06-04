package net.mikoto.pixiv.forward.connector;

import net.mikoto.pixiv.api.connector.ArtworkDataSource;
import net.mikoto.pixiv.api.connector.Connector;
import net.mikoto.pixiv.api.model.ForwardServer;
import net.mikoto.pixiv.api.model.Series;
import net.mikoto.pixiv.forward.connector.exception.GetImageException;
import net.mikoto.pixiv.forward.connector.exception.GetSeriesInformationException;

import java.io.IOException;

/**
 * @author mikoto
 * @date 2022/4/3 2:27
 */
public interface ForwardConnector extends ArtworkDataSource, Connector {
    /**
     * Get the image of the artwork.
     *
     * @param url The url of this image.
     * @return The image data.
     * @throws IOException       An exception.
     * @throws GetImageException An exception.
     */
    byte[] getImage(String url) throws GetImageException, IOException;

    /**
     * Get the series.
     *
     * @param seriesId The id of the series.
     * @return A series object.
     * @throws IOException           An exception.
     * @throws GetSeriesInformationException           An exception.
     */
    Series getSeriesInformation(int seriesId) throws IOException, GetSeriesInformationException;

    /**
     * Add a forward server.
     *
     * @param forwardServer The forward server address.
     */
    void addForwardServer(ForwardServer forwardServer);

    /**
     * Get the forward server.
     *
     * @return The forward server.
     */
    ForwardServer getForwardServer();
}
