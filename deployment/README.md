# 100% Free Tier Deployment Guide

This directory contains everything you need to deploy the Enterprise Learning Platform completely for free, forever (or until the providers change their tiers).

## 🏗️ Architecture

We are utilizing a modern, decoupled architecture across three generous free-tier providers:

1.  **Frontend (Angular):** Hosted on **GitHub Pages** (Free, unlimited bandwidth for static sites).
2.  **Backend (Spring Boot):** Hosted on **Render.com** (Free tier, auto-sleeps after 15 mins).
3.  **Database (MySQL):** Hosted on **PlanetScale** (Free 5GB storage, serverless).

## 🚀 Step-by-Step Deployment Order

To successfully deploy the platform, you must follow these steps in this exact order:

### Step 1: Push to GitHub
1. Create a new public repository on GitHub.
2. Push all the code (the entire `learning-platform-implementation` folder contents) to the `main` branch.

### Step 2: Set up the Database (PlanetScale)
1. Read `planetscale/README.md`.
2. Create your free database.
3. Run the SQL initialization script.
4. Save your connection credentials (URL, username, password).

### Step 3: Deploy the Backend (Render.com)
1. Read `render/README.md`.
2. Connect Render to your GitHub repository.
3. Deploy using the included `render.yaml` Blueprint.
4. Input the PlanetScale credentials when prompted.
5. Wait for the deployment to finish and copy your new backend URL (e.g., `https://my-backend.onrender.com`).

### Step 4: Deploy the Frontend (GitHub Pages)
1. Read `github-pages/README.md`.
2. Edit `frontend/src/environments/environment.prod.ts` and paste your Render backend URL.
3. Edit `.github/workflows/deploy-frontend.yml` and update the `BASE_HREF` to match your repository name.
4. Commit and push these two changes to GitHub.
5. GitHub Actions will automatically build and deploy your frontend!
6. Go to your repository **Settings** -> **Pages** to find your live website URL.

## ⚠️ Important Considerations

*   **Cold Starts:** Because the backend is on Render's free tier, it will go to sleep after 15 minutes of inactivity. When you (or a user) accesses the site after it has been sleeping, the very first API request (usually the login page) will take **30 to 60 seconds** to respond. This is normal. Subsequent requests will be fast.
*   **Database Limits:** PlanetScale's free tier allows 1 billion row reads per month. For a learning platform, this is virtually impossible to exceed unless you have thousands of active daily users.
*   **Public Repository:** GitHub Pages requires your repository to be public if you are using a free GitHub account. Ensure you do not hardcode any passwords in your source code (always use environment variables as configured).
