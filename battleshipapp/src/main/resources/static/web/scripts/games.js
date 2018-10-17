$(function () {

    // display text in the output area
    function showOutput(data1, data2) {
        let li = document.createElement("li");
        let liTxt1 = document.createTextNode(`Game: ${data1}, `);
        let liTxt2;
        if (data2.length != 0) {
            liTxt2 = document.createTextNode(`Players: ${data2}`);
        } else {
            liTxt2 = document.createTextNode('There is no player for this game');
        }
        li.appendChild(liTxt1);
        li.appendChild(liTxt2)
        document.getElementById("games").appendChild(li);
    }

    // load and display JSON sent by server for /players
    function loadData() {
        fetch("../api/games")
            .then(response => response.json())
            .then((data) => {
                let gameData = data;
                gameData.forEach(map =>
                    showOutput(
                        [pad(new Date(Date.parse(map.created)).getDate()),
                            pad(new Date(Date.parse(map.created)).getMonth() + 1),
                            pad(new Date(Date.parse(map.created)).getFullYear())].join('/'),
                        map.gamePlayers.map(gameplayer => gameplayer.player.email))
                );
            })
            .catch(err => console.log(err))
    }

    function pad(s) {
        return (s < 10) ? '0' + s : s;
    }

    loadData();
});