# Google Cloud Platform (GCP) Free Tier Setup Guide

## 1. Introduction

Google Cloud Platform offers a robust Free Tier that provides enough resources to run the Enterprise Learning Platform at no cost for development, testing, and small-scale production use. This guide walks you through setting up your GCP account and configuring it to maximize the free tier benefits.

## 2. GCP Free Tier Limits

Before deploying, it is crucial to understand what is included in the "Always Free" tier to avoid unexpected charges:

*   **Compute Engine (e2-micro):** 1 non-preemptible e2-micro VM instance per month (us-west1, us-central1, us-east1).
*   **Cloud Run:** 2 million requests per month, 360,000 GB-seconds memory, 180,000 vCPU-seconds compute time, 1 GB network egress per month (North America).
*   **Cloud Storage:** 5 GB-months of Regional storage (US regions), 5,000 Class A Operations, 50,000 Class B Operations, 1 GB network egress.
*   **Cloud Build:** 120 build-minutes per day.
*   **Artifact Registry:** 120 GB-months storage, 120 build-minutes per day.
*   **Cloud Logging:** 50 GiB per project per month.

*Note: Cloud SQL does not have an "Always Free" tier, but the $300 initial credit covers it for the first 90 days. We will use a micro instance to minimize costs.*

## 3. Account Creation and Setup

### Step 3.1: Create a GCP Account

1.  Navigate to the [Google Cloud Console](https://console.cloud.google.com/).
2.  Sign in with your Google account.
3.  Click **"Get started for free"**.
4.  Follow the prompts to set up your billing profile. Google requires a credit card to verify your identity, but you will not be charged automatically after the $300/90-day trial ends unless you manually upgrade to a paid account.

### Step 3.2: Create a New Project

1.  In the Cloud Console, click the project drop-down menu at the top of the page.
2.  Click **"New Project"**.
3.  Enter a project name (e.g., `enterprise-learning-platform`).
4.  Note your **Project ID** (e.g., `enterprise-learning-platform-123456`). You will need this for all deployments.
5.  Click **"Create"**.

### Step 3.3: Enable Billing

Ensure billing is enabled for your new project. Go to **Billing** in the navigation menu and link your project to your billing account.

### Step 3.4: Install Google Cloud CLI (gcloud)

The `gcloud` CLI is essential for deploying and managing your application.

1.  Download and install the Google Cloud CLI from the [official documentation](https://cloud.google.com/sdk/docs/install).
2.  Initialize the CLI:
    ```bash
    gcloud init
    ```
3.  Log in with your Google account when prompted.
4.  Select the project you created in Step 3.2.
5.  Set your default compute region (choose a free-tier eligible region like `us-central1`):
    ```bash
    gcloud config set compute/region us-central1
    ```

## 4. Enable Required APIs

Enable the necessary APIs for the Learning Platform. You can do this via the Cloud Console or using the `gcloud` CLI:

```bash
gcloud services enable \
  run.googleapis.com \
  sqladmin.googleapis.com \
  compute.googleapis.com \
  cloudbuild.googleapis.com \
  artifactregistry.googleapis.com \
  secretmanager.googleapis.com \
  cloudresourcemanager.googleapis.com
```

## 5. Next Steps

With your GCP account set up and the necessary APIs enabled, you are ready to configure the infrastructure. Proceed to `02_GCP_Deployment_Architecture.md` for the architectural overview and setup instructions for Cloud Run, Cloud SQL, and Cloud Storage.
