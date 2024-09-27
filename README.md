# Blockchain
![](https://img.shields.io/badge/Java-Programming-green)
## Description
An educational project focused on understanding how blockchains work. 
Written in vanilla Java 17 utilizing some important language concepts.
It's a functioning blockchain with ability to self modify the difficulty level to prove a block.
Blockchain itself verifies its cohesion and validity of singular blocks in realtime working with multiple miners.
Blocks contains data about transactions between users that hold informations about simulated digital currency.
Each transaction has its digital signature created by it's sender and verified by blockchain using assymetric cryptography.
The simulation part of this project takes care of creating and managing users, miners and transactions.

## Technologies
* Vanilla Java 17
## Concepts Implemented

This project utilizes concepts of:
* Blochchain
* Multithreading
* Hashing
* Asymmetric cryptography
* Scheduled Executors
* Validation

## Installation

As of now, the simulation can only be controlled through parameters located in config/SimulationConfig.java class.
Therefore it's highly recommended to clone the repo and run project in IDE of choice to explore it and experiment with simulation.

However, running simulation with default values is possible with steps below

**1. Clone the repository:**

```
$ git clone https://github.com/Tyall/hs-blockchain.git
$ cd hs-blockchain
```
**2. Compile the code**

Use chosen compiler or built in IDE to compile the source code.

**3. Run the simulation:**

```
$ java src/blockchain/Main.java
```
## Usage

As mentioned earlier, it's recommended to change simulation parameters to observe how does the blockchain perform.
By default, the simulation should output data of first 15 blocks. 

**Example output consisting of two blocks**
```
Block:
Created by: miner9
miner9 gets 100 VC
Id: 1
Timestamp: 1539866031047
Magic number: 76384756
Hash of the previous block:
0
Hash of the block:
1d12cbbb5bfa278734285d261051f5484807120032cf6adcca5b9a3dbf0e7bb3
Block data:
No transactions
Block was generating for 0 seconds
N was increased to 1

Block:
Created by: miner7
miner7 gets 100 VC
Id: 2
Timestamp: 1539866031062
Magic number: 92347234
Hash of the previous block:
1d12cbbb5bfa278734285d261051f5484807120032cf6adcca5b9a3dbf0e7bb3
Hash of the block:
04a6735424357bf9af5a1467f8335e9427af714c0fb138595226d53beca5a05e
Block data:
Andrea12 sent 35 VC to Laura99
Developer1 sent 12 VC to Chainer36
Guest54 sent 92 VC to FastFood55
Block was generating for 0 seconds
N was increased to 2

... (another 13 blocks, so the output contains 15 blocks)
```

## Project status
There are still some improvements to be done.

List of known things ToDo:
* Moving configuration to a file or launch parameters
* Transactions sometimes are not being handled properly depending on simulation values.
* Extended logging
