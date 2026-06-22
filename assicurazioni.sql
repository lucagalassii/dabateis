DROP DATABASE IF EXISTS agenzia_assicurativa;
CREATE DATABASE agenzia_assicurativa;
USE agenzia_assicurativa;

CREATE TABLE RAMO (
    IDRamo INT PRIMARY KEY,
    NomeRamo VARCHAR(50) NOT NULL,
    GaranziaTassoTecnico DOUBLE,
    RegolamentazioneRCA VARCHAR(100),
    TipoRamo VARCHAR(30),
    Aliquota DOUBLE
);

CREATE TABLE PERIODO_VALIDITA (
    IDPeriodo INT PRIMARY KEY,
    DataInizio DATE NOT NULL,
    DataFine DATE NOT NULL,
    Stato VARCHAR(20)
);

CREATE TABLE CLASSE_RISCHIO (
    CodClsRischio INT PRIMARY KEY,
    Descrizione VARCHAR(100),
    Pericolosità INT
);

CREATE TABLE CLIENTE (
    CF VARCHAR(16) PRIMARY KEY,
    Nome VARCHAR(50) NOT NULL,
    Cognome VARCHAR(50) NOT NULL,
    Nascita DATE,
    LuogoNascita VARCHAR(50),
    Telefono VARCHAR(20)
);

CREATE TABLE INTERMEDIARIO (
    CodiceIntermediario INT PRIMARY KEY,
    Nome VARCHAR(50) NOT NULL,
    Cognome VARCHAR(50) NOT NULL,
    Nascita DATE,
    LuogoNascita VARCHAR(50),
    Specializzazione VARCHAR(50)
);

CREATE TABLE PERITO (
    CodPerito INT PRIMARY KEY,
    Nome VARCHAR(50),
    Cognome VARCHAR(50),
    Specializzazione VARCHAR(50)
);

CREATE TABLE TARIFFA (
    CodTariffa INT PRIMARY KEY,
    Provvigioni DOUBLE,
    IDPeriodo INT,
    FOREIGN KEY (IDPeriodo) REFERENCES PERIODO_VALIDITA(IDPeriodo)
);

CREATE TABLE FASCIA_PREMIO (
    IDFascia INT PRIMARY KEY,
    Scaglione VARCHAR(30),
    PremioBase DOUBLE,
    CodTariffa INT,
    FOREIGN KEY (CodTariffa) REFERENCES TARIFFA(CodTariffa)
);

CREATE TABLE PRODOTTO (
    CodProdotto INT PRIMARY KEY,
    Franchigia DOUBLE,
    Massimale DOUBLE,
    IDRamo INT,
    CodTariffa INT,
    FOREIGN KEY (IDRamo) REFERENCES RAMO(IDRamo),
    FOREIGN KEY (CodTariffa) REFERENCES TARIFFA(CodTariffa)
);

CREATE TABLE PREVENTIVO (
    NumeroPreventivo INT PRIMARY KEY,
    DataRichiesta DATE NOT NULL,
    DataScadenza DATE NOT NULL,
    Prezzo DOUBLE NOT NULL,
    Stato VARCHAR(20) DEFAULT 'In attesa',
    PrezzoLavorato DOUBLE, 
    Sconto DOUBLE,         
    CodProdotto INT,
    CodClsRischio INT,
    CF VARCHAR(16),
    CodiceIntermediario INT,
    FOREIGN KEY (CodProdotto) REFERENCES PRODOTTO(CodProdotto),
    FOREIGN KEY (CodClsRischio) REFERENCES CLASSE_RISCHIO(CodClsRischio),
    FOREIGN KEY (CF) REFERENCES CLIENTE(CF),
    FOREIGN KEY (CodiceIntermediario) REFERENCES INTERMEDIARIO(CodiceIntermediario)
);

CREATE TABLE POLIZZA (
    NumeroPolizza VARCHAR(30) PRIMARY KEY,
    Prezzo DOUBLE NOT NULL,
    Frazionamento VARCHAR(20),
    DataInizioEffetti DATE NOT NULL,
    DataFineEffetti DATE NOT NULL,
    NumeroPreventivo INT UNIQUE, 
    FOREIGN KEY (NumeroPreventivo) REFERENCES PREVENTIVO(NumeroPreventivo)
);

CREATE TABLE RATA (
    IDRata INT PRIMARY KEY,
    NumeroPolizza VARCHAR(30),
    FOREIGN KEY (NumeroPolizza) REFERENCES POLIZZA(NumeroPolizza)
);

CREATE TABLE SINISTRO (
    IdSinistro INT PRIMARY KEY,
    DataSinistro DATE NOT NULL,
    Stato VARCHAR(20),
    DannoStima DOUBLE,
    NumeroPolizza VARCHAR(30),
    FOREIGN KEY (NumeroPolizza) REFERENCES POLIZZA(NumeroPolizza)
);

CREATE TABLE PERIZIA (
    IDPerizia INT PRIMARY KEY,
    Data DATE NOT NULL,
    ImportoStimato DOUBLE,
    Descrizione TEXT,
    CodPerito INT,
    IdSinistro INT,
    FOREIGN KEY (CodPerito) REFERENCES PERITO(CodPerito),
    FOREIGN KEY (IdSinistro) REFERENCES SINISTRO(IdSinistro)
);

CREATE TABLE LIQUIDAZIONE (
    IDLiquidazione INT PRIMARY KEY,
    DataPagamento DATE NOT NULL,
    MetodoPagamento VARCHAR(30),
    CodPerito INT,
    FOREIGN KEY (CodPerito) REFERENCES PERITO(CodPerito)
);

INSERT INTO INTERMEDIARIO (CodiceIntermediario, Nome, Cognome, Nascita, LuogoNascita, Specializzazione)
VALUES (1, 'Luca', 'Galassi', '2005-07-11', 'Cesena', 'Agenti Generali')
ON DUPLICATE KEY UPDATE CodiceIntermediario=CodiceIntermediario;

INSERT INTO CLIENTE(CF, Nome, Cognome, Nascita, LuogoNascita, Telefono) 
VALUES('ABCDEF12G34H567I', 'Mario', 'Puppo', '1764-11-02', 'Cesena', '123456789'); 

INSERT INTO PREVENTIVO(NumeroPreventivo, DataRichiesta, DataScadenza, Prezzo, Stato, PrezzoLavorato, Sconto, CF, CodiceIntermediario) 
VALUES (1056, '2026-06-05', '2026-07-05', 450.00, 'In attesa', 450.00, 10.00, 'ABCDEF12G34H567I', 1);

UPDATE PREVENTIVO SET Stato = 'Accettato' 
WHERE NumeroPreventivo = 1001; 
INSERT INTO POLIZZA(NumeroPolizza, Prezzo, Frazionamento, DataInizioEffetti, DataFineEffetti, NumeroPreventivo) 
VALUES('POL-0000001', 450.00, 'Annuale', '2026-06-05', '2027-06-05', 1001);

UPDATE RATA SET NumeroPolizza = 'POL-0000001'
WHERE IDRata = 1069; 

INSERT INTO SINISTRO(IdSinistro, DataSinistro, Stato, DannoStima, NumeroPolizza) 
VALUES(104, '2026-06-19', 'Aperto', 1200.00, 'POL-0000001'); 

INSERT INTO PERITO (CodPerito, Nome, Cognome, Specializzazione)
VALUES (1, 'Maldestro', 'Pippo', 'Infortuni e Automotive')
ON DUPLICATE KEY UPDATE CodPerito=CodPerito;

INSERT INTO LIQUIDAZIONE(IDLiquidazione, DataPagamento, MetodoPagamento, CodPerito) 
VALUES(1, '2026-06-21', 'Bonifico', 1); 
UPDATE SINISTRO SET Stato = 'Chiuso' 
WHERE IdSinistro = 104; 

SELECT P.NumeroPolizza, P.DataInizioEffetti, P.DataFineEffetti, 
(SELECT COUNT(*) FROM RATA R WHERE R.NumeroPolizza = P.NumeroPolizza) AS TotaleRate 
FROM POLIZZA P 
JOIN SINISTRO S ON P.NumeroPolizza = S.NumeroPolizza WHERE S.IdSinistro = 104 
AND S.DataSinistro BETWEEN P.DataInizioEffetti 
AND P.DataFineEffetti; 

SELECT NumeroPreventivo, DataRichiesta, DataScadenza, Prezzo, Stato, CF FROM PREVENTIVO 
WHERE Stato = 'In attesa' 
ORDER BY DataRichiesta DESC;

SELECT C.Cognome, C.Nome, P.NumeroPolizza, P.Prezzo, P.Frazionamento, P.DataInizioEffetti, P.DataFineEffetti 
FROM CLIENTE C 
JOIN PREVENTIVO PR ON C.CF = PR.CF 
JOIN POLIZZA P ON PR.NumeroPreventivo = P.NumeroPreventivo 
WHERE C.CF = 'ABCDEF12G34H567I' 
ORDER BY P.DataFineEffetti DESC; 


