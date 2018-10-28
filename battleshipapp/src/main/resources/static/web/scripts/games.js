function mainGames() {
    fetch("../api/scores")
        .then(response => response.json())
        .then((data) => {
            let gameData = data;
            console.log(gameData);
            let gamePlayerList = [];
            gameData.forEach(gamedata => {
                if (!gamePlayerList.includes(gamedata.player.email)) {
                    gamePlayerList.push(gamedata.player.email);
                }
            });
            createTable("score-table", gameData, gamePlayerList);
        })
        .catch(err => console.log(err))
}

mainGames();

function createTable(tableId, gameData, gamePlayerList) {
    let table = document.getElementById(tableId);
    let tHead = document.createElement("thead");
    let tHeadContent = '';
    let label = ["Player", "Total", "Won", "Lost", "Tied"];
    for (let i = 0; i < label.length; i++) {
        tHeadContent += `<th>${label[i]}</th>`
    }
    table.appendChild(tHead).innerHTML = tHeadContent;
    let tBody = document.createElement("tbody");
    let tBodyContent = '';
    for (let j = 0; j < gamePlayerList.length; j++) {
        let totalScore = 0, won = 0, lost = 0, tied = 0;
        let playerInfo = gameData.filter(game => game.player.email.includes(gamePlayerList[j]));
        playerInfo.forEach(player => {
            if (player.score != null) {
                totalScore += player.score.score;
                switch (player.score.score) {
                    case 1:
                        won += 1;
                        break;
                    case 0.5:
                        tied += 1;
                        break;
                    case 0:
                        lost += 1;
                        break;
                }
            }
        });
        tBodyContent += `<tr>
                                        <td>${gamePlayerList[j]}</td>
                                        <td>${totalScore}</td>
                                        <td>${won}</td>
                                        <td>${lost}</td>
                                        <td>${tied}</td>
                                     </tr>`;

    }
    table.appendChild(tBody).innerHTML = tBodyContent;
}

function checkLogin() {
    fetch("../api/games")
        .then(response => response.json())
        .then((data) => {
            let userData = data.currentPlayer;
            let games = data.games;
            console.log(data);
            if (userData != null) {
                document.getElementById("playerLoginName").innerText = userData.email;
                document.getElementById("welcome").style.display = "block";
                document.getElementById("table-score").style.display = "block";
                document.getElementById("form-logIn-register").style.display = "none";
                let gameListCurrentUser = [];
                let linkRejoinGame = '';

                for (let i = 0; i < games.length; i++) {
                    games[i].gamePlayers.forEach(gp => {
                        if (gp.player.id == userData.id) {
                            gameListCurrentUser.push(gp);
                        }
                    })
                }
                if(gameListCurrentUser.length>0){
                    document.getElementById("current-game").innerHTML = `<p style="color: #f7ff00">You have ${gameListCurrentUser.length} current games:</p>`;
                    gameListCurrentUser.forEach(gamePlayer=>{
                        linkRejoinGame += `<li><a style="color: #f7ff00" target="_blank" href="../web/game.html?gp=${gamePlayer.id}">Click here to re-enter your game</a></li>`;
                    })
                }
                document.getElementById("link-to-reenter-game").innerHTML = linkRejoinGame;
            } else {
                document.getElementById("welcome").style.display = "none";
                document.getElementById("table-score").style.display = "none";
                document.getElementById("form-logIn-register").style.display = "block";
            }
        })
        .catch(err => alert(err))
}

function login(user, pass) {
    let username = document.getElementById(user);
    let password = document.getElementById(pass);
    $.post("/api/login",
        {
            name: username.value,
            pwd: password.value
        })
        .done(function () {
            window.location.reload();
            console.log("logged in!");
        })
        .fail(err => alert(`${err.responseJSON.status}: ${err.responseJSON.error}`));
}

function logout() {
    $.post("/api/logout")
        .done(function () {
            window.location.href = "../web/games.html";
            console.log("logged out");
        })
        .fail(err => alert(err.responseText));
}

function register() {
    let firstName = document.getElementById("firstNameRegister");
    let lastName = document.getElementById("lastNameRegister");
    let username = document.getElementById("usernameRegister");
    let password = document.getElementById("passwordRegister");
    $.post("/api/players",
        {
            firstname: firstName.value,
            lastname: lastName.value,
            username: username.value,
            password: password.value
        })
        .done(function () {
            login('usernameRegister', 'passwordRegister');
        })
        .fail(err => console.log(err));
}

function addGame() {
    fetch('/api/games', {
        method: 'POST',
        body: JSON.stringify({}),
        headers: new Headers({
            contentType: 'application/json'
        })
    })
        .then(response => response.json())
        .then((data) => {
            window.location.href = `../web/game.html?gp=${data.gamePlayerId}`;
        })
        .catch(error => alert(error.message));

    // $.post("/api/games", {})
    //     .done(response => {
    //         console.log(response);
    //     })
    //     .fail(err=> console.log(err));
}

function joinGame() {
    fetch("../api/games")
        .then(response => response.json())
        .then((data) => {
            let games = data.games;
            let gameIds = games.map(game=> game.id);
            let gameIdRandom = gameIds[Math.floor(Math.random()*gameIds.length)];
            let buttonJoinGame = document.getElementById("joinGame");
            buttonJoinGame.setAttribute('data-gameId',gameIdRandom);
            let gameId = buttonJoinGame.getAttribute('data-gameId');
            fetch(`/api/game/${gameId}/players`, {
                method: 'POST',
                body: JSON.stringify({}),
                headers: new Headers({
                    contentType: 'application/json'
                })
            })
                .then(response => response.json())
                .then((data) => {
                    if(data.gamePlayerId == undefined){
                        alert(data.message);
                    } else {
                    window.location.href = `../web/game.html?gp=${data.gamePlayerId}`;
                    }
                })
                .catch(error => alert(error));
        })
        .catch(err => alert(err))
}

