/*Criando database*/

CREATE DATABASE biblioteca;

use biblioteca;

CREATE TABLE cliente (
cpfCliente varchar(15) primary key not null,
nomeCliente varchar(50) not null,
telefoneCliente varchar(15),
emailCliente varchar(50)
);

CREATE TABLE funcionario (
idFunc int primary key not null auto_increment,
nomeFunc varchar(50),
loginFunc varchar(30) not null,
senhaFunc varchar(30) not null
);

CREATE TABLE livros (
idLivro int primary key not null auto_increment,
tituloLivro varchar(70) not null,
generoLivro varchar(70),
editoraLivro varchar(50),
autorLivro varchar(50),
flgDisponivel boolean not null
);

CREATE TABLE emprestimo (
idEmp int primary key not null auto_increment,
idLivro int not null,
cpfCliente varchar(15) not null,
vMulta float,
dataEmpIni datetime not null,
dataEmpEst datetime not null,
dataEmpFin datetime,
FOREIGN KEY (idLivro) REFERENCES livros(idLivro) ON DELETE CASCADE,
FOREIGN KEY (cpfCliente) REFERENCES cliente(cpfCliente) ON DELETE CASCADE
);

/*Inserts usados para testes:*/

insert into funcionario (nomeFunc, loginFunc, senhaFunc)
values ('admin', 'admin', 'admin');

insert into funcionario (nomeFunc, loginFunc, senhaFunc)
values ('Fernanda Martins', 'fe', '123');

insert into cliente (cpfCliente, nomeCliente, telefoneCliente, emailCliente)
values ('00000000001', 'Marcos Alberto Pereira', '4740028922', 'testando@gmail.com');

insert into cliente (cpfCliente, nomeCliente, telefoneCliente, emailCliente)
values ('00000000002', 'Alberto Peretro Marcos', '47987877878', 'testado@gmail.com');

insert into livros (tituloLivro, generoLivro, editoraLivro, autorLivro, flgDisponivel)
values ('As longas tranças de um careca', 'Terror', 'Aquela Ali', 'Ronaldo', TRUE);

insert into livros (tituloLivro, generoLivro, editoraLivro, autorLivro, flgDisponivel)
values ('Lambaris e Tambaquis', 'Comédia', 'Aquela Ali', 'Marcos', TRUE);
