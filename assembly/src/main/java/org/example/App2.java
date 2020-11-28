package org.example;

import com.google.common.base.Preconditions;
import lombok.SneakyThrows;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OutputEncryptor;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;


public class App2 {

    private static String PROVIDER = "BC";//"SunJCE";

    @SneakyThrows
    public static void certificateAndPrivateKey() {
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509", "BC");

        X509Certificate certificate = (X509Certificate) certFactory
                .generateCertificate(App2.class.getResourceAsStream("/Baeldung.cer"));

        char[] keystorePassword = "password".toCharArray();
        char[] keyPassword = "password".toCharArray();

        KeyStore keystore = KeyStore.getInstance("PKCS12");
        keystore.load(App2.class.getResourceAsStream("/Baeldung.p12"), keystorePassword);
        PrivateKey key = (PrivateKey) keystore.getKey("baeldung", keyPassword);
    }

    @SneakyThrows
    private static X509Certificate certificate() {
//        CertificateFactory certFactory = CertificateFactory.getInstance("X.509", "BC");

//        return (X509Certificate) certFactory.generateCertificate(
//                App2.class.getResourceAsStream("/Baeldung.cer")
//        );

        CertificateFactory certFactory = CertificateFactory.getInstance("X.509", PROVIDER);

        return (X509Certificate) certFactory.generateCertificate(
                App2.class.getResourceAsStream("/domain.crt")
        );
    }

    @SneakyThrows
    private static PrivateKey privateKey() {
//        char[] keystorePassword = "password".toCharArray();
//        char[] keyPassword = "password".toCharArray();
//
//        KeyStore keystore = KeyStore.getInstance("PKCS12");
//        keystore.load(App2.class.getResourceAsStream("/Baeldung.p12"), keystorePassword);
//        return (PrivateKey) keystore.getKey("baeldung", keyPassword);

        InputStream inputStream = App2.class.getResourceAsStream("/domain.key");

        byte[] bytes = new byte[inputStream.available()];

        Preconditions.checkState(inputStream.read(bytes) != 0);

        String encoded = new String(bytes);

        encoded = encoded.replaceAll("(-----BEGIN PRIVATE KEY-----)|(-----END PRIVATE KEY-----)", "");

        EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getMimeDecoder().decode(encoded));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }


    public static byte[] encryptData(byte[] data,
                                     X509Certificate encryptionCertificate)
            throws CertificateEncodingException, CMSException, IOException {

        byte[] encryptedData = null;
        if (null != data && null != encryptionCertificate) {
            CMSEnvelopedDataGenerator cmsEnvelopedDataGenerator
                    = new CMSEnvelopedDataGenerator();

            JceKeyTransRecipientInfoGenerator jceKey
                    = new JceKeyTransRecipientInfoGenerator(encryptionCertificate);

            cmsEnvelopedDataGenerator.addRecipientInfoGenerator(jceKey);
            CMSTypedData msg = new CMSProcessableByteArray(data);
            OutputEncryptor encryptor
                    = new JceCMSContentEncryptorBuilder(CMSAlgorithm.AES128_CBC)
                    .setProvider(PROVIDER).build();
            CMSEnvelopedData cmsEnvelopedData = cmsEnvelopedDataGenerator
                    .generate(msg, encryptor);
            encryptedData = cmsEnvelopedData.getEncoded();
        }
        return encryptedData;
    }

    public static byte[] decryptData(
            byte[] encryptedData,
            PrivateKey decryptionKey)
            throws CMSException {

        byte[] decryptedData = null;
        if (null != encryptedData && null != decryptionKey) {
            CMSEnvelopedData envelopedData = new CMSEnvelopedData(encryptedData);

            Collection<RecipientInformation> recipients
                    = envelopedData.getRecipientInfos().getRecipients();
            KeyTransRecipientInformation recipientInfo
                    = (KeyTransRecipientInformation) recipients.iterator().next();
            JceKeyTransRecipient recipient
                    = new JceKeyTransEnvelopedRecipient(decryptionKey);

            return recipientInfo.getContent(recipient);
        }
        return decryptedData;
    }

    public static byte[] signData(
            byte[] data,
            X509Certificate signingCertificate,
            PrivateKey signingKey) throws Exception {

        byte[] signedMessage = null;
        List<X509Certificate> certList = new ArrayList<X509Certificate>();
        CMSTypedData cmsData = new CMSProcessableByteArray(data);
        certList.add(signingCertificate);
        Store certs = new JcaCertStore(certList);

        CMSSignedDataGenerator cmsGenerator = new CMSSignedDataGenerator();
        ContentSigner contentSigner
                = new JcaContentSignerBuilder("SHA256withRSA").build(signingKey);
        cmsGenerator.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(
                new JcaDigestCalculatorProviderBuilder().setProvider(PROVIDER)
                        .build()).build(contentSigner, signingCertificate));
        cmsGenerator.addCertificates(certs);

        CMSSignedData cms = cmsGenerator.generate(cmsData, true);
        signedMessage = cms.getEncoded();
        return signedMessage;
    }

    @SneakyThrows
    public static boolean verifyData(byte[] signedData) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(signedData);
        ASN1InputStream asnInputStream = new ASN1InputStream(inputStream);
        CMSSignedData cmsSignedData = new CMSSignedData(ContentInfo.getInstance(asnInputStream.readObject()));

        SignerInformationStore signers = cmsSignedData.getSignerInfos();
        SignerInformation signer = signers.getSigners().iterator().next();
        Collection<X509CertificateHolder> certCollection = cmsSignedData.getCertificates().getMatches(signer.getSID());
        X509CertificateHolder certHolder = certCollection.iterator().next();

        return signer.verify(new JcaSimpleSignerInfoVerifierBuilder().build(certHolder));
    }

    //CMS/PKCS7 Encryption and Decryption
    @SneakyThrows
    public static void m1() {
        System.out.println("-------> m1");
        String secretMessage = "My password is 123456Seven";
        System.out.println("Original Message : " + secretMessage);
        byte[] stringToEncrypt = secretMessage.getBytes();
        byte[] encryptedData = encryptData(stringToEncrypt, certificate());
        System.out.println("Encrypted Message : " + new String(encryptedData));
        byte[] rawData = decryptData(encryptedData, privateKey());
        String decryptedMessage = new String(rawData);
        System.out.println("Decrypted Message : " + decryptedMessage);
        System.out.println("<------- m1");
    }

    //CMS/PKCS7 Signature and Verification
    @SneakyThrows
    public static void m2() {
        System.out.println("-------> m2");
        byte[] rawData = "Hello world !".getBytes();

        byte[] signedData = signData(rawData, certificate(), privateKey());
        Boolean check = verifyData(signedData);
        System.out.println(check);
        System.out.println("<------- m1");
    }


    public static void main(String[] args) {
        Security.setProperty("crypto.policy", "unlimited");

        //{JAVA_HOME}/jre/lib/security/java.security
        //security.provider.N = org.bouncycastle.jce.provider.BouncyCastleProviders

        Security.addProvider(new BouncyCastleProvider());

        m1();

        m2();

    }

}
