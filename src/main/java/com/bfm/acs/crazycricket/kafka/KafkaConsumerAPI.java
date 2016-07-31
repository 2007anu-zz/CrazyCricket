package com.bfm.acs.crazycricket.kafka;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import com.bfm.acs.crazycricket.CrazyCricketProtos.Game;
import com.bfm.acs.crazycricket.dao.DataStorage;
import com.google.protobuf.InvalidProtocolBufferException;

public class KafkaConsumerAPI implements Runnable {
	private String[] topics;
	private final String broker;
	private final KafkaConsumer<String, byte[]> consumer;
	private final DataStorage datasource;
	private final AtomicBoolean closed = new AtomicBoolean(false);

	public KafkaConsumerAPI(String[] topics, String broker, DataStorage datasource) {
		super();
		this.topics = topics;
		this.broker = broker;
		this.consumer = new KafkaConsumer<String, byte[]>(consumerConfig());
		this.datasource = datasource;
	}

	@Override
	public void run() {
		consumer.subscribe(Arrays.asList(this.topics));
		try {
			while (!closed.get()) {
				ConsumerRecords<String, byte[]> records = consumer.poll(100);
				for (ConsumerRecord<String, byte[]> record : records) {
					System.out.println(record.offset() + ": " + record.value());
					Game game = Game.parseFrom(record.value());
					datasource.save(game);
				}
			}
		} catch (WakeupException e) {
			if (!this.closed.get())
				throw e;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		} finally {
			consumer.close();
		}
	}

	public void shutdown() {
		this.closed.set(true);
		consumer.wakeup();
	}

	private Properties consumerConfig() {
		Properties props = new Properties();
		props.put("bootstrap.servers", this.broker);
		props.put("group.id", "test");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "30000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		return props;
	}

}
