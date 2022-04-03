package net.mikoto.pixiv.forward.connector.test;

import net.mikoto.pixiv.api.pojo.Artwork;
import net.mikoto.pixiv.api.pojo.ForwardServer;
import net.mikoto.pixiv.forward.connector.ForwardConnector;
import net.mikoto.pixiv.forward.connector.exception.GetArtworkInformationException;
import net.mikoto.pixiv.forward.connector.exception.GetImageException;
import net.mikoto.pixiv.forward.connector.exception.WrongSignException;
import net.mikoto.pixiv.forward.connector.impl.ForwardConnectorImpl;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

/**
 * @author mikoto
 * @date 2022/4/3 2:50
 */
public class ForwardControllerTest {
    @Test
    public void forwardControllerTest() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchMethodException, IllegalAccessException, WrongSignException, GetArtworkInformationException, GetImageException {
        ForwardConnector forwardConnector = new ForwardConnectorImpl();

        ForwardServer forwardServer = new ForwardServer("server", 1, "key");

        forwardConnector.addForwardServer(forwardServer);

        Artwork artwork = forwardConnector.getArtworkInformation(91262365);
        System.out.println(artwork.getArtworkTitle());

        File file = new File("test.jpg");

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(forwardConnector.getImage(artwork.getIllustUrls().get("original")));
        fileOutputStream.close();
    }
}
