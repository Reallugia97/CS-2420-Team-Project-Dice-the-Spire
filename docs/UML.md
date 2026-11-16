```mermaid
classDiagram
    direction TB

    class JFrame {
        <<Swing>>
    }

    class Digraph {
        <<algs4>>
        +V() int
        +E() int
        +addEdge(int v, int w) void
        +adj(int v) Iterable~Integer~
    }

    class GameGUI {
        -MapGenerator mapGenerator
        -Player player
        +GameGUI()
        +startGame() void
        +showMap() void
        +showEvent(Event event) void
        +updateHUD() void
    }

    class MapGenerator {
        -Digraph map
        -int floors
        -int width
        -Map~Integer, Event~ nodeEvents
        +generate() Digraph
        +getEvent(int vertex) Event
        +getNextNodes(int vertex) List~Integer~
    }

    class Player {
        -String name
        -int maxHealth
        -int currentHealth
        -int gold
        -int currentNode
        +takeDamage(int amount) void
        +heal(int amount) void
        +addGold(int amount) void
        +moveTo(int vertex) void
        +isAlive() boolean
    }

    class Event {
        <<abstract>>
        #EventType type
        +execute(Player player) void
        +getType() EventType
    }

    class MinionEvent {
        -List~Enemy~ enemies
        +execute(Player player) void
    }

    class EliteEvent {
        -Enemy elite
        +execute(Player player) void
    }

    class BossEvent {
        -Enemy boss
        +execute(Player player) void
    }

    class ShopEvent {
        -List~RewardItem~ stock
        +execute(Player player) void
        +buy(Player player, RewardItem item) boolean
    }

    class MysteryEvent {
        +execute(Player player) void
    }

    class RestEvent {
        -int healPercent
        +execute(Player player) void
    }

    class Enemy {
        -String name
        -EnemyType tier
        -int maxHealth
        -int currentHealth
        -int attackPower
        +takeDamage(int amount) void
        +chooseAction() Action
        +isDead() boolean
    }

    class Reward {
        -Map~EventType, List~RewardItem~~ pools
        +rollReward(EventType type) List~RewardItem~
        +rollGold(EventType type) int
    }

    class RewardItem {
        -String name
        -String description
    }

    class EventType {
        <<enumeration>>
        MINION
        ELITE
        SHOP
        BOSS
        MYSTERY
        REST
    }

    class EnemyType {
        <<enumeration>>
        MINION
        ELITE
        BOSS
    }

    JFrame <|-- GameGUI
    GameGUI --> MapGenerator : uses
    GameGUI --> Player : owns
    GameGUI ..> Event : displays

    MapGenerator o-- Digraph : graph
    MapGenerator ..> Event : creates

    Event <|-- MinionEvent
    Event <|-- EliteEvent
    Event <|-- BossEvent
    Event <|-- ShopEvent
    Event <|-- MysteryEvent
    Event <|-- RestEvent
    Event --> EventType
    Event ..> Player : modifies

    MinionEvent --> Enemy
    EliteEvent --> Enemy
    BossEvent --> Enemy
    Enemy --> EnemyType

    MinionEvent ..> Reward
    EliteEvent ..> Reward
    BossEvent ..> Reward
    ShopEvent ..> Reward
    MysteryEvent ..> Reward

    Reward --> RewardItem
    Reward ..> EventType
```
