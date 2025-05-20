// src/app/services/usuario.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Usuario } from '../models/usuario.model';
import { Observable } from 'rxjs';
import { AuthService } from '../../../services/auth.service';
import { StringToken } from '@angular/compiler';

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private apiUrl = 'http://localhost:8081/usuarios';

  constructor(private http: HttpClient, private auth: AuthService) {}

  criar(usuario: Usuario): Observable<any> {
    const token = this.auth.getToken(); // recupera token do storage
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    return this.http.post(this.apiUrl, usuario, { headers });
  }

  listar(): Observable<Usuario[]> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<Usuario[]>(this.apiUrl, { headers });
  }

  buscarPorId(id: String): Observable<Usuario> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({ 'Authorization': `Bearer ${token}` });
    return this.http.get<Usuario>(this.apiUrl+`/${id}`, { headers });
  }

  atualizar(id: String, usuario: Usuario): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({ 'Authorization': `Bearer ${token}` });
    return this.http.put(this.apiUrl+`/${id}`, usuario, { headers });
  }

  deletar(id: string): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({ 'Authorization': `Bearer ${token}` });
    return this.http.delete(this.apiUrl + `/${id}`, { headers });
  }
}
