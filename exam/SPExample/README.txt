================================================
* PONTIFICIA UNIVERSIDAD JAVERIANA             *
* GRUPO DE INVESTIGACIÓN SRDRE Y TAKINA        *    
* INTRODUCCIÓN AL PROVEEDOR DE SERVICIOS BESA  *
* Fecha: 2 noviembre de 2012                   *
* Para la versión de software BESA3BETA        * 
================================================


[Service Provider Agent]

BESA consta de un agente denominado ServiceProviderAgent quien tiene la responsabilidad de actuar como un proveedor de servicios, el cual en un contexto de una arquitectura lógica basada en capas actúa como un front-end entre la capa superior y la capa subordinada (la que presta los servicios). En el contexto de simuladores es especialmente útil para establecer una interfaz entre el mundo simulado y la lógica del sistema. Para tal fin, el agente ServiceProvider se sirve del concepto de adaptador el cual interactuara con el mundo simulado y ofrece servicios a la lógica.El agente ServiceProvider provee los servicios de dos formas: síncrona y asíncrona. La forma síncrona indica un protocolo llamada respuesta (similar a una invocación pero en términos distribuidos). La forma asíncrona indica el envió de un evento unidireccional (similar a un comando en donde el evento produce unos resultados en el lado del receptor).


[Implementación del agente SP]

Se debe crear el mundo y luego el adaptador para que interactué con el mundo:

	World world = new World(hiView);
	PresentationAdapter presentationAdapter = new PresentationAdapter();
	presentationAdapter.setWorld(world);

Se debe crear la estructura del agente SP:

	StructBESA theStruct = new StructBESA();
	theStruct.addBehavior("BehaviorServiceProvider");
	theStruct.bindGuard("BehaviorServiceProvider", GuardServiceProviderSuscribe.class);
	theStruct.bindGuard("BehaviorServiceProvider", GuardServiceProviderRequest.class);

Se debe crear el estado del agente SP:

	StateServiceProvider state = new StateServiceProvider(presentationAdapter, main.spDescriptorSendMessage);

Se debe crear el agente SP y iniciarlo.

	ServiceProviderMessageAgent serviceProviderMessageAgent = new ServiceProviderMessageAgent("SPMessageAgent", state, theStruct, PASSWORD);
        serviceProviderMessageAgent.start();

Y por ultimo al adaptador se le debe indicar el PS correspondiente.

	presentationAdapter.setServiceProviderBESA(serviceProviderMessageAgent);


[Implementación de un agente oferente de servicios]

La idea básica es que los agentes BESA que desean ofrecer servicios se suscriben frente al agente SP. Para ello hay dos formas:

Forma síncrona:

	String spAgId = AdmBESA.getInstance().lookupSPServiceInDirectory(ServiceProviderMessageAgent.SERVICEONDIRECTORY);
        AgHandlerBESA agH = AdmBESA.getInstance().getHandlerByAid(spAgId);
        //Crea el data de suscripcion
        ServiceProviderDataSuscribe spDataSuscribe = new ServiceProviderDataSuscribe(
        	GuardReplyTest.class.getName(),
                ServiceProviderBESA.SYNCHRONIC_SERVICE,
                SPServiceSendMessage.SERVICE_NAME,
                ResponseMessage.class.getName());
        //Crea el evento a enviar
        EventBESA evSP = new EventBESA(GuardServiceProviderSuscribe.class.getName(), spDataSuscribe);
        evSP.setSenderAgId(this.getAid());
        //Env?a el evento
        agH.sendEvent(evSP);

Forma asíncrona:

	//Crea el evento a enviar
        spDataSuscribe = new ServiceProviderDataSuscribe(
        	GuardTest.class.getName(),
                ServiceProviderBESA.ASYNCHRONIC_SERVICE,
                SPServiceSendMessage.SERVICE_NAME,
                MessageData.class.getName());
        evSP = new EventBESA(GuardServiceProviderSuscribe.class.getName(), spDataSuscribe);
        evSP.setSenderAgId(this.getAid());
        //Env?a el evento
        agH.sendEvent(evSP);            