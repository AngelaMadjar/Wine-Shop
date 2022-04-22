package com.petarangela.wineeshop.service.impl;

import com.petarangela.wineeshop.model.Manufacturer;
import com.petarangela.wineeshop.model.Wine;
import com.petarangela.wineeshop.model.exceptions.InvalidManufacturerIdException;
import com.petarangela.wineeshop.repository.ManufacturerRepository;
import com.petarangela.wineeshop.service.ManufacturerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;

    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public List<Manufacturer> findAll() {
        return this.manufacturerRepository.findAll();
    }

    @Override
    public Manufacturer findById(Long id) {
        return this.manufacturerRepository.findById(id).orElseThrow(() -> new InvalidManufacturerIdException(id));
    }

    @Override
    public Optional<Manufacturer> save(String name, String address) {
        return Optional.of(this.manufacturerRepository.save(new Manufacturer(name,address)));
    }

    @Override
    public Manufacturer create(String name, String address) {
        Manufacturer manufacturer = new Manufacturer(name, address);
        return this.manufacturerRepository.save(manufacturer);
    }

    @Override
    public void deleteById(Long id) {
        this.manufacturerRepository.deleteById(id);
    }

}
