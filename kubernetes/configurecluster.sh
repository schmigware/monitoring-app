#!/bin/sh

# K8S Zookeeper/Kafka setup

#RBAC roles
kubectl create clusterrolebinding cluster-system-serviceaccount-testbed --clusterrole=cluster-admin --user=system:system:serviceaccount:testbed:default
kubectl create clusterrolebinding permissive-binding \
   --clusterrole=cluster-admin \
   --user=admin \
   --user=kubelet \
   --group=system:serviceaccounts

#storage classes and namepaces
kubectl apply -f config/storage/minikube-storageclass-zookeeper.yml
kubectl apply -f config/storage/minikube-storageclass-broker.yml
kubectl apply -f config/kafka-namespace.yaml
kubectl apply -f config/rbac/node-reader.yml
kubectl apply -f config/rbac/pod-labler.yml

#Zookeeper configuration
kubectl apply -f config/zookeeper/zookeeper-config.yml
kubectl apply -f config/zookeeper/pzoo-service.yml
kubectl apply -f config/zookeeper/zoo-service.yml
kubectl apply -f config/zookeeper/service.yml
kubectl apply -f config/zookeeper/pzoo.yml
kubectl apply -f config/zookeeper/zoo.yml

#kafka configuration
kubectl apply -f config/kafka/broker-config.yml
kubectl apply -f config/kafka/dns.yml
kubectl apply -f config/kafka/bootstrap-service.yml
kubectl apply -f config/kafka/kafka.yml






