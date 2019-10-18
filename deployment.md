# create app folder for api and frontend for website
```bash
mkdir -p /home/app/ 
mkdir -p /home/frontend/
```
`/home/app` will be the folder where we deploy our backend application

```bash
cd /home/app/
```

clone the application from github
```bash
git clone https://github.com/bounswe/bounswe2019group6.git
cd bounswe2019group6
```

# checkout the backend-dev branch
```bash
git checkout -b backend-dev origin/backend-dev
```

# now add deployment settings file to folder
```bash
cd /home/app
nano application.yml
```
copy from default file and replace deployment ip and passwords


#create a deployment script
```bash
nano deploy.sh
```
copy this to into script

```
#!/usr/bin/env bash

# return error if no version number is provided
if [ "$#" -ne 1 ]; then
    echo "Illegal number of parameters. Specify build number"
    exit 123
fi


# replace dummy deployment settings with the actual file
cp /home/app/application.yml /home/app/bounswe2019group6/backend/TraderX/src/main/resources/application.yml


echo 'building traderx:$1'

cd /home/app/bounswe2019group6/backend/TraderX/

# build docker with given version number
docker build . -t traderx:$1


echo 'stopping old image'
docker stop traderx

echo 'cleaning up old image'
docker rm traderx

echo 'starting new image'
docker run -d --restart=unless-stopped -p 8080:8080 --name traderx traderx:$1
```

	# make it executable
```bash
chmod 755 deploy.sh
```
#deploy the api using
```bash
./deploy.sh 0.1
```

# check logs using
```bash
docker logs --tail 100 traderx
```
