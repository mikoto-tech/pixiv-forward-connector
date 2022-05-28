package net.mikoto.pixiv.forward.connector;

import net.mikoto.pixiv.api.connector.ArtworkDataSource;
import net.mikoto.pixiv.api.connector.Connector;
import net.mikoto.pixiv.api.model.ForwardServer;
import net.mikoto.pixiv.api.model.Series;
import net.mikoto.pixiv.forward.connector.exception.GetImageException;
import net.mikoto.pixiv.forward.connector.exception.GetSeriesInformationException;
import net.mikoto.pixiv.forward.connector.exception.WrongSignException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

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
     * @throws NoSuchMethodException An exception.
     * @throws IOException           An exception.
     * @throws GetImageException     An exception.
     */
    byte[] getImage(String url) throws NoSuchMethodException, IOException, GetImageException;

    /**
     * Get the series.
     *
     * @param seriesId The id of the series.
     * @return A series object.
     */
    Series getSeriesInformation(int seriesId) throws NoSuchMethodException, IOException, InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, WrongSignException, GetSeriesInformationException;

    /**
     * Add a forward server.
     *
     * @param forwardServer The forward server address.
     * @throws NoSuchMethodException An exception.
     * @throws IOException           An exception.
     */
    void addForwardServer(ForwardServer forwardServer) throws NoSuchMethodException, IOException;

    /**
     * Get the forward server.
     *
     * @return The forward server.
     */
    ForwardServer getForwardServer();
}
