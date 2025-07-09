# 🐳 **Guía Docker - Sistema de Soporte Técnico**

Esta guía explica cómo levantar el sistema completo usando Docker.

## 📋 **Prerrequisitos**

- Docker Desktop instalado
- Docker Compose v3.8+
- Puertos 8080 y 5432 disponibles

## 🚀 **Instrucciones de Uso**

### **1. Clonar el repositorio**
```bash
git clone <url-del-repo>
cd parcial-final-n-capas-012025
```

### **2. Levantar el entorno completo**
```bash
# Construir y levantar todos los servicios
docker-compose up --build

# O en modo detached (segundo plano)
docker-compose up --build -d
```

### **3. Verificar que todo esté funcionando**
```bash
# Ver logs de la aplicación
docker-compose logs -f app

# Ver logs de la base de datos
docker-compose logs -f db

# Ver estado de los contenedores
docker-compose ps
```

### **4. Acceder a la aplicación**
- **API:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **PostgreSQL:** localhost:5432 (usuario: postgres, password: root)

## 🔧 **Comandos Útiles**

### **Gestión de Contenedores**
```bash
# Detener todos los servicios
docker-compose down

# Detener y eliminar volúmenes (borra la BD)
docker-compose down -v

# Reconstruir solo la aplicación
docker-compose build app

# Reiniciar un servicio específico
docker-compose restart app
```

### **Debugging**
```bash
# Ejecutar comando en contenedor de la app
docker-compose exec app bash

# Ver logs en tiempo real
docker-compose logs -f

# Conectarse a PostgreSQL
docker-compose exec db psql -U postgres -d supportdb
```

## 🗄️ **Base de Datos**

### **Conexión desde herramientas externas:**
- **Host:** localhost
- **Puerto:** 5432
- **Base de datos:** supportdb
- **Usuario:** postgres
- **Contraseña:** root

### **Usuarios precargados:**
| Correo | Contraseña | Rol |
|--------|------------|-----|
| `eduardorivera@uca.edu.sv` | `Password123` | USER |
| `ronaldsanchez@uca.edu.sv` | `Password123` | TECH |

## 🧪 **Pruebas con Postman**

### **Base URL:**
```
http://localhost:8080
```

### **Flujo de prueba:**
1. **Login:**
   ```
   POST /auth/login
   Body: {"correo": "eduardorivera@uca.edu.sv", "password": "Password123"}
   ```

2. **Usar token en headers:**
   ```
   Authorization: Bearer <token_obtenido>
   ```

3. **Probar endpoints:**
   - `GET /api/tickets` - Ver tickets
   - `POST /api/tickets` - Crear ticket
   - `GET /api/users/all` - Ver usuarios (solo TECH)

## ⚠️ **Troubleshooting**

### **Problema: Puerto ya en uso**
```bash
# Ver qué usa el puerto 8080
netstat -tulpn | grep 8080

# Cambiar puerto en docker-compose.yml
ports:
  - "8081:8080"  # Usar puerto 8081 en lugar de 8080
```

### **Problema: Base de datos no conecta**
```bash
# Verificar que PostgreSQL esté corriendo
docker-compose ps

# Revisar logs de la BD
docker-compose logs db

# Reiniciar solo la BD
docker-compose restart db
```

### **Problema: Aplicación no inicia**
```bash
# Ver logs detallados
docker-compose logs app

# Reconstruir imagen
docker-compose build --no-cache app
docker-compose up app
```

## 🏗️ **Arquitectura Docker**

```
┌─────────────────────────────────────┐
│              Docker Host             │
│                                     │
│  ┌─────────────┐  ┌─────────────┐   │
│  │  support-app │  │ supportdb-  │   │
│  │             │  │ postgres    │   │
│  │ Spring Boot │  │             │   │
│  │ Port: 8080  │  │ PostgreSQL  │   │
│  │             │  │ Port: 5432  │   │
│  └─────────────┘  └─────────────┘   │
│          │                │         │
│          └────────────────┘         │
│           support-network           │
└─────────────────────────────────────┘
```

## 📝 **Logs y Monitoreo**

### **Ver logs específicos:**
```bash
# Solo errores
docker-compose logs app | grep ERROR

# Solo logs de autenticación
docker-compose logs app | grep JWT

# Logs de la última hora
docker-compose logs --since 1h app
```

## 🔄 **Desarrollo**

Para desarrollo con auto-reload:
```bash
# Montar código fuente como volumen (agregar en docker-compose.yml)
volumes:
  - ./src:/app/src
  - ./target:/app/target
```

¡El sistema está listo para usar! 🎉 