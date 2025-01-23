<h1 align="center" id="icons">Order Project - Spring Boot and Quarkus</h1>


<p align="center" id="icons">
  <a href="#icons">
    <img alt="GitHub language count" src="https://img.shields.io/github/languages/count/GuilhermePalma/order-event-driven-quarkus?color=2304D361">
  </a>

  <a href="https://github.com/GuilhermePalma/order-event-driven-quarkus">
    <img alt="Repository size" src="https://img.shields.io/github/repo-size/GuilhermePalma/order-event-driven-quarkus">
  </a>

  <a href="https://wakatime.com/badge/user/e9fe1d59-49e9-456e-8e0d-cefe5fda1770/project/2838a73d-d817-416a-b3d6-27eb0c2e3f73">
      <img src="https://wakatime.com/badge/user/e9fe1d59-49e9-456e-8e0d-cefe5fda1770/project/2838a73d-d817-416a-b3d6-27eb0c2e3f73.svg" alt="wakatime">
  </a>

  <a href="https://github.com/GuilhermePalma/order-event-driven-quarkus/commits/main">
    <img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/GuilhermePalma/order-event-driven-quarkus">
  </a>

</p>

## Como Executar ?

1. Setup: Ter instalado os Seguintes Softwares
   - [Docker Desktop](https://www.docker.com/products/docker-desktop/) Teste usando: ``docker version``
   - [JDK Correto 17 (ou equivalente) - AWS](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html): Teste usando ``java -version``
   - [Gradle - Opcional](https://gradle.org/install/): Teste usando ``gradle -v``
2. Order Spring Boot: Na raiz do Projeto, Executar o comando para criar o Executavel JAVA
    - ``./order-spring-boot/gradlew assemble -p "./order-spring-boot/"``
    - Caso Tenha Problemas, execute ``cd order-spring-boot``, em seguida ``./gradlew assemble``
3. Docker: Na raiz do Projeto, Iniciar o Container
    - ``docker-compose up``

Agora, todos os serviços estão disponiveis para realizar testes e validações

- [Interface Visual - API Spring Boot](http://localhost:8081/swagger-ui/index.html): Acesse ``http://localhost:8081/swagger-ui/index.html``
- [Interface Visual - API Kafka](http://localhost:19000/): Acesse ``http://localhost:19000/``
- [Interface Visual - API Postegre](http://localhost:8082/browser/): Acesse ``http://localhost:8082/browser/`` (Obs: Credenciais anexas no e-mail)

---

## Objetivos

- Desenvolver Aplicações em Spring Boot e Quarkus
- Utilizar Design Patterns Apropriados (Factory, Builder, Repository)
- Aplicar os Conceitos de Event Driven Architeture com Kafka
- Persistencia e Edição de Valores em Banco de Dados - PostgreSQL
- Recuperação dos Valores em Banco de Dados com Paginação - PostgreSQL
- Containizar Aplicações e Executaveis
- Acompanhar Metricas com DataDog

## Meu Processo

Para o Desenvolvimento desse Projeto:
- Realizei a Leitura e Validação do Entendimento do Documento Tecnico
- Priorizei os Itens me baseando em Criticidade, Necessidade e Familiaridade Tecnica
- Organizei os Itens em um [Projeto do GitHub](https://github.com/users/GuilhermePalma/projects/4)
- Iniciei o Desenvolvimento, [versionando as alterações](https://github.com/GuilhermePalma/order-event-driven-quarkus/commits/main/) com GitFlow
- Criei e Validei Exemplos de Requisições e Parametros para Auxiliar nas Validações
- Documentei o Projeto e Aprendizados

## Objetivos Atingidos

- Spring Boot: Desenvolvimento do Microserviço
- Design Patterns: Aplicação e Reutilziação
  - Factory: SQL e Kafka com Operações Comuns em uma Interface Generica
  - Builder: Criação de Classes a partir de Parameros da Requisição
  - Repository: Segregação da Logica SQL das Demais Camadas
- Boas Praticas: Tratamento de Exceções e Logging de Status, Informaçoes e Erros
- Banco de Dados: Utilização do Postegree e Aplicação customizadas de operações com JPA 
- Docker File: Containerizaçao do Microserviço em Spring Boot
- Docker Compose: Containerizaçao do PostegreSQL e Kafka
- Docker Compose: Implementação de Interfaces Visuais: Swagger (Spring Boot), KafDrop (Kafka) e PGAdmin (Postgree)

--- 

## Objetivos não Atingidos

- Criação do Serviço em Quarkus
  - Contexo: Realizada a configuração de depencias e migração do Spring Boot para o Quarkus. Entretanto, devo ao pouco contato e tempo habil para estudo (2 dias), 
  não possivel entregar por Impedimentos na Aplicação da Injeção de Dependencias no Quarkus
  - Ponto Positivo: Migração das Dependencias e Logicas em um Framework com apenas 1 dia de estudo
  - Ponto de Melhoria: Estudo acerca da Injeção de Dependencias no Quarkus
- Utilização do DataDog em Container
  - Contexto: Não foi possivel implementar a observabilidade por conta de Problemas com as Credenciais (API_KEY) no DataDog Agent. Devido ao tempo e outros requisitos
  mais importantes, optei por abrir mão da observalidade e aplicar o tempo em outros pontos necessários
  - Ponto de Melhoria: Aplicação Prática do Datadog em um Docker Container para visualização das Metricas 


## Se eu continuasse desenvolvendo, eu faria...

Ao pensar na Continuidade do Desenvolvimento desse Projeto...
- BACKEND: Removeria o Padrão Factory da Aplicação Quarkus, deixando apenas API, Service e Database
- BACKEND: Adicionaria a Camada de Eventos (Consumer e Producer), ainda sem o Padrão Factory
- BACKEND: Adicionaria o Padrão Factory, replicando a Ideia de Operações Comuns SQL e Kafka
- QA: Complementaria os Projetos com mais Testes Unitarios e Integrados
- DEVOPS: Criaria um Projeto apenas com o Datadog para Testes (PoC), ao funcionar, aplicaria no Projeto
- DEVOPS: Implementaria um Fluxo CI/CD no GitHub Actions junto à AWS para deploys automaticos

Com os passos acima, já seria um projeto bem desenvolvido, cobrindo todos os requisitos tecnicos iniciais e dando abertura para
seguir com alterações e novas as funcionalidades, assim como aplicar regras de negócio.

---

## Conclusão

Desde o momento que recebi o Desafio Tecnico, me organizei para focar primeiro na entrega dos Requisitos Necessários para 
a execução da aplicação. Dessa forma, priorizei o escopo que eu tinha mais familiaridade e que garantia a entrega,
para depois explorar os pontos que eu possuia menos contato e maior risco de não entregar. Nesse contexto, priorizei a entrega
no lugar da inovação, cumprindo principalmente o tempo estipulado.

Olhando de uma maneira mais interna, foi com pesar que deixei de fora a Aplicação em Quarkus e as Metricas no Datadog, visto que eram dois pontos que 
estava engajado em desenvolver e entregar. Entretanto, tambem foi importante para mostar pontos tecnicos de melhoria e entender os impedimentos que surgiram,
impedimentos esses que provavelmente seriam resolvidos com mais tempo habil e trocas de conhecimento com outros profissionais.

Olhando de maneira geral, acredito trazer uma entrega consistente e que seja possivel entender a organização, conhecimento e habilidades tecnicas, 
organizacionais e comunicativas aplicadas no projeto.


## Quem é Guilherme Palma

- GitHub - [Guilherme Palma](https://github.com/GuilhermePalma)
- Linkedin - [Guilherme Peres Lins da Palma](www.linkedin.com/in/guilherme-peres-lins-da-palma)
