package net.gymtij;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ResourceBundle;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.http.client.entity.EntityBuilder;


public class GuardaHuella {

    public static void guardaHuella(Integer socioId, Integer dedo, String huella) {
        //Esta variable res la usaremos únicamente para dar un respuesta final
        String res;

        // ResourceBundle rd = ResourceBundle.getBundle("system");
        // String serverIp = rd.getString("configuration");
        // String URL = "http://" + serverIp + ":5000/api/";

        String URL = "http://192.168.0.76:5000/api/";

        try {
        //Creamos el cliente de conexión al API Restful
            Client client = ClientBuilder.newClient();

        //Creamos el target lo cuál es nuestra URL junto con el nombre del método a llamar
            WebTarget target = client.target(URL + "huellas/add/");

        //Creamos nuestra solicitud que realizará el request
            Invocation.Builder solicitud = target.request().header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MTAsInVzdWFyaW9JZCI6MSwiaXAiOiI6OmZmZmY6MTkyLjE2OC4wLjEzMCIsImxvZ2luRGF0ZSI6IjIwMjMtMDUtMTFUMDQ6MTg6NDcuNjc5WiIsImxhc3RDb25uZWN0aW9uIjoiMjAyMy0wNS0xMVQwNDoxODo0Ny42NzlaIiwidG9rZW4iOiJlNDQzZThmNS1kYWYzLTQ5MmEtYTgzMC1mY2MyMzNhYTE3NzQiLCJ1cGRhdGVkQXQiOiIyMDIzLTA1LTExVDA0OjE4OjQ3LjY3OVoiLCJjcmVhdGVkQXQiOiIyMDIzLTA1LTExVDA0OjE4OjQ3LjY3OVoifQ.NsV3EN31yJAyF2wA8TqoJlXQkvdIr3hUt0DI-r8yU_k");


        //Creamos y llenamos nuestro objeto BaseReq con los datos que solicita el API
            BaseReqHuella req = new BaseReqHuella();
            req.setSocioId(socioId);
            req.setDedo(dedo);
            req.setHuella(huella);

        //Convertimos el objeto req a un json
            Gson gson = new Gson();
            String jsonString = gson.toJson(req);
            // System.out.println(jsonString);
        //Enviamos nuestro json vía post al API Restful
            // Response post = solicitud.get();
            // Response post = solicitud.post(Entity.json(jsonString));
            Response post = solicitud.post(Entity.json(jsonString));

            // System.out.println("RES "+post.toString());

             //Recibimos la respuesta y la leemos en una clase de tipo String, 
            //  en caso de que el json sea tipo json y no string, 
            //  debemos usar la clase de tipo JsonObject.class en lugar de String.class
            // String resJson =post.readEntity(String.class);
            String resJson = post.readEntity(String.class);
            res = resJson ;
            System.out.println("json "+ resJson);
       
            // HuellaResponse huellaResponse = gson.fromJson(resJson, HuellaResponse.class);
            HuellaResponse huellaResponse = gson.fromJson(resJson, HuellaResponse.class);
            // System.out.println("Huella: " + huellaResponse.toString());


        //Imprimimos el status de la solicitud
            System.out.println("Estatus: " + post.getStatus());

            // switch (post.getStatus()) { 
            //         case 200:
            //                 res = resJson;
            //                 break;
            //     default:
            //                 // res = "Error";
            //                 break; 
            //                         }

        } catch (Exception e) { 

            e.printStackTrace();
        //En caso de un error en la solicitud, llenaremos res con la exceptión para verificar que sucedió
            res = e.toString();
        } 
    }
}