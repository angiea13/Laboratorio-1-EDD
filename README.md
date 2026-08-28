# LABORATORIO 1: Academia de Artes Barranquilla - Sistema de Gestión de Archivos

Sistema de gestión de sesiones, instructores y aprendices desarrollado en **Java** con **JavaFX**, implementando manejo avanzado de archivos indexados para la **Academia de Artes Barranquilla**.

---

## Descripción del Proyecto

Este software forma parte del **Laboratorio 1** de **Estructuras de Datos 1**. Su propósito es automatizar el registro, control y programación de entrenamientos y sesiones artísticas de la academia, reemplazando el sistema tradicional de archivos físicos. Permite al administrador gestionar instructores, aprendices y sesiones de manera eficiente, garantizando la integridad de los datos mediante archivos indexados y validaciones rigurosas.

---

## Tecnologías y Requisitos

* **Lenguaje:** Java (JDK 17 o superior recomendado)
* **Interfaz Gráfica:** JavaFX 17+
* **Entorno de Desarrollo:** Visual Studio Code (VS Code) con Extension Pack for Java
* **Control de Archivos:** Archivos secuenciales indexados (`RandomAccessFile` / archivos binarios/texto estructurados)

---

## Estructura del Proyecto

```text
WIP/ #agregar aquí carpetas y cosas del proyecto
```

---

## Funcionalidades Principales

### 1. Gestión de Instructores (CRUD)
* Registro de instructores (Nombre, Cédula, Especialidad, Teléfono, Sesiones realizadas).
* Validación de cédulas únicas y campos obligatorios.
* Control estricto del límite mensual (máximo 15 sesiones por instructor).

### 2. Gestión de Aprendices (CRUD)
* Registro de aprendices (Nombre, Cédula, Especialidades practicadas, Sesiones realizadas).
* Control de límite por especialidad (míximo 4 sesiones al mes por cada especialidad).

### 3. Asignación y Control de Sesiones
* **Asignación inteligente:** Valida disponibilidad del aprendiz (límite de 4 sesiones) y busca instructores con la especialidad requerida que tengan menos de 15 sesiones en el mes.
* **Consulta:** Visualización rápida de sesiones programadas.
* **Cancelación:** Permite eliminar una sesión **únicamente si la fecha es futura** (regla de negocio no funcional).
* **Prevención de duplicados:** Evita asignar sesiones duplicadas para el mismo instructor o aprendiz en la misma fecha/hora.

### 4. Mantenimiento y Reinicio Mensual
* Reinicio del archivo de instructores (resetea el contador de sesiones a 0).
* Reinicio del archivo de aprendices.

---

## Guía de Instalación y Ejecución en VS Code

### Paso 1: Clonar o descargar el repositorio
Abre tu terminal en VS Code y clona el repositorio:
```bash
git clone https://github.com/angiea13/Laboratorio-1-EDD.git
```

### Paso 2: Configurar JavaFX en VS Code
1. Asegúrate de tener instalada la extensión **Extension Pack for Java** y **JavaFX Support**.
2. Configura las librerías de JavaFX en tu `launch.json` o mediante tu gestor de dependencias (Maven / Gradle) si aplica, o añade los archivos JAR de JavaFX al classpath del proyecto.

### Paso 3: Ejecutar la Aplicación
Compila y ejecuta la clase principal `Main.java` desde VS Code.

---

## Diseño y Arquitectura

* **Patrón DAO (Data Access Object):** Separa la lógica de negocio del acceso a archivos físicos indexados, optimizando la búsqueda y actualización de registros.
* **Manejo de Errores:** Control robusto de excepciones para archivos no encontrados, registros corruptos, campos vacíos y validación estricta de formatos (fechas, cédulas numéricas).
* **Interfaz Gráfica Interactiva (JavaFX):** Diseñada bajo principios de usabilidad, con menús limpios, alertas de validación y componentes visuales atractivos.

---

## Autores

* **Angie Arteta González** - Estructuras de Datos 1
* **Valerie Gómez Echeverría** - Estructuras de Datos 1
* **Luis Lafaurie Yaguna** - Estructuras de Datos 1

---

## Institución
**Universidad:** Facultad de Ingeniería  
**Curso:** Estructuras de Datos 1  
**Semana:** 7 (Entrega y Sustentación Presencial)
