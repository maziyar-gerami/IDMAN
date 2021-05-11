package parsso.idman.Configs;


import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import parsso.idman.Helpers.Communicate.Token;

import java.util.Collection;
import java.util.Collections;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

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

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));


        return MongoClients.create(connectionString);
    }

    @Override
    public Collection getMappingBasePackages() {
        return Collections.singleton(Token.collection);
    }
}
