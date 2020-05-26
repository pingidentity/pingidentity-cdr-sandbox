# CDR Reference Implementation

## Contents

* [docker-compose](https://github.com/pingidentity/pingidentity-cdr-reference/tree/master/docker-compose)       

  Define and run multi-container Ping Identity Docker images with Docker Compose.
  
  
* [kubernetes](https://github.com/pingidentity/pingidentity-cdr-reference/tree/master/kubernetes)

  Deploy in Kubernetes.

### Bash profile Docker helper aliases
Several aliases are available in the utility `bash_profile_devops` to perform common 
Docker commands with containers, images, services, and so on.  You can easily source this
from your bash startup file (i.e. .bash_profile) to make easy use of these alias.

### Docker images

* A complete listing of the Ping Identity solutions public images used in these examples is available at [Docker Hub](https://hub.docker.com/u/pingidentity/).

## Security Warning

The server profiles referenced within this repository are for evaluation and documentation purposes only. They contain default credentials that would be a substantial security risk in a production environment.

For additional information, please see [SECURITY.md](SECURITY.md)

## Troubleshooting
This repository is in active development and has not been officially released. 
If you experience issues with this project, see [Troubleshooting](docs/troubleshooting/BASIC_TROUBLESHOOTING.md).
