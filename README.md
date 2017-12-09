# hajo2
Second task for a course about distributed systems and cloud systems

## Mitä tässä oikeesti pitää tehdä

### 1. Java vaihtoehto (helpoin)

Toteutetaan kaikki mallikoodin TODO-kohdat, ne on todella hyvin selitetty kommenteissa.

1. Tehdään 'getCapabilitiesa' HTTP GET -kysely joka fetsaa XML muodossa karttatietoja
2. Parsitaan saadut karttatiedot ja Asetetaan niiden perusteella mahdolliset checkboxit käyttöliittymään
3. Toteutetaan nappulat muuttamaan getMap kyselyn parametreja niin että kuva liikkuu, zoomaa sisään ulos yms
4. Lähetetään erillisessä säikeessä getMap HTTP GET -kysely käyttäjän syöttämillä parametreilla, joka palauttaa kuvan joka vain näytetään käyttöliittymässä `imageLabel.setIcon(new ImageIcon(url));`
5. Muita UI muutoksia jos mieli tekee / koetaan tarpeellisiksi

### 2. Python / Scala / muu korkean tason kieli vaihtoehto (Vaikeahko)

Sama homma mutta käyttöliittymä toteutettava uusiksi esim PyQT:n avulla. Mulla ei ainakaan omakohtaista kokemusta oikeastaan mistään käyttöliittymä setistä mutta olen kuullut todella paljon hyvää QT kirjastosta.

Tämä vaatis paljon enemmän koodaamista mutta tulos olisi ihan vitusti parempi kuin esimerkkikoodin Java Swing ripuli.

### 3. Javascript + React webbistäcki (vaikein)

Sama homma kuin vaihtoehdossa 2. Koko koodi toteutettava uudelleen web tekniikoilla.

Eniten työtä mutta oikeasti hyödyllisin ja paras lopputulos. 
Ja mulla tosiaan on aika hyvä JS ja React osaaminen mut teistä en tiiä :D.

EDIT: onkohan tä liian raskas prokkis olemaan kaikki frontendissä? Mallikoodi ainakin sanoo että kartan päivitys tulisi tehdä omassa säikeessä. Oman Backendin tekeminen joka parsii XML:n voisi toisaalta olla ihan siisti homma.

Edit mun mielestä ois aika siistii toteuttaa sellanen backendi - I

EDITEDIT: Ei tä tarvii bäkkiä, ton HTTP GET kutsun siirtäminen toiseen säikeeeseen
perustuu mun mielestä vaan siihen ettei sovellus jumitu siksi ajaksi kun palvelimella menee
vastata HTTP pyyntöön. Tähän on super yksinkertanen ratkasu: tehdään HTTP pyyntö asynkronisesti - kuulostaa monimutkaselta mutta on oikeesti 0 markan homma. Googlaamalla esim 'async http requesst js' -> [Mozillan mielipiteitä asiasta](https://developer.mozilla.org/en-US/docs/Web/API/XMLHttpRequest/Synchronous_and_Asynchronous_Requests).
-------------------------

Tähän voitte lisäillä omiakin näkemyksiä jos kiinnostaa -Konsta
