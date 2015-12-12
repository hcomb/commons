rm -rf /tmp/docker
mkdir /tmp/docker
cp /media/sf_m2repo/eu/hcomb/authn-service/1.0.0/authn-service-1.0.0.jar /tmp/docker
cp /media/sf_hcomb/authn/authn-service/docker.yml /tmp/docker
cp Dockerfile /tmp/docker
cd /tmp/docker
docker build -t hcomb/authn .
