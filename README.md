# grant-application-java
Java Spring Boot project for Grant Application app





# mapping DB to Oracle/PostgreSQL

1. Remove H2 (or keep only for dev)

<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
  <scope>runtime</scope>
</dependency>

Add below dependnecy for mapping to Oracle

   <dependency>
     <groupId>com.oracle.database.jdbc</groupId>
     <artifactId>ojdbc11</artifactId>
     <scope>runtime</scope>
   </dependency>

Add below dependnecy for mapping to PostgreSQL

   <dependency>
     <groupId>org.postgresql</groupId>
     <artifactId>postgresql</artifactId>
     <scope>runtime</scope>
   </dependency>

2. update application.yml / profile config

for PostgreSQL

        spring:
          datasource:
            url: jdbc:postgresql://localhost:5432/grant_db
            username: grant_user
            password: grant_pwd
            driver-class-name: org.postgresql.Driver
        
        jpa:
          database-platform: org.hibernate.dialect.PostgreSQLDialect
          hibernate:
            ddl-auto: none
          show-sql: true
for Oracle:

       spring:
         datasource:
           url: jdbc:oracle:thin:@//localhost:1521/ORCLPDB1
           username: GRANT_USER
           password: GRANT_PWD
           driver-class-name: oracle.jdbc.OracleDriver
       
       jpa:
         database-platform: org.hibernate.dialect.OracleDialect
         hibernate:
           ddl-auto: none
         show-sql: true
3. remove below h2 config from application.yaml

   spring:
   h2:
   console:
   enabled: true


