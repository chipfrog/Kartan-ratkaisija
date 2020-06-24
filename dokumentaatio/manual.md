# Käyttöohjeet
Sovellus saattaa vaatia toimiakseen Java 11 mahdollisten JavaFX-ongelmien välttämiseksi.

## Sovelluksen suorittaminen
Lataa zip-tiedosto GitHubista ja pura se haluaamaasi paikkaan. Siirry konsolin kautta tähän sijaintiin ja siellä kansioon _Shortest-path-visualizer-master_.
Suorita nyt konsolissa komennot:

`./gradlew build`

`./gradlew run`

## Testien ajaminen

Testien suorittaminen konsolissa: `./gradlew test`

Jacoco-raportin generointi: `./gradlew test jacocoTestReport`

Jacocon generoiman testikattavuusraportin sijainti: _/build/reports/jacoco/test/html/index.html_

## Checkstyle
Konsolissa:
`./gradlew checkstyleMain`

Raportti löytyy sijainnista: _/build/reports/checkstyle_

## Sovelluksen käyttäminen
