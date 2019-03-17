# HaloOglasi Crawler

This crawler is created for easier listing of new advertisements on www.halooglasi.com. 
When run, it starts Google Chrome web browser and executes default appartment search if custom query is not provided.
Result advertisements are traversed and stored to database if already not found in database, after execution email is sent to "RECIPIENT_EMAIL" address with new advertisements. It is designed to be run in periodically eg. few times per day and purpose is to get latest adds from the website before other 
users ;)

### Technologies used

Appartment crawler uses a following open source projects in background:

* [Selenium Web Driver](https://github.com/SeleniumHQ/selenium) - UI Testing framework!
* [Webdriver Manager](https://github.com/bonigarcia/webdrivermanager) - Management for Selenium WebDriver
* [MongoDB](https://github.com/mongodb/mongo) - NoSQL Database
* [Javax Mail](https://github.com/eclipse-ee4j/javamail) - Email client for Java apps
* [Apache Maven](https://github.com/apache/maven) - Java building tool.

### How can I use this application: 
  - Clone repository
```sh
$ git clone git@github.com:milanbojovic/halooglasi_crawler.git
```
  - Build executable JAR file 
```sh
$ cd halooglasi_crawler
$ mvn assembly:assembly
```
  - Register for email account on SMTP2GO Website and create user/pass
  - Create two system environment variables "SMTP_USER", "SMTP_PASS"
```sh
$ export SMTP_USER=youruser
$ export SMTP_PASS=yourpass
```
  - Install MongoDb on default port - App will use default _"local"_ database and create and use collection _"advertisements"_ for internal purposes.

  - DEFAULT EXECUTION: 
```sh 
$ java -jar appartment-crawler-1.0-SNAPSHOT-jar-with-dependencies.jar
```
  - CUSTOM  EXECUTION: 
```sh
  $ java -jar appartment-crawler-1.0-SNAPSHOT-jar-with-dependencies.jar 
"https://www.halooglasi.com/nekretnine/izdavanje-stanova?grad_id_l-lokacija_id_l-mikrolokacija_id_l=40574%2C40787&cena_d_to=400&cena_d_unit=4&broj_soba_order_i_from=3&oglasivac_nekretnine_id_l=387238&namestenost_id_l=562&sa_fotografijom=true"
```

   - Executing via CRONTAB
```sh
$ crontab -e
```
Paste following text into your crontab (it will schedule two executions with different queries every hour):
```sh
*/60 * * * * /path_to_file/.bash_profile; java -jar /absolute_path_to_file/appartment-crawler-1.0-SNAPSHOT-jar-with-dependencies.jar > /tmp/appartment-crawler.log
*/60 * * * * /path_to_file/.bash_profile; java -jar /absolute_path_to_file/appartment-crawler-1.0-SNAPSHOT-jar-with-dependencies.jar 
"https://www.halooglasi.com/nekretnine/izdavanje-stanova\?grad_id_l-lokacija_id_l-mikrolokacija_id_l=40574\%2C40787\&cena_d_to=400\&cena_d_unit=4\&broj_soba_order_i_from=3\&oglasivac_nekretnine_id_l=387238\&namestenost_id_l=562\&sa_fotografijom=true"  > /tmp/appartment-crawler.log
```
Please note that in example above environment variables are writen to bash_profile and added to cron, also query string is escaped for bash script and % is also escaped because of crontab problems. 
**Have fun !**

License
----

MIT

**Free Software, Hell Yeah!**

