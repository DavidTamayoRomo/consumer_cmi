package gob.mdmq.mdmq_consumer_cmi.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Data;

@Data
public class Datos {
    Map<String, Object> datos = new LinkedHashMap<>();
    
    @JsonAnyGetter
    public Map<String, Object> getDatos(){
        return datos;
    }
    
    @JsonAnySetter
    public void setDatos(String Key, Object value){
        datos.put(Key, value);
    }
}
