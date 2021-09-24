package parsso.idman.Mobile.RepoImpls;


import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint(value = "/webSocket")
public class Socket {
	private static final Set<Socket> connections = new CopyOnWriteArraySet<>();
	public Session session;

	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		connections.add(this);
		System.out.println("websocket is open");

	}

	@OnMessage
	public void onMessage(String message) {
		broadcast(message);
	}

	@OnClose
	public void onClose() {
		System.out.println("websocket closed");
	}

	@OnError
	public void onError(Throwable throwable) {
		throwable.printStackTrace();
	}

	public void broadcast(String message) {
		for (Socket current : connections)
			try {
				current.session.getBasicRemote().sendText(message);

			} catch (IOException e) {
				e.printStackTrace();
			}

	}


}
