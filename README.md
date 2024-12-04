# Chatterbox

Chatterbox is a Java-based group chat application that uses socket programming to enable real-time communication between multiple clients. The application features a graphical user interface built with Swing.

## Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [LAN](#connecting-on-the-same-network)
- [Technologies](#technologies)
- [Contributing](#contributing)
- [License](#license)

## Features
- Real-time group chat functionality
- Graphical user interface for ease of use
- Supports multiple clients connecting to a single server

## Prerequisites
- Java JDK 8 or higher
- Java IDE

## Installation

You can either clone the repository using Git or download it as a ZIP file. Both methods work equally well.

### Option 1: Clone the Repository (Using Git)

1. Clone the repository:

```bash
git clone https://github.com/nirajktr/chatterbox.git
cd chatterbox

```
### Option 2: Download the ZIP File
1. Download the repository as a ZIP file from GitHub
2. Extract the contents of the ZIP file to a directory of your choice
3. Open the project directory in your preferred Java IDE (e.g., IntelliJ IDEA, Eclipse, NetBeans)
4. Compile the Java files using your IDE


## Usage
1. Compile both java files (example using bash):

```bash
javac SocketServer.java SocketClient.java
```
2. Run the server:
```bash
java SocketServer
```
3. Run the client afterwards:
```bash
java SocketClient
```
4. Connect:
- The UI will prompt you for the server IP address and your nickname. 
- Enter ```localhost``` if you're testing locally
- Once connected, you can start sending and receiving messages
5. Connect Multiple Clients:
- You can run multiple instances of the SocketClient on different terminals or IDEs. Each client can connect to the server, and the chat messages will be broadcast to all connected clients in real-time
- Each client will prompt for a unique nickname when connecting. Multiple users can join the chat using different names

## Connecting on the Same Network
1. Find the Server IP Address
    - On the server machine, open a terminal or command prompt and type ```ipconfig``` (Winodws) or ```ifconfig``` (Max/Linux)
    - Ask your teacher if in classroom environment
    - Find out the IPv4 address (e.g. ```192.168.x.x```)
2. Configure the Client:
    - On the client machine, when prompted for the server IP address, enter the IP address (e.g. ```192.168.x.x```)
3. Start Chatting
    - Once the client connects to the server, you can start sending and receiving messages with other clients on the same network

## Technologies
- **Java**: Programming language
- **Swing**: GUI
- **Socket Programming**: Real-time connection

## Contributing
Fork the repo, create a branch, make changes, and open a pull request

## License

[MIT](https://choosealicense.com/licenses/mit/)
