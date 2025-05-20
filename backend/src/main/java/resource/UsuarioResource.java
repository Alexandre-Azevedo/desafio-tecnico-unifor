package resource;

import model.Usuario;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.core.Response;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    private static final String KEYCLOAK_URL = "http://keycloak:8080/admin/realms/desafio/users";
    private static final String KEYCLOAK_URL_ROLES = "http://keycloak:8080/admin/realms/desafio/roles";

    @GET
    @RolesAllowed("ADMIN")
    public Response listar() throws IOException {
        String token = obterTokenAdminKeycloak();

        URL url = new URL(KEYCLOAK_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + token);

        StringBuilder sb = new StringBuilder();
        try (Scanner in = new Scanner(conn.getInputStream())) {
            while (in.hasNext()) sb.append(in.nextLine());
        }

        JSONArray usuarios = new JSONArray(sb.toString());
        for (int i = 0; i < usuarios.length(); i++) {
            JSONObject usuario = usuarios.getJSONObject(i);
            String userId = usuario.getString("id");

            URL roleUrl = new URL("http://keycloak:8080/admin/realms/desafio/users/" + userId + "/role-mappings/realm");
            HttpURLConnection roleConn = (HttpURLConnection) roleUrl.openConnection();
            roleConn.setRequestMethod("GET");
            roleConn.setRequestProperty("Authorization", "Bearer " + token);

            StringBuilder roleSb = new StringBuilder();
            try (Scanner inRole = new Scanner(roleConn.getInputStream())) {
                while (inRole.hasNext()) roleSb.append(inRole.nextLine());
            }

            JSONArray roles = new JSONArray(roleSb.toString());

            JSONArray nomesRoles = new JSONArray();
            for (int j = 0; j < roles.length(); j++) {
                nomesRoles.put(roles.getJSONObject(j).getString("name"));
            }

            usuario.put("realmRoles", nomesRoles);
        }

        return Response.ok(usuarios.toString()).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response buscarPorId(@PathParam("id") String id) throws IOException {
        String token = obterTokenAdminKeycloak();

        URL url = new URL(KEYCLOAK_URL + "/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + token);

        if (conn.getResponseCode() != 200) {
            return Response.status(conn.getResponseCode())
                    .entity("Usuário não encontrado ou erro ao consultar Keycloak").build();
        }

        StringBuilder sb = new StringBuilder();
        try (Scanner in = new Scanner(conn.getInputStream())) {
            while (in.hasNext()) sb.append(in.nextLine());
        }

        JSONObject usuario = new JSONObject(sb.toString());

        URL roleUrl = new URL(KEYCLOAK_URL + "/" + id + "/role-mappings/realm");
        HttpURLConnection roleConn = (HttpURLConnection) roleUrl.openConnection();
        roleConn.setRequestMethod("GET");
        roleConn.setRequestProperty("Authorization", "Bearer " + token);

        StringBuilder roleSb = new StringBuilder();
        try (Scanner inRole = new Scanner(roleConn.getInputStream())) {
            while (inRole.hasNext()) roleSb.append(inRole.nextLine());
        }

        JSONArray roles = new JSONArray(roleSb.toString());

        JSONArray nomesRoles = new JSONArray();
        for (int j = 0; j < roles.length(); j++) {
            nomesRoles.put(roles.getJSONObject(j).getString("name"));
        }

        usuario.put("realmRoles", nomesRoles);

        return Response.ok(usuario.toString()).build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response adicionar(Usuario usuario) throws IOException {
        String token = obterTokenAdminKeycloak();
        System.out.println(usuario.password);
        String payload = gerarPayloadKeycloak(usuario);

        URL url = new URL(KEYCLOAK_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(payload.getBytes());
        }

        int status = conn.getResponseCode();
        if (status == 201) {
            String location = conn.getHeaderField("Location");
            String userId = location.substring(location.lastIndexOf('/') + 1);
            String roleName = usuario.realmRoles.name();
            URL roleUrl = new URL(KEYCLOAK_URL_ROLES + "/" + roleName);
            HttpURLConnection roleConn = (HttpURLConnection) roleUrl.openConnection();
            roleConn.setRequestMethod("GET");
            roleConn.setRequestProperty("Authorization", "Bearer " + token);

            String roleJson;
            try (Scanner in = new Scanner(roleConn.getInputStream())) {
                StringBuilder sb = new StringBuilder();
                while (in.hasNext()) sb.append(in.nextLine());
                roleJson = sb.toString();
            }

            URL mappingUrl = new URL(KEYCLOAK_URL + "/" + userId + "/role-mappings/realm");
            HttpURLConnection mappingConn = (HttpURLConnection) mappingUrl.openConnection();
            mappingConn.setDoOutput(true);
            mappingConn.setRequestMethod("POST");
            mappingConn.setRequestProperty("Authorization", "Bearer " + token);
            mappingConn.setRequestProperty("Content-Type", "application/json");
            String mappingPayload = "[" + roleJson + "]";
            try (OutputStream os = mappingConn.getOutputStream()) {
                os.write(mappingPayload.getBytes());
            }

            int mappingStatus = mappingConn.getResponseCode();
            if (mappingStatus == 204) {
                return Response.status(201).entity("{\"username\":\"" + usuario.username + "\", \"senha\":\"" + usuario.password + "\"}").build();
            } else {
                return Response.status(201).entity("{\"username\":\"" + usuario.username + "\", \"senha\":\"" + usuario.password + "\", \"message\":\"Usuário criado, mas não foi possível atribuir a role.\"}").build();
            }
        } else {
            return Response.status(status).entity("{\"message\":\"Erro ao criar usuário!\"}").build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response atualizar(@PathParam("id") String id, Usuario usuario) throws IOException {
        deletar(usuario.username);
        return adicionar(usuario);
    }


    @DELETE
    @Path("/{username}")
    @RolesAllowed("ADMIN")
    public Response deletar(@PathParam("username") String username) throws IOException {
        String token = obterTokenAdminKeycloak();
        String id = buscarUsuarioKeycloakPorUsername(username, token);
        if (id == null) return Response.status(404).entity("Usuário não encontrado").build();

        URL url = new URL(KEYCLOAK_URL + "/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Authorization", "Bearer " + token);

        int status = conn.getResponseCode();
        return status == 204
                ? Response.noContent().build()
                : Response.status(status).entity("Erro ao deletar usuário").build();
    }


    private String buscarUsuarioKeycloakPorUsername(String username, String token) throws IOException {
        URL url = new URL(KEYCLOAK_URL + "?username=" + username);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + token);

        try (Scanner in = new Scanner(conn.getInputStream())) {
            StringBuilder sb = new StringBuilder();
            while (in.hasNext()) sb.append(in.nextLine());
            String response = sb.toString();
            if (!response.equals("[]")) {
                return response.split("\"id\":\"")[1].split("\"")[0];
            }
        }
        return null;
    }

    private String gerarPayloadKeycloak(Usuario usuario) {
        return String.format(
                "{\"firstName\":\"%s\", \"lastName\":\"%s\", \"enabled\":true, \"emailVerified\":true," +
                        "\"username\":\"%s\", \"email\":\"%s\", " +
                        "\"credentials\":[{\"type\":\"password\",\"value\":\"%s\",\"temporary\":false}]," +
                        "\"realmRoles\":[\"%s\"]}",
                usuario.firstName, usuario.lastName,
                usuario.username, usuario.email,
                usuario.password, usuario.realmRoles.name()
        );
    }

    private String obterTokenAdminKeycloak() throws IOException {
        String params = "client_id=backend-service" +
                "&client_secret=super-secret-value" +
                "&grant_type=client_credentials";

        URL url = new URL("http://keycloak:8080/realms/desafio/protocol/openid-connect/token");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(params.getBytes());
        }

        try (Scanner in = new Scanner(conn.getInputStream())) {
            StringBuilder sb = new StringBuilder();
            while (in.hasNext()) sb.append(in.nextLine());
            return new org.json.JSONObject(sb.toString()).getString("access_token");
        }
    }
}
