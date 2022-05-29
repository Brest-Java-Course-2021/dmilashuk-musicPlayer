#!/usr/bin/env bash
kubectl delete -f ./kubernates/web-app-deployment.yaml
kubectl delete -f ./kubernates/rest-app-deployment.yaml
kubectl delete -f ./kubernates/mysql-deployment.yaml
kubectl delete -f ./kubernates/music-player-configmap.yaml
kubectl delete -f ./kubernates/music-player-secrets.yaml
