Nume: Matisan Razvan-Andrei
Grupa: 324CA
Email: razvan.matisan@stud.acs.upb.ro

				Tema 1 POO - VideoDB
                                                                               

# Link-ul catre repo-ul de git:

https://github.com/razvanmatisan/VideoDB

# Detalii despre implementare

1. Informatii despre datele de intrare

  * In cadrul temei, pentru a nu strica in vreun fel clasele din schelet
care se ocupa cu parsarea datelor de input, mi-am creat alte clase, unde am
putut sa-mi definesc alte campuri si metode pentru rezolvarea cerintelor.

  * Astfel, clasele nou create sunt:
    - User (creata dupa modelul clasei UserInputData)
    - Video (creata dupa modelul clasei ShowInput)
    - Serial (creata dupa modelul clasei SerialInput), clasa copil a clasei
abstracte Video
    - Movie (creata dupa modelul clasei MovieInput), clasa copil a clasei
abstracte Video
    - Actor (creata dupa modelul clasei ActorInput)

  * In plus, am creat o clasa numita Conversion, ce imi converteste un obiect
de tip XInputData intr-unul de tip X (unde X este numele unei clase din cele
cinci de mai sus, mai putin clasa Video).

  * Pentru a stoca toate datele de intrare in functie de tipul lor, mi-am creat,
in plus, 3 clase cu rol de baza de date:
    - ActorDB (contine un camp de tip Hashmap ce retine toti actorii)
    - VideoDB (contine 2 campuri de tip ArrayList (unul pentru movies, altul
pentru serials))
    - UserDB (contine un camp de tip Hashmap ce retine toti userii)

  * Pentru a retine ordinea unui video din fisierul de intrare, am creat o
metoda setIndexDatabase() ce-mi seteaza cate un index pentru fiecare video si
care este apelata in Main, imediat ce am stocat in "bazele de date" toate
datele.


2. Informatii despre actiuni

  * Pentru a chema o actiune, m-am folosit de un Design Pattern de tip Factory.

  * ActionFactory este o clasa care imi creeaza un obiect de tip Action, ce
poate fi Command, Query sau Recommendation.

  * Intefata Action contine o metoda de callAction (implementata diferit in
clasele Command, Query si Recommendation) care primeste un obiect de tip
fileWriter si imi returneaza obiectul JSON de care am nevoie.

  * Functionalitatea tuturor actiunilor se gaseste in clasele amintite la
punctul 1) in acest mod:
    - comenzile si recomandarile au fost implementate in clasa User
(caci ele sunt un tip de actiune ce depinde de fiecare user in parte)
    - query-urile au fost implementate in clasele ActorDB, VideoDB, respectiv
UserDB (in functie de tipul de query avut)


3. Calcularea unor proprietati pentru rezolvarea cerintelor

  * Pentru rating
    - in clasa User, am creat un HashMap care imi retine video-urile pentru
care un utilizator a dat rating, precum si sezoanele aferente. Daca video-ul
este un Movie, atunci in campul destinat sezoanelor este trecut numarul 0.
    - De asemenea, tot in clasa User a fost tinuta evidenta numarului de
rating-uri, care este updatat de fiecare data cand un utilizator da un rating.

    - clasele Movie si Season retin fiecare cate o lista de rating uri, care
se actualizeaza de fiecare data cand un utilizator da un rating. In plus,
clasele Movie si Serial au fiecare cate un camp care retine rating-ul per tot
Movie-ul/Serial-ul si care se calculeaza cu ajutorul unei metode abstracte din
clasa parinte Video.

    - clasa Actor retine si ea un camp pentru rating, care se determina
calculand media aritmetica a tuturor rating-urilor din Show-urile in care
acesta a jucat.

  * Pentru favorite
    - clasa abstracta Video contine un camp "numberOfFavorites" care retine
numarul de aparitii ale unui video in listele de favorites ale tuturor userilor.
Astfel, pentru fiecare video, se cauta toate aparitiile sale in lista de
favorites a tuturor userilor in parte.

  * Pentru numarul de vizualizari
    - clasa abstracta Video contine un camp "numberViews" care retine
numarul de dati in care un video a fost vizualizat de catre toti utilizatorii.

  * Pentru numarul de awards
    - clasa Actor are un camp numberAwards in care retine numarul total
de premii castigate de catre un actor

  * Pentru durata unui Video
    - clasa Movie avea predefinit acest camp, in schimb clasa Serial nu. In
cazul serialelor, acest camp se calculeaza prin insumarea duratelor tuturor
sezoanelor.


4. Sortari

  * Pentru implementarea si apelarea sortarilor, am folosit de 4 ori
un design pattern de tip Strategy, clasificand sortarile astfel:
    - sortari pentru actori (de la query-uri)
    - sortari pentru video-uri (de la query-uri)
    - sortari pentru useri (de la query-uri)
    - sortari pentru recomandari
