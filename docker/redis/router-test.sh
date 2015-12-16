# read-write test
java -jar /d/dev/workspaces/personale/hcomb/integ/redis-iot/target/riot-1.0.0.jar get localhost 7379 test.queue

java -jar /d/dev/workspaces/personale/hcomb/integ/redis-iot/target/riot-1.0.0.jar put localhost 7379 test.queue hello! 3 1000


# router test
java -jar /d/dev/workspaces/personale/hcomb/integ/riot/target/riot-1.0.0.jar put localhost 6379 user.new {name:pluto\,id:RAND} 10000 1

java -jar /d/dev/workspaces/personale/hcomb/integ/riot/target/riot-1.0.0.jar get localhost 7379 user.hello
java -jar /d/dev/workspaces/personale/hcomb/integ/riot/target/riot-1.0.0.jar get localhost 7379 user.provision
java -jar /d/dev/workspaces/personale/hcomb/integ/riot/target/riot-1.0.0.jar get localhost 7379 welcome.mail
