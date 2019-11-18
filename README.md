# uniLol
Aplicação baseada na API da Riot Games que visa auxiliar usuários de League of Legends a aperfeiçoar e a monitorar sua performance no jogo, tornando sua aprendizagem mais intuitiva e rápida. Ela possibilita a análise dos dados das partidas e do comportamento do próprio usuário ou de seus adversários.

# Rodar a aplicação

<b>Prerequisitos<b>
  Java 8
  Maven
  MySql
  
Windows
1. ir até a raiz do projeto, onde se encontra o arquivo mvnw.
2. abrir cmd e mudar para essa pasta.
3. digite o comando: mvnw spring-boot:run

Linux
1. ir até a raiz do projeto, onde se encontra o arquivo mvnw.
2. digite: ./mvnw spring-boot:run


# Testar Swagger-UI
localhost:8080/swagger-ui.html

Para conseguir pegar informações da API da Riot é preciso atualizar a Key da Riot na aplicação. Para atualizar basta usar o endpoint /api/key/{riotKey}
