# Fractal Bank

This sample will help you get started instantiating shared infrastructure in the environments you have created earlier,
Production and Non-Production.

![Architectural Description](./deliverable.drawio.png)

## Objectives

* Definition of the `architecture_deployment` GitHub Action deploying the Fractals and initial Live Systems in the environments
* [Azure CAF](https://learn.microsoft.com/en-us/azure/cloud-adoption-framework/ready/landing-zone/design-areas) adoption
  as per diagram (Hub-and-spoke, management groups, etc.)
* All services are deployed in private mode, communication is only performed through azure private network by peering
  the spokes to the hub, opening the correct firewall rules, and
  using [private links](https://learn.microsoft.com/en-us/azure/private-link/private-link-overview) for storage account
  and postgres sql.
* The `architecture_deployment` action will deploy 1 Fractal and 2 Live Systems:
    * `containers:v01` Fractal will be created and 2 Live Systems will be deployed on both Non-Production and Production
      Operational Environments
* Observability stack will be deployed on each individual Kubernetes cluster with a basic dashboard showing services
  metrics.
