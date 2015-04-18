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

            if (response.userNotFound){
                if (confirm("User not found.\nDo you want to create a user?")){
                    window.location = './signUp.html';
                }
                else {
                    window.location = './login.html';
                }
            }
            else {
                //alert("The requested user's id is: " + response.userId);
                window.location = './listPage.html';
            }
        });
}