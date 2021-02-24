================================================
* PONTIFICIA UNIVERSIDAD JAVERIANA             *
* GRUPO DE INVESTIGACI�N SRDRE Y TAKINA        *    
* INTRODUCCI�N AL PROVEEDOR DE SERVICIOS BESA  *
* Fecha: 2 noviembre de 2012                   *
* Para la versi�n de software BESA3BETA        * 
================================================


[Service Provider Agent]

BESA consta de un agente denominado ServiceProviderAgent quien tiene la responsabilidad de actuar como un proveedor de servicios, el cual en un contexto de una arquitectura l�gica basada en capas act�a como un front-end entre la capa superior y la capa subordinada (la que presta los servicios). En el contexto de simuladores es especialmente �til para establecer una interfaz entre el mundo simulado y la l�gica del sistema. Para tal fin, el agente ServiceProvider se sirve del concepto de adaptador el cual interactuara con el mundo simulado y ofrece servicios a la l�gica.El agente ServiceProvider provee los servicios de dos formas: s�ncrona y as�ncrona. La forma s�ncrona indica un protocolo llamada respuesta (similar a una invocaci�n pero en t�rminos distribuidos). La forma as�ncrona indica el envi� de un evento unidireccional (similar a un comando en donde el evento produce unos resultados en el lado del receptor).


[Implementaci�n del agente SP]

Se debe crear el mundo y luego el adaptador para que interactu� con el mundo:

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


[Implementaci�n de un agente oferente de servicios]

La idea b�sica es que los agentes BESA que desean ofrecer servicios se suscriben frente al agente SP. Para ello hay dos formas:

Forma s�ncrona:

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

Forma as�ncrona:

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