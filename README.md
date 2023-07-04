# BASIC CRUD API

This is a basic CRUD REST API being made with Java 17, Spring Boot and in-memory database H2 for learning purposes. The front-end is a basic vanilla JavaScript page, made with the help of ChatGPT.

### What's working:

* [GET] Get a list of entities: http://localhost:8080/api/v1/things
* [GET] Get the entity with ID: http://localhost:8080/api/v1/things/findById/{id}
* [GET] Get a list of entities that contains the passed description (it is case sensitive): http://localhost:8080/api/v1/things/findAllWithDescription/{description}
* [POST] Save an entity (JSON format with 'description' and 'imageUrl' fields): http://localhost:8080/api/v1/things/save
* [POST] Save entities stored in an Excel sheet (must inform a parameter 'file'): http://localhost:8080/api/v1/things/saveFromExcel
  - Sheet model: column A is the description and column B is the imageUrl, it starts to read from the 2nd row
  
    ![image](https://github.com/mychellmotta/basic-crud-api/assets/13575346/9d6653e1-1a7f-4378-bdc3-75ec575f8415)
  
* [PUT] Update an entity passing the ID as a parameter (JSON format with 'description' and 'imageUrl' fields): http://localhost:8080/api/v1/things/update/{id}
* [DELETE] Delete an entity by its ID (type: UUID): http://localhost:8080/api/v1/things/delete/{id}
* Send email on transactional actions (save, update, delete)
  - Configure the email in application.properties and set the EMAIL_TO and EMAIL_FROM variables in EmailService

### To do:

* Store the imageUrl in Amazon S3
