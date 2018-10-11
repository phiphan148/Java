$(function() {

    // display text in the output area
    function showOutput(text) {
        $("#test").append(`<li>${text}</li>`);
        //  document.getElementById("output").innerText(text);
    }

    // load and display JSON sent by server for /players
    function loadData() {
        fetch("../api/games")
            .then(response => response.json())
            .then((data) => {
                let a = data;
                console.log(a);
                console.log(data);
                console.log("jsdh");
                // data.forEach(map=> console.log());
                data.forEach(map => showOutput(new Date(Date.parse(map.created))));

            })
            .catch(err => console.log(err))
    }

    // handler for when user clicks add person
    function addPlayer() {
        var fname = $("#fname").val();
        var lname = $("#lname").val();
        var username = $("#email").val();
        if (fname && lname && username) {
            postPlayer(fname, lname, username);
        }
    }

    // code to post a new player using AJAX
    // on success, reload and display the updated data from the server

    function postPlayer(fname, lname, username) {
        $.post({
            headers: {
                'Content-Type': 'application/json'
            },
            dataType: "text",
            url: "rest/players",
            data: JSON.stringify({"firstname": fname, "lastname": lname, "username": username})
        })
            .done(function( ) {
                showOutput( "Saved -- reloading");
                loadData();
            })
            .fail(function(jqXHR, textStatus ) {
                showOutput( "Failed: " + textStatus );
            });
    }

    function pad(s) { return (s < 10) ? '0' + s : s; }

    $("#add_player").on("click", addPlayer);

    loadData();
});