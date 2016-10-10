package org.wsock.example;

import org.springframework.stereotype.Component;
import org.wsock.pub.WebSocket;
import org.wsock.pub.WsockInit;
import org.wsock.pub.WsockService;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by joco on 10.10.16.
 */
@WebSocket("/chat")
@Component
public class ChatExample implements WsockInit {
    private AtomicInteger guestNum = new AtomicInteger(1);

    private AtomicInteger userCount = new AtomicInteger(0);

    @Override
    public void init(WsockService wsock) {
        final String bucket = "all";
        wsock.tokenAcceptor(token -> true); // accept everything
        wsock.onConnect(session -> {
            session.data().put("nick", "Guest #" + guestNum.incrementAndGet());
            wsock.broadcast(bucket, "/user/count/change", userCount.incrementAndGet());
            return bucket;
        });
        wsock.onDisconnect(s -> {
            wsock.broadcast(bucket, "/user/count/change", userCount.decrementAndGet());
        });
        wsock.on("/get/user/count", (Void) -> {
            return userCount.get();
        });
        wsock.on("/get/nick", (Void) -> {
            return wsock.current().data().get("nick");
        });
        final String systemNick = ".sys*";
        wsock.on("/set/nick", (String nick) -> {
            String current = (String) wsock.current().data().get("nick");
            if(systemNick.equals(nick)) {
                wsock.broadcast(bucket, "/message", new MessageBroadcast(systemNick, "Haha, nice try, user '"+current+"' wants to be me."));
            } else {
                wsock.current().data().put("nick", nick);
                wsock.broadcast(bucket, "/message", new MessageBroadcast(systemNick, "User '"+current+"' is now called '"+nick+"'"));
            }
        });

        wsock.on("/send/message", (String msg) -> {
            String nick = (String) wsock.current().data().get("nick");
            wsock.broadcast(bucket, "/message", new MessageBroadcast(nick, msg));
        });
    }
}
