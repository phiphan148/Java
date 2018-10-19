$(function () {

    // display text in the output area
    function gridCreate(tableId) {
        let table = document.getElementById(tableId);
        let thead = document.createElement("thead");
        let theadcontent = '';
        let firstrow = [" ", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
        for (let i = 0; i < firstrow.length; i++) {
            theadcontent += `<th>${firstrow[i]}</th>`
        }
        table.appendChild(thead).innerHTML = theadcontent;
        let tbody = document.createElement("tbody");
        let tbodycontent = '';
        let firstcol = [" ", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];
        for (let j = 1; j < firstrow.length; j++) {
            tbodycontent += `<tr>
                                <th id=${firstcol[j]}0>${firstcol[j]}</th>
                                <td id=${firstcol[j]}1></td>
                                <td id=${firstcol[j]}2></td>
                                <td id=${firstcol[j]}3></td>
                                <td id=${firstcol[j]}4></td>
                                <td id=${firstcol[j]}5></td>
                                <td id=${firstcol[j]}6></td>
                                <td id=${firstcol[j]}7></td>
                                <td id=${firstcol[j]}8></td>
                                <td id=${firstcol[j]}9></td>
                                <td id=${firstcol[j]}10></td>
                            </tr>`
        }
        table.appendChild(tbody).innerHTML = tbodycontent;
    }

    // load and display JSON sent by server for /players
    function loadData() {
        // var obj = {};
        // var reg = /(?:[?&]([^?&#=]+)(?:=([^&#]*))?)(?:#.*)?/g;
        // search.replace(reg, function(match, param, val) {
        //     obj[decodeURIComponent(param)] = val === undefined ? "" : decodeURIComponent(val);
        // });
        // return obj;
        let urlParams = new URLSearchParams(window.location.search);
        let gpId = urlParams.get('gp');
        fetch(`../api/game_view/${gpId}`)
            .then(response => response.json())
            .then((data) => {
                let gamePlayerData = data;
                let ships = gamePlayerData.mainPlayerShips;
                let salvos = gamePlayerData.mainPlayerSalvos;
                let shipsGetHit = gamePlayerData.opponentSalvos;

                document.getElementById("main-player").innerText = gamePlayerData.mainPlayer.email;
                document.getElementById("created").innerText = convertDate(gamePlayerData.game.created);
                if (gamePlayerData.opponent.length == 0) {
                    document.getElementById("opponent").innerText = "There is no opponent"
                } else {
                    for (let i = 0; i < gamePlayerData.opponent.length; i++) {
                        document.getElementById("opponent").innerText = gamePlayerData.opponent[i].email;
                    }
                }

                let shipLocation = [];
                ships.forEach(ship => ship.shipLocation.forEach(location => shipLocation.push(location)));
                console.log(shipLocation);
                if (shipLocation.length > 0) {
                    for (let i = 0; i < shipLocation.length; i++) {
                        document.getElementById("ship-grid").querySelector(`#${shipLocation[i]}`).style.background = "blue";
                    }
                }

                if (shipsGetHit.length > 0) {
                    for (let i = 0; i < shipsGetHit.length; i++) {
                        let shots = shipsGetHit[i].turn;
                        for (let j = 0; j < shipsGetHit[i].location.length; j++) {
                            if(shipLocation.includes(shipsGetHit[i].location[j])){
                                document.getElementById("ship-grid").querySelector(`#${shipsGetHit[i].location[j]}`).style.background = "red";
                                document.getElementById("ship-grid").querySelector(`#${shipsGetHit[i].location[j]}`).innerHTML = shots;
                            }
                        }
                    }
                }

                if (salvos.length > 0) {
                    for (let i = 0; i < salvos.length; i++) {
                        let shots = salvos[i].turn;
                        for (let j = 0; j < salvos[i].location.length; j++) {
                            document.getElementById("salvo-grid").querySelector(`#${salvos[i].location[j]}`).style.background = "green";
                            document.getElementById("salvo-grid").querySelector(`#${salvos[i].location[j]}`).innerHTML = shots;
                        }
                    }
                }
            })
            .catch(err => console.log(err))
    }

    function convertDate(date) {
        date = new Date(date);
        return [pad(date.getDate()), pad(date.getMonth() + 1), pad(date.getFullYear())].join("/");
    }

    function pad(s) {
        return (s < 10) ? '0' + s : s;
    }

    loadData();
    gridCreate("ship-grid");
    gridCreate("salvo-grid");
});