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

## Authors


