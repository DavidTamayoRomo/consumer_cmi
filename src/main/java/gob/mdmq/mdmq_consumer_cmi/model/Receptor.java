package gob.mdmq.mdmq_consumer_cmi.model;

import java.util.HashMap;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document
public class Receptor {
    @Id
    private String id;
    private Map<String, Object> datos;

    public Receptor() {
        this.datos = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getDatos() {
        return datos;
    }

    public void setDatos(Map<String, Object> datos) {
        this.datos = datos;
    }

    public void addDato(String key, Object value) {
        this.datos.put(key, value);
    }

    public Object getDato(String key) {
        return this.datos.get(key);
    }
}
