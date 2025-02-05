# Translation Server

## Overview

Translation Server is a backend service developed in Java, designed to provide language detection and translation capabilities. It exposes RESTful APIs that can be consumed by clients, such as the [TranslationClient](https://github.com/salonipriyani/TranslationClient), to detect the language of a given text and translate it into a specified target language. The server is containerized using Docker and integrates with MongoDB for logging and operational analytics, ensuring efficient performance and scalability.