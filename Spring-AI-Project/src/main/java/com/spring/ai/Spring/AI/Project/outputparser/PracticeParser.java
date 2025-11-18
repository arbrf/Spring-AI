package com.spring.ai.Spring.AI.Project.outputparser;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PracticeParser {

   String raw = "Hello from Public API!";
   Pair pair=new Pair("India",List.of("Chennai","Madurai"));

   public String returnRawJson(){
      return """
               {
                 "countryName": "India",
                 "cities": [
                   "Hyderabad",
                   "Mumbai",
                   "Delhi"
                 ]
               }
               """;
   }
   public String returnMapValues(){

      return """
            {
              "numbers": [1,2,3,4,5]
            }
            """;
   }


   public String returnListOfValues(){
      return  """
            flavor,
            Chocolate,
            Vanilla,
            Strawberry
            """;
   }


}
