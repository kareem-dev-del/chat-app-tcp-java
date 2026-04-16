# 💬 Client–Server Real-Time Chat Application

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![TCP](https://img.shields.io/badge/TCP%20Sockets-0078D4?style=for-the-badge&logo=socket.io&logoColor=white)
![Multithreading](https://img.shields.io/badge/Multithreading-00897B?style=for-the-badge&logo=java&logoColor=white)
![Status](https://img.shields.io/badge/Status-Complete-brightgreen?style=for-the-badge)

> A progressive client-server chat system built with Java TCP Sockets, demonstrating core concepts of **network programming**, **multithreading**, and **concurrent client handling**.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [How to Run](#how-to-run)
- [Level Details](#level-details)
- [Technologies Used](#technologies-used)

---

## 🗂 Overview

This project was developed as part of the **Network Programming** course (2nd Term 2025/2026) under the supervision of **Dr. Aya Ibrahim** — Information Technology Department, 3rd Year.

| Detail | Value |
|--------|-------|
| Student ID | 2320936 |
| Port Number | **20936** (last 5 digits of ID) |
| Language | Java SE |
| Protocol | TCP |

The application is built in **3 progressive levels**:

| Level | Description | Marks |
|-------|-------------|-------|
| Level 1 | Basic one-to-one TCP chat | 10 |
| Level 2 | Multithreaded multi-client server | 10 |
| Level 3 | Advanced features (Bonus) | +6 |

---

## ✨ Features

### Level 1 — Basic TCP Chat
- One server, one client
- Send and receive messages over TCP
- Clean disconnect with `exit` command

### Level 2 — Multithreaded Server
- Handles **multiple clients simultaneously**
- Each client runs in its own **dedicated thread**
- Graceful error handling and disconnection management
- Tested with 3 concurrent clients

### Level 3 — Advanced Features *(Bonus)*
- **Online Users List** — `/users` command shows all connected clients
- **Broadcast Messaging** — `/broadcast <msg>` sends to all clients
- **Simulated Clients** — 3 automated bot threads that connect and chat automatically
- Thread-safe user registry using `ConcurrentHashMap`

---

## 📁 Project Structure

```
ChatApp/
│
├── Level1/
│   ├── Server.java          # Basic TCP server (1 client)
│   └── Client.java          # Basic TCP client
│
├── Level2/
│   ├── Server.java          # Multithreaded server
│   ├── ClientHandler.java   # Runnable thread per client
│   └── Client.java          # Client with background listener
│
└── Level3/
    ├── Server.java          # Advanced server with shared features
    ├── ClientHandler.java   # Handles /users, /broadcast, events
    ├── Client.java          # Full command-line client
    └── SimulatedClient.java # 3 automated bot threads
```

---

## 🚀 Getting Started

### Prerequisites
- Java JDK 8 or higher
- Command line / Terminal

### Check Java version
```bash
java -version
```

---

## ▶️ How to Run

### Level 1 — Basic Chat

**Step 1: Compile**
```bash
cd Level1
javac Server.java Client.java
```

**Step 2: Run Server** (Terminal 1)
```bash
java Server
```

**Step 3: Run Client** (Terminal 2)
```bash
java Client
```

**Example interaction:**
```
[You] Hello Server!
[Server] Message received: "Hello Server!"
[You] exit
[Server] Goodbye! Connection closed.
```

---

### Level 2 — Multi-Client Chat

**Step 1: Compile**
```bash
cd Level2
javac Server.java ClientHandler.java Client.java
```

**Step 2: Run Server** (Terminal 1)
```bash
java Server
```

**Step 3: Run multiple clients** (Terminal 2, 3, 4)
```bash
java Client
```

---

### Level 3 — Advanced Chat

**Step 1: Compile**
```bash
cd Level3
javac Server.java ClientHandler.java Client.java SimulatedClient.java
```

**Step 2: Run Server** (Terminal 1)
```bash
java Server
```

**Step 3: Run human clients** (Terminal 2, 3)
```bash
java Client
```

**Step 4: Run simulated bots** (Terminal 4)
```bash
java SimulatedClient
```

**Available commands:**
| Command | Description |
|---------|-------------|
| `/users` | Show all connected users |
| `/broadcast <message>` | Send message to everyone |
| `exit` | Disconnect from server |

---

## 🔍 Level Details

### Architecture — Level 3

```
Client 1 (human) ──┐
Client 2 (human) ──┤──→ Server ──→ Thread 1 ──→ ConcurrentHashMap
SimulatedClient  ──┘         └──→ Thread 2 ──→ broadcast()
  (3 bot threads)             └──→ Thread 3 ──→ getOnlineUsers()
```

### Key Design Decisions

**Why `ConcurrentHashMap`?**
Multiple threads read and write the user registry simultaneously. `ConcurrentHashMap` provides thread-safe operations without explicit `synchronized` blocks, avoiding data corruption and deadlocks.

**Why a background listener thread in the Client?**
Without it, the client's main thread would block on `readLine()` waiting for user input, and could never display broadcast messages arriving from other clients. The daemon listener thread receives server messages independently.

**Why `setDaemon(true)` on the listener?**
A daemon thread terminates automatically when the main thread exits — preventing the client process from hanging after the user types `exit`.

---

## 🛠 Technologies Used

| Technology | Usage |
|------------|-------|
| `java.net.Socket` | TCP client connection |
| `java.net.ServerSocket` | Server listener |
| `java.lang.Thread` | Concurrent client handling |
| `java.lang.Runnable` | Thread task interface |
| `java.io.BufferedReader` | Read incoming data |
| `java.io.PrintWriter` | Send outgoing data |
| `java.util.concurrent.ConcurrentHashMap` | Thread-safe user registry |
| `java.util.Scanner` | Keyboard input |

---

## 📚 Course Info

- **Course:** Network Programming
- **Semester:** 2nd Term 2025/2026
- **Lecturer:** Dr. Aya Ibrahim
- **Department:** Information Technology — 3rd Year
