package net.mikoto.pixiv.forward.connector.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.mikoto.pixiv.api.http.forward.artwork.GetImage;
import net.mikoto.pixiv.api.http.forward.artwork.GetInformation;
import net.mikoto.pixiv.api.http.forward.web.PublicKey;
import net.mikoto.pixiv.api.pojo.Artwork;
import net.mikoto.pixiv.api.pojo.ForwardServer;
import net.mikoto.pixiv.forward.connector.exception.GetArtworkInformationException;
import net.mikoto.pixiv.forward.connector.exception.GetImageException;
import net.mikoto.pixiv.forward.connector.exception.WrongSignException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static net.mikoto.pixiv.api.util.HttpApiUtil.getHttpApi;
import static net.mikoto.pixiv.forward.connector.util.RsaUtil.getPublicKey;
import static net.mikoto.pixiv.forward.connector.util.RsaUtil.verify;
import static net.mikoto.pixiv.forward.connector.util.Sha256Util.getSha256;

/**
 * @author mikoto
 * @date 2022/4/3 2:28
 */
public class ForwardConnectorImpl implements net.mikoto.pixiv.forward.connector.ForwardConnector {
    private static final Set<ForwardServer> FORWARD_SERVER_SET = new HashSet<>();
    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .build();
    private static final String SUCCESS_KEY = "success";
    private static final int SUCCESS_CODE = 200;
    private static final String SIGN = "sign";
    private static final String BODY = "body";

    /**
     * Get the information of the artwork.
     * It will confirm the artwork's sign.
     * You should use the pixiv-forward version v1.2.4 or newer.
     * Get the release at: https://github.com/mikoto2464/pixiv-forward/releases/tag/v1.2.4
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
    @Override
    public Artwork getArtworkInformation(int artworkId) throws NoSuchMethodException, IOException, InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, WrongSignException, GetArtworkInformationException {
        Artwork artwork = new Artwork();
        ForwardServer forwardServer = getForwardServer();
        Request artworkRequest = new Request.Builder()
                .url(
                        forwardServer.getAddress() +
                                getHttpApi(
                                        GetInformation.class,
                                        forwardServer.getKey(),
                                        String.valueOf(artworkId)
                                )
                )
                .get()
                .build();
        Response artworkResponse = OK_HTTP_CLIENT.newCall(artworkRequest).execute();
        if (artworkResponse.code() == SUCCESS_CODE) {
            JSONObject jsonObject = JSON.parseObject(Objects.requireNonNull(artworkResponse.body()).string());
            if (jsonObject != null) {
                if (jsonObject.getBoolean(SUCCESS_KEY)) {
                    if (verify(getSha256(jsonObject.getJSONObject(BODY).toJSONString()), getPublicKey(forwardServer.getPublicKey()), jsonObject.getString(SIGN))) {
                        artwork.loadJson(jsonObject.getJSONObject(BODY));
                    } else {
                        throw new WrongSignException(getSha256(jsonObject.getJSONObject(BODY).toJSONString()), forwardServer.getPublicKey(), jsonObject.getString(SIGN));
                    }
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
     * @throws NoSuchMethodException An exception.
     * @throws IOException           An exception.
     * @throws GetImageException     An exception.
     */
    @Override
    public byte[] getImage(String url) throws NoSuchMethodException, IOException, GetImageException {
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
            } catch (NullPointerException e) {
                throw new GetImageException(e.getMessage());
            }
            return bytes;
        } else {
            throw new GetImageException(String.valueOf(imageResponse.code()));
        }
    }

    /**
     * Add a forward server.
     *
     * @param forwardServer The forward server address.
     * @throws NoSuchMethodException An exception.
     * @throws IOException           An exception.
     */
    @Override
    public void addForwardServer(@NotNull ForwardServer forwardServer) throws NoSuchMethodException, IOException {
        Request publicKeyRequest = new Request.Builder()
                .url(forwardServer.getAddress() + getHttpApi(PublicKey.class))
                .get()
                .build();
        Response publicKeyResponse = OK_HTTP_CLIENT.newCall(publicKeyRequest).execute();
        forwardServer.setPublicKey(Objects.requireNonNull(publicKeyResponse.body()).string());
        FORWARD_SERVER_SET.add(forwardServer);
    }

    /**
     * Get the forward server.
     *
     * @return The forward server.
     */
    @Override
    public ForwardServer getForwardServer() {
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
