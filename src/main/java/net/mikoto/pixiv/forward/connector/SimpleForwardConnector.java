package net.mikoto.pixiv.forward.connector;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import net.mikoto.pixiv.api.http.forward.artwork.GetImage;
import net.mikoto.pixiv.api.http.forward.artwork.GetInformation;
import net.mikoto.pixiv.api.model.Artwork;
import net.mikoto.pixiv.api.model.ForwardServer;
import net.mikoto.pixiv.api.model.Series;
import net.mikoto.pixiv.forward.connector.exception.GetArtworkInformationException;
import net.mikoto.pixiv.forward.connector.exception.GetImageException;
import net.mikoto.pixiv.forward.connector.exception.GetSeriesInformationException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static net.mikoto.pixiv.api.http.forward.artwork.GetInformation.PARAM_ARTWORK_ID;
import static net.mikoto.pixiv.api.http.forward.artwork.GetInformation.PARAM_KEY;
import static net.mikoto.pixiv.api.util.HttpApiUtil.getHttpApi;

/**
 * @author mikoto
 * @date 2022/4/3 2:28
 */
@Component("forwardConnector")
public class SimpleForwardConnector implements net.mikoto.pixiv.forward.connector.ForwardConnector {
    /**
     * Constants
     */
    private static final Set<ForwardServer> FORWARD_SERVER_SET = new HashSet<>();
    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .build();
    private static final String SUCCESS_KEY = "success";
    private static final String BODY = "body";
    private static final int SUCCESS_CODE = 200;

    /**
     * Get the information of the artwork.
     * It will <b>not<b/> confirm the artwork's sign.
     * You should use the pixiv-forward version v1.3.2 or newer.
     * Get the release at: <a href="https://github.com/mikoto-tech/pixiv-forward/releases/tag/v1.3.2">Pixiv-Forward v1.3.2</a>
     *
     * @param artworkId The id of this artwork.
     * @return The artwork.
     * @throws IOException                    An exception.
     * @throws GetArtworkInformationException An exception.
     */
    @Override
    public Artwork getArtworkById(int artworkId) throws IOException, GetArtworkInformationException {
        Artwork artwork;
        ForwardServer forwardServer = getForwardServer();
        Request artworkRequest = new Request.Builder()
                .url(
                        forwardServer.getAddress() + getHttpApi(
                                GetInformation.class,
                                PARAM_KEY + forwardServer.getKey(),
                                PARAM_ARTWORK_ID + artworkId
                        )
                )
                .get()
                .build();
        Response artworkResponse = OK_HTTP_CLIENT.newCall(artworkRequest).execute();
        if (artworkResponse.code() == SUCCESS_CODE) {
            JSONObject jsonObject = JSON.parseObject(Objects.requireNonNull(artworkResponse.body()).string());
            artworkResponse.close();
            if (jsonObject != null) {
                if (jsonObject.getBoolean(SUCCESS_KEY)) {
                    artwork = jsonObject.getObject(BODY, Artwork.class);
                } else {
                    throw new GetArtworkInformationException(jsonObject.getString("message"));
                }
            } else {
                throw new GetArtworkInformationException("The json object is null!");
            }
        } else {
            throw new GetArtworkInformationException(String.valueOf(artworkResponse.code()));
        }
        return artwork;
    }

    /**
     * Get the image of the artwork.
     *
     * @param url The url of this image.
     * @return The image data.
     * @throws IOException       An exception.
     * @throws GetImageException An exception.
     */
    @Override
    public byte[] getImage(String url) throws GetImageException, IOException {
        ForwardServer forwardServer = getForwardServer();
        Request imageRequest = new Request.Builder()
                .url(forwardServer.getAddress() + getHttpApi(GetImage.class, forwardServer.getKey(), url))
                .get()
                .build();
        Response imageResponse = OK_HTTP_CLIENT.newCall(imageRequest).execute();
        if (imageResponse.code() == SUCCESS_CODE) {
            byte[] bytes;
            try {
                bytes = Objects.requireNonNull(imageResponse.body()).bytes();
            } catch (NullPointerException | IOException e) {
                throw new GetImageException(e.getMessage());
            } finally {
                imageResponse.close();
            }
            return bytes;
        } else {
            throw new GetImageException(String.valueOf(imageResponse.code()));
        }
    }

    /**
     * Get the series.
     *
     * @param seriesId The id of the series.
     * @return A series object.
     */
    @Override
    public Series getSeriesInformation(int seriesId) throws IOException, GetSeriesInformationException {
        Series series;
        ForwardServer forwardServer = getForwardServer();
        Request seriesRequest = new Request.Builder()
                .url(
                        forwardServer.getAddress() +
                                getHttpApi(
                                        net.mikoto.pixiv.api.http.forward.series.GetInformation.class,
                                        forwardServer.getKey(),
                                        String.valueOf(seriesId)
                                )
                )
                .get()
                .build();
        Response seriesResponse = OK_HTTP_CLIENT.newCall(seriesRequest).execute();
        if (seriesResponse.code() == SUCCESS_CODE) {
            JSONObject jsonObject = JSON.parseObject(Objects.requireNonNull(seriesResponse.body()).string());
            seriesResponse.close();
            if (jsonObject != null) {
                if (jsonObject.getBoolean(SUCCESS_KEY)) {
                    series = JSONObject.parseObject(jsonObject.getJSONObject(BODY).toJSONString(), Series.class);
                } else {
                    throw new GetSeriesInformationException(jsonObject.getString("message"));
                }
            } else {
                throw new GetSeriesInformationException("The json object is null!");
            }
        } else {
            throw new GetSeriesInformationException(String.valueOf(seriesResponse.code()));
        }
        return series;
    }

    /**
     * Add a forward server.
     *
     * @param forwardServer The forward server address.
     */
    @Override
    public synchronized void addForwardServer(@NotNull ForwardServer forwardServer) {
        FORWARD_SERVER_SET.add(forwardServer);
    }

    /**
     * Get the forward server.
     *
     * @return The forward server.
     */
    @Override
    public synchronized ForwardServer getForwardServer() {
        ForwardServer resultForwardServer = new ForwardServer("You haven't set any forward server", 0);
        int weightSum = 0;

        for (ForwardServer forwardServer :
                FORWARD_SERVER_SET) {
            forwardServer.setCurrentWeight(forwardServer.getCurrentWeight() + forwardServer.getWeight());
            if (forwardServer.getCurrentWeight() > resultForwardServer.getCurrentWeight()) {
                resultForwardServer = forwardServer;
            }
            weightSum += forwardServer.getWeight();
        }

        resultForwardServer.setCurrentWeight(resultForwardServer.getCurrentWeight() - weightSum);

        return resultForwardServer;
    }
}
