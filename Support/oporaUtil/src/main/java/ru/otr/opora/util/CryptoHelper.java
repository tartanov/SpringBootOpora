package ru.otr.opora.util;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class CryptoHelper {
    //getCertPrincipal возвращает строку о владельце сертификата из base64 представления сертификата userCert
    public static String getCertPrincipal(String userCert) throws IOException, CertificateException {
        String certPrincipal = "ошибка получения данных из сертификата";
        try (ByteArrayInputStream bais = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(userCert));) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate)cf.generateCertificate(bais);
            certPrincipal= cert.getSubjectX500Principal().toString();
        }
        return certPrincipal;
    }
}
