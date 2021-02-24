
package BESA.Mobile.BAPLocator;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the juma.mohammad package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    //========================================================================//
    //
    //========================================================================//

    private final static QName _GetMasterLocation_QNAME = new QName("http://Interop.BESA/", "getMasterLocation");
    private final static QName _GetMasterLocationResponse_QNAME = new QName("http://Interop.BESA/", "getMasterLocationResponse");

      //========================================================================//
    //
    //========================================================================//

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: juma.mohammad
     * 
     */
    public ObjectFactory() {
    }

 
    //========================================================================//
    //
    //========================================================================//
    //getMasterLocation

    /**
     * Create an instance of {@link SayHello }
     *
     */
    public GetMasterLocation createGetMasterLocation() {
        return new GetMasterLocation();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHello }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://Interop.BESA/", name = "getMasterLocation")
    public JAXBElement<GetMasterLocation> createGetMasterLocation(GetMasterLocation value) {
        return new JAXBElement<GetMasterLocation>(_GetMasterLocation_QNAME, GetMasterLocation.class, null, value);
    }

    /**
     * Create an instance of {@link SayHelloResponse }
     *
     */
    public GetMasterLocationResponse createGetMasterLocationResponse() {
        return new GetMasterLocationResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHelloResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://Interop.BESA/", name = "getMasterLocationResponse")
    public JAXBElement<GetMasterLocationResponse> createGetAgentLocationResponse(GetMasterLocationResponse value) {
        return new JAXBElement<GetMasterLocationResponse>(_GetMasterLocationResponse_QNAME, GetMasterLocationResponse.class, null, value);
    }

  
}
