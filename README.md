# Scholar Desktop

Sistema de gerenciamento de alunos para cursos de inglês, desenvolvido como trabalho final da disciplina de Linguagem de Programação 1 do IFSP — Campus São Paulo.

A aplicação é voltada para professores que desejam gerenciar suas turmas, acompanhar o desempenho dos alunos e registrar atividades avaliativas de forma centralizada.

## Funcionalidades

- Cadastro e autenticação de professor com hash de senha via BCrypt
- Gerenciamento de turmas com geração automática de nome baseado no módulo
- Cadastro e listagem de alunos por turma
- Registro de atividades avaliativas com tipos pré-definidos
- Cálculo automático da nota do aluno com pesos por tipo de atividade
- Transferência de alunos entre turmas do mesmo módulo
- Interface gráfica desktop com tema escuro

## Cálculo de Nota

A nota de cada aluno é calculada automaticamente a partir das atividades registradas, seguindo a seguinte distribuição de pesos:

| Atividade | Quantidade | Peso unitário | Total |
|---|---|---|---|
| Avaliação Contínua | 2 | 5% | 10% |
| Lição de Casa | 8 | 1,25% | 10% |
| Lição Web | 8 | 1,25% | 10% |
| Teste Oral MID | 1 | 10% | 10% |
| Teste Oral FINAL | 1 | 20% | 20% |
| Teste Escrito MID | 1 | 20% | 20% |
| Teste Escrito FINAL | 1 | 20% | 20% |

## Arquitetura

O projeto segue o padrão MVC com camada de serviço e DAO:

```
src/main/java/org/ifsp/scholardesktop/
├── model/          # Entidades do domínio
├── dao/            # Interfaces e implementações de acesso ao banco
│   ├── interfaces/
│   └── impl/
├── service/        # Regras de negócio
├── controller/     # Controllers JavaFX
├── view/           # Arquivos FXML e CSS
└── exception/      # Exceções customizadas
```

## Tecnologias

- Java 21
- JavaFX 21
- MySQL 8
- JDBC
- Maven
- BCrypt (jbcrypt 0.4)
- dotenv-java
- JUnit 5
- Mockito 5

## Pré-requisitos

- JDK 21
- MySQL 8 rodando localmente
- Maven 3.8+

## Configuração

1. Clone o repositório:
```bash
git clone https://github.com/tsouzz/scholar-desktop.git
```

2. Crie o banco de dados executando o script SQL:
```bash
mysql -u root -p < schema.sql
```

3. Crie um arquivo `.env` na raiz do projeto com as credenciais do banco:
```
DB_URL=jdbc:mysql://localhost:3306/english_school
DB_USER=seu_usuario
DB_PASSWORD=sua_senha
```

4. Execute o projeto:
```bash
mvn clean javafx:run
```

## Testes

```bash
mvn test
```

Os testes unitários cobrem as camadas de serviço com JUnit 5 e Mockito, sem dependência de banco de dados.
