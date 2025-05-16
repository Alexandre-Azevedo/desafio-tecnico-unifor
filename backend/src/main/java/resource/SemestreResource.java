package resource;

import model.Semestre;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/semestre")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SemestreResource {
    @GET
    @RolesAllowed({"COORDENADOR"})
    public List<Semestre> listar() {
        return Semestre.listAll();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"COORDENADOR"})
    public Semestre buscar(@PathParam("id") Long id) {
        return Semestre.findById(id);
    }

    @POST
    @Transactional
    @RolesAllowed({"COORDENADOR"})
    public void adicionar(Semestre semestre) {
        semestre.persist();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed({"COORDENADOR"})
    public void atualizar(@PathParam("id") Long id, Semestre atualizado) {
        Semestre semestre = Semestre.findById(id);
        if (semestre != null) {
            semestre.numero = atualizado.numero;
            semestre.curso = atualizado.curso;
        } else {
            throw new NotFoundException("Semestre não encontrado.");
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed({"COORDENADOR"})
    public void deletar(@PathParam("id") Long id) {
        boolean deleted = Semestre.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("Semestre não encontrado.");
        }
    }
}
