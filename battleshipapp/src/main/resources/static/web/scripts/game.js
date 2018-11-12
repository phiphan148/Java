var mainPlayerShipsLeftTurn = [];
function mainGame() {
    let urlParams = new URLSearchParams(window.location.search);
    let gpId = urlParams.get('gp');
    fetch(`../api/game_view/${gpId}`)
        .then(response => response.json())
        .then((data) => {
            let gamePlayerData = data;
            if(gamePlayerData.mainPlayerShipLeftFINAL != null){
                mainPlayerShipsLeftTurn = gamePlayerData.turnHistory[gamePlayerData.turnHistory.length - 1].mainPlayerShipsLeft;
                console.log(mainPlayerShipsLeftTurn);
            } else {
                mainPlayerShipsLeftTurn = gamePlayerData.mainPlayerShips;
                console.log(mainPlayerShipsLeftTurn);
            }
            if (gamePlayerData.mainPlayer == null) {
                window.location.href = "../web/games.html";
            } else {
                let mainPlayerShips = gamePlayerData.mainPlayerShips;
                console.log(mainPlayerShips);
                let mainPlayerSalvos = gamePlayerData.mainPlayerSalvos;
                let opponentSalvos = gamePlayerData.opponentSalvos;
                let mainPlayerShipType = mainPlayerShips.map(ship => ship.shipType);
                console.log(mainPlayerShipType);
                console.log(mainPlayerShips);
                let finalScore = null;

                let radioCheckList = document.getElementsByName("ship");
                radioCheckList.forEach(check => {
                    if (mainPlayerShipType.includes(check.getAttribute("data-shiptype"))) {
                        check.setAttribute("disabled", "true");
                    }
                });
                displayInfo(gamePlayerData);
                displayTurnHistory(gamePlayerData);
                if(gamePlayerData.gameOver == false){
                    document.getElementById("ships-and-direction").style.display = "block";
                    document.getElementById("grids").style.display = "flex";
                    if (mainPlayerShips.length < 5) {
                        document.getElementById("turn-history").style.display = "none";
                        document.getElementById("salvo-div").style.display = "none";
                        gridCreate("ship-grid");
                        displayGridShip(mainPlayerShips, opponentSalvos);
                        document.getElementById('ships-to-add').innerHTML = `<button class="button-style" onclick="createShip(${gpId})">Add ship</button>`;
                    } else {
                        document.getElementById("turn-history").style.display = "inline-table";
                        document.getElementById("salvo-div").style.display = "block";
                        document.getElementById("ships-and-direction").style.display = "none";
                        gridCreate("ship-grid");
                        displayGridShip(mainPlayerShips, opponentSalvos);
                        gridCreate("salvo-grid");
                        // setInterval(function () {window.location.href = `../web/game.html?gp=${gpId}`}, 100000);
                        if (mainPlayerSalvos.length > opponentSalvos.length) {
                            document.getElementById('salvos-to-add').innerHTML = '<p class="titleTxt-color">Please wait for your turn</p>';
                        } else {
                            document.getElementById('salvos-to-add').innerHTML = `<button class="button-style" onclick="addSalvo(${gpId})">Add salvo</button>`;
                        }
                        displaySalvoGrid(mainPlayerSalvos);
                    }
                } else {
                    document.getElementById("ships-and-direction").style.display = "none";
                    document.getElementById("grids").style.display = "none";
                }
            }

            if (gamePlayerData.gameOver == true) {
                if (gamePlayerData.mainPlayerShipLeftFINAL > gamePlayerData.opponentShipLeftFINAL) {
                    finalScore = 1;
                } else if (gamePlayerData.mainPlayerShipLeftFINAL == gamePlayerData.opponentShipLeftFINAL) {
                    finalScore = 0.5;
                } else {
                    finalScore = 0;
                }
                let score = {score: finalScore};
                $.post({
                    url: `/api/games/players/${gpId}/scores`,
                    data: JSON.stringify(score),
                    dataType: "text",
                    contentType: "application/json"
                })
                    .done(jqXHR => console.log(score))
                    .fail((jqXHR, status) => alert(status + " " + jqXHR.responseText));
            }

        })
        .catch(err => console.log(err))
}

mainGame();

function displayInfo(gamePlayerData) {
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
                                <td tabindex="0" onclick="chooseLocation(this)" onmouseover="mouseChooseLocation(this)" onmouseout="removeMouseChooseLocation(this)" data-classname="${firstcol[j]}1" class=${firstcol[j]}1></td>
                                <td tabindex="0" onclick="chooseLocation(this)" onmouseover="mouseChooseLocation(this)" onmouseout="removeMouseChooseLocation(this)" data-classname="${firstcol[j]}2" class=${firstcol[j]}2></td>
                                <td tabindex="0" onclick="chooseLocation(this)" onmouseover="mouseChooseLocation(this)" onmouseout="removeMouseChooseLocation(this)" data-classname="${firstcol[j]}3" class=${firstcol[j]}3></td>
                                <td tabindex="0" onclick="chooseLocation(this)" onmouseover="mouseChooseLocation(this)" onmouseout="removeMouseChooseLocation(this)" data-classname="${firstcol[j]}4" class=${firstcol[j]}4></td>
                                <td tabindex="0" onclick="chooseLocation(this)" onmouseover="mouseChooseLocation(this)" onmouseout="removeMouseChooseLocation(this)" data-classname="${firstcol[j]}5" class=${firstcol[j]}5></td>
                                <td tabindex="0" onclick="chooseLocation(this)" onmouseover="mouseChooseLocation(this)" onmouseout="removeMouseChooseLocation(this)" data-classname="${firstcol[j]}6" class=${firstcol[j]}6></td>
                                <td tabindex="0" onclick="chooseLocation(this)" onmouseover="mouseChooseLocation(this)" onmouseout="removeMouseChooseLocation(this)" data-classname="${firstcol[j]}7" class=${firstcol[j]}7></td>
                                <td tabindex="0" onclick="chooseLocation(this)" onmouseover="mouseChooseLocation(this)" onmouseout="removeMouseChooseLocation(this)" data-classname="${firstcol[j]}8" class=${firstcol[j]}8></td>
                                <td tabindex="0" onclick="chooseLocation(this)" onmouseover="mouseChooseLocation(this)" onmouseout="removeMouseChooseLocation(this)" data-classname="${firstcol[j]}9" class=${firstcol[j]}9></td>
                                <td tabindex="0" onclick="chooseLocation(this)" onmouseover="mouseChooseLocation(this)" onmouseout="removeMouseChooseLocation(this)" data-classname="${firstcol[j]}10" class=${firstcol[j]}10></td>
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
            document.getElementById("ship-grid").querySelector(`.${shipLocation[i]}`).style.backgroundColor = "blue";
        }

    }

    if (opponentSalvos.length > 0) {
        for (let i = 0; i < opponentSalvos.length; i++) {
            let shots = opponentSalvos[i].turn;
            for (let j = 0; j < opponentSalvos[i].location.length; j++) {
                document.getElementById("ship-grid").querySelector(`.${opponentSalvos[i].location[j]}`).style.backgroundColor = "green";
                document.getElementById("ship-grid").querySelector(`.${opponentSalvos[i].location[j]}`).innerHTML = shots;
                if (shipLocation.includes(opponentSalvos[i].location[j])) {
                    document.getElementById("ship-grid").querySelector(`.${opponentSalvos[i].location[j]}`).style.backgroundColor = "blue";
                    document.getElementById("ship-grid").querySelector(`.${opponentSalvos[i].location[j]}`).style.backgroundImage = "linear-gradient(to bottom right,  transparent calc(50% - 1px), white, transparent calc(50% + 1px))";
                    document.getElementById("ship-grid").querySelector(`.${opponentSalvos[i].location[j]}`).innerHTML = shots;
                }
            }
        }
    }
}

function displaySalvoGrid(salvos) {
    if (salvos.length > 0) {
        for (let i = 0; i < salvos.length; i++) {
            let shots = salvos[i].turn;
            for (let j = 0; j < salvos[i].location.length; j++) {
                document.getElementById("salvo-grid").querySelector(`.${salvos[i].location[j]}`).style.background = "green";
                document.getElementById("salvo-grid").querySelector(`.${salvos[i].location[j]}`).innerHTML = shots;
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
                document.getElementById("header").style.display = "flex";
            } else {
                document.getElementById("header").style.display = "none";
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
var salvoLocation = [];
function chooseLocation(className) {
    shipBow = "";

    if (document.getElementById("ship-grid").contains(className) && className.style.backgroundColor != "blue") {
        shipBow = className.getAttribute("data-classname");
        console.log(className);
        console.log(shipBow);
    }

    if (document.getElementById("salvo-grid").contains(className) && className.style.backgroundColor != "green" && className.style.backgroundColor != "orange" && salvoLocation.length < 5) {
        className.style.backgroundColor = "orange";
        salvoLocation.push(className.getAttribute("data-classname"));
        console.log(salvoLocation);
    } else if (className.style.backgroundColor == "orange") {
        className.style.backgroundColor = "white";
        salvoLocation = salvoLocation.filter(lo => lo != className.getAttribute("data-classname"));
        console.log(salvoLocation)
    }
}

function loopHoverH(className, classCell, cellNum, arrayLength, color, borderColor, borderWidth) {
    for (let i = cellNum; i < arrayLength; i++) {
        let subClass = classCell.charAt(0).concat(i);
        if(document.querySelector(`.${subClass}`).style.backgroundColor != "blue"){
            className.style.backgroundColor = color;
            document.querySelector(`.${subClass}`).style.backgroundColor = color;
        } else {
            document.querySelector(`.${subClass}`).style.borderColor = borderColor;
            document.querySelector(`.${subClass}`).style.borderWidth = borderWidth;
        }
    }

}

function loopHoverV(className, classCell, cellCharNum, arrayLength, color, borderColor, borderWidth) {
    for (let i = cellCharNum; i < arrayLength; i++) {
        let subClass = String.fromCharCode(i).toUpperCase().concat(classCell.slice(1,3));
        if(document.querySelector(`.${subClass}`).style.backgroundColor != "blue"){
            className.style.backgroundColor = color;
            document.querySelector(`.${subClass}`).style.backgroundColor = color;
        } else {
            document.querySelector(`.${subClass}`).style.borderColor = borderColor;
            document.querySelector(`.${subClass}`).style.borderWidth = borderWidth;
        }
    }
}

function countForHover(className, color, warningColor, borderColor, borderWidth) {
    let classCell = className.getAttribute("data-classname");
    if (shipDirecttion == 'horizontal') {
        let cellNum = parseInt(classCell.slice(1,3));
        if (cellNum + shipLength <= 11) {
            loopHoverH(className, classCell, cellNum, (cellNum + shipLength), color, borderColor, borderWidth);
        } else {
            loopHoverH(className, classCell, cellNum, 11, warningColor, borderColor, borderWidth);
        }
    } else if (shipDirecttion == 'vertical'){
        let cellCharNum = classCell.charCodeAt(0);
        if (cellCharNum + shipLength <= 75) {
            loopHoverV(className, classCell, cellCharNum, (cellCharNum + shipLength), color, borderColor, borderWidth);
        } else {
            loopHoverV(className, classCell, cellCharNum, 75, warningColor, borderColor, borderWidth);
        }
    }
}

function mouseChooseLocation(className) {
    if (document.getElementById("ship-grid").contains(className) && className.style.backgroundColor != "blue") {
        console.log(className);
        countForHover(className, "orange", "red", "red", "thick");
    }
}

function removeMouseChooseLocation(className) {
    if (document.getElementById("ship-grid").contains(className) && className.style.backgroundColor != "blue") {
        countForHover(className, "", "", "#dee2e6", "thin");
    }
}

function createShip(gamePlayerId) {
    if (shipChooseType != "" && shipDirecttion != "" && shipBow != "") {
        let location = [];
        if (shipDirecttion == 'horizontal') {
            let num = parseInt(shipBow.slice(1, 3));
            if (num + shipLength > 11) {
                alert("Your ship location is out of grid, please place ship again");
            } else {
                for (let i = 0; i < shipLength; i++) {
                    location.push(shipBow.charAt(0).concat(num + i));
                }
            }
        } else {
            let letter = shipBow.charCodeAt(0);
            if (letter + shipLength > 75) {
                alert("Your ship location is out of grid, please place ship again");
            } else {
                for (let i = 0; i < shipLength; i++) {
                    location.push(String.fromCharCode(letter + i).toUpperCase().concat(shipBow.slice(1, 3)));
                }
            }
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

function addSalvo(gamePlayerId) {
    if (salvoLocation.length != 0) {
        let data = {turnLocation: salvoLocation};
        $.post({
            url: `/api/games/players/${gamePlayerId}/salvos`,
            data: JSON.stringify(data),
            dataType: "text",
            contentType: "application/json"
        })
            .done(jqXHR => window.location.href = `../web/game.html?gp=${gamePlayerId}`)
            .fail((jqXHR, status) => alert(status + " " + jqXHR.responseText))
    } else {
        alert('Please make sure you choose salvo location');
    }

}

function displayTurnHistory(data) {
    let turnData = data.turnHistory;
    console.log(turnData);
    let table = document.getElementById("turn-history");
    let tBody = document.createElement("tbody");
    let row = '';
    for (let i = 0; i < turnData.length; i++) {
        let tem = '';
        let mainPlayerHits = '';
        let opponentHits = '';
        tem += `<td style="vertical-align: middle">${turnData[i].turn}</td>`
        for (let j = 0; j < turnData[i].mainPlayerShipGetHitList.length; j++) {
            mainPlayerHits += `<p>${turnData[i].mainPlayerShipGetHitList[j].shipType} (get hit (${turnData[i].mainPlayerShipGetHitList[j].hitNumber}), sunk(${turnData[i].mainPlayerShipGetHitList[j].sunk}))</p>`;
        }
        if (mainPlayerHits == '') {
            tem += `<td class="text-left">There is no new ship get hit</td>`;
        } else {
            tem += `<td class="text-left">${mainPlayerHits}</td>`;
        }
        tem += `<td style="vertical-align: middle">${turnData[i].mainPlayerShipsLeft}</td>`
        for (let j = 0; j < turnData[i].opponentShipGetHitList.length; j++) {
            opponentHits += `<p>${turnData[i].opponentShipGetHitList[j].shipType} (get hit (${turnData[i].opponentShipGetHitList[j].hitNumber}), sunk(${turnData[i].opponentShipGetHitList[j].sunk}))</p>`;
        }
        if (opponentHits == '') {
            tem += `<td class="text-left">No opponent new ship get hit</td>`;
        } else {
            tem += `<td class="text-left">${opponentHits}</td>`;
        }
        tem += `<td style="vertical-align: middle">${turnData[i].opponentShipsLeft}</td>`
        row += `<tr>${tem}</tr>`;
    }

    if(data.gameOver == false){
        if(data.waitForNextTurn == true){
            row += `<tr>
                <td style="background: #ece9e9; border: none" class="text-left" colspan="5">Game status: Waiting for fire</td>
            </tr>`
        } else {
            row += `<tr>
                <td style="background: #ece9e9; border: none" class="text-left" colspan="5">Game status: Fire</td>
            </tr>`
        }
    } else {
        row += `<tr>
                <td style="background: #ece9e9; border: none" class="text-left" colspan="5">Game status: Game Over</td>
            </tr>`
    }

    table.appendChild(tBody).innerHTML = row;
}