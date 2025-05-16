package resource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Path("/token")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class TokenResource {

    @POST
    public Response getToken(
            @FormParam("client_id") String clientId,
            @FormParam("client_secret") String clientSecret,
            @FormParam("username") String username,
            @FormParam("password") String password
    ) {
        try {
            String urlParameters = "client_id=" + clientId +
                    "&client_secret=" + clientSecret +
                    "&grant_type=password" +
                    "&username=" + username +
                    "&password=" + password;

            URL url = new URL("http://keycloak:8080/realms/desafio/protocol/openid-connect/token");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            try (OutputStream os = conn.getOutputStream()) {
                os.write(urlParameters.getBytes());
            }
            Scanner in = new Scanner(conn.getInputStream());
            StringBuilder sb = new StringBuilder();
            while (in.hasNext()) sb.append(in.nextLine());
            in.close();
            return Response.ok(sb.toString()).build();
        } catch (Exception e) {
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
}