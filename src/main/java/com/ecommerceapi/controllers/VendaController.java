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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

        if (quantidade > produtoOptional.get().getQuantidade_estoque()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quantidade indisponível no estoque.");
        }

        carrinho.add(new CarrinhoModel(produtoOptional.get(), quantidade));
        return ResponseEntity.status(HttpStatus.OK).body(carrinho);
    }

    @GetMapping
    public ResponseEntity<List<String>> verCarrinho() {
        List<String> listaItens = new ArrayList<String>();
        for (CarrinhoModel itens : carrinho) {
//            int numCarrinho = carrinho.indexOf(itens) + 1;
            listaItens.add("Número no carrinho: " + (carrinho.indexOf(itens) + 1));
            listaItens.add("Id do Produto: " + itens.getProdutoModel().getId());
            listaItens.add("Produto: " + itens.getProdutoModel().getNome());
            listaItens.add("Preço do produto: " + NumberFormat.getCurrencyInstance().format(itens.getProdutoModel().getPreco()));
            listaItens.add("Quantidade: " + itens.getQuantidade());
            listaItens.add("Preço total: " + NumberFormat.getCurrencyInstance().format(itens.getValorTotalProduto()));
            listaItens.add("-------------------------");
            //PASSAR ISTO PARA UM MAP ORDENADO
//            listaItens.add(String.format("\nNúmero no carrinho: " + (carrinho.indexOf(itens) + 1)
//                                       + "\nProduto: " + itens.getProdutoModel().getNome())
//                                       + "\nPreço do produto: " + NumberFormat.getCurrencyInstance().format(itens.getProdutoModel().getPreco())
//                                       + "\nQuantidade: " + itens.getQuantidade()
//                                       + "\nPreço total: " + NumberFormat.getCurrencyInstance().format(itens.getValorTotalProduto())
//                                       + "\n-------------------------");
//            System.out.println("\nNúmero no carrinho: " + numCarrinho);
//            System.out.println("Produto: " + itens.getProdutoModel().getNome());
//            System.out.println("Preço do produto: " + NumberFormat.getCurrencyInstance().format(itens.getProdutoModel().getPreco()));
//            System.out.println("Quantidade: " + itens.getQuantidade());
//            System.out.println("Preço total: " + NumberFormat.getCurrencyInstance().format(itens.getValorTotalProduto()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(listaItens);
    }


    @PostMapping("/confirmacao")
    public ResponseEntity<Object> confirmaVenda(){
        if (carrinho.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body("Carrinho vazio. Não é possivel efetivar a venda.");
        }

        VendaModel venda = vendaService.salvarVenda(new VendaModel());

        for (CarrinhoModel itens : carrinho) {
            ProdutoDaVendaModel produtoDaVendaModel = new ProdutoDaVendaModel();

            produtoDaVendaModel.setId_venda(venda.getId());
            ProdutoModel produtoModel = produtoService.buscarProdutoPorId(itens.getProdutoModel().getId()).get();
            produtoDaVendaModel.setId_produto(produtoModel.getId());
            produtoDaVendaModel.setQuantidade(itens.getQuantidade());
//            produtoDaVendaModel.setValor_total_produto(BigDecimal.ONE);
            produtoDaVendaService.inserirProduto(produtoDaVendaModel);
        }

        carrinho.clear();

        return ResponseEntity.status(HttpStatus.OK).body("Venda confirmada.");
    }

}
