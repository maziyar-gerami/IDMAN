package parsso.idman.configs;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import parsso.idman.helpers.communicate.Token;

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
