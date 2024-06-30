# Projeto de Coleta e Processamento de Dados Climáticos

Este projeto tem como objetivo demonstrar a coleta, processamento e análise de dados climáticos de diversas cidades, utilizando Java com Apache POI para geração de arquivos Excel. O projeto inclui experimentos para comparar o desempenho da coleta de dados com diferentes números de threads.

## Tecnologias Utilizadas

- Java
- Apache POI (para manipulação de arquivos Excel)
- Maven (para gerenciamento de dependências)
- Git (controle de versão)

## Estrutura do Projeto

O projeto está estruturado da seguinte maneira:

- **src/main/java**: Contém o código-fonte Java.
- **src/main/resources**: Contém recursos necessários para o projeto.
- **pom.xml**: Arquivo de configuração do Maven.

## Funcionalidades

O projeto realiza as seguintes funcionalidades:

1. **Coleta e Processamento de Dados**: Coleta dados climáticos de várias cidades e processa esses dados para calcular temperaturas mínimas, máximas e médias.

2. **Comparação de Desempenho**: Realiza experimentos para comparar o desempenho da coleta de dados utilizando diferentes números de threads (sem threads, 3 threads, 9 threads, 27 threads).

3. **Geração de Relatórios**: Gera relatórios em formato Excel com os dados climáticos coletados e tabelas de comparação de desempenho.

## Como Rodar o Projeto

Para rodar o projeto, siga os passos abaixo:

### Pré-requisitos

- Java JDK instalado (versão 8 ou superior)
- Maven instalado

### Passos

1. **Clone o Repositório**

   ```bash
   git clone https://seu-repositorio.git
   cd nome-do-repositorio
    ```
   
2. **Instale as dependencias**

   Certifique-se de instalar as dependencias:

   ```bash
    mvn mvn clean install -U
    ```
   
3. **Compile o Projeto**

   Utilize o Maven para compilar o projeto:

   ```bash
    mvn clean package
    ```
   
4. **Execute o Projeto**

    Após compilar, execute o projeto com o seguinte comando:

   ```bash
    java -cp target/nome-do-arquivo-jar.jar package.Main
    ```
   
    ps: Se preferir apenas rode a main.

## Verifique os Resultados

Os resultados dos experimentos serão impressos no console, incluindo o tempo médio de execução para cada configuração de threads. Arquivos Excel serão gerados na pasta de saída especificada no código.

## Estrutura dos Arquivos Gerados
Os arquivos gerados são organizados da seguinte forma:

- /sem_threads: Resultados dos experimentos sem threads.
- /com_3_threads: Resultados dos experimentos com 3 threads.
- /com_9_threads: Resultados dos experimentos com 9 threads.
- /com_27_threads: Resultados dos experimentos com 27 threads.
- /tabela_comparacao_tempos.xlsx: Tabela de comparação de tempos médios de execução.

## Contribuições
Contribuições são bem-vindas! Sinta-se à vontade para enviar pull requests com melhorias, correções de bugs ou novas funcionalidades.
