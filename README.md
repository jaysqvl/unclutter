# Unclutter
## What is it?
A java to-do list application inspired by the features of google calendar and the reminders app in iOS.

Unclutter is a task manager with the additional (optional but recommended) metrics for sorting your important tasks.

![Unclutter](https://raw.githubusercontent.com/jsqvl/To-do-list/main/data/logo.gif)

## Instructions
OPTION 1:
1. Navigate to the projects jar build directory (or build it yourself) "out/artifacts/main.jar"
2. Open a terminal here
3. Run the following command
    * $ java -jar main.jar

OPTION 2:
1. Open the project in your preferred Java IDE
2. Navigate to the project directory "src/main/ui/main.java"
3. Run from this file

## Challenges and Reflection
* Time management
  * As my first solo-dive into application development, this project helped me better understand managing and estimating
    feature implementation time
* Project Structure
  * Structuring my code in the model-view-controller inspired format with the additional persistence module was new to
  me
  * Having to consciously separate and section my codes for both organizational and efficiency purposes was new to
  me due to not having created projects of this size before
* Test Suite
  * Adding a test suite for my model and persistence packages while seemingly time-consuming, allowed me to progress
  at a steady rate
  * At the time it was hard to appreciate, however, the test suite allowed me to verify, observe, and adjust the
  behaviour of all of my functions and packages during the implementation process
  * Writing tests before the implementation delineated the edge cases in advance

## Accomplishments
This project successfully incorporates many of CPSC210's (UBC's Software Construction Course) design and development 
principles. From the test-suite and software construction patterns to UML Design. 

## Recommended Uses Cases
* Studying
  * Create a list for homework automatically sorted by date or grade weighted importance
* Daily life
  * Manage your errands
  * Shopping List
* Simple List Manager
  * Insert items without a due date or importance

## Features:
* Multiple to-do lists to keep track of different categories
* Mark a task as complete but not necessarily delete it
* Add, remove and shuffle tasks in terms of importance
* Sort tasks based on completion
* Importance weight to items in the todo list where it adds the task based on the importance on a scale of 1 to 10
* Due date to tasks but not necessary to have one
* Save my to-do lists to a file
* Open to-do lists from a file (read somebody elses list or open your own on another device)

*To Do App Sample Event Log*

> Fri Nov 26 06:52:15 PST 2021
New List Inserted At Index: 0


> Fri Nov 26 06:52:15 PST 2021
Task inserted in List:  Homework @ Index: 0


>Fri Nov 26 06:52:15 PST 2021
New List Inserted At Index: 1


>Fri Nov 26 06:52:15 PST 2021
New List Inserted At Index: 2


>Fri Nov 26 06:52:30 PST 2021
Task Inserted in List: Homework @ Index: 0


>Fri Nov 26 06:52:46 PST 2021
Modified task from: 'test' to 'Finish testing'


>Fri Nov 26 06:52:46 PST 2021
Modified task date from: '2021/11/26' to ''


>Fri Nov 26 06:52:46 PST 2021
Modified task importance from: '4' to '3'
