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
                data.forEach(map => showOutput(JSON.stringify(map, null, 2)));
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

    $("#add_player").on("click", addPlayer);

    loadData();
});