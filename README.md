# Atividade POO: Classes de Personagens

## Componentes do Projeto (Turma 05)

* Enzo Adriel
* Isabele de Almeida
* Maria Luiza
* Natalia de Araujo

Este é um projeto Java de Programação Orientada a Objetos (POO) onde criamos e testamos classes para diferentes personagens: Arcanista, Caçador, Combatente e Guardião. Ele já vem com testes unitários para garantir que tudo funcione como deveria.

## Estrutura do Projeto

- Arcanista.java, Cacador.java, Combatente.java, Guardiao.java: As classes dos nossos personagens.
- ArcanistaTest.java: Os testes unitários para a classe Arcanista, feitos com JUnit.
- Main.java: Onde a aplicação começa, se tiver uma lógica principal.
- assets/: Aqui estão as imagens dos personagens. (As imagens foram geradas utilizando o ChatGPT.)
- lib/: Nossas bibliotecas externas, como o junit-platform-console-standalone para rodar os testes.

## Como Compilar

Para compilar os arquivos .java, vá até a pasta raiz do projeto e execute este comando:

bash
mkdir -p bin
javac -d bin -cp lib/junit-platform-console-standalone-1.10.2.jar *.java


Ele vai criar uma pasta bin e colocar todas as classes compiladas (.class) lá dentro. O JAR do JUnit é incluído para que o compilador encontre as dependências dos testes.

## Como Executar os Testes

Usamos JUnit 5 para os testes. Para rodar, por exemplo, o ArcanistaTest, use este comando depois de compilar:

bash
java -jar lib/junit-platform-console-standalone-1.10.2.jar --class-path bin --scan-classpath --include-classname ArcanistaTest


Você pode substituir ArcanistaTest pelo nome de qualquer outra classe de teste que exista no projeto.

## Licença

Este projeto está sob a licença [LICENSE](LICENSE).
