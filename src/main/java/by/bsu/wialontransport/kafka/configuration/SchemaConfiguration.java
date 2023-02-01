package by.bsu.wialontransport.kafka.configuration;

import by.bsu.wialontransport.kafka.model.TransportableData;
import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchemaConfiguration {
    private final ReflectData reflectData;

    public SchemaConfiguration() {
        this.reflectData = ReflectData.get();
    }

    @Bean
    public Schema transportableDataSchema() {
        return this.reflectData.getSchema(TransportableData.class);
    }
}