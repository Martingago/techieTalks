# Proyecto fin de ciclo

![Imagen portada index: TechieTalks: Habla tecnología vive innovación](/doc/img/index/banner-TechieTalks.png)
## Descripción

**TechieTalks** es una aplicación móvil diseñada para apasionados de la tecnología que buscan un espacio para informarse, debatir y compartir sobre temas relacionados con el desarrollo de software y nuevas tecnologías. En esta plataforma, los usuarios pueden registrarse, iniciar sesión y participar activamente visualizando publicaciones, comentando y respondiendo a otros usuarios en hilos y subhilos. La interfaz es intuitiva y permite una interacción fluida entre los miembros de la comunidad.

Toda la información y datos de la aplicación se gestionan a través de un servicio de **API REST** que he desarrollado personalmente, lo que asegura que el 100% del desarrollo de TechieTalks, tanto del lado del servidor como de la aplicación móvil, ha sido creados por mí. Este desarrollo garantiza de TechieTalks una experiencia optimizada única y personalizada para los usuarios.

## Instalación / Puesta en marcha

La aplicación de [`TechieTalks`](/project/Blog_Retrofit/) se conecta al servicio proporcionado por el [`SERVIDOR API REST`](/project/API_REST_BLOG/), es por ello que para utilizar la aplicación en un dispositivo móvil simplemente nos bastará con descargar e instalar la [`APK`](/project/APK/) en nuestro dispositivo movil. Para el correcto funcionamiento de la aplicación se requiere de conexión a internet.
Si nunca hemos instalado una APK en nuestro dispositivo movil, va a ser necesario que previamente sigamos una serie de pasos para que nuestro dispositivo nos permita descargar y ejecutar código de terceros, en nuestro caso una APK.

### Pasos para configurar un teléfono para instalar/ejecutar APKs

Si nunca has instalado una APK en tu dispositivo móvil, sigue estos pasos para permitir la descarga y ejecución de aplicaciones de terceros:

1. **Descarga la APK en tu dispositivo móvil**: Puedes descargar la APK de dos formas diferentes: 
    - Conectando tu dispositivo al ordenador a través de un cable USB y transfiriendo el archivo [`APK`](/project/APK/) a tu gestor de archivos.
    - Desde tu propio dispositivo movil descarga este [`fichero APK`](/project/APK/).

2. **Permitir la instalación de aplicaciones de orígenes desconocidos**:
    - Android 8.0 y superior:
        1. Abre la aplicación Configuración en tu dispositivo.
        2. Ve a Aplicaciones y notificaciones.
        3. Selecciona Avanzado.
        4. Toca Acceso especial de aplicaciones.
        5. Selecciona Instalar aplicaciones desconocidas.
        6. Selecciona el navegador (por ejemplo, Chrome) desde el cual descargarás la APK.
        7. Activa Permitir desde esta fuente.

    - Android 7.0 y versiones anteriores:
        1. Abre la aplicación Configuración en tu dispositivo.
        2. Desplázate hacia abajo y selecciona Seguridad.
        3. Activa la opción Fuentes desconocidas.
        4. Confirma tocando Aceptar en el mensaje de advertencia.

3. **Instalar la APK**:
    - Una vez descargada la APK en nuestro dispositivo nos dirigimos a nuestro `Gestor de archivos > APKs > TechieTalks`.  
    - Pulsamos el botón de instalar, aceptamos las alertas sobre seguridad y permisos de la apliación.

4. **Abrir la aplicación**: 
    - Una vez instalada, toca **Abrir** para iniciar la aplicación `TechieTalks`.
    - También puedes encontrar la aplicación en tu lista de aplicaciones.

Siguiendo estos pasos, habrás configurado tu dispositivo para permitir la instalación de aplicaciones de orígenes desconocidos y habrás instalado la aplicación TechieTalks. Ahora podrás disfrutar de todas sus funcionalidades, siempre que tengas una conexión a internet activa.

## Uso

Este proyecto consiste en una aplicación fullstack que incluye un [**servidor API REST**](/project/API_REST_BLOG/) y una [**aplicación móvil intuitiva**](/project/Blog_Retrofit/). A continuación, se describe brevemente cómo interactúa cada componente:

**Servidor API REST**
El servidor API REST gestiona las peticiones realizadas por los usuarios a través de la aplicación móvil. Sus principales funcionalidades incluyen:

- Registro de Usuario: Permite a los usuarios registrarse proporcionando sus datos básicos.
- Inicio de Sesión: Permite a los usuarios autenticarse en la aplicación.
- Gestión de Posts: Los usuarios pueden crear, editar y eliminar publicaciones.
- Gestión de Comentarios: Los usuarios pueden agregar, editar y eliminar comentarios en las publicaciones.
- Moderación: Dependiendo de los roles asignados (usuario, moderador, administrador), se pueden realizar acciones de moderación como la aprobación o eliminación de contenido.

**Aplicación Móvil**
La aplicación móvil proporciona una interfaz amigable y fácil de usar para interactuar con el servidor API REST. Sus principales funcionalidades son:

- Registro e Inicio de Sesión: Los usuarios pueden registrarse y autenticarse de manera sencilla.
- Creación de Posts: Los usuarios pueden crear nuevas publicaciones con texto e imágenes.
- Comentarios: Los usuarios pueden comentar en las publicaciones y responder a otros comentarios.
- Visualización de Contenidos: Los usuarios pueden explorar y visualizar publicaciones y comentarios de otros usuarios.
- Moderación de Contenidos: Dependiendo del rol asignado, los usuarios pueden moderar contenido, incluyendo la aprobación o eliminación de publicaciones y comentarios.

Esta estructura permite una experiencia completa y fluida, facilitando la interacción entre los usuarios y el contenido generado dentro de la aplicación.

## Sobre el autor

Encantado, soy Martín, desarrollador Full-stack con experiencia en  las siguientes tecnologías: `JavaScript`,`Java`,`MySQL`,`VUE3` y `Wordpress`. 
Me encanta la maquetación y el diseño web y estoy listo para aprender más. 
He elegido este proyecto para explorar una tecnología del ecosistema Java: `Spring`, y enfrentar el desafío de crear mi primera API REST funcional 🚀.

Puedes contactar conmigo a través de [Linkedin](https://www.linkedin.com/in/martin-gago-choren/) o a través de mi portfolio web [martingago.dev](https://martingago.dev/)

## Licencia

Este proyecto está licenciado bajo la [GNU Free Documentation License Version 1.3](https://www.gnu.org/licenses/fdl-1.3.html). Esta licencia permite a cualquier usuario o institución copiar, distribuir y modificar el contenido del proyecto, siempre y cuando se mantenga la misma licencia en cualquier versión derivada del trabajo. Además, se deben preservar las notas de copyright y se deben proporcionar copias del texto de la licencia junto con el proyecto. Esta licencia garantiza que el proyecto y cualquier obra derivada de él permanezcan libres y accesibles para todos, fomentando la colaboración y la mejora continua.

### Derechos y responsabilidades bajo licencia

- **Copiar y Distribuir**: Los usuarios pueden copiar y distribuir el proyecto en cualquier medio, ya sea en formato digital o físico, sin restricciones.  
- **Modificar**: Los usuarios pueden modificar el contenido del proyecto para adaptarlo a sus necesidades o mejorar sus funcionalidades.
- **Obras Derivadas**: Cualquier trabajo derivado debe ser licenciado bajo la misma licencia **GNU Free Documentation License Version 1.3**, asegurando que las futuras versiones también permanezcan libres.
- **Notas de Copyright**: Es necesario mantener todas las notas de copyright originales y las notas de permiso en todas las copias del proyecto, ya sean originales o modificadas.
- **Texto de la Licencia**: Se debe proporcionar una copia del texto de la licencia junto con cualquier distribución del proyecto, ya sea en su forma original o modificada.

Para más detalles, consulta el archivo [LICENSE](/LICENSE.md) en la raíz del repositorio.

## Índice

1. Anteproyecto
    * 1.1. [Idea](doc/templates/1_idea.md)
2. [Diseño](doc/templates/5_deseño.md)
3. [Implantación](doc/templates/6_implantacion.md)


## Guía de contribución

¡Gracias por considerar contribuir a mi proyecto! Aprecio cualquier tipo de contribución, ya sea en forma de nuevas funcionalidades, corrección de errores, optimización del código, realización de tests automatizados, desarrollo de nuevas interfaces de integración o creación de plugins.

### Tipos de Contribuciones
- Nuevas Funcionalidades: Si tienes una idea para una nueva característica, por favor abre un issue primero para discutirlo antes de empezar a trabajar en ella.
- Corrección de Errores: Revisa la lista de issues para ver si hay algún problema que puedas solucionar.
- Optimización del Código: Siempre estoy buscando maneras de hacer el código más eficiente.
- Tests Automatizados: Me encantaría tener más cobertura de tests para asegurar la calidad del código.
- Interfaces de Integración: Si quieres integrar mi API con otras herramientas o servicios, por favor, házmelo saber.
- Plugins: Si tienes ideas para plugins que podrían mejorar la funcionalidad del proyecto, estaré encantado de revisarlas.

### Revisión de Código
Todos los pull requests serán revisados por uno de los mantenedores del proyecto. Asegúrate de que tu código esté bien documentado y siga las guías de estilo del proyecto. También, ten paciencia ya que la revisión puede tardar un poco.

## Links

Estos son algunos links de utilidad que pueden ayudar a otro desarrolladores a llevar a cabo sus proyectos en un entorno de pruebas **gratuíto**. Además incluyo la documentación de mi API REST que he creado utilizando los servicios de **Postman Documenter**

Plataforma gratuíta en la que se hostea el servidor **API REST**: [Render - https://render.com/](https://render.com/)  
Plataforma gratuíta en la que se hostea el servidor de **Bases de Datos**: [Clever Cloud - https://www.clever-cloud.com/](https://www.clever-cloud.com/product/mysql/)  
Documentación accesible de la API REST creada con **Postman documenter**: [Postman - https://www.postman.com/](https://documenter.getpostman.com/view/36991500/2sAYBPnEeL#intro)  