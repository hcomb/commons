rm -rf /tmp/docker
mkdir /tmp/docker
cp /media/sf_m2repo/eu/hcomb/authz-service/1.0.0/authz-service-1.0.0.jar /tmp/docker
cp /media/sf_hcomb/authz/authz-service/docker.yml /tmp/docker
cp Dockerfile /tmp/docker
cd /tmp/docker
docker build -t hcomb/authz .
