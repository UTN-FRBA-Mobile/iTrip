# Configurar un proyecto en android

## **Programas y plugins a instalar**

**Android studio**

Descargar la última version de android studio https://developer.android.com/studio y ejecutarlo.

Asegurarse que, durante la instalación, en la pestaña de selección de componentes "android virtual devices" este marcado

**Plugins**

JsonToKotlinClass

Descargar JsonToKotlinClass v2.2.0 plugin manualmente (La versiones mas nuevas personalmente me tirar errores críticos en Android studio) desde la siguiente url [http://production-active.plugins.aws.intellij.net/plugin/9960-json-to-kotlin-class-jsontokotlinclass-/versions](http://production-active.plugins.aws.intellij.net/plugin/9960-json-to-kotlin-class-jsontokotlinclass-/versions)

Descomprimir el archivo en una carpeta

En Android studio ir a opciones (ctrl+alt+s ) y en la parte de plugins seleccionar “install plugin from disk” dentro de “Mas opciones” o el engranaje de opciones. Luego seleccionar el archivo descargado e instalarlo.

Kotlin

En caso de no tener ya el plugin de kotlin instalado instalarlo manualmente desde opciones (ctrl+alt+s )->plugins y en la barra de búsqueda escribir kotlin. Seleccionarlo e instalarlo.

## **Configurar un celular para testear la app**

Existen 2 maneras de probar nuestra app. De manera virtual o con un dispositivo celular con Android conectado a la pc.

Con Dispositivo Celular

Ir a Opciones del celular -> Información sobre el teléfono -> seleccionar el número de compilación unas 10 veces hasta que habilite el modo desarrollador. En caso de no encontrar buscar la manera de habilitar el modo desarrollador de tu teléfono en internet.

Una vez habilitado el modo desarrollador este se encontrará en opciones del sistema. Ingresar y activar la opción de depurar USB

Una vez activada la opción conectarlo al pc a través del usb y aceptar la petición de depurar/debuguear el celular a través de la pc.

Ahora desde Android studio, si no se buildeo la app buildearla(el martillo) para corroborar que no haya problemas. Luego seleccionar “correr la aplicación” con el botón de play. A continuación, aparecerá una ventana de dispositivos habilitados para instalar y correr la aplicación. Seleccione el modelo de su celular y seleccione siguiente. Ahora Android studio instalara la app en el celular y lo ejectuara.

Para debuguear son lo mismos pasos pero ejecutando el Bug(botón de insecto)

NOTA: Siempre tener el celular desbloqueado en todo momento a la hora de buildear o debuguear

Con Dispositivo Virtual

Nota: Este método funciona de manera horrenda para procesadores AMD ya que todavía está en desarrollo. Pero para procesadores INTEL funciona de maravilla.

Seleccionar “correr la aplicación” con el botón de play. A continuación, aparecerá una ventana de dispositivos habilitados para instalar y correr la aplicación. En caso de que no haya dispositivos activados o ninguno sea conforme a lo que necesita crear uno seleccionando “crear nuevo dispositivo virtual”.

Elegimos el modelo de dispositivo que deseamos emular y seleccionamos “Siguiente”

Elegimos el sistema operativo que necesitemos y le damos a “siguiente” y finalizar.

Android studio instalará el emulador y luego podrá seleccionarlo para ejecutarlo.