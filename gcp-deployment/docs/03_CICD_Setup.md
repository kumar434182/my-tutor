# CI/CD Pipeline Setup Guide

This guide explains how to set up the GitHub Actions CI/CD pipeline to automatically deploy your application to GCP Cloud Run whenever changes are pushed to the `main` branch.

## 1. Overview

The pipeline uses **Workload Identity Federation (WIF)**, which is the recommended, keyless authentication method for GitHub Actions to access GCP resources. This is far more secure than exporting and storing long-lived service account JSON keys.

## 2. Setting up Workload Identity Federation

You need to configure GCP to trust your GitHub repository.

### 2.1 Create a Workload Identity Pool and Provider

Run these commands in your Cloud Shell or local terminal (authenticated with `gcloud`):

```bash
export PROJECT_ID="your-project-id"
export GITHUB_ORG="your-github-username"
export GITHUB_REPO="your-repo-name"

# Create the Workload Identity Pool
gcloud iam workload-identity-pools create "github-actions-pool" \
  --project="${PROJECT_ID}" \
  --location="global" \
  --display-name="GitHub Actions Pool"

# Get the full ID of the Workload Identity Pool
export WORKLOAD_IDENTITY_POOL_ID=$(gcloud iam workload-identity-pools describe "github-actions-pool" \
  --project="${PROJECT_ID}" \
  --location="global" \
  --format="value(name)")

# Create the Workload Identity Provider
gcloud iam workload-identity-pools providers create-oidc "github-actions-provider" \
  --project="${PROJECT_ID}" \
  --location="global" \
  --workload-identity-pool="github-actions-pool" \
  --display-name="GitHub Actions Provider" \
  --attribute-mapping="google.subject=assertion.sub,attribute.actor=assertion.actor,attribute.repository=assertion.repository" \
  --issuer-uri="https://token.actions.githubusercontent.com"
```

### 2.2 Bind the Service Account to the GitHub Repository

We will use the same `cloud-run-sa` service account created in the Architecture Guide, but we need to grant it permissions to deploy to Cloud Run and push to Artifact Registry.

```bash
# Grant Artifact Registry Writer role
gcloud projects add-iam-policy-binding ${PROJECT_ID} \
  --member="serviceAccount:cloud-run-sa@${PROJECT_ID}.iam.gserviceaccount.com" \
  --role="roles/artifactregistry.writer"

# Grant Cloud Run Developer role
gcloud projects add-iam-policy-binding ${PROJECT_ID} \
  --member="serviceAccount:cloud-run-sa@${PROJECT_ID}.iam.gserviceaccount.com" \
  --role="roles/run.developer"

# Grant Service Account User role (required to deploy Cloud Run services)
gcloud projects add-iam-policy-binding ${PROJECT_ID} \
  --member="serviceAccount:cloud-run-sa@${PROJECT_ID}.iam.gserviceaccount.com" \
  --role="roles/iam.serviceAccountUser"

# Bind the Workload Identity Provider to the Service Account
gcloud iam service-accounts add-iam-policy-binding "cloud-run-sa@${PROJECT_ID}.iam.gserviceaccount.com" \
  --project="${PROJECT_ID}" \
  --role="roles/iam.workloadIdentityUser" \
  --member="principalSet://iam.googleapis.com/${WORKLOAD_IDENTITY_POOL_ID}/attribute.repository/${GITHUB_ORG}/${GITHUB_REPO}"
```

### 2.3 Get the Provider Resource Name

You will need this value for your GitHub Secrets:

```bash
gcloud iam workload-identity-pools providers describe "github-actions-provider" \
  --project="${PROJECT_ID}" \
  --location="global" \
  --workload-identity-pool="github-actions-pool" \
  --format="value(name)"
```

## 3. Configuring GitHub Secrets

Go to your GitHub repository -> **Settings** -> **Secrets and variables** -> **Actions**.

Add the following repository secrets:

1.  `GCP_PROJECT_ID`: Your GCP Project ID (e.g., `enterprise-learning-platform-123`).
2.  `WIF_PROVIDER`: The output from step 2.3 (e.g., `projects/123456789/locations/global/workloadIdentityPools/github-actions-pool/providers/github-actions-provider`).
3.  `WIF_SERVICE_ACCOUNT`: The email of your service account (e.g., `cloud-run-sa@your-project-id.iam.gserviceaccount.com`).

## 4. Triggering the Pipeline

Once the secrets are configured, the pipeline defined in `.github/workflows/gcp-deploy.yml` will automatically trigger whenever you push code to the `main` branch.

You can also trigger it manually from the **Actions** tab in GitHub.
