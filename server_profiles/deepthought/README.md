# Deep Thought

[![Maven Central](https://img.shields.io/maven-central/v/io.biza/deepthought?label=latest%20release)](https://search.maven.org/artifact/io.biza/deepthought) [![Nexus Latest Snapshot](https://img.shields.io/nexus/s/io.biza/deepthought?label=latest%20snapshot&server=https%3A%2F%2Foss.sonatype.org)](https://oss.sonatype.org/#nexus-search;classname~io.biza.deepthought) [![Consumer Data Standards v1.2.0](https://img.shields.io/badge/Consumer%20Data%20Standards-v1.2.0-success)](https://consumerdatastandardsaustralia.github.io/standards)

[![develop build](https://img.shields.io/travis/com/bizaio/deepthought/develop?label=develop%20build)](https://travis-ci.com/bizaio/deepthought) [![master build](https://img.shields.io/travis/com/bizaio/deepthought/master?label=master%20build)](https://travis-ci.com/bizaio/deepthought) [![GitHub issues](https://img.shields.io/github/issues/bizaio/deepthought)](https://github.com/bizaio/deepthought/issues) ![GitHub](https://img.shields.io/github/license/bizaio/deepthought) 

The Deep Thought project is intended to be a reference Data Holder for the Australian Consumer Data Right (aka "open banking"). It is currently being developed by Biza.io as part of it's DataRight Lab initiative.

Deep Thought is currently developed and maintained by [Biza.io](https://www.biza.io).

## Features

  - Complete Product Reference Data support for V1 and V2 Payloads powered by [babelfish-cdr](https://github.com/bizaio/babelfish-cdr)
  - Administration API for Payload Manipulation backed by Hibernate
  - Graphical User Interface for Administration with full validation and type labeling presentation
  - Database backed storage of data with payload mappings powered by [Orika Mapper](https://github.com/orika-mapper/orika)
  - Ansible configuration and Packer rules for DevOps enabled deployment
  - Self Contained Amazon AMI for rapid deployment
  - OpenAPI 3 Support for all components
  - Support for all [Banking API Endpoints](https://consumerdatastandardsaustralia.github.io/standards/#consumer-data-standards-banking-apis)
  - Support for [Customer API Endpoints](https://consumerdatastandardsaustralia.github.io/standards/#get-customer)
  - Grant/Consent association support
  - Pairwise Identifier support for Customer, Account, Transaction, Direct Debit, Payments identifiers
  - Postman Collections as follows:
    - [Consumer Data Standards](https://documenter.getpostman.com/view/8730833/SzKWswP4?version=latest)
    - [Deep Thought Administration](https://documenter.getpostman.com/view/8730833/SzKWswP6?version=latest)

We are currently working on adding the following:
   - Support for [Discovery Endpoints](https://consumerdatastandardsaustralia.github.io/standards/#get-status)
   - Support for [Admin APIs](https://consumerdatastandardsaustralia.github.io/standards/#admin-apis)
   - Integration with the CDR Consent flow

## Screenshots

<img src="/screenshots/product-list.png?raw=true" width="500px"> | <img src="/screenshots/product-view.png?raw=true" width="500px"> | <img src="/screenshots/fees-list.png?raw=true" width="500px">
:----:|:-----:|:-----:|
Product List | Product View | Fees List |
<img src="/screenshots/constraints-eligibility-list.png?raw=true" width="500px"> | <img src="/screenshots/additional-info-validation.png?raw=true" width="500px"> | <img src="/screenshots/card-art-list.png?raw=true" width="500px"> |
Constraints & Eligibility List | Additional Information Validation | Card Art List


## Quick Start

Deep Thought is split into a number of individual components which interact with each other. The easiest way to get started is to use the [Amazon AMI](#amazon-ami) we make available with the latest release which includes a preconfigured Keycloak authentication server and MySQL database. Once up and running you will need to initialise some test data, using the [Deep Thought Administration Postman Collection](https://documenter.getpostman.com/view/8730833/SzKWswP6?version=latest) the initial setup with a Branch, Customer, Savings Product and Savings account is the following functions in the following order:
  1) Bind Subject
  2) Create Brand
  3) Create Bank Branch
  4) Associated Branch to Brand
  5) Create Product
  6) Create Customer (Person)
  7) Create Bank Account (Savings)
  8) Create Customer Account Connection
  9) Create Grant

Once this is completed you can use the `subject` defined during `Bind Subject` to access the appropriate services (on port 8081) as defined in the [Consumer Data Standards Postman Collection](https://documenter.getpostman.com/view/8730833/SzKWswP4?version=latest).

## Table of Contents

- [Features](#features)
- [Screenshots](#screenshots)
- [Quick Start](#quick-start)
- [Running](#running)
  - [Amazon AMI](#amazon-ami)
  - [GitHub Releases](#github-releases)
- [Deployments](#deployments)
- [Support](#support)
- [Compatibility](#compatibility)
- [Prerequisites](#prerequisites)
- [Architecture](#architecture)
  - [Components](#components)
  - [Database Support](#database-support)
  - [Authentication](#authentication)
- [Production Deployment](#production-deployment)
- [Building](#building)
- [Contributing](#contributing)
- [License](#license)

## Running

[(Back to top)](#table-of-contents)

Deep Thought can be deployed via a number of methods:
  - Amazon AMI deployment ([See Amazon AMI](#amazon-ami))
  - From GitHub Release artefacts ([See GitHub Releases](#github-releases))
  - Compiled from Source ([See Building](#building))

### Amazon AMI

[(Back to top)](#table-of-contents)

Deploying using the preconfigured Amazon AMI is an ideal way to test Deep Thought.

The All in One image contains the following:

- `product-api-service` deployed at the path specified by the [Standards](https://consumerdatastandardsaustralia.github.io/standards/#uri-structure)
- `admin-frontend` attached to `admin-service`
- `admin-service` attached to a local MySQL database
- A Keycloak service configured for the `admin-service` and setup with a default username (`deepthought`) and password (`solongandthanksforallthefish`)
- An Nginx reverse proxy which has been automatically configured with a dynamically issued SSL certificate as a subdomain of `cdr.zone` (one of our domains)

#### Deployment

Our Amazon Marketplace entry, which makes things even easier, is currently awaiting approval however in the meantime you can deploy the public AMI `ami-0d0fe791606551e99` in the `ap-southeast-2` region directly as follows:

**Note:** In order to conduct SSL initialisation the AMI requires a Public IP to be assigned with Internet access.

**Step 1**

From the *Launch Instance* dialog within AWS find the current Deep Thought AMI (`ami-0d0fe791606551e99`).

<img src="/screenshots/step1-launchinstance.png?raw=true" width="800px"> 

**Step 2**

Select an instance type of at least `t2.small` type. Other instance types should function however small instance types will encounter performance degradation

<img src="/screenshots/step2-instancetype.png?raw=true" width="800px"> 

**Step 3**

Select Network details and **make sure** that *Auto-assign Public IP* is selected.

<img src="/screenshots/step3-instancedetails.png?raw=true" width="800px"> 

**Step 4**

Intialise a Root volume of at least 8GB

<img src="/screenshots/step4-storage.png?raw=true" width="800px"> 

**Step 5**

Add tags if desired

<img src="/screenshots/step5-tags.png?raw=true" width="800px"> 

**Step 6**

Initialise a Security Group with access to SSH, HTTP and HTTPS. HTTP is used for Letsencrypt SSL certificate initialisation and should be 0.0.0.0/0. For SSH and HTTPS you can choose to limit the IP range to your source address.

<img src="/screenshots/step6-securitygroups.png?raw=true" width="800px"> 

**Step 7**

Review your instance and proceed with launch making sure you choose an SSH key pair you have access to.

<img src="/screenshots/step7-instancelaunch.png?raw=true" width="800px"> 

**Step 8**

On first boot Deep Thought performs a number of initialisation activities associated with setting up a Root CA accepted certificate. Consequently initialisation can take up to 10 minutes.

**Step 9**

After obtaining the publicly assigned IP from the Amazon console and ssh to the IP using your SSH key and the username of `ubuntu`. On login you will be presented a message of the day containing the endpoint details. If the endpoint details still refer to `localhost` the initialisation has not yet completed.

<img src="/screenshots/step9-motd.png?raw=true" width="800px"> 

**Step 10**

Load the URL for *Deep Thought Administration GUI* and you should be presented with the Deep Thought themed Keycloak login. Login with the default username of `deepthought` and password of `solongandthanksforallthefish`.

<img src="/screenshots/step10-login.png?raw=true" width="800px"> 

**Step 11**

After logging in and changing your password you can now begin building out your Data Holder.

<img src="/screenshots/step11-deepthought.png?raw=true" width="800px"> 

### GitHub Releases

As a fast alternative to using the Amazon AMI you can download the GitHub Releases and run the components locally. By default the components are configured to use a H2 file based database (at `../localdb`) and authenticated via a registration enabled OIDC server hosted by [Biza.io](https://biza.io/) for our [DataRight.io](https://dataright.io) project.

#### Running

1. From the [GitHub Releases page](https://github.com/bizaio/deepthought/releases) download each of the components comprising the release, currently this is `admin-service`, `admin-frontend` and `product-api-service`
2. For `admin-service` and `product-api-service` execute `jar -jar jar-file-name`, they are configured to operate on ports 8080 (`admin-service`) and 8081 (`product-api-service`)
3. For `admin-frontend` you will need to run this on a local http server. After extracting the zip file, `cd` into the directory and then execute your [simple http server of choice](https://gist.github.com/willurd/5720255). For example for Python 3.x enabled hosts `python -m http.server 4200` will result in a server available at [http://localhost:4200](http://localhost:4200)

## Deployments

[(Back to top)](#table-of-contents)

*Deep Thought* is currently deployed within the following projects or organisations:
- [DataRight Lab](https://dataright.io/lab)
- [Biza Hosted Holder](https://biza.io/holder/)

If you are using *Deep Thought* in your organisation we welcome you to let us know by [email](mailto:hello@biza.io).

## Support

[(Back to top)](#table-of-contents)

[Biza Pty Ltd](https://biza.io/) are currently the primary maintainers of this software. 

We welcome bug reports via [GitHub Issues](https://github.com/bizaio/deepthought/issues) or if you prefer via [email](mailto:hello@biza.io).

If you are looking for commercial support we offer a number of deployment options including commercial software support, a managed service or pure Software-as-a-Service.


## Compatibility

[(Back to top)](#table-of-contents)

The Deep Thought project aims to be entirely compliant to the [Consumer Data Standards](https://consumerdatastandardsaustralia.github.io/standards). While we try to align our version numbers to those of the Standards unfortunately the DSB has chosen to use all of the *x.y.z* versioning of the semantic versioning scheme. Consequently the following table outlines the alignment between Deep Thought versions and the Standards:

Deep Thought Version                 | Release Date | CDS Spec Compatibility     | Notes                                                             | Status
------------------------------------ | ------------ | -------------------------- | ----------------------------------------------------------------- | --------
1.1.0-SNAPSHOT (**current develop**) | Regular      | 1.2.0                      | Snapshot Development Release                                      | Active Development
1.0.2 (**current stable**)           | 2020-02-12   | 1.2.0                      | [tag v1.0.2](https://github.com/bizaio/deepthought/tree/v1.0.2)   | Supported

## Prerequisites

[(Back to top)](#table-of-contents)

You need the following installed and available in your $PATH during compilation:
- Java 11+
- Apache Maven 3.6.3 or later
- NodeJS 12.14+
- NPM 6.12+

## Architecture

[(Back to top)](#table-of-contents)

Deep Thought is a combination of frontend and backend components, database storage and authentication clients designed to be implemented either all together or in a distributed fashion for production deployments.

### Components

[(Back to top)](#table-of-contents)

Deep Thought is comprised of multiple services designed for either self contained or complete Production deployment.

Component Name                       | Description                                                                           | Dependencies
-------------------------------------|---------------------------------------------------------------------------------------|------------------------------
admin-angular-client                 | An NPMJS.com published artefact written in Typescript for accessing the admin-service | `admin-service`
admin-frontend                       | Angular based GUI for Holder Administration activities                                | `admin-service`
admin-service                        | OpenAPI 3 Administration API secured by an OIDC server                                | `data` `common`
ansible                              | Ansible rules for deployment of the All in One Server                                 |
common                               | Shared Spring components                                                              |
data                                 | Shared Hibernate components                                                           |
keycloak-theme                       | Customer Keycloak Login theme used within the AIO AMI                                 | Keycloak Server
packer                               | Packer definitions for the AIO AMI                                                    |
product-api-service                  | CDR Compliant Product Reference Data Endpoint                                         | `data` `common`

### Database Support

[(Back to top)](#table-of-contents)

Deep Thought utilises Java Hibernate for database operations. While it is likely that it can support any database Hibernate supports we currently test it for the following database architectures:
  - H2 Database using Local Directory/File storage
  - MySQL Database via Network

**By default** Deep Thought components will initialise using a H2 file based database located at `../localdb/deepthought`. Database access parameters can modified via a custom Spring YAML configuration file as demonstrated by the `spring-config.yml` [template contained within](https://github.com/bizaio/deepthought/blob/develop/ansible/roles/deepthought-service/templates/spring-config.yml) the Ansible component.

### Authentication

[(Back to top)](#table-of-contents)

Deep Thought uses OpenID Connect for authentication within the Administration interface. The Administration interface expects the authenticated user to be granted the scopes of `DEEPTHOUGHT:ADMIN:PRODUCT:READ` and `DEEPTHOUGHT:ADMIN:PRODUCT:WRITE`.

**By default** Deep Thought utilises an OpenID Connect server hosted by DataRight.io. While no warranty is implied for this server we have enabled User Registration for this realm to ease testing Deep Thought locally. As with database configuration it is possible to alter the JWKS endpoint used by utilising a custom Spring config as demonstrated within the Ansible rules for [Spring Configuration](https://github.com/bizaio/deepthought/blob/develop/ansible/roles/deepthought-service/templates/spring-config.yml) and by deploying a custom `config.json` within the Admin Frontend in `assets/config.json` as demonstrated in the Ansible rules for [deepthought-frontend](https://github.com/bizaio/deepthought/blob/develop/ansible/roles/deepthought-frontend/templates/config.json).

## Production Deployment

[(Back to top)](#table-of-contents)

Deep Thought is specifically built to be deployed within Production like environments. This means that it deliberately isolates individual components and, where relevent, assumes that certain components will be deployed in separate security zones from others. While we are still putting together some more indepth documentation around these deployment methodologies at a bare minimum we **recommend** the following:

1. Deployment of `admin-service` and `admin-frontend` should occur in a protected security environment with an internal OIDC server
2. `product-api-service` should be deployed behind an API Gateway (typically in reverse proxy configuration)
3. Databases should be configured in a Master for `admin-service` access and Read-Only Replica for `product-api-service`

If you are considering deploying *Deep Thought* into production we encourage you to contact us by [email](mailto:hello@biza.io).

## Building

This project is a Maven based meta package. Consequently it is possible to build all components at once then run from each sub directory.

1. Clone the repository: `$ git clone https://github.com/bizaio/deepthought`
2. Change to the root project directory: `cd deepthought`
3. Execute the build including the Angular npm wrapper: `mvn clean install -D -Dskip.npm=false`
4. Start each service individually:
   - Start `admin-service`: `cd admin-service; mvn spring-boot:run`
   - Start `admin-frontend`: `cd admin-frontend; ng serve`
   - Start `product-api-service`: `cd product-api-service; mvn spring-boot:run`


## Contributing

[(Back to top)](#table-of-contents)

1. Clone repository and create a new branch: `$ git checkout https://github.com/bizaio/deepthought -b my_new_branch`
2. Make changes (including tests please!)
3. Submit Pull Request for integration of changes

## License

[(Back to top)](#table-of-contents)

GNU General Public License v3.0 2020 - [Biza Pty Ltd](https://biza.io/). Please have a look at the [LICENSE.md](LICENSE.md) for more details.


