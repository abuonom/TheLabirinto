# Progetto: Algoritmo di Cammino per un Robot in un Labirinto

## Studente

- **Cognome:** Buonomo
- **Nome:** Alessio
- **Matricola:** 0124002066
- **Codice gruppo:** ow987yljysb
- **Componenti gruppo:** 1

## Descrizione del Progetto

Questo progetto prevede lo sviluppo di un algoritmo per il cammino di un robot all'interno di un labirinto. La stanza del labirinto è pavimentata a tasselli quadrati (caselle) ed è dotata di pareti esterne. All'interno della stanza sono presenti degli ostacoli (m, scelto dall'utente) che cambiano disposizione ad ogni passo. Il robot si muove di una casella alla volta tra le otto caselle vicine. La simulazione termina quando il robot esce dalla porta della stanza.

### Livelli di Difficoltà
Ci sono tre livelli di difficoltà che variano a seconda del numero di ostacoli (m crescente).

### Strategie di Movimento del Robot
Il robot utilizza tre strategie per muoversi:
1. **Movimento Casuale (30%)**: Il robot si muove a caso in una delle quattro caselle vicine possibili (parete permettendo).
2. **Movimento Deciso dal Giocatore (30%)**: Il giocatore decide la direzione del movimento.
3. **Algoritmo A** **(40%)**: La direzione del robot viene calcolata usando l’algoritmo A*.

### Algoritmo A*
L'algoritmo A* è un algoritmo di ricerca su grafi che individua un percorso da un nodo iniziale a un nodo goal, utilizzando una stima euristica per classificare ogni nodo. L'algoritmo visita i nodi in base a questa stima euristica. Lo pseudocodice dell'algoritmo è riportato nella sezione sottostante.

### Pseudocodice dell'Algoritmo A*

```text
function A*(start,goal)
    closedset := the empty set
    openset := set containing the initial node
    g_score[start] := 0
    came_from := the empty map
    h_score[start] := heuristic_estimate_of_distance(start, goal)
    f_score[start] := h_score[start]
    while openset is not empty
        x := the node in openset having the lowest f_score[] value
        if x = goal
            return reconstruct_path(came_from,goal)
        remove x from openset
        add x to closedset
        foreach y in neighbor_nodes(x)
            if y in closedset
                continue
            tentative_g_score := g_score[x] + dist_between(x,y)
            if y not in openset
                add y to openset
                tentative_is_better := true
            elseif tentative_g_score < g_score[y]
                tentative_is_better := true
            else
                tentative_is_better := false
            if tentative_is_better = true
                came_from[y] := x
                g_score[y] := tentative_g_score
                h_score[y] := heuristic_estimate_of_distance(y, goal)
                f_score[y] := g_score[y] + h_score[y]
    return failure

function reconstruct_path(came_from,current_node)
    if came_from[current_node] is set
        p = reconstruct_path(came_from,came_from[current_node])
        return (p + current_node)
    else
        return the empty path
```

## Requisiti di Sviluppo

- **Linguaggio:** Java
- **Principi di Progettazione:** SOLID
- **Pattern di Design:** Utilizzo di almeno due design pattern (almeno uno per applicazioni web).
- **Gestione delle Eccezioni:** Implementata
- **Persistenza:** Uso di file o database
- **Commenti e Annotazioni:** Inclusi per Javadoc
- **Interfaccia Grafica:** Utilizzo di strumenti come GUI Designer (IntelliJ, WindowsBuilder) o tools compatibili con JavaFx come Scene Builder.

### Design Patterns Utilizzati

Per il progetto sono stati selezionati i seguenti due design pattern:

### 1. **Strategy Pattern**
**Scopo: Comportamentale (Object)**

Il **Strategy Pattern** è stato utilizzato per implementare le diverse strategie di movimento del robot. Ogni strategia di movimento (movimento casuale, deciso dal giocatore, algoritmo A*) è stata implementata come una classe separata che implementa una comune interfaccia `MovementStrategy`. Questo permette di cambiare facilmente la strategia di movimento del robot senza modificare il codice che utilizza queste strategie.

**Implementazione:**
- Creata un'interfaccia `MovementStrategy` con un metodo `move()`.
- Implementate le classi `RandomMovement`, `PlayerMovement`, e `AStarMovement` che implementano l'interfaccia `MovementStrategy`.
- La classe `Robot` ha un riferimento a un'istanza di `MovementStrategy` e utilizza il metodo `move()` per decidere il movimento.

### 2. **Builder Pattern**
**Scopo: Creazionale (Object)**

Il **Builder Pattern** è stato utilizzato per la configurazione iniziale del labirinto, inclusa la disposizione delle caselle e degli ostacoli.

**Implementazione:**
- Creata una classe `MazeBuilder` con metodi per configurare diversi aspetti del labirinto (dimensioni, ostacoli, ecc.).
- Implementato un metodo `build()` che restituisce l'istanza di `Maze`.
- La classe `Maze` contiene la struttura del labirinto e i metodi per gestire gli ostacoli e la posizione del robot.

## Consegna del Progetto

Il progetto deve essere consegnato tramite email ai docenti o tramite Microsoft Teams. L'archivio consegnato deve includere:

1. **Relazione sintetica**: Descrizione dei requisiti del progetto, diagramma UML delle classi, altri diagrammi se opportuni, parti rilevanti del codice sviluppato.
2. **Codice sorgente**: Completamente commentato e documentato.
3. **Presentazione sintetica**: Slide per descrivere il progetto svolto.

### Relazione Sintetica
La relazione deve essere preparata secondo il template disponibile sulla piattaforma e-learning o su Overleaf: [Overleaf Template](https://www.overleaf.com/).

---

Questi pattern garantiranno una progettazione modulare e flessibile, facilitando l'estensione e la manutenzione del progetto.