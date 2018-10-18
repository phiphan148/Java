$(function () {

    // load and display JSON sent by server for /players
    function loadData() {
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
                let table = document.getElementById("score-table");
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
            })
            .catch(err => console.log(err))
    }

    loadData();
});