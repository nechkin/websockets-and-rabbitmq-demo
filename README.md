# Demo of using WebSockets and RabbitMQ as a Stomp broker

### Description

### Technologies used

* Spring Boot
* Spring AMQP
* Spring Integration
* Maven
* WebSocket
* Stomp protocol

### Components

* RabbitMQ

One may start a RabbitMQ instance like so (management console accessible at localhost:15672, 
rabbitmq/rabbitmq):
```
cd docker
docker-compose up -d 
```

* GatewayApplication

Exposes an HTTP access at localhost:8001. It is secured with default Spring Boot security autoconfig. Set up users 
are: user/user and john/john.<br/>

After login, a page will render that shows messages published to global topic, user topic and current window topic 
(more on these below). There 3 sources, that feed data to the page via a RabbitMQ stomp broker.<br/> 
 
Two websocket connections will be made. Sockjs library is used to support websocket connection. Websocket connections 
will be used to communicate with StompBrokerRelay on the server. Stomp js library is used to support handling stomp 
protocol messages.

The global topic has a constant flow of message, messages are delivered to every subscriber.

The user topic is used for messages for the user, identified by the username. Messages for a single user, either from 
different browser windows or different sessions (try to login in a separate browser/incognito window), are delivered 
to all windows for that user with opened websocket/stomp connections. Use an input field to send a message to the 
user topic.

Message to the window topic are delivered to the window socket. Use an input field to send a message to the window 
topic, only the same window will display the echo of this message.

* SenderRabbitApplication

Usees RabbitTemplate to send message to RabbitMQ exchanges. Uses AMQP protocol.

* SenderStompIntegrationApplication

Usees ReactorNettyTcpStompClient to send messages to desired Stomp destinations. Uses Stomp protocol.

* SenderStompRelayApplication

Mimics the configuration of the GatewayApplication to connect to the stomp broker (RabbitMQ in our case). A 
UserRegistryBroadcast("/topic/simp-user-registry") configuration is used within SenderStompRelayApplication and 
GatewayApplication to share Stomp users/sessions. A Spring messaging SimpMessagingTemplate is used to send messages. 
Uses Stomp protocol.

### Links

* https://stomp.github.io/stomp-specification-1.2.html
* Todo: add more links