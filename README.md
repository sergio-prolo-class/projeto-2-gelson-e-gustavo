# Projeto Java of Empires
### Instituto Federal de Santa Catarina (IFSC)
### Disciplina de POO
### Docente: Sérgio Prolo
### Discentes: Gelson Junior e Gustavo Ribeiro

## Ferramentas

- Linguagem: Java 
- Paradigma Programação Orientada a Objeto
- Projeto gradle

## Funcionalidades Implementadas

**Sistemas de combate**

**Ataque Básico (3 pontos)**

Implementa sistema de ataque onde personagens podem atacar outros personagens no jogo.

**Sistema de Morte (3 pontos)**

Personagens com vida zero são removidos do jogo com efeitos visuais.

**Alcance Variável (4 pontos)**

Ataques têm limite de distância para acertar, variando conforme tipo de personagem.

## Controles Avançados

**Filtro por Tipo (4 pontos)**

Implementar radio buttons para filtrar quais personagens serão afetados pelos comandos.

**Controle de Montaria (5 pontos)**

Adicionar botão para alternar estado montado/desmontado dos personagens com montaria.

**Atalhos de Teclado (6 pontos)**

Implementar controles por teclado para facilitar comandos rápidos. 

## Arquitetura de Software

**Arquivo de Configurações (3 pontos)**

Centralizar valores constantes em arquivo ou classe de configuração. 


## Interface do Usuário

**Barra de Vida (4 pontos)**

Indicador visual da vida atual de cada personagem acima do sprite.

## Funcionalidades de Jogo

**Sistema de Coleta (4 pontos)**

Implementar mecânica de coleta de recursos adicionados ao jogador. 

## 🎮 Como Executar o Jogo

### No terminal, execute:
```bash
1 Clone o repositório

git clone https://github.com/sergio-prolo-class/projeto-2-gelson-e-gustavo.git

2 Acesse o diretório do projeto

cd projeto-2-gelson-e-gustavo

3 Execute o projeto

Você pode executar o jogo de duas formas:

Pela classe App,

Pelo terminal, utilizando o comando:

./gradlew run

```
### Decisões de design importantes

### 1. Implementação de Ataques dos Personagens

A implementação do sistema de ataques foi um dos principais desafios do desenvolvimento do jogo, especialmente pela necessidade de manter comportamentos consistentes entre diferentes tipos de personagens.

- **Problema:** Cada personagem possui atributos distintos (como alcance, dano e tempo de ataque), o que dificultava a padronização da lógica de ataque 

- **Solução:** Foi criada uma estrutura comum para o sistema de ataque, centralizando a lógica principal e permitindo que cada personagem apenas especialize seus atributos e comportamentos específicos.

  ## Licença

Este projeto está licenciado sob a MIT License - veja o arquivo [LICENSE](LICENSE) para detalhes.

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
