# Flash-Separation
Flash-Separation simulator



This tool is a JAVA program that calculates the composition of the output streams of a flash distillation. It was written as a project for a chemical engineering course. The program uses the Virial equation of state to calculate the fugacity in the nonideal case, and the Rachford-Rice equation to determine the vapor-liquid equilibrium data (K-values) in the ideal case.

The program is able to perform the following calculations:

1- Given the tank pressure, a specified constant operating temperature, the feed composition and flowrate, it will calculate the vapour and liquid product compositions and flowrates, as well as the heat required to maintain the operating temperature as specified.

2- Given the tank pressure, the feed temperature, the feed composition and flowrate, it will perform an energy balance to  calculate the adiabatic flash temperature of the mixture and calculate the vapour and liquid product compositions and flowrates of the adiabatic flash.


3- Given the tank pressure, the feed composition and flowrate, it will optimize the operating temperature to maximize the separation of a specified component in the vapour or liquid product streams.

4- The user can choose between ideal and non-ideal options to determine the vapor-liquid equilibrium data (K-values) and  Ridders method is used as numerical method to solve equations. The program also performs bubble and dew point calculations to confirm that the flash separation is possible.

The species mixture considered when testing and validating the program consists of ethane, pentane, hexane, cyclohexane, water, and (non-condensable) nitrogen. However, the program is designed to be able to handle any number and type of chemical species.

The program is robust, generic, and extensible; it employs numerous aspects of object-oriented programming, including polymorphism, inheritance, interfaces, cloning, exception handling, and file I/O.
