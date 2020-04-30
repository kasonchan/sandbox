# ssl-config

## Steps to create keystore.pkcs12

Create server.key using passphrase

```
openssl genrsa [-des3] -out server.key [2048|4096]
Generating RSA private key, 2048 bit long modulus
.......................+++
........................+++
e is 65537 (0x10001)
Enter pass phrase for server.key:
Verifying - Enter pass phrase for server.key:
Remove [-des3] for passphraseless server.key
```

Create Certificate Signing Request (CSR) server.csr

```
openssl req -new -key server.key -out server.csr
Enter pass phrase for server.key:
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) []:US
State or Province Name (full name) []:California
Locality Name (eg, city) []:San Francisco
Organization Name (eg, company) []:Template Company
Organizational Unit Name (eg, section) []:Template Org
Common Name (eg, fully qualified host name) []:localhost
Email Address []:.

Please enter the following 'extra' attributes
to be sent with your certificate request
A challenge password []:
```

Create Certificate (CRT) server.crt

```
openssl x509 -req -days 365 -in server.csr -signkey server.key -out server.crt
Signature ok
subject=/C=US/ST=California/L=San Francisco/O=Template Company/OU=Template Org/CN=templateCA
Getting Private key
Enter pass phrase for server.key:
```

Create a PEM

```
A .pem (Privacy Enhanced Mail) file is a container format that may just include the public certificate or the entire certificate chain (private key, public key, root certificates).
cat server.key > server.pem
cat server.crt >> server.pem
```

Create PKCS12

> A PKCS12 (Public-Key Cryptography Standards) defines an archive-file format for storing server certificates, intermediate certificate if any and private key into a single encryptable  file. 

```
openssl pkcs12 -export -in server.pem -out keystore.pkcs12
Enter pass phrase for server.pem: 
Enter Export Password: 
Verifying - Enter Export Password:
```

Copy the PKCS12 file to resources

```
cp keystore.pkcs12 src/main/resources
```

## References
- https://doc.akka.io/docs/akka-http/current/server-side/server-https-support.html
- https://docs.oracle.com/javase/8/docs/technotes/guides/security/certpath/CertPathProgGuide.html
- https://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html#CreateKeystore
- https://tersesystems.com/blog/2014/03/20/fixing-x509-certificates/
- https://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html
