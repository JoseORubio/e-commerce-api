package com.ecommerceapi.controllers;

import com.ecommerceapi.models.*;
import com.ecommerceapi.services.*;
import com.ecommerceapi.utils.ValidacaoUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/itens-carrinho")
public class CarrinhoController {

    final VendaService vendaService;
    final ProdutoDaVendaService produtoDaVendaService;
    final ProdutoService produtoService;
    final UsuarioService usuarioService;
    final CarrinhoService carrinhoService;


    public CarrinhoController(VendaService vendaService, ProdutoDaVendaService produtoDaVendaService, ProdutoService produtoService, UsuarioService usuarioService, CarrinhoService carrinhoService) {
        this.vendaService = vendaService;
        this.produtoDaVendaService = produtoDaVendaService;
        this.produtoService = produtoService;
        this.usuarioService = usuarioService;
        this.carrinhoService = carrinhoService;
    }

    private UsuarioModel pegarUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken)) {
            return null;
        }
        String login = authentication.getName();
        UsuarioModel usuarioModel = usuarioService.buscarUsuarioPorLogin(login).get();
        return usuarioModel;
    }

    @PostMapping
    public ResponseEntity<Object> inserirProduto(@RequestParam("id_produto") String id_produto,
                                                 @RequestParam("quantidade") String quantidadeString) {
        UUID id = ValidacaoUtils.converteUUID(id_produto);
        if (id_produto.equals("") || id == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id do produto inválida");
        Optional<ProdutoModel> produtoOptional = produtoService.buscarProdutoPorId(id);
        if (!produtoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }

        int quantidade;
        try {
            quantidade = Integer.parseInt(quantidadeString);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantidade inválida.");
        }

        if (quantidade < 1) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantidade inválida.");

        UsuarioModel usuario = pegarUsuario();
        Optional<CarrinhoModel> carrinhoExistente = carrinhoService
                .buscarProdutoDoUsuarioNoCarrinho(usuario, produtoOptional.get());

        if (carrinhoExistente.isPresent()) {
            if (carrinhoExistente.get().getQuantidade() + quantidade > produtoOptional.get().getQuantidade_estoque()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Quantidade indisponível no estoque.");
            } else {
                carrinhoExistente.get().setQuantidade(carrinhoExistente.get().getQuantidade() + quantidade);
                carrinhoService.inserirCarrinho(carrinhoExistente.get());
                return ResponseEntity.status(HttpStatus.CREATED).body(verCarrinho().getBody());
            }

        }

        if (quantidade > produtoOptional.get().getQuantidade_estoque()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Quantidade indisponível no estoque.");
        }

        CarrinhoModel carrinho = new CarrinhoModel(pegarUsuario(), produtoOptional.get(), quantidade);
        carrinhoService.inserirCarrinho(carrinho);
        return ResponseEntity.status(HttpStatus.CREATED).body(verCarrinho().getBody());
    }

    @GetMapping
    public ResponseEntity<Object> verCarrinho() {
        UsuarioModel usuario = pegarUsuario();
        Optional<List<CarrinhoModel>> carrinhoExistente = carrinhoService.buscarCarrinhoDoUsuario(usuario);

        if (carrinhoExistente.get().isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("Carrinho vazio");
        }

        List<Map<String, String>> listaItens = new ArrayList<Map<String, String>>();
        for (CarrinhoModel item : carrinhoExistente.get()) {
            Map<String, String> infoItens = new LinkedHashMap<>();
//            infoItens.put("Id do Produto", String.valueOf(item.getProdutoModel().getId()));
            infoItens.put("Produto", String.valueOf(item.getProduto().getNome()));
            infoItens.put("Preço do produto", String.valueOf(item.getProduto().getPreco()));
            infoItens.put("Quantidade", String.valueOf(item.getQuantidade()));
            infoItens.put("Preço total", String.valueOf(item.getValorTotalProduto()));
            listaItens.add(infoItens);
        }
        return ResponseEntity.status(HttpStatus.OK).body(listaItens);
    }


    @DeleteMapping("/{id_produto}")
    public ResponseEntity<Object> removeItemCarrinho(@PathVariable(value = "id_produto") String id_produto) {

        UUID id = ValidacaoUtils.converteUUID(id_produto);
        if (id_produto.equals("") || id == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id inválida");
        Optional<ProdutoModel> produtoOptional = produtoService.buscarProdutoPorId(id);
        if (produtoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }

        UsuarioModel usuario = pegarUsuario();
        Optional<CarrinhoModel> carrinhoExistente = carrinhoService
                .buscarProdutoDoUsuarioNoCarrinho(usuario, produtoOptional.get());

        if (carrinhoExistente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("Produto não encontrado neste carrinho.");
        }

        carrinhoService.apagarItemCarrinho(carrinhoExistente.get());
        return ResponseEntity.status(HttpStatus.OK).body("Item " + carrinhoExistente.get().getProduto().getNome() + " removido.");

    }

    @DeleteMapping
    public ResponseEntity<Object> cancelaCarrinho() {
        UsuarioModel usuario = pegarUsuario();
        Optional<List<CarrinhoModel>> carrinhoExistente = carrinhoService.buscarCarrinhoDoUsuario(usuario);
        if (carrinhoExistente.get().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário " + usuario.getNome() + " não possui carrinho ativo.");
        }
        for (CarrinhoModel carrinho : carrinhoExistente.get()) {
            carrinhoService.apagarItemCarrinho(carrinho);
        }

        return ResponseEntity.status(HttpStatus.OK).body("Carrinho cancelado.");
    }

    @PutMapping("/{id_produto}/{quantidade}")
    public ResponseEntity<Object> alteraItemCarrinho(@PathVariable(value = "id_produto") String id_produto,
                                                     @PathVariable(value = "quantidade") String quantidadeString) {

        UUID id = ValidacaoUtils.converteUUID(id_produto);
        if (id_produto.equals("") || id == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id inválida");
        Optional<ProdutoModel> produtoOptional = produtoService.buscarProdutoPorId(id);
        if (produtoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
        int quantidade;
        try {
            quantidade = Integer.parseInt(quantidadeString);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantidade inválida.");
        }
        if (quantidade < 1) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantidade inválida.");

        UsuarioModel usuario = pegarUsuario();
        Optional<CarrinhoModel> carrinhoExistente = carrinhoService
                .buscarProdutoDoUsuarioNoCarrinho(usuario, produtoOptional.get());

        if (carrinhoExistente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("Produto não encontrado neste carrinho.");
        }

        if (quantidade > carrinhoExistente.get().getProduto().getQuantidade_estoque()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Quantidade indisponível no estoque.");
        }
        carrinhoExistente.get().setQuantidade(quantidade);
        carrinhoService.inserirCarrinho(carrinhoExistente.get());
        return ResponseEntity.status(HttpStatus.OK).body(String.format("Item %s alterado.",
                carrinhoExistente.get().getProduto().getNome()));

    }

    @PostMapping("/venda")
    public ResponseEntity<Object> confirmaVenda() {
        UsuarioModel usuario = pegarUsuario();
        Optional<List<CarrinhoModel>> carrinhoExistente = carrinhoService.buscarCarrinhoDoUsuario(usuario);

        if (carrinhoExistente.get().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário " + usuario.getNome() + " não possui carrinho ativo.");
        }

        for (CarrinhoModel itens : carrinhoExistente.get()) {
            if (itens.getQuantidade() >
                    produtoService.buscarProdutoPorId(itens.getProduto().getId()).get().getQuantidade_estoque()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Quantidade de "+ itens.getProduto().getNome() +" indisponível no estoque.");
            }
        }

        VendaModel venda = vendaService.salvarVenda(new VendaModel(usuario.getId()));

        for (CarrinhoModel itens : carrinhoExistente.get()) {
            ProdutoDaVendaModel produtoDaVendaModel = new ProdutoDaVendaModel();
            produtoDaVendaModel.setId_venda(venda.getId());
            produtoDaVendaModel.setId_produto(itens.getProduto().getId());
            produtoDaVendaModel.setQuantidade(itens.getQuantidade());
            produtoDaVendaService.inserirProduto(produtoDaVendaModel);

            ProdutoModel produtoModel = itens.getProduto();
            produtoModel.setQuantidade_estoque(produtoModel.getQuantidade_estoque() - itens.getQuantidade());
            produtoService.salvarProduto(produtoModel);
        }

        cancelaCarrinho();
        return ResponseEntity.status(HttpStatus.CREATED).body("Venda confirmada.");
    }


}
