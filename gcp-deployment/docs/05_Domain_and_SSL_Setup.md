# Custom Domain and SSL/TLS Configuration Guide

By default, Cloud Run provides a URL ending in `run.app` with an automatically provisioned SSL certificate. For a production Enterprise Learning Platform, you will want to map a custom domain (e.g., `learn.yourcompany.com`).

## 1. Domain Mapping Options in Cloud Run

Google Cloud offers two primary ways to map a custom domain to Cloud Run:

1.  **Cloud Run Domain Mapping (Preview/Limited Availability):** The simplest method, but it is not available in all regions and has limitations regarding wildcard domains.
2.  **Global External HTTP(S) Load Balancer:** The recommended, enterprise-grade approach. It provides DDoS protection (Cloud Armor), Cloud CDN, and managed SSL certificates.

Since this is an Enterprise platform, we will document the **Load Balancer** approach, which is more robust, though it incurs additional costs outside the free tier.

*Note: If you are strictly optimizing for zero cost, use the default `*.run.app` URL provided by Cloud Run.*

## 2. Setting up a Global External Application Load Balancer

This setup routes traffic from your custom domain to your Cloud Run frontend service.

### 2.1 Reserve a Static IP Address

First, reserve a global static IP address for your domain.

```bash
gcloud compute addresses create learning-platform-ip \
    --network-tier=PREMIUM \
    --ip-version=IPV4 \
    --global
```

Retrieve the IP address:

```bash
gcloud compute addresses describe learning-platform-ip \
    --format="get(address)" \
    --global
```

*Important: Go to your DNS provider (e.g., GoDaddy, Cloudflare, Route53) and create an `A` record pointing your domain (e.g., `learn.yourcompany.com`) to this IP address.*

### 2.2 Create a Serverless Network Endpoint Group (NEG)

The NEG acts as the backend for the Load Balancer, pointing to your Cloud Run service.

```bash
gcloud compute network-endpoint-groups create frontend-neg \
    --region=us-central1 \
    --network-endpoint-type=serverless  \
    --cloud-run-service=frontend-service
```

### 2.3 Create a Backend Service

Create a backend service and attach the NEG to it.

```bash
gcloud compute backend-services create learning-platform-backend \
    --load-balancing-scheme=EXTERNAL \
    --global

gcloud compute backend-services add-backend learning-platform-backend \
    --global \
    --network-endpoint-group=frontend-neg \
    --network-endpoint-group-region=us-central1
```

### 2.4 Create a URL Map

The URL map routes incoming requests to the backend service.

```bash
gcloud compute url-maps create learning-platform-url-map \
    --default-service learning-platform-backend
```

### 2.5 Create a Google-managed SSL Certificate

Google will automatically provision and renew the SSL certificate for your domain.

```bash
gcloud compute ssl-certificates create learning-platform-cert \
    --domains="learn.yourcompany.com"
```

### 2.6 Create Target HTTPS Proxy

Route requests to the URL map using the SSL certificate.

```bash
gcloud compute target-https-proxies create learning-platform-https-proxy \
    --ssl-certificates=learning-platform-cert \
    --url-map=learning-platform-url-map
```

### 2.7 Create Global Forwarding Rule

Finally, tie the static IP address to the HTTPS proxy.

```bash
gcloud compute forwarding-rules create learning-platform-https-rule \
    --load-balancing-scheme=EXTERNAL \
    --network-tier=PREMIUM \
    --address=learning-platform-ip \
    --target-https-proxy=learning-platform-https-proxy \
    --global \
    --ports=443
```

## 3. Verification

1.  Check the status of the SSL certificate. It may take 15-30 minutes for Google to provision the certificate after the DNS record propagates.
    ```bash
    gcloud compute ssl-certificates list
    ```
2.  Once the certificate status is `ACTIVE`, navigate to `https://learn.yourcompany.com`.

## 4. Optional: HTTP to HTTPS Redirect

To ensure all traffic uses secure connections, you can create an HTTP load balancer that redirects to the HTTPS load balancer.

```bash
# Create a URL map for redirect
gcloud compute url-maps import http-redirect-map \
    --global \
    --source /dev/stdin <<EOF
name: http-redirect-map
defaultUrlRedirect:
  redirectResponseCode: MOVED_PERMANENTLY_DEFAULT
  httpsRedirect: True
EOF

# Create target HTTP proxy
gcloud compute target-http-proxies create http-redirect-proxy \
    --url-map=http-redirect-map

# Create forwarding rule for port 80
gcloud compute forwarding-rules create http-redirect-rule \
    --load-balancing-scheme=EXTERNAL \
    --network-tier=PREMIUM \
    --address=learning-platform-ip \
    --target-http-proxy=http-redirect-proxy \
    --global \
    --ports=80
```
