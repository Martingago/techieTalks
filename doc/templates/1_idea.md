# Diseño, desarrollo y publicación de una API REST con Spring-boot e implementación en app móvil.

## Idea global

La idea a presentar con este proyecto es crear una API REST y hospedarla en un servidor para poder realizar consultas de forma remota desde cualquier dispositivo. Además, se creará una aplicación móvil (Android) que permitirá acceder a las funcionalidades principales que presenta la API facilitando su uso desde una aplicación de uso móvil.

Esta API ha sido diseñada para crear la base de datos y la lógica de negocio de un blog o un foro, ya que con algunos ajustes se podría emplear para estos dos tipos de servicios.

## A lo largo del proyecto se detallan:
- Las tecnologías empleadas para desarrollar la API.
- Cuál ha sido el patrón de diseño elegido para el proyecto.
- Explicación de los servicios principales de la API.
- Errores, problemas e inconvenientes que surgieron durante el desarrollo.
- Pruebas realizadas para validar el código.
- Despliegue de la base de datos en un servidor.
- Despliegue de la API en un servidor.
- Aplicación móvil.

## Finalidad del proyecto
La finalidad principal de este proyecto es aprender sobre una nueva tecnología y conocer las estructuras y patrones de diseño a aplicar para desarrollar una API REST. Más allá del nivel académico, este proyecto tiene como finalidad crear la parte lógica de un Blog para poder implementarlo como un microservicio en una web o portfolio. Con la integración de una aplicación móvil, el objetivo se amplía a proporcionar una herramienta accesible para que los usuarios puedan acceder a los datos de la aplicación desde una interfaz gráfica.

## Servicios que ofrece la API Blog Spring:
- Crear/eliminar/modificar/visualizar usuarios.
- Asignación de roles a usuarios (Los roles son: ADMIN, EDITOR, USER).
- Autenticación de los usuarios mediante Basic Auth.
- Autorización de los usuarios a servicios en base a sus roles.
- Creación, edición y eliminación de posts (ADMIN, EDITOR).
- Creación de tags para asociar contenidos (ADMIN).
- Comentar y responder a comentarios de publicaciones (USERS).
- Moderación (eliminación) de comentarios (ADMIN).

## Aplicación móvil Android:
Se implementará una aplicación de uso móvil que permitirá a los usuarios interactuar con la API de manera sencilla. Esta aplicación contará con funcionalidades como:
- Registro e inicio de sesión de usuarios.
- Creación y edición de posts y comentarios.
- Gestión de roles y de usuarios.
- Visualización y moderación de contenido.

## Tecnologías empleadas:
- Spring Boot
- Spring Data
- Spring Security
- Spring Validation
- Base de datos MySQL
- Kotlin
- Retrofit

## Herramientas de desarrollo empleadas:
- IntelliJ IDEA
- Android Studio
- MySQL Workbench
- POSTMAN
- Docker