package resource;

import model.Disciplina;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/disciplinas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DisciplinaResource {
    @GET
    @RolesAllowed({"COORDENADOR"})
    public List<Disciplina> listar() {
        return Disciplina.listAll();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"COORDENADOR"})
    public Disciplina buscar(@PathParam("id") Long id) {
        return Disciplina.findById(id);
    }

    @POST
    @Transactional
    @RolesAllowed({"COORDENADOR"})
    public Response adicionar(Disciplina disciplina) {
        if (disciplina.nome == null || disciplina.nome.isEmpty()) {
            return Response.status(400).entity("Disciplina não pode ser nula ou vazia").build();
        }
        if (disciplina.find("nome", disciplina.nome).firstResult() != null) {
            return Response.status(409).entity("Disciplina já cadastrada").build();
        }
        disciplina.persist();
        return Response.status(201).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed({"COORDENADOR"})
    public Response atualizar(@PathParam("id") Long id, Disciplina atualizada) {
        Disciplina disciplina = Disciplina.findById(id);
        if (disciplina != null) {
            if (disciplina.nome == null || disciplina.nome.isEmpty()) {
                return Response.status(400).entity("Disciplina não pode ser nula ou vazia").build();
            }
            if (disciplina.find("nome", disciplina.nome).firstResult() != null) {
                return Response.status(409).entity("Disciplina já cadastrada").build();
            }
            disciplina.nome = atualizada.nome;
            disciplina.semestre = atualizada.semestre;
            disciplina.persist();
            return Response.noContent().build();
        } else {
            return Response.status(404).entity("Disciplina não encontrada").build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed({"COORDENADOR"})
    public Response deletar(@PathParam("id") Long id) {
        boolean deleted = Disciplina.deleteById(id);
        if (!deleted) {
            return Response.status(404).entity("Disciplina não encontrada").build();
        } else {
            return Response.noContent().build();
        }
    }
}
