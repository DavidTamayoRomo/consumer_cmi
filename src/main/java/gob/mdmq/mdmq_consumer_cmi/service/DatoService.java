package gob.mdmq.mdmq_consumer_cmi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import gob.mdmq.mdmq_consumer_cmi.model.Datos;
import gob.mdmq.mdmq_consumer_cmi.repository.DatoRepository;

@Service
public class DatoService {
    private final DatoRepository datoRepository;

    @Autowired
    public DatoService(DatoRepository datoRepository) {
        this.datoRepository = datoRepository;
    }

    public void save(Datos dato) {
        datoRepository.save(dato);
    }

    public Object listarDatos() {
        return datoRepository.findAll();
    }

    public Object listarDatosPag(Pageable pageable) {
        return datoRepository.findAll(pageable);
    }
    
}
