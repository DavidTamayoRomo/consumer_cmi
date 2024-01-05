package gob.mdmq.mdmq_consumer_cmi.model;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Informacion {
    @Id
    private String idSistema;

    private List<Datos> informacion;
}
