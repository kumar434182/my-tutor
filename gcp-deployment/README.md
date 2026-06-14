# Enterprise Learning Platform - GCP Deployment Guide

This directory contains everything you need to deploy the Enterprise Learning Platform to Google Cloud Platform (GCP), optimized for the Free Tier.

## 📁 Directory Structure

*   `docs/`: Comprehensive step-by-step guides.
*   `scripts/`: Bash scripts for automated deployment using `gcloud` CLI.
*   `terraform/`: Infrastructure as Code (IaC) configuration for GCP resources.
*   `../.github/workflows/`: GitHub Actions CI/CD pipeline configuration.

## 📚 Documentation Guides

Please read the guides in the following order:

1.  **[01_GCP_Free_Tier_Setup.md](docs/01_GCP_Free_Tier_Setup.md)**: How to create a GCP account and maximize free tier benefits.
2.  **[02_GCP_Deployment_Architecture.md](docs/02_GCP_Deployment_Architecture.md)**: Architectural overview and component setup (Cloud Run, Cloud SQL, Secret Manager).
3.  **[03_CICD_Setup.md](docs/03_CICD_Setup.md)**: Setting up automated deployments using GitHub Actions and Workload Identity Federation.
4.  **[04_Cost_Optimization_Guide.md](docs/04_Cost_Optimization_Guide.md)**: Strategies to keep costs low and set up billing alerts.
5.  **[05_Domain_and_SSL_Setup.md](docs/05_Domain_and_SSL_Setup.md)**: (Optional) Configuring a custom domain and SSL certificate via a Global Load Balancer.

## 🚀 Quick Start (Manual Deployment)

If you want to deploy quickly using the provided bash script:

1.  Ensure you have the `gcloud` CLI installed and authenticated.
2.  Set your environment variables:
    ```bash
    export PROJECT_ID="your-gcp-project-id"
    export REGION="us-central1"
    ```
3.  Run the deployment script:
    ```bash
    ./scripts/deploy.sh
    ```

## 🏗️ Infrastructure as Code (Terraform)

For production environments, it is highly recommended to use Terraform to manage your infrastructure.

1.  Navigate to the `terraform/` directory.
2.  Initialize Terraform:
    ```bash
    terraform init
    ```
3.  Review the execution plan:
    ```bash
    terraform plan -var="project_id=your-project-id" -var="db_password=your-secure-password" -var="jwt_secret=your-jwt-secret"
    ```
4.  Apply the configuration:
    ```bash
    terraform apply -var="project_id=your-project-id" -var="db_password=your-secure-password" -var="jwt_secret=your-jwt-secret"
    ```
