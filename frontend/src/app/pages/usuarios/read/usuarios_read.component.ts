import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { UsuarioService } from '../services/usuario.service';
import { Usuario } from '../models/usuario.model';

@Component({
  selector: 'app-usuarios-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
    <div class="list-page">
      <div class="list-container">
        <h2>Lista de Usuários</h2>
        <input
          type="text"
          placeholder="Buscar por nome, username ou email"
          [(ngModel)]="filtro"
          (input)="filtrarUsuarios()"
        />
        <ul *ngIf="usuariosFiltrados.length > 0">
          <li *ngFor="let usuario of usuariosFiltrados">
            <a [routerLink]="['/usuarios/update', usuario.id]">{{ usuario.firstName }} {{ usuario.lastName }}</a><br>
            Username: {{ usuario.username }}<br>
            Email: {{ usuario.email }}<br>
            Perfil: {{ usuario.realmRoles }}
            <br>
          <button (click)="deletarUsuario(usuario.username)">🗑️ Deletar</button>
          </li>
        </ul>
        <p *ngIf="usuariosFiltrados.length === 0 && filtro">Nenhum usuário encontrado.</p>
        <p *ngIf="erro" class="erro">{{ erro }}</p>
      </div>
    </div>
  `,
  styles: [`
    .list-page {
      height: 100vh;
      background-color: #f6f8fb;
      display: flex;
      justify-content: center;
      align-items: flex-start;
      padding-top: 40px;
    }
    .list-container {
      background-color: #fff;
      padding: 2rem 3rem;
      border-radius: 15px;
      box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
      width: 500px;
    }
    h2 {
      text-align: center;
      margin-bottom: 1.5rem;
      font-weight: 600;
    }
    input {
      width: 100%;
      padding: 0.5rem;
      border: 1px solid #ccc;
      border-radius: 6px;
      margin-bottom: 1rem;
    }
    ul {
      list-style: none;
      padding: 0;
    }
    li {
      background: #f4f6f9;
      margin-bottom: 1rem;
      padding: 1rem;
      border-radius: 8px;
    }
    .erro {
      color: red;
      margin-top: 1rem;
      text-align: center;
    }
  `]
})
export class UsuariosListComponent implements OnInit {
  usuarios: Usuario[] = [];
  usuariosFiltrados: Usuario[] = [];
  filtro = '';
  erro = '';
  mensagem = '';

  constructor(private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    this.buscarUsuarios();
  }

  buscarUsuarios(): void {
    this.usuarioService.listar().subscribe({
      next: (res) => {
        if (typeof res === 'string') {
          console.log('Resposta do backend:', JSON.parse(res));
          this.usuarios = JSON.parse(res);
        } else {
          console.log('Resposta do backend:', res);
          this.usuarios = res;
        }
        this.filtrarUsuarios();
        this.erro = '';
      },
      error: (err) => {
        const backendMsg = err.error?.message || err.error || err.message;
        this.erro = 'Erro ao buscar usuários: ' + backendMsg;
      }
    });
  }

  filtrarUsuarios(): void {
    const filtroLower = this.filtro.toLowerCase();
    this.usuariosFiltrados = this.usuarios.filter(u =>
      u.firstName?.toLowerCase().includes(filtroLower) ||
      u.lastName?.toLowerCase().includes(filtroLower) ||
      u.username?.toLowerCase().includes(filtroLower) ||
      u.email?.toLowerCase().includes(filtroLower)
    );
  }
  deletarUsuario(username: string): void {
    if (window.confirm('Tem certeza que deseja deletar este usuário?')) {
      this.usuarioService.deletar(username).subscribe({
        next: () => {
          this.mensagem = 'Usuário deletado com sucesso!';
          this.buscarUsuarios();
        },
        error: (err) => {
          const backendMsg = err.error?.message || err.error || err.message;
          this.erro = 'Erro ao deletar usuário: ' + backendMsg;
        }
      });
    }
  }
}
