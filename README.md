# 👤 Serviço de Clientes — Revenda de Veículos

[![CI/CD Pipeline](https://github.com/SEU_USUARIO/servico-revenda-veiculos-clientes/actions/workflows/deploy.yml/badge.svg)](https://github.com/SEU_USUARIO/servico-revenda-veiculos-clientes/actions/workflows/deploy.yml)

Microserviço responsável pelo cadastro e gerenciamento de clientes da plataforma de revenda. Desenvolvido com **Java 17**, **Spring Boot 3** e arquitetura hexagonal, com suporte a **criptografia KMS (LGPD)** para proteção de dados sensíveis e integração com **AWS SQS** para validação de clientes na Saga de compra.

## 📋 Sumário

- [Sobre o Projeto](#-sobre-o-projeto)
- [Arquitetura](#-arquitetura)
- [Tecnologias](#-tecnologias)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Execução](#-instalação-e-execução)
  - [Executando Localmente](#executando-localmente)
  - [Executando com Docker](#executando-com-docker)
- [Documentação da API](#-documentação-da-api)
- [Testes](#-testes)
- [CI/CD](#-cicd)
- [Infraestrutura](#-infraestrutura)

## 🎯 Sobre o Projeto

O serviço de clientes gerencia o ciclo de vida dos compradores cadastrados na plataforma. Implementa conformidade com a **LGPD** por meio de criptografia assimétrica via **AWS KMS**, garantindo que dados pessoais (CPF, e-mail, telefone) sejam armazenados criptografados. O serviço também participa da Saga de compra validando clientes via SQS.

### Funcionalidades Principais

- ✅ Cadastro e edição de clientes
- ✅ Criptografia de dados sensíveis com **AWS KMS** (CPF, e-mail, telefone)
- ✅ Conformidade com **LGPD** — exclusão de dados pessoais sob demanda
- ✅ Validação de cliente na Saga (via SQS)
- ✅ Auditoria de acesso a dados
- ✅ Controle de consentimento (`consentimentoLgpd`)

## 🏗 Arquitetura

O projeto segue os princípios da Arquitetura Hexagonal:

```
src/main/java/com/servico/revenda/veiculos_clientes/
├── domain/                      # Camada de Domínio
│   ├── model/                   # Entidade: Cliente
│   ├── port/
│   │   ├── in/                  # Casos de uso (interfaces)
│   │   └── out/                 # Portas de saída (interfaces)
│   └── exception/               # Exceções de domínio
├── application/                 # Camada de Aplicação
│   ├── service/                 # Implementações dos casos de uso
│   ├── dto/                     # Data Transfer Objects
│   └── mapper/                  # Mapeadores Domain ↔ DTO
└── infrastructure/              # Camada de Infraestrutura
    ├── adapter/
│   │   ├── in/
│   │   │   ├── web/             # Controllers REST
│   │   │   └── messaging/       # Consumer SQS
│   │   └── out/
│   │       ├── persistence/     # Repositório JPA (H2)
│   │       ├── crypto/          # KmsAdapter (criptografia)
│   │       └── audit/           # AuditAdapter
│   └── config/                  # Beans e configuração AWS
```

### Camadas

- **Domain**: Entidade `Cliente` com regras de negócio (consentimento LGPD, ativação)
- **Application**: Casos de uso que orquestram criptografia + persistência
- **Adapters IN**: Controller REST + Consumer SQS (`VALIDAR_CLIENTE`)
- **Adapters OUT**: Repositório JPA + KmsAdapter (AWS KMS) + AuditAdapter
- **Infrastructure**: Configuração dos clientes AWS SDK (SQS, SFN, KMS)

## 🛠 Tecnologias

- **Java 17** — Linguagem de programação
- **Spring Boot 3** — Framework principal
- **Spring Data JPA** — Persistência de dados
- **H2 Database** — Banco de dados em memória
- **AWS KMS** — Criptografia de dados sensíveis (LGPD)
- **AWS SQS** — Fila de mensagens (validação de cliente na Saga)
- **AWS SDK v2** — Integração com serviços AWS
- **Gradle** — Gerenciamento de dependências e build
- **Docker** — Containerização
- **GitHub Actions** — CI/CD
- **JUnit 5 + Mockito** — Testes unitários

## 📦 Pré-requisitos

- **Java 17** ou superior
- **Gradle 8+** (ou use o wrapper incluído)
- **Docker** (opcional)
- **AWS CLI** configurado com credenciais válidas — ou variáveis de ambiente exportadas  
  > As credenciais precisam ter permissão de uso na chave KMS `arn:aws:kms:us-east-1:557187971532:key/6965c06c-da2b-4f4b-943c-0a7bc8ed90ac`

## 🚀 Instalação e Execução

### Executando Localmente

1. **Clone o repositório:**
```bash
git clone https://github.com/SEU_USUARIO/servico-revenda-veiculos-clientes.git
cd servico-revenda-veiculos-clientes
```

2. **Exporte as credenciais AWS:**
```bash
# Linux / macOS
export AWS_ACCESS_KEY_ID=SUA_ACCESS_KEY
export AWS_SECRET_ACCESS_KEY=SUA_SECRET_KEY
export AWS_REGION=us-east-1
export SPRING_PROFILES_ACTIVE=local

# Windows (PowerShell)
$env:AWS_ACCESS_KEY_ID     = "SUA_ACCESS_KEY"
$env:AWS_SECRET_ACCESS_KEY = "SUA_SECRET_KEY"
$env:AWS_REGION            = "us-east-1"
$env:SPRING_PROFILES_ACTIVE = "local"
```

3. **Execute a aplicação:**
```bash
./gradlew bootRun
```

A API estará disponível em `http://localhost:8082`  
Console H2: `http://localhost:8082/h2-console`

### Executando com Docker

1. **Build da imagem:**
```bash
docker build -t servico-revenda-veiculos-clientes .
```

2. **Execute o container:**
```bash
docker run -p 8080:8080 \
  -e AWS_ACCESS_KEY_ID=SUA_ACCESS_KEY \
  -e AWS_SECRET_ACCESS_KEY=SUA_SECRET_KEY \
  -e AWS_REGION=us-east-1 \
  servico-revenda-veiculos-clientes
```

## 📚 Documentação da API

### Base URL
```
Local:      http://localhost:8082
Produção:   http://revenda-alb-1838980277.us-east-1.elb.amazonaws.com
```

---

### 👤 Endpoints de Clientes

#### 1. Cadastrar Cliente

```http
POST /api/clientes
Content-Type: application/json
```

**Corpo da Requisição:**
```json
{
  "nome": "João Silva",
  "cpf": "11122233300",
  "email": "joao@email.com",
  "telefone": "11900000000",
  "endereco": "Rua das Flores 100",
  "consentimentoLgpd": true
}
```

**Validações:**
- `cpf`: 11 dígitos, sem máscara
- `consentimentoLgpd`: deve ser `true` para cadastro

**Resposta de Sucesso (201 Created):**
```json
{
  "id": "34a78509-da1f-4dab-aa28-e619b720b25a",
  "nome": "João Silva",
  "cpfMascarado": "***.***.",
  "emailMascarado": "***@***",
  "telefoneMascarado": "(**) *****-",
  "endereco": "Rua das Flores 100",
  "consentimentoLgpd": true,
  "ativo": true,
  "dataCriacao": "2026-03-01T19:48:09",
  "dataAtualizacao": "2026-03-01T19:48:09"
}
```

> CPF, e-mail e telefone são **criptografados com AWS KMS** e retornados mascarados.

#### 2. Consultar Cliente por ID

```http
GET /api/clientes/{id}
```

**Resposta de Erro (404 Not Found):**
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Cliente não encontrado: {id}"
}
```

#### 3. Editar Cliente

Permite atualizar nome, e-mail, telefone e endereço. **CPF não pode ser alterado.**

```http
PUT /api/clientes/{id}
Content-Type: application/json
```

**Corpo da Requisição:**
```json
{
  "nome": "João Silva Jr",
  "email": "joaojr@email.com",
  "telefone": "11911111111",
  "endereco": "Av. Nova 200"
}
```

#### 4. Excluir Dados Pessoais (LGPD)

Exclui os dados pessoais do cliente (anonimização), mantendo apenas o registro histórico.

```http
DELETE /api/clientes/{id}/dados
```

**Resposta de Sucesso (204 No Content)**

---

### Exemplos com cURL

```bash
ALB="http://revenda-alb-1838980277.us-east-1.elb.amazonaws.com"

# Cadastrar cliente
curl -s -X POST "$ALB/api/clientes" \
  -H "Content-Type: application/json" \
  -d '{"nome":"João Silva","cpf":"11122233300","email":"joao@email.com","telefone":"11900000000","endereco":"Rua das Flores 100","consentimentoLgpd":true}'

# Consultar por ID
curl -s "$ALB/api/clientes/{id}"

# Editar cliente
curl -s -X PUT "$ALB/api/clientes/{id}" \
  -H "Content-Type: application/json" \
  -d '{"nome":"João Silva Jr","email":"novo@email.com","telefone":"11911111111","endereco":"Av. Nova 200"}'

# Excluir dados (LGPD)
curl -s -X DELETE "$ALB/api/clientes/{id}/dados"
```

---

### Mensagens SQS Consumidas

O serviço escuta a fila `revenda-clientes-commands` e processa:

| Tipo | Ação |
|------|------|
| `VALIDAR_CLIENTE` | Verifica se o cliente existe e está ativo + callback SFN sucesso/falha |

## 🧪 Testes

```bash
# Executar todos os testes
./gradlew test

# Relatório de testes
open build/reports/tests/test/index.html
```

Cobertura de testes unitários nas camadas:
- **Domain Model**: regras de negócio do cliente (consentimento, ativação)
- **Application Services**: cadastro, edição, consulta e exclusão com Mockito
- **Crypto**: comportamento de criptografia/decriptografia KMS mockado

## 🔄 CI/CD

Pipeline automatizado via GitHub Actions (`.github/workflows/deploy.yml`):

- ✅ Build com Gradle (`./gradlew clean build`)
- ✅ Execução dos testes automatizados
- ✅ Build e push da imagem Docker para **Amazon ECR**
- ✅ Deploy automático no **Amazon ECS Fargate**
- ✅ Aguarda estabilização do serviço (health check)

### Secrets necessários no repositório

| Secret | Descrição |
|--------|-----------|
| `AWS_ACCESS_KEY_ID` | Access key com permissões ECR + ECS + KMS |
| `AWS_SECRET_ACCESS_KEY` | Secret key correspondente |
| `AWS_REGION` | Região AWS (ex: `us-east-1`) |

## ☁️ Infraestrutura


**Recursos AWS:**
- **ECS Cluster**: `revenda-veiculos`
- **ECS Service**: `servico-revenda-veiculos-clientes`
- **ECR Repository**: `servico-revenda-veiculos-clientes`
- **SQS Queue**: `revenda-clientes-commands`
- **KMS Key ARN**: `arn:aws:kms:us-east-1:557187971532:key/6965c06c-da2b-4f4b-943c-0a7bc8ed90ac`
- **ALB Route**: `/api/clientes*` (priority 10)

## 📄 Licença

Este projeto foi desenvolvido como trabalho acadêmico para a Pós-Tech FIAP.

## 👥 Autores

- **Fabio** — [@SEU_GITHUB](https://github.com/SEU_GITHUB)