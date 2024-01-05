package gob.mdmq.mdmq_consumer_cmi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import gob.mdmq.mdmq_consumer_cmi.model.Datos;

@Repository
public interface DatoRepository extends MongoRepository<Datos, String> {
    
}
