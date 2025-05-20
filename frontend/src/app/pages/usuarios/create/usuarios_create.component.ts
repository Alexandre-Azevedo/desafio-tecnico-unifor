import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UsuarioService } from '../services/usuario.service';
import { Usuario } from '../models/usuario.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-usuario-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="form-page">
      <div class="form-container">
        <h2>Criar Usuário</h2>

        <form (ngSubmit)="criarUsuario()">
          <div class="form-group">
            <label for="nome">Nome</label>
            <input type="text" id="nome" [(ngModel)]="nome" name="nome" required>
          </div>

          <div class="form-group">
            <label for="nome">Sobrenome</label>
            <input type="text" id="sobrenome" [(ngModel)]="sobrenome" name="sobrenome" required>
          </div>

          <div class="form-group">
            <label for="username">Username</label>
            <input type="text" id="username" [(ngModel)]="username" name="username" required>
          </div>

          <div class="form-group">
            <label for="email">E-mail</label>
            <input type="email" id="email" [(ngModel)]="email" name="email" required>
          </div>

          <div class="form-group">
            <label for="senha">Senha</label>
            <input type="password" id="senha" [(ngModel)]="senha" name="senha" required>
          </div>

          <div class="form-group">
            <label for="perfil">Papel</label>
            <select id="perfil" [(ngModel)]="perfil" name="perfil" required>
              <option value="ADMIN">Admin</option>
              <option value="COORDENADOR">Coordenador</option>
              <option value="PROFESSOR">Professor</option>
              <option value="ALUNO">Aluno</option>
            </select>
          </div>

          <button type="submit">➕ Criar Usuário</button>
        </form>

        <p class="mensagem" *ngIf="mensagem">{{ mensagem }}</p>
        <p class="erro" *ngIf="erro">{{ erro }}</p>
      </div>
    </div>
  `,
  styles: [`
    .form-page {
      height: 100vh;
      background-color: #f6f8fb;
      display: flex;
      justify-content: center;
      align-items: center;
    }

    .form-container {
      background-color: #fff;
      padding: 2rem 3rem;
      border-radius: 15px;
      box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
      width: 400px;
    }

    h2 {
      text-align: center;
      margin-bottom: 1.5rem;
      font-weight: 600;
    }

    .form-group {
      margin-bottom: 1rem;
    }

    label {
      display: block;
      margin-bottom: 0.3rem;
      font-weight: 500;
    }

    input, select {
      width: 100%;
      padding: 0.5rem;
      border: 1px solid #ccc;
      border-radius: 6px;
    }

    button {
      width: 100%;
      background-color: #0d6efd;
      color: #fff;
      padding: 0.6rem;
      font-weight: 600;
      border: none;
      border-radius: 6px;
      cursor: pointer;
      margin-top: 1rem;
    }

    button:hover {
      background-color: #0b5ed7;
    }

    .mensagem {
      color: green;
      margin-top: 1rem;
      text-align: center;
    }

    .erro {
      color: red;
      margin-top: 1rem;
      text-align: center;
    }
  `]
})
export class UsuarioFormComponent {
  nome = '';
  sobrenome = '';
  username = '';
  email = '';
  senha = '';
  perfil: Usuario['realmRoles'] = 'ALUNO';
  mensagem = '';
  erro = '';

  constructor(private usuarioService: UsuarioService, private router: Router) {}

  criarUsuario(): void {
    const novoUsuario: Usuario = {
      id: '',
      firstName: this.nome,
      lastName: this.sobrenome,
      username: this.username,
      email: this.email,
      password: this.senha,
      realmRoles: this.perfil
    };

    this.usuarioService.criar(novoUsuario).subscribe({
      next: (res) => {
        this.mensagem = 'Usuário criado com sucesso!';
        setTimeout(() => window.alert(this.mensagem), 1500);
      },
      error: (err) => {
        const backendMsg = err.error?.message || err.error || err.message;
        this.erro = 'Erro ao criar usuário: ' + backendMsg;
        setTimeout(() => window.alert(this.erro), 1500);
      }
    });
  }
}
