/**
 * Created by Flaviu Ratiu on 18/04/2015.
 */

function signUp() {
    var username = document.getElementById('new-username').value;
    $.ajax({
        url: './signUpServlet',
        dataType: 'json',
        data: {username: username},
        method: 'POST'
    })
        .done(function (response) {
            window.location = './listPage.html';
        });
}