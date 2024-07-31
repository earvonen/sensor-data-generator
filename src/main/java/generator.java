import java.util.Random;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import org.eclipse.microprofile.reactive.messaging.*;

import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
@Startup
public class generator {

    public static void main(String[] args) {
        System.out.println("Hello");
    }

    private static Double previousValue = 0.0;
    private static Random random = new Random();

    @Inject
    @Channel("sensor-data")
    Emitter<String> emitter;

    void onStart(@Observes StartupEvent ev) {
        
        while(true) {
            Double newValue = previousValue + (random.nextDouble() - 0.5)/10;
            newValue = Math.max(-1, newValue);
            newValue = Math.min(1, newValue);

            try {
                emitter.send(newValue.toString());
                previousValue = newValue;
                Thread.sleep(1000);
            }catch(Exception e) {
                e.printStackTrace();
                //ignore this
            }
        }
    }
}
