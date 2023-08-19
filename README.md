# API para E-commerce

### Índice

- [Descrição do projeto](#descrição-do-projeto)
  
- [Funcionalidades](#funcionalidades)
 
- [Documentação](#documentação)
 
- [Segurança](#segurança)
 
- [Diagrama de entidade-relacionamento](#diagrama-de-entidade-relacionamento)
 
- [Tecnologias, dependências e ferramentas utilizadas](#tecnologias,-dependências-e-ferramentas-utilizadas)
 
- [Acesso ao projeto](#acesso-ao-projeto)
 
- [Abrir e rodar o projeto](#abrir-e-rodar-o-projeto)
 
- [Próximos Passos](#próximos-Passos)

## Descrição do projeto 

API RESTful para um sistema de E-Commerce seguindo o padrão arquitetural MVC, porém sem o VIEW mas com respostas em JSON, podendo ser testado via SWAGGER e POSTMAN, ambos documentados nesta API. Suas funcionalidades são CRUDS para produtos e usuários, com validação de dados, gerenciamento de autoridades (ADMIN, USER), e um módulo de compras, com um CRUD para o carrinho e efetivação da venda.

A aplicação permite que um usuário gerencie seu próprio carrinho até a efetivação da venda, dando baixa no estoque.

Busquei seguir as boas práticas do uso dos verbos HTTP e seus códigos de status. Também criei payloads específicos para alguns endpoints, expostos na documentação.

Todos os dados de entrada são validados, em alguns casos com o uso de expressões regulares para a modelagem de dados mais específica. Para os casos de entradas inválidas estas serão informadas no payload.

É utilizado o uso do webservice ViaCep para consultar o CEP no momento do cadastro, bem como para auto completar os campos relacionados (Estado, Cidade, Rua) com os dados correspondentes.

Cada endpoint possui uma regra de permissão específica que será exposto no tópico "Segurança".
Este é meu projeto principal no momento, onde pretendo adicionar funcionalidades aliadas às novas tecnologias ou ferramentas que eu estiver estudando.

## Funcionalidades

:heavy_check_mark: `Funcionalidade 1:` CRUD completo, com validação para os usuários. Para a autoridade "USER" os dados são filtrados para exposição visando a segurança da aplicação, já a autoridade "ADMIN" tem acesso completo.

:heavy_check_mark: `Funcionalidade 2:` CRUD completo, com validação para os produtos.

:heavy_check_mark: `Funcionalidade 3:` CRUD completo de um carrinho de compras.

## Documentação

Para visualizar a documentação copie o conteúdo deste [arquivo](https://github.com/JoseORubio/e-commerce-api/blob/master/Arquivos/Swagger%20doc.yaml) e cole neste [editor de documentação do Swagger](https://editor.swagger.io/)

## Segurança

As permissões ("ADMIN", "USER", anônimo) são gerenciadas da seguinte forma de acordo com cada endpoint:

- Usuário POST: Todos

- Usuário PUT, DELETE, GET: "ADMIN" e "USER"

- Usuário GET (todos, por ID, por nome) : "ADMIN"

- Produto POST, PUT, DELETE: "ADMIN"

- Produto GET(todos, por ID, por nome): Todos

- Carrinho POST, PUT, DELETE, GET: "USER". Apenas o próprio usuário tem acesso a seu carrinho.

## Diagrama de entidade-relacionamento

<div align="center">
  <img src="Arquivos/Diagrama Entidade-Relacionamento.png">
 </div>

 ## Tecnologias, dependências e ferramentas utilizadas

- Java 17
- MySQL
- Spring 3.0.6
- - Spring Boot
- - Spring Web MVC
- - Spring Data JPA
- - Spring Validation    
- - Spring Security
- - Spring HATEOAS
- Maven 
- Jackson 2.14.2
- Springdoc-Openapi 2.1.0
- InteliJ IDEA
- Postman
- Swagger
 
## Acesso ao projeto
Você pode [acessar o código fonte do projeto](https://github.com/JoseORubio/e-commerce-api) ou [baixá-lo](https://github.com/JoseORubio/e-commerce-api/archive/refs/heads/master.zip).

## Abrir e rodar o projeto

- É necessário importar no seu MySQL o arquivo dump de suas tabelas e dados disponibilizado. [link](https://github.com/JoseORubio/e-commerce-api/blob/master/Arquivos/e-commerce-dump20230819.sql)
- Clone este projeto, importe as dependências com Maven e execute-o em sua IDE. Eu utilizei o IntelliJ IDEA Community Edition.
- Exemplos úteis de logins com as autoridades: berna123 (ADMIN, USER), mayaf123 (ADMIN), artur123 (USER). Para todos a senha é "asd123aaD" .
- Teste-o através do Postman, baixando o arquivo [E-Commerce.postman_collection.json](https://github.com/JoseORubio/e-commerce-api/blob/master/Arquivos/E-Commerce.postman_collection.json)  e importanto-o em suas coleções.
- Também é possível testar via Swagger neste [LINK](http://localhost:8080/swagger-ui/index.html) 
- Para obter outros detalhes de funcionamento da api existem algumas views no banco de dados que pretendo implementar futuramente  no serviço de administração. Apenas execute em seu MySQL "select * from nome_da_view", seguem os nomes das views:
- - carrinho_view: exibe todos os carrinhos ativos no momento de todos os usuários.
- - papeis_do_usuario_view: exibe os papéis de cada usuário.
- - produtos_vendidos: exibe todos os produtos já vendidos com usuários.
- - vendas_view: exibe todos as vendas já efetuadas com usuários.

## Próximos Passos

Ordenado por prioridade.

- Criar testes unitários e de integração
- Aprofundar a especificidade de produtos, com criação de sub-categorias, preço de compra, taxas, registro de imagem...
- Utilizar o Lombok para limpar mais o código do programa.
- Criar serviço de administração do sistema, aplicando ciclo vermelho-verde-refatora do TDD de 3 etapas desde o início
- - Esta funcionalidade deverá permitir aos administradores gerenciar autoridades e acessar dados de histórico de vendas por usuário, produto e data.
- Implementar a aplicação num docker e nuvem.

Por fim seguirei agregando novas ideias de acordo com alguma ferramenta específica que eu estiver estudando.
