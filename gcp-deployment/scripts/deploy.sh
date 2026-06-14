#!/bin/bash
set -e

# Colors for output
GREEN='\03    [0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Enterprise Learning Platform - GCP Deployment Script${NC}"

# Check if variables are set
if [ -z "$PROJECT_ID" ] || [ -z "$REGION" ]; then
    echo -e "${RED}Error: PROJECT_ID and REGION environment variables must be set.${NC}"
    echo "Usage: PROJECT_ID=my-project REGION=us-central1 ./deploy.sh"
    exit 1
fi

echo -e "${GREEN}Deploying to Project: $PROJECT_ID in Region: $REGION${NC}"

# 1. Build and Push Backend Image
echo -e "\n${YELLOW}1. Building and Pushing Backend Image...${NC}"
cd ../../backend
gcloud builds submit --tag $REGION-docker.pkg.dev/$PROJECT_ID/learning-platform-repo/backend:latest .

# 2. Build and Push Frontend Image
echo -e "\n${YELLOW}2. Building and Pushing Frontend Image...${NC}"
cd ../frontend
gcloud builds submit --tag $REGION-docker.pkg.dev/$PROJECT_ID/learning-platform-repo/frontend:latest .

# 3. Deploy Backend to Cloud Run
echo -e "\n${YELLOW}3. Deploying Backend to Cloud Run...${NC}"
gcloud run deploy backend-service \
    --image=$REGION-docker.pkg.dev/$PROJECT_ID/learning-platform-repo/backend:latest \
    --region=$REGION \
    --service-account=cloud-run-sa@$PROJECT_ID.iam.gserviceaccount.com \
    --add-cloudsql-instances=$PROJECT_ID:$REGION:learning-platform-db \
    --set-env-vars="SPRING_PROFILES_ACTIVE=gcp" \
    --set-env-vars="SPRING_DATASOURCE_URL=jdbc:mysql:///learning_platform?cloudSqlInstance=$PROJECT_ID:$REGION:learning-platform-db&socketFactory=com.google.cloud.sql.mysql.SocketFactory" \
    --set-env-vars="SPRING_DATASOURCE_USERNAME=root" \
    --set-secrets="SPRING_DATASOURCE_PASSWORD=db-password:latest,JWT_SECRET=jwt-secret:latest" \
    --allow-unauthenticated \
    --project=$PROJECT_ID

# Get Backend URL
BACKEND_URL=$(gcloud run services describe backend-service --region=$REGION --project=$PROJECT_ID --format="value(status.url)")
echo -e "${GREEN}Backend deployed at: $BACKEND_URL${NC}"

# 4. Deploy Frontend to Cloud Run
echo -e "\n${YELLOW}4. Deploying Frontend to Cloud Run...${NC}"
gcloud run deploy frontend-service \
    --image=$REGION-docker.pkg.dev/$PROJECT_ID/learning-platform-repo/frontend:latest \
    --region=$REGION \
    --set-env-vars="API_URL=$BACKEND_URL/api/v1" \
    --allow-unauthenticated \
    --project=$PROJECT_ID

# Get Frontend URL
FRONTEND_URL=$(gcloud run services describe frontend-service --region=$REGION --project=$PROJECT_ID --format="value(status.url)")
echo -e "${GREEN}Frontend deployed at: $FRONTEND_URL${NC}"

echo -e "\n${GREEN}Deployment Complete! 🎉${NC}"
echo -e "Access your application at: ${YELLOW}$FRONTEND_URL${NC}"
