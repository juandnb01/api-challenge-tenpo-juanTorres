# API REST - Challenge Tenpo

Esta aplicación es una API REST desarrollada en Spring Boot (Java 21) que implementa las siguientes funcionalidades:

- **Cálculo con porcentaje dinámico:** Recibe dos números, los suma y aplica un porcentaje adicional obtenido de un servicio externo (o de la caché).
- **Caché del porcentaje:** Almacena el porcentaje en memoria durante 30 minutos y lo utiliza en caso de fallo del servicio externo.
- **Historial de llamadas:** Registra de forma asíncrona las llamadas a la API, incluyendo fecha, endpoint, parámetros y respuesta/error. Se recomienda la paginación en entornos con muchos registros.

## Tecnologías utilizadas

- Java 21
- Spring Boot
- Maven
- Docker / Docker Compose
- PostgreSQL

## Estructura del Proyecto

- **Controller:** Exposición de endpoints REST.
- **Service:** Lógica del negocio, incluyendo cálculo, manejo de caché y registro de llamadas.
- **Repository:** Acceso a la base de datos para el historial de llamadas.
- **DTO / Entity:** Clases de transferencia de datos y mapeo de entidades.

## Instrucciones para ejecutar el servicio

### Prerrequisitos

- Tener Docker y Docker Compose instalados.
- Asegurarse de que los puertos 8007 (API) y 5432 (PostgreSQL) estén disponibles.

### Pasos para ejecutar

1. **Clonar el repositorio**

   ```bash
   git clone https://github.com/juandnb01/api-challenge-tenpo-juanTorres
   cd api-challenge-tenpo-juanTorres
   ```

2. **Construir y levantar los contenedores con Docker Compose**

   Asegúrate de que los archivos `Dockerfile` y `docker-compose.yaml` se encuentren en la raíz del proyecto. Luego, ejecuta el siguiente comando:

   ```bash
   docker-compose up --build
   ```

   Este comando realizará las siguientes acciones:

   - **Construcción de la imagen de la API:** El Dockerfile utiliza una etapa de compilación (con Maven) y otra de ejecución (con OpenJDK) para generar la imagen que contiene el JAR compilado.
   - **Inicio del contenedor PostgreSQL:** Se levanta un contenedor basado en la imagen oficial de PostgreSQL, configurado con las variables de entorno necesarias y un volumen para persistir los datos.
   - **Inicio del contenedor de la API:** El contenedor de la API se levanta y se conecta a la red `my-network`, asegurando que la aplicación se comunique con la base de datos.

3. **Acceder a la aplicación**

   Una vez que los contenedores estén en funcionamiento, la API estará disponible en:

   ```
   http://localhost:8007
   ```

   Puedes utilizar herramientas como Postman para probar los endpoints:

   - **Cálculo con porcentaje dinámico:**

     ```http
     GET http://localhost:8007/api/calculate?num1=10&num2=20
     ```

     Respuesta esperada (ejemplo):

     ```json
     {
       "sum": 30.0,
       "percentage": 10.0,
       "result": 33.0
     }
     ```

   - **Consulta del porcentaje actual:**

     ```http
     GET http://localhost:8007/api/percentage
     ```

   - **Historial de llamadas:**

     Primero, realiza algunas llamadas a `/api/calculate` para generar registros y luego consulta:

     ```http
     GET http://localhost:8007/api/history
     ```

     Respuesta esperada (ejemplo):

     ```json
     [
       {
         "id": 1,
         "endpoint": "/api/calculate",
         "parameters": "num1=10&num2=20",
         "response": "CalculationResponse{sum=30.0, percentage=10.0, result=33.0}",
         "timestamp": "2025-03-07T14:30:00"
       }
     ]
     ```


