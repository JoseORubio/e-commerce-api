package com.ecommerceapi.utils;

import com.ecommerceapi.models.ClienteModel;
import com.sun.net.httpserver.HttpContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.util.descriptor.web.ContextHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

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
