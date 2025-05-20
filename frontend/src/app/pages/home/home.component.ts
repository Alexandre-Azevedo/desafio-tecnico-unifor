import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="login-wrapper">
      <div class="login-card">
        <h2>Entrar</h2>

        <form (ngSubmit)="onLogin()">
          <div class="form-group">
            <label for="email">E-mail</label>
            <input
              id="email"
              type="email"
              [(ngModel)]="email"
              name="email"
              placeholder="Digite seu e-mail"
              required
            />
          </div>

          <div class="form-group">
            <label for="password">Senha</label>
            <input
              id="password"
              type="password"
              [(ngModel)]="password"
              name="password"
              placeholder="Digite sua senha"
              required
            />
          </div>

          <button type="submit">Entrar</button>
        </form>
      </div>
    </div>
  `,
  styles: [`
    * {
      box-sizing: border-box;
    }

    body, html {
      margin: 0;
      padding: 0;
      font-family: 'Segoe UI', sans-serif;
      background-color: #f4f6f9;
    }

    .login-wrapper {
      height: calc(100vh - 60px); /* compensar a navbar */
      display: flex;
      justify-content: center;
      align-items: center;
      background-color: #f4f6f9;
    }

    .login-card {
      background: white;
      padding: 32px;
      border-radius: 12px;
      box-shadow: 0 6px 24px rgba(0, 0, 0, 0.1);
      width: 100%;
      max-width: 400px;
    }

    h2 {
      margin-bottom: 24px;
      text-align: center;
      color: #333;
    }

    .form-group {
      margin-bottom: 18px;
    }

    label {
      display: block;
      margin-bottom: 6px;
      font-weight: 500;
      color: #444;
    }

    input {
      width: 100%;
      padding: 10px 12px;
      border: 1px solid #ccc;
      border-radius: 6px;
      font-size: 15px;
      transition: border-color 0.2s;
    }

    input:focus {
      outline: none;
      border-color: #1976d2;
      box-shadow: 0 0 0 2px rgba(25, 118, 210, 0.2);
    }

    button {
      width: 100%;
      padding: 12px;
      background-color: #1976d2;
      color: white;
      border: none;
      border-radius: 6px;
      font-size: 16px;
      font-weight: 500;
      cursor: pointer;
      transition: background-color 0.3s;
    }

    button:hover {
      background-color: #125a9c;
    }
  `]
})
export class HomeComponent {
  email: string = '';
  password: string = '';
  error: string = '';
  constructor(private authService: AuthService, private router: Router) {}

  onLogin(): void {
    this.authService.login(this.email, this.password).subscribe({
      next: (res) => {
        try {
          const json = typeof res === 'string' ? JSON.parse(res) : res;
          const token = json.access_token;
          this.authService.saveToken(token);
          this.router.navigate(['/dashboard']);
        } catch (e) {
          console.error('Erro ao interpretar resposta do backend', e);
          this.error = 'Erro inesperado.';
        }
      },
      error: (err) => {
        window.alert('Falha na autenticação! Verifique usuário e senha.');
        this.error = 'E-mail ou senha inválidos';
      }
    });
  }

}
