# Railway & Road Traffic Simulation

![Java](https://img.shields.io/badge/Java-16%2B-orange) ![Swing](https://img.shields.io/badge/GUI-Swing-blue) ![Concurrency](https://img.shields.io/badge/Concurrency-Multithreaded-green) ![Project](https://img.shields.io/badge/Project-University-blueviolet)

A **university project developed in Java**, simulating concurrent railway and road traffic on a **30×30 grid**.

Each train, car and truck runs in its **own thread**, with **no central movement scheduler**. Railway stations coordinate access to single-track segments, while synchronization mechanisms manage shared resources, opposing traffic and level-crossing barriers. The project explores **multithreading, synchronization, race-condition challenges, resource contention and decentralized coordination**.

Beyond the traffic simulation, it includes a **Java Swing GUI**, live configuration updates, train composition validation, routing and serialized movement history.

**▶ [Watch the simulation - trafficSimulation.mp4](trafficSimulation.mp4)**

![Railway and road network](data/TRAFFIC%20SIMULATION/map.png)

## Highlights

- **One thread per moving actor.** Train compositions (`Composition`) and road vehicles (`Vehicle`) execute independently, with no predetermined execution order.
- **Synchronization and resource contention.** `CountDownLatch`, `await()`, `countDown()`, `synchronized` methods and shared occupancy data coordinate access to railway segments, crossings and map cells.
- **Decentralized railway control.** Trains know their destination but obtain routing and movement information from railway stations; trains do not communicate directly with one another.
- **Single-track coordination.** Opposing trains wait for an occupied segment to become available. Trains moving in the same direction can follow one another at adjusted speeds.
- **Level-crossing control.** Cars and trucks wait at crossings while trains pass. Barriers are released after the composition clears the crossing, with additional timing for electric locomotives.
- **Live updates without restarting.** Java NIO `WatchService` detects new train files and changes to road traffic settings during execution.
- **Train modeling and validation.** Multiple locomotive, engine and wagon types, composition compatibility checks and configurable routes and speeds.
- **GUI and persistence.** A Swing map displays moving actors; completed train journeys record visited positions and travel time using Java serialization.

## Concurrency and architecture

The simulation models multiple independent actors competing for a limited set of shared resources: track segments, level crossings and grid cells. Coordination happens through station logic and synchronization primitives rather than a single thread controlling every movement.

| Responsibility | Main mechanism |
| --- | --- |
| Concurrent trains and road vehicles | `Thread` subclasses (`Composition`, `Vehicle`) |
| Track occupancy and opposing directions | `CountDownLatch` and per-direction segment state |
| Level-crossing barriers | Latches; road vehicles block with `await()` |
| Shared station operations | `synchronized` methods |
| Road and railway occupancy | Shared 30×30 grid |
| Cached route/crossing information | `ConcurrentHashMap` in selected components |
| New train files and live configuration | Java NIO `WatchService` |
| Completed journey history | Serializable `Trajectory` objects |

**Example interactions:** a train waits at a station when another train is travelling toward it on the same single-track segment; road vehicles queue at a closed crossing and resume when it is released; new trains can be introduced by placing definition files into a watched directory.

## Run

Requires **JDK 16+** with desktop GUI support. From the repository root:

```bash
javac -d bin $(find src -name "*.java")
java -cp bin org.unibl.etf.pj2.Simulation
```

The application provides controls to **add a train**, **start the simulation** and **view movement history**. It reads settings from `configuration.txt`; new train definitions can be added to `data/TRAINS/` while the simulation is running.

A prepared scenario is also available if the `demo/` directory is included:

```bash
./demo/run-demo.sh
```

It demonstrates opposing trains, same-direction movement and vehicle queues at level crossings. You can see the result in the [recorded demo](trafficSimulation.mp4).

## Project structure

```text
src/org/unibl/etf/pj2/
├── composition/       Train threads, locomotives, wagons, validation, trajectories
├── vehicle/           Car/truck threads and live vehicle configuration
├── railwayStation/    Routing, segment coordination and crossing control
├── coordinates/       Track and road coordinates
├── display/           Swing interface and map rendering
└── Simulation.java    Simulation entry point and shared state

data/                  Map assets, routes, configuration data and train files
TestTrain/             Example train definitions
demo/                  Prepared demo scenario (if included)
trafficSimulation.mp4  Recorded simulation
```

## Project notes

This is a **university coursework project**, presented as a demonstration of Java OOP, concurrent execution, synchronization and simulation of shared resources. It is not intended for real railway control systems. As in many educational concurrency projects, the shared occupancy grid and some shared state would benefit from stronger atomicity guarantees and dedicated concurrency tests.