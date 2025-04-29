
# Solution Homework 7
## Why Iterator pattern is preferred than using List<Episode>?
The Iterator pattern is preferred over exposing a List<Episode> because it encapsulates the internal structure of Series and Season, hiding how episodes are stored (e.g., List, Array, or other structures) from clients. It offers flexibility with custom iterators like
NormalIterator, ReverseIterator, ShuffleIterator, BingeIterator, SkipIntroIterator, and WatchHistoryIterator, enabling specialized traversal (forward, reverse, random, unwatched only) without modifying the collection. Iterators provide control for features like 
skipping intros or filtering watched episodes, which would be cumbersome with a raw List<Episode>. 

They also support extensibility, allowing new iterator types (e.g., genre-based or rating-based) without changing core classes, keeping the codebase modular. 

Additionally, iterators can optimize performance, such as lazy loading for large series, and reduce memory overhead compared to exposing an entire 
List<Episode>. Using a List<Episode> would expose implementation details, limit traversal options, and complicate adding features like custom ordering or filtering without altering client code.

## Why Mediator Pattern is Better than Aircraft Talking Directly?

The Mediator pattern, implemented via ControlTower, improves the airport simulation by centralizing communication, reducing complexity, and enhancing safety compared to aircraft communicating directly. However, it has a drawback: a single point of failure.

Advantages: The Mediator reduces coupling—aircraft only interact with ControlTower, not each other, simplifying their logic. For example, PassengerPlane AC1 requests runway access from the tower instead of negotiating with other aircraft like CargoPlane AC2. The tower also ensures safety by enforcing rules, such as prioritizing emergencies (e.g., MAYDAY) or low-fuel aircraft, preventing conflicts like simultaneous runway use.

Disadvantage: The Mediator introduces a single point of failure. If ControlTower fails, the entire system halts, as aircraft cannot coordinate without it. Direct communication might allow some negotiation, though inefficiently.

The Mediator pattern enhances safety and simplicity but risks system failure if the mediator fails, requiring careful design to mitigate this.
