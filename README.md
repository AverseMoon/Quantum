# Quantum - Very modular Java library!

Quantum is a simple library designed to load jars (or what is internally called Modules).

If you run a compiled jar of Quantum, it will create a folder (./modules) where you can place compiled modules.

For example code go see [/ExampleModule/](https://github.com/AverseMoon/Quantum/tree/main/ExampleModule)

## Usage Tutorial:
### You will need:
- A Quantum jar release, I will be using [Quantum-1.0-BETA.jar](https://github.com/AverseMoon/Quantum/releases/tag/v1.0-beta)
- One or more modules, I will be using [ExampleModule-1.0.jar](https://github.com/AverseMoon/Quantum/releases/tag/examplemodule-v1.0) (PS. Make sure the module is compatible with your Quantum version)
- Java 17 or greater (older versions may work, I haven't tested them though)
- A computer/laptop (not a phone or tablet), I will be using Windows 11 but the steps should be similar for Linux and Mac.

### Step 0: Make sure you have the neccesary files
![Quantum-1.0-BETA.jar and ExampleModule-1.0.jar in the same folder together](https://github.com/AverseMoon/Quantum/blob/e074202fcd134bb7e0c517dd28df7f4eb3000165/media/tutorial0.png)

### Step 1: Execute the Quantum jar
![Double clicking on Quantum-1.0-BETA.jar](https://github.com/AverseMoon/Quantum/blob/e074202fcd134bb7e0c517dd28df7f4eb3000165/media/tutorial1.png)

*After a few seconds, a new folder named `modules` should pop up.*

### Step 2: Move your modules into the `modules` folder
![Moving ExampleModule-1.0.jar into the modules folder](https://github.com/AverseMoon/Quantum/blob/e074202fcd134bb7e0c517dd28df7f4eb3000165/media/tutorial2.png)

Setup is complete now!

### To run your modules:
Just run the Quantum jar again!
NOTE: To see the console/output you need to run it from the Command Prompt / Terminal using `java -jar Quantum-1.0-BETA.jar`
