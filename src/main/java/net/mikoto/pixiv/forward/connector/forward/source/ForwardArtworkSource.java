package net.mikoto.pixiv.forward.connector.forward.source;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import net.mikoto.pixiv.api.http.forward.artwork.GetInformation;
import net.mikoto.pixiv.api.model.Artwork;
import net.mikoto.pixiv.api.model.ForwardServer;
import net.mikoto.pixiv.api.patcher.connector.Connector;
import net.mikoto.pixiv.api.patcher.connector.HttpConnector;
import net.mikoto.pixiv.api.patcher.source.ArtworkSource;
import net.mikoto.pixiv.forward.connector.exception.GetArtworkInformationException;
import net.mikoto.pixiv.forward.connector.exception.NoSuchArtworkException;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Objects;

import static net.mikoto.pixiv.api.http.forward.artwork.GetInformation.PARAM_ARTWORK_ID;
import static net.mikoto.pixiv.api.http.forward.artwork.GetInformation.PARAM_KEY;
import static net.mikoto.pixiv.api.util.HttpApiUtil.getHttpApi;

/**
 * @author mikoto
 * @date 2022/6/18 17:35
 */
public class ForwardArtworkSource implements ArtworkSource<ForwardServer> {
    private static final String SUCCESS_KEY = "success";
    private static final String BODY = "body";
    private static final int SUCCESS_CODE = 200;

    @Override
    public Artwork obtain(int dataId, Connector<ForwardServer> connector) throws Exception {
        if (connector instanceof HttpConnector) {
            Artwork artwork;
            ForwardServer forwardServer = connector.getServer();
            Request artworkRequest = new Request.Builder()
                    .url(
                            forwardServer.getAddress() + getHttpApi(
                                    GetInformation.class,
                                    PARAM_KEY + forwardServer.getKey(),
                                    PARAM_ARTWORK_ID + dataId
                            )
                    )
                    .get()
                    .build();
            Response artworkResponse = ((HttpConnector<ForwardServer>) connector).getHttpClient().newCall(artworkRequest).execute();
            if (artworkResponse.code() == SUCCESS_CODE) {
                JSONObject jsonObject = JSON.parseObject(Objects.requireNonNull(artworkResponse.body()).string());
                artworkResponse.close();
                if (jsonObject != null) {
                    if (jsonObject.getBoolean(SUCCESS_KEY)) {
                        artwork = jsonObject.getObject(BODY, Artwork.class);
                    } else {
                        throw new NoSuchArtworkException(String.valueOf(dataId));
                    }
                } else {
                    throw new GetArtworkInformationException("The json object is null!");
                }
            } else {
                throw new GetArtworkInformationException(String.valueOf(artworkResponse.code()));
            }
            return artwork;
        } else {
            throw new RuntimeException("No http client");
        }
    }
}
