# 📚 Sistema de Biblioteca - Arquitetura MVC Enterprise

> **Projeto Full-Stack Java demonstrando conceitos avançados de Programação Orientada a Objetos, padrões de design e boas práticas de desenvolvimento.**

[![Java](https://img.shields.io/badge/Java-11+-orange.svg)](https://www.oracle.com/java/)
[![SQLite](https://img.shields.io/badge/SQLite-3.45-blue.svg)](https://www.sqlite.org/)
[![MVC](https://img.shields.io/badge/Architecture-MVC-green.svg)](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller)
[![POO](https://img.shields.io/badge/POO-Complete-success.svg)](https://pt.wikipedia.org/wiki/Programa%C3%A7%C3%A3o_orientada_a_objetos)

---

## 🎯 Sobre o Projeto

Sistema completo de gerenciamento de biblioteca desenvolvido em **Java puro** (sem frameworks), implementando:

- ✅ Arquitetura MVC profissional
- ✅ Sistema de autenticação com hierarquia de usuários
- ✅ API REST com servidor HTTP nativo
- ✅ Interface Web (SPA) com HTML/CSS/JavaScript
- ✅ Persistência em SQLite com Repository Pattern
- ✅ Sistema de logging estruturado
- ✅ Tratamento de exceções customizadas
- ✅ Validações e segurança
- ✅ Testes unitários

---

## 🏗️ Arquitetura

### Padrões de Design Implementados

```
┌─────────────────────────────────────────────────────────────┐
│                      PRESENTATION LAYER                      │
│  ┌─────────────┐  ┌──────────────┐  ┌──────────────────┐   │
│  │  Web View   │  │ Console View │  │   REST API       │   │
│  │ (HTML/CSS/JS)│  │ (Terminal)   │  │ (HttpServer)     │   │
│  └──────┬──────┘  └──────┬───────┘  └────────┬─────────┘   │
└─────────┼────────────────┼──────────────────┼──────────────┘
          │                │                   │
          └────────────────┴───────────────────┘
                           │
┌──────────────────────────▼───────────────────────────────────┐
│                     CONTROLLER LAYER                          │
│  ┌──────────────────────┐  ┌───────────────────────────┐    │
│  │ BibliotecaController │  │ AutenticacaoController    │    │
│  └──────────┬───────────┘  └────────────┬──────────────┘    │
└─────────────┼──────────────────────────┼────────────────────┘
              │                           │
┌─────────────▼───────────────────────────▼────────────────────┐
│                      SERVICE LAYER                            │
│  ┌──────────────────┐  ┌─────────────────────────────────┐  │
│  │ BibliotecaService│  │  AutenticacaoService            │  │
│  │ (Business Logic) │  │  (Auth Logic + Sessions)        │  │
│  └────────┬─────────┘  └────────────┬──────────────────────┘│
└───────────┼──────────────────────────┼────────────────────────┘
            │                          │
┌───────────▼──────────────────────────▼────────────────────────┐
│                    REPOSITORY LAYER                            │
│  ┌──────────────────┐  ┌─────────────────────────────────┐   │
│  │ ItemRepository   │  │  UsuarioRepository              │   │
│  │ (SQLite/Memory)  │  │  (Memory)                       │   │
│  └────────┬─────────┘  └────────────┬────────────────────┘   │
└───────────┼──────────────────────────┼────────────────────────┘
            │                          │
┌───────────▼──────────────────────────▼────────────────────────┐
│                       DATA LAYER                               │
│  ┌──────────────────┐  ┌─────────────────────────────────┐   │
│  │ biblioteca.db    │  │  In-Memory Collections          │   │
│  │ (SQLite)         │  │  (ConcurrentHashMap)            │   │
│  └──────────────────┘  └─────────────────────────────────┘   │
└────────────────────────────────────────────────────────────────┘
```

---

## 🎓 Conceitos de POO Demonstrados

### 1️⃣ **Herança**
```java
// Hierarquia de Itens
ItemBiblioteca (abstract)
  ├── Livro
  ├── Revista
  └── DVD

// Hierarquia de Usuários
Usuario (abstract)
  ├── Admin
  ├── Bibliotecario
  └── UsuarioComum
```

### 2️⃣ **Polimorfismo**
```java
// Cada tipo de usuário implementa permissões diferentes
@Override
public boolean temPermissao(Permissao permissao) {
    // Admin: retorna true para tudo
    // Bibliotecario: lista específica de permissões
    // UsuarioComum: permissões mínimas
}
```

### 3️⃣ **Encapsulamento**
```java
// Atributos privados com acesso controlado
private String email;
private String senha;
public String getEmail() { return email; }
public void setSenha(String senha) { this.senha = senha; }
```

### 4️⃣ **Abstração**
```java
// Classes abstratas definem contratos
public abstract class Usuario {
    public abstract boolean temPermissao(Permissao permissao);
    public abstract String getDescricaoTipo();
}
```

---

## 🔐 Sistema de Autenticação

### Hierarquia de Usuários

| Tipo | Permissões | Casos de Uso |
|------|------------|--------------|
| **Admin** | • CRUD de usuários<br>• CRUD de itens<br>• Todos empréstimos<br>• Relatórios | Administrador do sistema |
| **Bibliotecário** | • CRUD de itens<br>• Gerenciar empréstimos<br>• Visualizar usuários | Funcionário da biblioteca |
| **Usuário Comum** | • Visualizar acervo<br>• Emprestar seus itens<br>• Limite de 3 empréstimos | Cliente da biblioteca |

### Fluxo de Autenticação

```
1. Login → ValidarCredenciais → CriarSessão → GerarToken
2. Requisição → ValidarToken → VerificarPermissão → ExecutarAção
3. Logout → InvalidarSessão → RemoverToken
```

---

## 🚀 Funcionalidades

### 📖 Gerenciamento de Acervo
- ✅ Cadastrar/Editar/Remover itens (Livros, Revistas, DVDs)
- ✅ Listar todos os itens
- ✅ Filtrar por disponibilidade
- ✅ Buscar por código

### 👥 Sistema de Usuários
- ✅ Registro e autenticação
- ✅ 3 níveis de permissão
- ✅ Gerenciamento de sessões
- ✅ Controle de acesso baseado em roles

### 📦 Empréstimos
- ✅ Emprestar itens
- ✅ Devolver itens
- ✅ Histórico de empréstimos
- ✅ Validação de limites

### 🌐 Interface Web
- ✅ SPA (Single Page Application)
- ✅ API REST completa
- ✅ Design responsivo
- ✅ CORS configurado

---

## 🛠️ Tecnologias Utilizadas

### Core
- **Java 11** - Linguagem principal
- **SQLite 3.45** - Banco de dados embarcado
- **HttpServer (JDK)** - Servidor HTTP nativo

### Bibliotecas
- **SLF4J 2.0.9** - Logging API
- **JDBC SQLite** - Driver de banco de dados

### Frontend
- **HTML5/CSS3** - Interface web
- **JavaScript (Vanilla)** - Lógica do cliente
- **Fetch API** - Requisições HTTP

---

## 📁 Estrutura do Projeto

```
java_poo/
├── src/main/
│   ├── java/com/biblioteca/
│   │   ├── model/                    # Entidades de domínio
│   │   │   ├── ItemBiblioteca.java  (abstract)
│   │   │   ├── Livro.java
│   │   │   ├── Revista.java
│   │   │   ├── DVD.java
│   │   │   ├── Sessao.java
│   │   │   └── usuario/
│   │   │       ├── Usuario.java      (abstract)
│   │   │       ├── Admin.java
│   │   │       ├── Bibliotecario.java
│   │   │       ├── UsuarioComum.java
│   │   │       ├── TipoUsuario.java  (enum)
│   │   │       └── Permissao.java    (enum)
│   │   ├── dto/                      # Data Transfer Objects
│   │   │   ├── ItemDTO.java
│   │   │   ├── ItemResponseDTO.java
│   │   │   ├── LoginDTO.java
│   │   │   ├── UsuarioDTO.java
│   │   │   └── UsuarioResponseDTO.java
│   │   ├── repository/               # Camada de persistência
│   │   │   ├── ItemRepository.java
│   │   │   ├── ItemRepositoryImpl.java
│   │   │   ├── ItemRepositorySQLite.java
│   │   │   ├── UsuarioRepository.java
│   │   │   └── UsuarioRepositoryImpl.java
│   │   ├── service/                  # Lógica de negócio
│   │   │   ├── BibliotecaService.java
│   │   │   └── AutenticacaoService.java
│   │   ├── controller/               # Controladores
│   │   │   ├── BibliotecaController.java
│   │   │   └── AutenticacaoController.java
│   │   ├── view/                     # Camada de apresentação
│   │   │   └── BibliotecaView.java
│   │   ├── web/                      # Servidor web
│   │   │   ├── BibliotecaServer.java
│   │   │   └── response/
│   │   │       └── ApiResponse.java
│   │   ├── config/                   # Configurações
│   │   │   ├── DatabaseConfig.java
│   │   │   └── ConfigManager.java
│   │   ├── exception/                # Exceções customizadas
│   │   │   ├── BibliotecaException.java
│   │   │   ├── ItemNaoEncontradoException.java
│   │   │   ├── ItemJaExisteException.java
│   │   │   ├── OperacaoInvalidaException.java
│   │   │   ├── ValidacaoException.java
│   │   │   ├── AutenticacaoException.java
│   │   │   ├── PermissaoNegadaException.java
│   │   │   ├── UsuarioJaExisteException.java
│   │   │   └── UsuarioNaoEncontradoException.java
│   │   ├── util/                     # Utilitários
│   │   │   ├── Logger.java
│   │   │   └── Validador.java
│   │   ├── test/                     # Testes
│   │   │   └── BibliotecaTest.java
│   │   ├── Main.java                 # Aplicação console
│   │   ├── MainWeb.java              # Servidor web
│   │   └── MainAutenticacao.java     # Demo autenticação
│   └── resources/
│       ├── db/
│       │   ├── schema-sqlite.sql
│       │   └── seed-sqlite.sql
│       ├── web/
│       │   ├── index.html
│       │   ├── style.css
│       │   └── app.js
│       └── application.properties
├── lib/                              # Dependências
│   ├── sqlite-jdbc-3.45.0.0.jar
│   ├── slf4j-api-2.0.9.jar
│   └── slf4j-simple-2.0.9.jar
├── scripts/                          # Scripts PowerShell
│   ├── menu.ps1
│   ├── compile.ps1
│   ├── run-console.ps1
│   ├── run-web.ps1
│   ├── run-autenticacao.ps1
│   ├── run-tests.ps1
│   ├── setup-db.ps1
│   └── clean.ps1
├── biblioteca.db                     # Banco SQLite
├── README.md
└── ARCHITECTURE.md
```

**Total**: 46+ arquivos Java | 26 classes no sistema de autenticação

---

## 🚀 Como Executar

### Opção 1: Menu Interativo (Recomendado)

```powershell
.\scripts\menu.ps1
```

### Opção 2: Comandos Individuais

```powershell
# 1. Compilar o projeto
.\scripts\compile.ps1

# 2. Configurar banco de dados
.\scripts\setup-db.ps1

# 3. Escolher modo de execução:

# Modo Console
.\scripts\run-console.ps1

# Modo Web (servidor na porta 8080)
.\scripts\run-web.ps1

# Demonstração de Autenticação
.\scripts\run-autenticacao.ps1

# Executar testes
.\scripts\run-tests.ps1
```

### Acessar Interface Web

Após executar `.\run-web.ps1`:
- **Interface**: http://localhost:8080
- **API**: http://localhost:8080/api/itens
- **Health Check**: http://localhost:8080/health

---

## 📊 API REST Endpoints

### Itens

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/itens` | Listar todos os itens |
| POST | `/api/item/adicionar` | Adicionar novo item |
| POST | `/api/item/emprestar` | Emprestar item |
| POST | `/api/item/devolver` | Devolver item |
| POST | `/api/item/remover` | Remover item |

### Autenticação

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/auth/login` | Fazer login |
| POST | `/api/auth/logout` | Fazer logout |
| GET | `/api/auth/me` | Dados do usuário logado |

### Health Check

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/health` | Status do servidor |

#### Exemplo de Resposta (ApiResponse)

```json
{
  "sucesso": true,
  "timestamp": 1767014588463,
  "mensagem": "Itens recuperados com sucesso",
  "dados": [
    {
      "codigo": "LIV001",
      "titulo": "Clean Code",
      "tipo": "LIVRO",
      "detalhes": "Autor: Robert C. Martin, Paginas: 464",
      "emprestado": false
    }
  ]
}
```

---

## 🧪 Testes

O projeto inclui testes unitários sem frameworks externos:

```powershell
.\scripts\run-tests.ps1
```

**Cobertura de Testes:**
- ✅ Criação de entidades
- ✅ Operações de empréstimo/devolução
- ✅ Validações e exceções
- ✅ Regras de negócio
- ✅ Permissões de usuários

---

## 📝 Logs e Monitoramento

Sistema de logging estruturado com 4 níveis:

```java
[2025-12-29 10:23:08] [INFO] [BibliotecaService] Item adicionado com sucesso: LIV001
[2025-12-29 10:23:08] [WARN] [BibliotecaService] Tentativa de emprestar item já emprestado
[2025-12-29 10:23:08] [ERROR] [AutenticacaoController] Erro ao criar usuário: Permissão negada
[2025-12-29 10:23:08] [DEBUG] [BibliotecaController] Controller: listarTodos()
```

---

## 🎨 Interface Web

Interface moderna e responsiva com:
- 📱 Design responsivo
- 🎨 Gradientes e animações CSS
- ⚡ SPA sem recarregamento
- 🔄 Atualização em tempo real
- 🎯 UX intuitiva

---

## 🔒 Segurança Implementada

- ✅ Validação de entrada em todas as camadas
- ✅ Exceções customizadas para cada tipo de erro
- ✅ Controle de acesso baseado em permissões
- ✅ Sessões com expiração automática (8 horas)
- ✅ Logs de auditoria para ações sensíveis
- ✅ CORS configurado para API

---

## 📈 Melhorias Profissionais

### Padrões Enterprise
- ✅ **Repository Pattern** - Abstração de persistência
- ✅ **Service Layer** - Lógica de negócio isolada
- ✅ **DTO Pattern** - Transferência segura de dados
- ✅ **Factory Method** - Criação de usuários por tipo
- ✅ **Singleton** - ConfigManager, Logger
- ✅ **Strategy** - Diferentes implementações de Repository

### Boas Práticas
- ✅ **SOLID Principles** aplicados
- ✅ **Clean Code** - código legível e manutenível
- ✅ **JavaDoc** completo em todas as classes
- ✅ **Logging estruturado** com timestamps
- ✅ **Exception Handling** robusto
- ✅ **Validações** em todas as entradas

---

## 📚 Aprendizados do Projeto

### Conceitos Técnicos
- Arquitetura em camadas (MVC)
- Injeção de dependências manual
- Persistência com JDBC
- Servidor HTTP nativo Java
- REST API design
- Autenticação e autorização

### Soft Skills
- Organização de código
- Documentação técnica
- Versionamento
- Debugging e troubleshooting
- Pensamento arquitetural

---

## 🤝 Contribuições

Este é um projeto educacional para demonstração de conceitos de POO e arquitetura MVC.

Sinta-se à vontade para:
- ⭐ Dar uma estrela no projeto
- 🐛 Reportar bugs
- 💡 Sugerir melhorias
- 🔀 Fazer fork e experimentar

---

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais e de portfólio.

---

## 👤 Autor

**Desenvolvedor Full-Stack Java**

- 💼 LinkedIn: [Seu LinkedIn]
- 🐙 GitHub: [Seu GitHub]
- 📧 Email: [Seu Email]

---

## 🎯 Próximos Passos (Roadmap)

- [ ] Migrar para PostgreSQL em produção
- [ ] Implementar JWT para autenticação
- [ ] Adicionar Spring Boot
- [ ] Criar frontend com React
- [ ] Deploy em cloud (AWS/Azure)
- [ ] CI/CD com GitHub Actions
- [ ] Testes de integração
- [ ] Docker containerization

---

<div align="center">

### ⭐ Se este projeto foi útil, considere dar uma estrela!

**Desenvolvido com ☕ e muito código**

</div>
