$(function() {

    // display text in the output area
    function showOutput(text) {
        $("#output").text(text);
        //  document.getElementById("output").innerText(text);
    }

    // load and display JSON sent by server for /players
    function loadData() {
        // fetch("rest/players")
        //     .then(response => response.json())
        //     .then((data) => {
        //         showOutput(JSON.stringify(data, null, 2));
        //     })
        //     .catch(err => console.log(err))
        $.get("api/games")
            .done(function(data) {
                showOutput(JSON.stringify(data, null, 2));
            })
            .fail(function(jqXHR, textStatus ) {
                showOutput( "Failed: " + textStatus );
            });
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