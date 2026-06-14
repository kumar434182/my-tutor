# GCP Deployment Architecture & Configuration

## 1. Architecture Overview

The Enterprise Learning Platform will be deployed using a modern, serverless architecture on Google Cloud Platform to maximize free tier benefits and minimize operational overhead.

### Key Components

*   **Frontend (Angular):** Hosted on **Cloud Run** (serving static files via Nginx container).
*   **Backend (Spring Boot):** Hosted on **Cloud Run** (fully managed serverless execution).
*   **Database (MySQL):** Hosted on **Cloud SQL** (db-f1-micro instance to minimize cost).
*   **Container Registry:** **Artifact Registry** (for storing Docker images).
*   **Secrets Management:** **Secret Manager** (for storing database credentials and JWT secrets).

## 2. Component Configuration

### 2.1 Artifact Registry Setup

Artifact Registry is required to store the Docker images for your frontend and backend before deploying them to Cloud Run.

```bash
# Create a repository for Docker images
gcloud artifacts repositories create learning-platform-repo \
    --repository-format=docker \
    --location=us-central1 \
    --description="Docker repository for Learning Platform"

# Configure Docker to authenticate with Artifact Registry
gcloud auth configure-docker us-central1-docker.pkg.dev
```

### 2.2 Cloud SQL (MySQL) Setup

Cloud SQL provides a managed MySQL database. We will use the smallest instance type (`db-f1-micro`) to keep costs low.

```bash
# Create the Cloud SQL instance
gcloud sql instances create learning-platform-db \
    --database-version=MYSQL_8_0 \
    --tier=db-f1-micro \
    --region=us-central1 \
    --root-password="YourStrongPassword123!"

# Create the database
gcloud sql databases create learning_platform \
    --instance=learning-platform-db

# Get the connection name (format: project:region:instance)
gcloud sql instances describe learning-platform-db --format="value(connectionName)"
```

### 2.3 Secret Manager Setup

Store sensitive configuration securely using Secret Manager.

```bash
# Create secrets
gcloud secrets create db-password --replication-policy="automatic"
gcloud secrets create jwt-secret --replication-policy="automatic"

# Add secret versions (values)
echo -n "YourStrongPassword123!" | gcloud secrets versions add db-password --data-file=-
echo -n "YourSuperSecretJwtKeyWithAtLeast256Bits!" | gcloud secrets versions add jwt-secret --data-file=-
```

### 2.4 Cloud Run Service Accounts

Create a dedicated service account for Cloud Run to access Cloud SQL and Secret Manager.

```bash
# Create service account
gcloud iam service-accounts create cloud-run-sa \
    --display-name="Cloud Run Service Account"

# Grant Cloud SQL Client role
gcloud projects add-iam-policy-binding YOUR_PROJECT_ID \
    --member="serviceAccount:cloud-run-sa@YOUR_PROJECT_ID.iam.gserviceaccount.com" \
    --role="roles/cloudsql.client"

# Grant Secret Manager Access role
gcloud projects add-iam-policy-binding YOUR_PROJECT_ID \
    --member="serviceAccount:cloud-run-sa@YOUR_PROJECT_ID.iam.gserviceaccount.com" \
    --role="roles/secretmanager.secretAccessor"
```

## 3. Deployment Configuration

### 3.1 Backend Cloud Run Configuration

When deploying the Spring Boot backend to Cloud Run, we must configure the Cloud SQL connection and environment variables.

```bash
# Example deployment command (automated in CI/CD)
gcloud run deploy backend-service \
    --image=us-central1-docker.pkg.dev/YOUR_PROJECT_ID/learning-platform-repo/backend:latest \
    --region=us-central1 \
    --service-account=cloud-run-sa@YOUR_PROJECT_ID.iam.gserviceaccount.com \
    --add-cloudsql-instances=YOUR_PROJECT_ID:us-central1:learning-platform-db \
    --set-env-vars="SPRING_PROFILES_ACTIVE=gcp" \
    --set-env-vars="SPRING_DATASOURCE_URL=jdbc:mysql:///learning_platform?cloudSqlInstance=YOUR_PROJECT_ID:us-central1:learning-platform-db&socketFactory=com.google.cloud.sql.mysql.SocketFactory" \
    --set-env-vars="SPRING_DATASOURCE_USERNAME=root" \
    --set-secrets="SPRING_DATASOURCE_PASSWORD=db-password:latest" \
    --set-secrets="JWT_SECRET=jwt-secret:latest" \
    --allow-unauthenticated
```

### 3.2 Frontend Cloud Run Configuration

The Angular frontend needs to know the URL of the deployed backend API.

```bash
# Example deployment command
gcloud run deploy frontend-service \
    --image=us-central1-docker.pkg.dev/YOUR_PROJECT_ID/learning-platform-repo/frontend:latest \
    --region=us-central1 \
    --set-env-vars="API_URL=https://backend-service-xxxxx-uc.a.run.app/api/v1" \
    --allow-unauthenticated
```

## 4. Next Steps

To automate this infrastructure provisioning, proceed to `03_Terraform_Automation.md` to use Infrastructure as Code (IaC) instead of manual `gcloud` commands.
