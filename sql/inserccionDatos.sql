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
INSERT INTO PreguntasTest (Pregunta, Correcta, Opcion1, Opcion2, Opcion3)
SELECT CONCAT('Pregunta número ', n),
       'Correcta',
       'Opción A',
       'Opción B',
       'Opción C'
FROM (SELECT @n := @n + 1 AS n FROM 
      (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 
       UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t1,
      (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 
       UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t2,
      (SELECT @n := 0) init) x
LIMIT 100;

-- === FALLOS DE ALUMNOS TEÓRICOS (10 cada uno) ===
INSERT INTO Fallos (ID_Pregunta, ID_Alumno, Fecha)
SELECT (a.ID_Alumno * 10 + b.n) % 100 + 1, a.ID_Alumno, CURDATE()
FROM (SELECT ID_Alumno FROM Alumno WHERE Tipo = 'teorico') a,
     (SELECT 1 AS n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5
      UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10) b;

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
