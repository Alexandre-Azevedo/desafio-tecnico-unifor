package resource;

import model.Semestre;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    public Response adicionar(Semestre semestre) {
        if (semestre.numero == null) {
            return Response.status(400).entity("Semestre não pode ser nulo ou vazio").build();
        }
        if (semestre.find("numero", semestre.numero).firstResult() != null) {
            return Response.status(409).entity("Semestre já cadastrado").build();
        }
        semestre.persist();
        return Response.status(201).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed({"COORDENADOR"})
    public Response atualizar(@PathParam("id") Long id, Semestre atualizado) {
        Semestre semestre = Semestre.findById(id);
        if (semestre != null) {
            semestre.numero = atualizado.numero;
            semestre.curso = atualizado.curso;
            semestre.persist();
            return Response.noContent().build();
        } else {
            return Response.status(404).entity("Semestre não encontrado").build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed({"COORDENADOR"})
    public Response deletar(@PathParam("id") Long id) {
        boolean deleted = Semestre.deleteById(id);
        if (!deleted) {
            return Response.status(404).entity("Semestre não encontrado").build();
        } else {
            return Response.noContent().build();
        }
    }
}
