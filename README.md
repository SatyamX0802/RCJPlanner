# RCJ Planner — Resume, Course & Job Planner

A full-stack web application built with **Spring Boot** that helps users manage their resume, track enrolled courses, and apply for jobs — all from a single platform. Recruiters get a dedicated dashboard to post jobs and review applications.

---

## Live Demo

> Deployed on **Railway** — 

---

## Features

### For Users (Job Seekers)
- **Resume Builder** — Create and manage a structured resume with sections for education, experience, projects, skills, and certifications
- **Course Tracker** — Browse available courses and track enrollment progress
- **Job Board** — View all posted jobs and apply with a single click
- **Application Dashboard** — Track the status of submitted job applications

### For Recruiters
- **Job Posting** — Post and manage job listings via a dedicated recruiter dashboard
- **Application Review** — View and manage all applications received for posted jobs

### Auth & Security
- **JWT-based Authentication** — Stateless, token-secured login and session management
- **Role-based Access Control** — Separate access levels for `USER` and `RECRUITER` roles
- **Secure Routes** — Protected endpoints enforced via Spring Security filter chain

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3 |
| Security | Spring Security, JWT |
| ORM | Spring Data JPA, Hibernate |
| Database | MySQL |
| Frontend | Thymeleaf, HTML/CSS |
| Build Tool | Maven |
| Deployment | Railway |

---

## Project Structure

```
src/
└── main/
    ├── java/com/planner/RCJPlanner/
    │   ├── config/
    │   │   └── DataSeeder.java           # Seeds initial data on startup
    │   ├── controller/
    │   │   ├── AuthController.java       # Login, register endpoints
    │   │   ├── CourseController.java     # Course listing and enrollment
    │   │   ├── JobController.java        # Job browsing and applications
    │   │   ├── RecruiterController.java  # Recruiter dashboard and job management
    │   │   └── ResumeController.java     # Resume creation and viewing
    │   ├── entities/
    │   │   ├── AppUser.java
    │   │   ├── Certificate.java
    │   │   ├── Course.java
    │   │   ├── Education.java
    │   │   ├── Experience.java
    │   │   ├── Job.java
    │   │   ├── Project.java
    │   │   ├── Resume.java
    │   │   ├── Skill.java
    │   │   ├── UserCourse.java           # Join entity: user ↔ course enrollment
    │   │   └── UserJob.java              # Join entity: user ↔ job application
    │   ├── exception/
    │   │   └── GlobalExceptionHandler.java  # Centralized error handling
    │   ├── repository/                   # Spring Data JPA repositories
    │   ├── security/
    │   │   ├── AppUserDetails.java
    │   │   ├── AppUserDetailsService.java
    │   │   ├── LoginSuccessHandler.java  # Role-based redirect on login
    │   │   └── SecurityConfig.java       # JWT filter chain and route protection
    │   └── RcjPlannerApplication.java    # Application entry point
    └── resources/
        ├── templates/
        │   ├── courses/                  # Course list view
        │   ├── error/                    # 404 and 500 error pages
        │   ├── jobs/                     # All jobs + my applications views
        │   ├── recruiter/                # Recruiter dashboard, post form, applications
        │   ├── resume/                   # Resume form and view
        │   ├── dashboard.html
        │   ├── layout.html               # Shared base layout
        │   ├── login.html
        │   └── register.html
        └── application.properties
```

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8+

### Local Setup

**1. Clone the repository**
```bash
git clone https://github.com/SatyamX0802/<repo-name>.git
cd <repo-name>
```

**2. Configure the database**

Create a MySQL database:
```sql
CREATE DATABASE rcj_planner;
```

Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/rcj_planner
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.jpa.hibernate.ddl-auto=update
```

**3. Add your JWT secret**
```properties
jwt.secret=your_secret_key
jwt.expiration=86400000
```

**4. Run the application**
```bash
./mvnw spring:boot:run
```

The application will start at `http://localhost:8080`

---

## API Overview

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/auth/register` | Public | Register a new user |
| POST | `/auth/login` | Public | Authenticate and receive JWT |
| GET | `/resume/form` | USER | View resume builder form |
| POST | `/resume/save` | USER | Save resume data |
| GET | `/courses/list` | USER | Browse all courses |
| GET | `/jobs/all` | USER | View all job listings |
| POST | `/jobs/apply/{id}` | USER | Apply for a job |
| GET | `/recruiter/dashboard` | RECRUITER | View recruiter dashboard |
| POST | `/recruiter/post` | RECRUITER | Post a new job |
| GET | `/recruiter/applications` | RECRUITER | View all received applications |

---

## Deployment (Railway)

This project is deployed on [Railway](https://railway.app).

**Environment variables configured on Railway:**

| Variable | Description |
|---|---|
| `SPRING_DATASOURCE_URL` | Railway MySQL connection URL |
| `SPRING_DATASOURCE_USERNAME` | DB username |
| `SPRING_DATASOURCE_PASSWORD` | DB password |
| `JWT_SECRET` | Secret key for token signing |

Railway auto-detects the Maven project and builds using the included `mvnw` wrapper.

---

## Database Schema (Key Entities)

```
AppUser ──< UserCourse >── Course
AppUser ──< UserJob    >── Job
AppUser ──  Resume
Resume  ──< Education
Resume  ──< Experience
Resume  ──< Project
Resume  ──< Skill
Resume  ──< Certificate
```

---

## Author

**Satyam Sharma**
[GitHub](https://github.com/SatyamX0802) · [LinkedIn](https://www.linkedin.com/in/satyam-a36681227/) · [sharmasatyam.0165@gmail.com](mailto:sharmasatyam.0165@gmail.com)
