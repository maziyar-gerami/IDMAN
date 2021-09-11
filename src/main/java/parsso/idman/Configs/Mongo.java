package parsso.idman.Configs;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import parsso.idman.Helpers.Communicate.Token;

import java.util.Collection;
import java.util.Collections;

@SuppressWarnings("unchecked")
@Configuration
public class Mongo extends AbstractMongoClientConfiguration {
	@Value("${mongo.uri}")
	private String connectionString;
	@Value("${mongo.db}")
	private String databaseName;

	@Override
	protected String getDatabaseName() {
		return databaseName;
	}

	@Override
	public MongoClient mongoClient() {
		return MongoClients.create(connectionString);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Collection getMappingBasePackages() {
		return Collections.singleton(Token.collection);
	}
}
