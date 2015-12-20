#single redis
docker run -p 6379:6379 -d redis

#test multiple instances
docker run --name redis_frontier -p 6379:6379 -d redis
docker run --name redis_internal -p 7379:6379 -d redis
docker run --name redis_batch -p 8379:6379 -d redis