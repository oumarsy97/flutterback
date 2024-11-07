package sn.odc.oumar.springproject.Web.Controller.Impl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.odc.oumar.springproject.Datas.listeners.impl.SoftDeletable;
import sn.odc.oumar.springproject.Exceptions.ControllerException;
import sn.odc.oumar.springproject.Exceptions.ServiceException;
import sn.odc.oumar.springproject.Services.Interfaces.BaseService;

import java.util.List;
import java.util.Optional;

public abstract class BaseControllerImpl<T extends SoftDeletable, Dto, ID> {

    protected final BaseService<T, ID> service;

    protected BaseControllerImpl(BaseService<T, ID> service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<T> create(@RequestBody Dto dto) {
        T entity = convertToEntity(dto); // Conversion DTO -> Entity
        T createdEntity = service.create(entity); // Création de l'entité
        return ResponseEntity.ok(createdEntity); // Conversion Entity -> DTO
    }

    @GetMapping("/{id}")
    public Dto findById(@PathVariable ID id) {
        Optional<T> entity = service.findById(id);
        if (entity.isPresent()) {
            return convertToDto(entity.get()); // Conversion Entity -> DTO
        } else {
            // Le filtre gère la réponse en cas d'erreur
            throw new ControllerException("Entity not found");
        }
    }

    @GetMapping

    public List<T> readAll() {
        return   service.findAll();

    }

    @PutMapping("/{id}")
    public T update(@PathVariable ID id, @RequestBody Dto dto) {
        T entity = convertToEntity(dto); // Conversion DTO -> Entity
        T updatedEntity = service.update(id, entity); // Mise à jour de l'entité
        return updatedEntity; // Conversion Entity -> DTO
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable ID id) {
        service.delete(id);
        // Le filtre s'occupera d'envoyer une réponse de succès ou d'échec
    }

    // Méthodes abstraites à implémenter pour la conversion entre Entity et DTO
    protected abstract T convertToEntity(Dto dto);

    protected abstract Dto convertToDto(T entity);
}
