package resource;

import model.Curso;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
    public void adicionar(Curso curso) {
        if (curso.nome == null || curso.nome.isEmpty()) {
            return Response.status(400).entity("Curso não pode ser nulo ou vazio").build();
        }
        if (curso.find("nome", curso.nome).firstResult() != null) {
            return Response.status(409).entity("Curso já cadastrado").build();
        }
        curso.persist();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed("COORDENADOR")
    public void atualizar(@PathParam("id") Long id, Curso atualizado) {
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
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed("COORDENADOR")
    public void deletar(@PathParam("id") Long id) {
        Curso.deleteById(id);
    }
}
