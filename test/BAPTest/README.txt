**********************************************
* Grupo Investigaci�n SIDRe.                 *
* Departamento de Sistemas.                  *
* Facultad de  Ingenier�a.                   *
* Pontificia Universidad Javeriana.          *
* Bogot�, 15 de octubre de 2011.             *
**********************************************

Un cordial saludo,

La prueba consiste en el envi�  de un evento (a modo de �hola�) de un agente �A� a un agente �B�. La prueba consta de tres contenedores y un BAPLocator para establecer un entorno de interoperabilidad entre dos barrios. Los contenedores �BAPC� y �MAS_2� conforman un barrio por estar en el mismo segmento multidifusi�n; el agente �B� est� en el contenedor  �MAS_2�, la idea es iniciar el contenedor  �BAPC�  antes de �MAS_2� para que este despliegue el BAP correspondiente al barrio.El otro  barrio corresponde al creado por el contenedor �MAS_1� ya que este se encuentra en un segmento multidifusi�n diferente a los que se encuentran �BAPC� y �MAS_2�. En el contenedor �MAS_1� est� el agente �A� quien le env�a el evento al agente �B�.

-----------------------
- Ejecutar la prueba: -
-----------------------
1.	Lance el main del BAPLocator.
2.	Lance BAPMain.
3.	Lance AgentBMain.
4.	Lance AgentAMain.

-----------------------------
- El resultado esperado es: -
-----------------------------
-	En la consola AgentAMain debe salir: The agent A is saying hello to extent agent B...
-	En la consola AgentBMain debe salir: The agent BAPTestPeer recieves: Hello[0].


________________________
Gracias por su atenci�n.
 