
# Descripción del proyecto

Proyecto simple que muestra una API Rest y su consumo
mediante un motor de plantillas.


## Tecnologías usadas

 - Thymeleaf: Motor de renderizado de plantillas.
 - H2: Base de datos en memoria volátil.  
 - JPA: API de persistencia de Java.


## Rutas API

#### Devuelve todos los usuarios

```http
  GET /api/v1
```

#### Obtiene un usuario

```http
  GET /api/v1/${id}
```

| Parámetro | Tipo     | Descripción                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `int`    | **Requerido**. Id del usuario     |

#### Crea un usuario

```http
  POST /api/v1
```

| Parámetro | Tipo     | Descripción                       |
| :-------- | :------- | :-------------------------------- |
| `user`    | `User`   | **Requerido**. Objeto Usuario     |

#### Actualiza un usuario

```http
  PUT /api/v1/${id}
```

| Parámetro | Tipo     | Descripción                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `int`    | **Requerido**. Id del usuario     |

#### Elimina un usuario

```http
  DELETE /api/v1/${id}
```

| Parámetro | Tipo     | Descripción                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `int`    | **Requerido**. Id del usuario     |

## Ejecución

Para ejecutar el proyecto, simplemente utilize el fichero
mvnw adjunto.

```bash
  ./mvnw spring-boot:run
```
    
