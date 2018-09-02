# Versione finale di TempStation
Rispetto le [versioni precedenti](https://github.com/FrancescoTaurino/TempStation), quest'ultima versione introduce un'applicazione per smartphone (*companion application*), tramite la quale è possibile controllare il comportamento del Raspberry da remoto.

In particolare: tramite l'applicazione per smartphone è possibile accendere/spegnere il sensore di temperatura del Rainbow HAT. Quando acceso, il sensore cattura ripetutamente la temperatura corrente e la comunica realtime allo smartphone, il quale le mostra all'utente all'interno di una lista.

## Applicazione per Smartphone
L'applicazione per smartphone si chiama `TempStationRedux` e, per il suo sviluppo, sono state utilizzate diverse tecnologie.

1. **Linguaggio di programmazione.**
La scrittura del codice sorgente non è stata realizzata tramite Java/Android, bensì tramite [Flutter](https://flutter.io/).
Flutter è un framework (realizzato da Google e attualmente in *Release Preview*) che permette di sviluppare applicazioni mobile per iOS e Android, utilizzando un singolo linguaggio di programmazione: [Dart](https://www.dartlang.org/). 

2. **Pattern archittetturale.**
L'applicazione è strutturata utilizzando [Redux](https://redux), design pattern nato per JavaScript ma ben presto esteso a molti altri linguaggi di programmazione, tra cui Dart e Flutter ([Redux](https://pub.dartlang.org/packages/redux) per Dart e [Redux](https://pub.dartlang.org/packages/flutter_redux) per Flutter).
Redux si basa su cinque componenti:
	- `State`: rappresenta lo stato dell'applicazione, il quale è un oggetto immutabile, ovvero di sola lettura. Se lo stato dell'applicazione cambia, non viene modificato lo stato corrente ma ne niene generato uno completamente nuovo;
	- `Store:` contiene lo stato dell'applicazione ed è unico. In questo modo esiste una sola fonte di informazioni all'interno dell'applicazione;
	- `Action`: semplice oggetto che rappresenta la necessità di effettuare un aggiornamento nello `Store`. In una applicazione più `Actions` sono definite;
	- `Reducer`:  funzione pura che prende in input lo stato  corrente dell'applicazione e un'azione e, sulla base di questi, genera un nuovo stato. La UI viene aggiornata ogni volta che un nuovo stato viene generato;
	- `Middleware`:  funzione che effettua delle 'operazioni di contorno' (ad esempio: *logging*, *crash reporting*, *chiamate asincrone*), prima che l'azione venga processata dal `Reducer`.

	L'immagine che segue mostra schematicamente in che modo i componenti interagiscono tra loro.
	<p align="center"> <img src="https://image.ibb.co/nKBP4e/Redux_Flow_Chart.png"></p>

3. **Principali dipendenze.**
La sicurezza e la comunicazione tra le due applicazioni è stata realizzata usando [Firebase Authentication](https://firebase.google.com/docs/auth/) e [Firebase Realtime Database](https://firebase.google.com/docs/database/):
	- **Firebase Authentication** fornisce più meccanismi per autenticare l'utente che utilizza l'applicazione, ad esempio tramite Email/Password, numero di telefono, Google Sign-In, Facebook, Twitter e altri;
	- **Firebase Realtime Database** fornisce un database online dove salvare i propri dati, con la caratteristica fondamentale che ogni volta che si accede in scrittura, tutti i dispositivi in ascolto vengono notificati realtime del cambiamento.

	Il processo di autenticazione permette di conoscere l'identità dell'utente, la quale viene rappresentata con uno **uid**.

	La console di Firebase permette di visualizzare gli account autorizzati. Nel caso di `TempStationRedux`, solo un'account è autenticato ed in grado di fare il login correttamente.
	
	<p align="center"> <img src="https://image.ibb.co/b2t2qK/auth_console.png"></p>

	La prima schermata dell'applicazione permette di effettuare il login tramite Email e Password e quindi di accedere alla lista delle temperature. 
	  <p align="center"> <img src="https://image.ibb.co/hJNbje/login.gif"></p>

	L'autenticazione è necessaria al fine di di garantire un accesso autorizzato al Database. Infatti, è possibile stabilire delle regole di sicurezza (dette [Firebase Realtime Database Rules](https://firebase.google.com/docs/database/security/)) che decidono chi può accedere in lettura e/o scrittura al Database. Tali regole vengono specificate nella console di Firebase. 
	
	<p align="center"> <img src="https://image.ibb.co/gb23VK/fb_rules.png"></p>

	Ovvero: l'accesso in lettura e scrittura al Database è ammesso solo ad un utente autenticato e con un determinato **uid**.

	Infine, la struttura interna del Database è la seguente.
	
	<p align="center"> <img src="https://image.ibb.co/hG1iEe/fb_db.png"></p>

	In particolare:
	- `isStarted`: valore booleano che determina se il sensore di temperatura del Rainbow HAT è acceso o spento;
	- `measurements`: lista contenente le misurazioni rilevate dal sensore. Ogni misurazione è identificata da una (stringa generata random) ed è caratterizzata da un timestamp ed il valore della temperatura.

## `TempStationV3` e `TempStationRedux` Flowchart
Il diagramma che segue riassume ed illustra (ad alto livello) in che modo le due applicazioni interagiscono tra di loro.

<p align="center"> <img src="https://image.ibb.co/moSFue/Temp_Station_Flow_Chart_2.png"></p>

In particolare:
1. I riquadri in rosso rappresentano operazioni che vengono effettuate esternamente dal dispositivo;
2. Le frecce tratteggiate in rosso rappresentano una notifica di cambiamento all'interno del Database.

## `TempStationV3` UML Sequence Diagram
Il sequence diagram che segue illustra come le classi di `TempStationV3` interagiscono tra loro. Il diagramma si concentra sull'evidenziare come gli *Android Architecture Components* si adeguino correttamente alle esigenze di programmazione che pone Android Things (la parte codice dedicata a Firebase Auth viene quindi tralasciata).

<p align="center"> <img src="https://image.ibb.co/fxA29e/Temp_Station_V3_Sequence_Diagram_8.png"></p>

In particolare, si noti che `MainActivityViewModel` viene creato in `MainActivity` ma sopravvive (ovvero, non viene ricreato) ad ogni suo cambio di stato / configurazione. Il ciclo di vita del `ViewModel` è illustrato nell'immagine che segue.

<p align="center"> <img src="https://image.ibb.co/mpVNaK/viewmodel_lifecycle.png"></p>
