var myusername = 'anonymous';

/////Sending a message/////

function sendOnEnter() {
	if (event.keyCode == 13) {
		sendMessage();
		return false;
	}
	return true;
}

function sendMessage() {
	var msg = document.getElementById('message').value;
	if(msg !='') {
		printMessage(msg);
		document.getElementById('message').value = '';
	}
}

function printMessage(text) {
	var message = document.createElement('div');
	message.className = 'message';
	text = text.replace(/\n/g, '<br />');
	message.innerHTML = '<table><tr><td style="width:25%"><b>' + myusername + ':</b></td>                      <td width=70% style="word-wrap: break-word" onBlur="makeUneditable()">' + text + '</td>                      <td style="padding-left: 5px">                                            <img src="icons/edit_icon.png" onClick="editMessage()" style="cursor: pointer"></img>                      <img src="icons/delete_icon.png" onClick="deleteMessage()" style="cursor: pointer"</img>                      </td></tr></table>';
	document.getElementById('messages').appendChild(message);
}

///////////////////////////

/////Enter username/////

function enterName() {
	if (event.keyCode == 13) {
		myusername = document.getElementById('username').value;
		return false;
	}
	return true;
}

function clearUsername() {
	document.getElementById('username').value = '';
}

////////////////////////

/////Delete message/////

function deleteMessage() {
	var toDelete = event.target;
	toDelete = toDelete.parentNode.parentNode.parentNode.parentNode.parentNode;
	toDelete.parentNode.removeChild(toDelete);
}

////////////////////////

/////Edit message/////

function editMessage() {
	var toEdit = event.target;
	toEdit = toEdit.parentNode.previousElementSibling;
	toEdit.setAttribute('contenteditable', true);
	toEdit.setAttribute('outline', 'none');
	toEdit.focus();
}

function makeUneditable() {
	var td = event.target;
	td.setAttribute('contenteditable', false);
}
 
//////////////////////