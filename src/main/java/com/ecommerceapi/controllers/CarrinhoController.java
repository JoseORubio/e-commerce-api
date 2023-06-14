package com.ecommerceapi.controllers;

import com.ecommerceapi.models.CarrinhoModel;
import com.ecommerceapi.models.ProdutoDaVendaModel;
import com.ecommerceapi.models.ProdutoModel;
import com.ecommerceapi.models.VendaModel;
import com.ecommerceapi.services.ProdutoDaVendaService;
import com.ecommerceapi.services.ProdutoService;
import com.ecommerceapi.services.VendaService;
import com.ecommerceapi.utils.ControllerUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.ecommerceapi.controllers.HomeController.clienteLogado;

@RestController
@RequestMapping("/itens-carrinho")
public class CarrinhoController {

    final VendaService vendaService;
    final ProdutoDaVendaService produtoDaVendaService;
    final ProdutoService produtoService;
    private List<CarrinhoModel> carrinho = new ArrayList<CarrinhoModel>();


    public CarrinhoController(VendaService vendaService, ProdutoDaVendaService produtoDaVendaService, ProdutoService produtoService) {
        this.vendaService = vendaService;
        this.produtoDaVendaService = produtoDaVendaService;
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<Object> inserirProduto(@RequestParam("id_produto") String id_produto, @RequestParam("quantidade") String quantidadeString) {
        UUID id = ControllerUtils.converteUUID(id_produto);
        if (id_produto.equals("") || id == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id inválida");
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

        for (CarrinhoModel item : carrinho) {
            if (item.getProdutoModel().getId().equals(produtoOptional.get().getId())) {
                if (item.getQuantidade() + quantidade > produtoOptional.get().getQuantidade_estoque()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Quantidade indisponível no estoque.");
                } else {
                    item.setQuantidade(item.getQuantidade() + quantidade);
                    return ResponseEntity.status(HttpStatus.OK).body(carrinho);
                }
            }
        }

        if (quantidade > produtoOptional.get().getQuantidade_estoque()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Quantidade indisponível no estoque.");
        }

        carrinho.add(new CarrinhoModel(produtoOptional.get(), quantidade));
        return ResponseEntity.status(HttpStatus.CREATED).body(carrinho);
    }

    @GetMapping
    public ResponseEntity<Object> verCarrinho() {
        if (carrinho.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("Carrinho vazio");
        }
        List<Map<String, String>> listaItens = new ArrayList<Map<String, String>>();
        for (CarrinhoModel item : carrinho) {
            Map<String, String> infoItens = new LinkedHashMap<>();
            infoItens.put("Id do Produto", String.valueOf(item.getProdutoModel().getId()));
            infoItens.put("Produto", String.valueOf(item.getProdutoModel().getNome()));
            infoItens.put("Preço do produto", String.valueOf(item.getProdutoModel().getPreco()));
            infoItens.put("Quantidade", String.valueOf(item.getQuantidade()));
            infoItens.put("Preço total", String.valueOf(item.getValorTotalProduto()));
            listaItens.add(infoItens);
        }
        return ResponseEntity.status(HttpStatus.OK).body(listaItens);
    }

    @DeleteMapping("/{id_produto}")
    public ResponseEntity<Object> removeItemCarrinho(@PathVariable(value = "id_produto") String id_produto) {

        if (carrinho.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Carrinho vazio");
        }

        UUID id = ControllerUtils.converteUUID(id_produto);
        if (id_produto.equals("") || id == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id inválida");

        String nomeItem = null;

        for (CarrinhoModel item : carrinho) {
            if (item.getProdutoModel().getId().equals(id)) {
                nomeItem = item.getProdutoModel().getNome();
                carrinho.remove(item);
                break;
            }
        }

        if (nomeItem == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item não encontrado no carrinho.");
        return ResponseEntity.status(HttpStatus.OK).body("Item " + nomeItem + " removido.");
    }

    @PutMapping("/{id_produto}/{quantidade}")
    public ResponseEntity<Object> alteraItemCarrinho(@PathVariable(value = "id_produto") String id_produto,
                                                     @PathVariable(value = "quantidade") String quantidadeString) {
        if (carrinho.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Carrinho vazio");
        }
        UUID id = ControllerUtils.converteUUID(id_produto);
        if (id_produto.equals("") || id == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id inválida");

        int quantidade;
        try {
            quantidade = Integer.parseInt(quantidadeString);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantidade inválida.");
        }
        if (quantidade < 1) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantidade inválida.");

        for (CarrinhoModel item : carrinho) {
            if (item.getProdutoModel().getId().equals(id)){
                if(quantidade > item.getProdutoModel().getQuantidade_estoque()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Quantidade indisponível no estoque.");}
                item.setQuantidade(quantidade);
                return ResponseEntity.status(HttpStatus.OK).body(String.format("Item %s alterado.",
                        item.getProdutoModel().getNome()));
            }
        }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item não existe");
    }

    @PostMapping("/venda")
    public ResponseEntity<Object> confirmaVenda() {
        if (carrinho.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Carrinho vazio. Não é possivel efetivar a venda.");
        }
        for (CarrinhoModel itens : carrinho) {
            if (itens.getQuantidade() >
                    produtoService.buscarProdutoPorId(itens.getProdutoModel().getId()).get().getQuantidade_estoque()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(String.format("Quantidade de %s indisponível no estoque.", itens.getProdutoModel().getNome()));
            }
        }

        VendaModel venda = vendaService.salvarVenda(new VendaModel(clienteLogado.getId()));

        for (CarrinhoModel itens : carrinho) {
            ProdutoDaVendaModel produtoDaVendaModel = new ProdutoDaVendaModel();
            produtoDaVendaModel.setId_venda(venda.getId());
            produtoDaVendaModel.setId_produto(itens.getProdutoModel().getId());
            produtoDaVendaModel.setQuantidade(itens.getQuantidade());
            produtoDaVendaService.inserirProduto(produtoDaVendaModel);

            ProdutoModel produtoModel = itens.getProdutoModel();
            produtoModel.setQuantidade_estoque(produtoModel.getQuantidade_estoque() - itens.getQuantidade());
            produtoService.baixaProduto(produtoModel);
        }

        carrinho.clear();
        return ResponseEntity.status(HttpStatus.CREATED).body("Venda confirmada.");
    }

    @DeleteMapping
    public ResponseEntity<Object> cancelaCarrinho() {
        carrinho.clear();
        return ResponseEntity.status(HttpStatus.OK).body("Carrinho cancelado.");
    }

}
