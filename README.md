# Java Spring-boot with MySQL and CI/CD Pipeline with OCP-Sandbox

# Table of Contents
* Dockerfile
* Connection to database in OCP
* CI/CD Pipeline
* Attention with OCP Pipeline
* Instructions to OCP Pipeline base Tekton

# Dockerfile
* For using OCP Pipeline base Tekton, `image` in `Dockerfile` must be defined as fully resouces
* For example `maven:3.9.6-eclipse-temurin-17-alpine` -> `docker.io/library/maven:3.9.6-eclipse-temurin-17-alpine`

# Connection to database in OCP
* Enter `src/resources/application.properties`, change the configuration of `MySQL` database
* `spring.datasource.url=jdbc:mysql://mysql:3306/testdb` with name of database `Service` in OCP
* `spring.datasource.username=user1` with `username` of database 
* `spring.datasource.password=userpass` with `password` of database

# CI/CD Pipeline
* Enter `.github/workflows/pipeline.yaml`
* `Option 1`: Using without OCP Pipeline as Tekton (after push image to registry, change the image tag in `k8s/deployment.yaml`)
 ```yaml
 # Remember to config DOCKERHUB_PASSWORD, DOCKERHUB_USERNAME, OCP_TOKEN, OCP_SERVER in Github Actions Settings
name: Full CI/CD without Tekton

on:
  push:
    branches:
      - main

env:
  IMAGE_NAME: be_application
  IMAGE_REGISTRY: docker.io
  GIT_SHA: ${{ github.sha }}
jobs:
  build-push-deploy-backend-java-and-database:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout source
        uses: actions/checkout@v4

      - name: Docker Login
        run: |
          echo "${{ secrets.DOCKERHUB_PASSWORD }}" | docker login -u "${{ secrets.DOCKERHUB_USERNAME }}" --password-stdin

      - name: Build image
        run: |
          docker build -t ${IMAGE_REGISTRY}/${{ secrets.DOCKERHUB_USERNAME }}/${{ env.IMAGE_NAME }}:${GIT_SHA} .

      - name: Push image
        run: |
          docker push ${IMAGE_REGISTRY}/${{ secrets.DOCKERHUB_USERNAME }}/${{ env.IMAGE_NAME }}:${GIT_SHA}

      - name: Patch image tag in deployment.yaml
        run: |
          sed -i "s|image: .*|image: ${IMAGE_REGISTRY}/${{ secrets.DOCKERHUB_USERNAME }}/${{ env.IMAGE_NAME }}:${GIT_SHA}|" k8s/deployment.yaml
          echo "----- PATCHED deployment.yaml -----"
          cat k8s/deployment.yaml

      - name: Install OpenShift CLI
        run: |
          curl https://mirror.openshift.com/pub/openshift-v4/clients/oc/latest/linux/oc.tar.gz | tar -xz        
          sudo mv oc kubectl /usr/local/bin/
          oc

      - name: Login to OpenShift
        run: |
          echo "Logging into OpenShift..."
          oc login --token=${{ secrets.OCP_TOKEN }} --server=${{ secrets.OCP_SERVER }}

      - name: Deploy Java Application and Database to OpenShift
        run: |
          oc apply -f k8s/deployment.yaml
          oc apply -f k8s/database_deployment.yaml
  ```


* `Option 2`: Using  Pipeline as Tekton and trigger OCP webhook when push (call OCP webhook to trigger OCP Pipeline when pushing code in `main` branch) 
 ```yaml
# Remember to config WEBHOOK_SECRET, WEBHOOK_URL in Github Actions Settings
name: Full CI/CD without Tekton

on:
  push:
    branches:
      - main

env:
  IMAGE_NAME: be_application
  IMAGE_REGISTRY: docker.io
  GIT_SHA: ${{ github.sha }}
jobs:
  build-push-deploy-backend-java-and-database:
    runs-on: ubuntu-latest

    steps:
      - name: Trigger Tekton Webhook
        env:
          WEBHOOK_URL: ${{ secrets.WEBHOOK_URL }}
          WEBHOOK_SECRET: ${{ secrets.WEBHOOK_SECRET }}
        run: |
          payload=$(cat "$GITHUB_EVENT_PATH")
          signature='sha1='$(echo -n "$payload" | openssl sha1 -hmac "$WEBHOOK_SECRET" | sed 's/^.* //')

          curl -X POST $WEBHOOK_URL \
            -H "Content-Type: application/json" \
            -H "X-GitHub-Event: push" \
            -H "X-Hub-Signature: $signature" \
            -d "$payload"
  ```

# Attention with OCP Pipeline
* Name of remote repository must not contains `_` for creating resouces for OCP Pipeline. For example `fe_application` must be `fe-application`

# Instructions to OCP Pipeline base Tekton
Check out the instructions repository: [IoT Light System](https://github.com/huynguyenquang116/iot-light-system)


