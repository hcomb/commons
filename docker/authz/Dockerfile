FROM java:7

ADD authz-service-1.0.0.jar .

ADD docker.yml .

CMD java -jar authz-service-1.0.0.jar server docker.yml

EXPOSE 8180