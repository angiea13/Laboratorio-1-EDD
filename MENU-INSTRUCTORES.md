# CRUD de instructores desde el menú

Compilar con Java 17 y Maven:

```powershell
mvn test
java -cp target/classes core.main.Main
```

Las opciones 1 a 5 usan `ArchivoInstructoresIndexado`: registrar, consultar,
modificar, eliminar y listar. La opción 6 consulta ese mismo archivo mediante
el índice por especialidad; los cambios de especialidad y las bajas actualizan
el índice. Las opciones 7 y 8 mantienen las consultas de sesiones y el reinicio
mensual de aprendices. Este menú sigue siendo de consola.

En el primer arranque se importan los instructores activos de
`datos/instructores.dat` a `datos/instructores-indexados.dat`. El original se
conserva como respaldo y no vuelve a importarse si la copia nueva ya existe.
El índice se guarda en `datos/instructores-indexados.idx` y puede reconstruirse
desde el archivo de datos al iniciar. Todos los cambios posteriores del menú
se guardan en el archivo nuevo, con registros de 154 bytes.

El formato nuevo conserva los tipos del CRUD: cédula `int` positiva, teléfono
`long` positivo, nombre de hasta 50 caracteres, especialidad de hasta 20 y entre
0 y 15 sesiones. Si un dato anterior no cabe o perdería ceros iniciales, la
importación se detiene con un mensaje y conserva el original; no publica una
copia parcial. Los archivos de ambos formatos no se deben intercambiar.

Se puede indicar otra carpeta de datos para hacer prácticas sin afectar los
archivos habituales:

```powershell
java -cp target/classes core.main.Main target/practica
```

Las pruebas de Maven incluyen el flujo del menú y verifican persistencia,
reportes, rechazo de entradas incorrectas y conservación del archivo anterior.

## Aprendices y validaciones

Las opciones 9 a 13 permiten registrar, consultar, modificar, eliminar y listar
aprendices. Cada aprendiz conserva un contador de 0 a 4 por especialidad.
Para modificar se introduce la cédula existente y la lista completa de
especialidades y contadores que debe quedar guardada.

REQ-006 utiliza el archivo existente `datos/aprendices.dat`, sin cambiar el
tamaño de sus registros. La baja lógica vacía el campo cédula; al reabrir,
esos registros se excluyen del índice, del listado y del reinicio mensual.
Se puede volver a registrar una cédula eliminada como un registro nuevo.

REQ-009 valida los datos antes de crear o modificar: nombre obligatorio de hasta
80 caracteres, cédula de 6 a 12 dígitos, teléfono de 7 a 10 dígitos, ambos
distintos de cero, y al menos una especialidad del catálogo. El catálogo inicial
es Música, Pintura, Escritura y Baile, definido en `CatalogoEspecialidades.java`.
Acepta diferencias de mayúsculas, espacios exteriores y `musica` sin tilde;
rechaza especialidades repetidas después de normalizar y contadores fuera de rango.

REQ-005 identifica el campo inválido de instructor y rechaza campos vacíos,
teléfonos con formato incorrecto, textos demasiado largos y sesiones fuera de
0 a 15. El archivo numérico nuevo mantiene la cédula positiva de tipo `int`;
la implementación anterior conserva su validación de cédula de 6 a 12 dígitos.
Las validaciones nuevas usan ciclos y comparaciones, sin expresiones regulares.

Estos cambios no incluyen asignación de sesiones ni interfaz gráfica. Los
estados de ClickUp se mantienen sin cambios hasta la revisión del equipo.
