package net.gymtij;

import com.google.gson.Gson;
import java.util.ArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class RecuperaHuellas {

     public static ArrayList<Huella> recuperaHuellas() {
    
        ConfigReader cr = new ConfigReader();
        String URL = "http://" + cr.getServerIp() + ":5000/api/";
      
        try {
        //Creamos el cliente de conexión al API Restful
            Client client = ClientBuilder.newClient();

        //Creamos el target lo cuál es nuestra URL junto con el nombre del método a llamar
            WebTarget target = client.target(URL + "huellas");

        //Creamos nuestra solicitud que realizará el request
            Invocation.Builder solicitud = target.request().header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MTAsInVzdWFyaW9JZCI6MSwiaXAiOiI6OmZmZmY6MTkyLjE2OC4wLjEzMCIsImxvZ2luRGF0ZSI6IjIwMjMtMDUtMTFUMDQ6MTg6NDcuNjc5WiIsImxhc3RDb25uZWN0aW9uIjoiMjAyMy0wNS0xMVQwNDoxODo0Ny42NzlaIiwidG9rZW4iOiJlNDQzZThmNS1kYWYzLTQ5MmEtYTgzMC1mY2MyMzNhYTE3NzQiLCJ1cGRhdGVkQXQiOiIyMDIzLTA1LTExVDA0OjE4OjQ3LjY3OVoiLCJjcmVhdGVkQXQiOiIyMDIzLTA1LTExVDA0OjE4OjQ3LjY3OVoifQ.NsV3EN31yJAyF2wA8TqoJlXQkvdIr3hUt0DI-r8yU_k");

        //Creamos y llenamos nuestro objeto BaseReq con los datos que solicita el API
            BaseReq req = new BaseReq();
        

        //Convertimos el objeto req a un json
            Gson gson = new Gson();
            String jsonString = gson.toJson(req);

           
        //Enviamos nuestro json vía post al API Restful
            Response post = solicitud.get();

           
            String resJson = post.readEntity(String.class);
            // System.out.println("json "+ resJson);
            // System.out.println("RES "+resJson);


        //Recibimos la respuesta y la leemos en una clase de tipo String, en caso de que el json sea tipo json y no string, debemos usar la clase de tipo JsonObject.class en lugar de String.class
            HuellasResponse huellasResponse = gson.fromJson(resJson, HuellasResponse.class);


        //Imprimimos el status de la solicitud
            System.out.println("Estatus: " + post.getStatus());

            return huellasResponse.huellas;

        } catch (Exception e) { 

            e.printStackTrace();

        } 
        //Imprimimos la respuesta del API Restful

        return new ArrayList<>();
    }
}