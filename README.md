# API para E-commerce

### Índice

- [Descrição do projeto](#descrição-do-projeto)
  
- [Funcionalidades](#funcionalidades)
 
- [Documentação](#documentação)
 
- [Segurança](#segurança)
 
- [Diagrama de entidade-relacionamento](#diagrama-de-entidade-relacionamento)
 
- [Tecnologias utilizadas](#tecnologias-utilizadas)
 
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
