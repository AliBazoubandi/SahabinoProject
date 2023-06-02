package com.myfileingesterpart;

import com.myfileingesterpart.kafka.JsonKafkaProducer;
import com.myfileingesterpart.api.model.MyLog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/*
* this is our main class
* in this class we read all the files from a directory and go through all of them.
* if the componentName of the file is firstComponent, the data are sent to firstComponent topic(myTopic)
* if the componentName of the file is secondComponent, the data are sent to secondComponent topic(yourTopic)
* if the componentName of the file is thirdComponent, the data are sent to thirdComponent topic(hisTopic)
* we read each line and the line will be separated into 6 parts based on a space,
* and we used limit in our split method because we want everything that is after the "-" to be considered as message for that log
 */
@SpringBootApplication
public class MyFileIngesterPartApplication {


	public static void main(String[] args) {
		ApplicationContext context =  SpringApplication.run(MyFileIngesterPartApplication.class, args);
		JsonKafkaProducer jsonKafkaProducer = context.getBean(JsonKafkaProducer.class);

		String filePath = "D:\\sahbino\\Logs";
		File directory = new File(filePath);
		File[] files = directory.listFiles();
		assert files != null;
		for (File file : files) {
			try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
				String oneLog;
				String topic = file.getName().split("-")[0];
				while ((oneLog = bufferedReader.readLine()) != null) {
					String[] parts = oneLog.split("\s", 7);
					MyLog log = new MyLog();
					log.setDate(parts[0]);
					log.setTime(parts[1]);
					log.setComponentName(topic);
					log.setThreadName(parts[2]);
					log.setType(parts[3]);
					log.setClassName(parts[4]);
					log.setMessage(parts[6]);
					switch (topic) {
						case "firstComponent" -> jsonKafkaProducer.sendMessage(log, "myTopic");
						case "secondComponent" -> jsonKafkaProducer.sendMessage(log, "yourTopic");
						case "thirdComponent" -> jsonKafkaProducer.sendMessage(log, "hisTopic");
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
