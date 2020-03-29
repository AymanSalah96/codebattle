# CodeBattle

> Web platform to practice problem solving, algorithms and data structures.

The main focus of this platform is to force participants to use best practices while solving programming problems/challenges to get the most outcome from their training.

### The problem:

Many competitive programmers follow wrong ways/happites while solving problems and the most two common things are:
1. **NOT** taking enough time checking/testing correctness of their solutions and submit their solution once they got a working idea from the first glance.
2. **NOT** focusing on a problems set but instead distracting themselves by looking regularly on the standings and what others are doing, and this my lead to some frustration during a contest.

### The solution:

1. Limiting number of submissions per problem in a contest (max 2) to let participants think more about correctness of a solution before submit, otherwise submission for tried problem will be closed and getting penalty.
2. Standings are published after the end of a contest to just compete against a problems set and focus on them only.

**NOTE: This project is currently in progress and planning to be published to the world soon...**

### Features:
* Login
* Register
* User profile
* Problems
* Contests
* Standings
* Solution tutorial (in progress...)

#### Admins only can create problems and contests.
  1. Problem creation:
      * Problem name.
      * Main Description (story) section.
      * Input description section.
      * Output description section.
      * Additional notes section.
      * Upload sample/test cases files (currently just one sample and one test files are supported).
      * Choosing file comparing checker type.
        * Line Checker.
        * Token Sequence Checker.
        * ... planning to allow for customized checkers.
  2. Contest creation:
      * Contest name.
      * Adding created problems to contest.
      * Start date.
      * Duration in minutes.
      * Contest privaciy (public or private).  NOTE: private is not supported currently but it's in the plan.

#### A participant can:
  1. Show all upcoming/running/archieved contests.
  2. Register in a contest.
  3. Submit a problem.
  4. Show standings after a contest ends immediatly.
  5. Show problems solution tutorial after a contest ends immediatly.

#### Problem submission:
  * Currently it's just output files comparing.
  * Could make it submit source code in the future.
  * Submission steps:
    1. Download input file.
    2. Upload output file.
    3. Recieving judgement verdict.

### Standings:
  * Ranking criteria is based on the ICPC style.

### Technologies used:
  * Java
  * Spring (MVC, Data JPA, Security)
  * MySQL
  * Hibernate
  * Thymeleaf
  * Bootstrap
  * JavaScript
  * JQuery
  * MathJax
  * IntelliJ IDEA

#### If you have any idea, or want to contribute to this project please don't hesitate for that and it will be so appreciated.
