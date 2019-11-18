# uniLol
Aplicação baseada na API da Riot Games que visa auxiliar usuários de League of Legends a aperfeiçoar e a monitorar sua performance no jogo, tornando sua aprendizagem mais intuitiva e rápida. Ela possibilita a análise dos dados das partidas e do comportamento do próprio usuário ou de seus adversários.

# Rodar a aplicação

<b>Prerequisitos</b>
  <li>Java 8</li>
  <li>Maven</li>
  <li>MySql</li>
  
 <br>

<b>Windows</b>
1. ir até a raiz do projeto, onde se encontra o arquivo mvnw.
2. abrir cmd e mudar para essa pasta.
3. digite o comando: mvnw spring-boot:run

<b>Linux</b>
1. ir até a raiz do projeto, onde se encontra o arquivo mvnw.
2. digite: ./mvnw spring-boot:run


# Testar Swagger-UI
localhost:8080/swagger-ui.html

Para conseguir pegar informações da API da Riot é preciso atualizar a Key da Riot na aplicação. Para atualizar basta usar o endpoint /api/key/{riotKey}

# Front-end

segue link do front end da aplicação: <a href="https://github.com/andrey02/uniLol-client">FronEnd</a>

# Créditos
Aplicação foi desenvolvida para um projeto da faculdade em grupo, contendo 6 integrantes: Andrei(Eu), Ana, Pedro, Nildo, Emi e Victor.
