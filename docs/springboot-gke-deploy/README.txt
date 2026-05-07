Grant Application Deployment to GKE

This ZIP contains:
- Dockerfile
- Kubernetes deployment.yaml
- Kubernetes service.yaml

Steps:
1. Build JAR with Maven: 
	mvn clean package
2. Build Docker image 
	docker build -t grannt-application .
3. Push to Artifact Registry
	docker push grannt-application
4. Apply k8s manifests
	kubectl apply -f deployment.yaml
	kubectl apply -f sevice.yaml
5. Access via Load Balancer IP