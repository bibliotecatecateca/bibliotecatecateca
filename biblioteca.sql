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
loginFunc varchar(30) not null unique,
senhaFunc varchar(30) not null
);

CREATE TABLE livros (
idLivro int primary key not null auto_increment,
tituloLivro varchar(70) not null,
generoLivro varchar(70),
editoraLivro varchar(50),
autorLivro varchar(50),
flgDisponivel boolean not null,
caminhoImagem tinytext
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
values ('Fernanda', 'fe', '123');

insert into cliente (cpfCliente, nomeCliente, telefoneCliente, emailCliente)
values ('000.000.000-01', 'Marcos Alberto Pereira', '4740028922', 'testando@gmail.com');

insert into livros (tituloLivro, generoLivro, editoraLivro, autorLivro, flgDisponivel)
values ('As longas tranças de um careca', 'Terror', 'Aquela Ali', 'Ronaldo', TRUE);

/*Selects teste

Verificar se um livro possui algum empréstimo ativo*/
SELECT
	l.tituloLivro,
	e.dataEmpIni as DataEmprestimo,
	c.nomeCliente as Cliente
FROM
	emprestimo e
JOIN livros l on e.idLivro = l.idLivro
JOIN cliente c on e.cpfCliente = c.cpfCliente
WHERE
	e.dataEmpFin is null and
	l.tituloLivro = 'As longas tranças de um careca';
	
/*Procedures criadas*/

DELIMITER // /* define que o texto que avisa que é o final do comando seja // ao invés de ; */

/* Cadastra emprestimos */
CREATE PROCEDURE cadEmprestimo(
	IN codLivro int, /* recebe código do livro*/
	IN idCliente varchar(15) /* recebe cpf do cliente*/
)
BEGIN
	declare val int;

	select count(*)
	into val
	from emprestimo
	where 
		idLivro = codLivro and
		dataEmpFin is null;
		
	if val >= 1 then
		SELECT CONCAT('O livro informado já está cadastrado no empréstimo de código ', idemp) as mensagem from emprestimo where idLivro = codLivro and dataEmpFin is null;
	else
		INSERT INTO EMPRESTIMO (idLivro, cpfCliente, dataEmpIni, dataEmpEst)
		VALUES (codLivro, idCliente, SYSDATE(), (DATE_ADD(SYSDATE(), INTERVAL 7 DAY))); /*insere os dados do livro, cliente e data do empréstimo na tabela de empréstimos*/
		
		UPDATE LIVROS SET FLGDISPONIVEL = FALSE WHERE IDLIVRO = CODLIVRO; /* altera o cadastro do livro para informar que o mesmo não consta mais disponível */
		
		select CONCAT('Empréstimo cadastrado com sucesso! O código do empréstimo é: ',idemp) as mensagem from emprestimo where idLivro = codLivro and dataEmpFin is null;
	end if;
END//

/*encerra empréstimo*/
CREATE PROCEDURE encEmprestimo(
	IN codEmp INT /* recebe código do empréstimo*/
)
BEGIN
	UPDATE EMPRESTIMO SET dataEmpFin = SYSDATE() WHERE IDEMP = CODEMP; /* insere a data de encerramento do empréstimo no empréstimo informado*/
	
	UPDATE LIVROS SET FLGDISPONIVEL = TRUE WHERE IDLIVRO IN (SELECT IDLIVRO FROM EMPRESTIMO WHERE IDEMP = CODEMP); /* informa que o livro agora consta disponível novamente*/
	
	call calcMulta(codEmp); /* chama a procedure de calcular multa para o empréstimo que deseja encerrar */
	
	SELECT /* Trás os dados do empréstimo encerrado */
			a.idEmp as CodigoEmprestimo,
			b.tituloLivro as Livro,
			a.dataEmpIni as DataEmprestimo,
			a.dataEmpEst as DataDevolucaoEstimada,
			a.dataEmpFin as DataDevolucaoFinal,
			a.vMulta as Multa
		FROM emprestimo a
		JOIN LIVROS b on a.idLivro = b.idLivro
		JOIN cliente c on a.cpfCliente = c.cpfCliente
		WHERE
			idEmp = codEmp;
END//

/*Consulta empréstimos por cpf de cliente*/
CREATE PROCEDURE conEmprestimos(
	IN codCliente varchar(14) /* recebe o cpf do cliente*/
)
BEGIN
	declare val int;

	select count(*)
	into val
	from emprestimo
	where 
		cpfCliente = codCliente and
		dataEmpFin is null;
	
	IF val >= 1 then /* valida se existe ao menos 1 empréstimo ativo para o cpf informado */
		SELECT
			a.idEmp as CodigoEmprestimo,
			b.tituloLivro as Livro,
			a.dataEmpIni as DataEmprestimo,
			a.dataEmpEst as DataDevolucaoEstimada
		FROM emprestimo a
		JOIN LIVROS b on a.idLivro = b.idLivro
		JOIN cliente c on a.cpfCliente = c.cpfCliente
		WHERE
			a.cpfCliente = codCliente and
			a.dataEmpFin is null;
	else
		select 'Não foram localizados empréstimos ativos para este cliente' as mensagem; /* caso não exista, informa a mensagem ao lado*/
	end if;
END//

/*Consulta cliente cadastrado*/
CREATE PROCEDURE conCliente(
	IN codCliente varchar(14) /*recebe cpf do cliente*/
)
BEGIN
	declare val int;

	select count(*)
	into val
	from cliente
	where 
		cpfCliente = codCliente;
	
	IF val >= 1 then /*valida se existe ao menos 1 cliente cadastrado para o cpf informado*/
		SELECT
			*
		FROM cliente
		WHERE
			cpfCliente = codCliente;
	else
		select 'Cliente não cadastrado' as mensagem; /*caso não exista, informa a mensagem ao lado*/
	end if;
END//

CREATE PROCEDURE conLivro(
	IN titLivro varchar(70) /*recebe titulo do livro*/
)
BEGIN
	declare val int;

	select count(*)
	into val
	from livros
	where 
		tituloLivro like CONCAT('%', titLivro, '%');
	
	IF val >= 1 then /*valida se existe ao menos 1 livro cadastrado para o título informado*/
		SELECT
			idLivro as CodigoLivro,
			tituloLivro as Titulo,
			generoLivro as Genero,
			editoraLivro as Editora,
			autorLivro as Autor,
			CASE
				WHEN flgDisponivel = 1 THEN 'Disponível' /*Caso o valor seja 1, o livro está disponível*/
				WHEN flgDisponivel = 0 THEN 'Indisponível' /*Caso o valor seja 0, o livro está indisponível*/
			END as Disponivel
		FROM livros
		WHERE
			tituloLivro like CONCAT('%', titLivro, '%');
	else
		select 'Livro não localizado' as mensagem; /*caso não exista, informa a mensagem ao lado*/
	end if;
END//

/* Calcula valor de multa pendente */
CREATE PROCEDURE calcMulta(
	IN codEmp int /* recebe o código do empréstimo */
)
BEGIN
	declare diferenca int;
	declare dataestimada date;
	
	select dataEmpEst
	into dataestimada
	from emprestimo
	where idEmp = codEmp;
	
	set diferenca = DATEDIFF(SYSDATE(), dataestimada);
	
	if diferenca >= 1 THEN
		UPDATE EMPRESTIMO SET VMULTA = (diferenca * 1.50) WHERE IDEMP = CODEMP;
	END IF;
END//

/* consulta funcionario, se existe retorna TRUE */
CREATE PROCEDURE conFunc (
	IN logFunc varchar(30),
	OUT valid boolean
)
BEGIN
	declare val int;
	
	select count(*)
	into val
	from funcionario
	where loginFunc = logFunc;
	
	if val >= 1 then
		return True
	end if;
END//

/* cadastra funcionario */
CREATE PROCEDURE cadFunc (
	IN logFunc varchar(30),
	IN nomFunc varchar(50),
	IN senFunc varchar(30)
)
BEGIN
	DECLARE val boolean;
	
	set val = call conFunc(logFunc);
	
	if val is true then
		select 'Login já cadastrado, favor tentar novamente com outro login' as mensagem from funcionario;
	else
		insert into funcionario (nomeFunc, loginFunc, senhaFunc) values (nomFunc, logFunc, senFunc);
	end if;
END//

/* excluir funcionario */
CREATE PROCEDURE excFunc (
	IN logFunc varchar(30)
)
BEGIN
	DECLARE val boolean;
	
	set val = call conFunc(logFunc);
	
	if val is false then
		select 'O login informado não consta cadastrado' as mensagem from funcionario;
	else
		delete from funcionario where loginFunc = logFunc;
	end if;
END//

CREATE PROCEDURE altFunc (
	IN logFunc varchar(30),
	IN nomFuncNovo varchar(50),
	IN senFuncNovo varchar(30)
)
BEGIN
	declare val1 

DELIMITER ; /* define que o texto que avisa que é o final do comando ; */
