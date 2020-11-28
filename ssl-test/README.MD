```bash
#файлы "центра сертификации": ca-key.pem и ca-crt.pem
openssl req -new -x509 -days 9999 -config ca.cnf -keyout ca-key.pem -out ca-crt.pem

#приватный ключ
openssl genrsa -out server-key.pem 4096

#запрос на подпись сертификата
openssl req -new -config server.cnf -key server-key.pem -out server-csr.pem

#подпишем "запрос на подпись сертификата"
openssl x509 -req -extfile server.cnf -days 999 -passin "pass:password" -in server-csr.pem -CA ca-crt.pem -CAkey ca-key.pem -CAcreateserial -out server-crt.pem

#приватный ключ клиента
openssl genrsa -out client1-key.pem 4096

#запрос на подпись сертификата клиента
openssl req -new -config client1.cnf -key client1-key.pem -out client1-csr.pem

#подпишем "запрос на подпись сертификата клиента"
openssl x509 -req -extfile client1.cnf -days 999 -passin "pass:password" -in client1-csr.pem -CA ca-crt.pem -CAkey ca-key.pem -CAcreateserial -out client1-crt.pem

#проверка
openssl verify -CAfile ca-crt.pem server-crt.pem
openssl verify -CAfile ca-crt.pem client1-crt.pem

#просмотр сертификата
openssl x509 -noout -modulus -in client1-crt.pem
openssl x509 -in client1-crt.pem -noout -text

```

https://blog.regolit.com/2010/02/16/personal-ca-and-self-signed-certificates


```bash
openssl s_client -connect google.ru:443 </dev/null | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' | openssl x509 -out certificate.der -outform DER
openssl x509 -inform DER -in certificate.der -noout -text
```

```
Certificate:
    Data:
        Version: 3 (0x2)
        Serial Number:
            58:34:a2:4f:7d:e6:5b:6e:08:00:00:00:00:62:d9:00
        Signature Algorithm: sha256WithRSAEncryption
        Issuer: C = US, O = Google Trust Services, CN = GTS CA 1O1
        Validity
            Not Before: Nov  3 07:42:08 2020 GMT
            Not After : Jan 26 07:42:08 2021 GMT
        Subject: C = US, ST = California, L = Mountain View, O = Google LLC, CN = *.google.com.ru
        Subject Public Key Info:
            Public Key Algorithm: id-ecPublicKey
                Public-Key: (256 bit)
                pub:
                    04:a8:66:e3:93:83:21:37:3f:88:b8:9f:6b:3c:30:
                    84:1c:18:fb:1e:52:91:c4:79:d4:6d:96:5d:16:b1:
                    80:2b:6f:92:d0:2d:52:4a:83:5a:dd:74:3f:ee:30:
                    ec:87:57:2c:ff:bb:3f:19:b8:95:08:bd:40:9b:e9:
                    ca:6c:0f:d5:a5
                ASN1 OID: prime256v1
                NIST CURVE: P-256
        X509v3 extensions:
            X509v3 Key Usage: critical
                Digital Signature
            X509v3 Extended Key Usage:
                TLS Web Server Authentication
            X509v3 Basic Constraints: critical
                CA:FALSE
            X509v3 Subject Key Identifier:
                5D:45:E4:77:02:A9:B5:53:7C:AD:1E:65:C3:3E:95:92:48:98:35:C3
            X509v3 Authority Key Identifier:
                keyid:98:D1:F8:6E:10:EB:CF:9B:EC:60:9F:18:90:1B:A0:EB:7D:09:FD:2B

            Authority Information Access:
                OCSP - URI:http://ocsp.pki.goog/gts1o1core
                CA Issuers - URI:http://pki.goog/gsr2/GTS1O1.crt

            X509v3 Subject Alternative Name:
                DNS:*.google.com.ru, DNS:*.google.ru, DNS:google.com.ru, DNS:google.ru
            X509v3 Certificate Policies:
                Policy: 2.23.140.1.2.2
                Policy: 1.3.6.1.4.1.11129.2.5.3

            X509v3 CRL Distribution Points:

                Full Name:
                  URI:http://crl.pki.goog/GTS1O1core.crl

            CT Precertificate SCTs:
                Signed Certificate Timestamp:
                    Version   : v1 (0x0)
                    Log ID    : F6:5C:94:2F:D1:77:30:22:14:54:18:08:30:94:56:8E:
                                E3:4D:13:19:33:BF:DF:0C:2F:20:0B:CC:4E:F1:64:E3
                    Timestamp : Nov  3 08:42:08.453 2020 GMT
                    Extensions: none
                    Signature : ecdsa-with-SHA256
                                30:44:02:20:61:95:CB:78:2D:F0:B3:40:49:95:E5:80:
                                76:37:24:4F:67:1C:85:3C:B9:2D:0E:89:F5:3A:45:2F:
                                C1:2B:CC:CA:02:20:53:DC:2C:25:55:0D:AE:23:3D:B6:
                                3B:30:BA:26:00:8E:12:14:E6:C2:D2:DD:4A:C1:E9:2F:
                                48:5F:C9:B2:C8:E9
                Signed Certificate Timestamp:
                    Version   : v1 (0x0)
                    Log ID    : 44:94:65:2E:B0:EE:CE:AF:C4:40:07:D8:A8:FE:28:C0:
                                DA:E6:82:BE:D8:CB:31:B5:3F:D3:33:96:B5:B6:81:A8
                    Timestamp : Nov  3 08:42:08.504 2020 GMT
                    Extensions: none
                    Signature : ecdsa-with-SHA256
                                30:44:02:20:76:0C:4D:DA:78:17:64:C0:1E:B5:DF:3B:
                                35:F5:76:4B:83:9A:83:FF:28:F9:B0:A2:02:D4:E6:8C:
                                DD:B7:29:36:02:20:34:78:5D:53:4D:02:60:C9:50:67:
                                DA:EF:37:5F:E8:92:3F:6F:E2:DF:C7:4D:A6:86:50:8A:
                                A5:7E:C2:80:30:E7
    Signature Algorithm: sha256WithRSAEncryption
         05:e7:7d:2b:8b:4e:cf:e4:1b:b9:24:8c:b8:41:fe:1d:17:ad:
         9d:6f:c2:7b:f1:89:ab:bb:0f:07:26:66:d2:45:50:2c:e7:28:
         57:73:e8:56:11:31:94:11:ff:74:5a:1e:69:a8:12:23:b0:2e:
         0a:c2:ef:2f:94:66:25:0d:de:e6:c4:c9:62:07:75:29:ae:c0:
         81:42:c1:36:fa:e1:22:2b:76:86:f2:6c:1d:14:2f:aa:63:7c:
         fe:33:35:eb:4c:fe:c2:5e:9f:2a:ed:ed:be:29:9f:7d:f9:35:
         39:bb:91:6c:3b:da:62:96:73:5e:33:24:74:65:14:f3:53:82:
         d9:1f:bf:33:32:65:89:e8:23:20:a9:c7:39:3e:97:7c:d1:39:
         10:56:00:42:7f:0c:91:a5:34:58:67:23:94:ef:a8:f1:52:fa:
         b6:2b:b0:91:68:7f:d9:43:dd:9b:c8:a2:a0:87:ca:0a:83:dc:
         8d:8d:89:be:9a:98:07:a7:86:f3:45:54:14:94:8f:15:90:2f:
         3a:63:18:f8:16:3e:9d:97:23:b6:28:6b:17:2f:51:46:26:25:
         1f:31:85:01:f4:1c:54:2d:43:af:97:8c:54:62:63:a1:74:62:
         6b:89:28:0a:77:7f:53:d9:12:1e:6a:81:0b:4d:58:5e:10:9c:
         86:30:2a:5e
```

### Version: 3 (0x2)
Это версия не конкретно этого сертификата, а используемого «профайла», на данный момент таких версий всего три: 1, 2, 3; чаще всего используется 3. Фактическое значение поля — 2, так как нумерация версий идёт с нуля.

### Serial Number: 1a:c8:5e:b7:ae:c3:51:3c:d8:0d:85:38:5e:cf:d2:08
Это «серийный номер» сертификата, по сути он является уникальным идентификатором в базе CA, для каждого подписанного сертификата этот номер должен быть свой.

### Signature Algorithm: sha256WithRSAEncryption
Это мы уже знаем: для подписывания сертификата используется алгоритм sha256WithRSAEncryption

### Issuer: C=US, O=Symantec Corporation, OU=Symantec Trust Network, CN=Symantec Class 3 EV SSL CA - G3
Это имя (distinguished name, DN) центра авторизации, является по сути идентификатором в браузерной базе CA-сертификатов, и чтобы корректно проверифицировать серверный сертификат, браузер должен у себя иметь CA-сертификат, в поле subject которого стоит точно такое же значение, как здесь.

### Validity
В этом поле две даты, с какой и по какую сертификат считается валидным. Это поле является главной кормушкой центров авторизации: каждые год или два нужно выписывать (=подписывать у центра авторизации) новый сертификат, не бесплатно, конечно.

### Subject: 1.3.6.1.4.1.311.60.2.1.3=US/1.3.6.1.4.1.311.60.2.1.2=Delaware/businessCategory=Private Organization/serialNumber=4337446, C=US/postalCode=94103-1307, ST=California, L=San Francisco/street=1355 Market St, O=Twitter, Inc., OU=Twitter Security, CN=twitter.com
Субъект. Тот, кому выписан сертификат. Это DN, то есть структурированное поле. Обратите внимание на самые первые компоненты — 1.3.6.1.4.1.311.60.2.1.3=US/1.3.6.1.4.1.311.60.2.1.2=Delaware, в них мы видим, что openssl не смог распознать эти OID и вывели их в «сыром» виде. Вот они в репозитории OID: 1.3.6.1.4.1.311.60.2.1.3 jurisdictionOfIncorporationCountryName, 1.3.6.1.4.1.311.60.2.1.2 jurisdictionOfIncorporationStateOrProvinceName. Дальше идут уже распознанные компоненты, в качестве разделителя компонентов используется запятая или слеш, такие особенности вывода openssl.
Обратите внимание на компонент CN, это Common name, именно здесь находится имя домена, для которого выписывается сертификат.

### Subject Public Key Info
Этот блок содержит публичный ключ субъекта, синтаксически там два поля: OID алгоритма шифрования и собственно тело публичного ключа. Openssl при показе сертификата пытается показать детали публичного ключа, в данном случае он показывает модуль и экспоненту RSA-ключа. Ниже я покажу, как openssl справляется с сертификатми, с алгоритмами которых он не знаком.

### X509v3 extensions
В этом блоке содержатся так называемые расширения сертификата. Хотя структура сертификата очень жёсткая, в ней были оставлены точки расширения, чтобы можно было в сертификат добавлять дополнительные данные, которые изначально не предусмотрели. Фактически, блок с расширениями — это набор пар OID-значение, где тип значения зависит от конкретного OID. Расширения допускаются только для сертификатов версии 3.
Обычно в расширениях указываются дополнительные параметры сертификата. Например, в поле X509v3 Subject Alternative Name указываются дополнительные доменные адреса сайта, для которых сертификат применим. Или, например, в поле X509v3 Basic Constraints, указываются ограничения в применимости сертификата, в данном случае указано, что сертификат не может использоваться в качестве сертификата удостоверяющего центра.

### Signature Algorithm
Этим блоком сертификат всегда завершается, в нём содержится идентификатор алгоритма подписи и собственно подпись вышестоящего блока.
Дальше я рекомендую выполнить команду dumpasn1 certificate-twitter.com.der и сравнить только что прочитанное с показанным в дампе, параллельно полезно держать открытыми RFC 5280 и RF 3280.