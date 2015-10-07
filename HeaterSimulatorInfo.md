

# Introduction #

Heater simulator is an application for simulating the temperature evolution of a virtual heating element. The temperature of the virtual heater is measured by a virtual thermistor. The power applied to the heating element can be controlled by various controller algorithms, which try to maintain a certain target temperature. The application can be used to compare the performance of the algorithms or to optimize their parameters.

The application simulates three essential components:
  * The **heater** itself (the heating element)
  * The **thermistor** for measuring the heater's temperature
  * The **environment** towards which the heater looses heat

Different **algorithms** can be selected for controlling the power applied to the heating element. These algorithms try to maintain a constant heater temperature.

# Simulated components #

## The heater ##

The heating element can be heated up by applying power to it. It cools down by dissipating heat to the environment.

The temperature rise of the heater depends on the amount of power applied and on its thermal mass (cfr. [Thermal mass](http://en.wikipedia.org/wiki/Thermal_mass)). The temperature drop due to dissipation depends on the heat transfer coefficient of the heater to the environment and on the difference between the heater's temperature and the environment temperature (cfr. [Newton's law of cooling](http://en.wikipedia.org/wiki/Convective_heat_transfer#Equations_.28Newton.27s_law_of_cooling.29)).

Thus, the heater's behavior depends on four parameters:
  * The _power_ applied to it
  * Its _thermal mass_
  * The _heat transfer coefficient_ to the environment
  * The _environment_ temperature


## The thermistor ##

The application simulates the following circuit, which measures a heater's  temperature using an NTC thermistor connected to a 10-bit ADC:

```
     Vref_________
                _|_
               |   |
               |R2 |
               |___|
                 |______________V (to ADC)
                _|_     |
               |the|   _|_
               |rmi|   ___C1
               |sto|    |
               |_r_|    |
    GND__________|______|
```

The thermistor's resistance decreases as its temperature goes up. However, the resistance normally is not a linear function of the temperature. The relation between the thermistor's resistance and its temperature can be approximated based on three parameters: its resistance at a specific temperature (r<sub>0</sub> @ t<sub>0</sub>) and its beta-value (cfr. [Thermistor B parameter equation](http://en.wikipedia.org/wiki/Thermistor#B_parameter_equation)).

When heating a copper block by a resistor, the heat will eventually spread out over the entire copper block. However, this process needs some time, so it might take a few seconds before the heat generated by the resistor reaches the thermistor. Thus, there is a lag between the real temperature and the temperature measured by the thermistor. This lag is included in the simulation.

Furthermore, the analog-to-digital conversion is not 100% accurate and can be under the influence of noise. This noise is also included in the simulation.

Thus, the thermistor's behaviour depends on these parameters:
  * Its _r<sub>0</sub>_, _t<sub>0</sub>_ and _beta_ parameters
  * The value of resistor _r<sub>2</sub>_ in the above circuit
  * The _lag_ between the temperature of the heating element and the thermistor's temperature
  * The amount of _noise_ influencing the ADC

## The environment ##

The environment is modeled as an object with constant temperature (infinite thermal mass). Its temperature has an influence on the amount of heat dissipated from the heater to the environment.

Thus, the environment depends only on the following parameter:
  * Its constant _temperature_

# Available algorithms #

## PID32 ##

The PID32 algorithm simulates a proportional–integral–derivative controller (cfr. [PID](http://en.wikipedia.org/wiki/PID_controller)) using only 32-bit integer variables and operations. The specific algorithm is based on the [Arduino Bare Bones (PID) Coffee Controller](http://www.arduino.cc/playground/Main/BarebonesPIDForEspresso). It uses a very simple method for preventing [integral windup](http://en.wikipedia.org/wiki/Integral_windup): it prevents the integral term from accumulating above or below pre-determined bounds.

This algorithm uses five (32-bit integer) parameters:
  * The gain of the _proportional_ component
  * The gain of the _integra\_l component
  * The gain of the_derivative_component
  * The limits of the integral component (to prevent_integral windup_)
  * A_divisor_for dividing the output of the algorithm to obtain a useful output value_

## CAAPID32 ##

The CAAPID32 algorithm is a Controllable Area Aware PID algorithm, using only 32-bit integer variables and operations. The only difference with the PID32 algorithm is the way it handles integral windup: instead of limiting the integral term from accumulating above or below pre-determined bounds, the integral term is used only when the controller is in its controllable area. This is the area in which the output is not saturated (i.e. when the heater does not need to use its maximum output power).

This algorithm uses four (32-bit integer) parameters:
  * The gain of the _proportional_ component
  * The gain of the _integral_ component
  * The gain of the _derivative_ component
  * A _divisor_ for dividing the output of the algorithm to obtain a useful output value

## CAAPID16 ##

The CAAPID16 algorithm is based on the CAAPID32 algorithm, but uses only 16-bit integer variables and operations. This variant of the algorithm is better suited for use in simple microcontrollers.

The algorithm uses four (16-bit integer) parameters:
  * The gain of the _proportional_ component
  * The gain of the _integral_ component
  * The gain of the _derivative_ component
  * A _divisor_ for dividing the output of the algorithm to obtain a useful output value