package net.gymtij;

import com.google.gson.Gson;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

public class ConsultaSocio {

    public static String consultaSocio(Integer id) {
        // Esta variable res la usaremos únicamente para dar un respuesta final

        String URL = "http://192.168.0.70:5000/api/";
        try {
            // Creamos el cliente de conexión al API Restful
            Client client = ClientBuilder.newClient();

            // Creamos el target lo cuál es nuestra URL junto con el nombre del método a llamar
            WebTarget target = client.target(URL + "socios/credencial/" + id);

            // Creamos nuestra solicitud que realizará el request
            Invocation.Builder solicitud = target.request();

            // Convertimos el objeto req a un json
            Gson gson = new Gson();

            // Enviamos nuestro json vía post al API Restful
            Response post = solicitud.get();
           
            String resJson = post.readEntity(String.class);
            
            // Recibimos la respuesta y la leemos en una clase de tipo String, en caso de
            // que el json sea tipo json y no string, debemos usar la clase de tipo
            // JsonObject.class en lugar de String.class
            SocioResponse socioResponse = gson.fromJson(resJson, SocioResponse.class);

            // Imprimimos el status de la solicitud
            System.out.println("Estatus: " + post.getStatus());
            
            
            switch (post.getStatus()) {
            case 200:
                return socioResponse.socio.nombre + " " + socioResponse.socio.paterno + " " + socioResponse.socio.materno;
            default:
                break;
            }

            
        } catch (Exception e) {

            e.printStackTrace();
            // En caso de un error en la solicitud, llenaremos res con la exceptión para
            // verificar que sucedió
            String res = e.toString();
            System.out.println(res);
            return null;

        }
        // Imprimimos la respuesta del API Restful
        return null;
        
    }
}