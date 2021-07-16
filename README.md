# Banking API Back-end

A banking API allows you to register-login user; create employee, customer, account entity; transfer money between two accounts.

#### Technologies
- Spring Boot (JPA, JDBC, Web, devtools, validation)
- Spring Framework
- PostgreSQL
- Maven
- Dropwizard
- Lombok

##### Application starts on localhost port 8080
- [http://localhost:8080/api/users]
- [http://localhost:8080/api/customers]
- [http://localhost:8080/api/employees]
- [http://localhost:8080/api/accounts]
- [http://localhost:8080/api/transfers] 

#### Available Services
| Http Method | Path | Usage |
| ------ | ------ | ------ |
| GET | /api/users | get all users |
| GET | /api//users/{id} | get user by id |
| POST | /api/users/register | register to user |
| POST | /api/users/login | login to user |
| PUT | /api/users/{id}} | update to user |
| DELETE | /api/users/{id}} | delete to user |
| GET | /api/customers | get all customers |
| GET | /api/customers/{id} | get customer by id |
| GET | /api/customers/accounts | get all customer accounts |
| POST | /api/customers | create a customer |
| PUT | /api/customers/{id}} | update to customer |
| DELETE | /api/customers/{id}} | delete to customer |
| GET | /api/employees | get all employees |
| GET | /api/employees/{id} | get employee by id |
| POST | /api/employees | create a employee |
| PUT | /api/employees/{id}} | update to employee |
| DELETE | /api/employees/{id}} | delete to employee |
| GET | /api/accounts | get all accounts of user (employee or customer) |
| GET | /api/accounts/{id} | get account by id |
| POST | /api/accounts | create a account |
| PUT | /api/accounts/{id}} | update to account |
| DELETE | /api/accounts/{id}} | delete to account |
| GET | /api/transfers | get all transfers of user (employee or customer) |
| GET | /api/transfers/{id} | get transfer by id |
| POST | /api/transfers | create a money transfer |

### User Resources

##### Request
#
```sh
POST /api/users/register
```
##### Body
#
```json
{   
    "firstName": "admin",
    "lastName": "admin",
    "email": "admin@testmail.com",
    "password": "password",
    "createdBy": "admin"
}
```
##### Response
#
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MjY0MDE2MjYsImV4cCI6MTYyNjQwODgyNiwiaWQiOjg1MSwiZW1haWwiOiJhZG1pbkB0ZXN0bWFpbC5jb20iLCJmaXJzdE5hbWUiOiJhZG1pbiIsImxhc3ROYW1lIjoiYWRtaW4ifQ.tiRYc-asMVzcDhSFDQmTWAB1pYWuammWrxccAcyJ6so"
}
```
##### Request
#
```sh
POST /api/users/login
```
##### Body
#
```json
{   
    "email": "admin@testmail.com",
    "password": "password"
}
```
##### Response
#
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MjY0MDE5MDIsImV4cCI6MTYyNjQwOTEwMiwiaWQiOjg1MSwiZW1haWwiOiJhZG1pbkB0ZXN0bWFpbC5jb20iLCJmaXJzdE5hbWUiOiJhZG1pbiIsImxhc3ROYW1lIjoiYWRtaW4ifQ.-1dAXuMD1kGS3pE7eO-NO5UrFaFL0D1o3ma8Y2imVNg"
}
```
##### Request
#
```sh
GET /api/users
```
##### Response
#
```json
[
    {
        "id": 851,
        "firstname": "admin",
        "lastname": "admin",
        "email": "admin@testmail.com",
        "password": "$2a$10$RYuTjuMIGAnfSZGqpVbdGexBnQOjf9hxxIdOcoPdyls8AFm2n82YC",
        "createdBy": "admin",
        "createdDate": "2021-07-16T02:13:46.206+00:00",
        "lastModifiedBy": "admin",
        "lastModifiedDate": "2021-07-16T02:13:46.206+00:00"
    }
]
```
##### Request
#
```sh
GET /api/users/851
```
##### Response
#
```json
{
    "id": 851,
    "firstname": "admin",
    "lastname": "admin",
    "email": "admin@testmail.com",
    "password": "$2a$10$RYuTjuMIGAnfSZGqpVbdGexBnQOjf9hxxIdOcoPdyls8AFm2n82YC",
    "createdBy": "admin",
    "createdDate": "2021-07-16T02:13:46.206+00:00",
    "lastModifiedBy": "admin",
    "lastModifiedDate": "2021-07-16T02:13:46.206+00:00"
}
```
##### Request
#
```sh
PUT /api/users/851
```
##### Body
#
```json
{   
    "firstname": "admin",
    "lastname": "sys",
    "email": "admin@mail.com",
    "password": "password1",
    "lastModifiedBy": "system"
}
```
##### Response
#
```json
{
    "success": true
}
```
##### Request
#
```sh
DELETE /api/users/851
```
##### Response
#
```json
{
    "success": true
}
```

