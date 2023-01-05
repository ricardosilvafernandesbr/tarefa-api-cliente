package br.com.curso;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.curso.dto.ClientDTO;
import br.com.curso.entities.Client;
import br.com.curso.exception.DatabaseException;
import br.com.curso.exception.EntityNotFoundException;
import br.com.curso.exception.ResourceNotFoundException;
import br.com.curso.repositories.ClientRepository;

//Tonar essa classe gerenciada pelo Spring
@Service
public class ClientService {
	@Autowired
	private ClientRepository clientRepository;

	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(Pageable pageable) {
		Page<Client> list = clientRepository.findAll(pageable);
		return list.map(x -> new ClientDTO(x));
	}

	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> obj = clientRepository.findById(id);
		return new ClientDTO(obj.orElseThrow(() -> new EntityNotFoundException("Entidade não encontrada!")));
	}

	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		Client entity = entytiToDto(dto);
		clientRepository.save(entity);
		return new ClientDTO(entity);
	}

	private Client entytiToDto(ClientDTO dto) {
		Client entity = new Client();
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setBirthDate(dto.getBirthDate());
		entity.setChildren(dto.getChildren());
		return entity;
	}

	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
			Client entity = clientRepository.getOne(id);
			entity = entytiToDto(dto);
			entity.setId(id);
			entity = clientRepository.save(entity);
			return new ClientDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id não encontrado !" + id);
		}
	}

	public void delete(Long id) {
		try {
			clientRepository.deleteById(id);

		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id não existe !");
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Violação de integridade");
		}

	}
}
