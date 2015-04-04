/**
 * Created by Flaviu Ratiu on 04/04/2015.
 */

function saveNewTask() {
    var task = $('#new-task').val();
    xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function () {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
            $('#message-area').show();
        }
    }
    xmlHttp.open('POST', '/servlet', true);
    xmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xmlHttp.send("newTask=" + task);
}
