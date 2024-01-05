package gob.mdmq.mdmq_consumer_cmi.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexDefinition;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gob.mdmq.mdmq_consumer_cmi.model.Datos;
import org.bson.Document;

import org.slf4j.Logger;

@Component
@RestController
@RequestMapping("/datos")
public class Consumer {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    private final MongoTemplate mongoTemplate;

    public Consumer(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Document> obtenerTodosLosDocumentos() {
        return mongoTemplate.findAll(Document.class, "INFORMACION");
    }

    public List<Document> buscarPorIdYSistema(String id, String sistema) {
        Query query = new Query();
        query.addCriteria(Criteria.where("ID").is(id).and("SISTEMA").is(sistema));
        return mongoTemplate.find(query, Document.class, "INFORMACION");
    }

    public void actualizarDocumento(String id, String nuevoValorSistema) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().set("sistema", nuevoValorSistema).set(id, nuevoValorSistema);
        mongoTemplate.updateFirst(query, update, "INFORMACION");
    }

    @KafkaListener(topics = { "cmi" }, groupId = "Group100")
    public void consumeMessage(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Datos datos = mapper.readValue(message, Datos.class);
            Object informacion = datos.getDatos();

            // Borrar variable enviada
            @SuppressWarnings("unchecked")
            Map<String, Object> informacionMap = (Map<String, Object>) datos.getDatos();
            informacionMap.remove("sistema");

            // Obtener en de la coleccion el sistema en el que se va a almacenar
            Object SISTEMA = datos.getDatos().get("SISTEMA");
            Object ID = datos.getDatos().get("ID");

            List<Document> buscarDato =buscarPorIdYSistema(ID.toString(), SISTEMA.toString());
            if (buscarDato.size()>0) {
                //Actualizar
                Query query = new Query(Criteria.where("_id").is(buscarDato.get(0).get("_id")));
                Update update = new Update();
                for (Map.Entry<String, Object> entry : informacionMap.entrySet()) {
                    update.set(entry.getKey(), entry.getValue());
                }
                update.set("updatedAt", new Date());
                mongoTemplate.updateFirst(query, update, SISTEMA.toString().toUpperCase());
            }else{
                //Crear
                mongoTemplate.save(informacion, SISTEMA.toString().toUpperCase());
            }
            acknowledgment.acknowledge();
            logger.info("Guardado correctamente");
        } catch (Exception e) {
            logger.info("Error al intentar guardar la información," + e.getMessage());
        }
    }

    public void crearIndex(String collectionName, String valor) {
        IndexOperations indexOps = mongoTemplate.indexOps(collectionName);
        IndexDefinition indexDefinition = new Index().on(valor, Direction.ASC);
        indexOps.ensureIndex(indexDefinition);
    }
}
