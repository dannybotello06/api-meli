
# Prueba Meli

Este proyecto se construyo para un prueba con meli. Esta es una api que debe devolver detalles de producto para compararlos.

Requerimientos:

Desarrollar una API RESTful básica que devuelva detalles de varios artículos para comparar.

 La API debe proporcionar campos como el nombre del producto, la URL de la imagen, la descripción, el precio, la calificación y las especificaciones.

Incluya la gestión básica de errores y comentarios en línea para explicar su lógica.


Puede usar cualquier tecnología de backend o framework de su elección (Se utiliza Spring boot )

No utilice bases de datos reales; conserve todo en archivos JSON o CSV locales.


## Herrramientas y Arquitectura

Se elige como arquitectura la Hexagonal con patrones de port y adapter. Se elige el framework de Spring boot con maven para las dependecias que son: 

>spring-boot-starter-security

>spring-boot-starter-web
lombok

mockito

jjwt

Se crea una autenticacion por token con JWT y una paginacion para la consulta de todos los productos


## Docker

Se creo el archivo Docker file para levantar la instancia con las siguientes instrucciones:

FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app


COPY pom.xml .
RUN mvn dependency:go-offline -B


COPY src ./src
RUN mvn clean package -DskipTests


FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app


COPY --from=build /app/target/*.jar app.jar


EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]


Y para inicializar se debe correr la siguiente instrucciones:

docker build -t apimeli

 docker run -p 8080:8080 apimeli
## API Reference

#### Obtener token de autenticacion 

```http
  POST /auth/login
```
Request Body
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `username` | `String` | Nombre de usuario |
| `password` | `String` | La contraseña del usuario. |


Ejemplo 


{
   
    "username": "admin",
    "password": "admin123"
}

Response Body 
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `token` | `String` | token autorizacion |

Ejemplo

{
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1Njc2MTY4MywiZXhwIjoxNzU2NzY1MjgzfQ.S3yFgIaIq8wojcDiWBRbhz3KaNTWDid-NS8mj56g8LxVeIHTy7i2QHMQfqFAGLgmYaf3lHgBFixMoDh_yDrpPQ"
}

#### Get item

```http
  GET /api/v1/products?page=0&size=10
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `page`      | `int` |  numero de pagina (default=0) |
| `size`      | `int` |  tamaño de registros por pagina(default=10) |

| Autorizacion | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `token`      | `string` | **Required** token |


Response Body

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `id` | `String` | Id del producto |
| `name` | `String` | Nombre del producto |
| `imageUrl` | `String` | url de Imagen del producto  |
| `description` | `String` | Descripcion del producto |
| `price` | `BigDecimal` | Precio del producto. |
| `rating` | `Double` | Calificacion del producto |
| `specs` | `Map<String, String>` | Especificaciones de producto. |

Ejemplo

[
    {
        "id": "p-1003",
        "name": "Nova Wireless Mouse",
        "imageUrl": "https://picsum.photos/id/1080/800/600",
        "description": "Ergonomic mouse with multi-device pairing.",
        "price": 59.99,
        "rating": 4.3,
        "specs": {
            "DPI": "8000",
            "Battery": "USB-C Rechargeable",
            "Weight": "78g"
        }
    },
    {
        "id": "p-1002",
        "name": "Bolt Mechanical Keyboard",
        "imageUrl": "https://picsum.photos/id/1062/800/600",
        "description": "Hot-swappable 75% board with RGB.",
        "price": 129.00,
        "rating": 4.4,
        "specs": {
            "Switches": "Gateron Brown",
            "Layout": "75%",
            "Connectivity": "USB-C"
        }
    },
    {
        "id": "p-1001",
        "name": "Acme Monitor 27\" 4K",
        "imageUrl": "https://picsum.photos/id/1011/800/600",
        "description": "27-inch 4K IPS monitor with HDR10.",
        "price": 349.99,
        "rating": 4.6,
        "specs": {
            "Panel": "IPS",
            "Resolution": "3840x2160",
            "RefreshRate": "60Hz",
            "Ports": "2xHDMI, 1xDP"
        }
    }
]

```http
  POST /api/v1/products/compare
```
| Parameter(Body)  | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `ids`      | `List<String>` | array de id (["p-1001", "p-1002", "p-1003"])  |
| `sortBy`      | `String` |  |  "price" o "rating"
| `direction`      | `String` | "asc" o "desc" |

| Autorizacion | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `token`      | `string` | **Required** token |


Ejemplo

{
  "ids": ["p-1001", "p-1002", "p-1003"],
  "sortBy": "price",
  "direction": "asc"
}


Response Body

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `id` | `String` | Id del producto |
| `name` | `String` | Nombre del producto |
| `imageUrl` | `String` | url de Imagen del producto  |
| `description` | `String` | Descripcion del producto |
| `price` | `BigDecimal` | Precio del producto. |
| `rating` | `Double` | Calificacion del producto |
| `specs` | `Map<String, String>` | Especificaciones de producto. |


Ejemplo

[
    {
        "id": "p-1003",
        "name": "Nova Wireless Mouse",
        "imageUrl": "https://picsum.photos/id/1080/800/600",
        "description": "Ergonomic mouse with multi-device pairing.",
        "price": 59.99,
        "rating": 4.3,
        "specs": {
            "DPI": "8000",
            "Battery": "USB-C Rechargeable",
            "Weight": "78g"
        }
    },
    {
        "id": "p-1002",
        "name": "Bolt Mechanical Keyboard",
        "imageUrl": "https://picsum.photos/id/1062/800/600",
        "description": "Hot-swappable 75% board with RGB.",
        "price": 129.00,
        "rating": 4.4,
        "specs": {
            "Switches": "Gateron Brown",
            "Layout": "75%",
            "Connectivity": "USB-C"
        }
    },
    {
        "id": "p-1001",
        "name": "Acme Monitor 27\" 4K",
        "imageUrl": "https://picsum.photos/id/1011/800/600",
        "description": "27-inch 4K IPS monitor with HDR10.",
        "price": 349.99,
        "rating": 4.6,
        "specs": {
            "Panel": "IPS",
            "Resolution": "3840x2160",
            "RefreshRate": "60Hz",
            "Ports": "2xHDMI, 1xDP"
        }
    }
]
## Paginas y IA 

Se utilizan los prompt :

Como dockerizar un api en springboot

Generar los casos para las pruebas unitarias del siguiente codigo

Como implementar JWT en un api springboot

Pagina para la documentacion:

Como crear unit test para clases de java

https://readme.so/es/editor