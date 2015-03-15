var myusername = 'anonymous';

var uniqueId = function() {
	var date = Date.now();
	var random = Math.random() * Math.random();

	return Math.floor(date * random).toString();
};

var theMessage = function(name, text) {
	return {
		username: name,
		message: text,
		id: uniqueId()
	};
};

var messageList = [];

function run() {
	var appContainer = document.getElementById('wrapper');

	appContainer.addEventListener('click', delegateEvent);
	appContainer.addEventListener('keydown', delegateEvent);
	appContainer.addEventListener('focus', delegateEvent);

	var allMessages = restore();
	createAllMessages(allMessages);
}

function createAllMessages(allMessages) {
	for(var i = 0; i < allMessages.length; i++)
		addMessage(allMessages[i]);
}

function delegateEvent(evtObj) {
	if(evtObj.type === 'click') {
		if(evtObj.target.id === 'submitbutton')
		sendMessage();
		if(evtObj.target.id === 'username')
		clearUsername();
		if(evtObj.target.className === 'editicon')
		editMessage(evtObj.target);
		if(evtObj.target.className === 'deleteicon')
		deleteMessage(evtObj.target);
	}
	if(evtObj.type === 'keydown'
		&& evtObj.keyCode == 13) {
			if(evtObj.target.id == 'message' && !evtObj.shiftKey) {
				sendMessage();
				evtObj.target.blur();
				setTimeout(function(){evtObj.target.focus(); }, 20);
			}
			if(evtObj.target.id == 'username') {
				enterName();
			}
		}
}

/////Dealing with messages/////

function addMessage(msg) {
	var message = createMessage(msg);
	var messages = document.getElementById('messages');
	messageList.push(msg);
	messages.appendChild(message);
	messages.scrollTop = messages.scrollHeight; //move scrollbar to the end of div
}

function createMessage(msg) {
	var message = document.createElement('div');
	var name = msg.username;
	var text = msg.message;
	message.className = 'message';
	message.id = msg.id;
	text = text.replace(/</g, '&lt;');
	text = text.replace(/>/g, '&gt;');
	text = text.replace(/\n/g, '<br />');
	message.innerHTML = '<table><tr><td style="width: auto; min-width: 60px "><b>' + name + ':</b></td><td style="width: 75%; word-wrap: break-word" onBlur="makeUneditable()">' + text + '</td><td width=5% style="padding-left: 5px"><img src="icons/icon_edit.png" class="editicon"></img><img src="icons/icon_delete.png" class="deleteicon"</img></td></tr></table>';
	return message;
}

function sendMessage() {
	var message = document.getElementById('message');
	var msg = message.value;
	if(msg !='') {
		addMessage(theMessage(myusername, msg));
		store(messageList);
		message.value = '';
		message.focus();
	}
}

///////////////////////////

/////Dealing with username/////

function enterName() {
	if (event.keyCode == 13) {
		myusername = document.getElementById('username').value;
		document.getElementById('username').blur();
		return false;
	}
	return true;
}

function clearUsername() {
	document.getElementById('username').value = '';
}

////////////////////////

/////Deleting messages/////

function deleteMessage(evtObj) {
	var toDelete = evtObj;
	toDelete = toDelete.parentNode.parentNode.parentNode.parentNode.parentNode;
	toDelete.parentNode.removeChild(toDelete);
	deleteFromList(toDelete.id);
	store(messageList);
}

function deleteFromList(id) {
	for(var i = 0; i < messageList.length; i++) {
		if(messageList[i].id == id) {
			messageList.splice(i, 1);
			return;
		}
	}
}

////////////////////////

/////Editing messages/////

function editMessage(evtObj) {
	var toEdit = event.target;
	var edit = toEdit.parentNode.parentNode.parentNode.parentNode.parentNode;
	toEdit = toEdit.parentNode.previousElementSibling;
	toEdit.setAttribute('contenteditable', true);
	toEdit.setAttribute('outline', 'none');
	toEdit.focus();
}

function editList(id, txt) {
	for(var i = 0; i < messageList.length; i++) {
		if(messageList[i].id == id) {
			messageList[i].message = txt;
			return;
		}
	}
}

//Make td of a table uneditable after finishing
function makeUneditable() {
	var td = event.target;
	var txt = td.innerText;
	td.setAttribute('contenteditable', false);
	td = td.parentNode.parentNode.parentNode.parentNode;
	editList(td.id, txt);
	store(messageList);
}
 
//////////////////////

/////Storing messages/////

function store(listToSave) {

	if(typeof(Storage) == "undefined") {
		alert('localStorage is not accessible');
		return;
	}

	localStorage.setItem("Messages history", JSON.stringify(listToSave));
}

function restore() {
	if(typeof(Storage) == "undefined") {
		alert('localStorage is not accessible');
		return;
	}

	var item = localStorage.getItem("Messages history");

	return item && JSON.parse(item);
}

//////////////////////////