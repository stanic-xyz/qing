### 1、本地安装

## 下载安装包

### Generate a Certificate Authority Certificate

1.Generate a CA certificate private key.

```shell

cd ~/harbor

pwd
#~/harbor
openssl genrsa -out ca.key 4096
```

2.Generate the CA certificate.

```shell
openssl req -x509 -new -nodes -sha512 -days 3650 \
 -subj "/C=CN/ST=Beijing/L=Beijing/O=stan/OU=Personal/CN=harbor.chenyunlong.cn" \
 -key ca.key \
 -out ca.crt
```

### Generate a Server Certificate

#### Generate a private key.

```shell
openssl genrsa -out harbor.chenyunlong.cn.key 4096
```

#### Generate a certificate signing request (CSR).

```shell
openssl req -sha512 -new \
    -subj "/C=CN/ST=Beijing/L=Beijing/O=stan/OU=Personal/CN=harbor.chenyunlong.cn" \
    -key harbor.chenyunlong.cn.key \
    -out harbor.chenyunlong.cn.csr
```

```shell
cat > v3.ext <<-EOF
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
keyUsage = digitalSignature, nonRepudiation, keyEncipherment, dataEncipherment
extendedKeyUsage = serverAuth
subjectAltName = @alt_names

[alt_names]
DNS.1=harbor.chenyunlong.cn
DNS.2=192.168.3.16
DNS.3=stanic.xyz
EOF
```

```shell
openssl x509 -req -sha512 -days 3650 \
    -extfile v3.ext \
    -CA ca.crt -CAkey ca.key -CAcreateserial \
    -in yourdomain.com.csr \
    -out yourdomain.com.crt
```
