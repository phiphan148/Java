function mainGame() {
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
            let mainPlayerShips = gamePlayerData.mainPlayerShips;
            let mainPlayerSalvos = gamePlayerData.mainPlayerSalvos;
            let opponentSalvos = gamePlayerData.opponentSalvos;
            let opponentShipGetHit = gamePlayerData.opponentShipGetHit;

            if (gamePlayerData.mainPlayer == null) {
                window.location.href = "../web/games.html";
            } else {
                document.getElementById('ships-to-add').innerHTML = `<button onclick="createShip(${gpId})">Add ship</button>`;

                gridCreate("ship-grid");
                gridCreate("salvo-grid");
                displayInfo(gamePlayerData);
                displayGridShip(mainPlayerShips, opponentSalvos);
                displaySalvoGrid(mainPlayerSalvos, opponentShipGetHit);
            }

        })
        .catch(err => console.log(err))
}

mainGame();

function displayInfo(gamePlayerData) {
    document.getElementById("main-player").innerText = gamePlayerData.mainPlayer.email;
    document.getElementById("created").innerText = convertDate(gamePlayerData.game.created);
    if (gamePlayerData.opponent.length == 0) {
        document.getElementById("opponent").innerText = "There is no opponent"
    } else {
        for (let i = 0; i < gamePlayerData.opponent.length; i++) {
            document.getElementById("opponent").innerText = gamePlayerData.opponent[i].email;
        }
    }
}

function convertDate(date) {
    date = new Date(date);
    return [pad(date.getDate()), pad(date.getMonth() + 1), pad(date.getFullYear())].join("/");
}

function pad(s) {
    return (s < 10) ? '0' + s : s;
}

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
                                <th class=${firstcol[j]}0>${firstcol[j]}</th>
                                <td tabindex="1" onclick="chooseLocation(this)" data-classname="${firstcol[j]}1" class=${firstcol[j]}1></td>
                                <td tabindex="1" onclick="chooseLocation(this)" data-classname="${firstcol[j]}2" class=${firstcol[j]}2></td>
                                <td tabindex="1" onclick="chooseLocation(this)" data-classname="${firstcol[j]}3" class=${firstcol[j]}3></td>
                                <td tabindex="1" onclick="chooseLocation(this)" data-classname="${firstcol[j]}4" class=${firstcol[j]}4></td>
                                <td tabindex="1" onclick="chooseLocation(this)" data-classname="${firstcol[j]}5" class=${firstcol[j]}5></td>
                                <td tabindex="1" onclick="chooseLocation(this)" data-classname="${firstcol[j]}6" class=${firstcol[j]}6></td>
                                <td tabindex="1" onclick="chooseLocation(this)" data-classname="${firstcol[j]}7" class=${firstcol[j]}7></td>
                                <td tabindex="1" onclick="chooseLocation(this)" data-classname="${firstcol[j]}8" class=${firstcol[j]}8></td>
                                <td tabindex="1" onclick="chooseLocation(this)" data-classname="${firstcol[j]}9" class=${firstcol[j]}9></td>
                                <td tabindex="1" onclick="chooseLocation(this)" data-classname="${firstcol[j]}10" class=${firstcol[j]}10></td>
                            </tr>`
    }
    table.appendChild(tbody).innerHTML = tbodycontent;
}

function displayGridShip(ships, opponentSalvos) {
    let shipLocation = [];
    ships.forEach(ship => ship.shipLocation.forEach(location => shipLocation.push(location)));
    console.log(shipLocation);
    if (shipLocation.length > 0) {
        for (let i = 0; i < shipLocation.length; i++) {
            document.getElementById("ship-grid").querySelector(`.${shipLocation[i]}`).style.background = "blue";
        }

    }

    if (opponentSalvos.length > 0) {
        for (let i = 0; i < opponentSalvos.length; i++) {
            let shots = opponentSalvos[i].turn;
            for (let j = 0; j < opponentSalvos[i].location.length; j++) {
                if (shipLocation.includes(opponentSalvos[i].location[j])) {
                    document.getElementById("ship-grid").querySelector(`.${opponentSalvos[i].location[j]}`).style.background = "red";
                    document.getElementById("ship-grid").querySelector(`.${opponentSalvos[i].location[j]}`).innerHTML = shots;
                }
            }
        }
    }
}

function displaySalvoGrid(salvos, opponentShipGetHit) {
    if (salvos.length > 0) {
        for (let i = 0; i < salvos.length; i++) {
            let shots = salvos[i].turn;
            for (let j = 0; j < salvos[i].location.length; j++) {
                document.getElementById("salvo-grid").querySelector(`.${salvos[i].location[j]}`).style.background = "green";
                document.getElementById("salvo-grid").querySelector(`.${salvos[i].location[j]}`).innerHTML = shots;
                if (opponentShipGetHit.includes(salvos[i].location[j])) {
                    document.getElementById("salvo-grid").querySelector(`.${salvos[i].location[j]}`).style.background = "red";
                }
            }
        }
    }
}

function checkLoginForGame() {
    fetch("../api/games")
        .then(response => response.json())
        .then((data) => {
            let userData = data.currentPlayer;
            if (userData != null) {
                document.getElementById("playerLoginName").innerText = userData.email;
                document.getElementById("welcome").style.display = "block";
            } else {
                document.getElementById("welcome").style.display = "none";
            }
        })
        .catch(err => alert(err))
}

function logout() {
    $.post("/api/logout")
        .done(function () {
            window.location.href = "../web/games.html";
            console.log("logged out");
        })
        .fail(err => alert(err.responseText));
}

var shipChooseType = "";
var shipLength = 0;

function getShipType() {
    let radioCheckList = document.getElementsByName("ship");
    shipChooseType = "";
    shipLength = 0;
    radioCheckList.forEach(check => {
        if (check.checked == true) {
            shipChooseType = check.getAttribute("data-shiptype");
            shipLength = parseInt(check.getAttribute("data-shiplength"));
        }
    });
}

var shipDirecttion = "";

function getDirection() {
    let radioCheckList = document.getElementsByName("direction");
    shipDirecttion = "";
    radioCheckList.forEach(check => {
        if (check.checked == true) {
            shipDirecttion = check.getAttribute("data-direction");
        }
    });
}

var shipBow = "";
function chooseLocation(className) {
    shipBow = "";
    if(className.style.background != "blue"){
        shipBow = className.getAttribute("data-classname");
        console.log(className);
        console.log(shipBow);
    }
}

function createShip(gamePlayerId) {
    if (shipChooseType != "" && shipDirecttion != "" && shipBow != "") {
        let location = [];
        if (shipDirecttion == 'horizontal') {
            let num = parseInt(shipBow.charAt(1));
            console.log(num);
            console.log(shipLength);
            if (num + shipLength > 11) {
                alert("Your ship location is out of grid, please place ship again");
            } else {
                for (let i = 0; i < shipLength; i++) {
                    location.push(shipBow.charAt(0).concat(num + i));
                }
            }
            console.log(shipLength);
            console.log(location);
        } else {
            let letter = shipBow.charCodeAt(0);
            if (letter + shipLength > 75) {
                alert("Your ship location is out of grid, please place ship again");
            } else {
                for (let i = 0; i < shipLength; i++) {
                    location.push(String.fromCharCode(letter + i).toUpperCase().concat(shipBow.charAt(1)));
                }
            }
            console.log(letter);
            console.log(shipLength);
        }
        if (location != "") {
            let data = {shipType: `${shipChooseType}`, location: location};
            $.post({
                url: `/api/games/players/${gamePlayerId}/ships`,
                data: JSON.stringify(data),
                dataType: "text",
                contentType: "application/json"
            })
                .done(jqXHR => window.location.href = `../web/game.html?gp=${gamePlayerId}`)
                .fail((jqXHR, status) => alert(status + " " + jqXHR.responseText))
        }
    } else {
        alert('Please make sure you choose ship type, direction and location');
    }


}


//JAVASCRiPT POST
// function createShip(gamePlayerId) {
//     let data = {shipType: "destroyer", location:["A1", "B1", "C1"]};
//     fetch(`/api/games/players/${gamePlayerId}/ships`, {
//         method: 'POST',
//         body: JSON.stringify({data}),
//         headers: new Headers({
//             contentType: 'application/json'
//         })
//     })
//         .then(response => console.log("Request complete! response:", response))
//         .catch(error => alert(error))
// }