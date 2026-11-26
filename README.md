# CupHead Clicker

Tap game (clicker) para Android hecho en **Java** con **Android Studio**, con estética de dibujos animados de los años 30 (estilo *rubber hose*). Práctica de PMDM de 2º DAM — CampusFP Humanes.

> Proyecto académico. Todos los personajes y gráficos son originales; no se usa ningún recurso oficial de Cuphead.

## Funcionalidades

- **Login** con nombre de jugador validado (3-15 caracteres, letras, números y `_`) y feedback de error en el propio campo.
- **Juego**: tocar al jefe le quita vida y da monedas y puntos. 5 jefes distintos; al terminar la vuelta vuelven con el doble de vida.
- **Mejoras**: +1 daño por toque y +1 daño automático por segundo, con precio que sube en cada compra.
- **Ficha de personaje evolutiva**: nivel, rango (de "Novato de feria" a "Leyenda en blanco y negro"), monedas y puntos.
- **Ranking** desde base de datos local (Room) ordenable por puntuación, nivel o nombre. Tu fila aparece resaltada.
- **Contacto** con la empresa: formulario validado que abre la app de correo.
- **Persistencia**: la sesión se guarda en SharedPreferences y el progreso en Room (funciona offline).

## Arquitectura (MVVM)

```
ui/            Activities (solo vista) + ViewModels (lógica de pantalla)
 ├─ login/     LoginActivity, LoginViewModel
 ├─ menu/      MainActivity
 ├─ game/      GameActivity, GameViewModel, GameUiState
 ├─ ranking/   RankingActivity, RankingViewModel, RankingAdapter
 └─ contact/   ContactActivity
data/
 ├─ db/        AppDatabase, PlayerDao, PlayerEntity (Room)
 ├─ prefs/     SessionManager (SharedPreferences)
 ├─ repository/PlayerRepository (hilos con ExecutorService)
 └─ PlayerMapper
domain/        GameEngine, Boss, BossCatalog, Rank, UsernameValidator, ContactValidator
               (Java puro, sin Android → testeable con JUnit)
```

- Las consultas a la base de datos se hacen en un hilo secundario (`ExecutorService`) y el resultado vuelve al hilo principal con un `Handler`.
- Sin fugas de memoria: los singletons solo guardan el `ApplicationContext`, los ViewModels no guardan referencias a Activities, el temporizador del juego se cancela en `onCleared()` y los `LiveData` se desuscriben solos. Se usa **LeakCanary** en debug para comprobarlo.

## Tests

- Unitarios (JVM): `app/src/test` → `GameEngineTest`, `UsernameValidatorTest`, `ContactValidatorTest`.
- Instrumentados (emulador): `app/src/androidTest` → `PlayerDaoTest` con base de datos en memoria.

Ejecutar: clic derecho en la carpeta `test` → *Run 'Tests in ...'*, o desde terminal `./gradlew test`.

## Compilar y ejecutar

1. Clonar: `git clone https://github.com/juanluissimancasprundea25-create/CupHeadClicker.git`
2. Abrir la carpeta con Android Studio y esperar a que termine el *Gradle Sync*.
3. Elegir un emulador (API 24 o superior) o un móvil con depuración USB y pulsar **Run ▶**.

## Autor

Juan Luis Simancas Prundea — 2º DAM, CampusFP Humanes.
