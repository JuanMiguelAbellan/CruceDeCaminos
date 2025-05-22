drop database if exists Cruce_De_Caminos;
create database Cruce_De_Caminos;
use Cruce_De_Caminos;

CREATE TABLE Usuarios (
    ID_Usuario INT PRIMARY KEY AUTO_INCREMENT,
    Username VARCHAR(50) NOT NULL UNIQUE,
    Password_Hash VARCHAR(255) NOT NULL,
    Rol ENUM('alumno', 'profesor', 'administrador') NOT NULL,
    ID_Persona INT
);

CREATE TABLE Telefonos (
    ID_Persona INT PRIMARY KEY,
    Telefono1 VARCHAR(15),
    Telefono2 VARCHAR(15),
    Telefono3 VARCHAR(15)
);

CREATE TABLE Alumno (
    ID_Alumno INT PRIMARY KEY AUTO_INCREMENT,
    DNI VARCHAR(10) NOT NULL,
    Nombre VARCHAR(50),
    Apellido VARCHAR(50),
    Tipo VARCHAR(20),
    FOREIGN KEY (ID_Alumno) REFERENCES Usuarios(ID_Usuario)
);

CREATE TABLE Profesor (
    ID_Profesor INT PRIMARY KEY AUTO_INCREMENT,
    DNI VARCHAR(10) NOT NULL,
    Nombre VARCHAR(50),
    Apellido VARCHAR(50),
    Tipo VARCHAR(20),
    FOREIGN KEY (ID_Profesor) REFERENCES Usuarios(ID_Usuario)
);

CREATE TABLE Administrador (
    ID_Administrador INT PRIMARY KEY AUTO_INCREMENT,
    DNI VARCHAR(10) NOT NULL,
    Nombre VARCHAR(50),
    Apellido VARCHAR(50),
    FOREIGN KEY (ID_Administrador) REFERENCES Usuarios(ID_Usuario)
);

CREATE TABLE PreguntasTest (
    ID_PreguntaTest INT PRIMARY KEY AUTO_INCREMENT,
    Pregunta TEXT NOT NULL,
    Correcta VARCHAR(255) NOT NULL,
    Opcion1 VARCHAR(255),
    Opcion2 VARCHAR(255),
    Opcion3 VARCHAR(255)
);

CREATE TABLE ResultadosTeoricos (
    ID_Alumno INT PRIMARY KEY,
    Total_Test INT DEFAULT 0,
    Total_Suspensos INT DEFAULT 0,
    Porcentaje FLOAT DEFAULT 0,
    FOREIGN KEY (ID_Alumno) REFERENCES Alumno(ID_Alumno)
);

CREATE TABLE ResultadosPracticos (
    ID_Alumno INT PRIMARY KEY,
    ID_Profesor INT,
    Comentario TEXT,
    Maniobra_Superada BOOLEAN,
    FOREIGN KEY (ID_Alumno) REFERENCES Alumno(ID_Alumno),
    FOREIGN KEY (ID_Profesor) REFERENCES Profesor(ID_Profesor)
);

CREATE TABLE Fallos (
	ID_Fallo int primary key auto_increment,
    ID_Pregunta INT,
    ID_Alumno INT,
    Fecha DATE,
    FOREIGN KEY (ID_Pregunta) REFERENCES PreguntasTest(ID_PreguntaTest),
    FOREIGN KEY (ID_Alumno) REFERENCES Alumno(ID_Alumno)
);

CREATE TABLE Reservas (
    ID_Reservas INT PRIMARY KEY AUTO_INCREMENT,
    ID_Profesor INT,
    ID_Alumno INT,
    Fecha_Hora DATETIME,
    FOREIGN KEY (ID_Profesor) REFERENCES Profesor(ID_Profesor),
    FOREIGN KEY (ID_Alumno) REFERENCES Alumno(ID_Alumno)
);

CREATE TABLE Notificaciones (
    ID_Notificacion INT PRIMARY KEY AUTO_INCREMENT,
    ID_Usuario INT,
    Mensaje TEXT NOT NULL,
    Fecha_Hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Leido BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (ID_Usuario) REFERENCES Usuarios(ID_Usuario)
);
