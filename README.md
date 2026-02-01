# Atividade POO: Classes de Personagens

## Componentes do Projeto (Turma 05)

* Enzo Adriel
* Isabele de Almeida
* Maria Luiza
* Natalia de Araujo

Este é um projeto Java de Programação Orientada a Objetos (POO) onde criamos e testamos classes para diferentes personagens: Arcanista, Caçador, Combatente e Guardião. Ele já vem com testes unitários para garantir que tudo funcione como deveria.
## Vídeo de apresentação

<video width="320" height="240" controls>
  <source src="assets/apresentation.mp4" type="video/mp4">
</video>

## Estrutura do Projeto

- [Arcanista/Cacador/Combatente/Guardiao].java: As classes dos nossos personagens.
- [Arcanista/Cacador/Guardiao]Test.java: Os testes unitários para cada classe, feitos com JUnit.
- Main.java: Onde a aplicação começa, a lógica principal.
- assets/: Aqui estão as imagens dos personagens. (As imagens foram geradas utilizando o ChatGPT.)
- lib/: Nossas bibliotecas externas, como o junit-platform-console-standalone para rodar os testes.

## Como Jogar

Primeiro precisará baixar o repositório e entrar nele, para isto, execute o seguinte comando:

```bash
git clone https://github.com/Linksyyy/atvd-poo-game
cd atvd-poo-game
```

Então vai precisar compilar os arquivos .java com o comando a seguir:

```bash
mkdir -p bin
javac -d bin -cp lib/junit-platform-console-standalone-1.10.2.jar *.java
```

Ele vai criar uma pasta bin e colocar todas as classes compiladas (.class) lá dentro. O JAR do JUnit é incluído para que o compilador encontre as dependências dos testes.

Após a compilação, você pode iniciar a aplicação (classe Main) utilizando o seguinte comando:

 ```bash
java -cp bin Main
```

## Como Executar os Testes

Usamos JUnit 5 para os testes. Para rodar, por exemplo, o ArcanistaTest, use este comando depois de compilar:


```bash
java -jar lib/junit-platform-console-standalone-1.10.2.jar --class-path bin --scan-classpath --include-classname ArcanistaTest
```

Você pode substituir ArcanistaTest pelo nome de qualquer outra classe de teste que exista no projeto.

## Licença

Este projeto está sob a licença [LICENSE](LICENSE).
