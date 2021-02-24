
package BESA.Extern;

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

    private final static QName _Restore_QNAME = new QName("http://Interop.BESA/", "restore");
    private final static QName _RestoreResponse_QNAME = new QName("http://Interop.BESA/", "restoreResponse");

    private final static QName _RegisterBAP_QNAME = new QName("http://Interop.BESA/", "registerBAP");
    private final static QName _RegisterBAPResponse_QNAME = new QName("http://Interop.BESA/", "registerBAPResponse");

    private final static QName _RegisterAgent_QNAME = new QName("http://Interop.BESA/", "registerAgent");
    private final static QName _RegisterAgentResponse_QNAME = new QName("http://Interop.BESA/", "registerAgentResponse");

    private final static QName _GetAgentLocation_QNAME = new QName("http://Interop.BESA/", "getAgentLocation");
    private final static QName _GetAgentLocationResponse_QNAME = new QName("http://Interop.BESA/", "getAgentLocationResponse");

    private final static QName _GetMasterLocation_QNAME = new QName("http://Interop.BESA/", "getMasterLocation");
    private final static QName _GetMasterLocationResponse_QNAME = new QName("http://Interop.BESA/", "getMasterLocationResponse");

    private final static QName _GetBAPsLocation_QNAME = new QName("http://Interop.BESA/", "getBAPsLocation");
    private final static QName _GetBAPsLocationResponse_QNAME = new QName("http://Interop.BESA/", "getBAPsLocationResponse");

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

    /**
     * Create an instance of {@link SayHello }
     *
     */
    public Restore createRestore() {
        return new Restore();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHello }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://Interop.BESA/", name = "restore")
    public JAXBElement<Restore> createRestore(Restore value) {
        return new JAXBElement<Restore>(_Restore_QNAME, Restore.class, null, value);
    }

    /**
     * Create an instance of {@link SayHello }
     *
     */
    public RestoreResponse createRestoreResponse() {
        return new RestoreResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHello }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://Interop.BESA/", name = "restoreResponse")
    public JAXBElement<RestoreResponse> createRestoreResponse(RestoreResponse value) {
        return new JAXBElement<RestoreResponse>(_RestoreResponse_QNAME, RestoreResponse.class, null, value);
    }

    //========================================================================//
    //
    //========================================================================//

    /**
     * Create an instance of {@link SayHello }
     * 
     */
    public RegisterBAP createRegisterBAP() {
        return new RegisterBAP();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHello }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Interop.BESA/", name = "registerBAP")
    public JAXBElement<RegisterBAP> createRegisterBAP(RegisterBAP value) {
        return new JAXBElement<RegisterBAP>(_RegisterBAP_QNAME, RegisterBAP.class, null, value);
    }

    /**
     * Create an instance of {@link SayHello }
     *
     */
    public RegisterBAPResponse createRegisterBAPResponse() {
        return new RegisterBAPResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHello }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://Interop.BESA/", name = "registerBAPResponse")
    public JAXBElement<RegisterBAPResponse> createRegisterBAPResponse(RegisterBAPResponse value) {
        return new JAXBElement<RegisterBAPResponse>(_RegisterBAPResponse_QNAME, RegisterBAPResponse.class, null, value);
    }
    //========================================================================//
    //
    //========================================================================//

    /**
     * Create an instance of {@link SayHello }
     *
     */
    public RegisterAgent createRegisterAgent() {
        return new RegisterAgent();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHello }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://Interop.BESA/", name = "registerAgent")
    public JAXBElement<RegisterAgent> createRegisterAgent(RegisterAgent value) {
        return new JAXBElement<RegisterAgent>(_RegisterAgent_QNAME, RegisterAgent.class, null, value);
    }

    /**
     * Create an instance of {@link SayHello }
     *
     */
    public RegisterAgentResponse createRegisterAgentResponse() {
        return new RegisterAgentResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHello }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://Interop.BESA/", name = "registerAgentResponse")
    public JAXBElement<RegisterAgentResponse> createRegisterAgentResponse(RegisterAgentResponse value) {
        return new JAXBElement<RegisterAgentResponse>(_RegisterAgentResponse_QNAME, RegisterAgentResponse.class, null, value);
    }
    //========================================================================//
    //
    //========================================================================//

    /**
     * Create an instance of {@link SayHello }
     *
     */
    public GetAgentLocation createGetAgentLocation() {
        return new GetAgentLocation();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHello }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://Interop.BESA/", name = "getAgentLocation")
    public JAXBElement<GetAgentLocation> createGetAgentLocation(GetAgentLocation value) {
        return new JAXBElement<GetAgentLocation>(_GetAgentLocation_QNAME, GetAgentLocation.class, null, value);
    }

    /**
     * Create an instance of {@link SayHelloResponse }
     *
     */
    public GetAgentLocationResponse createGetAgentLocationResponse() {
        return new GetAgentLocationResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHelloResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://Interop.BESA/", name = "getAgentLocationResponse")
    public JAXBElement<GetAgentLocationResponse> createGetAgentLocationResponse(GetAgentLocationResponse value) {
        return new JAXBElement<GetAgentLocationResponse>(_GetAgentLocationResponse_QNAME, GetAgentLocationResponse.class, null, value);
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

    //========================================================================//
    //
    //========================================================================//

    /**
     * Create an instance of {@link SayHello }
     *
     */
    public GetBAPsLocation createGetBAPsLocation() {
        return new GetBAPsLocation();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHello }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://Interop.BESA/", name = "getBAPsLocation")
    public JAXBElement<GetBAPsLocation> createGetBAPsLocation(GetBAPsLocation value) {
        return new JAXBElement<GetBAPsLocation>(_GetBAPsLocation_QNAME, GetBAPsLocation.class, null, value);
    }

    /**
     * Create an instance of {@link SayHelloResponse }
     *
     */
    public GetBAPsLocationResponse createGetBAPsLocationResponse() {
        return new GetBAPsLocationResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHelloResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://Interop.BESA/", name = "getBAPsLocationResponse")
    public JAXBElement<GetBAPsLocationResponse> createSayHelloResponse(GetBAPsLocationResponse value) {
        return new JAXBElement<GetBAPsLocationResponse>(_GetBAPsLocationResponse_QNAME,GetBAPsLocationResponse.class, null, value);
    }
}
