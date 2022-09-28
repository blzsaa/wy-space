# WY Space exercise

## TASK

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

## MINI BACKLOG

Sub-tasks of user story 1701:

- [x] **1702**: create dummy project
  - Acceptance criteria:
    - hello-world on gradle and git
- [ ] **1703**: add CI
  - Acceptance criteria:
    - successful run on github CI
- [ ] **1704**: find the total downlink maximum if **all satellites start there pass on either x:00 or x:30**
- [ ] **1705**: find the total downlink maximum if **all satellites can start there pass at any minutes**, e.g. 00:12
- [ ] **1706**: find the total downlink maximum even if **a satellite changes its speed** a.k.a. pass won't be periodic.
- [ ] **1707**: determine if **ONE** ground station has the bandwidth to support the total downlink maximum.
- [ ] **1708**: determine if **MULTIPLE** ground stations have the bandwidth to support the total downlink maximum.
- [ ] **1709**: package as a microservice
- [ ] **1710**: add CD