create Database DIETAS_CABALLOS;
use DIETAS_CABALLOS;


CREATE TABLE Due�os (
ID_Due�o INT PRIMARY KEY IDENTITY(1,1),
Nombre_Due�o VARCHAR(255),
Cedula_Due�o VARCHAR(20)
);


CREATE TABLE Caballos (
ID_Caballo INT PRIMARY KEY IDENTITY(1,1),
Nombre_Caballo VARCHAR(255),
Codigo_Caballo VARCHAR(20),
Edad VARCHAR(20),
Afeccion VARCHAR(20),
ID_Due�o INT FOREIGN KEY REFERENCES Due�os(ID_Due�o)
);
ALTER TABLE Caballos
ALTER COLUMN Edad VARCHAR(50);

ALTER TABLE Caballos
ALTER COLUMN Codigo_Caballo NVARCHAR(50);

CREATE TABLE Alimentos (
ID_Alimento INT PRIMARY KEY IDENTITY(1,1),
Nombre_Alimento VARCHAR(255),
Caracteristica VARCHAR(255)
);



CREATE TABLE Dietas (
ID_Dieta INT PRIMARY KEY IDENTITY(1,1),
ID_Caballo INT FOREIGN KEY REFERENCES Caballos(ID_Caballo),
ID_Alimento INT FOREIGN KEY REFERENCES Alimentos(ID_Alimento),
Cantidad VARCHAR(50)
);



INSERT INTO Alimentos (Nombre_Alimento, Caracteristica) VALUES
('Heno de alfalfa', 'Fibra y nutrientes'),
('Agua', 'Hidrataci�n'),
('Cereales (Avena, Ma�z)', 'Energ�a'),
('Sal', 'Sodio'),
('Pasto fresco', 'Fibra y vitaminas'),
('Zanahorias', 'Vitaminas y minerales'),
('Manzanas', 'Vitaminas y fibra'),
('Remolacha', 'Vitaminas y hierro');


INSERT INTO Alimentos (Nombre_Alimento, Caracteristica) VALUES
('Leche de yegua o sustituto', 'Nutrientes esenciales'),
('Melaza', 'Energ�a adicional'),
('Vitaminas y minerales', 'Suplemento nutricional');


SELECT ID_Caballo, Nombre_Caballo FROM Caballos;