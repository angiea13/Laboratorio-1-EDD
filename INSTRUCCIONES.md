# LABORATORIO 1: MANEJO DE ARCHIVOS EN JAVA

## Generalidades

Número máximo de integrantes: 3  
Fecha de entrega: semana 7   
Lenguaje de programación utilizado: Java  
Modalidad de entrega: sustentación presencial

**Objetivo:** desarrollar las competencias en análisis, diseño e implementación de software aplicadas a la resolución de problemas. 

**Actividad:** desarrollar un código en lenguaje Java que evidencie el manejo de archivos indexados, cumpliendo con los requerimientos solicitados.

## Caso de estudio: Gestión de Archivos para la Academia de Artes Barranquilla

### 

Actualmente, la Academia de Artes Barranquilla maneja el registro de sus instructores personalizados y usuarios a través de archivos físicos, lo que ha generado dificultades en la organización y programación de entrenamientos. Para mejorar la eficiencia, han decidido contratar a un equipo de desarrollo para crear un software que permita gestionar las sesiones de práctica de manera más rápida y eficiente.

Cada instructor puede atender hasta 15 sesiones al mes, por lo que el archivo de instructor debe reiniciarse cada mes.

Los archivos que maneja la academia son:

* **Archivo Instructores:** Guarda los datos de los instructores como nombre, cédula, especialidad (música, pintura, escritura, baile, etc.), teléfono y cantidad de sesiones realizadas en el mes. Este último registro se actualiza según la programación de sesiones.

* **Archivo aprendices:** Guarda los datos de los aprendices como nombre, cédula, especialidad/es practicada/s y cantidad de sesiones realizadas en el mes. Cada aprendiz puede tener un máximo de 4 sesiones al mes por cada especialidad.

* **Archivo sesiones:** Guarda los datos de las sesiones asignadas a los aprendices con información de la sesión como: código único de la sesión, nombre y cédula del aprendiz, especialidad, instructor asignado y fecha de la sesión de entrenamiento.

**Diseño del Software:**

El usuario final del software es el **administrador de la academia**, quien gestionará las sesiones y asegurará que cada instructor y aprendiz cuente con disponibilidad adecuada.

## Requerimientos Funcionales

Para el desarrollo de este laboratorio, se deben utilizar los comandos de Java para **manejo de archivos indexados**.

Con este software se pueden realizar las siguientes tareas:

1. Asignar, consultar y cancelar una sesión a un aprendiz. Si la fecha de la sesión es anterior a la fecha actual, no se puede eliminar porque ya se realizó.

2. CRUD (Create, Read, Update, Delete)  del archivo de instructores y aprendices.

3. Reiniciar el archivo de instructores (colocar el campo “cantidad de sesiones realizados” en 0 para todos los instructores) y reiniciar el archivo de aprendices.

**Proceso de Asignación de una Sesión**

1. Se solicitan los datos del aprendiz y se verifica que esté registrado en el archivo. Si el aprendiz ya cumplió con las sesiones programadas por mes, no se puede asignar una nueva. Datos solicitados: **nombre, cédula y especialidad solicitada**.

2. Con este último dato, se busca en el archivo de instructores los que cuentan con esa especialidad.

3. Se muestran al usuario los instructores disponibles. Un instructor está disponible si tiene menos de 15 sesiones programadas en el mes.

4. Si no hay instructores disponibles, no se registra la sesión.

5. El administrador selecciona el instructor disponible para el aprendiz.

6. El **archivo de instructores** es modificado en el campo “cantidad de sesiones realizadas”, al igual que el archivo de aprendices y se agrega el registro de la sesión en el **archivo de sesiones**.

## Requerimientos No Funcionales

1. **Validación de datos:**

   * No se pueden asignar más sesiones a un instructor que ya tiene 15 programadas en el mes.

   * No se pueden registrar sesiones duplicadas para el mismo instructor o aprendiz.

   * Solo se pueden eliminar sesiones con fecha futura.

   * En los casos que se quiera agregar un nuevo registro, se debe validar que el dato no exista en los registros actuales. Mostrar un mensaje al usuario que advierta que ya existe ese registro. 

   * En los casos que se quiera eliminar un registro, se debe verificar que el registro existe en el archivo.

2. **Interfaz interactiva:** El desarrollo debe incluir una **interfaz gráfica** donde se presente un menú al usuario para que pueda escoger la operación que desea realizar. 

3. Tener un **manejo adecuado de** **errores** cuando se presenten los siguientes casos: “archivo no encontrado”, “archivo con registros errados” y los que usted considere. No ingresar un registro si contiene algún error: por ejemplo, una cédula mal digitada, una fecha errada o un campo vacío.   
4. **Código comentado** adecuadamente sobre los procedimientos y estructuras utilizadas.

5. **Optimización del acceso a la información:**

   * Visualización rápida de instructores disponibles.

   * Consulta eficiente de sesiones programados.

   * Generación de reportes sobre disponibilidad de entrenadores y sesiones asignadas.

## **Evaluación tridimensional**

La evaluación del laboratorio será de la siguiente forma:

| Dimensión | Componente | Porcentaje |
| :---- | :---- | :---- |
| El producto (lo que hizo) | Requerimientos funcionales | 50% |
| El producto (lo que hizo) | Requerimientos no funcionales | 20% |
| El Proceso (cómo lo hizo) | Metodología de diseño (algoritmos y estructuras). Seguimiento. | 20% |
| El discurso (justificación de porqué lo hizo así) (Individual) | Sustentación (manejo del tema y explicación) | 10% |

### 

## **Bonos**

Presentación personal: \+0.1

Mejor interfaz gráfica (diseño, colores, tamaño de letra, usabilidad): \+0.2

Millas extras: Realizar tareas adicionales a las pedidas \+0.1 por cada tarea, hasta un máximo de \+0.3.

