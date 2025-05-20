import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { CursosComponent } from './pages/cursos/cursos.component';
import { SemestresComponent } from './pages/semestres/semestres.component';
import { DisciplinasComponent } from './pages/disciplinas/disciplinas.component';
import { UsuarioComponent } from './pages/usuarios/usuarios.component';
import { UsuarioFormComponent } from './pages/usuarios/create/usuarios_create.component';
import { UsuariosListComponent } from './pages/usuarios/read/usuarios_read.component';
import { UsuarioUpdateComponent } from './pages/usuarios/update/usuarios_update.component';
import { MatrizComponent } from './pages/matriz/matriz.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { AuthGuard } from './guard/auth.guard';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN', 'COORDENADOR', 'PROFESSOR', 'ALUNO'] }
  },
  {
    path: 'cursos',
    component: CursosComponent,
    canActivate: [AuthGuard],
    data: { roles: ['COORDENADOR'] }
  },
  {
    path: 'semestres',
    component: SemestresComponent,
    canActivate: [AuthGuard],
    data: { roles: ['COORDENADOR'] }
  },
  { path: 'disciplinas',
    component: DisciplinasComponent,
    canActivate: [AuthGuard],
    data: { roles: ['COORDENADOR'] }
  },
  { path: 'usuarios',
    component: UsuarioComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] }
  },
  { path: 'usuarios/create', component: UsuarioFormComponent, canActivate: [AuthGuard], data: { roles: ['ADMIN'] } },
  { path: 'usuarios/read', component: UsuariosListComponent, canActivate: [AuthGuard], data: { roles: ['ADMIN'] } },
  { path: 'usuarios/update/:id', component: UsuarioUpdateComponent, canActivate: [AuthGuard], data: { roles: ['ADMIN'] } },
  { path: 'matriz',
    component: MatrizComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ALUNO', 'PROFESSOR'] }
  }
];
