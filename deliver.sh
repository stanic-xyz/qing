echo "开始构建了"

docker build -t stanics/zhanlgi:0.1 .
docker push stanics/zhanlgi:0.1
docker tag stanics/zhanlgi:0.1 stanics/zhanlgi:$0.1:latest
docker push stanics/zhanlgi:latest