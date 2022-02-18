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

There are several ways to store files in S3 compatible storage. Each project often has to implement the same functionalities and solve the same problems. With this library, we create the possibility to store and clean up files to specific references / folders in a structured and simple way.
Here's why:
* Files often need to be stored in folder structures
* Folders often must be stored in a structured way and enriched with metadata
* Cleaning up the data must be done in a structured way
* Synchronous and asynchronous interfaces are often required

Of course, one service is not suitable for all projects, as your needs may be different. That's why we decided to provide a Spring Boot Starter library that can be easily customized

<p align="right">(<a href="#top">back to top</a>)</p>


### Built With

This project is built with:

* [Spring Boot](https://spring.io/projects/spring-boot)
* [minio](https://min.io)
* [Hibernate](https://hibernate.org)

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

_Below is an example of how you can installing and setup up your service_

1. Use the spring initalizer and areate a Spring Boot application with `Spring Web` dependencies [https://start.spring.io](https://start.spring.io)
2. Add the digiwf-s3-integration dependency

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
  
5. Enjoy the [OpenAPI definition](http://localhost:8089/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config) 

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- USAGE EXAMPLES -->
## Usage

The library has several functionalities that can be configured.
We have provided examples that show how you can use them.

_For more examples, please refer to the [Examples](https://github.com/it-at-m/digiwf-s3-integration/tree/dev/example) folder_

### Cron Job Cleanup
Files need to be deleted after some time.
We have developed a folder structure to which an end of life time can be saved. 
The cron job setting determines how often and when the folders are checked and deleted.
To use this functionality configure the property:
``io.muenchendigital.digiwf.s3.cleanupcronjob=0 15 10 15 * ?``


### more coming soon...

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

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
