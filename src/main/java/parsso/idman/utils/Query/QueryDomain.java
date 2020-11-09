package parsso.idman.utils.Query;

import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class QueryDomain {
    @Autowired
    MongoTemplate mongoTemplate;

    String collection= "IDMAN_DNS";

    private String ipToDomain(String ip) throws UnknownHostException {

        InetAddress addr = InetAddress.getByName(ip);
        return addr.getHostName();

    }
    public String matchIPwithService(String ip) throws UnknownHostException {
        String host = ipToDomain(ip);
        Query query = new Query(Criteria.where("host").is(host));
        String serviceName = mongoTemplate.findOne(query,String.class,collection);
        return serviceName;

    }
}
