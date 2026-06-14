# GitHub Pages Frontend Deployment Guide

GitHub Pages provides free hosting for static websites, which is perfect for our compiled Angular application.

## 1. Prerequisites

1.  Your repository must be pushed to GitHub.
2.  You need the URL of your deployed Render.com backend (e.g., `https://learning-platform-backend-xyz1.onrender.com`).

## 2. Preparing the Angular Application

Before deploying, we need to ensure the Angular application knows where to find the backend API and is configured correctly for GitHub Pages routing.

### 2.1 Update Environment Variables

Update the `frontend/src/environments/environment.prod.ts` file with your Render.com backend URL.

```typescript
export const environment = {
  production: true,
  apiUrl: 'https://YOUR-RENDER-BACKEND-URL.onrender.com/api/v1' // Replace this!
};
```

### 2.2 Handling Angular Routing on GitHub Pages

GitHub Pages doesn't natively understand Single Page Application (SPA) routing (like Angular's router). If a user navigates directly to `yourdomain.com/courses`, GitHub Pages will look for a `courses/index.html` file and return a 404 error.

To fix this, we use a neat trick: copying `index.html` to `404.html`. This tells GitHub Pages to serve the Angular app for any unknown route, allowing Angular to handle the routing.

This step is handled automatically by the GitHub Actions workflow we have provided in `.github/workflows/deploy-frontend.yml`.

## 3. Configuring GitHub Pages

1.  Go to your repository on GitHub.
2.  Navigate to **Settings** > **Pages**.
3.  Under **Build and deployment**, ensure the **Source** is set to **Deploy from a branch**.
4.  Wait for the GitHub Actions workflow to run (see Section 4).
5.  Once the workflow completes, return to this Settings page.
6.  Under **Branch**, select `gh-pages` and `/ (root)`.
7.  Click **Save**.

## 4. Automated Deployment via GitHub Actions

We have provided a GitHub Actions workflow (`.github/workflows/deploy-frontend.yml`) that automatically builds and deploys your Angular application to the `gh-pages` branch whenever you push to `main`.

### Workflow Steps Explained:

1.  **Checkout:** Grabs your code.
2.  **Setup Node:** Installs Node.js.
3.  **Install Dependencies:** Runs `npm install`.
4.  **Build:** Runs `ng build --configuration production --base-href /YOUR-REPO-NAME/`.
    *   *Note: The `--base-href` is crucial if you are not using a custom domain. It tells Angular that the app is hosted in a subdirectory.*
5.  **Copy index.html:** Copies `index.html` to `404.html` to fix routing.
6.  **Deploy:** Pushes the compiled files to the `gh-pages` branch.

### Required Configuration

You need to edit the `.github/workflows/deploy-frontend.yml` file and update the `BASE_HREF` environment variable to match your repository name.

```yaml
env:
  BASE_HREF: '/your-repository-name/' # E.g., '/enterprise-learning-platform/'
```

*If you are using a custom domain (e.g., `learn.yourcompany.com`), set `BASE_HREF` to `/`.*

## 5. Custom Domain (Optional)

If you have a custom domain:

1.  Go to your DNS provider and create a `CNAME` record pointing to `<your-github-username>.github.io`.
2.  In your GitHub repository **Settings** > **Pages**, enter your custom domain in the "Custom domain" field and click Save.
3.  Check the "Enforce HTTPS" box.
4.  Update the `BASE_HREF` in your workflow to `/`.
