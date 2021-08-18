package parsso.idman.Utils.SMS.Magfa.Classes;


import javax.xml.namespace.QName;
import javax.xml.ws.*;
import java.net.MalformedURLException;
import java.net.URL;

@WebServiceClient(name = "MagfaSoapServer", targetNamespace = "http://impl.webservice.magfa.com/", wsdlLocation = "https://sms.magfa.com/api/soap/sms/v2/server?wsdl")
public class MagfaSoapServer_Service
        extends Service {
    private final static URL MAGFASOAPSERVER_WSDL_LOCATION;
    private final static WebServiceException MAGFASOAPSERVER_EXCEPTION;
    private final static QName MAGFASOAPSERVER_QNAME = new QName("http://impl.webservice.magfa.com/", "MagfaSoapServer");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("https://sms.magfa.com/api/soap/sms/v2/server?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        MAGFASOAPSERVER_WSDL_LOCATION = url;
        MAGFASOAPSERVER_EXCEPTION = e;
    }


    public MagfaSoapServer_Service() {
        super(__getWsdlLocation(), MAGFASOAPSERVER_QNAME);
    }

    public MagfaSoapServer_Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), MAGFASOAPSERVER_QNAME, features);
    }

    public MagfaSoapServer_Service(URL wsdlLocation) {
        super(wsdlLocation, MAGFASOAPSERVER_QNAME);
    }

    public MagfaSoapServer_Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, MAGFASOAPSERVER_QNAME, features);
    }

    public MagfaSoapServer_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public MagfaSoapServer_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    private static URL __getWsdlLocation() {
        if (MAGFASOAPSERVER_EXCEPTION != null) {
            throw MAGFASOAPSERVER_EXCEPTION;
        }
        return MAGFASOAPSERVER_WSDL_LOCATION;
    }

    @WebEndpoint(name = "MagfaSoapServer")
    public MagfaSoapServer getMagfaSoapServer() {
        return super.getPort(new QName("http://impl.webservice.magfa.com/", "MagfaSoapServer"), MagfaSoapServer.class);
    }

    @WebEndpoint(name = "MagfaSoapServer")
    public MagfaSoapServer getMagfaSoapServer(WebServiceFeature... features) {
        return super.getPort(new QName("http://impl.webservice.magfa.com/", "MagfaSoapServer"), MagfaSoapServer.class, features);
    }

}
