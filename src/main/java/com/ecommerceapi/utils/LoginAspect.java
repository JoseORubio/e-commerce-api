package com.ecommerceapi.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static com.ecommerceapi.controllers.HomeController.clienteLogado;

@Aspect
@Component
public class LoginAspect {

    @Around("execution(* com.ecommerceapi.controllers.CarrinhoController.*(..))")
    public Object autenticaLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        if (clienteLogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("O cliente precisa estar logado.");
        }
        Object resultado = joinPoint.proceed();
        return resultado;
    }

}
