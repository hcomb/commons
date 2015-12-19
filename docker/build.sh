echo " * building rrouter-api"
cd /d/dev/workspaces/personale/hcomb/integ/rrouter-api
mvn -q clean install

echo " * building rrouter-client"
cd /d/dev/workspaces/personale/hcomb/integ/rrouter-client
mvn -q clean install

echo " * building service-common"
cd /d/dev/workspaces/personale/hcomb/commons/service-common
mvn -q clean install

echo " * building authn-api"
cd /d/dev/workspaces/personale/hcomb/authn/authn-api
mvn -q clean install

echo " * building authn-client"
cd /d/dev/workspaces/personale/hcomb/authn/authn-client
mvn -q clean install

echo " * building authz-api"
cd /d/dev/workspaces/personale/hcomb/authz/authz-api
mvn -q clean install

echo " * building authz-client"
cd /d/dev/workspaces/personale/hcomb/authz/authz-client
mvn -q clean install

echo " * building authn-service"
cd /d/dev/workspaces/personale/hcomb/authn/authn-service
mvn -q clean install

echo " * building authz-service"
cd /d/dev/workspaces/personale/hcomb/authz/authz-service
mvn -q clean install

echo " * building rrouter-service"
cd /d/dev/workspaces/personale/hcomb/integ/rrouter-service
mvn -q clean install