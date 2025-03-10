# Cargo Maze

## Resumen

**Cargo Maze** es una versión adaptada y colaborativa del clásico juego **Sokoban**. En esta plataforma, múltiples jugadores pueden interactuar y resolver puzzles de manera cooperativa en tiempo real. El objetivo del juego sigue siendo el mismo: mover cajas a posiciones predeterminadas dentro de un laberinto, pero ahora con la colaboración de otros jugadores. Introduciendo elementos adicionales como trampas, vidas y monedas, brindando una experiencia más dinámica, todo dentro de un entorno cooperativo.

## Descripción del Proyecto

### Antecedentes

**Sokoban** es un popular juego de puzzles donde los jugadores deben empujar cajas dentro de un almacén y colocarlas en posiciones específicas. Algunas de las restricciones clave del juego son:

- Las cajas solo se pueden **empujar**, no tirar de ellas.
- El jugador solo puede mover una caja a la vez.
- Un movimiento mal planificado puede bloquear una caja o hacer imposible cumplir el objetivo, lo que implica un desafío estratégico.

En su versión tradicional, **Sokoban** es un juego para un solo jugador. **Cargo Maze** amplía este concepto al agregar un enfoque **colaborativo** en el que dos o más jugadores deben coordinar sus movimientos para resolver los puzzles en conjunto.

### Problema que Resuelve

El principal desafío de **Sokoban** tradicional es que es un juego individual, lo que puede limitar la capacidad de colaboración y resolución conjunta de problemas. En **Cargo Maze**, se introduce un componente social y estratégico: la colaboración en tiempo real entre jugadores, que deben trabajar juntos para planificar y ejecutar sus movimientos sin bloquearse mutuamente.

Adicionalmente, la incorporación de trampas, vidas y monedas introduce un nuevo nivel de dificultad y toma de decisiones en el juego, requiriendo una planificación conjunta más compleja y la capacidad de coordinar acciones simultáneas.

### Objetivo del Proyecto

El objetivo de **Cargo Maze** es crear una experiencia de puzzle colaborativo donde los jugadores puedan:

- Coordinarse para empujar las cajas a sus posiciones con la inclusión de trampas, vidas y monedas aumenta la complejidad del juego, haciendo cada nivel más desafiante.
- Resolver los desafíos en tiempo real, optimizando la estrategia conjunta.
- Mantener la esencia del reto original del juego, pero adaptado a un entorno cooperativo.
- Competir entre ellos para recolectar monedas, lo que introduce un aspecto de recompensa y riesgo adicional al juego.
- Gestionar la concurrencia entre jugadores en tiempo real, con interacciones como mover cajas, evitar trampas, recolectar monedas, y usar vidas.

## Arquitectura del Proyecto

### Tecnologías Utilizadas

- **Backend**: El proyecto utilizará **Spring Boot** como el framework principal para la gestión del backend, permitiendo un desarrollo rápido y estructurado.
- **Frontend**: Se emplearán tecnologías web para la visualización y control en tiempo real de los movimientos y el estado del tablero.
- **Maven**: Se utiliza **Maven** para gestionar las dependencias y la estructura del proyecto, lo que facilita la integración y el desarrollo modular.

### Patrones de Diseño

- **Inyección de Dependencias**: Para garantizar que los componentes del sistema estén desacoplados y más fáciles de probar, se utiliza inyección de dependencias en el diseño de la aplicación.
- **Inversión de Control**: A través de Spring Boot, se facilita la inversión de control, permitiendo que el flujo de la aplicación esté dirigido por el framework en lugar del código de la lógica de negocio.
- **Concurrencia**: Para manejar interacciones simultáneas entre jugadores sin conflictos, especialmente al mover cajas, interactuar con trampas y recolectar monedas en tiempo real.

### Diagrama de Arquitectura

**General**

**Ejemplo de Diseño del Tablero (Sokoban)**

## Funcionalidades Clave (Features)

### 1. Gestión de Usuarios

- **Registro y Autenticación**: Los jugadores pueden registrarse y autenticarse para jugar. El sistema maneja perfiles de jugadores con identificadores únicos como nicknames.

### 2. Juego en el Tablero

- **Visualización en Tiempo Real**: Los jugadores pueden ver el estado del tablero, los movimientos de los otros jugadores y las interacciones con las cajas en tiempo real.
- **Movimiento de Jugadores**: Los jugadores se desplazan por el tablero, empujando cajas y coordinando sus acciones.
- **Interacción con Cajas**: Los jugadores colaboran para mover las cajas a las posiciones correctas.
- **Interacción con Paredes**: Las paredes definen los límites del juego, obligando a los jugadores a planificar sus rutas estratégicamente.

### 3. Elementos Dinámicos (Trampas, Vidas y Monedas)

- **Trampas**: Los niveles contienen trampas que aumentan la dificultad. Los jugadores deben evitarlas para no perder vidas o tener que reiniciar el nivel.
- **Vidas**: Cada jugador cuenta con un número limitado de vidas para completar los niveles. Si un jugador pierde todas sus vidas, debe esperar hasta que los demás jugadores completen el nivel o que alguien recolecte monedas suficientes para otorgar una vida extra.
- **Monedas**: Las monedas están dispersas por el tablero y pueden ser recolectadas por los jugadores. Las monedas sirven como elemento competitivo, permitiendo a los jugadores ganar recompensas y otorgar vidas adicionales a otros miembros del equipo.

### 4. Gestión de Sesiones

- **Reinicio del Nivel**: Los jugadores pueden reiniciar un nivel si se quedan atascados.
- **Guardado de Progreso**: El sistema permite guardar y continuar partidas en sesiones posteriores.

### 5. Salas Cooperativas

- **Creación de Salas**: Los jugadores pueden crear salas cooperativas para invitar a otros jugadores a participar en un nivel de manera colaborativa.
- **Acceso a Salas**: Los jugadores pueden unirse a salas cooperativas existentes para jugar en tiempo real con otros.

## Concurrencia

Uno de los principales retos y requisitos del proyecto es gestionar la concurrencia de múltiples jugadores en tiempo real, asegurando que:

- Todos los movimientos, interacciones con cajas, trampas, y recolección de monedas estén sincronizados.
- No se generen conflictos entre los jugadores (por ejemplo, dos jugadores empujando la misma caja al mismo tiempo).
- Las vidas y monedas se gestionen de forma consistente para todos los jugadores en la sala.

## Integrantes

- Ana Maria Duran
- Milton Andrés Gutierrez
