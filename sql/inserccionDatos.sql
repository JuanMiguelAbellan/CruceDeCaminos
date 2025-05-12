USE Cruce_De_Caminos;
SET foreign_key_checks = 0;

-- === ADMINISTRADORES ===
INSERT INTO Usuarios (Username, Password_Hash, Rol) VALUES
('admin1', 'hash_admin1', 'administrador'),
('admin2', 'hash_admin2', 'administrador');

INSERT INTO Administradores (ID_Administrador, DNI, Nombre, Apellido) VALUES
(1, '00000001A', 'Admin1', 'Apellido1'),
(2, '00000002A', 'Admin2', 'Apellido2');

-- === PROFESORES ===
-- Teóricos: ID 3-7
-- Prácticos: ID 8-12
INSERT INTO Usuarios (Username, Password_Hash, Rol) VALUES
('profteo1', 'hash_profteo1', 'profesor'),
('profteo2', 'hash_profteo2', 'profesor'),
('profteo3', 'hash_profteo3', 'profesor'),
('profteo4', 'hash_profteo4', 'profesor'),
('profteo5', 'hash_profteo5', 'profesor'),
('profprac1', 'hash_profprac1', 'profesor'),
('profprac2', 'hash_profprac2', 'profesor'),
('profprac3', 'hash_profprac3', 'profesor'),
('profprac4', 'hash_profprac4', 'profesor'),
('profprac5', 'hash_profprac5', 'profesor');

INSERT INTO Profesor (ID_Profesor, DNI, Nombre, Apellido, Tipo) VALUES
(3, '10000001B', 'Teorico1', 'Apellido', 'teorico'),
(4, '10000002B', 'Teorico2', 'Apellido', 'teorico'),
(5, '10000003B', 'Teorico3', 'Apellido', 'teorico'),
(6, '10000004B', 'Teorico4', 'Apellido', 'teorico'),
(7, '10000005B', 'Teorico5', 'Apellido', 'teorico'),
(8, '20000001B', 'Practico1', 'Apellido', 'practico'),
(9, '20000002B', 'Practico2', 'Apellido', 'practico'),
(10, '20000003B', 'Practico3', 'Apellido', 'practico'),
(11, '20000004B', 'Practico4', 'Apellido', 'practico'),
(12, '20000005B', 'Practico5', 'Apellido', 'practico');

-- === ALUMNOS ===
-- Teóricos: ID 13-22
-- Prácticos: ID 23-32
INSERT INTO Usuarios (Username, Password_Hash, Rol) VALUES
-- Teóricos
('alumteo1', 'hash_teo1', 'alumno'),
('alumteo2', 'hash_teo2', 'alumno'),
('alumteo3', 'hash_teo3', 'alumno'),
('alumteo4', 'hash_teo4', 'alumno'),
('alumteo5', 'hash_teo5', 'alumno'),
('alumteo6', 'hash_teo6', 'alumno'),
('alumteo7', 'hash_teo7', 'alumno'),
('alumteo8', 'hash_teo8', 'alumno'),
('alumteo9', 'hash_teo9', 'alumno'),
('alumteo10', 'hash_teo10', 'alumno'),

-- Prácticos
('alumprac1', 'hash_prac1', 'alumno'),
('alumprac2', 'hash_prac2', 'alumno'),
('alumprac3', 'hash_prac3', 'alumno'),
('alumprac4', 'hash_prac4', 'alumno'),
('alumprac5', 'hash_prac5', 'alumno'),
('alumprac6', 'hash_prac6', 'alumno'),
('alumprac7', 'hash_prac7', 'alumno'),
('alumprac8', 'hash_prac8', 'alumno'),
('alumprac9', 'hash_prac9', 'alumno'),
('alumprac10', 'hash_prac10', 'alumno');

INSERT INTO Alumno (ID_Alumno, DNI, Nombre, Apellido, Tipo) VALUES
-- Teóricos
(13, '30000001C', 'Teorico1', 'Apellido', 'teorico'),
(14, '30000002C', 'Teorico2', 'Apellido', 'teorico'),
(15, '30000003C', 'Teorico3', 'Apellido', 'teorico'),
(16, '30000004C', 'Teorico4', 'Apellido', 'teorico'),
(17, '30000005C', 'Teorico5', 'Apellido', 'teorico'),
(18, '30000006C', 'Teorico6', 'Apellido', 'teorico'),
(19, '30000007C', 'Teorico7', 'Apellido', 'teorico'),
(20, '30000008C', 'Teorico8', 'Apellido', 'teorico'),
(21, '30000009C', 'Teorico9', 'Apellido', 'teorico'),
(22, '30000010C', 'Teorico10', 'Apellido', 'teorico'),

-- Prácticos
(23, '40000001C', 'Practico1', 'Apellido', 'practico'),
(24, '40000002C', 'Practico2', 'Apellido', 'practico'),
(25, '40000003C', 'Practico3', 'Apellido', 'practico'),
(26, '40000004C', 'Practico4', 'Apellido', 'practico'),
(27, '40000005C', 'Practico5', 'Apellido', 'practico'),
(28, '40000006C', 'Practico6', 'Apellido', 'practico'),
(29, '40000007C', 'Practico7', 'Apellido', 'practico'),
(30, '40000008C', 'Practico8', 'Apellido', 'practico'),
(31, '40000009C', 'Practico9', 'Apellido', 'practico'),
(32, '40000010C', 'Practico10', 'Apellido', 'practico');

-- === PREGUNTAS TEST ===
INSERT INTO PreguntasTest (ID_PreguntaTest, Pregunta, Correcta, Opcion1, Opcion2, Opcion3) VALUES
(1, '¿Cuál es la velocidad máxima permitida en una autopista para un turismo?', '120 km/h', '100 km/h', '130 km/h', '110 km/h'),
(2, '¿Qué debe hacer si ve una señal de STOP?', 'Detenerse completamente', 'Reducir la velocidad', 'Tocar el claxon', 'Aumentar la velocidad'),
(3, '¿Qué indica una luz verde en un semáforo?', 'Puede continuar', 'Debe detenerse', 'Precaución extrema', 'Solo para peatones'),
(4, '¿Cuál es la tasa máxima de alcoholemia permitida para conductores profesionales?', '0,3 g/l en sangre', '0,5 g/l en sangre', '0,0 g/l en sangre', '0,8 g/l en sangre'),
(5, '¿Está permitido adelantar en un paso de peatones?', 'No', 'Sí, si no hay peatones', 'Sí, siempre', 'Depende del tipo de paso'),
(6, '¿Qué color tiene la señal de prohibido estacionar?', 'Azul con borde rojo', 'Rojo completo', 'Amarillo', 'Blanco'),
(7, '¿Cuál es la distancia mínima de seguridad al adelantar a un ciclista?', '1,5 metros', '1 metro', '2 metros', '50 centímetros'),
(8, '¿Qué documento es obligatorio llevar siempre en el vehículo?', 'Permiso de circulación', 'Factura de compra', 'Libro de revisiones', 'Manual del coche'),
(9, '¿Qué significa una señal triangular con borde rojo?', 'Peligro', 'Prohibición', 'Obligación', 'Información'),
(10, '¿Qué sistema evita que las ruedas se bloqueen al frenar?', 'ABS', 'ESP', 'Airbag', 'Control de crucero'),
(11, '¿Cuándo debe usar las luces de cruce?', 'Cuando haya poca visibilidad', 'Solo de noche', 'Solo en autopista', 'Nunca en ciudad'),
(12, '¿Qué indica una señal de círculo azul con una flecha blanca hacia arriba?', 'Obligación de seguir recto', 'Prohibición de seguir recto', 'Zona peligrosa', 'Prohibido peatones'),
(13, '¿Puede usar el teléfono móvil mientras conduce?', 'No, salvo con manos libres', 'Sí, si es urgente', 'Sí, en vías urbanas', 'Depende del tráfico'),
(14, '¿Qué hacer si el vehículo patina en una curva?', 'Girar en la dirección de la curva', 'Girar en sentido contrario', 'Frenar bruscamente', 'Acelerar'),
(15, '¿Cuándo se considera que un neumático está en mal estado?', 'Cuando el dibujo tiene menos de 1,6 mm', 'Cuando supera los 3 años', 'Cuando ha sido pinchado una vez', 'Cuando es de otra marca'),
(16, '¿Cuál es la maniobra más peligrosa?', 'Adelantar', 'Girar a la izquierda', 'Circular marcha atrás', 'Parar en doble fila'),
(17, '¿Qué debe hacer si sufre un accidente sin heridos?', 'Retirar los vehículos si es posible', 'Dejar el coche en la calzada', 'Abandonar el lugar', 'Esperar dentro del coche'),
(18, '¿Cuál es el orden correcto para pasar en un cruce sin señalizar?', 'El de la derecha pasa primero', 'El de la izquierda pasa primero', 'El coche más grande pasa primero', 'El coche rojo pasa primero'),
(19, '¿Qué significa una línea continua en el centro de la calzada?', 'Prohibido adelantar', 'Carril especial', 'Carril de emergencia', 'Adelantamiento permitido'),
(20, '¿Qué debe hacer si ve un autobús escolar parado con luces intermitentes?', 'Detenerse', 'Acelerar para adelantar', 'Tocar el claxon', 'Ignorarlo'),
(21, '¿En qué situaciones debe usar el triángulo de emergencia?', 'En averías o accidentes', 'Siempre que llueva', 'De noche', 'Al aparcar en pendiente'),
(22, '¿Qué significa una luz amarilla intermitente en el semáforo?', 'Precaución', 'Detención obligatoria', 'Prohibición', 'Paso libre solo para peatones'),
(23, '¿Qué es el aquaplaning?', 'Pérdida de control por agua', 'Frenado en seco', 'Conducción sobre hielo', 'Fallo de frenos'),
(24, '¿Qué significa una señal azul con una bicicleta blanca?', 'Carril bici', 'Prohibido bicis', 'Precaución bicicletas', 'Zona de carga'),
(25, '¿Qué significa una señal de círculo rojo con un coche y una moto?', 'Prohibido el paso a vehículos de motor', 'Solo acceso a motos', 'Carril especial', 'Zona escolar'),
(26, '¿Qué indica una marca vial de rombos blancos en la calzada?', 'Carril reservado', 'Obligación de girar', 'Obras', 'Zona de carga y descarga'),
(27, '¿Qué debe hacer si un peatón cruza indebidamente?', 'Reducir la velocidad o detenerse', 'Acelerar para asustarlo', 'Tocar el claxon', 'No hacer nada'),
(28, '¿Qué medida de seguridad es obligatoria para los menores de 135 cm?', 'Sistema de retención infantil', 'Cinturón de seguridad normal', 'Silla trasera', 'Ninguna medida especial'),
(29, '¿Qué significan las luces de emergencia encendidas?', 'Avisa de peligro', 'Autorización para circular rápido', 'Indica coche de autoescuela', 'Señala que puede girar'),
(30, '¿Qué significa una señal de prohibido adelantar con un coche negro y otro rojo?', 'Prohibido adelantar a vehículos de motor', 'Obligación de adelantar', 'Prohibido detenerse', 'Prioridad en curvas');

-- === RESULTADOS TEÓRICOS ===
INSERT INTO ResultadosTeoricos (ID_Alumno, Total_Test, Total_Suspensos, Porcentaje)
SELECT ID_Alumno, 20, 5, 75.0 FROM Alumno WHERE Tipo = 'teorico';

-- === RESULTADOS PRÁCTICOS ===
INSERT INTO ResultadosPracticos (ID_Alumno, ID_Profesor, Comentario, Maniobra_Superada)
SELECT ID_Alumno, 8 + (ID_Alumno % 5), 'Buen desempeño', TRUE
FROM Alumno WHERE Tipo = 'practico';

-- === RESERVAS (4 por profesor) ===
INSERT INTO Reservas (ID_Profesor, ID_Alumno, Fecha_Hora)
SELECT p.ID_Profesor, a.ID_Alumno, NOW() + INTERVAL (p.ID_Profesor * a.ID_Alumno) MINUTE
FROM Profesor p
JOIN (SELECT ID_Alumno FROM Alumno LIMIT 4) a;

-- === NOTIFICACIONES ===
INSERT INTO Notificaciones (ID_Usuario, Mensaje)
SELECT ID_Usuario, CONCAT('Mensaje para usuario ', ID_Usuario)
FROM Usuarios
ORDER BY RAND()
LIMIT 10;

SET foreign_key_checks = 1;
