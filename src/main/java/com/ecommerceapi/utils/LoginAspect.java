package com.ecommerceapi.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.ecommerceapi.controllers.HomeController.clienteLogado;

@Aspect
@Component
public class LoginAspect {

    @Around("execution(* com.ecommerceapi.controllers.CarrinhoController.*(..))")
    public Object confereLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        if (clienteLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("O cliente precisa estar logado.");
        }
        Object resultado = joinPoint.proceed();
        return resultado;
    }

    @Before("execution(* com.ecommerceapi.controllers.HomeController.logoff(..))")
    public void limpaCarrinho() throws IOException, InterruptedException {
        URI endereco = URI.create("http://localhost:8080/carrinho/cancelamento");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(endereco).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }


}
