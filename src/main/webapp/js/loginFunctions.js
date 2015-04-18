/**
 * Created by Flaviu Ratiu on 18/04/2015.
 */

function loginUser(){
    var username = document.getElementById('username').value;
    $.ajax({
        url: './loginServlet',
        dataType: 'json',
        data: {username: username},
        method: 'POST'
    })
        .done(function (response) {
            alert(response);
            //if (response) {
            //}
            //else {
            //    // TODO: add create user methods
            //    alert('User does not exist');
            //}
        });
}