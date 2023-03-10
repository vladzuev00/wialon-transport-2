package by.bsu.wialontransport.kafka.producer;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public final class KafkaInboundDataProducer {
    //private final KafkaTemplate<Long, Data> kafkaTemplate;
    private final String topicName;

    public KafkaInboundDataProducer(//final KafkaTemplate<Long, Data> kafkaTemplate,
                                    @Value("${kafka.topic.inbound-data.name}") final String topicName) {
        //this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void send(final Data data) {
        //this.kafkaTemplate.send(new ProducerRecord<>(this.topicName, tracker.getId(), data));
    }
}
