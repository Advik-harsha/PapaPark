# 🌐 Live Cloud Deployment & Public Tunnel Guide for PapaPark

This guide provides step-by-step instructions to make **PapaPark - Smart Parking System** live on the internet!

---

## ⚡ Option 1: Instant Public Access via Localtunnel / Ngrok (Zero Setup, 30 Seconds)

If your local server is currently running on `http://localhost:8080` and you want to share a live URL with anyone immediately:

### Method A: Localtunnel (Free, No Signup Required)
Run this command in PowerShell or Command Prompt:

```powershell
npx localtunnel --port 8080
```

You will get a public live URL like:
`https://smartpark-papa.loca.lt`

---

### Method B: Ngrok (Secure SSL Tunnel)
1. Download [Ngrok](https://ngrok.com/download)
2. Run:
```bash
ngrok http 8080
```
3. Copy the generated `https://xxxx.ngrok-free.app` URL and share it!

---

## ☁️ Option 2: Permanent Free Cloud Deployment (MongoDB Atlas + Render.com)

To host your app permanently 24/7 online for free directly from your GitHub repository ([https://github.com/Advik-harsha/PapaPark](https://github.com/Advik-harsha/PapaPark)):

### Step 1: Create a Free MongoDB Cloud Database (MongoDB Atlas)
1. Go to [MongoDB Atlas](https://www.mongodb.com/cloud/atlas/register) and create a free account.
2. Click **Build a Database** ➔ Choose **M0 Free Tier**.
3. Under **Database Access**, create a database user (e.g., `admin` / `password123`).
4. Under **Network Access**, click **Add IP Address** ➔ Choose **Allow Access from Anywhere (`0.0.0.0/0`)**.
5. Click **Connect** ➔ **Drivers** ➔ Copy your MongoDB Connection String:
   `mongodb+srv://admin:<password>@cluster0.mongodb.net/smart_parking_db?retryWrites=true&w=majority`

---

### Step 2: Deploy Spring Boot on Render.com (Free Tier)
1. Sign up at [Render.com](https://render.com) using your GitHub account.
2. Click **New +** ➔ Select **Web Service**.
3. Connect your repository: `Advik-harsha/PapaPark`.
4. Configure service settings:
   - **Name**: `papapark-smart-parking`
   - **Environment**: `Java` or `Docker`
   - **Build Command**: `./mvnw clean package -DskipTests`
   - **Start Command**: `java -jar target/smart-parking-system-0.0.1-SNAPSHOT.jar`
5. Under **Environment Variables**, add:
   - Key: `SPRING_DATA_MONGODB_URI`
   - Value: `mongodb+srv://admin:password123@cluster0.mongodb.net/smart_parking_db?retryWrites=true&w=majority`
   - Key: `JAVA_VERSION`
   - Value: `17`
6. Click **Create Web Service**.

Render will automatically build and launch your application live at a URL like:
`https://papapark-smart-parking.onrender.com`

---

## 🐳 Option 3: Docker Deployment

PapaPark can also be packaged as a Docker container. Create a `Dockerfile` in the root folder:

```dockerfile
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/smart-parking-system-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080
```

Build and run with Docker:
```bash
docker build -t papapark .
docker run -p 8080:8080 -e SPRING_DATA_MONGODB_URI="mongodb://host.docker.internal:27017/smart_parking_db" papapark
```

---

## ✅ Post-Deployment Verification
Once deployed live:
1. Visit your live web URL (e.g. `https://papapark-smart-parking.onrender.com`).
2. Log in with pre-seeded Admin credentials (`admin@smartpark.com` / `admin123`).
3. Verify live 3D parking slot map, QR gate pass generation, and Razorpay test mode payment.
