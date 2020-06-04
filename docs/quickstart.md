# Quick Start Guide

The CDR Sandbox is an all-encompassing “black-box” environment created using Docker to deploy images in stable, network-enabled containers. The sandbox includes preconfigured Docker Compose YAML files for quick and easy deployment in a testing/lab scenario. For production deployments, a more comprehensive orchestration solution (eg. Kubernetes) is recommended.

## Prerequisites

* Root access to a Linux or MacOS machine with:
  * Git
  * Docker
  * At least 8GB of RAM available to docker

```sh
127.0.0.1 sso-admin.data-holder.local
127.0.0.1 sso.data-holder.local
127.0.0.1 api.data-holder.local
127.0.0.1 dr.data-recipient.local
```
