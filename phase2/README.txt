# TransitNetworkManager

## Getting Started

```
$ git clone https://bitbucket.org/hotcupsofjava/transitnetworkmanager.git
```
Instructions to run the program:
1. Run the MainSystem and a system screen will appear which will allow the admin to either create a new transit system
or load an existing system.
2. After creating or loading a system, the admin will have to start the day by clicking on Start Day Button
for the users to travel.
3. The users can now login with their name and userId or sign up if they are new users which can be seen in users.txt.
4. After logging in, the users see a screen which displays all their cards, their name and balance.
5. They can either change their name, open a card for its information, and even add a new card for them.
6. Once a user opens a card, he can either suspend or remove that card or travel by going to the travel tab.
7. The user will select the station or bus stop they are travelling from and click on tap On or tap Off.
8. For bus stops, the users will be asked to input the route since the bus stops are part of multiple routes.
9. There is a special tap Off button which accelerates the time for checking if a new trip is created when a user
exceeds the 2 hrs limit while travelling if the cap < $6.
10. The user can load their card by selecting the Load Card tab and selecting either the default amounts or custom values.
11. The users can also check their 3 most recent trips in the Card Information column which is updated when refresh
button is clicked.
12. The admin can view the transit information for that particular day in the Transit Information tab.
13. The admin can also see the statistics for each station and each card in the system by clicking the Statistics tab.
14. There is another tab called Log which lets the admin see all the previous days logs and check the revenue and all
the statistics.
15. Finally the admin will end the day by clicking on End Day button and when the admin closes all the screens, the
data will be saved in the instances directory if the system was loaded or be created as a new system.

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
Creates routes that exist in the network. Routes can contain infinitely many stops and the system can have ,ultiple
routes.

Syntax:
```
routeId
routeId stopId1 stopId2 stopId3 stopIdx
```
#### users.txt
Creates users

Syntax:
```
userId Email Name
```

#### cards.txt
Creates cards for the existing users.

Syntax:
```
userId cardId1 cardId2 cardIdx
```