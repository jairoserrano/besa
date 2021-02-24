PONTIFICIA UNIVERSIDAD JAVERIANA  
DEPARTAMENTO DE INGENIERÍA DE SISTEMAS
BESA: PLATAFORMA DE SISTEMAS MULTIAGENTE

**Como correr el ejemplo**

Para poder hacer uso de este ejemplo y ver el funcionamiento del balanceador de carga, se debe correr primero el jugador3 que es el contenedor MAS3, luego el jugador2 que es el contenedor MAS2 y por ultimo el jugador1 que es el contenedor MAS1, este va a ser el contenedor que va  a contener todos los agentes del sistema y va a empezar sobrecargado. Con el tiempo los agentes del contenedor MAS1 van a ser transladados a los otros contenedores para balancear la carga.

**Manual de usuario** 

Este documento contiene el manual de usuario del balanceador de carga del framework BESA, primero explicando el agente balanceador y cada una de las partes que lo compones mas sus guardas y por ultimo se explica cómo debería ser inicializado para su correcto uso.

**Agente balanceador**

El agente balanceador es un agente BESA desarrollado para ser el encargado de realizar el proceso de balanceo de cargas en los contenedores, este en su estado va a tener los siguientes atributos:

- Long periodicTime: va a ser el tiempo que va a esperar la guarda periódica para volver a ejecutarse, este tiempo va a poder ser dado por el usuario o se podrá usar el tiempo predeterminado.

- Long max: Esta es la carga máxima que debería tener cada contenedor en el sistema, esta también va a poder ser establecida por el usuario o se podrá usar el valor predeterminado, este valor va a determinar cuando se inicia el proceso del balanceo de carga.
 
- Map<String, Long> agentsLoads: Este mapa contiene las cargas de cada agente por separado en los últimos 10 segundos donde la llave del mapa va a ser el alias del agente y el valor va a ser la carga del agente.
 
- Map<String, Long> agentsComunications: Comunicaciones del agente por implemntar.

- Map<String, ArrayList<Long>> threadsIDs: Este mapa va a tener el registro de los hilos que corresponden a cada agente, para saber que agente tiene los hilos y así poder calcular la carga personal de cada agente.
 
- List<String> contenedores: Esta lista se va a utilizar a la hora de realizar el balanceo de carga ya que va a tener los nombres de los contenedores mas prometedores a los cuales puede ser trasladado el agente.


Además de este va a tener dos comportamientos uno el cual va a ser el comportamiento de las solicitudes que se va a encargar tanto de enviar las solicitudes de movimiento de agentes tanto como de recibirlas y enviar una respuesta a las mismas, este comportamiento va a estar compuesto de dos guardas las cuales son:

- Answers_Guard: Esta guarda se encarga de recibir la respuesta de un agente balanceador de otro contenedor con los datos del agente a enviar y si es viable para ese otro contenedor recibir el agente, de ser el caso inicia el proceso de mover el agente a ese contendor, de lo contrario se retira ese contenedor de la lista y se envía al siguiente en la lista.

- Requests_Guard: Esta guarda es la encargada de recibir las solicitudes de balanceo de otros contenedores, recibiendo los datos del agente que se le quiere enviar. Con estos datos evaluara si es conveniente recibir el agente y en base a esto enviara un mensaje de respuesta afirmativo o negativo dependiendo del caso y será el mensaje que reciba la guarda anteriormente mencionada.


El otro comportamiento va a ser el que se va a encargar estrictamente del proceso balanceo que se va a tener las funciones de estar calculando la carga del contenedor, decidir cuando iniciar el proceso de balanceo de carga y por último de enviar la solicitud al contenedor más prometedor, este va a estar compuesto también por dos guardas las cuales son:

- Balancer_Guard: Esta es una guarda periódica que se ejecuta cada cierto tiempo y calcula la carga del contenedor entre ese periodo de tiempo, si este nivel de carga pasa el límite establecido esta inicia el proceso de balanceo de carga y empieza a calcular la carga individual de cada uno de los agentes para determinar cuál agente debería ser trasladado, una vez se sabe que agente debe ser trasladado se procede a determinar la lista con los contenedores a los cuales se va a enviar la solicitud de traslado y por ultimo envía la solicitud al primer contenedor de la lista.

- Update_Guard: Esta guarda se va a encargar una vez le sea comunicado que el proceso de balanceo de carga termino y fue recibido un nuevo agente va a agregar el agente nuevo a los datos del agente balanceador del nuevo contenedor.


**Uso del balanceador de carga.**

El uso del balanceador de carga es bastante simple basta con crear una instancia de BalancerBESA e iniciar el agente balanceador. Este se construirá por debajo y empezara a funcionar de inmediato, es importante saber que este debe ser iniciado una vez se hayan inicializado todos los otros agentes ya que al iniciar el agente balanceador registra los agentes que están presentes en el contenedor al momento de el ser inicializado, si se declara algún agente después de iniciar el balanceador este no será tenido en cuenta.

```
BalancerBESA balanceador = new BalancerBESA();

balanceador.initBalancer();
```


Esta es una alternativa si se quieren dejar los valores predeterminados para el balanceador de tiempo de ejecución de la guarda periódica y de carga máxima que debería tener cada contenedor, si se desea especificar estos parámetros se debe utilizar el otro constructor donde el primer parámetro es el tiempo de ejecución de la guarda periódica, es importante mencionar que la carga del contenedor se mide según los periodos de tiempo definidos para la guarda periódica, es decir si se definió que la guarda periódica se ejecuta cada minuto, la carga que mida el agente va a ser la carga del contenedor en el último minuto, entonces se debe tener esto en cuenta a la hora de definir el máximo de carga que debería tener cada contenedor ya que el valor con el cual se va a comparar va a ser el que se calculo en a guarda periódica que ya mencionamos que depende del tiempo.

```
BalancerBESA balanceador = new BalancerBESA( 60000, 10000000000L);

balanceador.initBalancer();
```


El tiempo que se incluya va a estar en milisegundos como se ve en el ejemplo se puso que fuera cada minuto y el otro parámetro es el uso de CPU máximo que debería hacer el contenedor en ese minuto, este va a estar medido en nanosegundos.

Una vez inicia el balanceador este registra todos los agentes en el contenedor y empieza la ejecución de la guarda periódica para iniciar el proceso de balanceo. Solo los contenedores que tengan un agente balanceador van a poder participar de los procesos de balanceo, no es necesario que todos los contenedores tengan un agente balanceador, pero si es necesario que mas de uno lo tenga para poder llevar a cabo los procesos.

