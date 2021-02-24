PONTIFICIA UNIVERSIDAD JAVERIANA  
DEPARTAMENTO DE INGENIERÍA DE SISTEMAS
BESA: PLATAFORMA DE SISTEMAS MULTIAGENTE


# Descripción de Contenido Repositorio

El repositorio está organizado en las siguientes cinco carpetas:
-	dist: Carpeta en donde se alojan las distribuciones de la plataforma BESA organizadas por años.
-	doc: Carpeta en donde se aloja la documentación relacionada con el diseño arquitectónico de la plataforma y una presentación a modo de manual de usuario desarrollador.
-	exam: Carpeta en donde se alojan ejemplos de uso de la plataforma.
-	src: Carpeta en donde se alojan los archivos fuente de la plataforma BESA.
-	test: Carpeta en donde se alojan proyectos de prueba de concepto de funcionalidades experimentales de la plataforma BESA.


# Instalación
- Para usar BESA es tan sencillo como importar unas librerías al proyecto en el que estamos trabajando. La librerías de BESA se encuentran en la carpeta ‘dist’. Se recomienda usar la última distribución identificada por la fecha.

- Si el proyecto es local: al proyecto hay que importar BESAKernel.jar y BESALocal.jar.

- Si el proyecto es distribuido en el mismo segmento multicast: al proyecto hay que importar BESAKernel.jar, BESALocal.jar y BESARemote.jar.

- Si el proyecto es distribuido pero no el mismo segmento multicast: al proyecto hay que importar BESAKernel.jar, BESALocal.jar, BESARemote.jar y BESAExtern. En este caso se requiere desplegar un componente adicional que es el BAPLocator quien media la comunicación entre redes. 

# Requerimientos

JDK 8

# Get Started

Ver carpeta exam proyecto HolaMundo.

