import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="login-page">
      <div class="form-container">
        <h2>Menu Principal</h2>
        <p>Escolha uma funcionalidade:</p>
        <ul class="menu-list">
          <li><a routerLink="/cursos">📘 Cursos</a></li>
          <li><a routerLink="/semestres">📆 Semestres</a></li>
          <li><a routerLink="/disciplinas">📚 Disciplinas</a></li>
          <li><a routerLink="/matriz">🧩 Matriz Curricular</a></li>
          <li><a routerLink="/usuarios">👤 Usuários</a></li>
        </ul>
      </div>
    </div>
  `,
  styles: [`
    .login-page {
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
      text-align: center;
      width: 400px;
    }

    .form-container h2 {
      margin-bottom: 0.5rem;
      font-weight: 600;
    }

    .menu-list {
      list-style: none;
      padding: 0;
      margin-top: 1.5rem;
    }

    .menu-list li {
      margin: 1rem 0;
    }

    .menu-list a {
      text-decoration: none;
      color: #0d6efd;
      font-weight: 500;
      font-size: 1.1rem;
    }

    .menu-list a:hover {
      text-decoration: underline;
    }
  `]
})
export class DashboardComponent {}
