package resource;

import model.Curso;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/matriz")
@Produces(MediaType.APPLICATION_JSON)
public class MatrizResource {
    @GET
    @RolesAllowed({"ALUNO", "PROFESSOR"})
    public List<Curso> visualizarMatriz() {
        return Curso.listAll();
    }
}
