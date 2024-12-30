# FASE DE DISEÑO

## Modelo conceptual do dominio da aplicación e/ou Diagrama de clases [usando UML, ConML, o lenguaje similar].
La siguiente imagen representa el Diagrama de clases de la estructura principal de la aplicación:
  
!["Imagen Diagrama de Clases UML"](/doc/img/design/Diagrama-api-blog.png)

## Casos de uso.

En las siguientes imágenes se muestran los diferentes casos de uso en base a los roles de los usuarios que realizan las acciones, ordenados de mayor cantidad de privilegios a menor:
### Usuario roles de ADMINISTRACIÓN
!["Imagen casos de uso para usuarios con rol de adminitración"](/doc/img/design/Caso-uso-administrador.png)

### Usuario roles de EDITOR
!["Imagen casos uso usuarios con rol de edicion"](/doc/img/design/Caso-uso-editor.png)

### Usuario roles BÁSICOS
!["Imagen casos uso usuarios con rol de edicion"](/doc/img/design/Caso-uso-user.png)

### Usuario que no se ha logueado
!["Imagen casos de uso usuario sin loguear"](/doc/img/design/Caso-uso-no-login.png)


## Diseño interfaces de usuarios [mockups]

| Index web claro | Menu app claro | Publicación claro |
|----------|----------|----------|
| ![Imagen del index de la aplicación](/doc/img/layout/light/index.png) | ![Menu app](/doc/img/layout/light/menu.png) | ![Imagen  de una publicación](/doc/img/layout/light/publication.png) |

| Index web oscuro | Menu app oscuro | Publicación oscuro |
|----------|----------|----------|
| ![Imagen del index de la aplicación](/doc/img/layout/dark/index_dark.png) | ![Menu app](/doc/img/layout/dark/menu_dark.png) | ![Imagen de una publicación](/doc/img/layout/dark/post_dark.png) |

| Registrar usuario | Login de usuario | Perfil del usuario |
|----------|----------|----------|
| ![Imagen de Registrar usuario](/doc/img/layout/light/register.png) | ![Imagen de Login de usuario](/doc/img/layout/light/login.png) | ![Imagen Perfil del usuario](/doc/img/layout/light/account.png) |

| Administración de post | Administración de etiquetas | Administración de usuarios |
|----------|----------|----------|
| ![Imagen Administración de post](/doc/img/layout/light/handle_post.png) | ![Imagen Administración de etiquetas](/doc/img/layout/light/handle_tag.png) | ![Imagen Administración de usuarios](/doc/img/layout/light/handle_users.png) |

| Menú de opciones | Términos y condiciones | Sección de comentarios |
|----------|----------|----------|
| ![Imagen menu options](/doc/img/layout/light/options.png) | ![Imagen terminos app](/doc/img/layout/light/terms.png) | ![Imagen comentarios de un post](/doc/img/layout/light/comments.png) |


## Diagrama de Base de Datos.
La siguiente imagen representa la estructura de la base de datos existente en la aplicación.

!["Imagen del diagrama de la base de datos"](/doc/img/design/Esquema-base-datos.png)

## Tablas y Relaciones

### 1. **posts**
- **Descripción**: Almacena las publicaciones del blog o foro.
- **Atributos**:
  - `id`: BIGINT (Identificador único de la publicación)
  - `contenido`: LONGTEXT (Contenido del post)
  - `titulo`: VARCHAR(255) (Título del post)
  
- **Relaciones**:
  - Uno a uno `post_details` (una publicación sólo puede tener UN detalles de publicación)
  - Uno a muchos con `comentarios` (una publicación puede tener varios comentarios)
  - Muchos a muchos con `tags` a través de `posts_tags` (una publicación puede tener varios tags)

### 2. **post_details**
- **Descripción**: Almacena detalles adicionales sobre las publicaciones, como la fecha de creación y el autor.
- **Atributos**:
  - `fecha_creacion`: DATETIME(6) (Fecha de creación del post)
  - `post_id`: BIGINT (Clave foránea de la tabla `posts`)
  - `usuarios_id`: BIGINT (Clave foránea de la tabla `usuarios`) Representa al usuario que creó dicho post

- **Relaciones**:
  - Uno a uno con `posts` (UN detalle de post puede pertenecer únicamente a un post)
  - Muchos a uno con `usuarios` (un usuario puede ser el autor de varios posts)

### 3. **comentarios**
- **Descripción**: Almacena los comentarios realizados en los posts.
- **Atributos**:
  - `id`: BIGINT (Identificador único del comentario)
  - `contenido`: VARCHAR(500) (Contenido del comentario)
  - `fecha_comentario`: DATETIME(6) (Fecha del comentario)
  - `post_id`: BIGINT (Clave foránea de la tabla `post_details`) Publicación en la que fue realizado el comentario
  - `usuarios_id`: BIGINT (Clave foránea de la tabla `usuarios`) Usuario que hizo el comentario
  - `comentario_origen_id`: BIGINT (Clave foránea a la tabla `comentarios`)
  - `comentario_padre_id` : BIGINT (Clave foránea a la tabla `comentairos`)

> **NOTA**  
`Id` es el identificador propio del comentario  
`comentario_origen_id` es el comentario raiz del cual se generan las respuestas y subrespuestas (hilos).  
`comentario_padre_id` es el identificador del comentario al que se está respondiendo directamente.  
 
!["Imagen explicativa del funcionamiento de los comentarios_origen_id y los comentario_padre_id"](/doc/img/design/comentarios-section-explicacion.png)

- **Relaciones**:
  - Muchos a uno con `post_details` (un post puede tener varios comentarios)
  - Muchos a uno con `usuarios` (un usuario puede hacer varios comentarios)
  - Muchos a uno con `comentarios` (un comentario puede tener varias respuestas de comentarios)

### 4. **usuarios**
- **Descripción**: Almacena los datos de los usuarios del sistema.
- **Atributos**:
  - `id`: BIGINT (Identificador único del usuario)
  - `name`: VARCHAR(255) (Nombre del usuario)
  - `password`: VARCHAR(255) (Contraseña del usuario)
  - `username`: VARCHAR(255) (Nombre de usuario)

- **Relaciones**:
  - Uno a muchos con `post_details` (un usuario puede ser el autor de varios posts)
  - Uno a muchos con `comentarios` (un usuario puede hacer varios comentarios)
  - Muchos a muchos con `roles` a través de `users_roles` (un usuario puede tener varios roles)

### 5. **tags**
- **Descripción**: Almacena etiquetas asociadas a las publicaciones.
- **Atributos**:
  - `id`: BIGINT (Identificador único de la etiqueta)
  - `nombre`: VARCHAR(255) (Nombre de la etiqueta)

- **Relaciones**:
  - Muchos a muchos con `posts` a través de `posts_tags` (una etiqueta puede estar en varios posts)

### 6. **posts_tags**
- **Descripción**: Tabla intermedia para la relación muchos a muchos entre `posts` y `tags`.
- **Atributos**:
  - `post_id`: BIGINT (Clave foránea de la tabla `posts`)
  - `tag_id`: BIGINT (Clave foránea de la tabla `tags`)

- **Relaciones**:
  - Muchos a uno con `posts`
  - Muchos a uno con `tags`

### 7. **roles**
- **Descripción**: Almacena los roles disponibles en el sistema.
- **Atributos**:
  - `id`: BIGINT (Identificador único del rol)
  - `rol`: VARCHAR(255) (Nombre del rol)

- **Relaciones**:
  - Muchos a muchos con `usuarios` a través de `users_roles`

### 8. **users_roles**
- **Descripción**: Tabla intermedia para la relación muchos a muchos entre `usuarios` y `roles`.
- **Atributos**:
  - `user_id`: BIGINT (Clave foránea de la tabla `usuarios`)
  - `role_id`: BIGINT (Clave foránea de la tabla `roles`)

- **Relaciones**:
  - Muchos a uno con `usuarios`
  - Muchos a uno con `roles`

## Diagrama de componentes software que contituyen el producto y el despliegue.

Esta imagen indica los componentes de software que contituyen el despligue de la aplicación.  
Este diagrama podría escalar si creasemos también una aplicación web, siendo añadida como un componente extra que también se contectaría con el servidor API REST.

!["Imagen del diagrama de despliegue de la aplicación"](/doc/img/design/diagrama-despegue.png)