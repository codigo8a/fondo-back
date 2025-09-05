# Fondo de Pensión API
## 📋 Descripción del Proyecto
API REST para la gestión de un fondo de pensión desarrollada con Spring Boot 3.5.5 y Java 17 . El sistema permite administrar clientes, productos de inversión, sucursales e inscripciones de clientes a productos financieros.

## 🏗️ Arquitectura y Tecnologías
### Stack Tecnológico
- Framework : Spring Boot 3.5.5
- Lenguaje : Java 17
- Base de Datos : MongoDB (Atlas Cloud)
- Documentación : OpenAPI 3 / Swagger UI
- Validación : Bean Validation (Jakarta)
- Testing : JUnit 5, Mockito
- Build Tool : Maven
### Dependencias Principales
## 🗄️ Modelo de Datos
### Entidades Principales Cliente
- ID : Identificador único (MongoDB ObjectId)
- Nombre : Nombre del cliente (requerido)
- Apellidos : Apellidos del cliente (requerido)
- Ciudad : Ciudad de residencia (requerido)
- Monto : Monto del fondo de pensión (BigDecimal, > 0) Producto
- ID : Identificador único
- Nombre : Nombre del producto (ej: "FPV_BTG_PACTUAL_ECOPETROL")
- Tipo Producto : Tipo de producto (ej: "FPV")
- Monto Mínimo : Inversión mínima requerida (BigDecimal)
- Disponible En : Lista de IDs de sucursales donde está disponible Inscripción
- ID : Identificador único
- ID Cliente : Referencia al cliente
- ID Producto : Referencia al producto
- ID Sucursal : Referencia a la sucursal
- Monto Invertido : Cantidad invertida (BigDecimal)
- Fecha Transacción : Timestamp de la operación Sucursal
- ID : Identificador único
- Nombre : Nombre de la sucursal
- Ciudad : Ubicación de la sucursal Log
- ID : Identificador único
- Tipo Movimiento : Tipo de operación realizada
- Detalles : Información adicional de la transacción
- Fecha : Timestamp del movimiento
## 🚀 Endpoints de la API
### Clientes (/api/clientes)
- GET / - Obtener todos los clientes
- GET /{id} - Obtener cliente por ID
- POST / - Crear nuevo cliente
- PUT /{id} - Actualizar cliente
- DELETE /{id} - Eliminar cliente
- PUT /{id}/monto - Actualizar monto del cliente
### Productos (/api/productos)
- GET / - Obtener todos los productos
- GET /{id} - Obtener producto por ID
- POST / - Crear nuevo producto
- PUT /{id} - Actualizar producto
- DELETE /{id} - Eliminar producto
- GET /sucursal/{idSucursal} - Productos disponibles en sucursal
### Inscripciones (/api/inscripcion)
- GET / - Obtener todas las inscripciones
- GET /{id} - Obtener inscripción por ID
- POST / - Crear nueva inscripción
- PUT /{id} - Actualizar inscripción
- DELETE /{id} - Eliminar inscripción
- GET /existe/{id} - Verificar existencia de inscripción
### Sucursales (/api/sucursales)
- GET / - Obtener todas las sucursales
- GET /{id} - Obtener sucursal por ID
- POST / - Crear nueva sucursal
- PUT /{id} - Actualizar sucursal
- DELETE /{id} - Eliminar sucursal
### Logs (/api/logs)
- GET / - Obtener todos los logs
- GET /{id} - Obtener log por ID
### Health Check (/api/health)
- GET / - Verificar estado de la aplicación
## 🔧 Configuración y Ejecución
### Prerrequisitos
- Java 17+
- Maven 3.6+
- Acceso a MongoDB (configurado en application.properties)
### Configuración de Base de Datos
### Comandos de Ejecución
### URLs Importantes
- Aplicación : http://localhost:8080
- Swagger UI : http://localhost:8080/swagger-ui.html
- API Docs : http://localhost:8080/api-docs
- Health Check : http://localhost:8080/api/health
## 📊 Características Técnicas
### Validaciones
- Validación de entrada con Bean Validation
- Validaciones de negocio personalizadas
- Manejo de errores con ResponseEntity
### Documentación
- OpenAPI 3.0 completamente documentado
- Swagger UI interactivo
- Anotaciones detalladas en todos los endpoints
### Logging y Auditoría
- Sistema de logs para tracking de operaciones
- Registro automático de movimientos de inscripciones
### Testing
- Cobertura de pruebas unitarias con JUnit 5
- Mocking con Mockito
- Reportes de cobertura con JaCoCo
### CORS
- Configuración CORS para frontend (localhost:3000, localhost:4200)
- Soporte para aplicaciones SPA
## 🏢 Lógica de Negocio
### Reglas de Inscripción
1. 1.
   El cliente debe existir en el sistema
2. 2.
   El producto debe existir y estar activo
3. 3.
   El producto debe estar disponible en la sucursal seleccionada
4. 4.
   No se permiten inscripciones duplicadas (mismo cliente-producto-sucursal)
5. 5.
   El monto invertido debe ser mayor al monto mínimo del producto
6. 6.
   Se registra automáticamente un log de la transacción
### Gestión de Montos
- Uso de BigDecimal para precisión financiera
- Validaciones de montos mínimos
- Actualización de montos de clientes
## 🔍 Monitoreo y Observabilidad
### Health Checks
- Endpoint de salud para verificar estado de la aplicación
- Útil para deployment y monitoreo
### Logs de Auditoría
- Tracking completo de operaciones de inscripción
- Información detallada de transacciones
- Timestamps para auditoría temporal
## 📈 Escalabilidad y Rendimiento
### Base de Datos
- MongoDB para escalabilidad horizontal
- Índices automáticos configurados
- Connection pooling optimizado
### Arquitectura
- Separación clara de responsabilidades (Controller-Service-Repository)
- DTOs para transferencia de datos
- Inyección de dependencias con Spring
## 🚀 Deployment
La aplicación está lista para deployment en:

- Contenedores Docker
- Servicios cloud (AWS, Azure, GCP)
- Servidores tradicionales
### Variables de Entorno Recomendadas
```
SPRING_DATA_MONGODB_URI=mongodb+srv://...
SPRING_DATA_MONGODB_DATABASE=BTG
SERVER_PORT=8080
```
