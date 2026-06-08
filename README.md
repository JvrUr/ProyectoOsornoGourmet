#Osorno Gourmet

Aplicación móvil Android desarrollada en **Kotlin** con **Jetpack Compose** para explorar rutas de locales de comida en la ciudad de **Osorno, Chile**.

## Capturas de Pantalla

La app incluye las siguientes pantallas:
- **Login / Registro** → Autenticación de usuarios
- **Home** → Dashboard con resumen de locales y rutas
- **Locales de Comida** → CRUD completo con filtro por categoría
- **Rutas** → Crear recorridos gastronómicos personalizados
- **Mapa** → Google Maps con marcadores y polilíneas de rutas

## Arquitectura

### Clean Architecture (3 Capas)
```
┌─────────────────────────────────┐
│   Presentation (UI + MVVM)      │  → Compose Screens + ViewModels
├─────────────────────────────────┤
│   Domain (Negocio)              │  → Models + UseCases + Interfaces
├─────────────────────────────────┤
│   Data (Persistencia)           │  → Room DB + Repository Impl
└─────────────────────────────────┘
```

### MVVM (Model-View-ViewModel)
- **Model**: Entidades de dominio (`User`, `FoodPlace`, `Route`)
- **View**: Composables (pantallas declarativas)
- **ViewModel**: Lógica de presentación con `StateFlow` (patrón Observer)

## Patrones de Diseño

| Patrón | Implementación |
|--------|---------------|
| **Repository** | Abstracción de acceso a datos Room |
| **Observer** | `StateFlow` en ViewModels → UI reactiva |
| **Factory** | `ViewModelProvider.Factory` para crear ViewModels |
| **Singleton** | Instancia única de `AppDatabase` (Room) |

## Principios SOLID

| Principio | Aplicación |
|-----------|-----------|
| **S** - Single Responsibility | Cada clase (ViewModel, UseCase, Repository) tiene una sola responsabilidad |
| **O** - Open/Closed | `FoodPlaceFormScreen` reutilizado para crear y editar sin modificación |
| **L** - Liskov Substitution | Las implementaciones de repositorios son intercambiables |
| **I** - Interface Segregation | Interfaces separadas para `UserRepository`, `FoodPlaceRepository`, `RouteRepository` |
| **D** - Dependency Inversion | Los UseCases dependen de interfaces, no de implementaciones concretas |


## Estructura del Proyecto

```
app/src/main/java/com/OsornoGourmet/
├── data/                           
│   ├── local/
│   │   ├── AppDatabase.kt         
│   │   ├── FoodPlaceSeeder.kt     
│   │   ├── Mappers.kt             
│   │   ├── dao/                   
│   │   └── entity/                
│   └── repository/                
├── domain/                        
│   ├── model/                     
│   ├── repository/                
│   └── usecase/                   
│       ├── auth/                  
│       ├── foodplace/             
│       └── route/                 
├── presentation/                  
│   ├── theme/                     
│   ├── viewmodel/                
│   ├── navigation/                
│   ├── ui/
│   │   ├── auth/                  
│   │   ├── home/                  
│   │   ├── foodplace/             
│   │   ├── route/                 
│   │   └── map/                   
│   └── MainActivity.kt           
└── OsornoGourmetApp.kt        
```

## 🚀 Configuración

### 1. Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/Proyecto-Kotlin.git
```

### 2. Abrir en Android Studio
- Abrir el proyecto con Android Studio Hedgehog o superior
- Sincronizar Gradle
- Ejecutar en un emulador o dispositivo físico

## 📍 Datos Pre-cargados

La app incluye 12 locales de comida reales de Osorno:
- Mercado Municipal de Osorno
- Café Haussman
- La Parrilla de Toño
- Restaurante Bavaria
- El Fogón Sureño
- Donde la Negra
- Café Central
- Fuente Alemana Osorno
- Pastelería Maestranza
- Rincón Cervecero
- Emporio Sureño

## Javier Uribe

Desarrollo de Aplicaciones Móviles con Kotlin

