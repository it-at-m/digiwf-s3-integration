<div id="top"></div>

<!-- PROJECT SHIELDS -->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]


<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/it-at-m/digiwf-s3-integration">
    <img src="images/logo.png" alt="Logo" height="200">
  </a>

<h3 align="center">DigiWF S3 Integration</h3>

  <p align="center">
    This is a Spring Boot Starter library to connect to S3 compatible services
     <!-- <br />
   <a href="https://github.com/it-at-m/digiwf-s3-integration"><strong>Explore the docs »</strong></a> -->
    <br />
    <br />
     <!-- <a href="https://github.com/it-at-m/digiwf-s3-integration">View Demo</a>
    · -->
    <a href="https://github.com/it-at-m/digiwf-s3-integration/issues">Report Bug</a>
    ·
    <a href="https://github.com/it-at-m/digiwf-s3-integration/issues">Request Feature</a>
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->

## About The Project

There are several ways to store files in S3 compatible storage. Each project often has to implement the same
functionalities and solve the same problems. With this library, we create the possibility to store and clean up files to 
in a structured and simple way. Here's why:

* Files often need to be stored in folder structures
* Files often must be stored in a structured way and enriched with metadata
* Cleaning up the data must be done in a structured way
* Synchronous and asynchronous interfaces are often required

Of course, one service is not suitable for all projects, as your needs may be different. That's why we decided to
provide a Spring Boot Starter library for an integration service that can be easily customized. 
Additionally a second starter library is included, which serves as a client library to handle files and folders
with the above-mentioned starter.

<p align="right">(<a href="#top">back to top</a>)</p>

### Built With

This project is built with:

* [Spring Boot](https://spring.io/projects/spring-boot)
* [minio](https://min.io)
* [Hibernate](https://hibernate.org)

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- GETTING STARTED -->

## Getting the integration library

_Below is an example of how you can install and setup your service_

1. Use the spring initalizer and create a Spring Boot application with `Spring Web`
   dependencies [https://start.spring.io](https://start.spring.io)
2. Add the `digiwf-s3-integration-starter` dependency

With Maven:

```
   <dependency>
        <groupId>io.muenchendigital.digiwf</groupId>
        <artifactId>digiwf-s3-integration-starter</artifactId>
        <version>${digiwf.version}</version>
   </dependency>
```

With Gradle:

```
implementation group: 'io.muenchendigital.digiwf', name: 'digiwf-s3-integration-starter', version: '${digiwf.version}'
```

3. Configure your S3 bucket with the following properties:
    - `io.muenchendigital.digiwf.s3.bucketName`
    - `io.muenchendigital.digiwf.s3.secretKey`
    - `io.muenchendigital.digiwf.s3.accessKey`
    - `io.muenchendigital.digiwf.s3.url`
    - `io.muenchendigital.digiwf.s3.initialConnectionTest`

`io.muenchendigital.digiwf.s3.initialConnectionTest` is an optional property which allows to enable or disable an initial connection test to the s3 bucket during boot up. 
If the property is `true` or not set, the connection test is performed.
If the property is explicitly set to `false`, no connection test is carried out.

If you want to use the cron job cleanup, take a look at the <a href="#cron-job-cleanup">usage exmaple</a> .

5. OpenAPI specification:

   - Enjoy the [OpenAPI definition](http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config)
   - Get the [Api-Docs](http://localhost:8080/v3/api-docs)

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- USAGE EXAMPLES -->

### Usage

The library has several functionalities that can be configured. We have provided examples that show how you can use
them.

_For more examples, please refer to the [example-s3-integration](https://github.com/it-at-m/digiwf-s3-integration/tree/dev/example-s3-integration)
and/or [example-s3-integration-client](https://github.com/it-at-m/digiwf-s3-integration/tree/dev/example-s3-integration-client)
folder._

### Minimum necessary spring boot annotations

Listed below are the required Spring boot annotations, which are minimal.

* ```@SpringBootApplication```
* ```@EnableJpaAuditing```
* ```@EnableScheduling```

### Cron Job Cleanup

Files need to be deleted after some time. We have developed a file structure to which an end of life timestamp can be saved. 
The cron job setting determines how often and when the files are checked and deleted. To use this functionality
configure the property:

``io.muenchendigital.digiwf.s3.cronjob.cleanup.expired-files=0 15 10 15 * ?``

This job cleans the metadata of the S3 files in the database if no corresponding file within the S3 storage exists.

``io.muenchendigital.digiwf.s3.cronjob.cleanup.unused-files=0 15 10 16 * ?``

## Getting the integration client library

_Below is an example of how you can installing and setup up your service_

1. Use the spring initalizer and create a Spring Boot application with `Spring Web`
   dependencies [https://start.spring.io](https://start.spring.io)
2. Add the `digiwf-s3-integration-client-starter` dependency

With Maven:

```
   <dependency>
        <groupId>io.muenchendigital.digiwf</groupId>
        <artifactId>digiwf-s3-integration-client-starter</artifactId>
        <version>${digiwf.version}</version>
   </dependency>
```

With Gradle:

```
implementation group: 'io.muenchendigital.digiwf', name: 'digiwf-s3-integration-client-starter', version: '${digiwf.version}'
```

3. Configure your service which uses this starter with the following properties:
   - `io.muenchendigital.digiwf.s3.client.document-storage-url`

### Usage

The client library provides several beans that can be used to interact with the `digiwf-s3-integration-starter`. 
We have provided examples that show how you can use them.

Each method within the client library, which communicates directly with a `digiwf-s3-integration-service`, is available in two different flavors.
One method that uses the document storage url defined in `io.muenchendigital.digiwf.s3.client.document-storage-url`.
Another method that expects the document storage url within the method parameter.
This allows to use different `digiwf-s3-integration-service` with the same client lib.

_For more examples, please refer to the [example-s3-integration](https://github.com/it-at-m/digiwf-s3-integration/tree/dev/example-s3-integration)
and/or [example-s3-integration-client](https://github.com/it-at-m/digiwf-s3-integration/tree/dev/example-s3-integration-client)
folder._

The images used in this example are not subject to any license.

### Minimum necessary spring boot annotations

Listed below are the required Spring boot annotations, which are minimal.

* ```@SpringBootApplication```

## more coming soon...

<p align="right">(<a href="#top">back to top</a>)</p>

<!-- CONTRIBUTING -->

## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any
contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also
simply open an issue with the tag "enhancement". Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- LICENSE -->

## License

Distributed under the MIT License. See `LICENSE` for more information.

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- CONTACT -->

## Contact

it@m - opensource@muenchendigital.io

Join our [Slack Channel](https://join.slack.com/t/digiwf/shared_invite/zt-14jxazj1j-jq0WNtXp7S7HAwJA7tKgpw) for more
information!

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[contributors-shield]: https://img.shields.io/github/contributors/it-at-m/digiwf-s3-integration.svg?style=for-the-badge

[contributors-url]: https://github.com/it-at-m/digiwf-s3-integration/graphs/contributors

[forks-shield]: https://img.shields.io/github/forks/it-at-m/digiwf-s3-integration.svg?style=for-the-badge

[forks-url]: https://github.com/it-at-m/digiwf-s3-integration/network/members

[stars-shield]: https://img.shields.io/github/stars/it-at-m/digiwf-s3-integration.svg?style=for-the-badge

[stars-url]: https://github.com/it-at-m/digiwf-s3-integration/stargazers

[issues-shield]: https://img.shields.io/github/issues/it-at-m/digiwf-s3-integration.svg?style=for-the-badge

[issues-url]: https://github.com/it-at-m/digiwf-s3-integration/issues

[license-shield]: https://img.shields.io/github/license/it-at-m/digiwf-s3-integration.svg?style=for-the-badge

[license-url]: https://github.com/it-at-m/digiwf-s3-integration/blob/master/LICENSE

[product-screenshot]: images/screenshot.png
