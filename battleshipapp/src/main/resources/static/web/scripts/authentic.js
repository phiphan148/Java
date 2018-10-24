function checkLogin() {
    fetch("../api/games")
        .then(response => response.json())
        .then((data)=>{
            let userData = data.currentPlayer;
            if(userData != null){
                document.getElementById("playerLoginName").innerText = userData.email;
                document.getElementById("welcome").style.display = "block";
            } else {
                document.getElementById("welcome").style.display = "none";
            }
            })
        .catch(err=>alert(err))
}
checkLogin();

function login() {
    // evt.preventDefault();
    let username = document.getElementById("usernameLogin");
    let password = document.getElementById("passwordLogin");
    $.post("/api/login",
        {
            name: username.value,
            pwd: password.value
        })
        .done(function () {
            window.location.reload();
            console.log("logged in!");
        })
        .fail();
    // $.post("/api/login", { name: "phi@gmail.com", pwd: "12345678" }).done(function() { console.log("logged in!"); }).fail();
}

function logout() {
    $.post("/api/logout")
        .done(function() {
            window.location.href = "../web/games.html";
            console.log("logged out");
        })
        .fail();
}

function register() {
    // evt.preventDefault();
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
        // .done(response=>location.reload())
        .done(function () {
            window.location.reload();
            console.log("Register!");
        })
        .fail(err=> alert(err.responseText));
}
