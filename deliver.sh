echo "开始构建了"

docker build -t stanics/zhangli:0.1 .
docker push stanics/zhangli:0.1
docker tag stanics/zhangli:0.1 stanics/zhangli:0.1:latest
docker push stanics/zhangli:latest