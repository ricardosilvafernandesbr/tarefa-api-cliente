package br.com.curso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.curso.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long>{
		Client findByName(String name);
}
