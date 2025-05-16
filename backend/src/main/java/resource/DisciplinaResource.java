package resource;

import model.Disciplina;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
    public void adicionar(Disciplina disciplina) {
        if (disciplina.nome == null || disciplina.nome.isEmpty()) {
            return Response.status(400).entity("Disciplina não pode ser nulo ou vazio").build();
        }
        if (disciplina.find("nome", disciplina.nome).firstResult() != null) {
            return Response.status(409).entity("Disciplina já cadastrado").build();
        }
        disciplina.persist();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed({"COORDENADOR"})
    public void atualizar(@PathParam("id") Long id, Disciplina atualizada) {
        Disciplina disciplina = Disciplina.findById(id);
        if (disciplina != null) {
            if (disciplina.nome == null || disciplina.nome.isEmpty()) {
                return Response.status(400).entity("Disciplina não pode ser nulo ou vazio").build();
            }
            if (disciplina.find("nome", disciplina.nome).firstResult() != null) {
                return Response.status(409).entity("Disciplina já cadastrado").build();
            }
            disciplina.nome = atualizada.nome;
            disciplina.semestre = atualizada.semestre;
        } else {
            throw new NotFoundException("Disciplina não encontrada.");
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed({"COORDENADOR"})
    public void deletar(@PathParam("id") Long id) {
        boolean deleted = Disciplina.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("Disciplina não encontrada.");
        }
    }
}
