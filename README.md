# Added-Value-swen610-F4-09272020
Software Engineering Course Project, SWEN-610

### Getting Started

1. The project file can be opened up with IntelliJ IDEA. 
2. Once project is opened with IntelliJ IDEA, click on `pom.xml` and refresh Maven sources. 
3. If you are getting any errors about `Java.ClassNotFoundException` about MySQL. This can be missing MySQL connector not being imported. Import `mysql-connector-java-8.0.21` through Module settings. 
4. Once everything is running, access MyPLS through http://localhost:8080/register
5. To set up the database locally, add the Data Source MySQL and add `jdbc:mysql://b8bfeaec94d3d2:b7e7427e@18.217.126.50:3306/mypls` as the URL. Test the connection, you should now be connected.
6. We are using JDBC Connector v[8.0.2](https://dev.mysql.com/downloads/connector/j/)

### Some test users are given below:
1. learner:
email: jw@rit.edu
password: 123456
email: js@rit.edu
password: 123456

2. professor:
email: tf@rit.edu
password: 123456
email: ps@rit.edu
password: 123456

3. admin:
email: mc1927@rit.edu
password: 123456
