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

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

@RestController
@RequestMapping("/venda")
public class VendaController {

    final VendaService vendaService;
    final ProdutoDaVendaService produtoDaVendaService;
    final ProdutoService produtoService;
    private List<CarrinhoModel> carrinho = new ArrayList<CarrinhoModel>();


    public VendaController(VendaService vendaService, ProdutoDaVendaService produtoDaVendaService, ProdutoService produtoService) {
        this.vendaService = vendaService;
        this.produtoDaVendaService = produtoDaVendaService;
        this.produtoService = produtoService;
    }

    @PostMapping("/item")
    public ResponseEntity<Object> inserirProduto(@RequestParam("id_produto") String id_produto, @RequestParam("quantidade") String quantidadeString) {
        UUID id = ControllerUtils.converteUUID(id_produto);
        if (id == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        Optional<ProdutoModel> produtoOptional = produtoService.buscarProdutoPorId(id);
        if (!produtoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }

        int quantidade;
        try {
            quantidade = Integer.parseInt(quantidadeString);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quantidade inválida.");
        }

        if (quantidade < 1) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quantidade inválida.");

        for (CarrinhoModel itens : carrinho) {
            if (itens.getProdutoModel().getId().equals(produtoOptional.get().getId())) {
                if (itens.getQuantidade() + quantidade > produtoOptional.get().getQuantidade_estoque()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quantidade indisponível no estoque.");
                } else {
                    itens.setQuantidade(itens.getQuantidade() + quantidade);
                    return ResponseEntity.status(HttpStatus.OK).body(carrinho);
                }
            }
        }

        if (quantidade > produtoOptional.get().getQuantidade_estoque()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quantidade indisponível no estoque.");
        }

        carrinho.add(new CarrinhoModel(produtoOptional.get(), quantidade));
        return ResponseEntity.status(HttpStatus.OK).body(carrinho);
    }

    @GetMapping
    public ResponseEntity<List<Map<String, String>>> verCarrinho() {
        List<Map<String, String>> listaItens = new ArrayList<Map<String, String>>();
        for (CarrinhoModel itens : carrinho) {
            Map<String, String> infoItens = new LinkedHashMap<>();
            infoItens.put("Número no carrinho", String.valueOf(carrinho.indexOf(itens) + 1));
            infoItens.put("Produto", String.valueOf(itens.getProdutoModel().getNome()));
            infoItens.put("Preço do produto", String.valueOf(itens.getProdutoModel().getPreco()));
            infoItens.put("Quantidade", String.valueOf(itens.getQuantidade()));
            infoItens.put("Preço total", String.valueOf(itens.getValorTotalProduto()));
            listaItens.add(infoItens);
        }
        return ResponseEntity.status(HttpStatus.OK).body(listaItens);
    }

    @PostMapping("/{numCarrinho}/remocao")
    public ResponseEntity<Object> removeItemCarrinho(@PathVariable(value = "numCarrinho") String numItemString) {

        if  (carrinho.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Carrinho vazio");
        }
        int numItem;
        try {
            numItem = Integer.parseInt(numItemString);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Número inválido.");
        }
        String nomeItem = null;
        try {
            nomeItem = carrinho.get(numItem-1).getProdutoModel().getNome();
            carrinho.remove(numItem - 1);
        }
        catch (IndexOutOfBoundsException e){
            return ResponseEntity.status(HttpStatus.OK).body("Item não existe.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Item " + nomeItem +" removido.");
    }
    @PostMapping("/{numCarrinho}/{quantidade}/alteracao")
    public ResponseEntity<Object> alteraItemCarrinho(@PathVariable(value = "numCarrinho") String numItemString,
                                                     @PathVariable(value = "quantidade") String quantidadeString) {
        if  (carrinho.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Carrinho vazio");
        }

        int numItem;
        try {
            numItem = Integer.parseInt(numItemString);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Número inválido.");
        }

        int quantidade;
        try {
            quantidade = Integer.parseInt(quantidadeString);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quantidade inválida.");
        }

        if (quantidade < 1) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quantidade inválida.");

        if (quantidade> carrinho.get(numItem-1).getProdutoModel().getQuantidade_estoque()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quantidade indisponível no estoque.");
        }

        try {
            carrinho.get(numItem-1).setQuantidade(quantidade);
        }
        catch (IndexOutOfBoundsException e){
            return ResponseEntity.status(HttpStatus.OK).body("Item não existe");
        }

        return ResponseEntity.status(HttpStatus.OK).body(String.format("Item %s alterado." ,
                carrinho.get(numItem-1).getProdutoModel().getNome() ));
    }


    @PostMapping("/confirmacao")
    public ResponseEntity<Object> confirmaVenda() {
        if (carrinho.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("Carrinho vazio. Não é possivel efetivar a venda.");
        }
        for (CarrinhoModel itens : carrinho) {
            if (itens.getQuantidade() >
                    produtoService.buscarProdutoPorId(itens.getProdutoModel().getId()).get().getQuantidade_estoque()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(String.format("Quantidade de %s indisponível no estoque.", itens.getProdutoModel().getNome()));
            }
        }

        VendaModel venda = vendaService.salvarVenda(new VendaModel());

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

        return ResponseEntity.status(HttpStatus.OK).body("Venda confirmada.");
    }

}
