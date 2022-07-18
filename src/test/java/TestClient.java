import io.restassured.http.ContentType;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class TestClient {

    String baseUrl = "http://localhost:8080/";
    String endpointClient = "cliente/";
    String endpointClientId = "10001/";
    String endpointDeleteAll = "apagaTodos";
    String expectedNull = "{}";
    String expectedDelete ="CLIENTE REMOVIDO: { NOME: Brenno, IDADE: 32, ID: 10001 }";
    String newClient = """
            {
              "id": 10001,
              "idade": 32,
              "nome": "Brenno",
              "risco": 0
            }""";
    String expectedClient = "{\"10001\":{\"nome\":\"Brenno\",\"idade\":32,\"id\":10001,\"risco\":0}}";
    String updatedClient = """
            {
              "id": 10001,
              "idade": 31,
              "nome": "Brenno",
              "risco": 0
            }""";
    String expectedUpdated = "{\"10001\":{\"nome\":\"Brenno\",\"idade\":31,\"id\":10001,\"risco\":0}}";

    public void deleteAll () {
        given()
                .contentType(ContentType.JSON)
                .when().delete(baseUrl+endpointClient+endpointDeleteAll)
                .then().statusCode(200).body(new IsEqual<>(expectedNull));
    }

    @Test
    @DisplayName("When to get all clients without a client register, then the result should be empty.")
    public void getAllClients () {

        deleteAll();

        given()
                .contentType(ContentType.JSON)
                .when().get(baseUrl)
                .then().statusCode(200).body(new IsEqual<>(expectedNull));

    }

    @Test
    @DisplayName("When to register a new client, then a new client should be created.")
    public void createNewClient () {
        given()
                .contentType(ContentType.JSON).body(newClient)
                .when().post(baseUrl+endpointClient)
                .then().statusCode(201).body(containsString(expectedClient));
    }

    @Test
    @DisplayName("When to update a client information, then client data should be updated.")
    public void updateClientData () {

        createNewClient();

        given()
                .contentType(ContentType.JSON).body(updatedClient)
                .when().put(baseUrl+endpointClient)
                .then().statusCode(200).body(containsString(expectedUpdated));

    }

    @Test
    @DisplayName("When to delete a client information, all client data of that ID shoud be erased.")
    public void deleteClient () {

        createNewClient();

        given()
                .contentType(ContentType.JSON)
                .when().delete(baseUrl+endpointClient+endpointClientId)
                .then().statusCode(200).body(new IsEqual<>(expectedDelete));

    }

}
