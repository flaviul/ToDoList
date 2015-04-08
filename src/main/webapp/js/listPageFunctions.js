/**
 * Created by Flaviu Ratiu on 04/04/2015.
 */

// Displays all active to-do lists
function showActiveLists() {
    $.ajax({
        url: './getActiveListsServlet',
        dataType: 'json',
        data: {getActiveLists: true},
        method: 'POST'})
        .done(function(response){
            generateListElements(response);
        });
  }

function generateListElements(json) {
    //var active_lists = $('#active-lists');
    var active_lists = document.getElementById('active-lists');
    clearNodeContent(active_lists);
    if (json.noActiveLists) {
        $('#empty-list-message').show();
    }
    else {
        $('#empty-list-message').hide();
        var lists = json.lists;
        for (var i = 0; i < lists.length; i++) {
            var to_do_list = document.createElement('li');
            to_do_list.innerHTML = lists[i];
            to_do_list.className = 'to-do-list';
            active_lists.appendChild(to_do_list);
        }
    }
}

function clearNodeContent(node) {
    while (node.hasChildNodes()) {
        node.removeChild(node.firstChild);
    }
}


// Navigating to the Add Task page
function newTask() {
    location.href = 'newTask.html'
}

function addNewList() {
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

showActiveLists();