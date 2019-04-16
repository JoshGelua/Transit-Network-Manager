# TransitNetworkManager

## Getting Started

```
$ git clone https://bitbucket.org/hotcupsofjava/transitnetworkmanager.git
```

### Configuration
All files must be in the `config` folder.
Files are explained in order of execution.

#### stops.txt
Creates all the bus stops that exist in the network.

Syntax:
```stopId Name Of Stop```

#### stations.txt
Creates all stations that exist in the network and has the option of connecting to a pre-existing bus stop.

Syntax:
```
stationId Name Of Station
stationId Name Of Station connects stopId
```

#### routes.txt
Creates routes that exist in the network. Routes can contain infinitely many stops.

Syntax:
```
routeId
routeId stopId1 stopId2 stopId3 stopIdx
```
### events.txt
Contains events which will be read and run during the simulation.

User input:
    - User can create a new card.
    syntax:
            timeStamp "user" userId "newCard" cardId
    - User can load money to their card.
    syntax:
            timeStamp "user" userId "load" cardId amount
    - User can suspend his card if the card is stolen.
    syntax:
        timeStamp "user" userId "suspend" cardId
    - User can view their 3 recent trips.
    syntax:
            timeStamp "user" userId "viewRecentTrips" cardId
    - User can change his name.
    syntax:
            timeStamp "user" userId "changeName" newName

Card input:
    - When a card is tapped on at a stop:
        - If the card was tapped on at a BusStop:
            syntax:
                timesStamp "card" id "tapOn" "busStop" stopId routeId
        - If the card was tapped on at a Subway Station:
            syntax:
                timeStamp "card" id "tapOn" "station" stationId
    - When a card is tapped off at a stop:
        - If the card was tapped off at a BusStop:
                    syntax:
                        timesStamp "card" id "tapOff" "busStop" stopId routeId
                - If the card was tapped off at a Subway Station:
                    syntax:
                        timeStamp "card" id "tapOff" "station" stationId
## Authors


