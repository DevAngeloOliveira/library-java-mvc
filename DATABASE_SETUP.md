# Sistema de Biblioteca - Guia PostgreSQL

## 📋 Pré-requisitos

1. **PostgreSQL instalado** (versão 12 ou superior)
2. **Maven instalado** (versão 3.6 ou superior)
3. **Java JDK 11** ou superior

## 🚀 Configuração do Banco de Dados

### 1. Instalar PostgreSQL

**Windows:**
- Download: https://www.postgresql.org/download/windows/
- Siga o instalador e anote a senha do usuário `postgres`

**Linux (Ubuntu/Debian):**
```bash
sudo apt update
sudo apt install postgresql postgresql-contrib
```

**MacOS:**
```bash
brew install postgresql
brew services start postgresql
```

### 2. Criar o Banco de Dados

Acesse o PostgreSQL:
```bash
psql -U postgres
```

Crie o banco:
```sql
CREATE DATABASE biblioteca;
\c biblioteca
```

Ou via pgAdmin 4 (interface gráfica).

### 3. Executar Scripts SQL

**Opção 1 - Via linha de comando:**
```bash
# Navegar até o diretório do projeto
cd C:\Users\Lancelloth\Workspace\java_poo

# Criar as tabelas
psql -U postgres -d biblioteca -f src/main/resources/db/schema.sql

# Popular com dados iniciais
psql -U postgres -d biblioteca -f src/main/resources/db/seed.sql
```

**Opção 2 - Via pgAdmin:**
1. Abra pgAdmin 4
2. Conecte ao servidor PostgreSQL
3. Selecione o banco `biblioteca`
4. Abra Query Tool
5. Carregue e execute `schema.sql`
6. Depois carregue e execute `seed.sql`

### 4. Configurar Credenciais

Edite o arquivo `src/main/resources/database.properties`:

```properties
db.url=jdbc:postgresql://localhost:5432/biblioteca
db.username=postgres
db.password=SUA_SENHA_AQUI
```

## 📦 Compilar e Executar

### Compilar com Maven

```bash
mvn clean compile
```

### Executar com PostgreSQL

1. Abra `src/main/java/com/biblioteca/Main.java`
2. Altere a linha:
   ```java
   private static final boolean USE_DATABASE = true;
   ```

3. Execute:
   ```bash
   mvn exec:java -Dexec.mainClass="com.biblioteca.Main"
   ```

### Executar sem Maven (modo tradicional)

```bash
# Baixar dependências manualmente ou usar Maven para isso
mvn dependency:copy-dependencies

# Compilar
javac -encoding UTF-8 -cp "target/dependency/*" -d bin -sourcepath src/main/java src/main/java/com/biblioteca/Main.java

# Executar
java -cp "bin;target/dependency/*" com.biblioteca.Main
```

## 🗃️ Estrutura do Banco de Dados

### Tabelas

- **itens_biblioteca**: Tabela principal com dados comuns
- **livros**: Dados específicos de livros
- **revistas**: Dados específicos de revistas
- **dvds**: Dados específicos de DVDs

### Relacionamentos

```
itens_biblioteca (1) ---> (1) livros
itens_biblioteca (1) ---> (1) revistas
itens_biblioteca (1) ---> (1) dvds
```

## 🔍 Verificar Dados

```sql
-- Ver todos os itens
SELECT * FROM itens_biblioteca;

-- Ver livros com detalhes
SELECT i.codigo, i.titulo, l.autor, l.numero_paginas 
FROM itens_biblioteca i 
JOIN livros l ON i.id = l.item_id;

-- Ver itens emprestados
SELECT codigo, titulo, tipo 
FROM itens_biblioteca 
WHERE emprestado = TRUE;

-- Estatísticas
SELECT tipo, COUNT(*) as total 
FROM itens_biblioteca 
GROUP BY tipo;
```

## 🐛 Troubleshooting

### Erro de conexão

Se aparecer erro de conexão:

1. Verifique se PostgreSQL está rodando:
   ```bash
   # Windows
   net start postgresql-x64-14
   
   # Linux
   sudo systemctl status postgresql
   ```

2. Verifique o arquivo `database.properties`
3. Teste a conexão:
   ```bash
   psql -U postgres -d biblioteca
   ```

### Erro de autenticação

Edite `pg_hba.conf`:
- Windows: `C:\Program Files\PostgreSQL\14\data\pg_hba.conf`
- Linux: `/etc/postgresql/14/main/pg_hba.conf`

Mude `md5` para `trust` (apenas para desenvolvimento):
```
# IPv4 local connections:
host    all             all             127.0.0.1/32            trust
```

Reinicie o PostgreSQL.

### Limpar e recriar banco

```sql
DROP DATABASE biblioteca;
CREATE DATABASE biblioteca;
\c biblioteca
\i src/main/resources/db/schema.sql
\i src/main/resources/db/seed.sql
```

## 📊 Dados Iniciais (Seeders)

O script `seed.sql` cria:
- **5 Livros** (1 emprestado)
- **3 Revistas** (1 emprestada)
- **4 DVDs** (1 emprestado)

Total: **12 itens** no acervo

## 🔄 Alternar entre Memória e PostgreSQL

No arquivo `Main.java`:

```java
// Usar PostgreSQL
private static final boolean USE_DATABASE = true;

// Usar memória
private static final boolean USE_DATABASE = false;
```

## 📚 Comandos Úteis

```bash
# Listar bancos
psql -U postgres -l

# Conectar ao banco
psql -U postgres -d biblioteca

# Listar tabelas
\dt

# Descrever tabela
\d itens_biblioteca

# Sair
\q
```
