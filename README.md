**Backend ERP Systeem**

**Table of contents**  
1. Introduction
2. Project Structure
3. Technologies & Frameworks
4. installation & Setup
5. Testing & Quality
6. Authorization & Users

**Introduction**

This is an Web API for a ERP-system automating business processes.
The goal is to be able to track the costs for maintenance on equipments and manage inventory
with stock control using stock movements,  purchase orders and work orders.

**Project Structure**

&bull; ```src/main/java```: Contains the Java sourcecode (Controllers, Services, Repositories, Models, Dto's, Mappers, Exceptions, Security)  
&bull; ```src/main/resources```: Contains  configuration files like ```application.properties``` and database test data like ```data.sql```  
&bull; ```src/test/java```: Contains unit and integration tests  
&bull; ```pom.xml```: Maven configuration file for dependencies  

**Technologies & Frameworks**

&bull; **Java:** Programming language (JDK 21)   
&bull; **Springboot:** Framework for standalone Web API  
&bull; **Apache Maven:** Dependency management  
&bull; **Spring Data JPA & Hibernate:** Database-independent architecture (MySQL, PostgresSQL, H2, etc.)  
&bull; **Spring Security:** Authentication and authorization  

**Installation & Setup**  
1. **Clone repository:**  
    ```git clone https://github.com/mrgrinsven/backend-eindopdracht-springboot-erp-systeem```
2. **Configure the database**  
    Configure the settings in ```src/main/respurces/application.properties``` like ```spring.datasource.password``` and ```spring.datasource.url```.  
    The ```application.properties``` file is configured for postgreSQL with an environment variable for the password if you want to use it out of the box.  
    Do not forget to set the environment variable for the password and change the ```spring.datasource.url``` if you want to use another port or name:  
    ```spring.datasource.url=jdbc:postgresql://localhost:(PORT here usually 5432)/(DB NAME HERE)```
4. **Build project**  
    run ```mvn clean install``` to build the project or build using your IDE.
5. **Start the application**  
    Run ```mvn spring-boot:run``` to run the application or run by using your IDE.  

**Testing and Quality**    
&bull; **JUnit:** Framework to write unit and integration test  
&bull; **Mockito:** Used to mock (simulate) so that the components can be tested  
&bull; **Spring Test & Spring Boot test:** Allows to test code in the Spring framework  
&bull; **AssertJ & Hamcrest:** Used to write assertions  
&bull; **JsonPath:** Used to validate JSON data  

**Authorization and Users**  
&bull; **ADMIN(username: admin password: admin)**    
    Has access to all rights listed below   
&bull; **MANAGER(username: test_manager password: password123)**     
    Full control. The only role that can create/modify Users, Equipment and Parts  
&bull; **TECHNICIAN(username: test_technician password: password123)**      
    Maintenance focus. Can view Equipment and manage Work Orders and Stock Movements.   
    Cannot modify or create Equipment, Users or Purchase Order only modification of the password is allowed   
&bull; **PURCHASER(username: test_technician password: password123)**    
    Inventory focus. Can view Parts and manage Purchase Orders.   
    Cannot modify or see Equipment and Work Orders. Cannot modify Users only modification of the password is allowed
