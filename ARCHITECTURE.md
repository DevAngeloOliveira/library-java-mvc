# Documento de Arquitetura e Boas Práticas

## 🏗️ Arquitetura do Projeto

### Camadas da Aplicação

```
┌─────────────────────────────────────────┐
│         Camada de Apresentação          │
│  (View Console + Web/API REST)          │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Camada de Controller            │
│  (Coordenação e Controle de Fluxo)      │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Camada de Service               │
│  (Lógica de Negócio + Validações)       │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Camada de Repository            │
│  (Acesso a Dados + Persistência)        │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Camada de Model                 │
│  (Entidades de Domínio)                 │
└─────────────────────────────────────────┘
```

## 📦 Organização de Packages

### com.biblioteca.model
- **Responsabilidade**: Entidades de domínio
- **Conceitos POO**: Herança, Encapsulamento, Abstração
- **Classes**: ItemBiblioteca (abstract), Livro, Revista, DVD

### com.biblioteca.repository
- **Responsabilidade**: Persistência de dados
- **Padrão**: Repository Pattern
- **Implementações**: In-Memory, SQLite

### com.biblioteca.service
- **Responsabilidade**: Lógica de negócio
- **Padrão**: Service Layer
- **Validações**: Regras de negócio centralizadas

### com.biblioteca.controller
- **Responsabilidade**: Coordenação de fluxo
- **Padrão**: MVC Controller
- **Função**: Ponte entre View e Service

### com.biblioteca.view
- **Responsabilidade**: Apresentação console
- **Padrão**: MVC View
- **Função**: Exibição de dados

### com.biblioteca.web
- **Responsabilidade**: API REST e servidor HTTP
- **Tecnologia**: HttpServer (JDK nativo)
- **Endpoints**: CRUD completo + Health Check

### com.biblioteca.dto
- **Responsabilidade**: Transferência de dados
- **Padrão**: Data Transfer Object
- **Tipos**: ItemDTO (entrada), ItemResponseDTO (saída)

### com.biblioteca.config
- **Responsabilidade**: Configurações
- **Classes**: DatabaseConfig, ConfigManager
- **Arquivos**: database.properties, application.properties

### com.biblioteca.exception
- **Responsabilidade**: Exceções customizadas
- **Hierarquia**: BibliotecaException → Exceções específicas
- **Tipos**: ItemNaoEncontradoException, ValidacaoException, etc.

### com.biblioteca.util
- **Responsabilidade**: Utilitários
- **Classes**: Logger, Validador
- **Função**: Helpers reutilizáveis

### com.biblioteca.test
- **Responsabilidade**: Testes unitários
- **Abordagem**: Testes sem framework externo
- **Cobertura**: Service layer e validações

## 🎯 Padrões de Projeto Implementados

### 1. MVC (Model-View-Controller)
- **Model**: Entidades de negócio
- **View**: Console + Web Interface
- **Controller**: Coordenação

### 2. Repository Pattern
- Interface: `ItemRepository`
- Implementações: `ItemRepositoryImpl`, `ItemRepositorySQLite`
- Benefício: Abstração de persistência

### 3. Service Layer
- Centralização de lógica de negócio
- Validações e regras
- Transações (futuro)

### 4. DTO Pattern
- Separação entre modelo e transporte
- Validação de entrada
- Formatação de saída

### 5. Singleton
- `ConfigManager`: Instância única
- Carregamento lazy
- Thread-safe

### 6. Template Method
- `ItemBiblioteca`: Classe abstrata
- Métodos abstratos: `getTipo()`
- Comportamento comum + específico

## 🔐 Tratamento de Exceções

### Hierarquia
```
BibliotecaException (base)
├── ItemNaoEncontradoException
├── ItemJaExisteException
├── OperacaoInvalidaException
└── ValidacaoException
```

### Benefícios
- Mensagens de erro claras
- Códigos de erro padronizados
- Rastreabilidade
- Melhor UX

## 📝 Sistema de Logging

### Níveis
- **DEBUG**: Informações detalhadas
- **INFO**: Eventos importantes
- **WARN**: Avisos
- **ERROR**: Erros

### Uso
```java
private static final Logger logger = Logger.getLogger(MinhaClasse.class);
logger.info("Operação realizada");
logger.error("Erro ao processar", exception);
```

## ✅ Validações

### Tipos
- Campos obrigatórios
- Formato de dados
- Regras de negócio
- Integridade referencial

### Classe Validador
```java
Validador.validarNaoVazio(valor, "campo");
Validador.validarPositivo(numero, "campo");
Validador.validarFormatoCodigo(codigo);
```

## 🌐 API REST

### Padrão de Resposta
```json
{
  "sucesso": true/false,
  "dados": {...},
  "mensagem": "...",
  "codigo": "ERROR_CODE",
  "timestamp": 1234567890
}
```

### Endpoints
- GET `/api/itens` - Lista todos
- GET `/api/itens/disponiveis` - Lista disponíveis
- GET `/api/itens/emprestados` - Lista emprestados
- POST `/api/emprestar` - Empresta item
- POST `/api/devolver` - Devolve item
- GET `/health` - Health check

## 🔧 Configuração

### application.properties
- Configurações do servidor
- Parâmetros de negócio
- Features flags
- Metadados da aplicação

### database.properties
- String de conexão
- Configurações do banco

## 🧪 Testes

### Estratégia
- Testes unitários sem framework
- Cobertura de casos de sucesso
- Cobertura de casos de erro
- Validação de exceções

### Execução
```powershell
.\run-tests.ps1
```

## 📊 Métricas de Qualidade

### Princípios SOLID
- ✅ **S**ingle Responsibility
- ✅ **O**pen/Closed
- ✅ **L**iskov Substitution
- ✅ **I**nterface Segregation
- ✅ **D**ependency Inversion

### Clean Code
- Nomes descritivos
- Métodos pequenos
- Comentários JavaDoc
- Sem código duplicado

## 🚀 Escalabilidade

### Futuras Melhorias
- [ ] Connection pooling otimizado
- [ ] Cache de consultas
- [ ] Paginação de resultados
- [ ] Autenticação JWT
- [ ] Rate limiting
- [ ] Métricas e monitoring

## 📚 Documentação JavaDoc

Todas as classes públicas e métodos importantes estão documentados com:
- Descrição funcional
- Parâmetros `@param`
- Retorno `@return`
- Exceções `@throws`
- Exemplos quando necessário
