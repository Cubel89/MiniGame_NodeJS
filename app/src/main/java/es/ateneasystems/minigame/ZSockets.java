package es.ateneasystems.minigame;

import android.content.Context;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by cubel on 25/04/15.
 */
public class ZSockets {
    protected Socket mSocket;
    protected Context ctx;

    {
        try {
            mSocket = IO.socket("http://81.202.9.119:8888");
        } catch (URISyntaxException e) {
        }
    }

    protected void enviarSocket(String evento, Object datos) {

        mSocket.emit(evento, datos);
    }


}
