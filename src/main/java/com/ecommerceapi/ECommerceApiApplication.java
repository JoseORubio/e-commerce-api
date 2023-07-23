package com.ecommerceapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "E-Commerce Swagger", version = "1"
		, description = "API desenvolvida para sistema de E-Commerce"))
@SecurityScheme(name = "ecommerce", scheme = "basic", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class ECommerceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ECommerceApiApplication.class, args);
//		System.out.println(new BCryptPasswordEncoder().encode("asd123aaD"));

//		List<List<String>> erros = new ArrayList<>();
//		List<String> camposEMensagens;
//		camposEMensagens = new ArrayList<>();
//		camposEMensagens.add("nome");
//		camposEMensagens.add("Não pode ser nulo");
//		camposEMensagens.add("Tem que ter os caracteres");
//		erros.add(camposEMensagens);
//		camposEMensagens = new ArrayList<>();
//		camposEMensagens.add("senha");
//		camposEMensagens.add("SSSSSNão pode ser nulo");
//		camposEMensagens.add("SSSSSTem que ter os caracteres");
//		erros.add(camposEMensagens);
//		for (List<String> erro : erros){
//			System.out.println(erro.get(0));
//		}
////		System.out.println(erros);
////		System.out.println(erros.get(0));
////		System.out.println(erros.get(1));


//		System.out.println(new BCryptPasswordEncoder().encode("123123"));
//		System.out.println(new BCryptPasswordEncoder().matches(
//				"abc123","$2a$10$1dnkT6ZepB0n9vAkfziB0..XmY1K.a7BDDZ3R265kiUnC8E1OS7LK"));
//	System.out.println(new BCryptPasswordEncoder().matches(
//				"abc123","$2a$10$Zg2UXG1SU3B3bkyGje8O/.IrVjQJiQJiXjC52FSHr187NiJNdMWje"));
//	System.out.println(new BCryptPasswordEncoder().matches(
//				"abc123","$2a$10$xV3L9iX.5p04j9dEWK/53.sLW9VCkCgscbxmF9601DenJX3OoeDAq"));
//	System.out.println(new BCryptPasswordEncoder().matches(
//				"abc123","$2a$10$2C/w1uWD3GnDtw56/2hSKeJRM5jM8.Bg2sc1YMhpAX2bG4bkLV9Li"));
	}

}
