# Render.com Backend Deployment Guide

Render.com provides a generous free tier for hosting web services, perfect for our Spring Boot backend.

## 1. Prerequisites

1.  A GitHub account with your repository pushed.
2.  A free account on [Render.com](https://render.com/).

## 2. Deployment Steps

### Option A: Blueprint Deployment (Recommended)

The easiest way to deploy is using the `render.yaml` file included in the root of the repository.

1.  Log in to the Render Dashboard.
2.  Click **New +** and select **Blueprint**.
3.  Connect your GitHub account and select the repository.
4.  Render will automatically detect the `render.yaml` file.
5.  Click **Apply Blueprint**.
6.  You will be prompted to enter the values for the environment variables marked `sync: false` in the `render.yaml` file:
    *   `SPRING_DATASOURCE_URL`: Your PlanetScale database URL (see PlanetScale guide).
    *   `SPRING_DATASOURCE_USERNAME`: Your PlanetScale username.
    *   `SPRING_DATASOURCE_PASSWORD`: Your PlanetScale password.
    *   `JWT_SECRET`: A long, random string (e.g., generate one using a password manager).
7.  Click **Save and Deploy**.

### Option B: Manual Deployment

If you prefer to deploy manually:

1.  Log in to the Render Dashboard.
2.  Click **New +** and select **Web Service**.
3.  Connect your GitHub account and select the repository.
4.  Configure the service:
    *   **Name:** `learning-platform-backend`
    *   **Environment:** `Docker`
    *   **Region:** Choose the one closest to you (e.g., Ohio).
    *   **Branch:** `main`
    *   **Root Directory:** `backend`
    *   **Instance Type:** Free
5.  Expand **Advanced** and add the following Environment Variables:
    *   `SPRING_PROFILES_ACTIVE`: `prod`
    *   `PORT`: `8080`
    *   `SPRING_DATASOURCE_URL`: (Your PlanetScale URL)
    *   `SPRING_DATASOURCE_USERNAME`: (Your PlanetScale Username)
    *   `SPRING_DATASOURCE_PASSWORD`: (Your PlanetScale Password)
    *   `JWT_SECRET`: (Your secret key)
6.  Set the **Health Check Path** to `/actuator/health` (optional but recommended).
7.  Click **Create Web Service**.

## 3. Important Notes on Free Tier

*   **Auto-Sleep:** Free web services spin down after 15 minutes of inactivity. The first request after a period of inactivity will take 30-60 seconds to process while the service spins back up.
*   **Hours Limit:** You get 750 free instance hours per month, which is enough to run one service 24/7.
*   **Build Time:** The initial Docker build may take 5-10 minutes.

## 4. Get Your Backend URL

Once deployed, Render will provide a URL like `https://learning-platform-backend-xyz1.onrender.com`. You will need this URL to configure the Angular frontend.
