/**
 * Created by Flaviu Ratiu on 04/04/2015.
 */

// Navigating to the Add Task page
function newTask(){
    location.href = 'newTask.html'
}

function addNewList(){
    var list = $('#new-list').val();
    xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function () {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
            $('#message-area').show();
            $('#new-list').val('');
        }
    };
    xmlHttp.open('POST', '/addListServlet', true);
    xmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xmlHttp.send("listName=" + list);
}