# monitoring-app
Thesis submission artefacts including:
  - Monitoring application sources / build scripts.
  - Testbed environment sources / build scripts.
  - Spring library for Kafka producer ID header injection.
  - Kubernetes / Minikube configuration scripts.
  - Supplementary and prototype code / scripts.
  - Dissertation sources (Latex).
  - Walthough video.
  
  
# Walkthrough script used in video. In unclear on anything, consult thesis appendices.
 
Welcome to demo. Recorded after thesis presentation 21/05/2019.

Pull everything from git
git clone https://github.com/schmigware/monitoring-app.git

Prepare the testbed pipeline infrastructure (Kafka/Zookeeper) (be patient…)
These steps are covered in Appendix A of thesis
Kafka/Zookeeper services up and running in K8S

Building and run the monitoring application 
These steps are covered in Appendix B of thesis

Fire up monitoring microservices: 
mvn spring-boot:run (using alias)

Service registry UI reports that all monitoring app micro services are up

Configure a monitored environment via the GraphQL UI:

- No environment profiles or environments defined
- Add a Kafka profile
- Add details of the testbed environment configured for discovery via kubernetes
- Enable environment monitoring using environment UUID
- Monitoring and Discovery services report that they’re now observing the newly registered environment
- No topology discovered as yet, no activity snapshots observed 

Deploy the testbed microservices
kubectl apply -f deployment.yml
kubectl apply -f configmap.yml

Topology gradually discovered as testbed services deployed. No activity rendered yet. 

Send some messages through topology.

*FIN*
 
Thanks!








