package com.bfm.acs.crazycricket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.bfm.acs.crazycricket.api.ServerConfig;
import com.bfm.acs.crazycricket.dao.DataStorage;

@SpringBootApplication
public class Application {

	public static void main(final String[] args) throws InterruptedException {
		if (args.length == 0) {
			System.out.println("Specify Kafka broker");
			System.exit(0);
		} else {
			ApplicationContext app = SpringApplication.run(Application.class, args);
			ServerConfig serviceConfig = app.getBean(ServerConfig.class);
			DataStorage dataStore = app.getBean(DataStorage.class);
			String[] topics = { "TEST", "LIMITED_OVERS", "TWENTY_TWENTY","OTHER" };
			// KafkaProcessor kfp = new KafkaProcessor(dataStore, args[0],
			// topics);
			// Thread processor = new Thread(kfp);
			// processor.start();
		}
	}
}
