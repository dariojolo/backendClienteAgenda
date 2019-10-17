package com.dariojolo.backend.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dariojolo.backend.app.models.entity.Cliente;
import com.dariojolo.backend.app.models.services.IClienteService;

@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClienteRestController {
	
	@Autowired
	private IClienteService clienteService;

	@GetMapping("/clientes")
	public List<Cliente>index(){
		return clienteService.findAll();
	}	
	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> show (@PathVariable Long id) {
		Cliente cliente = null;
		Map<String,Object> response = new HashMap<>();
		try {
			cliente = clienteService.findById(id);
		}catch(DataAccessException e){
			response.put("mensaje", "No se puede realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(":_").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (cliente == null) {
			response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}
	@PostMapping("/clientes")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@RequestBody Cliente cliente) {
		Cliente clienteNuevo = null;
		Map<String,Object> response = new HashMap<>();
		try {
		 clienteNuevo = clienteService.save(cliente);
		}catch(DataAccessException e) {
			response.put("mensaje", "No se puede realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(":_").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El cliente se ha creado con exito!");
		response.put("cliente", clienteNuevo);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	@PutMapping("/clientes/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> update (@RequestBody Cliente cliente, @PathVariable Long id) {
		Cliente cl = clienteService.findById(id);;
		Map<String,Object> response = new HashMap<>();
		Cliente clupdated = null;
		if (cl == null) {
			response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no se pudo editar")));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
		cl.setNombre(cliente.getNombre());
		cl.setApellido(cliente.getApellido());
		cl.setEmail(cliente.getEmail());
		clupdated = clienteService.save(cl);
		}catch(DataAccessException e) {
			response.put("mensaje", "No se puede realizar el update en la base de datos");
			response.put("error", e.getMessage().concat(":_").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El cliente se ha creado con exito!");
		response.put("cliente", clupdated);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/clientes/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String,Object> response = new HashMap<>();
		try {
			clienteService.delete(id);	
		}catch(DataAccessException e) {
			response.put("mensaje", "No se puede eliminar en la base de datos");
			response.put("error", e.getMessage().concat(":_").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El cliente ha sido eliminado");
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
	}
}
