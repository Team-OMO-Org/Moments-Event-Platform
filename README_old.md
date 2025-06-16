# Moments-Event-Platform
Moments Event Platform, DCI final project

Test
# Moments-Event-Platform
Moments Event Platform, DCI final project

## Table of Contents
- [Vision](#vision)
- [Project Summary](#project-summary)
- [System Design](#system-design)
- [Technologies](#technologies)
- [Installation](#installation)
- [Team](#team)
- [Workflow](#workflow)
- [Challenges](#hhallenges)

## Vision

Moments is a microservices-based platform enabling users to discover, filter, and purchase tickets for various types of events. Users can act as customers or hosts, while admins maintain system integrity. The platform supports real-time ticket availability, shopping cart functionality, and payments.

## Project Summary

This project is a scalable, open-source event management platform designed to streamline event creation, user registration, ticket purchasing, and event discovery. It leverages modern web technologies and frameworks to deliver a seamless customer experience.

1. **User Flow**

    
    As a 
    Discover events based on location, category, or date.
    Register for events 
    View event details, including descriptions, pricing, and purchase tickets.
    Receive notifications about successful purchases.

    As a Event Organizer:  

    Create, update, and delete events.
    Create and manage event details.
    Track ticket sales and attendee information.


## System Design -> Michael

- **REST**
    - **Swagger**

## Technologies

**Backend Core:**  
![Microservices](https://img.shields.io/badge/Microservices-1572B6?style=for-the-badge&logo=cloudflare&logoColor=white)
![REST API](https://img.shields.io/badge/REST_API-FF5733?style=for-the-badge&logo=api&logoColor=white)

![Java 21](https://img.shields.io/badge/Java_21-007396?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.4.4-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Web](https://img.shields.io/badge/Spring_Web-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![RestClient](https://img.shields.io/badge/Spring_RestClient-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-BC4521?style=for-the-badge&logo=lombok&logoColor=white)
![MapStruct](https://img.shields.io/badge/MapStruct-1.5.5-FF9E0F?style=for-the-badge&logo=java&logoColor=white)

**Security:**  
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)
![Keycloak](https://img.shields.io/badge/Keycloak_OIDC/OAuth2-4A4A55?style=for-the-badge&logo=keycloak&logoColor=white)

**Spring Cloud:**  
![Eureka](https://img.shields.io/badge/Eureka_Discovery-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Gateway MVC](https://img.shields.io/badge/Spring_Cloud_Gateway_MVC-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Resilience4j](https://img.shields.io/badge/Resilience4j-3F2C70?style=for-the-badge&logo=spring&logoColor=white)

**Kafka Ecosystem for notification event:**  
![Kafka](https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white)
![Zookeeper](https://img.shields.io/badge/Zookeeper-D22128?style=for-the-badge&logo=apache&logoColor=white)
![Schema Registry](https://img.shields.io/badge/Schema_Registry-24282A?style=for-the-badge&logo=apache-kafka&logoColor=white)
![Avro](https://img.shields.io/badge/Apache_Avro-FF9900?style=for-the-badge&logo=apache&logoColor=white)

**Email sent to customer:**  
![Spring Mail](https://img.shields.io/badge/Spring_Boot_Mail-6DB33F?style=for-the-badge&logo=spring&logoColor=white)

**Database and Versioning:**  
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway_Migrations-CC0200?style=for-the-badge&logo=flyway&logoColor=white)

**Frontend:**  
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![Bootstrap 4](https://img.shields.io/badge/Bootstrap_4-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)

**Documentation:**  
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![OpenAPI](https://img.shields.io/badge/OpenAPI_3-6BA539?style=for-the-badge&logo=openapi-initiative&logoColor=white)

**DevOps/Build:**  
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Docker Compose](https://img.shields.io/badge/Docker_Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)

**Testing:**  
![JUnit5](https://img.shields.io/badge/JUnit_5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Mockito](https://img.shields.io/badge/Mockito-78A641?style=for-the-badge&logo=java&logoColor=white)

## Installation -> Michael
	Requirements
		Reset Confidential Secret and set in Moments Web App
	Normal Java Installation and/or Docker
	Group All Environment Variables -> all application.properties

## Team -> Olena
	Team OMO at Digital Career Institute
	Olga Michael Olena -> Full Name, Link to personal Git Hub 

## Workflow -> Olena
	Integration
	Git Hub
	Jira
		Automation/Rules
	Slack
	Google Doc/Slides/meet -> Link to Presentation maybe earlier already

## Challenges -> Michael
	Everything! 5 Jobs at the same time
System Architecture
Business Domain
Front-End User Interface
Project Management
Time Constraints -> ~ 18 work days only

## Future Outlook -> Michael
Open Source, do whatever you want
Stripe Payment
(Observation)
Reviews/Ratings
Admin Panel
AI Customer Service/Chatbot
Favorite Items
Presents, Coupons, Discounts



## API Documentation

All endpoints listed below are rerouted through a secured API Gateway, which ensures centralized
authentication, authorization, and request routing.

## Moment Service
**Base URL**: `/api/v1/moments`

### Moment Endpoints
- **GET** `/`: Retrieve all moments with optional filters.
- **GET** `/host/{id}`: Retrieve all moments by host ID.
- **POST** `/`: Create a new moment.
- **PUT** `/{id}`: Update a moment by its ID.
- **DELETE** `/{id}`: Delete a moment by its ID.
- **GET** `/{id}`: Retrieve a moment by its ID.
- **GET** `/{id}/check-availability`: Check ticket availability for a specific moment.
- **POST** `/{id}/book-tickets`: Book tickets for a specific moment.
- **POST** `/{id}/cancel-tickets`: Cancel ticket booking for a specific moment.
- **GET** `/categories`: Retrieve all categories by moments count.
- **GET** `/categories/{id}`: Retrieve a category by its ID.
- **GET** `/cities`: Retrieve all cities by moments count.
- **POST** `/cart-items`: Retrieve cart items by moment IDs.
