// src/app/models/usuario.model.ts
export interface Usuario {
  id: string;
  firstName: string;
  lastName: string;
  username: string;
  email: string;
  password: string;
  realmRoles: 'ADMIN' | 'COORDENADOR' | 'PROFESSOR' | 'ALUNO';
}
