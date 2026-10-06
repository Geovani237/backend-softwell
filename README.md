# 🩺 Softwell API: Saúde Psicossocial no Trabalho

![Java](https://img.shields.io/badge/Java-24-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

> API REST da plataforma Softwell, focada no acompanhamento do bem-estar e gestão dos riscos psicossociais de colaboradores. A API centraliza registros de humor diário e questionários psicossociais enviados pelo aplicativo móvel, gerando indicadores analíticos por tema para suporte à gestão de RH e lideranças.

📅 **Projeto desenvolvido em equipe para o Challenge FIAP (2025).**
📱 **Aplicativo Android (Kotlin) integrado:** [Softwell-Challenge/Softwell](https://github.com/Softwell-Challenge/Softwell)

---

## ✨ Funcionalidades

### 👤 Colaborador
- **Autenticação:** Cadastro e login seguro via JWT.
- **Humor Diário:** Registro de humor com trava de *cooldown* de 24 horas entre envios.
- **Questionário Psicossocial:** Preenchimento de avaliação estruturada em 5 temas.
- **Votação em Ações de Bem-Estar:** Escolha mensal de atividades propostas pela empresa (limite de 1 voto a cada 30 dias).
- **Histórico:** Consulta do próprio histórico de registros de humor filtrado por data.

### 🛠️ Administrador
- **Gestão de Opções:** Cadastro, atualização e exclusão das opções de humor e atividades de bem-estar.
- **Análise de Indicadores:** Visualização de médias por tema do questionário psicossocial (visão geral ou filtrada por data).
- **Relatório de Participação:** Relatório e métricas da votação de atividades.

---

## 📊 Temas do Questionário Psicossocial

| Tema | O que mede |
| :--- | :--- |
| **Carga de trabalho** | Volume de demandas, impacto na qualidade de vida e horas extras. |
| **Sinais de alerta** | Sintomas como insônia, irritabilidade e prejuízo na produtividade. |
| **Clima de relacionamento** | Respeito, colaboração, acolhimento e liberdade de expressão no time. |
| **Comunicação** | Clareza no alinhamento de tarefas, metas e fluxo de informações. |
| **Relação com a liderança** | Disponibilidade, escuta, reconhecimento, confiança e suporte do gestor. |

---

## 🛠️ Tecnologias Utilizadas

| Categoria | Tecnologias |
| :--- | :--- |
| **Linguagem** | Java 24 |
| **Framework Base** | Spring Boot 3.5 (Web, Data MongoDB, Security) |
| **Segurança & Auth** | Spring Security, JWT (`jjwt 0.11.5`), BCrypt |
| **Banco de Dados** | MongoDB (NoSQL) |
| **Documentação** | Springdoc OpenAPI (Swagger UI) |
| **Produtividade & Build** | Lombok, Apache Maven |

---

## 🏗️ Arquitetura do Projeto

O projeto segue a divisão de responsabilidades em camadas bem definidas:

```text
src/main/java/com/example/softwell/
├── controller/   # Endpoints REST e tratamento global de exceções (@ControllerAdvice)
├── service/      # Regras de negócio (cooldowns, agregações, regras de acesso)
├── repository/   # Interfaces do Spring Data MongoDB
├── model/        # Documentos e subdocumentos mapeados do MongoDB
├── dto/          # Data Transfer Objects para request e response
├── security/     # Filtros e configurações do Spring Security e JWT
├── util/         # Helper classes para construção de Pipelines de Agregação
└── exception/    # Exceções customizadas de domínio (ex.: CooldownException)
```

### 💡 Destaque: Processamento de Indicadores via Aggregation Pipeline
Parte das respostas do questionário possui caráter qualitativo ("Nunca", "Às vezes", "Sempre", "Leve", "Alta"...). Para viabilizar a análise quantitativa, a API utiliza um **Pipeline de Agregação nativo do MongoDB**:

1. Mapeia cada resposta textual em uma nota numérica de **1 a 5** utilizando a etapa `$switch`.
2. Agrupa os documentos e calcula a média ponderada das respostas de cada pergunta.
3. Consolida as médias das perguntas no score final do tema.
4. Permite a aplicação de filtros temporais para acompanhar a evolução do clima ao longo do tempo.

> ⚡ **Benefício:** A computação é realizada totalmente no banco de dados, otimizando o consumo de memória da aplicação backend.

---

## 🗄️ Coleções do MongoDB

| Coleção | Descrição dos Dados |
| :--- | :--- |
| `usuarios` | Cadastro de usuários, perfis de acesso (ROLES) e senhas em hashes BCrypt. |
| `humores` | Opções de estados de humor gerenciadas pelo administrador. |
| `userHumorResponses` | Registros diários de humor submetidos pelos colaboradores. |
| `psychosocial_answers` | Respostas aos questionários, com subdocumentos divididos por tema. |
| `activity` | Catálogo de atividades de bem-estar disponíveis para votação. |
| `userChoice` | Registros individuais de votos dos colaboradores nas atividades. |

---

## 🚀 Endpoints da API

### 🔑 Autenticação e Usuários
| Método | Rota | Descrição | Nível de Acesso |
| :---: | :--- | :--- | :---: |
| `POST` | `/softwell/auth/register` | Cadastra um novo colaborador | Público |
| `POST` | `/softwell/auth/login` | Autentica e gera o Bearer JWT | Público |
| `GET` | `/softwell/auth/getAll` | Lista todos os usuários cadastrados | `Admin` |
| `DELETE` | `/softwell/auth/delete` | Remove um usuário | `Admin` |

### 😊 Gestão e Registro de Humor
| Método | Rota | Descrição | Nível de Acesso |
| :---: | :--- | :--- | :---: |
| `GET` | `/api/humores` | Lista as opções de humor | Autenticado |
| `POST` | `/api/humores/add` | Adiciona uma nova opção de humor | `Admin` |
| `DELETE` | `/api/humores/{id}` | Remove uma opção de humor | `Admin` |
| `POST` | `/api/humores/userhumor` | Registra o humor do colaborador | Autenticado |
| `GET` | `/api/humores/status/{userId}` | Verifica se o colaborador já registrou o humor hoje | Autenticado |
| `GET` | `/api/humores/history/by-date` | Consulta histórico de humor por período | Autenticado |

### 📋 Questionário Psicossocial
| Método | Rota | Descrição | Nível de Acesso |
| :---: | :--- | :--- | :---: |
| `POST` | `/api/psychosocial/submit` | Envia as respostas do questionário | Autenticado |
| `GET` | `/api/psychosocial/user/{userId}` | Busca histórico de respostas de um colaborador | Autenticado |
| `GET` | `/api/psychosocial/analysis/latest-averages` | Retorna as últimas médias agregadas por tema | `Admin` |
| `GET` | `/api/psychosocial/analysis/by-date/{date}` | Retorna as médias agregadas por tema em uma data específica | `Admin` |

### 🗳️ Atividades de Bem-Estar e Votação
| Método | Rota | Descrição | Nível de Acesso |
| :---: | :--- | :--- | :---: |
| `GET` | `/act/activity` | Lista as atividades disponíveis | Autenticado |
| `POST` | `/act/activity` | Cadastra uma nova atividade | `Admin` |
| `PUT` | `/act/activity` | Atualiza uma atividade existente | `Admin` |
| `DELETE` | `/act/activity/{id}` | Deleta uma atividade | `Admin` |
| `POST` | `/act/choice` | Computa o voto do colaborador em uma atividade | Autenticado |
| `GET` | `/act/report` | Relatório consolidado dos votos | `Admin` |
| `GET` | `/act/status/{userId}` | Verifica elegibilidade de voto do colaborador | Autenticado |

---

## 🔧 Como Executar Localmente

### 📋 Pré-requisitos
- **Java Development Kit (JDK) 24** instalado.
- Instância ativa do **MongoDB** (local ou cluster no MongoDB Atlas).

### ⚙️ Passo a Passo

1. **Clonar o repositório:**
   ```bash
   git clone [https://github.com/Softwell-Challenge/backend-softwell.git](https://github.com/Softwell-Challenge/backend-softwell.git)
   cd backend-softwell
   ```

2. **Configurar variáveis de ambiente:**
   Defina a URL de conexão do banco de dados antes de subir a aplicação:

   - **Linux / macOS:**
     ```bash
     export MONGODB_URI="mongodb://localhost:27017/softwell"
     ```
   - **Windows (PowerShell):**
     ```powershell
     $env:MONGODB_URI="mongodb://localhost:27017/softwell"
     ```

3. **Executar a aplicação:**
   Utilize o Maven Wrapper para compilar e inicializar o projeto:
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Acessar a documentação:**
   - **API Root:** `http://localhost:8080`
   - **Swagger UI (OpenAPI):** `http://localhost:8080/swagger-ui.html`

---

## 🧪 Exemplo de Uso via cURL

```bash
# 1. Autenticação e obtenção do Token JWT
curl -X POST http://localhost:8080/softwell/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "colaborador", "password": "minhaSenha"}'

# 2. Chamada a um endpoint protegido enviando o Bearer Token
curl -X GET http://localhost:8080/api/humores \
  -H "Authorization: Bearer SEU_TOKEN_JWT_AQUI"
```

---

## 👥 Equipe do Projeto

| Integrante | GitHub |
| :--- | :--- |
| **Geovani Carlos de Souza** | [@Geovani237](https://github.com/Geovani237) |
| **Iago Pachiani** | [@IagoPachiani](https://github.com/iagovalverde) |
| **Pedro Marquesini** | [@PedroMarquesini](https://github.com/pedromarquesini) |
| **Bruno Ferreira** | [@BrunoFerreira](https://github.com/Brunoeugenio01) |
