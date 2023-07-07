package com.ecommerceapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ECommerceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommerceApiApplication.class, args);
//		System.out.println(new BCryptPasswordEncoder().encode("asd123aaD"));

		List<List<String>> erros = new ArrayList<>();
		List<String> camposEMensagens;
		camposEMensagens = new ArrayList<>();
		camposEMensagens.add("nome");
		camposEMensagens.add("Não pode ser nulo");
		camposEMensagens.add("Tem que ter os caracteres");
		erros.add(camposEMensagens);
		camposEMensagens = new ArrayList<>();
		camposEMensagens.add("senha");
		camposEMensagens.add("SSSSSNão pode ser nulo");
		camposEMensagens.add("SSSSSTem que ter os caracteres");
		erros.add(camposEMensagens);
		for (List<String> erro : erros){
			System.out.println(erro.get(0));
		}
//		System.out.println(erros);
//		System.out.println(erros.get(0));
//		System.out.println(erros.get(1));
	}

}
