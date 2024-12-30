# TechieTalks - Aplicación Android
|!["Imagen del menu de la aplicación TechieTalks"](/doc/img/layout/light/menu.png) | !["Imagen de portada de la aplicación TechieTalks"](/doc/img/layout/light/index.png) | !["Imagen de una publicación en la aplicación TechieTalks"](/doc/img/layout/light/publication.png) |
|--------------------------------------------|-----------------------------------------------|----------------------------------------------------|

## Sobre el proyecto

Este repositorio es un proyecto de aplicación creada para dispositivos **Android** con una versión mínima `7.1 Nougat`.  
`TechieTalks` es una aplicación de `foro/blog` privado en el que ciertos usuarios con roles de **edicion** podrán crear publicaciones relacionadas con el sector de la **Ciencia**,**Tecnología** y **Programación**. La aplicación permite a los usuarios visitantes poder acceder a las publicaciones creadas, comentar y crear hilos y subhilos dentro de cada publicación, lo que puede dar pie a grandes debates sobre temas muy interesantes!

La aplicación de **TechieTalks** utiliza una [API REST](https://github.com/Martingago/techieTalks) 100% desarrollada por mí lo que me proporciona pleno control sobre nuevas implementaciones, manejo  y control de datos, gestión de incidencias, etc.

## Get started

Necesitas clonar el repositorio desde Gitlab. Puedes hacerlo mediante una terminal a través del siguiente comando:

```bash
git clone https://github.com/Martingago/techieTalks
```

### Requisitos

Para poder ejecutar esta parte del **Front-end** es necesario que dispongas de **Android Studio** con las siguientes configuraciones:

| Elemento     | Versión |
|--------------|---------|
|CompileSdk    |    34   |
|MinSdk        |    25   |
|TargetSdk     |    34   |
|JDK           |     8   |

Realmente no es necesario ningún otro ajuste o configuración a bases de datos locales, ya que la propia configuración establecida en **Retrofit** realiza las peticiones al servidor en producción.
 >NOTA:  
 El servidor en producción utiliza la capa gratuita de Render. Dado que el servidor puede estar en "Hibernación", la primera llamada/petición puede tardar varios minutos en responder (**de 2 a 15 minutos**). Las peticiones posteriores, una vez el servidor esté activo, tienen tiempos de respuesta de aproximadamente **~150ms**.
