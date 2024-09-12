# Parcial

![Parcial](https://github.com/user-attachments/assets/9422fa5c-2e70-4dba-add1-df70846b7f29)

## Descripción General
La tarea de construir una aplicación web que realiza cálculos matemáticos y ordena listas numéricas utilizando el algoritmo de Bubble Sort ha sido implementada con una arquitectura de tres componentes distribuidos. Esta arquitectura está compuesta por:

- Fachada de Servicios (HttpServerFachada)
- Servicio de Cálculo (HttpServerCalculator)
- Cliente Web (HTML + JavaScript)

Estos componentes están desplegados en máquinas virtuales separadas, y la comunicación entre ellos se realiza mediante HTTP con respuestas en formato JSON.

## Componentes de la Arquitectura

- Fachada de Servicios (HttpServerFachada)

La fachada de servicios actúa como el punto de entrada para el cliente web. Su responsabilidad principal es servir la interfaz de usuario (HTML + JavaScript) y delegar las solicitudes de cálculo al servicio de cálculo. Implementa dos rutas principales:

- /calculadora: Sirve una página web que permite al usuario ingresar comandos matemáticos. La página contiene un formulario para ingresar comandos y un botón para enviar estos comandos al servidor.
 -/computar: Procesa las solicitudes de cálculo recibidas del cliente web y las redirige al servicio de cálculo (HttpServerCalculator). Recibe el comando del cliente, lo envía al servidor de cálculo y retorna el resultado en formato JSON.


## Servicio de Cálculo (HttpServerCalculator)

El servicio de cálculo es el núcleo de la aplicación que realiza las operaciones matemáticas solicitadas y procesa el algoritmo de Bubble Sort. Utiliza reflexión para invocar métodos de la clase Math para realizar cálculos matemáticos y procesa el comando bbl para ordenar listas. Implementa las siguientes funcionalidades:

Operaciones Matemáticas: Suma, resta, multiplicación, y división de números, así como el cálculo de funciones matemáticas (sin, cos, tan, asin) mediante reflexión.
Bubble Sort: Ordena una lista de números en el formato bbl(...).
Decodificación de Comandos: Los comandos recibidos son decodificados y procesados para ejecutar la operación adecuada.
Cliente Web (HTML + JavaScript)

El cliente web se encarga de proporcionar una interfaz de usuario interactiva para la entrada de comandos matemáticos. Utiliza HTML para estructurar la página y JavaScript para manejar las solicitudes asíncronas a la fachada de servicios:

Interfaz de Usuario: Una página HTML con un campo de texto para ingresar comandos y un botón para enviar los comandos al servidor.
Solicitudes Asíncronas: Utiliza XMLHttpRequest para enviar comandos al servidor de cálculo y actualizar la página con el resultado sin necesidad de recargarla.
