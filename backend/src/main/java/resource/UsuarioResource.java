package resource;

import model.Usuario;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @GET
    @RolesAllowed("ADMIN")
    public List<Usuario> listar() {
        return Usuario.listAll();
    }

    @POST
    @Transactional
    @RolesAllowed("ADMIN")
    public Response adicionar(Usuario usuario) {
        if (usuario.email == null || usuario.email.isEmpty()) {
            return Response.status(400).entity("Email não pode ser nulo ou vazio").build();
        }
        if (Usuario.find("email", usuario.email).firstResult() != null) {
            return Response.status(409).entity("Email já cadastrado").build();
        }
        if (usuario.senha == null || usuario.senha.isEmpty()) {
            return Response.status(400).entity("Senha não pode ser nulo ou vazio").build();
        }
        if (usuario.perfil == null || usuario.perfil.isEmpty()) {
            return Response.status(400).entity("Perfil não pode ser nulo ou vazio").build();
        }
        if (usuario.username == null || usuario.username.isEmpty()) {
            return Response.status(400).entity("Username não pode ser nulo ou vazio").build();
        }
        if (Usuario.find("username", usuario.email).firstResult() != null) {
            return Response.status(409).entity("Username já cadastrado").build();
        }
        if (usuario.nome == null || usuario.nome.isEmpty()) {
            return Response.status(400).entity("Nome não pode ser nulo ou vazio").build();
        }
        usuario.persist();

        try {
            String token = obterTokenAdminKeycloak();

            String senha = usuario.senha;

            String json = String.format(
                "{\"username\":\"%s\",\"enabled\":true,\"email\":\"%s\",\"credentials\":[{\"type\":\"password\",\"value\":\"%s\",\"temporary\":false}],\"realmRoles\":[\"%s\"]}",
                usuario.email, usuario.email, senha, usuario.perfil.name()
            );

            URL url = new URL("http://keycloak:8080/admin/realms/desafio/users");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + token);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
            }

            int status = conn.getResponseCode();
            if (status == 201) {
                return Response.status(201).entity("{\"email\":\"" + senha + "\", \"senha\":\"" + senha + "\"}").build();
            } else {
                return Response.status(status).entity("Erro ao criar usuário no Keycloak").build();
            }
        } catch (Exception e) {
            return Response.status(500).entity("Erro ao criar usuário no Keycloak: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed("ADMIN")
    public void atualizar(@PathParam("id") Long id, Usuario usuarioAtualizado) {
        Usuario usuario = Usuario.findById(id);
        if (usuario != null) {
            usuario.nome = usuarioAtualizado.nome;
            usuario.email = usuarioAtualizado.email;
            usuario.perfil = usuarioAtualizado.perfil;
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed("ADMIN")
    public void deletar(@PathParam("id") Long id) {
        Usuario.deleteById(id);
    }

    private String obterTokenAdminKeycloak() throws Exception {
        String urlParameters = "client_id=admin-cli&username=admin&password=admin&grant_type=password";
        URL url = new URL("http://keycloak:8080/realms/master/protocol/openid-connect/token");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        try (OutputStream os = conn.getOutputStream()) {
            os.write(urlParameters.getBytes());
        }
        java.util.Scanner in = new java.util.Scanner(conn.getInputStream());
        StringBuilder sb = new StringBuilder();
        while (in.hasNext()) sb.append(in.nextLine());
        in.close();
        String response = sb.toString();
        // Extraia o access_token do JSON de resposta
        String token = response.split("\"access_token\":\"")[1].split("\"")[0];
        return token;
    }
}
