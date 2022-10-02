# WY Space exercise

# Content
- [Task description](#user-story-1701)
- [assumptions / definitions](#assumptions--definitions)
- [usage](#usage)
- [development](#development)
- [mini backlog](#mini-backlog)

### Intro

Your company has taken on a new project with WY Space and you have grabbed a user story (1701) related to ground station communications! Your task is to understand and implement a solution.

Document any assumptions you make as well as any thoughts on your solution.

### User Story 1701

WY Space has a fleet of satellites, and a ground station to communicate with them. Each satellite has a downlink rate measured in units per 30 minutes. The ground station has a maximum bandwidth that it can handle at any one time; it can handle multiple satellites in parallel.

A satellite can only downlink data when the ground station can see it, this window is called a pass.

When the pass begins, the connection and downlink to the ground station is immediate, you may assume there are no delays. Similarly, the downlink will immediately stop when the pass ends. All passes are a minimum of 30 minutes.

**QUESTIONS:**

- WY Space would like to take a text based schedule (detailed below) and use a program that can find the 30 minutes period where the total downlink (all satellite passes) will be at its maximum.
- Furthermore, they would like the program to determine if the ground station has the bandwidth to support this.

Since WY Space want to use your solution for multiple ground stations, the bandwidth of the ground station should be provided as an argument to the program.

**Attached File: pass-schedule.txt**

This is a text based schedule WY Space expect to use with your program.

**The format is as follows:**

Each line represents a single pass. A pass contains the satellite name, it’s bandwidth per 30 minutes period, the start time of the pass, and the end time of the pass. The four elements of a pass are comma separated.

### Notes to the candidate

Include a README text file detailing how we run your solution. The README can be in a format of your choice, e.g. plaintext, markdown, Word document etc.

## ASSUMPTIONS / DEFINITIONS
- pass of a satellite start and end at exactly at the beginning of a minute, e.i. seconds, milliseconds etc. are always 0
- **start times are inclusive while end times are exclusive**
  - e.g. satellite A is broadcasting at 8:30, 8:31, 8:32 ... 9:28, 9:29 but not at 9:30
- **total downlink** at any moment is the sum of the strength of all currently passing satellites
  - e.g. total downlink at  8:29 is 2 while 8:30 is 7
- **value of 30 minutes of total downlink** is the sum of total downlink in all minutes in that 30 minutes window
  - e.g. value of 30 minutes of total downlink from 7:45 to 8:15 is: 135, which can be calculated the following way:
    - total downlink from 7:45 to 8:00 is 2 in every minute, 15 * 2 = 30 overall
    - total downlink from 8:00 to 8:15 is 7 in every minute, 15 * 7 = 105 overall
    - therefore value of 30 minutes of total downlink is 135 (105 + 30)
- **period of maximum total downlink** 
  - is the 30 minutes where the total downlink will be at maximum, e.i. no other 30 minutes window have greater "overall strength"
  - its marked as a single number, saying which minute is it, counting from midnight
    - e.g. 0 is 00:00
    - e.g. 62 is 01:02
- **bandwidth of a ground station** supports 30 minutes of total downlink if in every minute of that 30 minutes the total downlink is less or equal than the bandwidth. 


| NAME | STRENGTH | START TIME | END TIME |
|------|----------|------------|----------|
| A    | 2        | 07:30      | 08:30    |
| B    | 5        | 08:00      | 09:00    |

## USAGE

* Available: https://us-central1-wy-space.cloudfunctions.net/wy-space
* Example usage: 
```shell
curl -F "file=@./src/integration/resources/pass-schedule.txt"  https://us-central1-wy-space.cloudfunctions.net/wy-space\?bandwidth\=13
```
* Header _Content-Type_ must include _multipart/form-data_
* file is mandatory parameter
* bandwidth is optional parameter
* lines of the file should be: name,strength/bandwidth,start-time,end-time where:
  * **name** is a string containing the name of the satellite
  * **strength/bandwidth** per 30 minutes period it has to be a positive natural number
  * **start-time** is the start time of the pass, it follows HH:mm pattern
  * **end-time** is the end time of the pass, it follows HH:mm pattern
  * all fields should be separated by comma without any space

## DEVELOPMENT

- requirements for local development:
  - docker
  - java 17
- requirements for deploying to google cloud as a function
  - google cloud account
  - gcloud
- steps to manually deploy it google cloud platform
  1. `gcloud auth login`
  1. `./gradlew build`
  1. ```shell
     gcloud functions deploy wy-space --entry-point=io.quarkus.gcp.functions.QuarkusHttpFunction \
     --trigger-http --runtime=java11 --source=build --allow-unauthenticated
     ```

## MINI BACKLOG

Sub-tasks of user story 1701:

- [x] **1702**: create dummy project
  - Acceptance criteria:
    - hello-world on gradle and git
- [X] **1703**: add CI
  - Acceptance criteria:
    - successful run on github CI
- [X] **1704**: find the total downlink maximum if **all satellites start there pass on either x:00 or x:30**
  - Acceptance criteria:
    - the total downlink maximum of [pass-schedule.txt](src/integration/resources/pass-schedule.txt) should be 20, where 20 means the 20th half hour of the day e.i. 10:00 to 10:30.
- [X] **1705**: find the total downlink maximum if **all satellites can start there pass at any minutes**, e.g. 00:12
  - Acceptance criteria:
    - the total downlink maximum of [pass-schedule-any-minute.txt](src/integration/resources/pass-schedule-any-minute.txt) should be 602, where 602 means the start time of the period is the 602nd minutes of the day e.i. 10:02 to 10:32.
- [X] **1706**: find the total downlink maximum even if **a satellite changes its speed** a.k.a. pass won't be periodic.
  - Acceptance criteria:
    - the total downlink maximum of [pass-schedule-with-changing-speed.txt](src/integration/resources/pass-schedule-with-changing-speed.txt) should be 151, where 151 means the start time of the period is the 151st minutes of the day e.i. 02:31 to 03:01.
- [X] **1707**: determine if **ONE** ground station has the bandwidth to support the total downlink maximum.
- [X] ~~**1708**: determine if **MULTIPLE** ground stations have the bandwidth to support the total downlink maximum.~~
  -  out of scope due after rereading the description: the bandwidth of **the** ground station should be provided as an argument to the program
- [X] **1709**: package as a ~~microservice~~ function
- [X] **1710**: add CD
- [X] **1711**: fix out of memory error in case of reading too large files
  - Acceptance criteria:
    - reading large files should not throw out of memory exception
- [X] **1712**: upgrade to java 17
