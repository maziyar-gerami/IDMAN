package parsso.idman.utils.query;


import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class QueryDomain {
    final String collection = "IDMAN_DNS";
    final
    MongoTemplate mongoTemplate;

    public QueryDomain(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    private static String ipToDomain(String ip) throws UnknownHostException {

        InetAddress addr = InetAddress.getByName(ip);
        return addr.getHostName();

    }

    public String matchIPwithService(String ip) throws UnknownHostException {
        String host = ipToDomain(ip);
        Query query = new Query(Criteria.where("host").is(host));
        return mongoTemplate.findOne(query, String.class, collection);

    }
}
