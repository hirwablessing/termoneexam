package rw.ac.rca.termOneExam.controller;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;
import rw.ac.rca.termOneExam.domain.City;
import rw.ac.rca.termOneExam.utils.APIResponse;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CityControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl = "/api/cities";

    @Test
    public void getAll_success() throws JSONException {
        String res = this.restTemplate.getForObject(baseUrl+"/all",String.class);

        JSONAssert.assertEquals("[{id:101},{id:102},{id:103},{id:104}]",res,false);

    }

    @Test
    public void getById_success() throws JSONException {
        ResponseEntity<City> myCity = this.restTemplate.getForEntity(baseUrl+"/id/101",City.class);

        assertTrue(myCity.getStatusCode().is2xxSuccessful());
        assertEquals("Kigali",myCity.getBody().getName());
        assertEquals(24,myCity.getBody().getWeather());

    }

    @Test
    public void getById_failure() throws JSONException {
        ResponseEntity<APIResponse> res = this.restTemplate.getForEntity(baseUrl+"/id/600",APIResponse.class);

        assertTrue(res.getStatusCodeValue()==404);
        assertFalse(res.getBody().isStatus());
        assertEquals("City can't be found by id 600",res.getBody().getMessage());

    }

    @Test
    public void addCity_success() throws JSONException {
        City requestBody = new City(105,"Cairo",70,12);
        ResponseEntity<City> myCity = this.restTemplate.postForEntity(baseUrl+"/add",requestBody, City.class);

        assertTrue(myCity.getStatusCode().is2xxSuccessful());
        assertEquals("Cairo",myCity.getBody().getName());

    }

    @Test
    public void addCity_failure() throws JSONException {
        City requestBody = new City(101,"Kigali",70,12);
        ResponseEntity<APIResponse> res = this.restTemplate.postForEntity(baseUrl+"/add",requestBody,APIResponse.class);

        assertTrue(res.getStatusCodeValue()==400);
        assertFalse(res.getBody().isStatus());
        assertEquals("City name Kigali is there already",res.getBody().getMessage());

    }

}
