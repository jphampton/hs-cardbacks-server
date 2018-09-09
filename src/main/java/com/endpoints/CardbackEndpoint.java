package com.endpoints;

import com.google.api.server.spi.config.ApiIssuer;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.models.Cardback;
import com.models.Month;
import com.storage.CardbackDao;
import com.google.api.server.spi.config.Api;

import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Logger;

@Api(
    name = "cardback",
    version = "v1",
    namespace =
    @ApiNamespace(
        ownerDomain = "cardback.endpoints.com",
        ownerName = "cardback.endpoints.com",
        packagePath = ""
    ),
    issuers = {
        @ApiIssuer(
            name = "firebase",
            issuer = "https://securetoken.google.com/hearthstonecardbacks-215221",
            jwksUri = "https://www.googleapis.com/service_accounts/v1/metadata/x509/securetoken@system"
                + ".gserviceaccount.com"
        )
    }
)
public class CardbackEndpoint {
  private static final Logger log = Logger.getLogger(CardbackEndpoint.class.getName());

  @ApiMethod(name = "current_cardback")
  public Cardback currentCardback(@Named("month") Month month, @Named("year") int year) throws Exception {
    log.warning(String.format("Month and year currentCardback %d %d", month.ordinal(), year));
    CardbackDao cd = new CardbackDao();
    Cardback current = cd.getMonthYear(month, year);
    String rawUrl = current.imgURL;
    String url = getSignedUrl(rawUrl);
    return current.toBuilder().setImgURL(url).build();
  }

  private String getSignedUrl(String raw) throws Exception {
    long now = System.currentTimeMillis();
    int minutes = 3;
    long expiredTimeInSeconds = (now + 60 * minutes * 1000L) / 1000;
    String expiry = expiredTimeInSeconds + "";
    PrivateKey pk = getPrivateKey();
    String objectPath = raw.substring(30);
    String stringToSign = "GET" + "\n"
        + "" + "\n"
        + "" + "\n"
        + expiry + "\n"
        + objectPath;
    String signedString = getSignedString(stringToSign, pk);
    signedString = URLEncoder.encode(signedString, "UTF-8");
    String signedUrl = raw
        + "?GoogleAccessId=" + PrivateConstants.CLIENT_EMAIL
        + "&Expires=" + expiry
        + "&Signature=" + signedString;
    return signedUrl;
  }


  private static String getSignedString(String input, PrivateKey pk) throws Exception {
    Signature privateSignature = Signature.getInstance("SHA256withRSA");
    privateSignature.initSign(pk);
    privateSignature.update(input.getBytes("UTF-8"));
    byte[] s = privateSignature.sign();
    return Base64.getEncoder().encodeToString(s);
  }

  private static PrivateKey getPrivateKey() throws Exception {
    // Remove extra characters in private key.
    byte[] b1 = Base64.getDecoder().decode(PrivateConstants.PRIVATE_KEY);
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b1);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    return kf.generatePrivate(spec);
  }

}

