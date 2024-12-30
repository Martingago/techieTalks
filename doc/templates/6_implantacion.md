# FASE DE IMPLANTACIÓN

## Manual técnico:

## Información relativa a la instalación: 

### Requerimientos de hardware.

- **Dispositivo Android**: La aplicación requiere un dispositivo que ejecute, como mínimo, la versión 7.1 (Nougat, API nivel 25) del sistema operativo Android.
- **Conexión a Internet**: Es necesario que el dispositivo esté conectado a Internet para poder interactuar con los servidores de la aplicación.
- **Cuenta de Administrador**: Se necesita una cuenta con permisos de administrador para realizar acciones de gestión dentro de la aplicación.

### Requisitos del software:

- **SDK de Compilación**: Android SDK 34
- **Versión Objetivo de Android**: 14 (API nivel 34)

### Infraestructura del servidor

|  Servidor Base de datos      |                                                                                                                                 |
|------------------------------|---------------------------------------------------------------------------------------------------------------------------------|
| Descripción                  | Un servidor dedicado para almacenar los datos de la aplicación, incluyendo información de usuarios, comentarios y publicaciones.|
| Página oficial               | [https://console.clever-cloud.com/](https://console.clever-cloud.com/)                                                          |

|  Servidor API REST      |                                                                                                                                      |
|-------------------------|--------------------------------------------------------------------------------------------------------------------------------------|
| Descripción             | Un servidor dedicado para almacenar los datos de la aplicación, incluyendo información de usuarios, comentarios y publicaciones.     |
| Página oficial          | [https://render.com/](https://render.com/)                                                                                           |

### Configuración inicial de seguridad

| Requisitos de conexión     |                                                                                                                                   |
|----------------------------|-----------------------------------------------------------------------------------------------------------------------------------|
| Protocolo comunicación     | La aplicación se comunica con el servidor API REST utilizando el protocolo HTTPS para asegurar la transferencia de datos.         |
| Autenticación y seguridad  | Actualmente para validar los usuarios dentro del servidor se utiliza un sistema de validación muy básico **Basic auth**, la previsión seria implementar a futuro un sistema de autenticación más seguro como OAuth 2.0 o JWT (JSON Web Tokens) para la autenticación de usuarios.                            |

|  Control usuarios       |                                                                                                                                      |
|-------------------------|--------------------------------------------------------------------------------------------------------------------------------------|
| Descripción             | El control de usuarios se realiza a nivel interno dentro del servidor de API Rest. Los usuarios tienen asignados una serie de `Roles`, que el servidor de encarga de validar antes de realizar una petición. Cada petición pasa por un proceso de **autenticación** y **autorización**. Primero se comprueba si las credenciales de un usuario son correctas, y posteriormente se valida si sus permisos son los suficientemente altos como para realizar la acción solicitada. (Ej: eliminar un usuario)|

|  Carga de datos      |                                                                                                                                         |
|-------------------------|--------------------------------------------------------------------------------------------------------------------------------------|
| Descripción             | Para el momento de lanzamiento de la Aplicación no es necesario cargar masivas cantidades de datos, únicamente es necesario cargar en la base de datos `MySQL` de forma manual los ROLES de los usuarios (ADMIN, EDITOR, USER) y cargar un usuario Administrador. A partir de ahí la aplicación puede crecer en `Tags`, `Publicaciones`, y `Usuarios` de forma natural.                                                                                                                                     |
| Carga de datos          | Si quisieramos cargar una serie de datos básicos para que la aplicación tuviera una base de publicaciones, podríamos hacer un `mysqldump` para hacer un volcado de nuestros datos en local y transferirlo al **servidor de bases de datos**                                                                              |

|  Usuarios del sistema           |                                                                                                                              |
|---------------------------------|------------------------------------------------------------------------------------------------------------------------------|
| Usuarios aplicación Android     | Como se mencionó anteriormente, dentro de la aplicación  `TechieTalks` los usuarios con rol de `ADMIN` podrán realizar peticiones al servidor API REST para realizar consultas/actualizaciones/inserción de datos delicados a los que un usuario sin privilegios no tiene acceso. Estos permisos incluyen: Capacidad de crear/editar/eliminar Tags. Asignar o revocar Roles a otros usuarios. Crear/editar/eliminar publicaciones. Eliminar cuentas de otros usuarios                          |
| Usuario base de datos          | Actualmente en el servidor de Base de datos existe únicamente un usuario con acceso al registro de `Logs`, a realizar acciones de `mantenimiento`, etc. Esto se debe a que estoy utilizando la capa gratuíta y sólo se permite un **único administrador**, que en este caso soy yo al ser el creador de la aplicación. |
| Servidor API REST             | El servidor API rest se encuentra enlazado con mi [`repositorio de Github`](https://github.com/Martingago/BlogSpring), cualquier cambio realizado en la rama de `main` se actualizará y se realiza un deploy automático al servidor. Por lo que en este caso el gestor/mantenedor del servidor soy yo |

## Diagrama de despliegue

El diagrama de despliegue de la aplicación se mantiene como el especificado en la sección de [Diseño](/doc/templates/5_deseño.md).  
!["Imagen con el diagrama de despliegue de la aplicación"](/doc/img/design/diagrama-despegue.png)


## Información Relativa a la Administración del Sistema

Tras haber desplegado el sistema de la aplicación, es crucial mantener un control de incidencias para asegurar que la aplicación se mantenga estable y libre de errores.

### Seguridad del sistema 
A nivel de seguridad del sistema, el servicio de [Render](https://dashboard.render.com/) proporciona un panel de administración completo. Este panel permite gestionar diversos aspectos del sistema. A continuación se señalan las principales funcionalidades que ofrece Render para la administración del sistema:

1. **Registro de Incidencias**:
   - Render nos permite llevar un registro detallado de todas las incidencias que se presenten. Este registro es fundamental para identificar y resolver problemas.
   - Las incidencias pueden ser categorizadas y priorizadas según su gravedad, facilitando la gestión.

2. **Estado del Servidor**:
   - El panel de Render muestra el estado en tiempo real el estado del servidor.
   - Las alertas automáticas se pueden configurar para notificar de incidencias a través de email.

3. **Configuraciones**:
   - Render permite gestionar y aplicar configuraciones al sistema de manera centralizada
   - La capacidad de modificar configuraciones sin necesidad de realizar nuevos despliegues.

4. **Gestión de Variables de Entorno**:
   - El panel de administración de Render facilita la gestión de variables de entorno. Por ejemplo para establecer la conexión con la Base de datos.
   - Las variables de entorno pueden ser actualizadas de manera segura.

![Imagen de la sección de administración proporciona Render](/doc/img/implantacion/render_admin.png)

### Seguridad y mantenimiento de la base de datos

A nivel de seguridad de la base de datos, la aplicación se encuentra mucho más limitada ya que la capa gratuita que ofrece [Clever Cloud](https://console.clever-cloud.com/) nos proporciona soporte mínimo, teniendo restringidas áreas de gestión y mantenimiento. Aun así, lo óptimo y esencial que deberíamos realizar en nuestra base de datos incluye:

1. **Registro de Incidencias**:
   - **Inserción de Información sobre Incidencias**: Documentar cualquier problema o fallo que ocurra en la base de datos.
   - **Acceso a los Logs**: Revisar estos logs regularmente para monitorear la actividad de la base de datos y detectar cualquier comportamiento anómalo.
   - **Alertas y Notificaciones**: Configurar alertas para notificar a los administradores de la base de datos sobre cualquier incidencia crítica o irregularidad detectada.

2. **Creación de Copias de Seguridad**:
   - **Backups Automatizados**: Configurar copias de seguridad automáticas en intervalos regulares para asegurar que los datos puedan ser restaurados en caso de pérdida o corrupción.
   - **Verificación de Backups**: Realizar pruebas periódicas de restauración de las copias de seguridad para asegurar su integridad y funcionalidad.
   - **Almacenamiento Seguro de Backups**: Guardar las copias de seguridad en ubicaciones seguras para evitar la pérdida de datos en caso de desastres.

3. **Gestión de Acceso y Permisos**:
   - **Control de Acceso**: Implementar políticas de control de acceso, asegurando que solo usuarios autorizados puedan acceder a la base de datos.
   - **Roles y Permisos**: Definir roles y asignar permisos mínimos necesarios para cada usuario.
   - **Auditoría de Accesos**: Mantener registros de quién accede a la base de datos y qué acciones realizan.

4. **Seguridad de la Conexión**:
   - **Conexiones Seguras**: Utilizar conexiones cifradas (por ejemplo, SSL/TLS) para proteger los datos en tránsito entre la aplicación y la base de datos.
   - **Rotación de Credenciales**: Cambiar regularmente las contraseñas y credenciales de acceso a la base de datos para minimizar el riesgo de acceso no autorizado.

5. **Monitoreo y Mantenimiento**:
   - **Monitoreo Continuo**: Utilizar herramientas de monitoreo para vigilar el rendimiento y la salud de la base de datos en tiempo real.
   - **Mantenimiento Regular**: Realizar tareas de mantenimiento periódicas, como la optimización de consultas y la limpieza de datos innecesarios, para mantener el rendimiento óptimo de la base de datos.

![Imagen de la sección de administración que proporciona Clever Cloud](/doc/img/implantacion/clever_cloud_admin.png)

### Gestión de Incidencias: Sistema y Fallos de Software

La gestión efectiva de incidencias es crucial para mantener la estabilidad, seguridad y funcionalidad de cualquier sistema y software. A continuación se describen las mejores prácticas para gestionar incidencias relacionadas con el sistema, como accesos no autorizados a la base de datos, y fallos de software.

#### Gestión de Incidencias del Sistema

1. **Detección y Notificación**:
   - **Monitoreo Continuo**: Implementar sistemas de monitoreo en tiempo real para detectar accesos no autorizados y otras actividades sospechosas en la base de datos.
   - **Alertas Automáticas**: Configurar alertas que notifiquen inmediatamente a los administradores de cualquier intento de acceso no autorizado o actividad inusual.

2. **Evaluación y Clasificación**:
   - **Evaluación Inicial**: Analizar rápidamente la incidencia para determinar su naturaleza y alcance. Identificar si se trata de un acceso no autorizado, un ataque en curso, o una falsa alarma.
   - **Clasificación**: Clasificar la incidencia según su gravedad (alta, media, baja) para priorizar la respuesta.

3. **Respuesta Inmediata**:
   - **Bloqueo de Acceso**: Cambiar las credenciales comprometidas y bloquear las cuentas afectadas para detener el acceso no autorizado.
   - **Recolección de Evidencias**: Recopilar y preservar evidencias (logs, registros de acceso) para una posible investigación.

4. **Aplicar soluciones**:
   - **Corrección de Vulnerabilidades**: Identificar y corregir las vulnerabilidades que permitieron el acceso no autorizado. Esto puede incluir la aplicación de parches de seguridad, mejora de configuraciones, etc.
   - **Revisión de Políticas**: Revisar y actualizar las políticas de seguridad para prevenir futuras incidencias similares.

5. **Comunicación y Reporte**:
   - **Informar a las Partes Interesadas**: Notificar a los usuarios afectados sobre la incidencia.
   - **Reporte Detallado**: Documentar el incidente, incluyendo la causa raíz, la respuesta, y las acciones correctivas implementadas.

#### Gestión de Fallos de Software

1. **Detección y Notificación**:
   - **Sistema de Monitoreo**: Utilizar herramientas de monitoreo y logs para detectar fallos de software en tiempo real.
   - **Reportes de Usuarios**: Facilitar un canal para que los usuarios reporten errores y fallos que encuentren mientras usan la aplicación. Por ejemplo un email de contacto o a través de redes sociales.

2. **Evaluación y Clasificación**:
   - **Análisis Inicial**: Revisar los Logs para determinar la naturaleza del problema y su impacto en el sistema y los usuarios.
   - **Clasificación**: Clasificar los fallos según su severidad (alta, media, baja) y urgencia para priorizar su resolución.

3. **Diagnóstico y Solución**:
   - **Reproducción del Fallo**: Intentar reproducir el fallo en un entorno de pruebas para entender su causa raíz.
   - **Desarrollo de Solución**: Implementar una solución temporal si es necesario y trabajar en una solución definitiva.
   - **Pruebas de Verificación**: Probar la solución en un entorno controlado para asegurar que el fallo se ha corregido y no ha introducido nuevos problemas.

4. **Implementación de la Solución**:
   - **Despliegue de Parches**: Desplegar la solución en el entorno de producción.
   - **Monitoreo Post-Despliegue**: Monitorear el sistema después del despliegue para confirmar que el fallo se ha resuelto y que el sistema funciona correctamente.

5. **Documentación y Mejora Continua**:
   - **Documentación**: Documentar el fallo, su causa raíz, la solución implementada, y cualquier lección aprendida.
   - **Retroalimentación**: Utilizar la información recopilada para mejorar los procesos de desarrollo y prueba, evitando futuros fallos similares.


### Gestión de la Introducción de Nuevas Funcionalidades

La introducción de nuevas funcionalidades en una aplicación es un proceso esencial para mantenerla competitiva, mejorar la experiencia del usuario y satisfacer nuevas necesidades. 

#### Etapas en la Introducción de Nuevas Funcionalidades

1. **Identificación y Planificación**:
   - **Análisis de Requisitos**: Identificar las necesidades y deseos de los usuarios a través de encuestas, retroalimentación y análisis de uso. Definir claramente los requisitos funcionales y no funcionales.
   - **Priorización**: Evaluar la importancia y urgencia de las nuevas funcionalidades. Priorizar las características en función de su valor para el usuario y el impacto en el negocio.
   - **Planificación del Proyecto**: Crear un plan detallado que incluya un cronograma, recursos necesarios, y hitos importantes.

2. **Diseño y Desarrollo**:
   - **Diseño de la Funcionalidad**: Crear diagramas de flujo, y prototipos para visualizar cómo se integrará la nueva funcionalidad en la aplicación existente.
   - **Desarrollo**: Asignar tareas de desarrollo y comenzar la implementación de la nueva funcionalidad. 
   - **Pruebas Unitarias**: Realizar pruebas unitarias para asegurar que cada componente de la nueva funcionalidad funciona correctamente de forma aislada.

3. **Pruebas e Integración**:
   - **Pruebas de Integración**: Verificar que la nueva funcionalidad se integra sin problemas con el sistema existente. Realizar pruebas de integración para asegurar que los diferentes módulos funcionan correctamente juntos.
   - **Pruebas de Usuario**: Implementar pruebas beta o pilotos con un grupo de usuarios seleccionados para recibir retroalimentación directa sobre la nueva funcionalidad.
   - **Pruebas de Rendimiento**: Evaluar el impacto de la nueva funcionalidad en el rendimiento general del sistema. 

4. **Despliegue y Monitoreo**:
   - **Despliegue Controlado**: Realizar un despliegue gradual o en fases para minimizar riesgos. 
   - **Monitoreo Post-Despliegue**: Utilizar herramientas de monitoreo para rastrear el rendimiento de la nueva funcionalidad en el entorno de producción.
   - **Soporte y Mantenimiento**: Estar preparado para ofrecer soporte a los usuarios y resolver cualquier incidencia que surja tras la implementación de la nueva funcionalidad.

5. **Revisión y Mejora Continua**:
   - **Revisión de Retroalimentación**: Analizar la retroalimentación de los usuarios y los datos de uso para evaluar el éxito de la nueva funcionalidad.
   - **Mejora Continua**: Utilizar el feedback recibido para realizar mejoras continuas.

### Protección de Datos de Carácter Personal

La protección de los datos personales es fundamental para garantizar la privacidad y seguridad de los usuarios. A continuación se detallan las medidas implementadas para cumplir con las normativas y asegurar el tratamiento adecuado de los datos.

1. **Cumplimiento Normativo**:
   - **GDPR**: La aplicación cumple con el **Reglamento General de Protección de Datos (GDPR)** de la Unión Europea. Los usuarios tienen derecho a acceder, corregir, eliminar y gestionar sus datos personales en todo momento.
   - **Política de Privacidad**: Al registrarse, los usuarios aceptan la **Política de Privacidad**, que detalla cómo se recopilan y utilizan sus datos personales, además de sus derechos.

2. **Recopilación y Uso de Datos Personales**:
   - **Consentimiento Explícito**: Los usuarios deben dar su consentimiento explícito para la recopilación de datos personales al registrarse.
   - **Minimización de Datos**: Solo se recopilan los datos estrictamente necesarios para el funcionamiento de la aplicación.

3. **Seguridad de los Datos**:
   - **Cifrado de Datos**: Todos los datos personales son cifrados tanto en tránsito como en reposo para protegerlos de accesos no autorizados.
   - **Acceso Restringido**: Solo el personal autorizado y los sistemas que lo necesiten tienen acceso a los datos personales.

4. **Derechos de los Usuarios**:
   - **Acceso, Rectificación y Eliminación**: Los usuarios pueden acceder, corregir o eliminar sus datos en cualquier momento a través de la configuración de su cuenta.
   - **Derecho a la Portabilidad**: Los usuarios pueden recibir sus datos personales en un formato estructurado para transferirlos a otro proveedor si lo desean.

5. **Eliminación de la Cuenta**:
   - **Eliminación de Datos**: Al eliminar su cuenta, todos los datos personales y comentarios del usuario son eliminados permanentemente de la base de datos.

## Manual de usuario

### Formación de Usuarios

No será necesario formar a los usuarios de la aplicación, ya que está diseñada para ser intuitiva y accesible a un público amplio, de diferentes edades y géneros. La interfaz es fácil de usar, y las funcionalidades están orientadas a que cualquier usuario, independientemente de su experiencia tecnológica, pueda utilizarla sin dificultades. No obstante, se proporcionará soporte en línea y documentación accesible en caso de que los usuarios necesiten ayuda adicional.

### Manual de Usuario y Guía de Uso

Para garantizar que los usuarios comprendan fácilmente cómo utilizar la aplicación, se implementará una pequeña **guía escrita** accesible a todos los públicos. Esta guía estará disponible en el enlace de descarga de la aplicación (en la **Play Store** o plataformas similares) y explicará de manera clara y concisa:

- **Funcionamiento de la Aplicación**: Un resumen de las principales funcionalidades de la app, cómo navegar por la interfaz y cómo realizar las acciones básicas.
- **Finalidad de la Aplicación**: Descripción breve de los objetivos de la aplicación y cómo puede beneficiar al usuario.
- **Preguntas Frecuentes (FAQ)**: Respuestas a las dudas más comunes que los usuarios pueden tener sobre el uso de la app.

La guía estará redactada de forma simple y comprensible, para que usuarios de diferentes edades y niveles de experiencia tecnológica puedan seguirla sin problemas.

