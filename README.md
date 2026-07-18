# Laboratorio Android 08 — Práctica de Código (Kotlin)

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=flat&logo=android&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=flat&logo=gradle&logoColor=white)

Octavo laboratorio del curso de desarrollo Android. Contiene ejercicios de código orientados a patrones de diseño, manejo eficiente de datos y componentes de UI avanzados, aplicando buenas prácticas de organización del código en Kotlin.

## 📚 Temas cubiertos

- Patrones de diseño aplicados a Android
- Manejo y validación de formularios
- Componentes de UI de Material Design
- Buenas prácticas de organización de código

## 🛠️ Tecnologías

| Tecnología | Uso |
|---|---|
| Kotlin | Lenguaje principal del proyecto |
| Android SDK | Plataforma de desarrollo |
| Gradle (Kotlin DSL) | Sistema de construcción (`build.gradle.kts`) |
| Material Design | Componentes de interfaz de usuario |

## ✅ Requisitos previos

Antes de ejecutar el proyecto asegúrate de tener instalado:

- **Android Studio** (versión reciente recomendada)
- **JDK 17** (incluido con las versiones actuales de Android Studio)
- **Android SDK** (se instala/gestiona desde Android Studio)
- Un **emulador de Android** configurado en el AVD Manager o un **dispositivo físico** con la depuración USB habilitada

## 🚀 Instalación y ejecución

1. Clona el repositorio:

   ```bash
   git clone https://github.com/NinaDIV/Android-Lab08-Code-Practice.git
   cd Android-Lab08-Code-Practice
   ```

2. Abre el proyecto en Android Studio (`File > Open` y selecciona la carpeta del repositorio).

3. Espera a que Gradle sincronice las dependencias automáticamente (primera vez puede tardar unos minutos).

4. Selecciona un emulador o dispositivo físico y ejecuta la app con el botón **Run** (▶) o con:

   ```bash
   ./gradlew installDebug
   ```

5. Al iniciar, la aplicación se instala y abre en el emulador o dispositivo mostrando la pantalla principal del laboratorio.

Para generar un APK de depuración desde la terminal:

```bash
./gradlew assembleDebug
# El APK queda en app/build/outputs/apk/debug/
```

## 📁 Estructura del proyecto

```
Android-Lab08-Code-Practice/
├── app/                   # Módulo principal de la aplicación (código Kotlin y recursos)
├── gradle/                # Wrapper y configuración de Gradle
├── build.gradle.kts       # Configuración de build a nivel de proyecto
├── settings.gradle.kts    # Definición de módulos del proyecto
├── gradle.properties      # Propiedades de Gradle
├── gradlew / gradlew.bat  # Scripts del wrapper de Gradle (Linux/macOS y Windows)
└── README.md
```
