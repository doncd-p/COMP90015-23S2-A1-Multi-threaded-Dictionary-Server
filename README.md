# COMP90015-23S2-A1-Multi-threaded-Dictionary-Server

## Overview

This project implements a multi-threaded dictionary server using a client-server architecture. It allows multiple clients to interact concurrently with the server through socket communication, ensuring reliable connections. The server handles various dictionary operations, including querying word meanings, adding new words, removing existing words, and updating word meanings.

## System Components

### Server Component

The server employs a worker pool architecture for efficient handling of client requests. It initializes with dictionary data from a file, and worker threads manage client requests sequentially, ensuring optimal resource utilization.

### Client Component

The client acts as a bridge for data exchange between the user interface and the server. It establishes a connection with the server for each operation and closes the connection after receiving the server's response.

### TCP/IP Communication via Sockets

Communication between the client and server relies on the TCP/IP protocol facilitated by sockets, ensuring reliable and ordered data transmission.

### JSON Message Exchange Protocol

JSON is used as the message exchange protocol, facilitating structured data exchange between the server and client. Requests and responses are formatted as JSON objects for clarity and ease of communication.

## Usage

### Server

1. Compile and run the `DictionaryServer` class.
2. The server initializes with dictionary data and awaits client requests.

### Client

1. Compile and run the `DictionaryClient` class.
2. Use the graphical user interface to perform dictionary operations.
3. The client establishes a connection with the server for each operation and closes it after receiving a response.

## Class Structure

### Server Class Structure

-   `DictionaryServer`: Initializes a WorkerPool for managing WorkerThread instances.
-   `Dictionary`: Facilitates synchronized operations on the dictionary data.
-   `WorkerPool` and `WorkerThread`: Core components for efficient task handling.

### Client Class Structure

-   `DictionaryClient`: Manages communication with the server using JSON-based requests.
-   `GUI`: Provides users with an intuitive interface for interacting with the server.

## Interaction Diagram

The interaction diagram illustrates how the server handles incoming requests from clients through the worker pool architecture.

## Critical Analysis

### Functionality

All functional requirements have been successfully implemented, meeting the intended goals of the system.

### Error Handling

The system promptly responds with informative error messages when users initiate incorrect requests, enhancing the user experience.

### Advantages of the System

-   Utilization of TCP sockets ensures reliable data transmission.
-   Worker pool architecture facilitates proper concurrency management.
-   Effective error handling mechanisms enhance system robustness.

## Future Improvements

-   Implement advanced scalability strategies for dynamic scaling.
-   Introduce load balancing mechanisms to prevent resource overloading.
-   Integrate a database for persistent data storage.

## Conclusion

The system's architecture, communication protocol, and error management contribute to a robust and efficient distributed dictionary server.
