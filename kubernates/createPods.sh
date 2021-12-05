#!/usr/bin/env bash
kubectl apply -f ./kubernates/music-player-secrets.yaml
kubectl apply -f ./kubernates/music-player-configmap.yaml
sleep 1
kubectl apply -f ./kubernates/mysql-deployment.yaml
sleep 30
kubectl apply -f ./kubernates/rest-app-deployment.yaml
sleep 5
kubectl apply -f ./kubernates/web-app-deployment.yaml