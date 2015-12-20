FROM java:7

ADD authn-service-1.0.0.jar .

ADD docker.yml .

CMD java -jar authn-service-1.0.0.jar server docker.yml

EXPOSE 8080