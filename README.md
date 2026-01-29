# 📂 File Management & Storage API

A **Spring Boot backend project** that provides a file storage and management system (similar to Google Drive).
This system supports uploading, downloading, organizing files/folders, user authentication, sharing files, and admin management.

---

## ⚡ Features Implemented

### ✅ User Authentication & Authorization

* User registration & login
* JWT-based authentication
* Role-based access control (`USER`, `ADMIN`)

### ✅ Folder Management

* Create, update, delete folders
* Nested subfolders support
* Soft-delete (Trash) and restore

### ✅ File Management

* Upload files into folders
* Download files as attachments
* Soft-delete and restore files
* Permanent deletion of files

### ✅ File Sharing

* Share files with other users
* View list of users a file is shared with
* Revoke sharing access

### ✅ Search & Metadata

* Search files by name
* Get file metadata (name, size, type, owner, etc.)

### ✅ Admin Module

* List all users
* Delete users
* View all files in the system

---

## 🔮 Upcoming Features

🚀 **Cloud Integration (AWS S3)**

* Store files in Amazon S3
* Pre-signed URLs for secure uploads/downloads
* File versioning

🔒 **Enhanced Security**

* JWT refresh tokens
* Fine-grained permissions for shared files

📊 **User Dashboard APIs**

* Storage usage per user
* File statistics

📧 **Email Notifications**

* File sharing alerts
* Storage limit notifications

---

## 🛠️ Tech Stack

* **Java 17**
* **Spring Boot 3**
* **Spring Security (JWT)**
* **Spring Data JPA (Hibernate)**
* **PostgreSQL**
* **ModelMapper**
* **Docker**
* **Render (Deployment)**
* **AWS SDK** (Upcoming)

---

# 🧑‍💻 Local Development Setup

## 1️⃣ Clone the Repository

```bash
git clone https://github.com/your-username/file-management-storage.git
cd file-management-storage
```

---

## 2️⃣ Database Configuration (Local PostgreSQL)

An example config file is provided:

```bash
cp src/main/resources/application.example.properties \
   src/main/resources/application.properties
```

Update it with your **local DB credentials**:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/file_db
spring.datasource.username=postgres
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8080
```

> 🔐 **Security Note**
>
> * `application.properties` must be **gitignored**
> * Never commit real credentials
> * `application.example.properties` is only a template

---

## 3️⃣ Run Locally

```bash
mvn spring-boot:run
```

API Base URL:

```
http://localhost:8080/api
```

---

# 🐳 Docker Deployment

## 4️⃣ Build JAR File

```bash
mvn clean package
```

JAR will be generated at:

```
target/file-management-storage-0.0.1-SNAPSHOT.jar
```

---

## 5️⃣ Dockerfile

Create a `Dockerfile` in the project root:

```dockerfile
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 6️⃣ Build Docker Image

```bash
docker build -t your-dockerhub-username/file-management-api:latest .
```

Verify:

```bash
docker images
```

---

## 7️⃣ Push Image to Docker Hub

Login:

```bash
docker login
```

Push image:

```bash
docker push your-dockerhub-username/file-management-api:latest
```

---

# ☁️ Render Deployment (Production)

## 8️⃣ Create PostgreSQL on Render

1. Go to **Render Dashboard**
2. Create → **PostgreSQL**
3. Copy:

   * **External Database URL**
   * Username
   * Password

⚠️ Render databases sleep on free tier. Cold start delays are normal.

---

## 9️⃣ Deploy Docker Image on Render

1. Create → **Web Service**
2. Choose **Docker**
3. Image:

   ```
   your-dockerhub-username/file-management-api:latest
   ```
4. Port:

   ```
   8080
   ```

---

## 🔐 Environment Variables (Render)

Add these in **Render → Environment Variables**:

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://<external-host>:5432/<db-name>
SPRING_DATASOURCE_USERNAME=<db-username>
SPRING_DATASOURCE_PASSWORD=<db-password>

SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=false

JWT_SECRET=your_secret_key
JWT_EXPIRATION_MS=86400000
```

> ✅ **Important**
>
> * Use **External DB URL** on Render
> * Never hardcode DB credentials in code
> * Do NOT commit production secrets

---

## 10️⃣ Access Live Application

Once deployed:

```
https://your-render-service.onrender.com/api
```

Example:

```
POST /auth/login
POST /files/upload
GET  /files/{id}/download
```

---

## 🚨 Common Production Logs (Expected)

```text
Unauthorized error: Full authentication is required to access this resource
```

✅ This is **normal behavior** when accessing protected APIs without JWT.

---

## 👨‍💻 Author

Built with ❤️ by **Panda**
Backend Developer | Spring Boot | PostgreSQL | Docker | Cloud
