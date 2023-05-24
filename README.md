# BASIC CRUD API

This is a basic CRUD REST API being made with Java 17, Spring Boot and in-memory database H2 for learning purposes.

### What's working:

* [GET] Get a list of entities: http://localhost:8080/thing
* [GET] Get a list of entities that contains the passed description: http://localhost:8080/thing/{description}
* [POST] Save an entity (JSON format with 'description' and 'imageUrl' fields): http://localhost:8080/thing/save
* [POST] Save entities stored in an Excel sheet (must inform a parameter 'file'): http://localhost:8080/thing/saveFromExcel
  - Sheet model: column A is the description and column B is the imageUrl, it starts to read from the 2nd row
  
    ![image](https://github.com/mychellmotta/basic-crud-api/assets/13575346/9d6653e1-1a7f-4378-bdc3-75ec575f8415)
  
* [DELETE] Delete an entity by its ID (type: UUID): http://localhost:8080/delete/{id}

### To do:

* Update method
* Send email on transactional actions such as saving and deleting
* Store the imageUrl in Amazon S3
