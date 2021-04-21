package com.my_pls.demo;


import spark.ModelAndView;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static spark.Spark.*;
public class App {

    private static Map<String,String> extractFields(String body){
        try{
            Map<String,String> map = new HashMap<>(0);
            String[] pairs =  body.split("&");
            for (String pair : pairs){
                String[] keyAndValue = pair.split("=");
                map.put(keyAndValue[0],keyAndValue[1]);
            }

            return map;
        }catch (Exception ex){
            return null;
        }

    }
//    Map<String,String> map = extractFields(request.body());
//        return map;

    public static void main(String[] args){

        port(8080);

        final TemplateEngine engine = new FreeMarkerEngine();
        internalServerError("<html><body>Something went wrong!</body></html>");
        staticFileLocation("/public"); //So that it has access to the pubic resources(stylesheets, etc.)

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(options);

        post("/sub", ((request, response) -> {

            Map<String,String> map = extractFields(request.body());
            return map;
        }
        ));

        // Setting any route (or filter) in Spark triggers initialization of the embedded Jetty web server.
        get("/", (request, response) -> {return new ModelAndView(new HashMap<>(),"sub.ftl");},engine);
        get("/hello/:name",(request, response) -> {
            String name = request.params(":name");
            Map<String,Object> map = new HashMap<>();
            map.put("title",name);
            return new ModelAndView(map,"home.ftl");

        },engine);

        get("/login",(request, response) -> {
            Map<String,Object> map = new HashMap<>();
            map.put("pageType","Login");
            map.put("styleVal", "max-width:32rem; margin-top:12%; left:30%");
            return new ModelAndView(map,"login.ftl");

        },engine);

        get("/register",(request, response) -> {
            Map<String,Object> map = new HashMap<>();
            map.put("pageType","Register");
            map.put("styleVal", "max-width:32rem; margin-top:5%; left:30%");
//            map.put("toast",(""))
            return new ModelAndView(map,"login.ftl");

        },engine);

        post("/verify-register",(request, response) -> {
            Map<String,String> map = extractFields(request.body());

            return new ModelAndView(map,"verifyRegister.ftl");

        },engine);

        get("/first/*/last/*",(request, response) -> {

            String firstName = request.splat()[0];
            String lastName = request.splat()[1];
            Map<String,Object> map = new HashMap<>();
            map.put("title",String.format("%s %s",firstName,lastName));
            return new ModelAndView(map,"home.ftl");

        },engine);



        Pokemon charmander  = new Pokemon();
        charmander.name = "Charry";
        charmander.type = "fire";

        Pokemon rattata = new Pokemon();
        rattata.name = "ratbi";
        rattata.type = "grass";

        get("/list",(request, response) -> {

            List<Pokemon> pokeList = new ArrayList<>(4);
            pokeList.add(charmander);
            pokeList.add(rattata);

            Map<String,Object> map = new HashMap<>();
            map.put("pokemon",pokeList);
            return new ModelAndView(map,"example.ftl");

        },engine);

        path("/user",()->{
            get("/",(req,res)-> req.session().attribute("name"));
            get("/update/:name",(req,res)->{
                String name = req.params(":name");
                req.session().attribute("name",name);
                return String.format("Value updated with %s",name);
            });

        });

    }

    public static class Pokemon{
        String name;
        String type;

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
    }

}
