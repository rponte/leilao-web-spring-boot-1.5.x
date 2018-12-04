LEILAO WEB SPRING BOOT 1.5
==========================

Aplicação demo para aprender um pouco mais sobre Spring Boot 1.5 e alguns de seus módulos.

Tecnologias utilizadas
----------------------

Tecnologias e frameworks utilizados para código de produção e testes automatizados:

1. Java 6;
2. Spring Boot 1.5.x;
3. jUnit 4.x;
4. Assertj;
5. Ant;

Para gerenciamento de dependências e configuração do projeto no Eclipse o **Maven** está sendo utilizado.

Configurando o projeto no Eclipse
---------------------------------

O ambiente de desevolvimento do projeto é configurado através da ferramenta **Maven**. Ela se encarregará de baixar todas as dependências (libs) e configurar o projeto no Eclipse, tudo através de linha de comando.

**IMPORTANTE**: Apesar da aplicação rodar corretamente com Java 8, é recomenado rodá-la e compilá-la com Java 6 para antecipar problemas de compatabilidade no WebLogic. Você pode utilizar o JDK da Oracle.

Para configurar o projeto siga os passos:

1. Baixe o projeto do Git da empresa, basta clona-lo:

```shell
git clone https://github.com/rponte/leilao-web-spring-boot-1.5.x.git leilao-web-spring-boot
```

2. Configure o projeto para ser importado pelo Eclipse (os arquivos `.project`, `.classpath` e `.settings` serão criados). Pode demorar um pouco ao executar este comando pela primeira vez, pois o Maven precisará baixar todas as dependências do projeto:

```shell
cd leilao-web-spring-boot
./mvnw eclipse:clean
```

3. Importe o projeto no Eclipse como **Existing Maven Project**;
4. (opcional) Caso não tenha o Java 6, configure o projeto no Eclipse para apontar para o Java 7 ou 8;
5. Rode a bateria de testes via Eclipse;

Todos os testes devem passar com sucesso.

Rodando a bateria de testes via Maven
--------------------------------------

Basta executar o comando abaixo:

```shell
./mvnw clean test
```

Se todos os testes passarem você verá ao final da execução a mensagem "**BUILD SUCCESS**".

Rodando a aplicação via linha de comando
----------------------------------------

Para rodar a aplicação sem a necessidade de um servidor de aplicação (como Tomcat ou WebLogic) ou mesmo da IDE Eclipse, execute o comando:

```shell
./mvnw spring-boot:run
```

Para acessar a aplicação, basta acessar a URL [http://localhost:8080/adp-federation/](http://localhost:8080/adp-federation/)

Gerando o WAR
-------------

Para gerar o WAR da aplicação, basta executar o comando abaixo:

```shell
./mvnw clean compile package -Dmaven.test.skip=true
```

Repare que estamos ignorando os testes automatizados nesta etapa. O arquivo WAR será gerado em  `/target/leilao-web-X.X.X.war` pronto para ser deployado no WebLogic ou Tomcat.


Deploy no WebLogic (local)
------------------

Para efetuar o deploy do WAR no WebLogic de forma automatizada, precisamos executar o script Ant via IDE Eclipse ou linha de comando. Para linha de comando basta executar a target `deploy:local` para deploy no WebLogic rodando na máquina local:

```shell
ant deploy:local
```

Lembre-se de levantar o servidor pela IDE JDeveloper.

O endereço da aplicação no servidor local é [http://localhost:7101/leilao-web/](http://localhost:7101/leilao-web/).

Deploy no WebLogic (DEV)
------------------

Para deployar a aplicação no WebLogic do ambiente de DEV, execute a target `deploy:dev` como abaixo:

```shell
ant deploy:dev
```

**ATENÇÃO-1**: Apesar do processo ser simples, o servidor de DEV apresenta diversos problemas e algumas vezes o deploy não ocorre, neste caso, tente uma 2a ou 3a vez. Se ainda assim não funcionar, faça o deploy manual.

**ATENÇÃO-2**: Por padrão o deploy ocorre no cluster `dev_cluster1`, caso queira trocar o cluster ou servidor basta alterar a propriedade `deploy.server.targets` no arquivo `/deploy/server-dev.properties`. Por exemplo, `dev_server1`.

O endereço da aplicação no servidor de DEV é [https://dev.server.com.br/leilao-web/](https://dev.server.com.br/leilao-web/).

Para mais informações
--------------------

Qualquer dúvida, basta falar com o Rafael Ponte, [rponte@gmail.com](rponte@gmail.com)