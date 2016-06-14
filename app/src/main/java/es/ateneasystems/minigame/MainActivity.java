package es.ateneasystems.minigame;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {
    private Button btn_enviar;
    private Button btn_conectar;
    private EditText et_mensaje;
    private TextView tv_recibido;
    private Context ctx;
    private Activity myActivity;
    private String marcaModelo;
    ZSockets socket = new ZSockets();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_enviar = (Button) findViewById(R.id.btn_enviar);
        btn_conectar = (Button) findViewById(R.id.btn_conectar);
        et_mensaje = (EditText) findViewById(R.id.et_mensaje);
        tv_recibido = (TextView) findViewById(R.id.tv_recibido);
        marcaModelo = android.os.Build.MANUFACTURER + " - " + android.os.Build.MODEL;//Marca y modelo
        ctx = getApplication().getApplicationContext();
        myActivity = this;
        socket.mSocket.connect();
        socket.mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);//Cuando se produce un error
        socket.mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);//Cuando se produce un error
        //socket.mSocket.on("new message", onNewMessage);//Cuando se envia un mensaje
        //socket.mSocket.on("typing", onTyping);//Cuando alquien esta hacindo uso

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviar();
                borrarMensaje();
            }
        });

        btn_conectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conectarAljuego();
            }
        });

        /**
         * funciones de inicio
         */
        meConecto();//Envia el aviso de conexion al servidor


    }

    /**
     * Fuciones
     */
    //Envair Mensaje
    public void enviar() {
        JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON

        try {
            cadena.put("mensaje", et_mensaje.getText());//Le asignamos los datos que necesitemos
            cadena.put("dispositivo", marcaModelo);//Le asignamos los datos que necesitemos
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.enviarSocket("new message", cadena);
    }

    //Conectarme al Juego pulsando el boton
    public void conectarAljuego() {
        JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON

        try {
            cadena.put("usuario", "Paco");//Le asignamos los datos que necesitemos
            cadena.put("dispositivo", marcaModelo);//Le asignamos los datos que necesitemos
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.enviarSocket("conectar al juego", cadena);
    }

    //Nuevo Usuario conectado
    public void meConecto() {
        JSONObject cadena = new JSONObject(); //Creamos un objeto de tipo JSON
        try {
            cadena.put("dispositivo", marcaModelo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.enviarSocket("nuevo dispositivo conectado", cadena);
    }

    ;

    //Borrar mensaje
    public void borrarMensaje() {
        et_mensaje.setText("");
    }

    /**
     * Error en la conexion
     */
    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            myActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(myActivity.getApplicationContext(),
                            "ERROR", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    /**
     * Al recibir un mensaje
     */
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            myActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //final JSONObject data = (JSONObject) final args[0];
                    JSONObject data;
                    data = (JSONObject) args[0];
                    String mensajeRecibido;
                    try {
                        mensajeRecibido = data.getString("mensaje");

                    } catch (JSONException e) {
                        return;
                    }

                    // add the message to view
                    tv_recibido.setText(mensajeRecibido);
                }
            });
        }


    };
    /**
     * Al estar escribiendo
     */
    /*private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            myActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        return;
                    }
                    addTyping(username);
                }
            });
        }
    };
    private Emitter.Listener onStopTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        return;
                    }
                    removeTyping(username);
                }
            });
        }
    };

    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            if (!mTyping) return;

            mTyping = false;
            mSocket.emit("stop typing");
        }
    };
*/


}

