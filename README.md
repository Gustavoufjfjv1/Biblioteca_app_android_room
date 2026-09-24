# Biblioteca_app_android_room


*   **Jetpack Compose**: Construção de interface declarativa e moderna.
*   **Room Database**: Camada de abstração sobre o SQLite, eliminando cursores e queries manuais.
*   **Retrofit**: Cliente HTTP para comunicação assíncrona com a PokeAPI.
*   **Kotlin Coroutines & Flow**: Gerenciamento de tarefas assíncronas e fluxo de dados reativo em tempo real.
*   **AndroidViewModel**: Ciclo de vida integrado com o contexto da aplicação para inicialização do Room.

---

### Detalhes das Camadas:
1.  **`model`**: Contém o modelo de domínio puro, livre de anotações do Room ou do Retrofit.
2.  **`data`**: Centraliza a persistência local (Room), a comunicação remota (Retrofit) e o **Repository**, que orquestra a lógica de sincronização sem expor detalhes de implementação para as camadas superiores.
3.  **`viewmodel`**: Coleta o `Flow` de dados do repositório e expõe um `StateFlow` com o estado atual da tela. Gerencia também uma `sealed interface` para expor o status de rede de forma isolada.
4.  **`ui`**: Camada puramente visual que renderiza o estado atualizado e reage a mudanças de conectividade.

---

## Integração com a API e Estratégia de Sincronização

### API Escolhida: Open Library API
O aplicativo consome os dados públicos da **https://openlibrary.org/search.json?q=harry+potter&limit=20**, uma API RESTful gratuita que fornece informações detalhadas sobre livros.
### Fluxo Single Source of Truth (SSOT)
A interface **nunca** exibe os dados vindos diretamente da internet. O fluxo acontece da seguinte forma:

1.  A UI observa um fluxo contínuo do banco de dados local.
2.  O `PokemonRepository` dispara uma requisição em segundo plano para a PokeAPI.
3.  Ao receber a lista de Pokémon, o repositório salva as novas entidades no `Room`.
4.  O `Room` detecta a alteração no banco e notifica automaticamente a UI através do `Flow`.

---

## Como Executar o Projeto

1.  Faça o clone deste repositório:
    ```bash
    git clone https://github.com
    ```
2.  Abra o projeto no **Android Studio** (versão Ladybug ou superior recomendada).
3.  Aguarde a sincronização completa do **Gradle**.
