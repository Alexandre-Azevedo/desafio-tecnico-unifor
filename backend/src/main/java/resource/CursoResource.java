package resource;

import model.Curso;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/cursos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CursoResource {

    @GET
    @RolesAllowed({"COORDENADOR"})
    public List<Curso> listar() {
        return Curso.listAll();
    }

    @POST
    @Transactional
    @RolesAllowed("COORDENADOR")
    public Response adicionar(Curso curso) {
        if (curso.nome == null || curso.nome.isEmpty()) {
            return Response.status(400).entity("Curso não pode ser nulo ou vazio").build();
        }
        if (curso.find("nome", curso.nome).firstResult() != null) {
            return Response.status(409).entity("Curso já cadastrado").build();
        }
        curso.persist();
        return Response.status(201).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed("COORDENADOR")
    public Response atualizar(@PathParam("id") Long id, Curso atualizado) {
        Curso curso = Curso.findById(id);
        if (curso != null) {
            if (curso.nome == null || curso.nome.isEmpty()) {
                return Response.status(400).entity("Curso não pode ser nulo ou vazio").build();
            }
            if (curso.find("nome", curso.nome).firstResult() != null) {
                return Response.status(409).entity("Curso já cadastrado").build();
            }
            curso.nome = atualizado.nome;
            curso.persist();
            return Response.noContent().build();
        } else {
            return Response.status(404).entity("Curso não encontrado").build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed("COORDENADOR")
    public Response deletar(@PathParam("id") Long id) {
        boolean deleted = Curso.deleteById(id);
        if (!deleted) {
            return Response.status(404).entity("Curso não encontrado").build();
        } else {
            return Response.noContent().build();
        }
    }
}
