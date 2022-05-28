package net.mikoto.pixiv.forward.connector;

import net.mikoto.pixiv.api.connector.ArtworkDataSource;
import net.mikoto.pixiv.api.model.Artwork;
import net.mikoto.pixiv.api.model.ForwardServer;
import net.mikoto.pixiv.api.model.Series;
import net.mikoto.pixiv.forward.connector.exception.GetArtworkInformationException;
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
public interface ForwardConnector extends ArtworkDataSource {
    /**
     * Get the information of the artwork.
     * It will confirm the artwork's sign.
     * You should use the pixiv-forward version v1.2.5 or newer.
     * Get the release at: <a href="https://github.com/mikoto2464/pixiv-forward/releases">pixiv-forward</a>
     *
     * @param artworkId The id of this artwork.
     * @return The artwork.
     * @throws NoSuchMethodException          An exception.
     * @throws IOException                    An exception.
     * @throws InvalidKeySpecException        An exception.
     * @throws NoSuchMethodException          An exception.
     * @throws SignatureException             An exception.
     * @throws InvalidKeyException            An exception.
     * @throws IllegalAccessException         An exception.
     * @throws NoSuchAlgorithmException       An exception.
     * @throws GetArtworkInformationException An exception.
     * @throws WrongSignException             An exception.
     */
    Artwork getArtworkInformation(int artworkId) throws NoSuchMethodException, IOException, InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, IllegalAccessException, WrongSignException, GetArtworkInformationException;

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
