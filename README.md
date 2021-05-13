# 山大猫猫项目

**微信搜索猫在山大小程序，快来寻找你最care的那只喵吧~**

![](https://sducat.top/file/qrcode.jpg)

### 部署方式

```bash
# 选择一个合适的目录
mkdir config
wget https://skyemperor.top/file/sducat/sducat.jar
wget -O config/application.yml https://skyemperor.top/file/sducat/application.yml
```

```bash
# 更改配置文件
vim config/application.yml

server:
  port: 6016  # 端口
  servlet:
    context-path: /sducat  # 起始路径

spring:
  application:
    name: SDUCAT
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver  # 在这里配置MySQL连接信息
    url: jdbc:mysql://127.0.0.1:3306/sducat?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowMultiQueries=true&serverTimezone=GMT%2B8&zeroDateTimeBehavior=convertToNull&allowPublicKeyRetrieval=true
    username: root
    password: pwd
    hikari:
      pool-name: MyHikariCP
      minimum-idle: 10
      maximum-pool-size: 10
      idle-timeout: 600000
      max-lifetime: 800000
      connection-timeout: 30000
      auto-commit: true
      connection-test-query: SELECT 1
    dbcp2:
      test-on-borrow: true
      validation-query: SELECT 1
  redis:  # 在这里配置redis连接信息
    host: 127.0.0.1
    port: 6379
    database: 3
    password: pwd
    timeout: 2000
    lettuce:
      pool:
        max-idle: 20
        min-idle: 10
        max-active: 50
        max-wait: 1000
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 20MB

mybatis-plus:
  typeAliasesPackage: com.sducat.**.data.po
  mapperLocations: classpath*:mapper/**/*Mapper.xml
  configuration:
    map-underscore-to-camel-case: true
#    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

# token配置
token:
  # 令牌自定义标识
  header: Token
  # 令牌密钥
  secret: a4d619a354b2f1e9397h14c72213qad6
  # 令牌有效期（单位：分钟）
  token_expire_time: 2880
  # refresh_token有效期（单位：天）
  refresh_token_expire_time: 60

# 微信小程序信息
weixin_app:
  app_id: wxhdsf4hsapb5b413d   # 这里填微信小程序app_id
  app_secret: <这里填微信小程序app_secret>

qiniuyun:
  access_key:  # 这里填七牛云access_key, 例mFIT6pUE32k9Ypy9Jcae5ypZVvfglGNrTxJ9ae08
  secret_key:  # 这里填七牛云secret_key, 例COJ-xeV9Q1KLraS8MYa60CMYGCjPgOHBPUWq7g0H
  bucket: # 这里填
  domain: #这里填图片的地址开头,最后要带/, 例https://xx.top/pic/

admin-user: 
  username: # 这里填管理员用户名
  password: # 这里填管理员密码
```

```bash
# 启动，JVM参数可根据服务器配置进行调整
nohup java -jar -Xms180M -Xmx180M -Xmn100M -XX:SurvivorRatio=6 -XX:+UseParNewGC -XX:+UseConcMarkSweepGC sducat.jar >> sducat.log 2>&1 &
```



















