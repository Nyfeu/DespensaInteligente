create database despensa_inteligente;
use despensa_inteligente;
create table usuario(
	id SMALLINT AUTO_INCREMENT NOT NULL,
	nome VARCHAR(60) NOT NULL,
    email VARCHAR(60) NOT NULL,
    senha_hash VARCHAR(128) NOT NULL,
    primary key(id)
);
create table ingrediente(
	nome VARCHAR(60) NOT NULL,
    categoria VARCHAR(60) NOT NULL,
    primary key(nome)
);
create table despensa(
	id_usuario SMALLINT UNSIGNED NOT NULL,
    nome_ingrediente VARCHAR(60) NOT NULL,
    validade VARCHAR(60) NOT NULL,
    quantidade SMALLINT UNSIGNED NOT NULL
);
create table receita(
	id SMALLINT AUTO_INCREMENT NOT NULL,
    titulo VARCHAR(60) NOT NULL,
    descricao VARCHAR(250) NOT NULL,
    modo_preparo VARCHAR(60) NOT NULL,
    primary key(id)
);
create table receita_ingrediente(
	id_receita SMALLINT UNSIGNED NOT NULL,
    nome_ingrediente VARCHAR(60) NOT NULL,
    quantidade SMALLINT UNSIGNED NOT NULL
);