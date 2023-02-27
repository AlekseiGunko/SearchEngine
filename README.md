# [SearchEngine](https://github.com/AlekseiGunko/SearchEngine) - поисковой локальный движок
_____
## Для чего предназначен: 
В первую очередь поисковой движок предназначен для словесного поиска по сайтам, указанным в [конфигурационном файле](https://github.com/AlekseiGunko/SearchEngine/blob/master/application.yaml) в корне проекта.
## Описание:
На стартовой странице - **DASHBOARD** отображается статус индексирования сайтов, а так же показывается количетсво уже проиндексированных сайтов, страниц и найденых на них лемм слова.
- *Лемма слова* - начальная, словарная форма слова. В русском языке для существительных и прилагательных это форма именительного падежа единственного числа, для глаголов и глагольных форм - форма инфинитива.
![dashboard](https://github.com/AlekseiGunko/SearchEngine/blob/master/imageForProject/2023-02-27_20-12-15.png)
Движок позволяет производить полную индексацию/переиндексацию всех сайтов указзаных в списке или отдельного сайта из списка на странице **MANAGEMENT**
![indexing](https://github.com/AlekseiGunko/SearchEngine/blob/master/imageForProject/2023-02-27_20-12-35.png)
Для поиска предназначена страница **SEARCH** где в поисковой строке вводим нужное нам для поиска слово и получаем результат в виде списка сайтов и страниц с найденными на них леммам указанных слов:
![search](https://github.com/AlekseiGunko/SearchEngine/blob/master/imageForProject/2023-02-27_20-13-56.png)
____
## Стэк технологий использованных в проекте:
Поисковой движок реализован на языке программирования **Java** и использует следующий стэк технологий:
- Spring boot
- MySQL
- Hibernate
- JSOUP
- [Skillbox - gitlab](https://github.com/skillbox-java/springMorphologyExample)
- ForkJoinPool
- Lombok
 ____
 ## Инструкция по локальному запуску проекта:
 Для начала нужно подключить в проект все нужные ему зависимости, а именно:
 - добавить информацию о фреймворке:
 ``` xml 
 <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.1</version>
        <relativePath/>
 </parent>
```
- подключить нужные зависимости из фрейма:
```
 spring-boot-starter-thymeleaf
 spring-boot-starter-web
 spring-boot-configuration-processor
 spring-boot-starter-data-jpa
 ```
- добавить ссылку на репозиторий для успешного скачивания зависимостей для лемматизатора (получаем лемму слова):
``` xml
    <repositories>
        <repository>
            <id>skillbox-gitlab</id>
            <url>https://gitlab.skillbox.ru/api/v4/projects/263574/packages/maven</url>
        </repository>
    </repositories>
```
- добавить список зависимостей из этого репозитория:
``` xml
        <dependency>
            <groupId>org.apache.lucene.morphology</groupId>
            <artifactId>morph</artifactId>
            <version>1.5</version>
        </dependency>
        <dependency>
            <groupId>org.apache.lucene.analysis</groupId>
            <artifactId>morphology</artifactId>
            <version>1.5</version>
        </dependency>
        <dependency>
            <groupId>org.apache.lucene.morphology</groupId>
            <artifactId>dictionary-reader</artifactId>
            <version>1.5</version>
        </dependency>
        <dependency>
            <groupId>org.apache.lucene.morphology</groupId>
            <artifactId>english</artifactId>
            <version>1.5</version>
        </dependency>
        <dependency>
            <groupId>org.apache.lucene.morphology</groupId>
            <artifactId>russian</artifactId>
            <version>1.5</version>
        </dependency>
 ```
Полную инструкцию по подключению можно посмотреть [здесь](https://github.com/skillbox-java/springMorphologyExample)
- добавить библиотеки MySQL, Hibernate, JSOUP, Lombok:
``` xml
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>

        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>5.6.5.Final</version>
        </dependency>

        <dependency>
            <groupId>org.jsoup</groupId>
            <artifactId>jsoup</artifactId>
            <version>1.15.3</version>
        </dependency>

         <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.24</version>
            <scope>provided</scope>
        </dependency>
 ```
 - настроить файл [application.yaml](https://github.com/AlekseiGunko/SearchEngine/blob/master/application.yaml)
 ``` yaml
 server:
  port: 8080

spring:
  datasource:
    username: root
    password: ваш пароль от БД
    url: jdbc:mysql://localhost:3306/search_engine?useSSL=false&requireSSL=false&allowPublicKeyRetrieval=true
  jpa:mysql://localhost:3306/ИМЯ ВАШЕЙ БД?useSSL=false&requireSSL=false&allowPublicKeyRetrieval=true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
    hibernate:
      ddl-auto: update
    show-sql: true


//добавить ваши сайты в формате ниже
indexing-settings:
  sites:
    - url: url сайта который хотите добавить
      name: имя сайта
```
- добавить нужные вам в [файле](https://github.com/AlekseiGunko/SearchEngine/blob/master/src/main/java/searchengine/parser/PageUrlParser.java) user agent и reffer, либо оставить текущие:
``` java
    String userAgent = "Mozilla/5.0 (X11; Fedora;Linux x86; rv:60.0) Gecko/20100101 Firefox/60.0";
    String referer = "https://www.google.com";
    
        public Document getConnect(String url) {
        Document doc = null;
        try {
            Thread.sleep(150);
            doc = Jsoup.connect(url).userAgent(userAgent).referrer(referer).get();
        } catch (Exception e) {
            log.debug("Не удалось установить подключение с " + url);
        }
        return doc;
    }
```
____
## Заключение:
После прочтения описания и внедрения всех зависимостей можете запускать поисковой движок, проект готов к работе.
 

 
