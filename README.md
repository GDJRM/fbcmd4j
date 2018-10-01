# fbcmd4j
Cliente de Facebook en línea de comando para la evidencia del curso "**Computación en Java CS13303**"

## Instalación
**Clonar el repositorio**  
```
git clone https://github.com/DavidServn/fbcmd4j.git
```

**Importar a Eclipse**  
1. Dar clic en `File -> Import -> Existing Projects into Workspace`.
2. Seleccionar la carpeta raíz donde se clonó el proyecto.

**Exportar .jar**  
1. Dar clic derecho al proyecto en Eclipse.
2. Seleccionar `Export -> Runnable JAR file`.

**Ejecutar .jar**  
1. Abrir la carpeta `bin`.
2. Abrir la terminal (*utilizar privilegios de administrador en Windows*).
3. Navegar a la carpeta `fbcmd4j/bin` donde se encuentra el archivo `fbcmd4j.jar`.
4. Ejecutar el comando `java -jar fbcmd4j.jar`.


## Uso
**Configurar la cuenta de Facebook**   
1. Seleccionar la opción 0 `Configurar Cliente`.
2. Ir a la página web que se muestra.
3. Escribir el código que generó la aplicación.
4. Aceptar y dar permisos a la aplicación.

**Obtener el NewsFeed**   
1. Seleccionar la opción 1 `NewsFeed`.
2. Escribir 'Si' en caso de querer guardar los posts en un archivo.
	* Escribir el número de posts a guardar.

**Obtener el Wall**   
1. Seleccionar la opción 2 `Wall`.
2. Escribir 'Si' en caso de querer guardar los posts en un archivo.
	* Escribir el número de posts a guardar.

**Publicar un Estado**   
1. Seleccionar la opción 3 `Publicar Estado`.
2. Escribir el estado deseado.

**Publicar un Link**   
1. Seleccionar la opción 4 `Publicar Link`.
2. Escribir el link deseado.


## Créditos
Desarrollado por:
- **2686599** David Elí Servín Peña


## Licencia
El código está disponible bajo la licencia **GNU GPL-3.0**. Consulte el archivo LICENSE en la raíz del proyecto para más información.