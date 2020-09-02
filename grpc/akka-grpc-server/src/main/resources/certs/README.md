# Generate client server certificate

> Boostrap steps 1 create ca root private, public and certificate, 
> 2 create client private, public and certificate signed with ca root,
> 3 create server private, public and certificate signed with ca root.

```
$ cat client_cert_ext.cnf
basicConstraints = CA:FALSE
nsCertType = client, email
nsComment = "OpenSSL Generated Client Certificate"
subjectKeyIdentifier = hash
authorityKeyIdentifier = keyid,issuer
keyUsage = critical, nonRepudiation, digitalSignature, keyEncipherment
extendedKeyUsage = clientAuth, emailProtection

$ openssl x509 -req -in client.csr -CA intermediate.cert.pem -CAkey intermediate.key.pem -out client.cert.pem -CAcreateserial -days 365 -sha256 -extfile client_cert_ext.cnf
```

```
$ cat server_cert_ext.cnf
basicConstraints = CA:FALSE
nsCertType = server
nsComment = "OpenSSL Generated Server Certificate"
subjectKeyIdentifier = hash
authorityKeyIdentifier = keyid,issuer:always
keyUsage = critical, digitalSignature, keyEncipherment
extendedKeyUsage = serverAuth

$ openssl x509 -req -in server.csr -CA intermediate.cert.pem -CAkey intermediate.key.pem -out server.cert.pem -CAcreateserial -days 365 -sha256 -extfile server_cert_ext.cnf
```

## References

- https://www.golinuxcloud.com/openssl-create-client-server-certificate/

## Generate a ca root certificate and private key using openSSL

```
$ openssl req -x509 -sha256 -nodes -days 365 -newkey rsa:2048 -keyout ca.key -out ca.crt
```

## Generate a signing server certificate

```
$ openssl genrsa -out server.key 2048
$ openssl req -new -key server.key -out server.csr
$ openssl x509 -req -days 365 -in server.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out server.crt
```

## References

- https://www.simba.com/products/SEN/doc/Client-Server_user_guide/content/clientserver/configuringssl/signingca.htm

## Root

> Make sure you have the Common Name matching your set interface in Server code.

``` 
$ openssl genrsa -out ca.key 4096

$ openssl req -x509 -new -nodes -key ca.key -sha256 -days 1024 -out ca.crt
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) []:IE
State or Province Name (full name) []:Dublin
Locality Name (eg, city) []:Dublin
Organization Name (eg, company) []:Akka Test
Organizational Unit Name (eg, section) []:Server
Common Name (eg, fully qualified host name) []:localhost
Email Address []:
```

## Each server

```
$ openssl genrsa -out s1.key 4096

$ openssl req -new -key s1.key -out s1.csr
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) []:IE
State or Province Name (full name) []:Dublin
Locality Name (eg, city) []:Dublin
Organization Name (eg, company) []:Akka Test
Organizational Unit Name (eg, section) []:Client
Common Name (eg, fully qualified host name) []:localhost
Email Address []:

Please enter the following 'extra' attributes
to be sent with your certificate request
A challenge password []:

$ openssl req -in s1.csr -noout -text

$ openssl x509 -req -in s1.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out s1.crt -days 500 -sha256

$ openssl x509 -in s1.crt -text -noout
```

## References

- https://gist.github.com/fntlnz/cf14feb5a46b2eda428e000157447309

