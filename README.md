
# MarneWeather

## Descripción
MarneWeather es una aplicación Android que proporciona información sobre el clima basándose en tu ubicación actual. 
Además de mostrarte el clima de tu zona, en el detalle de la aplicación se realiza una llamada a la API de GPT, 
la cual te sugiere qué tipo de ropa deberías llevar de acuerdo al clima actual. También es posible buscar otras ciudades y 
añadirlas a favoritas desde la sección de detalles.

## Arquitectura y Organización del Proyecto
El proyecto está estructurado siguiendo los principios de la Clean Architecture, y se ha organizado en los siguientes paquetes:

- `DI`: Relacionado con la inyección de dependencias.
- `Domain`: Contiene las reglas de negocio y las entidades.
- `Data`: Se encarga de los datos, ya sea obtenidos de fuentes locales o remotas.
- `Presentation`: Contiene las clases relacionadas con la interfaz de usuario y la presentación de
  datos.
- `Model`: Integrado dentro de cada módulo, contiene los modelos que necesita cada capa.

## Librerías Utilizadas
- **Koin**: Para la inyección de dependencias.
- **Room**: Biblioteca para persistencia de datos.
- **Gson**: Para la serialización y deserialización de objetos JSON.
- **Coroutines**: Para la programación asíncrona y concurrente en Kotlin.
- **Retrofit**: Cliente HTTP para Android y Java.
- **WorkManager**: Para la ejecución de tareas en segundo plano.
- **Coil**: Para la carga y visualización de imágenes.
- **Compose con Material 3**: Framework moderno de UI para Android.

## Recursos Utilizados

- **DALL·E 3**: Se utilizó para generar recursos basados en prompts del clima.

> [!IMPORTANT]
>## Configuración Previa
>Antes de ejecutar la aplicación, es necesario introducir tu __`GPT_API_KEY`__ en el archivo
> [TextGeneratorClientConstants](com.marneux.marneweather.data.remote.languagemodel.
> TextGeneratorClientConstants)

> [!NOTE]
> ### Notas sobre GPT
> GPT, al ser una API relativamente reciente, puede presentar ocasionalmente fallos o retardos en
> sus
> respuestas.
> En consecuencia, hay situaciones en las que la API podría no devolver texto.

> Para gestionar la presentación de textos devueltos por GPT en la aplicación,
> se emplean literales de cadenas de texto multilínea (""" """) en Kotlin. Esto permite
> representar el
> texto exactamente como se recibe, manteniendo su formato original. Para más información sobre
> literales de
> cadenas de texto multilínea en Kotlin, se puede
> consultar [esta fuente](https://realkotlin.com/tutorials/2018-06-26-multiline-string-literals-in-kotlin/).
