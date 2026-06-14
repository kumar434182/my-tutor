# GCP Cost Optimization & Monitoring Guide

## 1. Cost Optimization Strategy

To keep the Enterprise Learning Platform running within or very close to the GCP Free Tier limits, strict configuration is required.

### 1.1 Cloud Run Optimization

Cloud Run charges based on CPU and memory allocated *while processing a request* (unless CPU allocation is set to "always on").

*   **CPU Allocation:** Ensure CPU is **only allocated during request processing**. This is the default setting.
*   **Concurrency:** Maximize concurrency. The default is 80 requests per instance. Higher concurrency means fewer instances are spun up.
*   **Max Instances:** Set a strict limit on the maximum number of instances to prevent cost spikes during traffic surges.

```bash
# Update Cloud Run service limits
gcloud run services update backend-service \
    --max-instances=2 \
    --concurrency=80 \
    --cpu-boost \
    --region=us-central1
```

### 1.2 Cloud SQL Optimization

Cloud SQL is the only component that will consistently incur charges after the initial $300 trial.

*   **Instance Size:** Stick to `db-f1-micro` (1 vCPU, 0.6 GB RAM).
*   **Storage Type:** Use HDD instead of SSD if performance is acceptable, as it is significantly cheaper.
*   **Backups:** Retain only the minimum necessary backups (e.g., 7 days instead of 30).
*   **High Availability:** Disable HA (Zonal instead of Regional) for non-critical environments.

### 1.3 Artifact Registry Optimization

Docker images can consume storage quickly.

*   **Cleanup Policies:** Implement a policy to delete old image tags automatically. Keep only the last 3-5 successful builds.

## 2. Setting Up Billing Alerts

It is critical to set up billing alerts so you are notified immediately if costs begin to accrue.

1.  Go to the **Billing** section in the Cloud Console.
2.  Select **Budgets & alerts**.
3.  Click **Create budget**.
4.  Name the budget (e.g., "Free Tier Alert").
5.  Set the target amount to $1.00 (or $5.00).
6.  Configure alerts to trigger at 50%, 90%, and 100% of the budget.
7.  Ensure your email address is selected to receive notifications.

## 3. Monitoring and Logging

GCP provides robust observability tools via Cloud Operations (formerly Stackdriver).

### 3.1 Cloud Logging

Cloud Run automatically sends standard output (stdout) and standard error (stderr) to Cloud Logging.

*   View logs in the Console: **Logging** -> **Logs Explorer**.
*   Filter by resource type: `resource.type="cloud_run_revision"` and `resource.labels.service_name="backend-service"`.

### 3.2 Cloud Monitoring Dashboards

Create a custom dashboard to monitor your platform's health.

1.  Go to **Monitoring** -> **Dashboards**.
2.  Click **Create Dashboard**.
3.  Add widgets for:
    *   **Cloud Run Request Count:** `run.googleapis.com/request_count`
    *   **Cloud Run Latency:** `run.googleapis.com/request_latencies`
    *   **Cloud SQL CPU Utilization:** `cloudsql.googleapis.com/database/cpu/utilization`
    *   **Cloud SQL Memory Utilization:** `cloudsql.googleapis.com/database/memory/utilization`

### 3.3 Uptime Checks

Set up an uptime check to ensure your frontend is accessible.

1.  Go to **Monitoring** -> **Uptime checks**.
2.  Click **Create Uptime Check**.
3.  Set the Target to your Cloud Run frontend URL.
4.  Configure an alert policy to notify you via email if the check fails.
