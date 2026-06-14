# Render.com Deployment Guide (PostgreSQL)

This guide walks you through deploying the Enterprise Learning Platform on Render.com with a free PostgreSQL database.

## 1. Prerequisites

1. A GitHub account with the repository pushed.
2. A free Render.com account at [https://render.com](https://render.com).

## 2. Deploy Using Blueprint (Recommended)

The easiest way to deploy is using the `render.yaml` file included in the root of the repository.

### Step 1: Connect GitHub to Render

1. Log in to your Render dashboard.
2. Click **New +** and select **Blueprint**.
3. Select **Public Git repository**.
4. Paste your GitHub repository URL.
5. Click **Connect**.

### Step 2: Review the Blueprint

Render will automatically detect the `render.yaml` file and show you what will be created:
- **Backend Service:** Spring Boot application running on Docker.
- **PostgreSQL Database:** Free tier database (256 MB storage).

Click **Apply Blueprint**.

### Step 3: Configure Environment Variables

You will be prompted to enter values for environment variables marked `sync: false` in the `render.yaml`:

1. **SPRING_DATASOURCE_URL:** This will be provided by Render after the database is created. It will look like:
   ```
   jdbc:postgresql://your-db-host:5432/learning_platform?sslmode=require
   ```

2. **SPRING_DATASOURCE_USERNAME:** `postgres` (default Render user)

3. **SPRING_DATASOURCE_PASSWORD:** The password you set during database creation (or Render will generate one).

4. **JWT_SECRET:** A long, random string (minimum 32 characters). Generate one using:
   ```bash
   openssl rand -base64 32
   ```

### Step 4: Wait for Deployment

Render will now:
1. Create the PostgreSQL database.
2. Build your Docker image.
3. Deploy the backend service.
4. Initialize the database schema.

This typically takes 5-10 minutes. You can monitor progress in the Render dashboard.

### Step 5: Get Your Backend URL

Once deployed, Render will provide a URL like:
```
https://learning-platform-backend-xyz1.onrender.com
```

**Save this URL** - you will need it to configure the frontend.

## 3. Manual Database Setup (If Needed)

If the database schema is not automatically initialized, you can manually run the SQL script:

1. In the Render dashboard, go to your PostgreSQL database.
2. Click **Connect** and copy the **PSQL Command**.
3. Connect to your database using the PSQL command.
4. Copy the contents of `docs/database_postgres.sql` and paste it into the PSQL terminal.
5. Press Enter to execute.

## 4. Important Notes

- **Auto-Sleep:** Free services sleep after 15 minutes of inactivity. The first request will take 30-60 seconds.
- **Database Limits:** 256 MB storage is sufficient for a learning platform with moderate usage.
- **Logs:** View logs in the Render dashboard under your service's **Logs** tab.

## 5. Troubleshooting

### Database Connection Errors

If you see database connection errors:
1. Verify the `SPRING_DATASOURCE_URL` is correct.
2. Ensure the username and password match your database credentials.
3. Check that the database name is `learning_platform`.

### Build Failures

If the Docker build fails:
1. Check the **Build Logs** in the Render dashboard.
2. Ensure the `Dockerfile` in the `backend/` directory is correct.
3. Verify all dependencies in `pom.xml` are correct.

### Service Not Starting

If the service fails to start:
1. Check the **Logs** tab in the Render dashboard.
2. Verify all environment variables are set correctly.
3. Ensure the health check path `/actuator/health` is accessible.
