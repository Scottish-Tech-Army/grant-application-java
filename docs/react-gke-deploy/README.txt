React + Vite + TypeScript Deployment to GKE

This ZIP contains:
- Dockerfile
- nginx.conf
- Kubernetes deployment.yaml
- Kubernetes service.yaml

Steps:
1. Build docker image
	docker build -t grannt-hub .
2. Push to Artifact Registry
	docker push grannt-hub
3. Apply k8s manifests
	kubectl apply -f deployment.yaml
	kubectl apply -f sevice.yaml
4. Access external load balancer