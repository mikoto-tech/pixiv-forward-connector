package net.mikoto.pixiv.forward.connector.client;

import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Var;
import org.springframework.stereotype.Component;

import static net.mikoto.pixiv.api.http.HttpApi.FORWARD_ARTWORK;
import static net.mikoto.pixiv.api.http.HttpApi.FORWARD_ARTWORK_GET_INFORMATION;
import static net.mikoto.pixiv.api.http.forward.artwork.GetInformation.PARAM_ARTWORK_ID;
import static net.mikoto.pixiv.api.http.forward.artwork.GetInformation.PARAM_KEY;

/**
 * @author mikoto
 * @date 2022/6/25 2:29
 */
@Component
public interface PixivForwardClient {
    /**
     * Get the artwork(raw) from pixiv-forward.
     *
     * @param url       Pixiv forward url.
     * @param key       Pixiv forward key.
     * @param artworkId The artwork id.
     * @return Result.
     */
    @Get(
            url = "{url}" +
                    FORWARD_ARTWORK + FORWARD_ARTWORK_GET_INFORMATION + "?" +
                    PARAM_KEY + "{key}&" +
                    PARAM_ARTWORK_ID + "{artworkId}"
    )
    String getArtwork(@Var("url") String url, @Var("key") String key, @Var("artworkId") int artworkId);
}
