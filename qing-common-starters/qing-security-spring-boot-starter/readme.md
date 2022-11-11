\在实际的开发应用中，使用jwt不存储在服务器端，想踢出用户还需要在redis中
存储，redis在后台应用中也不会是性能瓶颈
所有jwt只是用于存储用户的信息，在不需要敏感信息时加入信息

注意url的命名
.antMatchers("/auth/**").permitAll()
.antMatchers("/public/**").permitAll()
