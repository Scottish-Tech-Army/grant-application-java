#!/bin/bash

# -----------------------------
#  CONFIGURATION (EDIT THESE)
# -----------------------------
PROJECT_ID="your-gcp-project-id"
CLUSTER_NAME="your-gke-cluster"
CLUSTER_ZONE="asia-south1-a"
APP_NAME="springboot-app"
IMAGE_NAME="gcr.io/$PROJECT_ID/$APP_NAME:latest"
DEPLOYMENT_NAME="springboot-deployment"
SERVICE_NAME="springboot-service"
PORT=8080

# -----------------------------
#  1. Build Spring Boot JAR
# -----------------------------
echo "📦 Building Maven Project..."
mvn clean package -DskipTests

# -----------------------------
#  2. Build Docker Image
# -----------------------------
echo "🐳 Building Docker Image..."
docker build -t $IMAGE_NAME .

# -----------------------------
#  3. Authenticate to Google Cloud
# -----------------------------
echo "🔐 Authenticating GCloud..."
gcloud auth login
gcloud config set project $PROJECT_ID

# -----------------------------
#  4. Enable Required APIs
# -----------------------------
echo "✅ Enabling GKE + Container Registry APIs..."
gcloud services enable container.googleapis.com
gcloud services enable containerregistry.googleapis.com

# -----------------------------
#  5. Authenticate Docker to GCR
# -----------------------------
echo "🔑 Configuring Docker for GCR..."
gcloud auth configure-docker

# -----------------------------
#  6. Push Docker Image to GCR
# -----------------------------
echo "📤 Pushing Image to GCR..."
docker push $IMAGE_NAME

# -----------------------------
#  7. Connect to GKE Cluster
# -----------------------------
echo "🔗 Connecting to GKE Cluster..."
gcloud container clusters get-credentials $CLUSTER_NAME --zone $CLUSTER_ZONE

# -----------------------------
#  8. Deploy to GKE
# -----------------------------
echo "🚀 Deploying application to GKE..."

# Create/update deployment
kubectl apply -f - <<EOF
apiVersion: apps/v1
kind: Deployment
metadata:
  name: $DEPLOYMENT_NAME
spec:
  replicas: 2
  selector:
    matchLabels:
      app: $APP_NAME
  template:
    metadata:
      labels:
        app: $APP_NAME
    spec:
      containers:
        - name: $APP_NAME
          image: $IMAGE_NAME
          ports:
            - containerPort: $PORT
EOF

# Create service
kubectl apply -f - <<EOF
apiVersion: v1
kind: Service
metadata:
  name: $SERVICE_NAME
spec:
  type: LoadBalancer
  selector:
    app: $APP_NAME
  ports:
    - protocol: TCP
      port: 80
      targetPort: $PORT
EOF

# -----------------------------
#  9. Wait for external IP
# -----------------------------
echo "⏳ Waiting for LoadBalancer external IP..."
kubectl get svc $SERVICE_NAME --watch