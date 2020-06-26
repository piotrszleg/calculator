![](https://i.ibb.co/MsCFLQG/calculator.gif)

# Features

- GUI with two panels: standard calculator front and functions and constants list
- Interpreter able to parse 
  - prefix and binary operators 
  - parentheses
  - function calls
  - constants
  - incomplete expressions

  (see `Tests.java` for all supported features)

# Requirements
Java compiler and virtual machine (it was tested using openjdk 11.0.5).

# How to run
On Unix-compatible environments with `javac` and `java` available as commands 
simply type `./run` in the terminal to run the GUI application and `./test` to run tests.

If you modified `FunctionsList.template` or `FunctionsListGenerator.java` type in `./generate` to rebuild the `FunctionsList.java` file.

On other environments try creating a project in your IDE of choice inside of this folder and run `main` methods in `Main.java`, `Tests.java` and `FunctionsListGenerator.java` respectively.
