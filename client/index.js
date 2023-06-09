const API_URL = "http://localhost:8080/api/agent";

const emptyAgent = {
    agentId: 0,
    firstName: '',
    middleName: '',
    lastName: '',
    dob: null,
    heightInInches: 0,
};

const MESSAGE_STYLES = {
    'SUCCESS': 'alert alert-success',
    'ERROR': 'alert alert-danger',
}


let currentView = 'list';
let agents = [];
let currentAgent = {};

const init = () => {
    refreshList();
};

const handleAdd = () => {
    hideMessages();
    currentAgent = { ...emptyAgent };
    resetForm();
    setView('form');
};

const handleEdit = (id) => {
    hideMessages();
    const index = agents.findIndex(a => a.agentId === id);
    if (index !== -1) {
        currentAgent = agents[index];
        resetForm();
        setView('form');
    }
};

const handleConfirmDelete = (id) => {
    hideMessages();
    const index = agents.findIndex(a => a.agentId === id);
    if (index !== -1) {
        currentAgent = agents[index];
        document.getElementById('deleteTitle').innerText = currentAgent.firstName;
        setView('delete');
    }
};

const handleChange = (evt) => {
    let nextValue = evt.target.value;
    if (evt.target.type === 'number') {
        nextValue = parseInt(nextValue, 10);
        if (isNaN(nextValue)) {
            nextValue = evt.target.valueAsNumber;
        }
    }
    currentAgent[evt.target.name] = nextValue;

};

const handleSaveAgent = (evt) => {
    evt.preventDefault();

    if (currentAgent.agentId === 0) {
        addAgent(currentAgent)
            .then(data => {
                displayMessage(`${data.firstName} was added!`, 'SUCCESS');
                refreshList();
                setView('list');
            })
            .catch(errors => {
                displayMessage(errors, 'ERROR');
            });
    }
    else {
        updateAgent(currentAgent)
            .then(() => {
                displayMessage(`${currentAgent.firstName} was updated!`, 'SUCCESS');
                refreshList();
                setView('list');
            })
            .catch(errors => {
                displayMessage(errors, 'ERROR');
            });
    }
};

const handleDelete = () => {
    deleteAgent(currentAgent)
        .then(() => {
            displayMessage(`${currentAgent.firstName} was deleted!`, 'SUCCESS');
            refreshList();
            setView('list');
        })
        .catch(errors => {
            displayMessage(errors, 'ERROR');
        });

};

const refreshList = () => {
    fetchAgents()
        .then(data => {
            agents = data;
            renderList();
        })
        .catch(err => {
            displayMessage('Could not add agent', 'ERROR');
        });
};



const renderList = () => {
    let htmlString = '<div class="row">';

    for (let agent of agents) {
        const AVATAR_URL = "https://api.dicebear.com/6.x/avataaars/svg?seed=";
        const getRandomPersona = (personaNames) => {
            const randomIndex = Math.floor(Math.random() * personaNames.length);
            return personaNames[randomIndex];
        }

        const randomPersonaName = getRandomPersona(personaNameList);
        const agentHtml = `
        <div class="col">
            <div class="card" style="width: 17rem;">
                    <img class="card-img rounded-circle img-fluid" src="${AVATAR_URL}${randomPersonaName}" alt="persona-avatar">
                    <div class="card-body">
                        <h6 class="main-text">${agent.firstName} ${agent.lastName}</h5>
                        <h7 class="dob-text mb-2"><b>Dob:</b> ${agent.dob}</h7>
                        <br>
                        <div class="icons my-2">
                        <button type="button" title="Edit" class="btn btn-primary btn-sm rounded-pill" onclick="handleEdit(${agent.agentId})"><i class="fa-solid fa-pen-to-square"></i></button>
                        <button type="button" title="Remove" class="btn btn-danger btn-sm rounded-pill" onclick="handleConfirmDelete(${agent.agentId})"><i class="fa-solid fa-trash-can"></i></button>
                        </div>
                    </div>
            </div>   
        </div>
        <br>
        `;
        htmlString += agentHtml;
    }

    htmlString += '</div>';
    document.getElementById('listView').innerHTML = htmlString;

};


const setView = (nextView) => {
    currentView = nextView;
    document.getElementById('formView').style.display = 'none';
    document.getElementById('listView').style.display = 'none';
    document.getElementById('deleteView').style.display = 'none';

    if (currentView === 'form') {
        document.getElementById('formView').style.display = 'block';
    } else if (currentView === 'list') {
        document.getElementById('listView').style.display = 'block';
    } else {
        document.getElementById('deleteView').style.display = 'block';
    }
};

const resetForm = () => {
    document.getElementById('firstName').value = currentAgent.firstName;
    document.getElementById('middleName').value = currentAgent.middleName;
    document.getElementById('lastName').value = currentAgent.lastName;
    document.getElementById('dob').value = currentAgent.dob;
    document.getElementById('heightInInches').value = currentAgent.heightInInches;
};

const displayMessage = (message, messageStyle) => {
    const messagesDisplay = document.getElementById('messages');
    messagesDisplay.className = MESSAGE_STYLES[messageStyle];
    const messages = Array.isArray(message) ? message : [message];
    let messageHtml = '<ul>';
    for (let msg of messages) {
        messageHtml += `<li>${msg}</li>`;
    }
    messageHtml += '</ul>';
    messagesDisplay.innerHTML = messageHtml;
    messagesDisplay.style.display = 'block';
};

const hideMessages = () => {
    const messages = document.getElementById('messages');
    messages.style.display = 'none';
};

const fetchAgents = async () => {
    const response = await fetch(API_URL);

    if (response.status === 200) {
        return await response.json();
    }
}

const addAgent = async (agent) => {
    const init = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(agent)
    };

    const response = await fetch(API_URL, init);

    if (response.status === 201) {
        return await response.json();
    } else {
        const errors = await response.json();
        return Promise.reject(errors);
    }
};

const updateAgent = async (agent) => {
    const init = {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(agent)
    };

    const url = `${API_URL}/${agent.agentId}`;

    const response = await fetch(url, init);

    if (response.status === 404) {
        Promise.reject(`${agent.firstName} was not found!`);
    } else if (response.status !== 204) {
        const errors = await response.json();
        Promise.reject(errors);
    }
};

const deleteAgent = async (agent) => {
    const init = {
        method: 'DELETE',
    };

    const response = await fetch(`${API_URL}/${agent.agentId}`, init);

    if (response.status === 404) {
        Promise.reject(`${agent.firstName} was not found!`);
    }
};

init();