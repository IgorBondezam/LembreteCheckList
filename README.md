# LembreteCheckList
[![NPM](https://img.shields.io/npm/l/react)](https://github.com/IgorBondezam/LembreteCheckList/blob/main/LICENSE) 

# Sobre o projeto

Aplicação de To do list, desenvolvido para melhorar meus conhecimentos em Java e organizar as tarefas do meu dia a dia.

Usando os conhecimentos de java, POO, conexões com banco de dados e realizando o View com JavaFX, foi um projeto desafiador, porém o resultado saiu como o esperado


## Layout Application
### Menu principal das tarefas
![Principal](https://github.com/IgorBondezam/assents/blob/main/LembreteCheckList/Principal.png)

### Menu de editar das tarefas
![Editar](https://github.com/IgorBondezam/assents/blob/main/LembreteCheckList/Editar.png)


# Tecnologias utilizadas
## Back end
- Java
- JDBC
- Banco de dados: MySQL
## Front end
- JavaFX
- Uso da ferramenta: SceneBuilder


# Como executar o projeto


## Pré-requisitos:
- Java 17
- MySQL e MySQL Workbench 8.0

### Tabelas no Banco de dados

```bash
CREATE SCHEMA `lembretes` ;

CREATE TABLE `lembretes`.`lembrete` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  `date` DATE NULL,
  PRIMARY KEY (`id`));

ALTER TABLE lembrete add status boolean;

```

### Adicionar a biblioteca

#### Biblioteca JavaFX

- (Instalar JavaFX) - https://gluonhq.com/products/javafx/

- Adicione uma nova variavel do sistema como exemplo:

![variavelJavaFx](https://github.com/IgorBondezam/assents/blob/main/LembreteCheckList/variaveisJavaFX.png)

- Na sua IDE adicione a biblioteca do JavaFX ao projeto

#### Biblioteca MySQL connector

Durante a instalação do MySQL, faça também a instalação do MySQL Connector J.
Isso gerará um arquivo .jar, que deve ser adicionado como biblioteca no projeto.

No arquivo <a href="https://github.com/IgorBondezam/LembreteCheckList/blob/main/db.properties">db.properties</a> configure os dados do banco de dados

### Ao Executar

Adicione em opções VM
--module-path "CAMINHO DA PASTA DO LIB DO JAVAFX" --add-modules=javafx.fxml,javafx.controls 

```bash
#Como exemplo 
--module-path "C:\java-libs\javafx-sdk-17\lib" --add-modules=javafx.fxml,javafx.controls 

```


# Autor

Igor Bondezam França

https://www.linkedin.com/in/igor-bondezam-fran%C3%A7a-48364920a/

