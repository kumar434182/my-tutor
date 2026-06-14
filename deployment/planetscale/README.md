# PlanetScale Database Setup Guide

PlanetScale is a serverless MySQL platform that offers a fantastic free tier (Hobby plan) which includes 5GB of storage and 1 billion row reads per month, perfect for our application.

## 1. Prerequisites

1.  A free account on [PlanetScale](https://planetscale.com/).

## 2. Creating the Database

1.  Log in to your PlanetScale dashboard.
2.  Click **Create a database**.
3.  Name the database `learning_platform`.
4.  Select a region closest to your Render.com backend (e.g., US East / Ohio).
5.  Select the **Hobby** (free) plan.
6.  Click **Create database**.

## 3. Getting Connection Credentials

Once the database is created, you need to generate credentials for the Spring Boot backend.

1.  On the database overview page, click the **Connect** button.
2.  In the "Connect with" dropdown, select **Java (JDBC)**.
3.  PlanetScale will generate a username, password, and JDBC URL.
4.  **Important:** Copy the password immediately. It will only be shown once.

The generated JDBC URL will look something like this:
`jdbc:mysql://aws.connect.psdb.cloud/learning_platform?sslMode=VERIFY_IDENTITY`

## 4. Initializing the Schema

PlanetScale does not support traditional `USE database;` commands in the same way standard MySQL does, and it handles schema changes via branches. However, for the initial setup, you can run the SQL script directly against the `main` branch.

### Option A: Using the PlanetScale Console (Easiest)

1.  Go to the **Console** tab in your PlanetScale dashboard.
2.  Open the `docs/database_complete.sql` file from this repository.
3.  **Important Modification:** Remove the first two lines of the script before pasting:
    ```sql
    -- REMOVE THESE LINES:
    -- CREATE DATABASE IF NOT EXISTS learning_platform;
    -- USE learning_platform;
    ```
4.  Copy the rest of the script and paste it into the PlanetScale console.
5.  Press **Enter** to execute.

### Option B: Using the PlanetScale CLI

1.  Install the [pscale CLI](https://planetscale.com/docs/concepts/planetscale-environment-setup).
2.  Authenticate: `pscale auth login`
3.  Connect to the database: `pscale shell learning_platform main`
4.  Source the modified SQL file.

## 5. Configuring the Backend

Use the credentials obtained in Step 3 to configure your Render.com environment variables or your local `application.yml` for testing.

*   `SPRING_DATASOURCE_URL`: The full JDBC URL from PlanetScale.
*   `SPRING_DATASOURCE_USERNAME`: The generated username.
*   `SPRING_DATASOURCE_PASSWORD`: The generated password.

*Note: PlanetScale requires SSL, which is already included in the JDBC URL they provide (`sslMode=VERIFY_IDENTITY`).*
