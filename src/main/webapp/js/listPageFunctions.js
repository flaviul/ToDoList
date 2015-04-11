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
            var list_link = document.createElement('a');
            list_link.href = '#';
            list_link.text = lists[i];
            list_link.className = 'to-do-list';
            list_link.onclick = function(){showCurrentListDetails(this.text)};

            to_do_list.appendChild(list_link);
            active_lists.appendChild(to_do_list);
        }
    }
}

function clearNodeContent(node) {
    while (node.hasChildNodes()) {
        node.removeChild(node.firstChild);
    }
}

function addNewList() {
    var list = $('#new-list').val();
    xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function () {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
            $('#message-area').show();
            $('#new-list').val('');
            showActiveLists();
        }
    };
    xmlHttp.open('POST', '/addListServlet', true);
    xmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xmlHttp.send("listName=" + list);
}


function generateTasksHtml(json){
    var current_list = document.getElementById('to-do-list');
    clearNodeContent(current_list);
    if (json.noActiveTasks) {
        $('#empty-list-message').show();
    }
    else {
        $('#empty-list-message').hide();
        var tasks = json.tasks;
        for (var i = 0; i < tasks.length; i++) {
            // <label><input type="checkbox" class="task"/>Task 1</label>
            var list_item = document.createElement('li');
            var label = document.createElement('label');
            var checkbox = document.createElement('input');

            checkbox.type = 'checkbox';
            checkbox.class = 'task';

            label.appendChild(checkbox);
            label.innerHTML = tasks[i];
            list_item.appendChild(label);
            current_list.appendChild(list_item);
        }
    }
}

function showCurrentTasks(parent_list) {
    $.ajax({
        url: './getCurrentTasksServlet',
        dataType: 'json',
        data: {getCurrentTasks: true, parentListName: parent_list},
        method: 'POST'})
        .done(function(response){
            generateTasksHtml(response);
        });
}

function showCurrentListDetails(list_name){
    //$('#current-list-details').find('h2').innerHTML = list_name;
    document.getElementById('current-list-details').getElementsByTagName('h4')[0].innerHTML = list_name;
    showCurrentTasks(list_name);
}


function saveNewTask() {
    var task = $('#new-task').val();
    var list = document.getElementById('current-list-details').getElementsByTagName('h4')[0].innerHTML;
    xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function () {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
            $('#message-area').show();
            $('#new-task').val('');
        }
    };
    xmlHttp.open('POST', '/addItemServlet', true);
    xmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xmlHttp.send("listName=" + list + "&newTask=" + task);
}


showActiveLists();