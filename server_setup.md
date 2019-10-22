After setting up a new Ubuntu18:04 server


# first update and upgrade
```bash
sudo apt-get update
sudo apt-get upgrade
```

# install nginx 
```bash
sudo apt-get install nginx 
```

# install docker
```bash
sudo apt install apt-transport-https ca-certificates curl software-properties-common
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu bionic stable"
sudo apt update
sudo apt install docker-ce
```


# install letsencrpyt tools
```bash
sudo add-apt-repository ppa:certbot/certbot
sudo apt install python-certbot-nginx
```

# setup nginx for default domain validation
```bash
cd /etc/nginx/sites-enabled
rm default
nano api
```

copy this to file
```
server {
    listen 80;
    server_name api.traderx.company www.api.traderx.company;

    root /var/www/html;

    location / {
        proxy_set_header   X-Real-IP $remote_addr;
        proxy_set_header   Host      $http_host;
        proxy_pass http://127.0.0.1:8080;
    }
    
    location ~ /.well-known {
        allow all;
    }

}
```


# restart or reload nginx settings
```bash
service nginx restart
```

# validate domain
```bash
sudo certbot --nginx -d api.traderx.company 
```

# if no errors remove the current nginx setting
```bash
rm api
nano api
```


# create a new one and add below settings for an auto https redirect

```
server {                                                                                                                                                                                                                                     
    listen 80;                                                                                                                                                                                                                               
    listen 443 ssl http2;                                                                                                                                                                                                                    
    server_name api.traderx.company www.api.traderx.company;                                                                                                                                                                             
    ssl_protocols TLSv1.2;                                                                                                                                                                                                                   
    ssl_ciphers EECDH+AES128:RSA+AES128:EECDH+AES256:RSA+AES256:EECDH+3DES:RSA+3DES:!MD5;                                                                                                                                                    
    ssl_prefer_server_ciphers On;       
    ssl_trusted_certificate /etc/letsencrypt/live/api.traderx.company/chain.pem;
    ssl_session_cache shared:SSL:128m;
    add_header Strict-Transport-Security "max-age=31557600; includeSubDomains";
    ssl_stapling on;
    ssl_stapling_verify on;
    # Your favorite resolver may be used instead of the Google one below
    resolver 8.8.8.8;
    root /var/www/html/;
    index index.html;

    location ~ /.well-known {
                allow all;
    }

    location / {
        if ($scheme = http) {
            return 301 https://$server_name$request_uri;
        }
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        client_max_body_size 20M;
        proxy_pass http://localhost:8080;
    }
    
   
    ssl_certificate /etc/letsencrypt/live/api.traderx.company/fullchain.pem; # managed by Certbot
    ssl_certificate_key /etc/letsencrypt/live/api.traderx.company/privkey.pem; # managed by Certbot
}

```
# restart or reload nginx settings
```bash
service nginx restart
```
We will do similar setup for website domain as well

# add default settings for website domain validation
```bash
nano website
```

Copy this into website file

```
server {
    listen 80;
    server_name traderx.company www.traderx.company;

    root /var/www/html;

    location / {
        proxy_set_header   X-Real-IP $remote_addr;
        proxy_set_header   Host      $http_host;
        proxy_pass http://127.0.0.1:3000;
    }
    
    location ~ /.well-known {
        allow all;
    }

}
```

# validate website domain
```bash
sudo certbot --nginx -d traderx.company  
```
if validation was successful then remove current setup and add https redirects
```bash
rm website
nano website
```

copy this into website file
```
server {                                                                                                                                                                                                                                     
    listen 80;                                                                                                                                                                                                                               
    listen 443 ssl http2;                                                                                                                                                                                                                    
    server_name traderx.company www.traderx.company;                                                                                                                                                                             
    ssl_protocols TLSv1.2;                                                                                                                                                                                                                   
    ssl_ciphers EECDH+AES128:RSA+AES128:EECDH+AES256:RSA+AES256:EECDH+3DES:RSA+3DES:!MD5;                                                                                                                                                    
    ssl_prefer_server_ciphers On;       
    ssl_trusted_certificate /etc/letsencrypt/live/traderx.company/chain.pem;
    ssl_session_cache shared:SSL:128m;
    add_header Strict-Transport-Security "max-age=31557600; includeSubDomains";
    ssl_stapling on;
    ssl_stapling_verify on;
    # Your favorite resolver may be used instead of the Google one below
    resolver 8.8.8.8;
    root /var/www/html/;
    index index.html;

    location ~ /.well-known {
                allow all;
    }

    location / {
        if ($scheme = http) {
            return 301 https://$server_name$request_uri;
        }
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        client_max_body_size 20M;
        proxy_pass http://localhost:3000;
    }
    
   
    ssl_certificate /etc/letsencrypt/live/traderx.company/fullchain.pem; # managed by Certbot
    ssl_certificate_key /etc/letsencrypt/live/traderx.company/privkey.pem; # managed by Certbot
}
```
