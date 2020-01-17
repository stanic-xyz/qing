echo "开始构建了"

docker login --username=stanics hub.docker.com -p 4745701816Long
docker build -t stanics/zhangli:0.1 .
docker push stanics/zhangli:0.1
docker tag stanics/zhangli:0.1 stanics/zhangli:0.1:latest
docker push stanics/zhangli:latest