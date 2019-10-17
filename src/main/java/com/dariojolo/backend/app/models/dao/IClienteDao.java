package com.dariojolo.backend.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.dariojolo.backend.app.models.entity.Cliente;

public interface IClienteDao extends CrudRepository<Cliente, Long>{

	
}
